# Dependency: Set
A variable, which is just declared before, is set a value by a method.
## Supported pattern
```yaml
name : Set
```
### Syntax : 
```text
Assignment:
    var = value;
```
### Examples : 
- Method Set Local Var
```java
//Foo.java
public class Foo{
    public static final String MSG = "MSG";
    
    public void getNum(){
        int i = 1;
        String hello = "Hello";
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
    items:
        -   src: Foo/Class[0]
            dest: Foo/Variable[0]
            category: Set
        -   src: Foo/Method[0]
            dest: Foo/Variable[1]
            category: Set
        -   src: Foo/Method[0]
            dest: Foo/Variable[2]
            category: Set
```