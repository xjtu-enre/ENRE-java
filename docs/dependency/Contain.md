# Dependency: Contain
A Package contains files, or a file contains types.
## Supported pattern
```yaml
name: 
    - ClassDeclaration
    - InterfaceDeclaration
    - EnumDeclaration
    - AnnotationTypeDeclaration
```
### Syntax : 
```yaml
AnnotationTypeDeclaration:
   [ Javadoc ] { ExtendedModifier } @ interface Identifier
                { { AnnotationTypeBodyDeclaration | ; } }
Interface Declaration:
      [ Javadoc ] { ExtendedModifier } interface Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { InterfaceBodyDeclaration | ; } }
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
EnumDeclaration:
     [ Javadoc ] { ExtendedModifier } enum Identifier
         [ implements Type { , Type } ]
         {
         [ EnumConstantDeclaration { , EnumConstantDeclaration } ] [ , ]
         [ ; { ClassBodyDeclaration | ; } ]
         }
```
### Examples : 
- Package contains package

- Package contains files

- File contains class(es)

- File contains enum(s)

- File contains interface(s)

- File contains annotation(s)