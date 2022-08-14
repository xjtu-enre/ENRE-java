## Dependency: Typed

A variable's type is one of the (self-defined) Class or other types.

### Supported Patterns

```yaml
name: Typed
```

#### Syntax: Typed Definitions

```text
VariableDeclarationStatement:
    { ExtendedModifier } Type VariableDeclarationFragment
        { , VariableDeclarationFragment } ;

FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;
```

##### Examples

###### Variable Declaration Statement

```java
//// Foo.java
public class Foo {
    public Hello getHello() {
        Hello hello = new Hello();
    }
}

class Hello{
    /* ... */
}
```

```yaml
name: Type A Var
entity:
    items:
        -   name: Hello
            type : Class
            loc: 7:7
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: getHello
            type : Method
            qualified: Foo.getHello
            loc: 2:18
        -   name: hello
            type : Variable
            qualified: Foo.getHello.hello
            loc: 3:15
relation:
    items:
        -   from: Variable:'hello'
            to: Class:'Hello'
            type: Typed
            loc: file0:3:15
```

###### Field Declaration

```java
//// Foo.java
public class Foo{
    Hello hello = new Hello();
}

public class Hello{
    /* ... */
}
```

```yaml
name: Type A Field
entity:
    items:
        -   name: Hello
            type : Class
            loc: 5:14
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: hello
            type : Variable
            qualified: Foo.hello
            loc: 2:11
relation:
    items:
        -   from: Variable:'hello'
            to: Class:'Hello'
            type: Typed
            loc: file0:2:11
```
