# Entity : Interface
An `interface` is a contract between a class and the outside world. When a class implements an interface, it promises to provide the behavior published by that interface.
## Supported pattern
```yaml
name : TypeDeclaration
```
### Syntax : Class Definitions
```yaml
Interface Declaration:
      [ Javadoc ] { ExtendedModifier } interface Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { InterfaceBodyDeclaration | ; } }
```
### Examples : 
- Interface declaration
```java
interface foo{
    
}
```
```yaml
name: Interface Declaration
entities:
    filter: interface
    items:
        -   name: foo
            loc: [ 1, 1 ]
```