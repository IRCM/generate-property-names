![Java CI](https://github.com/IRCM/generate-property-names/workflows/Java%20CI/badge.svg?branch=master)

# Property names annotation processor.

## Class produced.

Creates a metadata class containing property names of annotated classes as public static fields.

So, if you have a class like this.
```
package com.mypackage;

@GeneratePropertyNames
public class MyClass {
  private myProperty;
  private mySecondProperty;
  // Getters / setter.
}
```

This class will be generated.
```
package com.mypackage;

public class MyClassProperties {
  public static final String myProperty = "myProperty";
  public static final String mySecondProperty = "mySecondProperty";
}
```

## Supported annotations.

- ca.qc.ircm.processing.GeneratePropertyNames
