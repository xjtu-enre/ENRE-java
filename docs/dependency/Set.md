# Dependency: Set

A variable, which is just declared before, is set a value by a method.

## Supported pattern

```yaml
name: Set
```
### Syntax:

```text
Assignment:
    var = value;
```

#### Examples:

* Method Set Local Var

```java
// Foo.java
public class Foo {
    public static final String MSG = "MSG";
    
    public void getNum(){
        int i = 1;
        String hello = "Hello";
        hello = "World";
    }
}
```
```yaml
name: Set Local Var
entity:
    items:
        -   name: Foo
            category : Class
        -   name: MSG
            category : Variable
            modifiers: public static final
        -   name: getNum
            category : Method
            qualifiedName: Foo.getNum
        -   name: hello
            category : Variable
            qualifiedName: Foo.getNum.hello
        -   name: i
            category: Variable
            qualifiedName: Foo.getNum.i
relation:
    r:
        d: x
        e: .
        s: x
        u: Set Init
    items:
        -   src: file0/Foo
            dest: file0/MSG
            category: Set
        -   src: file0/getNum
            dest: file0/i
            category: Set
        -   src: file0/getNum
            dest: file0/Hello
            category: Set
        -   src: file0/getNum
            dest: file0/Hello
            category: Set
            r:
                d: x
                e: x
                s: x
                u: Set
```
