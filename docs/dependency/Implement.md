# Dependency: Implement

A type(class or enum) implements interfaces.

## Supported pattern

```yaml
name: Implement
```

### Syntax:

```text
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```

#### Examples:

* A class implements an interface

```java
// BaseService.java
package hello;

interface Bed {
    /* ... */
}

public class BaseService implements Bed {
    /* ... */
}
```

```yaml
name: Class Implements Interface
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
        -   src: file0/BaseService
            dest: file0/Bed
            category: Implement
```

* An enum implements an interface

```java
//BaseService.java
package hello;

interface Bed {
    /* ... */
}

public enum BaseService implements Bed {
    /* ... */
}
```

```yaml
name: Enum Implements Interface
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
        -   src: file0/BaseService
            dest: file0/Bed
            category: Implement
```

* A class implements multiple interfaces

```java
//BaseService.java
package hello;

interface Photo{
    /* ... */
}

public interface Drink{
    /* ... */
}

public class BaseService implements Photo, Drink {
    /* ... */
}
```

```yaml
name: Class Implements Multiple Interfaces
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
        -   src: file0/BaseService
            dest: file0/Photo
            category: Implement
        -   src: file0/BaseService
            dest: file0/Drink
            category: Implement
```

* An enum implements multiple interfaces

```java
//BaseService.java
package hello;

interface Photo {
    /* ... */
}

interface Drink {
    /* ... */
}

public enum BaseService implements Photo, Drink {
    /* ... */
}
```

```yaml
name: Enum Implements Multiple Interfaces
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
        -   src: file0/BaseService
            dest: file0/Photo
            category: Implement
        -   src: file0/BaseService
            dest: file0/Drink
            category: Implement
```