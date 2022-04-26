# Entity : File
The `.java` files which save the whole java information
### Examples : 
- File created
```java
package hello;

class foo{

}
```
```yaml
entities:
    filter: file
    items:
        -   name: foo.java
            qualifiedName: hello.foo.java
            File: hello/foo.java
```
