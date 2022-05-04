# Dependency: Call
An entity calls other methods in its scope   
## Supported pattern
```yaml
name : Call
```
### Syntax : 
```yaml
MethodInvocation:
     [ Expression . ]
         [  Type { , Type }  ]
         Identifier ( [ Expression { , Expression } ] )
```
### Examples : 
- Method call (through ".")
```java
//Hello.java
public class Hello{
    
    public void getHello(){
        System.out.println("Hello!");
    }
    
}
```
```java
//Foo.java
public class Foo{
    public String hello;
    
    public void print(){
        Hello hello = new Hello();
        hello.getHello();
    }
}
```
```yaml
name: Method Call
entities:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Hello.getHello
        -   name: print
            category : Method
            qualifiedName: Foo.print
dependencies:
    items:
        -   src: Foo/Method[0]
            dest: Hello/Method[0]
            category: Call
```
- Method call (not through ".")
```java
//Hello.java
public class Hello{
    
    public void getHello(){
        System.out.println("Hello!");
    }
    
}
```
```java
//Foo.java
public class Foo{
    public String hello;

    public Hello getHello(){
        Hello hello = new Hello();
        hello.getHello();
        return hello;
    }
    
    public void print(){
        getHello();
    }
}
```
```yaml
name: Method Call
entities:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Hello.getHello
        -   name: getHello
            category : Method
            qualifiedName: Foo.getHello
        -   name: print
            category : Method
            qualifiedName: Foo.print
dependencies:
    items:
        -   src: Foo/Method[0]
            dest: Hello/Method[0]
            category: Call
        -   src: Foo/Method[1]
            dest: Foo/Method[0]
            category: Call
```
- Class call
```java
//Hello.java
public class Hello{
    
    public static void getHello(){
        System.out.println("Hello!");
    }
    
}
```
```java
//Foo.java
public class Foo{
    public Hello hello = Hello.getHello();
}
```
```yaml
name: Method Call
entities:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Hello.getHello
        -   name: hello
            category : Variable
            qualifiedName: Foo.hello
dependencies:
    items:
        -   src: Foo/Class[0]
            dest: Hello/Method[0]
            category: Call
```
- Method call (multiple methods)
```java
//Hello.java
public class Hello{
    
    public static void getter(){
        System.out.println("Hello!");
    }
    
}
```
```java
//Foo.java
import hello;

public class Foo{
    
    public hello getHello(){
        Hello hello = new hello();
        return hello;
    }
    
    public void print(){
        getHello().getter();
    }
}
```
```yaml
name: Method Call
entities:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Foo.getHello
        -   name: getter
            category : Method
            qualifiedName: Hello.getter
        -   name: print
            category : Method
            qualifiedName: Foo.print
dependencies:
    items:
        -   src: Foo/Method[1]
            dest: Hello/Method[0]
            category: Call
        -   src: Foo/Method[1]
            dest: Foo/Method[0]
            category: Call
```