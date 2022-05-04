# Entity : Class

A class is a blueprint or prototype from which objects are created,it models the state and behavior of a real-world object.

## Supported pattern

```yaml
name : Class
```

### Syntax: Class Definitions

```text
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
AnonymousClassDeclaration:
      { ClassBodyDeclaration }
```

#### Examples:

* Class declaration

```java
class Foo{
    /* ... */
}
```

```yaml
name: Class Declaration
entity:
    filter: Class
    r:
        d: Type
        e: .
        s: .
        u: .
    items:
        -   name: Foo
            qualifiedName : Foo
```

* Nested class declaration

```java
class Foo {
    class Nested {
        /* ... */
    }
}
```

```yaml
name: Nested Class Declaration
entity:
    filter: Class
    r:
        d: Type
        e: .
        s: .
        u: .
    items:
        -   name: Foo
            qualifiedName : Foo
        -   name: Nested
            qualifiedName : Foo.Nested
```

* Anonymous class declaration

```java
class Foo {
  
    interface Bar {
        void a();
    }
  
    public void doThings() {

        /**
         * Below creates 2 anonymous class which
         * implements interface `Bar` instantly
         */
        Bar anony0 = new Bar() {
            public void a() {
                /* ... */
            }
        };

        Bar anony1 = new Bar() {
            public void a() {
                /* ... */
            }
        };
        
    }         
}
```

```yaml
name: Anonymous Class Declaration
entity:
    filter: Class
    r:
        d: r/Create
        e: .
        s: .
        u: .
    items:
        -   name: <Anonymous type="Class">
            qualifiedName : Foo.doThings.<Anonymous type="Class">
        -   name: <Anonymous type="Class">
            qualifiedName : Foo.doThings.<Anonymous type="Class">

```