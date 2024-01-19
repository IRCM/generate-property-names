package ca.qc.ircm.processing;

/**
 * Requirements to generate property names.
 */
public enum GeneratePropertyRequirements {
  /**
   * Property name is generated only if a getter and a setter is present for property.
   */
  GETTER_AND_SETTER,
  /**
   * Property name is generated only if a getter is present for property.
   */
  GETTER,
  /**
   * Property name is generated only if a setter is present for property.
   */
  SETTER,
  /**
   * Property name is generated if a getter or a setter is present for property.
   */
  GETTER_OR_SETTER
}
