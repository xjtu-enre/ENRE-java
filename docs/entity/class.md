# Entity : Class
A class is a blueprint or prototype from which objects are created,it models the state and behavior of a real-world object.
## Supported pattern
```yaml
name : TypeDeclaration
```
### Syntax : Class Definitions
```yaml
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
AnonymousClassDeclaration:
      { ClassBodyDeclaration }
```
### Examples : 
- Class declaration
```java
package hello;

class foo{
    int a;
}
```
```yaml
name: Class Declaration
entities:
    filter: class
    items:
        -   name: foo
            qualifiedName : hello.foo
            loc: [ 3, 0, 5, 0]
            id : 3
            parentId : 2
            childrenIds : [4]
```
- Inner class declaration

- Anonymous class declaration