# Dependency: Define
A type define fields or methods, a method defines variables.
## Supported pattern
```yaml
name: Define
```
### Syntax : 
```yaml
MethodDeclaration:
[ Javadoc ] { ExtendedModifier } [  TypeParameter { , TypeParameter } ] ( Type | void )
        Identifier (
            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
        ) { Dimension }
        [ throws Type { , Type } ]
        ( Block | ; )

VariableDeclaration: 
{ ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment }
FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;
```
### Examples : 
- Define a method (class)
```java
//BaseService.java
package hello;

public class BaseService {

    public void laundry(){
    }

}
```
```yaml
name: A class defines a method
entity:
    items:
        -   name: BaseService
            category : Class
            modifiers: public
        -   name: laundry
            category : Method
            modifiers: public
relation: 
    items:
        -   src: file0/Class[0]
            dest: file0/Method[0]
            category: Define
```
- Define a method (interface)
```java
//BaseService.java
package hello;

public interface BaseService {

    void laundry(){}

}
```
```yaml
name: An interface defines a method
entity:
    items:
        -   name: BaseService
            category : Interface
            modifiers: public
        -   name: laundry
            category : Method
relation: 
    items:
        -   src: file0/Interface[0]
            dest: file0/Method[0]
            category: Define
```
- Define a field (global variable)
```java
//BaseService.java
package hello;

public class BaseService {

    public static final String MSG = "OnCall";

}
```
```yaml
name: A class defines a field
entity:
    items:
        -   name: BaseService
            category : Class
            modifiers: public
        -   name: MSG
            category : Variable
            modifiers: public static final
relation: 
    items:
        -   src: file0/Class[0]
            dest: file0/Variable[0]
            category: Define
```
- Define a variable (local variable)
```java
//BaseService.java
package hello;

public class BaseService {

    public static final String MSG = "OnCall";

    public void laundry(){
        
        String clothes;
    }
}
```
```yaml
name: A method defines a variable
entity:
    items:
        -   name: laundry
            category : method
            modifiers: public
        -   name: MSG
            category : Variable
            modifiers: public static final
        -   name: clothes
            category : Variable
            rawType: String
relation: 
    items:
        -   src: file0/Method[0]
            dest: file0/Variable[1]
            category: Define
```
- Define an enum constant
```java
//AttachmentType.java
public enum AttachmentType implements ValueEnum<Integer> {

    /**
     * 服务器
     */
    LOCAL(0),

    /**
     * MINIO
     */
    MINIO(8);
}
```
```yaml
name: A enum defines enum constants
entity:
    items:
        -   name: AttachmentType
            category : Enum
            modifiers: public
        -   name: LOCAL
            category : Enum Constant
        -   name: MINIO
            category : Enum Constant
relation: 
    items:
        -   src: file0/Enum[0]
            dest: file0/Enum Constant[0]
            category: Define
        -   src: file0/Enum[0]
            dest: file0/Enum Constant[1]
            category: Define
```
- Define an annotation member
```java
//DisableOnCondition.java
package run.halo.app.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import run.halo.app.model.enums.Mode;

/**
 * 该注解可以限制某些条件下禁止访问api
 *
 * @author 
 * @date 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableOnCondition {
    @AliasFor("mode")
    Mode value() default Mode.DEMO;

    @AliasFor("value")
    Mode mode() default Mode.DEMO;
}
```
```yaml
name: An annotation defines annotation members
entity:
    items:
        -   name: DisableOnCondition
            category : Annotation
            modifiers: public
            loc: [ 12, 0, 27, 0 ]
        -   name: value
            category : Annotation Member
            loc: [ 22, 4, 23, 34 ]
        -   name: mode
            category : Annotation Member
            loc: [ 25, 4, 26, 33 ]
relation: 
    items:
        -   src: file0/Annotation[0]
            dest: file0/Annotation Member[0]
            category: Define
        -   src: file0/Enum[0]
            dest: file0/Annotation Member[1]
            category: Define
```