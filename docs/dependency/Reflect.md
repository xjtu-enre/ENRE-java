# Dependency: Reflect
`Reflection` is commonly used by programs which require the ability to examine or modify the runtime behavior of applications running in the Java virtual machine. This is a relatively advanced feature and should be used only by developers who have a strong grasp of the fundamentals of the language. With that caveat in mind, reflection is a powerful technique and can enable applications to perform operations which would otherwise be impossible.
## Supported pattern
```yaml
name : Reflect
```
### Syntax : 
```txt
Retrieving Class Objects:  
    Object.getClass()
    The .class Syntax
    Class.forName()
Obtaining Method Type Information:
    getMethods()
    getDeclaredMethods()
    getMethod(name，params)
    getDeclaredMethod(name, params)
invoke a method:
    method.invoke()
```
### Examples : 

* Obtaining Method
```java
//Person.java
public class Person {
        private String name;
        private int age;
        private static String msg="hello world";
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public int getAge() {
            return age;
        }
    
        public void setAge(int age) {
            this.age = age;
        }
    
        public Person() {
        }
    
        private Person(String name) {
            this.name = name;
            System.out.println(age);
        }
    
        public void fun() {
            System.out.println("fun "+Person.msg);
        }
    
        public void fun(String name,int age) {
            System.out.println("我叫"+name+",今年"+age+"岁");
        }
    }
```
```java
//ReflectDemo.java
import java.lang.reflect.Method;
import Person;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Person o = new Person();
            Class c = o.getClass();
            Method method = c.getMethod("fun", String.class, int.class);
            method.invoke(o, "tengj", 10);
            method = c.getMethod("fun", null);
            method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        -   name: ReflectDemo
            category: Class
        -   name: main
            qualifiedName: ReflectDemo.main
            category: Method
relation:
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   src: file1/Method[0]
            dest: file0/"fun"[0]
            category: Reflect
        -   src: file1/Method[0]
            dest: file0/"fun"[1]
            category: Reflect
```

* Retrieving Class Objects (Class.forName())

```java
//Reflect.java
package helloJDT.pkg;

public class Reflect {

    public static void  main(String args[]){

        try{
            Class reflectClass = Class.forName("helloJDT.pkg.Reflect");
            System.out.println(reflectClass.getName());
            System.out.println(reflectClass.getSimpleName());
        }
        catch(ClassNotFoundException e){
            System.out.println("CLASS NOT FOUND");
        }

    }
}
```

```yaml
name: Retrieving Class Objects (Class.forName())
entity:
    items:
        -   name: pkg
            category: Package
            qualifiedName: helloJDT.pkg
        -   name: Reflect
            qualifiedName: helloJDT.pkg.Reflect
            category: Class
        -   name: main
            qualifiedName: helloJDT.pkg.Reflect.main
            category: Method
relation:
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   src: file0/Method[0]
            dest: file0/Class[0]
            category: Reflect
```

* Retrieving Class Objects (The .class Syntax)

```java
//Person.java
public class Person {
               private String name;
               private int age;
               private static String msg="hello world";
               public String getName() {
                   return name;
               }
           
               public void setName(String name) {
                   this.name = name;
               }
           
               public int getAge() {
                   return age;
               }
           
               public void setAge(int age) {
                   this.age = age;
               }
           
               public Person() {
               }
           
               private Person(String name) {
                   this.name = name;
                   System.out.println(age);
               }
           
               public void fun() {
                   System.out.println("fun "+Person.msg);
               }
           
               public void fun(String name,int age) {
                   System.out.println("我叫"+name+",今年"+age+"岁");
               }
           }
```

```java
//ReflectDemo.java
import java.lang.reflect.Method;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Class c;
            c = Person.class;
            Object o = c.newInstance();
            Method method = c.getMethod("fun", String.class, int.class);
            method.invoke(o, "tengj", 10);
            method = c.getMethod("fun", null);
            method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```yaml
name: Retrieving Class Objects (The .class Syntax)
entity:
    items:
        -   name: Person
            category: Class
        -   name: fun
            qualifiedName: Person.fun
            category: Method
        -   name: ReflectDemo
            category: Class
        -   name: main
            qualifiedName: ReflectDemo.main
            category: Method
relation:
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   src: file1/Method[0]
            dest: file0/Class[0]
            category: Reflect
```

* Retrieving Class Objects (Object.getClass())

```java
//Person.java
public class Person {
        private String name;
        private int age;
        private static String msg="hello world";
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public int getAge() {
            return age;
        }
    
        public void setAge(int age) {
            this.age = age;
        }
    
        public Person() {
        }
    
        private Person(String name) {
            this.name = name;
            System.out.println(age);
        }
    
        public void fun() {
            System.out.println("fun "+Person.msg);
        }
    
        public void fun(String name,int age) {
            System.out.println("我叫"+name+",今年"+age+"岁");
        }
    }
```
```java
//ReflectDemo.java
import java.lang.reflect.Method;
import Person;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Person o = new Person();
            Class c = o.getClass();
            Method method = c.getMethod("fun", String.class, int.class);
            method.invoke(o, "tengj", 10);
            method = c.getMethod("fun", null);
            method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```yaml
name: Retrieving Class Objects (Object.getClass())
entity:
    items:
        -   name: Person
            category: Class
        -   name: fun
            qualifiedName: Person.fun
            category: Method
        -   name: ReflectDemo
            category: Class
        -   name: main
            qualifiedName: ReflectDemo.main
            category: Method
relation:
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   src: file1/Method[0]
            dest: file0/Class[0]
            category: Reflect
```