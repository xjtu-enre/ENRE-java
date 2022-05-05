# Entity : File
The `.java` files which save the whole java information
### Examples : 
- Create a file
```java
//Foo.java
package hello;

class Foo{

}
```
```yaml
name: File Created
entity:
    filter: File
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   name: Foo.java
            qualifiedName : Foo.java
```
