## Dependency: Reflect
`Reflection` is commonly used by programs which require the ability to examine or modify the runtime behavior of applications running in the Java virtual machine. This is a relatively advanced feature and should be used only by developers who have a strong grasp of the fundamentals of the language. With that caveat in mind, reflection is a powerful technique and can enable applications to perform operations which would otherwise be impossible.
### Supported Patterns
```yaml
name : Reflect
```
#### Syntax: Reflect Definitions
```text
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
##### Examples

###### Obtaining Method (object.getMethod())
```java
//// Person.java
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
//// ReflectDemo.java
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
name: Obtaining Method (object.getMethod())
entity:
    items:
        -   name: Person
            qualified: Person
            type: Class
            loc: file0:1:14
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:29:21
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:33:21
        -   name: ReflectDemo
            type: Class
            loc: file1:4:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:6:24
relation:
    items:
        -   from: Method:'main'
            to: Method:'fun'[@loc=file0:29:21]
            type: Reflect
            loc: file1:12:22
        -   from: Method:'main'
            to: Method:'fun'[@loc=file0:33:21]
            type: Reflect
            loc: file1:10:29
```

###### Obtaining Method (object.getDeclaringMethod())
```java
//// Person.java
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
//// ReflectDemo.java
import java.lang.reflect.Method;
import Person;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Person o = new Person();
            Class c = o.getClass();
            Method method = c.getMethod("fun", String.class, int.class);
            method.invoke(o, "tengj", 10);
            method = c.getDeclaredMethod("fun", null);
            method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```yaml
name: Obtaining Method (object.getDeclaringMethod())
entity:
    items:
        -   name: Person
            type: Class
            loc: file0:1:14
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:29:21
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:33:21
        -   name: ReflectDemo
            type: Class
            loc: file1:4:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:6:24
relation:
    items:
        -   from: Method:'main'
            to: Method:'fun'[@loc=file0:29:21]
            type: Reflect
            loc: file1:12:22
        -   from: Method:'main'
            to: Method:'fun'[@loc=file0:33:21]
            type: Reflect
            loc: file1:10:29
```

###### Retrieving Class Objects (Class.forName())

```java
//// Reflect.java
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
            type: Package
            qualified: helloJDT.pkg
            loc: 1:9
        -   name: Reflect
            qualified: helloJDT.pkg.Reflect
            type: Class
            loc: 3:14
        -   name: main
            qualified: helloJDT.pkg.Reflect.main
            type: Method
            loc: 5:25
relation:
    items:
        -   from: Method:'main'
            to: Class:'Reflect'
            type: Reflect
            loc: file0:8:34
```

###### Retrieving Class Objects (The .class Syntax)

```java
//// Person.java
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
//// ReflectDemo.java
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
            type: Class
            loc: file0:1:14
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:29:28
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:33:28
        -   name: ReflectDemo
            type: Class
            loc: file1:3:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:5:24
relation:
    items:
        -   from: Method:'main'
            to: Class:'Person'
            type: Reflect
            loc: file1:8:17
```

###### Retrieving Class Objects (Object.getClass())

```java
//// Person.java
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
//// ReflectDemo.java
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
            type: Class
            loc: file0:1:14
        -   name: fun
            qualified: Person.fun
            type: Method
            loc: file0:29:21
        -   name: fun
            qualified: Person.fun
            loc: file0:33:21
            type: Method
        -   name: ReflectDemo
            type: Class
            loc: file1:4:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:6:24
relation:
    items:
        -   from: Method:'main'
            to: Class:'Person'
            type: Reflect
            loc: file1:9:23
```

###### Retrieving Class Fields (Object.getField())

```java
//// Person.java
public class Person {
    public String test;
    private String name;
    private int age;
    private static String msg="hello world";
    }
```
```java
//// ReflectDemo.java
import java.lang.reflect.Field;
import Person;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Person o = new Person();
            Class c = o.getClass();
            Field field = c.getField("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```yaml
name: Retrieving Class Fields (Object.getField())
entity:
    items:
        -   name: Person
            type: Class
            loc: file0:1:14
        -   name: test
            qualified: Person.test
            type: Variable
            loc: file0:2:18
        -   name: ReflectDemo
            type: Class
            loc: file1:4:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:6:24
relation:
    items:
        -   from: Method:'main'
            to: Variable:'test'
            type: Reflect
            loc: file1:10:27
```

###### Retrieving Class Fields (Object.getDeclaredField())

```java
//// Person.java
public class Person {
    public String test;
    private String name;
    private int age;
    private static String msg="hello world";
    }
```
```java
//// ReflectDemo.java
import java.lang.reflect.Field;
import Person;

public class ReflectDemo {

    public static void main(String[] args){
        try {
            Person o = new Person();
            Class c = o.getClass();
            Field field = c.getDeclaredField("age");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```yaml
name: Retrieving Class Fields (Object.getDeclaredField())
entity:
    items:
        -   name: Person
            type: Class
            loc: file0:1:14
        -   name: age
            qualified: Person.age
            type: Variable
            loc: file0:4:17
        -   name: ReflectDemo
            type: Class
            loc: file1:4:14
        -   name: main
            qualified: ReflectDemo.main
            type: Method
            loc: file1:6:24
relation:
    items:
        -   from: Method:'main'
            to: Variable:'age'
            type: Reflect
            loc: file1:10:27
```