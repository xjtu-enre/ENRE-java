# Dependency: Inherit

A class extends one super (abstract) class, or an interface extends single or multiple super interfaces.

## Supported pattern

```yaml
name: Inherit
```

### Syntax: 

```text
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```

#### Examples: 

* Class extends one super class

```java
// Foo.java
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
            category : Class
            qualifiedName: Foo
            modifiers: public
        -   name: Bar
            category : Class
            qualifiedName: Bar
relation:
    r:
        d: .
        e: .
        s: Inheritance
        u: Extend Couple
    items:
        -   src: file0/Foo
            dest: file0/Bar
            category: Inherit
```

* Class extends one parameterized type

```java
// Foo.java
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
            category : Class
            qualifiedName: Foo
        -   name: Bar
            category : Class
            qualifiedName: Bar
relation:
    r:
        d: .
        e: .
        s: Inheritance
        u: Extend Couple
    items:
        -   src: file0/Foo
            dest: file0/Bar
            category: Inherit
```

* Interface extends one super interface

```java
// Foo.java
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
            category : Interface
            qualifiedName: Foo
            modifiers: public
        -   name: Bar
            category : Interface
            qualifiedName: Bar
relation:
    r:
        d: .
        e: .
        s: Inheritance
        u: Extend Couple
    items:
        -   src: file0/Foo
            dest: file0/Bar
            category: Inherit
```

* Interface extends multiple super interfaces

```java
// Foo.java
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
            category : Interface
            modifiers: public
        -   name: Bar
            category : Interface
        -   name: Baz
            category : Interface
relation:
    r:
        d: .
        e: .
        s: Inheritance
        u: Extend Couple
    items:
        -   src: file0/Foo
            dest: file0/Bar
            category: Inherit
        -   src: file0/Foo
            dest: file0/Bar
            category: Inherit
```
