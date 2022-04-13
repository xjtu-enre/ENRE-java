# Entity : Class
A class is a blueprint or prototype from which objects are created,it models the state and behavior of a real-world object.
## Supported pattern
```
name : TypeDeclaration
```
### Syntax : Class Definitions
```
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```
### Examples : 
- Class declaration
```java
class foo{
    
}
```
```yaml
name: Class Declaration
entities:
    filter: class
    items:
        -   name: foo
            loc: [ 1, 1 ]
```
