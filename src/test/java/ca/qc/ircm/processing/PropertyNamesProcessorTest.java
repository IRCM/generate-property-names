/*
 * The MIT License
 * Copyright (c) 2018 Institut de recherches cliniques de Montreal (IRCM)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
