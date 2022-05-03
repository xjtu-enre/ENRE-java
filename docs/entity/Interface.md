# Entity : Interface
An `interface` is a contract between a class and the outside world. When a class implements an interface, it promises to provide the behavior published by that interface.
## Supported pattern
```yaml
name : InterfaceDeclaration
```
### Syntax : Interface Definitions
```yaml
Interface Declaration:
      [ Javadoc ] { ExtendedModifier } interface Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { InterfaceBodyDeclaration | ; } }
```
### Examples : 
- Interface declaration
```java
package helloJDT.pkg;

public interface JDTpkg_2 {

    public String b = null;

    public abstract void imply();
}

```
```yaml
name: Interface Declaration
entities:
    filter: interface
    items:
        -   name: JDTpkg_2
            loc: [ 3, 0, 8, 0 ]
            rawType: helloJDT.pkg.JDTpkg_2
            qualifiedName: helloJDT.pkg.JDTpkg_2
            category: Interface
            modifiers: public
```
- Inner interface
```java
package helloJDT;

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

    public static void main(String... args) {
        HelloWorldAnonymousClasses myApp =
                new HelloWorldAnonymousClasses();
        myApp.sayHello();
    }
}

```
```yaml
name: Nested Interface Declaration
entities:
    filter: interface
    items:
        -   name: HelloWorld
            loc: [ 5, 4, 8, 4 ]
            rawType: helloJDT.HelloWorldAnonymousClasses.HelloWorld
            qualifiedName: helloJDT.HelloWorldAnonymousClasses.HelloWorld
            category: Interface
```