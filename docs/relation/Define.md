## Dependency: Define
A type define fields or methods, a method defines variables.
### Supported Patterns
```yaml
name: Define
```
#### Syntax: Define Definitions
```text
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
##### Examples
###### Define a method (class)
```java
//// BaseService.java
package hello;

public class BaseService {

    public void laundry(){
        /* ... */
    }

}
```
```yaml
name: A class defines a method
entity:
    items:
        -   name: BaseService
            type : Class
            loc: 3:14
            modifiers: public
        -   name: laundry
            type : Method
            loc: 5:17
            modifiers: public
relation: 
    items:
        -   from: Class:'BaseService'
            to: Method:'laundry'
            type: Define
            loc: file0:5:17
```
###### Define a method (record)
```java
//// BaseService.java
package hello;

public record BaseService(int x) {

    public void laundry(){
        /* ... */
    }

}
```
```yaml
name: A record defines a method
entity:
    items:
        -   name: BaseService
            type : Record
            loc: 3:15
            modifiers: public
        -   name: laundry
            type : Method
            loc: 5:17
            modifiers: public
relation: 
    items:
        -   from: Record:'BaseService'
            to: Method:'laundry'
            type: Define
            loc: file0:5:17
```

###### Define a method (interface)
```java
//// BaseService.java
package hello;

public interface BaseService {

    void laundry();

}
```
```yaml
name: An interface defines a method
entity:
    items:
        -   name: BaseService
            type : Interface
            loc: 3:18
            modifiers: public
        -   name: laundry
            type : Method
            loc: 5:10
relation: 
    items:
        -   from: Interface:'BaseService'
            to: Method:'laundry'
            type: Define
            loc: file0:5:10
```
###### Define a field (global variable)
```java
//// BaseService.java
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
            type : Class
            loc: 3:14
            modifiers: public
        -   name: MSG
            type : Variable
            loc: 5:32
            modifiers: public static final
relation: 
    items:
        -   from: Class:'BaseService'
            to: Variable:'MSG'
            type: Define
            loc: file0:5:32
```
###### Define a variable (local variable)
```java
//// BaseService.java
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
            type : method
            loc: 7:17
            modifiers: public
        -   name: MSG
            type : Variable
            loc: 5:32
            modifiers: public static final
        -   name: clothes
            type : Variable
            loc: 9:16
            rawType: String
relation: 
    items:
        -   from: Method:'laudry'
            to: Variable:'clothes'
            type: Define
            loc: file0:9:16
```

###### Define an enumconstant
```java
//// AttachmentType.java
public enum AttachmentType  {
    
    /**
     * 服务器
     */
    LOCAL(0),

    /**
     * MINIO
     */
    MINIO(8);

    int num;
    
    public AttachmentType(int i){
        this.num = i;
    }
}
```
```yaml
name: A enum defines enumconstants
entity:
    items:
        -   name: AttachmentType
            type : Enum
            loc: 1:13
            modifiers: public
        -   name: LOCAL
            type : EnumConstant
            loc: 6:5
        -   name: MINIO
            type: EnumConstant
            loc: 11:5
relation: 
    items:
        -   from: Enum:'AttachmentType'
            to: EnumConstant:'LOCAL'
            type: Define
            loc: file0:6:5
        -   from: Enum:'AttachmentType'
            to: EnumConstant:'MINIO'
            type: Define
            loc: file0:11:5
```
###### Define an annotationmember
```java
//// DisableOnCondition.java
public @interface DisableOnCondition {
    
    String value() default "Mode.DEMO";
    
    String mode() default "Mode.DEMO";
}
```
```yaml
name: An annotation defines annotationmembers
entity:
    items:
        -   name: DisableOnCondition
            type: Annotation
            modifiers: public
            loc: 1:19
        -   name: value
            type: AnnotationMember
            loc: 3:12
        -   name: mode
            type: AnnotationMember
            loc: 5:12
relation: 
    items:
        -   from: Annotation:'DisableOnCondition'
            to: AnnotationMember:'value'
            type: Define
            loc: file0:3:12
        -   from: Annotation:'DisableOnCondition'
            to: AnnotationMember:'mode'
            type: Define
            loc: file0:5:12
```

###### Define a variable (local variable)
```java
//// BaseService.java
public record BaseService(int x) {
    /*...*/
}
```
```yaml
name: A record defines a variable
entity:
    items:
        -   name: BaseService
            type : Record
            loc: 1:15
        -   name: x
            type : Variable
            loc: 1:31
            rawType: int
relation: 
    items:
        -   from: Record:'Baseservice'
            to: Variable:'x'
            type: Define
            loc: file0:1:31
```