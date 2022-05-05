# Dependency: Import
A file imports other class, enum or package, or static imports method or var.
## Supported pattern
```yaml
name : Import
```
### Syntax : 
```yaml
ImportDeclaration:
    import [ static ] Name [ . * ] ;
```
### Examples : 
- Import declaration (unknown package)
```java
//Foo.java
class Foo {
    public void doThings(){}
    public int failed(){}
}
```
```java
//Bar.java
import Foo;

class Bar extends Foo { 
    // Overriding Foo.doThings
    public void doThings(){
    
    }
       
}

```
```yaml
name: Import Class From Default Class
entity:
    items:
        -   name: Foo
            category : Class
            loc: file1/[ 2, 0, 5, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: Bar
            category : Class
            loc: file2/[ 2, 0, 8, 0 ]
            rawType: Bar
            qualifiedName: Bar
relation:
    negative: true
    items:
        -   src: file1
            dest: file0/Class[0]
            category: import
            r:
                d: x
                e: x
                s: x
                u: .
```
- Import class (known package)
```java
//test_pkg2/Hello.java
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
//test_pkg1/Name.java
package test_pkg1;

public class Name {
    public String getIt(){
        return "Java World";
    }
}
```
```yaml
name: Import Class From Explicit Package
entity:
    items:
        -   name: Name
            category : Class
            loc: file1/[ 4, 0, 8, 0 ]
            rawType: test_package1.Name
            qualifiedName: test_package1.Name
        -   name: Hello.java
            category : File
            qualifiedName: test_package2.Hello.java
relation:
    items:
        -   src: file0
            dest: file1/Class[0]
            category: import
            r:
                d: .
                e: .
                s: .
                u: .
```
- Import Static Var
```java
// Foo.java
class Foo {
    public static final String MSG = "msg";

    public void doThings(){}
    public int failed(){}
}
```
```java
//Bar.java
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
name: Import Static Var
entity:
    items:
        -   name: Foo
            category : Class
            loc: file0/[ 2, 0, 7, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: MSG
            category : Variable
            rawType: String
            qualifiedName: Foo.MSG
        -   name: Bar
            category : Class
            loc: file1/[ 2, 0, 11, 0 ]
            rawType: Bar
            qualifiedName: Bar
relation:
    items:
        -   src: file1
            dest: file0/Variable[0]
            category: import
            r:
                d: .
                e: .
                s: .
                u: .
```
- Import On Demand
```java
//helloJDT/pkg/JDTpkg_2.java
package helloJDT.pkg;

interface JDTpkg_2 {

}
```
```java
//helloJDT/HelloJDT.java
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
name: Import on demand
entity:
    items:
        -   name: HelloJDT.java
            category : File
            qualifiedName: helloJDT.HelloJDT.java
        -   name: helloJDT.pkg
            category : Package
            qualifiedName: helloJDT.pkg
relation:
    items:
        -   src: file0
            dest: helloJDT.pkg
            category: import
            r:
                d: x
                e: .
                s: .
                u: .
```
- Import Enum
```java
//BasePostMinimalDTO.java
import PostStatus;

public class BasePostMinimalDTO  {

    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;
}
```
```java
//PostStatus.java
public enum PostStatus {

    /**
     * Published status.
     */
    PUBLISHED(0),

    /**
     * Draft status.
     */
    DRAFT(1),

    /**
     * Recycle status.
     */
    RECYCLE(2),

    /**
     * Intimate status
     */
    INTIMATE(3);

    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

```
```yaml
name: Import enum
entity:
    items:
        -   name: BasePostMinimalDTO.java
            category : File
            qualifiedName: BasePostMinimalDTO.java
        -   name: PostStatus
            category : Enum
            qualifiedName: PostStatus
            rawType: PostStatus
            modifiers: public
relation:
    items:
        -   src: file0
            dest: file1/Enum[0]
            category: import
            r:
                d: .
                e: .
                s: .
                u: .
```
- Import Annotation
```java
//CacheParam.java
public @interface CacheParam {

}
```
```java
//JournalController.java
import CacheParam;

public class JournalController {

    public void like(@CacheParam Integer id) {
        /* ... */
    }
}
```
```yaml
name: Import annotation
entity:
    items:
        -   name: JournalController.java
            category : File
        -   name: CacheParam
            category : Annotation
            qualifiedName: CacheParam
            rawType: CacheParam
            modifiers: public
relation:
    items:
        -   src: file1
            dest: file0/Annotation[0]
            category: import
            r:
                d: .
                e: .
                s: .
                u: .
```