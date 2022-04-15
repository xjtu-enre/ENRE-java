# Entity : Variable
A `variablr entity' is a container which stores values.
## Supported pattern
```yaml
name : VariableDeclaration
```
### Syntax : Variable Definitions
```yaml
 VariableDeclarationExpression:
    { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment }

 VariableDeclarationStatement:
    { ExtendedModifier } Type VariableDeclarationFragment
        { , VariableDeclarationFragment } ;

 FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;

 VariableDeclarationFragment:
    Identifier { Dimension } [ = Expression ]

 SingleVariableDeclaration:
    { ExtendedModifier } Type {Annotation} [ ... ] Identifier { Dimension } [ = Expression ]

```
### Examples : 
- Field (global variable) declaration 
```java
package hello;

class foo{
    public int a;
}
```
```yaml
name: Field Declaration
entities:
    filter: variable
    items:
        -   name: a
            qualifiedName : hello.foo.a
            loc: [ 4, 16 ]
```
