## Entity: Enum Constant

An `Enum Constant` is a set of predefined constants defined in enum.

### Supported Patterns

```yaml
name: Enum Constant
```
#### Syntax: Enum Constant Definitions

```text
EnumConstantDeclaration:
     [ Javadoc ] { ExtendedModifier } Identifier
         [ ( [ Expression { , Expression } ] ) ]
         [ AnonymousClassDeclaration ]
```

##### Examples

###### Simple enum constant declaration

```java
enum Foo {
    BAR
}
```

```yaml
name: Simple Enum Constant
entity:
    type: Enum Constant
    items:
        -   name: BAR
            qualified: Foo.BAR
            loc: 2:5
```

###### Enum with methods

```java
enum Planet {
    MERCURY (3.303e+23, 2.4397e6),              // <--- Enum constant
    VENUS   (4.869e+24, 6.0518e6);              // <--- Enum constant

    private final double mass;
    private final double radius;
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    private double mass() { return mass; }
    private double radius() { return radius; }

    public static final double G = 6.67300E-11; // <--- NOT enum constant
}
```

```yaml
name: Enum Constant With Methods
entity:
    type: Enum Constant
    items:
        -   name: MERCURY
            qualified: Planet.MERCURY
            loc: 2:5
        -   name: VENUS
            qualified: Planet.VENUS
            loc: 3:5
```
