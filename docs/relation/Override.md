## Dependency: Override

A type override its super class method, which has the same name, return and parameter type of super class's method.

### Supported Patterns

```yaml
name: Override
```
#### Syntax: Override Definitions

```text
Override:
    public SuperClass{ void method() }
    public Class extends SuperClass { void method() }
```

##### Examples

###### Override Superclass Method

```java
//// AbstractException.java
public abstract class AbstractException {
    public abstract String getStatus();
}
```

```java
//// AuthenticationException.java
import AbstractException;
public class AuthenticationException extends AbstractException {
    @Override
    public String getStatus() {
        return "UNAUTHORIZED";
    }
}
```

```yaml
name: Override Superclass Method
entity:
    items:
        -   name: AbstractException
            type : Class
            modifiers: public abstract
            loc: file0:1:23
            File: AbstractException.java
        -   name: AuthenticationException
            type : Class
            qualified: AuthenticationException
            loc: file1:1:14
            modifiers: public
            File: AuthenticationException.java
        -   name: getStatus
            type : Method
            qualified: AbstractException.getStatus
            loc: file0:2:28
            File: AbstractException.java
        -   name: getStatus
            type : Method
            qualified: AuthenticationException.getStatus
            loc: file1:4:19
            File: AuthenticationException.java 
relation:
    items:
        -   from: Method:'getStatus'
            to: Method:'getStatus'
            type: Override
            loc: file1:4:19
```

###### NOT overriding parent's method

`Override` is forcefully conform to some conditions to be performed, i.e. the method signature has to be as equal or a sub-signature.

```java
//// Foo.java
public class Foo {
    public void doThings() {}
    public Foo doNeatThings() {}
    public int failed() {}
}
```

```java
//// Bar.java
import Foo;

public class Bar extends Foo {
    public void doThings() {}       // <---- Overriding

    public Bar doNeatThings() {}    // <---- Overriding, subsignature

    public void failed() {}         // <---- NOT overriding, because the signarture is not the same
}
```

```yaml
name: The Condition Of Overriding
entity:
    items:
        -   name: Foo
            type: Class
            loc: file0:1:14
        -   name: doThings
            type: Method
            loc: file0:2:17
        -   name: doNeatThings
            type: Method
            loc: file0:3:16
        -   name: failed
            type: Method
            loc: file0:4:16
        -   name: Bar
            type: Class
            loc: file1:3:14
        -   name: doThings
            type: Method
            loc: file1:4:17
        -   name: doNeatThings
            type: Method
            loc: file1:6:16
        -   name: failed
            type: Method
            loc: file1:8:17
relation:
    items:
        -   from: Method:'doThings'
            to: Method:'doThings'
            type: Override
            loc: file1:4:17
        -   from: Method:'doNeatThings'
            to: Method:'doNeatThings'
            type: Override
            loc: file1:6:16
        -   from: Method:'failed'
            to: Method:'failed'
            type: Override
            loc: file1:8:17
            negative: true
```
