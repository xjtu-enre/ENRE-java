# Dependency: Implement
A type(class or enum) implements interfaces.
## Supported pattern
```yaml
name : Implement
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
name: A class implements an interface
entity:
    items:
        -   name: BaseService
            category : Class
            modifiers: public
        -   name: Bed
            category : Interface
            modifiers: public
relation:
    items:
        -   src: file0/Class[0]
            dest: file1/Interface[0]
            category: implement
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
name: An enum implements an interface
entity:
    items:
        -   name: BaseService
            category : Enum
            modifiers: public
        -   name: Bed
            category : Interface
            modifiers: public
relation:
    items:
        -   src: file0/Enum[0]
            dest: file1/Interface[0]
            category: implement
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
name: A class implements multiple interfaces
entity:
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
relation:
    items:
        -   src: file0/Class[0]
            dest: file1/Interface[0]
            category: implement
        -   src: file0/Class[0]
            dest: file2/Interface[0]
            category: implement
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
name: A enum implements multiple interfaces
entity:
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
relation:
    items:
        -   src: file0/Enum[0]
            dest: file1/Interface[0]
            category: implement
        -   src: file0/Enum[0]
            dest: file2/Interface[0]
            category: implement
```