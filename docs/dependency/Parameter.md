# Dependency: Parameter

A method needs parameters to receive messages.

## Supported pattern

```yaml
name: Parameter
```

### Syntax:

```text
method (parType par){...}
```

#### Examples: 

* Simple Parameter

```java
// Foo.java
public class Foo {
    public void foo(String name) {
        /* ... */
    }
}
```

```yaml
name: Obtaining Method
entity:
    items:
        -   name: Foo
            category: Class
        -   name: do
            qualifiedName: Foo.do
            category: Method
        -   name: name
            qualifiedName: Foo.do.name
            category: Variable
relation:
    r:
        d: x
        e: .
        s: x
        u: e/Parameter
    items:
        -   src: file0/do
            dest: file0/name
            category: Parameter
```
