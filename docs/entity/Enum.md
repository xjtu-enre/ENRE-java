# Entity: Enum

An `enum type` is a special data type that enables for a variable to be a set of predefined constants. The variable must be equal to one of the values that have been predefined for it.

## Supported pattern

```yaml
name: Enum
```

### Syntax: Enum Definitions

```text
EnumDeclaration:
     [ Javadoc ] { ExtendedModifier } enum Identifier
         [ implements Type { , Type } ]
         {
         [ EnumConstantDeclaration { , EnumConstantDeclaration } ] [ , ]
         [ ; { ClassBodyDeclaration | ; } ]
         }
```

#### Examples:

* Enum declared in default package

```java
enum Foo {
    /* ... */
}
```

```yaml
name: Enum Declared In Default Package
entity:
    filter: Enum
    r:
        d: Type
        e: .
        s: .
        u: .
    items:
        -   name: Foo
            qualifiedName: Foo
```

* Enum declared in named package

```java
package foo;

enum Bar {
    /* ... */
}
```

```yaml
name: Enum Declared In Named Package
entity:
    filter: Enum
    r:
        d: Type
        e: .
        s: .
        u: .
    items:
        -   name: Bar
            qualifiedName: foo.Bar
```
