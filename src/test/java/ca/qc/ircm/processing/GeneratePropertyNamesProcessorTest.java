package ca.qc.ircm.processing;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.google.common.truth.StringSubject;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjectSubject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

public class GeneratePropertyNamesProcessorTest {
  @Test
  public void firstClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/FirstClass.java"));
    assertThat(compilation).succeeded();
    assertThat(compilation).generatedSourceFile("ca.qc.ircm.processing.FirstClassProperties")
        .hasSourceEquivalentTo(
            JavaFileObjects.forResource("ca/qc/ircm/processing/FirstClassProperties.java"));
    StringSubject generateSourceString = assertThat(compilation)
        .generatedSourceFile("ca.qc.ircm.processing.FirstClassProperties").contentsAsUtf8String();
    generateSourceString.contains("/**\n * Name of properties of class {@link FirstClass}.\n */");
    generateSourceString.contains("  /**\n   * {@link FirstClass#id} property's name.\n   */");
    generateSourceString.contains("  /**\n   * {@link FirstClass#name} property's name.\n   */");
    generateSourceString.contains("  /**\n   * {@link FirstClass#valid} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link FirstClass#camelCaseProperty} property's name.\n   */");
    generateSourceString.contains("  /**\n   * {@link FirstClass#UPPER} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link FirstClass#camelMANYUpperCases} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link FirstClass#FirstUpperCase} property's name.\n   */");
    generateSourceString.doesNotContain("nonProperty");
    generateSourceString.doesNotContain("onlyGetter");
    generateSourceString.doesNotContain("onlySetter");
  }

  @Test
  public void capitalizeClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/CapitalizeClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource = assertThat(compilation)
        .generatedSourceFile("ca.qc.ircm.processing.CapitalizeClassProperties");
    generatedSource.hasSourceEquivalentTo(
        JavaFileObjects.forResource("ca/qc/ircm/processing/CapitalizeClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString
        .contains("/**\n * Name of properties of class {@link CapitalizeClass}.\n */");
    generateSourceString.contains("  /**\n   * {@link CapitalizeClass#id} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link CapitalizeClass#name} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link CapitalizeClass#valid} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link CapitalizeClass#camelCaseProperty} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link CapitalizeClass#UPPER} property's name.\n   */");
    generateSourceString.contains(
        "  /**\n   * {@link CapitalizeClass#camelMANYUpperCases} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link CapitalizeClass#FirstUpperCase} property's name.\n   */");
    generateSourceString.doesNotContain("nonProperty");
    generateSourceString.doesNotContain("onlyGetter");
    generateSourceString.doesNotContain("onlySetter");
  }

  @Test
  public void secondClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("SecondClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource =
        assertThat(compilation).generatedSourceFile("SecondClassProperties");
    generatedSource
        .hasSourceEquivalentTo(JavaFileObjects.forResource("SecondClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString.contains("/**\n * Name of properties of class {@link SecondClass}.\n */");
    generateSourceString.contains("  /**\n   * {@link SecondClass#id} property's name.\n   */");
    generateSourceString.contains("  /**\n   * {@link SecondClass#name} property's name.\n   */");
  }

  @Test
  public void getterAndSetterClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/GetterAndSetterClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource = assertThat(compilation)
        .generatedSourceFile("ca/qc/ircm/processing/GetterAndSetterClassProperties");
    generatedSource.hasSourceEquivalentTo(
        JavaFileObjects.forResource("ca/qc/ircm/processing/GetterAndSetterClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString
        .contains("/**\n * Name of properties of class {@link GetterAndSetterClass}.\n */");
    generateSourceString.contains(
        "  /**\n   * {@link GetterAndSetterClass#getterAndSetter} property's name.\n   */");
    generateSourceString.doesNotContain("getterOnly");
    generateSourceString.doesNotContain("setterOnly");
    generateSourceString.doesNotContain("noGetterOrSetter");
  }

  @Test
  public void getterClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/GetterClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource =
        assertThat(compilation).generatedSourceFile("ca/qc/ircm/processing/GetterClassProperties");
    generatedSource.hasSourceEquivalentTo(
        JavaFileObjects.forResource("ca/qc/ircm/processing/GetterClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString.contains("/**\n * Name of properties of class {@link GetterClass}.\n */");
    generateSourceString
        .contains("  /**\n   * {@link GetterClass#getterAndSetter} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link GetterClass#getterOnly} property's name.\n   */");
    generateSourceString.doesNotContain("setterOnly");
    generateSourceString.doesNotContain("noGetterOrSetter");
  }

  @Test
  public void setterClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/SetterClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource =
        assertThat(compilation).generatedSourceFile("ca/qc/ircm/processing/SetterClassProperties");
    generatedSource.hasSourceEquivalentTo(
        JavaFileObjects.forResource("ca/qc/ircm/processing/SetterClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString.contains("/**\n * Name of properties of class {@link SetterClass}.\n */");
    generateSourceString
        .contains("  /**\n   * {@link SetterClass#getterAndSetter} property's name.\n   */");
    generateSourceString.doesNotContain("getterOnly");
    generateSourceString
        .contains("  /**\n   * {@link SetterClass#setterOnly} property's name.\n   */");
    generateSourceString.doesNotContain("noGetterOrSetter");
  }

  @Test
  public void getterOrSetterClass() {
    Compilation compilation = javac().withProcessors(new GeneratePropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/GetterOrSetterClass.java"));
    assertThat(compilation).succeeded();
    JavaFileObjectSubject generatedSource = assertThat(compilation)
        .generatedSourceFile("ca/qc/ircm/processing/GetterOrSetterClassProperties");
    generatedSource.hasSourceEquivalentTo(
        JavaFileObjects.forResource("ca/qc/ircm/processing/GetterOrSetterClassProperties.java"));
    StringSubject generateSourceString = generatedSource.contentsAsUtf8String();
    generateSourceString
        .contains("/**\n * Name of properties of class {@link GetterOrSetterClass}.\n */");
    generateSourceString.contains(
        "  /**\n   * {@link GetterOrSetterClass#getterAndSetter} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link GetterOrSetterClass#getterOnly} property's name.\n   */");
    generateSourceString
        .contains("  /**\n   * {@link GetterOrSetterClass#setterOnly} property's name.\n   */");
    generateSourceString.doesNotContain("noGetterOrSetter");
  }
}
