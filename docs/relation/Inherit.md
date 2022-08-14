## Dependency: Inherit

A class extends one super (abstract) class, or an interface extends single or multiple super interfaces.

### Supported Patterns

```yaml
name: Inherit
```

#### Syntax: Inherit Definitions

```text
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```

##### Examples

###### Class extends one super class

```java
//// Foo.java
class Bar {
    /* ... */
}

public class Foo extends Bar {
    /* ... */
}
```

```yaml
name: Class Extends Class
entity:
    items:
        -   name: Foo
            type : Class
            qualified: Foo
            loc: 5:14
            modifiers: public
        -   name: Bar
            type : Class
            qualified: Bar
            loc: 1:7
relation:
    items:
        -   from: Class:'Foo'
            to: Class:'Bar'
            type: Inherit
            loc: file0:5:26
```

###### Class extends one parameterized type

```java
//// Foo.java
class Bar<K, V> {
    /* ... */
}

public class Foo extends Bar<String, Integer> {
    /* ... */
}
```

```yaml
name: Class Extends Class With Generics
entity:
    items:
        -   name: Foo
            type : Class
            qualified: Foo
            loc: 5:14
        -   name: Bar
            type : Class
            qualified: Bar
            loc: 1:7
relation:
    items:
        -   from: Class:'Foo'
            to: Class:'Bar'
            type: Inherit
            loc: file0:5:26
```

###### Interface extends one super interface

```java
//// Foo.java
interface Bar<T> {
    /* ... */
}

public interface Foo extends Bar<String> {
    /* ... */
}
```

```yaml
name: Interface Extends Interface
entity:
    items:
        -   name: Foo
            type : Interface
            qualified: Foo
            loc: 5:18
            modifiers: public
        -   name: Bar
            type : Interface
            qualified: Bar
            loc: 1:11
relation:
    items:
        -   from: Interface:'Foo'
            to: Interface:'Bar'
            type: Inherit
            loc: file0:5:30
```

###### Interface extends multiple super interfaces

```java
//// Foo.java
interface Bar<T> {
    /* ... */
}

interface Baz {
    /* ... */
}

public interface Foo extends Bar<String>, Baz {
    /* ... */
}
```

```yaml
name: Interface Extends Multiple Interfaces
entity:
    items:
        -   name: Foo
            type : Interface
            loc: 9:18
            modifiers: public
        -   name: Bar
            type : Interface
            loc: 1:11
        -   name: Baz
            type : Interface
            loc: 5:11
relation:
    items:
        -   from: Interface:'Foo'
            to: Interface:'Bar'
            type: Inherit
            loc: 9:30
        -   from: Interface:'Foo'
            to: Interface:'Bar'
            type: Inherit
            loc: file0:9:43
```
