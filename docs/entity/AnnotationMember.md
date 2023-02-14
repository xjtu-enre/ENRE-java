## Entity: Annotation Member

 A `Annotation Member` looks a lot like a method, which provides extra actions about this annotation.

### Supported Patterns

```yaml
name: Annotation Member
```

#### Syntax: Annotation Member Definitions

```text
AnnotationTypeMemberDeclaration:
   [ Javadoc ] { ExtendedModifier }
       Type Identifier ( ) [ default Expression ] ;
```

##### Examples

###### Annotation member declaration

```java
@interface Foo {
   String bar();
   int baz() default 1;     // Assign a default value
}
```

```yaml
name: Annotation Member Declaration
entity:
    type: Annotation Member
    items:
        -   name: bar
            qualified: Foo.bar
            loc: 2:11
        -   name: baz
            qualified: Foo.baz
            loc: 3:8
```

### Properties

| Name     | Description             |    Type     |     Default     |
|----------|-------------------------|:-----------:|:---------------:|
| Modifier | Accessibility modifier. | `'public'`\| `'protected'` \| `'private'` | `'public'` |

