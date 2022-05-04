# Dependency: Override
A type override its super class method, which has the same name, return and parameter type of super class's method.
## Supported pattern
```yaml
name : Override
```
### Syntax : 
```yaml
Override:
    public SuperClass{ void method() }
    public Class extends SuperClass { void method() }
```
### Examples : 
- Override Superclass Method
```java
//AbstractException.java
public abstract class AbstractException {

    public abstract HttpStatus getStatus();
}
```
```java
//AuthenticationException.java
import AbstractException;
public class AuthenticationException extends AbstractException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
```
```yaml
name: Override Superclass Method
entities:
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
dependencies:
    items:
        -   src: AuthenticationException/Method[0]
            dest: AbstractException/Method[0]
            category: Override
```