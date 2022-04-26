# Entity: Enum
An `enum type` is a special data type that enables for a variable to be a set of predefined constants. The variable must be equal to one of the values that have been predefined for it. 
## Supported pattern
```yaml
name : EnumDeclaration
```
### Syntax : Package Definitions
```yaml
EnumDeclaration:
     [ Javadoc ] { ExtendedModifier } enum Identifier
         [ implements Type { , Type } ]
         {
         [ EnumConstantDeclaration { , EnumConstantDeclaration } ] [ , ]
         [ ; { ClassBodyDeclaration | ; } ]
         }
```
### Examples : 
- Enum declaration
```java
package hello;

public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY 
}
```
```yaml
name: Enum Declaration
entities:
    filter: enum
    items:
        -   name: Day
            qualifiedName: hello.Day
            loc: [ 3, 0, 6, 0]
```
