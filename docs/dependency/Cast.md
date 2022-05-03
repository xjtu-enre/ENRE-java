# Dependency: Cast
An entity casts another (self-defined) type to a variable in its scope.
## Supported pattern
```yaml
name : CastExpression
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
entities:
    items:
        -   name: Hello
            category : Class
        -   name: service
            category : Method
            qualifiedName: Controller.service
dependencies: 
        -   src: @Controller/Method[0]
            dest: @Hello/Class[0]
            kind: Cast
```