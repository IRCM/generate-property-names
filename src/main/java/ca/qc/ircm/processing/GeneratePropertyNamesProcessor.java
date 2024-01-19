package ca.qc.ircm.processing;

import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Creates a class naming all fields of annotated classes.
 */
@SupportedAnnotationTypes("ca.qc.ircm.processing.GeneratePropertyNames")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class GeneratePropertyNamesProcessor extends AbstractProcessor {
  /**
   * Name of this class.
   */
  private static final String CLASSNAME = GeneratePropertyNamesProcessor.class.getName();
  /**
   * Value to be used in the Generated annotation of generated class.
   */
  private static final String GENERATED_VALUE = CLASSNAME;
  /**
   * Name of generated class.
   *
   * <code>{0}</code> will be replaced by the name of the class being processed.
   */
  private static final String GENERATE_CLASSNAME = "{0}Properties";
  /**
   * Pattern of a getter method's name.
   *
   * <code>{0}</code> will be replaced by the name of the class being processed.
   */
  private static final String GETTER_METHOD_NAME_LOWERCASE_PATTERN = "is{0}|get{0}";
  /**
   * Pattern of a setter method's name.
   *
   * <code>{0}</code> will be replaced by the name of the class being processed.
   */
  private static final String SETTER_METHOD_NAME_LOWERCASE_PATTERN = "set{0}";
  /**
   * Logger.
   */
  private static final Logger logger = Logger.getLogger(CLASSNAME);

  /**
   * Generates a properties name class for all classes annotated with {@link GeneratePropertyNames}.
   *
   * @param annotations
   *          the annotation types requested to be processed
   * @param roundEnv
   *          environment for information about the current and prior round
   * @return true if all classes were generated, false if an exception occurs
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<TypeElement> annotatedClasses = new HashSet<>();
    for (TypeElement annotation : annotations) {
      Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
      annotatedClasses.addAll(
          annotatedElements.stream().filter(element -> element.getKind() == ElementKind.CLASS)
              .map(element -> (TypeElement) element).collect(Collectors.toSet()));
    }
    for (TypeElement clazz : annotatedClasses) {
      try {
        createPropertyNamesClass(clazz);
      } catch (IOException e) {
        logger.warning("Could not create fields class for class " + clazz.getSimpleName()
            + ", error " + e.getMessage());
        return false;
      }
    }
    return true;
  }

  /**
   * Generate a class that contains all the names of properties found.
   *
   * @param clazz
   *          original class for which to generate a property names class
   * @throws IOException
   *           could not write the generated class
   */
  private void createPropertyNamesClass(TypeElement clazz) throws IOException {
    GeneratePropertyNames generatePropertyNames = clazz.getAnnotation(GeneratePropertyNames.class);
    PackageElement packageElement = (PackageElement) clazz.getEnclosingElement();
    final String packageName =
        packageElement != null ? packageElement.getQualifiedName().toString() : null;
    final String className = MessageFormat.format(GENERATE_CLASSNAME, clazz.getSimpleName());
    final String qualifiedClassName =
        (packageName != null && !packageName.isEmpty() ? packageName + "." : "") + className;
    logger.info("Generating class " + qualifiedClassName + " for " + clazz.getQualifiedName());
    List<Element> fields = clazz.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.FIELD).collect(Collectors.toList());
    List<Element> methods = clazz.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.METHOD).collect(Collectors.toList());
    List<Element> properties = fields.stream()
        .filter(field -> isProperty(field, methods, generatePropertyNames.requirements()))
        .collect(Collectors.toList());
    JavaFileObject fieldsDefinitionFile =
        processingEnv.getFiler().createSourceFile(qualifiedClassName, clazz);
    try (PrintWriter out = new PrintWriter(fieldsDefinitionFile.openWriter())) {
      if (packageName != null && !packageName.isEmpty()) {
        out.print("package ");
        out.print(packageName);
        out.println(";");
        out.println();
      }
      out.println("import javax.annotation.processing.Generated;");
      out.println();
      out.println("/**");
      out.print(" * Name of properties of class {@link ");
      out.print(clazz.getSimpleName());
      out.println("}.");
      out.println(" */");
      out.print("@Generated(\"");
      out.print(GENERATED_VALUE);
      out.println("\")");
      out.print("public class ");
      out.print(className);
      out.println(" {");
      out.println();
      for (Element property : properties) {
        out.println("  /**");
        out.print("   * {@link ");
        out.print(clazz.getSimpleName());
        out.print("#");
        out.print(property.getSimpleName());
        out.println("} property's name.");
        out.println("   */");
        out.print("  public static final String ");
        if (generatePropertyNames.capitalize()) {
          out.print(staticFieldName(property.getSimpleName().toString()));
        } else {
          out.print(property.getSimpleName());
        }
        out.print(" = \"");
        out.print(property.getSimpleName());
        out.println("\";");
      }
      out.println("}");
      out.println();
    }
  }

  /**
   * Returns true if field is considered a property base on the requirements, false otherwise.
   *
   * @param field
   *          field
   * @param methods
   *          all methods of class
   * @param requirements
   *          how to determine if a field is a property
   * @return true if field is considered a property base on the requirements, false otherwise
   */
  private boolean isProperty(Element field, Collection<? extends Element> methods,
      GeneratePropertyRequirements requirements) {
    switch (requirements) {
      case GETTER:
        return hasGetter(field, methods);
      case SETTER:
        return hasSetter(field, methods);
      case GETTER_OR_SETTER:
        return hasGetter(field, methods) || hasSetter(field, methods);
      case GETTER_AND_SETTER:
      default:
        return hasGetter(field, methods) && hasSetter(field, methods);
    }
  }

  /**
   * Returns true if field has a getter method, false otherwise.
   *
   * @param field
   *          field
   * @param methods
   *          all methods of the class
   * @return true if field has a getter method, false otherwise
   */
  private boolean hasGetter(Element field, Collection<? extends Element> methods) {
    Pattern getterPatern =
        Pattern.compile(MessageFormat.format(GETTER_METHOD_NAME_LOWERCASE_PATTERN,
            field.getSimpleName().toString().toLowerCase()));
    return methods.stream().anyMatch(
        method -> getterPatern.matcher(method.getSimpleName().toString().toLowerCase()).matches());
  }

  /**
   * Returns true if field has a setter method, false otherwise.
   *
   * @param field
   *          field
   * @param methods
   *          all methods of the class
   * @return true if field has a setter method, false otherwise
   */
  private boolean hasSetter(Element field, Collection<? extends Element> methods) {
    Pattern setterPatern =
        Pattern.compile(MessageFormat.format(SETTER_METHOD_NAME_LOWERCASE_PATTERN,
            field.getSimpleName().toString().toLowerCase()));
    return methods.stream().anyMatch(
        method -> setterPatern.matcher(method.getSimpleName().toString().toLowerCase()).matches());
  }

  /**
   * Returns capitalized name of field.
   *
   * <p>
   * This method will capitalize the name of field and add underscores before each upper case
   * letters following a lower case letter. <br>
   * For example, a property named <code>myProperty</code> or <code>myPROPERTY</code> will be
   * converted to <code>MY_PROPERTY</code>.
   * </p>
   *
   * @param instanceFieldName
   *          name of field
   * @return capitalized name of field
   */
  private String staticFieldName(String instanceFieldName) {
    StringBuilder builder = new StringBuilder();
    char[] chars = instanceFieldName.toCharArray();
    builder.append(Character.toUpperCase(chars[0]));
    for (int i = 1; i < chars.length; i++) {
      char ch = chars[i];
      if (Character.isUpperCase(ch)) {
        if (!Character.isUpperCase(chars[i - 1])
            || (i + 1 < chars.length && Character.isLowerCase(chars[i + 1]))) {
          builder.append('_');
        }
        builder.append(ch);
      } else {
        builder.append(Character.toUpperCase(ch));
      }
    }
    return builder.toString();
  }
}
