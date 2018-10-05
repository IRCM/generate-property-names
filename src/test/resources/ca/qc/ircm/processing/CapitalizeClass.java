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

@SuppressWarnings("checkstyle:all")
@GeneratePropertyNames
public class CapitalizeClass {
  private Long id;
  private String name;
  private boolean valid;
  private String camelCaseProperty;
  private String UPPER;
  private String camelMANYUpperCases;
  private String FirstUpperCase;
  @SuppressWarnings("unused")
  private String nonProperty;
  private String onlyGetter;
  @SuppressWarnings("unused")
  private String onlySetter;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }
  
  public String getCamelCaseProperty() {
    return camelCaseProperty;
  }

  public void setCamelCaseProperty(String camelCaseProperty) {
    this.camelCaseProperty = camelCaseProperty;
  }
  
  public String getUPPER() {
    return UPPER;
  }

  public void setUPPER(String UPPER) {
    this.UPPER = UPPER;
  }
  
  public String getCamelMANYUpperCases() {
    return camelMANYUpperCases;
  }

  public void setCamelMANYUpperCases(String camelMANYUpperCases) {
    this.camelMANYUpperCases = camelMANYUpperCases;
  }
  
  public String getFirstUpperCase() {
    return FirstUpperCase;
  }

  public void setFirstUpperCase(String FirstUpperCase) {
    this.FirstUpperCase = FirstUpperCase;
  }

  public String getOnlyGetter() {
    return onlyGetter;
  }

  public void setOnlySetter(String onlySetter) {
    this.onlySetter = onlySetter;
  }
}
