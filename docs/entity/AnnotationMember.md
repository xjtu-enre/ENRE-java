# Entity: Annotation Member
`Annotation Member` looks a lot like a method, which provides extra actions about this annotation.
## Supported pattern
```yaml
name : AnnotationTypeMemberDeclaration
```
### Syntax : Package Definitions
```yaml
AnnotationTypeMemberDeclaration:
   [ Javadoc ] { ExtendedModifier }
       Type Identifier ( ) [ default Expression ] ;
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
        -   name: author
            loc: [ 4, 4, 4, 19 ]
            rawType: String
            qualifiedName: helloJDT.ClassPreamble.author
        -   name: date
            loc: [ 5, 4, 5, 17 ]
            rawType: String
            qualifiedName: helloJDT.ClassPreamble.date
        -   name: currentRevision
            loc: [ 6, 4, 6, 35 ]
            rawType: int
            qualifiedName: helloJDT.ClassPreamble.currentRevision
        -   name: lastModified
            loc: [ 7, 4, 7, 39 ]
            rawType: String
            qualifiedName: helloJDT.ClassPreamble.lastModified
        -   name: lastModifiedBy
            loc: [ 8, 4, 8, 41 ]
            rawType: String
            qualifiedName: helloJDT.ClassPreamble.lastModifiedBy
        -   name: reviewers
            loc: [ 10, 4, 10, 24 ]
            rawType: String-
            qualifiedName: helloJDT.ClassPreamble.reviewers
```
