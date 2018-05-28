/*
 * Copyright (c) 2018 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.qc.ircm.processing;

import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@SupportedAnnotationTypes({ "ca.qc.ircm.processing.GeneratePropertyNames",
    "javax.persistence.Entity", "org.springframework.data.mongodb.core.mapping.Document" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class PropertyNamesProcessor extends AbstractProcessor {
  private static final String GENERATED_VALUE = "PropertyNamesProcessor";
  private static final String GENERATE_CLASSNAME = "{0}Properties";
  private static final String CLASSNAME = PropertyNamesProcessor.class.getName();
  private static final String GETTER_METHOD_NAME_LOWERCASE_PATTERN = "is{0}|get{0}";
  private static final String SETTER_METHOD_NAME_LOWERCASE_PATTERN = "set{0}";
  private static final Logger logger = Logger.getLogger(PropertyNamesProcessor.class.getName());

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
        logger.throwing(CLASSNAME, "process", e);
        throw new IllegalStateException(
            "Could not create fields class for class " + clazz.getSimpleName(), e);
      }
    }
    return true;
  }

  private void createPropertyNamesClass(TypeElement clazz) throws IOException {
    PackageElement packageElement = (PackageElement) clazz.getEnclosingElement();
    final String packageName =
        packageElement != null ? packageElement.getQualifiedName().toString() : null;
    final String className = MessageFormat.format(GENERATE_CLASSNAME, clazz.getSimpleName());
    final String qualifiedClassName = (packageName != null ? packageName + "." : "") + className;
    logger.info("Generating class " + qualifiedClassName + " for " + clazz.getQualifiedName());
    List<Element> fields = clazz.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.FIELD).collect(Collectors.toList());
    List<Element> methods = clazz.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.METHOD).collect(Collectors.toList());
    Set<Element> properties =
        fields.stream().filter(field -> isProperty(field, methods)).collect(Collectors.toSet());
    JavaFileObject fieldsDefinitionFile =
        processingEnv.getFiler().createSourceFile(qualifiedClassName, clazz);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    try (PrintWriter out = new PrintWriter(fieldsDefinitionFile.openWriter())) {
      if (packageName != null) {
        out.print("package ");
        out.print(packageName);
        out.println(";");
        out.println();
      }
      out.println("import javax.annotation.Generated;");
      out.println();
      out.print("@Generated(value=\"");
      out.print(GENERATED_VALUE);
      out.print("\", date=\"");
      out.print(dateTimeFormatter.format(LocalDateTime.now()));
      out.println("\")");
      out.print("public class ");
      out.print(className);
      out.println(" {");
      out.println();
      for (Element property : properties) {
        out.print("  public static final String ");
        out.print(property.getSimpleName());
        out.print(" = \"");
        out.print(property.getSimpleName());
        out.println("\";");
      }
      out.println("}");
      out.println();
    }
  }

  private boolean isProperty(Element field, Collection<? extends Element> methods) {
    return hasGetter(field, methods) && hasSetter(field, methods);
  }

  private boolean hasGetter(Element field, Collection<? extends Element> methods) {
    Pattern getterPatern =
        Pattern.compile(MessageFormat.format(GETTER_METHOD_NAME_LOWERCASE_PATTERN,
            field.getSimpleName().toString().toLowerCase()));
    return methods.stream().filter(
        method -> getterPatern.matcher(method.getSimpleName().toString().toLowerCase()).matches())
        .findAny().isPresent();
  }

  private boolean hasSetter(Element field, Collection<? extends Element> methods) {
    Pattern setterPatern =
        Pattern.compile(MessageFormat.format(SETTER_METHOD_NAME_LOWERCASE_PATTERN,
            field.getSimpleName().toString().toLowerCase()));
    return methods.stream().filter(
        method -> setterPatern.matcher(method.getSimpleName().toString().toLowerCase()).matches())
        .findAny().isPresent();
  }
}
