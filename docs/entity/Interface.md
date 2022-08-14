## Entity: Interface

An `interface` is a contract between a class and the outside world. When a class implements an interface, it promises to provide the behavior published by that interface.

### Supported Patterns

```yaml
name: Interface
```

#### Syntax: Interface Definitions

```text
Interface Declaration:
      [ Javadoc ] { ExtendedModifier } interface Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { InterfaceBodyDeclaration | ; } }
```

##### Examples

###### Interface declaration

```java
interface Foo {
    /* ... */
}

```

```yaml
name: Interface Declaration
entity:
    type: Interface
    items:
        -   name: Foo
            qualified: Foo
            loc: 1:11
```

###### Interface as class body

```java
class Foo {
    interface Bar {
        void baz();
    }
}

```
```yaml
name: Interface As Class Body
entity:
    type: Interface
    items:
        -   name: Bar
            qualified: Foo.Bar
            loc: 2:15
```