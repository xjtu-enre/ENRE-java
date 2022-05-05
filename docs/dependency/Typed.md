# Dependency: Typed
A variable's type is one of the (self-defined) Class or other types.
## Supported pattern
```yaml
name : Typed
```
### Syntax : 
```text
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
        -   src: File0/Variable[1]
            dest: File1/Class[0]
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
        -   src: File1/Variable[0]
            dest: File0/Class[0]
            category: Typed
```
