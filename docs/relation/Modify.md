## Dependency: Modify

A variable, which is read and write at the same time, is recognized as modified (Otherwise may only Use or Set).

### Supported Patterns

```yaml
name: Modify
```

#### Syntax: Modify Definitions

```text
Assignment:
    var++;
    var += / -= / *= / /= / ...
```

##### Examples

###### Modify a field

```java
//// Foo.java
public class Foo {
    int foo = 0;
    
    public void counting() {
        foo++;
        ++foo;
    }
}
```

```yaml
name: Modify Field
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: foo
            type : Variable
            qualified: Foo.foo
            loc: 2:9
        -   name: counting
            type : Method
            qualified: Foo.counting
            loc: 4:17
relation:
    type: Modify
    extra: false
    items:
        -   from: Method:'counting'
            to: Variable:'foo'
            loc: file0:5:9
        -   from: Method:'counting'
            to: Variable:'foo'
            loc: file0:6:11
```

###### Modify Local Var

```java
//// Foo.java
public class Foo {
    public int counting(int i) {
        int j = i % 2;              // <--- Set
        j += 1;                     // <--- Modify
        j -= 2;                     // <--- Modify
        j *= 3;                     // <--- Modify
        j /= 4;                     // <--- Modify
        return j;
    }
}
```

```yaml
name: Modify Local Var
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: j
            type : Variable
            qualified: Foo.counting.j
            loc: 3:13
        -   name: counting
            type : Method
            qualified: Foo.counting
            loc: 2:16
relation:
    type: Modify
    extra: false
    items:
        -   from: Method:'counting'
            to: Variable:'j'
            loc: file0:4:9
        -   from: Method:'counting'
            to: Variable:'j'
            loc: file0:5:9
        -   from: Method:'counting'
            to: Variable:'j'
            loc: file0:6:9
        -   from: Method:'counting'
            to: Variable:'j'
            loc: file0:7:9
```
