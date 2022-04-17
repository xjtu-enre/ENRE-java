# Entity : Method
## Supported pattern
```
name : MethodDeclaration
```
### Syntax : Method Definitions
```
Package Declaration:
    [ Javadoc ] { Annotation } package Name ;
```
### Examples : 
- Package declaration
```java
class foo{

}
```
```yaml
name: Package Declaration
entities:
    filter: package
    items:
        -   name: hello
            location : []
```
