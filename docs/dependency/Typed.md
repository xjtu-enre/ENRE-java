# Dependency: Typed
A variable's type is one of the (self-defined) Class or other types.
## Supported pattern
```yaml
name : Typed
```
### Syntax : 
```yaml
VariableDeclarationStatement:
    { ExtendedModifier } Type VariableDeclarationFragment
        { , VariableDeclarationFragment } ;

FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;
```
### Examples : 
- Variable Declaration Statement
```java
//Hello.java
public class Hello{

}
```
```java
//Foo.java
public class Foo{
    public String hello;
    
    public Hello getHello(){
        Hello hello = new Hello();
    }
}
```
```yaml
name: Cast Expression
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
        -   src: Foo/Variable[1]
            dest: Hello/Class[0]
            category: Typed
```
- Field Declaration
```java
//Hello.java
public class Hello{

}
```
```java
//Foo.java
public class Foo{
    Hello hello = new Hello();
}
```
```yaml
name: Cast Expression
entities:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: hello
            category : Variable
            qualifiedName: Foo.hello
dependencies:
    items:
        -   src: Foo/Variable[0]
            dest: Hello/Class[0]
            category: Typed
```
