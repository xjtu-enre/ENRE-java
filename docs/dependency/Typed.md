# Dependency: Typed
A variable's type is one of the (self-defined) Class or other types.
## Supported pattern
```yaml
name : 
    - VariableDeclaration
    - FieldDeclaration
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
- Field Declaration
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
scenario: Cast Expression
entities:
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
dependencies: 
        -   src: @Foo/Variable[0]
            dest: @Hello/Class[0]
            kind: Typed
```
- Variable Declaration Statement
