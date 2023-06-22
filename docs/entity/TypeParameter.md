## Entity: TypeParameter

A `type parameter entity' is a container which stores type in generics.

### Supported Patterns

```yaml
name: TypeParameter
```

#### Syntax: Variable Definitions

```text
TypeParameter:
    { ExtendedModifier } Identifier [ extends Type { & Type } ]
```

##### Examples

###### Generic class declaration 

```java
//// Foo.java
public abstract class Foo<K, V>{
    /* ... */
}
```

```yaml
name: Generic Class Declaration 
entity:
    type: Type Parameter
    items:
        -   name: K
            qualified: Foo.K
            loc: 1:27
        -   name : V
            qualified: Foo.V
            loc: 1:30
```
###### Generic class declaration 

```java
//// Foo.java
public interface Foo<T> {
    /* ... */
}
```

```yaml
name: Generic Interface Declaration 
entity:
    type: Type Parameter
    items:
        -   name: T
            qualified: Foo.T
            loc: 1:22
```
