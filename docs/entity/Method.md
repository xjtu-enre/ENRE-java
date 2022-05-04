# Entity: Method

`Method` is an entity inside the `class` to perform specific activity.

## Supported pattern

```yaml
name: Method
```
### Syntax: Method Definitions

```text
MethodDeclaration:
    [ Javadoc ] { ExtendedModifier } [  TypeParameter { , TypeParameter } ] ( Type | void )
        Identifier (
            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
        ) { Dimension }
        [ throws Type { , Type } ]
        ( Block | ; )

ConstructorDeclaration: 
    [ Javadoc ] { ExtendedModifier } [  TypeParameter { , TypeParameter }  ]
        Identifier (
            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
        ) { Dimension }
        [ throws Type { , Type } ]
        ( Block | ; )

CompactConstructorDeclaration:
    [ Javadoc ] ExtendedModifier { ExtendedModifier}
        Identifier
        ( Block | ; )
```

#### Examples:

* Method declaration

```java
class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

```yaml
name: Method Declaration
entity:
    filter: Method
    r:
        d: Function
        e: .
        s: .
        u: .
    items:
        -   name: getName
            qualifiedName: helloJDT.pkg.test.Person.getName
        -   name: setName
            qualifiedName: helloJDT.pkg.test.Person.setName
```
