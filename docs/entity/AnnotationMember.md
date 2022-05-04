# Entity: Annotation Member

`Annotation Member` looks a lot like a method, which provides extra actions about this annotation.

## Supported pattern

```yaml
name: Annotation Member
```

### Syntax: AnnotationMember Definitions

```text
AnnotationTypeMemberDeclaration:
   [ Javadoc ] { ExtendedModifier }
       Type Identifier ( ) [ default Expression ] ;
```

#### Examples:

* Annotation member declaration

```java
@interface Foo {
   String bar();
   int baz() default 1;     // Assign a default value
}
```

```yaml
name: Annotation Member Declaration
entity:
    filter: Annotation Member
    r:
        d: Function
        e: .
        s: Field
        u: Abstract Method
    items:
        -   name: bar
            qualifiedName: Foo.bar
        -   name: baz
            qualifiedName: Foo.baz
```
