# Entity : Class
A class is a blueprint or prototype from which objects are created,it models the state and behavior of a real-world object.
## Supported pattern
```yaml
name : ClassDeclaration
```
### Syntax : Class Definitions
```yaml
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
### Examples : 
- Class declaration
```java
package hello;

class Foo{
    int a;
}
```
```yaml
name: Class Declaration
entities:
    filter: class
    items:
        -   name: foo
            qualifiedName : hello.Foo
            loc: [ 3, 0, 5, 0]
            id : 3
            parentId : 2
            childrenIds : [4]
```
- Inner class declaration
```java
package hello;

class Foo{
    int a;
    class Inner{
        int b;
    }
}
```
```yaml
name: NestedClassDeclaration
entities:
    filter: class
    items:
        -   name: foo
            qualifiedName : hello.Foo
            loc: [ 3, 0, 8, 0]
        -   name: inner
            qualifiedName : hello.Foo.Inner
            loc: [5, 4, 7, 4]
```
- Anonymous class declaration
```java
public class HelloWorldAnonymousClasses {
  
    interface HelloWorld {
        void greet();
        void greetSomeone(String someone);
    }
  
    public void sayHello() {
        
        class EnglishGreeting implements HelloWorld {
            String name = "world";
            public void greet() {
                greetSomeone("world");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Hello " + name);
            }
        }
      
        HelloWorld englishGreeting = new EnglishGreeting();
        
        HelloWorld frenchGreeting = new HelloWorld() {
            String name = "tout le monde";
            public void greet() {
                greetSomeone("tout le monde");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Salut " + name);
            }
        };
        
        HelloWorld spanishGreeting = new HelloWorld() {
            String name = "mundo";
            public void greet() {
                greetSomeone("mundo");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Hola, " + name);
            }
        };
        englishGreeting.greet();
        frenchGreeting.greetSomeone("Fred");
        spanishGreeting.greet();
    }         
}
```
```yaml
name: Anonymous Class Declaration
entities:
    filter: class
    items:
        -   name: HelloWorldAnonymousClasses
            qualifiedName : HelloWorldAnonymousClasses
            rawType: HelloWorldAnonymousClasses
            loc: [ 1, 0, 53, 0]
            modifiers: public
        -   name: EnglishGreeting
            qualifiedName : HelloWorldAnonymousClasses.sayHello.EnglishGreeting
            rawType: HelloWorldAnonymousClasses.sayHello.EnglishGreeting
            loc: [ 9, 8, 18, 8]
            modifiers: public
        -   name: Anonymous_Class
            qualifiedName : HelloWorldAnonymousClasses.sayHello.Anonymous_Class
            rawType: HelloWorldAnonymousClasses.HelloWorld
            loc: [ 25, 53, 32, 8]
        -   name: Anonymous_Class
            qualifiedName : HelloWorldAnonymousClasses.sayHello.Anonymous_Class
            rawType: HelloWorldAnonymousClasses.HelloWorld
            loc: [36, 54, 45, 8]
```