# Dependency: Typed

A variable's type is one of the (self-defined) Class or other types.

## Supported pattern

```yaml
name: Typed
```

### Syntax:

```text
VariableDeclarationStatement:
    { ExtendedModifier } Type VariableDeclarationFragment
        { , VariableDeclarationFragment } ;

FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;
```

#### Examples:

* Variable Declaration Statement

```java
//Foo.java
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
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Foo.getHello
        -   name: hello
            category : Variable
            qualifiedName: Foo.getHello.hello
relation:
    items:
        -   src: file0/hello
            dest: file0/Hello
            category: Typed
            r:
                d: x
                e: .
                s: x
                u: .
```

* Field Declaration

```java
//Foo.java
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
            category : Class
        -   name: Foo
            category : Class
        -   name: hello
            category : Variable
            qualifiedName: Foo.hello
relation:
    items:
        -   src: file0/hello
            dest: file0/Hello
            category: Typed
            r:
                d: x
                e: .
                s: x
                u: .
```
