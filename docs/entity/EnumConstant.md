# Entity: Enum Constant

An `enum constant` is a set of predefined constants defined in enum.

## Supported pattern

```yaml
name: Enum Constant
```
### Syntax: Enum Constant Definitions

```text
EnumConstantDeclaration:
     [ Javadoc ] { ExtendedModifier } Identifier
         [ ( [ Expression { , Expression } ] ) ]
         [ AnonymousClassDeclaration ]
```

#### Examples:

* Simple enum constant declaration

```java
enum Foo {
    BAR
}
```

```yaml
name: Simple Enum Constant
entity:
    filter: Enum Constant
    r:
        d: Var
        e: .
        s: .
        u: .
    items:
        -   name: BAR
            qualifiedName: Foo.BAR
```

* Enum with methods

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
    filter: Enum Constant
    exact: true
    r:
        d: Var
        e: .
        s: .
        u: .
    items:
        -   name: MERCURY
            qualifiedName: Planet.MERCURY
        -   name: VENUS
            qualifiedName: Planet.VENUS
```
