## Dependency: Import
A file imports other class, enum or package, or static imports method or var.
### Supported Patterns
```yaml
name : Import
```
#### Syntax: Import Definitions
```text
ImportDeclaration:
    import [ static ] Name [ . * ] ;
```
##### Examples
###### Import declaration (unknown package)
```java
//// Foo.java
class Foo {
    public void doThings(){}
    public int failed(){}
}
```
```java
//// Bar.java
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
            type : Class
            loc: file0:1:7
            rawType: Foo
            qualified: Foo
        -   name: Bar
            type : Class
            loc: file1:3:7
            rawType: Bar
            qualified: Bar
relation:
    items:
        -   from: File:'file0'
            to: Class:'Bar'
            type: import
            loc: file1
            negative: true
```
###### Import class (known package)
```java
//// test_pkg2/Hello.java
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
//// test_pkg1/Name.java
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
            type : Class
            loc: file1:3:14
            rawType: test_package1.Name
            qualified: test_package1.Name
        -   name: Hello.java
            type : File
            qualified: test_package2.Hello.java
            loc: file0
relation:
    items:
        -   from: File:'file0'
            to: Class:'Name'
            type: import
            loc: file0:3:8
```
###### Import Static Var
```java
//// Foo.java
class Foo {
    public static final String MSG = "msg";

    public void doThings(){}
    public int failed(){}
}
```
```java
//// Bar.java
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
            type : Class
            loc: file0:1:7
            rawType: Foo
            qualified: Foo
        -   name: MSG
            type : Variable
            rawType: String
            qualified: Foo.MSG
            loc: file0:2:32
        -   name: Bar
            type : Class
            loc: file1:4:7
            rawType: Bar
            qualified: Bar
relation:
    items:
        -   from: File:'file1'
            to: Variable:'MSG'
            type: import
            loc: file1:7:28
```
###### Import On Demand
```java
//// helloJDT/pkg/JDTpkg_2.java
package helloJDT.pkg;

interface JDTpkg_2 {

}
```
```java
//// helloJDT/HelloJDT.java
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
            type : File
            qualified: helloJDT.HelloJDT.java
            loc: file1
        -   name: helloJDT.pkg
            type : Package
            qualified: helloJDT.pkg
            loc: file0:1:9
relation:
    items:
        -   from: File:'file0'
            to: Package:'helloJDT.pkg'
            type: import
            loc: file1:3:8
```
###### Import Enum
```java
//// BasePostMinimalDTO.java
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
//// PostStatus.java
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
            type : File
            qualified: BasePostMinimalDTO.java
            loc: file0
        -   name: PostStatus
            type : Enum
            qualified: PostStatus
            loc: file1:1:13
            rawType: PostStatus
            modifiers: public
relation:
    items:
        -   from: File:'file0'
            to: Enum:'PostStatus'
            type: import
            loc: file0:1:8
```
###### Import Annotation
```java
//// CacheParam.java
public @interface CacheParam {

}
```
```java
//// JournalController.java
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
            type : File
            loc: file1
        -   name: CacheParam
            type : Annotation
            qualified: CacheParam
            loc: file0:1:19
            rawType: CacheParam
            modifiers: public
relation:
    items:
        -   from: File:'file1'
            to: Annotation:'CacheParam'
            type: import
            loc: file1:1:8
```