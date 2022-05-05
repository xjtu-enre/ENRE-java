# Dependency: Cast
An entity casts another (self-defined) type to a variable in its scope.
## Supported pattern
```yaml
name : Cast
```
### Syntax : 
```yaml
CastExpression:
    ( Type ) Expression
```
### Examples : 
- Cast expression
```java
//Hello.java
public class Hello{

}
```
```java
//Controller.java
public class Controller{
    public void service(Test test){
        Hello hello = (Hello) test;
    }
}
```
```yaml
name: Cast Expression
entity:
    items:
        -   name: Hello
            category : Class
        -   name: service
            category : Method
            qualifiedName: Controller.service
relation:
    items:
        -   src: file1/Method[0]
            dest: file0/Class[0]
            category: Cast
```