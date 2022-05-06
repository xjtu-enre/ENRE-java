# Entity: TypeParameter

A `type parameter entity' is a container which stores type in generics.

## Supported pattern

```yaml
name: TypeParameter
```

### Syntax: Variable Definitions

```text
TypeParameter:
    { ExtendedModifier } Identifier [ extends Type { & Type } ]
```

#### Examples:

* Generic class declaration 

```java
// Foo.java
public abstract class Foo<K, V>{
    /* ... */
}
```

```yaml
name: Generic Class Declaration 
entity:
    filter: TypeParameter
    r:
        d: x
        e: x
        s: Type Parameter
        u: TypeVariable
    items:
        -   name : K
            qualifiedName : Foo.K
        -   name : V
            qualifiedName : Foo.V
```
