package ca.qc.ircm.processing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to generate property names class.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface GeneratePropertyNames {
  /**
   * True to capitalize property names, false to keep property names as is.
   *
   * @return true to capitalize property names, false to keep property names as is
   */
  boolean capitalize() default true;
}
