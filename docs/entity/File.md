# Entity : File

The `.java` files which save the whole java information

## Supported pattern

```yaml
name: File
```

### Syntax: Null

```text
While you create a type, first you need to create a file.
```

#### Examples:

* Create A File

```java
//Foo.java
package hello;

public class Foo{

}

class Bar{

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
            qualifiedName : hello.Foo.java
```
