package ca.qc.ircm.processing;

@SuppressWarnings("checkstyle:all")
@GeneratePropertyNames(capitalize=false)
public class FirstClass {
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
