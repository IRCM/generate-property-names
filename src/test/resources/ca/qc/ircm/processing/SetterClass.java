package ca.qc.ircm.processing;

@SuppressWarnings("checkstyle:all")
@GeneratePropertyNames(requirements=GeneratePropertyRequirements.SETTER)
public class SetterClass {
  private String getterAndSetter;
  private String getterOnly;
  private String setterOnly;
  private String noGetterOrSetter;

  public String getGetterAndSetter() {
    return getterAndSetter;
  }

  public void setGetterAndSetter(String getterAndSetter) {
    this.getterAndSetter = getterAndSetter;
  }

  public String getGetterOnly() {
    return getterOnly;
  }

  public void setSetterOnly(String setterOnly) {
    this.setterOnly = setterOnly;
  }
}
