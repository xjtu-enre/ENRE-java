## Dependency: Parameter

A method needs parameters to receive messages.

### Supported Patterns

```yaml
name: Parameter
```

#### Syntax: Parameter Definitions

```text
method (parType par){...}
```

##### Examples

###### Simple Parameter

```java
//// Foo.java
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
            type: Class
            loc: 1:14
        -   name: foo
            qualified: Foo.foo
            type: Method
            loc: 2:17
        -   name: name
            qualified: Foo.foo.name
            type: Variable
            loc: 2:28
relation:
    items:
        -   from: Method:'foo'
            to: Variable:'name'
            type: Parameter
            loc: file0:2:28
```
