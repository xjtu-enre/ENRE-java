## Entity : Class

A class is a blueprint or prototype from which objects are created,it models the state and behavior of a real-world object.

### Supported Patterns

```yaml
name : Class
```

#### Syntax: Class Definitions

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

##### Examples

###### Class declaration

```java
class Foo{
    /* ... */
}
```

```yaml
name: Class Declaration
entity:
    type: Class
    items:
        -   name: Foo
            qualified : Foo
            loc: 1:7
```

###### Nested class declaration

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
    type: Class
    items:
        -   name: Foo
            qualified : Foo
            loc: 1:7
        -   name: Nested
            qualified : Foo.Nested
            loc: 2:11
```

###### Anonymous class declaration

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
    type: Class
    items:
        -   name: <Anon Class>
            qualified: Foo.doThings.<Anon Class>
            loc: 13:13
        -   name: <Anon Class>
            qualified: Foo.doThings.<Anon Class>
            loc: 19:13

```
