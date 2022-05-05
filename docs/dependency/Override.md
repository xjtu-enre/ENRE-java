# Dependency: Override

A type override its super class method, which has the same name, return and parameter type of super class's method.

## Supported pattern

```yaml
name: Override
```
### Syntax:

```text
Override:
    public SuperClass{ void method() }
    public Class extends SuperClass { void method() }
```

#### Examples:

* Override Superclass Method

```java
// AbstractException.java
public abstract class AbstractException {
    public abstract String getStatus();
}
```

```java
// AuthenticationException.java
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
            category : Class
            modifiers: public abstract
            File: AbstractException.java
        -   name: AuthenticationException
            category : Class
            qualifiedName: AuthenticationException
            modifiers: public
            File: AuthenticationException.java
        -   name: getStatus
            category : Method
            qualifiedName: AbstractException.getStatus
            File: AbstractException.java
        -   name: getStatus
            category : Method
            qualifiedName: AuthenticationException.getStatus
            File: AuthenticationException.java 
relation:
    items:
        -   src: file1/getStatus
            dest: file0/getStatus
            category: Override
            r:
                d: x
                e: .
                s: .
                u: .
```

* NOT overriding parent's method

`Override` is forcefully conform to some conditions to be performed, i.e. the method signature has to be as equal or a sub-signature.

```java
// Foo.java
public class Foo {
    public void doThings() {}
    public Foo doNeatThings() {}
    public int failed() {}
```

```java
// Bar.java
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
            category: Class
        -   name: doThings
            category: Method
        -   name: doNeatThings
            category: Method
        -   name: failed
            category: Method
        -   name: Bar
            category: Class
        -   name: doThings
            category: Method
        -   name: doNeatThings
            category: Method
        -   name: failed
            category: Method
relation:
    items:
        -   src: file1/doThings
            dest: file0/doThings
            category: Override
            r:
                d: x
                e: .
                s: .
                u: .
        -   src: file1/doNeatThings
            dest: file0/doNeatThings
            category: Override
            r:
                d: x
                e: x
                s: .
                u: .
        -   src: file1/failed
            dest: file0/failed
            category: Override
            negative: true
            r:
                d: x!
                e: x
                s: .
                u: .
```
