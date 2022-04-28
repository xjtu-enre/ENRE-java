# Dependency: Implement
A type(class or enum) implements interfaces.
## Supported pattern
```yaml
keyword : implements
```
### Syntax : 
```yaml
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```
### Examples : 
- A class implements an interface
```java
//BaseService.java
package hello;

public class BaseService implements Bed {

}
```
```java
//Bed.java
package hello;

public interface Bed{

}
```
```yaml
scenario: A class implements an interface
entities:
    items:
        -   name: BaseService
            category : Class
            modifiers: public
        -   name: Bed
            category : Interface
            modifiers: public
dependencies: 
        -   src: @BaseService/Class[0]
            dest: @Bed/Interface[0]
            kind: implement
```
- An enum implements an interface
```java
//BaseService.java
package hello;

public enum BaseService implements Bed {

}
```
```java
//Bed.java
package hello;

public interface Bed{

}
```
```yaml
scenario: An enum implements an interface
entities:
    items:
        -   name: BaseService
            category : Enum
            modifiers: public
        -   name: Bed
            category : Interface
            modifiers: public
dependencies: 
        -   src: @BaseService/Enum[0]
            dest: @Bed/Interface[0]
            kind: implement
```
- A class implements multiple interfaces
```java
//BaseService.java
package hello;

public class BaseService implements Photo, Drink {

}
```
```java
//Photo.java
package hello;

public interface Photo{

}
```
```java
//Drink.java
package hello;

public interface Drink{

}
```
```yaml
scenario: A class implements multiple interfaces
entities:
    items:
        -   name: BaseService
            category : Class
            modifiers: public
        -   name: Photo
            category : Interface
            modifiers: public
        -   name: Drink
            category : Interface
            modifiers: public
dependencies: 
        -   src: @BaseService/Class[0]
            dest: @Photo/Interface[0]
            kind: implement
        -   src: @BaseService/Class[0]
            dest: @Drink/Interface[0]
            kind: implement
```
- An enum implements multiple interfaces
```java
//BaseService.java
package hello;

public enum BaseService implements Photo, Drink {

}
```
```java
//Photo.java
package hello;

public interface Photo{

}
```
```java
//Drink.java
package hello;

public interface Drink{

}
```
```yaml
scenario: A class implements multiple interfaces
entities:
    items:
        -   name: BaseService
            category : Enum
            modifiers: public
        -   name: Photo
            category : Interface
            modifiers: public
        -   name: Drink
            category : Interface
            modifiers: public
dependencies: 
        -   src: @BaseService/Enum[0]
            dest: @Photo/Interface[0]
            kind: implement
        -   src: @BaseService/Enum[0]
            dest: @Drink/Interface[0]
            kind: implement
```