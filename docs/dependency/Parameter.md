# Dependency: Parameter
A method needs parameters to receive messages.
## Supported pattern
```yaml
name : UseVar
```
### Syntax : 
```txt
method (parType par){...}
```
### Examples : 

* Simple Parameter
```java
//Person.java
public class Person {
        private String name;
        private int age;
        private static String msg="hello world";
    
        public Person() {
        }
         
        public void fun(String name,int age) {
            System.out.println("我叫"+name+",今年"+age+"岁");
        }
    }
```

```yaml
name: Obtaining Method
entity:
    items:
        -   name: Person
            category: Class
        -   name: fun
            qualifiedName: Person.fun
            category: Method
        -   name: Person
            category: Method
            qualifiedName: Person.Person
        -   name: name
            qualifiedName: Person.fun.name
            category: Variable
relation:
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   src: File0/Method[1]
            dest: File0/"name"[1]
            category: Parameter
        -   src: File0/Method[1]
            dest: File0/"age"[1]
            category: Parameter
```