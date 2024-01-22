package ca.qc.ircm.processing;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

public class PropertyNamesProcessorTest {
  @Test
  public void firstClass() {
    Compilation compilation = javac().withProcessors(new PropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/FirstClass.java"));
    assertThat(compilation).succeeded();
    assertThat(compilation).generatedSourceFile("ca.qc.ircm.processing.FirstClassProperties")
        .hasSourceEquivalentTo(
            JavaFileObjects.forResource("ca/qc/ircm/processing/FirstClassProperties.java"));
  }

  @Test
  public void capitalizeClass() {
    Compilation compilation = javac().withProcessors(new PropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("ca/qc/ircm/processing/CapitalizeClass.java"));
    assertThat(compilation).succeeded();
    assertThat(compilation).generatedSourceFile("ca.qc.ircm.processing.CapitalizeClassProperties")
        .hasSourceEquivalentTo(
            JavaFileObjects.forResource("ca/qc/ircm/processing/CapitalizeClassProperties.java"));
  }

  @Test
  public void secondClass() {
    Compilation compilation = javac().withProcessors(new PropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("SecondClass.java"));
    assertThat(compilation).succeeded();
    assertThat(compilation).generatedSourceFile("SecondClassProperties")
        .hasSourceEquivalentTo(JavaFileObjects.forResource("SecondClassProperties.java"));
  }
}
