# Entity: Enum
An `enum constant` is a set of predefined constants defined in enum. 
## Supported pattern
```yaml
name : EnumConstantDeclaration
```
### Syntax : Package Definitions
```yaml
EnumConstantDeclaration:
     [ Javadoc ] { ExtendedModifier } Identifier
         [ ( [ Expression { , Expression } ] ) ]
         [ AnonymousClassDeclaration ]
```
### Examples : 
- Enum constant declaration
```java
package hello;

public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY 
}
```
```yaml
name: Enum Constant Declaration
entities:
    filter: enum
    exact: false
    items:
        -   name: SUNDAY
            qualifiedName: helloJDT.Day.SUNDAY
            loc: [ 4, 4, 4, 9 ]
        -   name: MONDAY
            qualifiedName: helloJDT.Day.MONDAY
            loc: [ 4, 12, 4, 17 ]
        -   name: THURSDAY
            qualifiedName: helloJDT.Day.THURSDAY
            loc: [ 4, 4, 5, 11 ]
                     
```
