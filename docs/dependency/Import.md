# Dependency: Import
A file imports other class, enum or package, or static imports method or var.
## Supported pattern
```yaml
name : ImportDeclaration
```
### Syntax : 
```yaml
ImportDeclaration:
    import [ static ] Name [ . * ] ;
```
### Examples : 
- Import declaration (unknown package)
```java
// file1.java
class Foo {
    public void doThings(){}
    public int failed(){}
}
```
```java
//file2.java
import Foo;

class Bar extends Foo { 
    // Overriding Foo.doThings
    public void doThings(){
    
    }
       
}

```
```yaml
scenario: Import Class
entities:
    items:
        -   name: Foo
            category : Class
            loc: @file1/[ 2, 0, 5, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: Bar
            category : Class
            loc: @file2/[ 2, 0, 8, 0 ]
            rawType: Bar
            qualifiedName: Bar
dependencies: 
        -   src: @file2
            dest: @file1/@class[0]
            kind: import
```
- Import class (known package)
```java
//file1
package test_package2;

import test_package1.Name;

public class Hello {
    public static void main(String[] args){
        Name name = new Name();
        System.out.println("Hello "+ name.getIt());

    }
}
```
```java
//file2
package test_package1;

public class Name {
    public String getIt(){
        return "Java World";
    }
}
```
```yaml
scenario: Import Class
entities:
    items:
        -   name: Name
            category : Class
            loc: @file2/[ 4, 0, 8, 0 ]
            rawType: test_package1.Name
            qualifiedName: test_package1.Name
        -   name: Hello.java
            category : File
            qualifiedName: test_package2.Hello.java
dependencies: 
        -   src: @file1
            dest: @file2/@Class[1]
            kind: import
```
- Import Static
```java
// file1.java
class Foo {
    public static final String MSG = "msg";

    public void doThings(){}
    public int failed(){}
}
```
```java
//file2.java
import Foo.MSG;
import Foo;

class Bar extends Foo { 
    // Overriding Foo.doThings
    public void doThings(){
        System.out.println(MSG);
    }
       
}
```
```yaml
scenario: Import Static
entities:
    items:
        -   name: Foo
            category : Class
            loc: @file1/[ 2, 0, 7, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: MSG
            category : Variable
            rawType: String
            qualifiedName: Foo.MSG
        -   name: Bar
            category : Class
            loc: @file2/[ 2, 0, 11, 0 ]
            rawType: Bar
            qualifiedName: Bar
dependencies: 
        -   src: @file2
            dest: @file1/@variable[0]
            kind: import
```
- Import On Demand
```java
package helloJDT.pkg;
```
```java
//HelloJDT.java
package helloJDT;

import helloJDT.pkg.*;
import java.lang.reflect.Method;

public class HelloJDT implements JDTpkg_2 {
    String str1 = "hello JDT";
    String str2 ;

    public HelloJDT(String name){
        this.str2 = name;
    }

    public void hello(){
        System.out.println(this.str1);
    }

    public void hello(String name){
        System.out.println("This is " + name);
    }
}
```
```yaml
scenario: Import on demand
entities:
    items:
        -   name: HelloJDT.java
            category : File
            qualifiedName: helloJDT.HelloJDT.java
        -   name: helloJDT.pkg
            category : Package
            qualifiedName: helloJDT.pkg
dependencies: 
        -   src: @HelloJDT.java
            dest: helloJDT.pkg
            kind: import
```