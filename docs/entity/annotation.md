# Entity: Annotation
`Annotations`, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate.
## Supported pattern
```yaml
name : AnnotationTypeDeclaration
```
### Syntax : Package Definitions
```yaml
AnnotationTypeDeclaration:
   [ Javadoc ] { ExtendedModifier } @ interface Identifier
                { { AnnotationTypeBodyDeclaration | ; } }
   AnnotationTypeBodyDeclaration:
       AnnotationTypeMemberDeclaration
       FieldDeclaration
       TypeDeclaration
       EnumDeclaration
       AnnotationTypeDeclaration
```
### Examples : 
- Annotation declaration
```java
package hello;

@interface ClassPreamble {
   String author();
   String date();
   int currentRevision() default 1;
   String lastModified() default "N/A";
   String lastModifiedBy() default "N/A";
   // Note use of array
   String[] reviewers();
}
```
```yaml
name: Annotation Declaration
entities:
    filter: annotation
    items:
        -   name: Day
            loc: [ 1, 1 ]
```
