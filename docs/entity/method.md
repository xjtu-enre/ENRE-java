# Entity : Method
## Supported pattern
```
name : PackageDeclaration
```
### Syntax : Package Definitions
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
            loc: [ 1, 1 ]
```
