## Dependency: Cast

An entity casts another (self-defined) type to a variable in its scope.

### Supported Patterns

```yaml
name: Cast
```

#### Syntax: Cast Difinations

```text
CastExpression:
    ( Type ) Expression
```

##### Examples

###### Cast expression

```java
//// Foo.java
public class Foo{
    public void foo(Object barLike){
        Bar mBar = (Bar)barLike;
    }
}

class Bar {
    /* ... */
}
```

```yaml
name: Cast Expression
entity:
    items:
        -   name: Bar
            type: Class
            loc: 7:7
        -   name: foo
            type: Method
            qualified: Foo.foo
            loc: 2:17
relation:
    items:
        -   from: Method:'foo'
            to: Class:'Bar'
            type: Cast
            loc: file0:3:21
```
