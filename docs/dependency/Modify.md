# Dependency: Modify

A variable, which is read and write at the same time, is recognized as modified (Otherwise may only Use or Set).

## Supported pattern

```yaml
name: Modify
```

### Syntax: 

```text
Assignment:
    var++;
    var += / -= / *= / /= / ...
```

#### Examples:

* Modify a field

```java
// Foo.java
public class Foo {
    int foo = 0;
    
    public void counting() {
        foo++;
    }
}
```

```yaml
name: Modify Field
entity:
    items:
        -   name: Foo
            category : Class
        -   name: foo
            category : Variable
            qualifiedName: Foo.foo
        -   name: counting
            category : Method
            qualifiedName: Foo.counting
relation:
    items:
        -   src: file0/counting
            dest: file0/foo
            category: Modify
            r:
                d: x/weak/Use
                e: xUseVar
                s: o/weak/Use
                u: .
```

* Modify Local Var

```java
//Foo.java
public class Foo {
    public int counting(int i) {
        int j = i % 2;              // <--- Set
        j += 1;                     // <--- Modify
        j += 2;                     // <--- Modify
        j /= 3;                     // <--- Modify
        return j;
    }
}
```

```yaml
name: Modify Local Var
entity:
    items:
        -   name: Foo
            category : Class
        -   name: j
            category : Variable
            qualifiedName: Foo.counting.j
        -   name: counting
            category : Method
            qualifiedName: Foo.counting
relation:
    items:
        -   src: file0/counting
            dest: file0/j
            category: Modify
            r:
                d: o/weak/Use
                e: x
                s: x
                u: .
```
