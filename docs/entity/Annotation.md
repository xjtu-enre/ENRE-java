# Entity: Annotation

`Annotations`, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate.

## Supported pattern

```yaml
name: Annotation
```
### Syntax: Annotation Definitions

```text
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

#### Examples:

* Annotation declared in default package

```java
@interface Foo {
   /* ... */
}
```

```yaml
name: Annotation In Default Package
entity:
    filter: Annotation
    r:
        d: Type
        e: .
        s: .
        u: xInterface
    items:
        -   name: Foo
            qualifiedName: Foo
```

* Annotation declared in explicitly named package

```java
package foo;

@interface Bar {
   /* ... */
}
```

```yaml
name: Annotation In Named Package
entity:
    filter: Annotation
    items:
        -   name: Bar
            qualifiedName: foo.Bar
            r:
                d: Type
                e: .
                s: .
                u: .
```
