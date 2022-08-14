## Dependency: Implement

A type(class or enum) implements interfaces.

### Supported Patterns

```yaml
name: Implement
```

#### Syntax: Implement Definitions

```text
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```

##### Examples

###### A class implements an interface

```java
//// BaseService.java
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
            type : Class
            loc: 7:14
            modifiers: public
        -   name: Bed
            type : Interface
            loc: 3:11
            modifiers: public
relation:
    items:
        -   from: Class:'BaseService'
            to: Interface:'Bed'
            type: Implement
            loc: file0:7:37
```

###### An enum implements an interface

```java
//// BaseService.java
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
            type : Enum
            loc: 7:13
            modifiers: public
        -   name: Bed
            type : Interface
            loc: 3:11
            modifiers: public
relation:
    items:
        -   from: Enum:'BaseService'
            to: Interface:'Bed'
            type: Implement
            loc: file0:7:36
```

###### A class implements multiple interfaces

```java
//// BaseService.java
package hello;

interface Photo{
    /* ... */
}

interface Drink{
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
            type : Class
            loc: 11:14
            modifiers: public
        -   name: Photo
            type : Interface
            loc: 3:11
            modifiers: public
        -   name: Drink
            type : Interface
            loc: 7:11
            modifiers: public
relation:
    items:
        -   from: Class:'BaseService'
            to: Interface:'Photo'
            type: Implement
            loc: file0:11:37
        -   from: Class:'BaseService'
            to: Interface:'Drink'
            type: Implement
            loc: file0:11:44
```

###### An enum implements multiple interfaces

```java
//// BaseService.java
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
            type : Enum
            loc: 11:13
            modifiers: public
        -   name: Photo
            type : Interface
            loc: 3:11
            modifiers: public
        -   name: Drink
            type : Interface
            loc: 7:11
            modifiers: public
relation:
    items:
        -   from: Enum:'BaseService'
            to: Interface:'Photo'
            type: Implement
            loc: file0:11:36
        -   from: Enum:'BaseService'
            to: Interface:'Drink'
            type: Implement
            loc: file0:11:43
```

###### A record implements an interface

```java
//// BaseService.java
package hello;

interface Photo {
    /* ... */
}

public record BaseService(int x, String y) implements Photo{
    /* ... */
}
```

```yaml
name: record Implements Multiple Interfaces
entity:
    items:
        -   name: BaseService
            type : record
            loc: 7:15
            modifiers: public
        -   name: Photo
            type : Interface
            loc: 3:11
            modifiers: public
relation:
    items:
        -   from: Record:'BaseService'
            to: Interface:'Photo'
            type: Implement
            loc: file0:6:55
```