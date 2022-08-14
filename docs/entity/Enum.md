## Entity: Enum

An `enum type` is a special data type that enables for a variable to be a set of predefined constants. The variable must be equal to one of the values that have been predefined for it.

### Supported Patterns

```yaml
name: Enum
```

#### Syntax: Enum Definitions

```text
EnumDeclaration:
     [ Javadoc ] { ExtendedModifier } enum Identifier
         [ implements Type { , Type } ]
         {
         [ EnumConstantDeclaration { , EnumConstantDeclaration } ] [ , ]
         [ ; { ClassBodyDeclaration | ; } ]
         }
```

##### Examples

###### Enum declared in default package

```java
enum Foo {
    /* ... */
}
```

```yaml
name: Enum Declared In Default Package
entity:
    type: Enum
    items:
        -   name: Foo
            qualified: Foo
            loc: 1:6
```

###### Enum declared in named package

```java
package foo;

enum Bar {
    /* ... */
}
```

```yaml
name: Enum Declared In Named Package
entity:
    type: Enum
    items:
        -   name: Bar
            qualified: foo.Bar
            loc: 3:6
```
