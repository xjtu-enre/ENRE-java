# Dependency: Cast

An entity casts another (self-defined) type to a variable in its scope.

## Supported pattern

```yaml
name: Cast
```

### Syntax: 

```text
CastExpression:
    ( Type ) Expression
```

#### Examples:

* Cast expression

```java
// Foo.java
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
            category : Class
        -   name: foo
            category : Method
            qualifiedName: Foo.foo
relation:
    items:
        -   src: file0/foo
            dest: file0/Bar
            category: Cast
            r:
                d: .
                e: .
                s: o/Weak/Type Use
                u: Use Cast
```
