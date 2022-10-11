## Dependency: Set

A variable, which is just declared before, is set a value by a method.

### Supported Patterns

```yaml
name: Set
```
#### Syntax: Set Definitions

```text
Assignment:
    var = value;
```

##### Examples

###### Method Set Local Var

```java
//// Foo.java
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
            type : Class
            loc: 1:14
        -   name: MSG
            type : Variable
            loc: 2:32
            modifiers: public static final
        -   name: getNum
            type : Method
            qualified: Foo.getNum
            loc: 4:17
        -   name: hello
            type : Variable
            qualified: Foo.getNum.hello
            loc: 6:16
        -   name: i
            type: Variable
            qualified: Foo.getNum.i
            loc: 5:13
relation:
    items:
        -   from: Class:'Foo'
            to: Variable:'MSG'
            type: Set
            loc: file0:2:32
        -   from: Method:'getNum'
            to: Variable:'i'
            type: Set
            loc: file0:5:13
        -   from: Method:'getNum'
            to: Variable:'Hello'
            type: Set
            loc: file0:6:16
        -   from: Method:'getNum'
            to: Variable:'Hello'
            type: Set
            loc: file0:7:9
```
###### Class Set Global Var

```java
//// Foo.java
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
name: Set Global Var
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: MSG
            type : Variable
            loc: 2:32
            modifiers: public static final
        -   name: getNum
            type : Method
            qualified: Foo.getNum
            loc: 4:17
        -   name: hello
            type : Variable
            qualified: Foo.getNum.hello
            loc: 6:16
        -   name: i
            type: Variable
            qualified: Foo.getNum.i
            loc: 5:13
relation:
    items:
        -   from: Class:'Foo'
            to: Variable:'MSG'
            type: Set
            loc: file0:2:32
```