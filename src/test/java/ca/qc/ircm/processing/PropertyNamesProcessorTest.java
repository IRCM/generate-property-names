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
  public void secondClass() {
    Compilation compilation = javac().withProcessors(new PropertyNamesProcessor())
        .compile(JavaFileObjects.forResource("SecondClass.java"));
    assertThat(compilation).succeeded();
    assertThat(compilation).generatedSourceFile("SecondClassProperties")
        .hasSourceEquivalentTo(JavaFileObjects.forResource("SecondClassProperties.java"));
  }
}
