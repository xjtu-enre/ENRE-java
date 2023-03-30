## Dependency: Contain

A Package contains files, or a file contains types.

### Supported Patterns

```yaml
name: Contain
```

#### Syntax: Contain Definitions

```text
AnnotationTypeDeclaration:
   [ Javadoc ] { ExtendedModifier } @ interface Identifier
                { { AnnotationTypeBodyDeclaration | ; } }
Interface Declaration:
      [ Javadoc ] { ExtendedModifier } interface Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { InterfaceBodyDeclaration | ; } }
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
EnumDeclaration:
     [ Javadoc ] { ExtendedModifier } enum Identifier
         [ implements Type { , Type } ]
         {
         [ EnumConstantDeclaration { , EnumConstantDeclaration } ] [ , ]
         [ ; { ClassBodyDeclaration | ; } ]
         }
```

##### Examples

###### Package contains package

```java
package hello.pkg;
```

```yaml
name: Package Contains Package
entity:
    items:
        -   name: hello
            type : Package
            qualified: hello
            loc: 1:9
        -   name: pkg
            type: Package
            qualified: hello.pkg
            loc: 1:15
relation:
    items:
        -   from: Package:'hello'
            to: Package:'pkg'
            type: Contain
            loc: file0:1:9

```

###### Package contains files
```java
//// Hello.java
package pkg;

public class Hello {

}
```

```yaml
name: Package contains Files
entity:
    items:
        -   name: Hello.java
            type : File
            qualified: Hello.java
            loc: file0
        -   name: pkg
            type : Package
            qualified: pkg
            loc: 1:9
relation:
    items:
        -   from: Package:'pkg'
            to: File:'Hello.java'
            type: Contain
            loc: file0:1:9
```

###### File contains class(es)

```java
//// Hello.java
public class Hello {
    /* ... */
}

class Test {
    /* ... */
}
```

```yaml
name: File Contains Classes
entity:
    items:
        -   name: Hello.java
            type : File
            qualified: Hello.java
            loc: file0
        -   name: Hello
            type : Class
            loc: 1:14
        -   name: Test
            type : Class
            loc: 5:7
relation:
    items:
        -   from: File:'Hello.java'
            to: Class:'Hello'
            type: Contain
            loc: file0:1:14
        -   from: File:'Hello.java'
            to: Class:'Test'
            type: Contain
            loc: file0:5:7
```

###### File contains enum(s)

```java
//// Hello.java
public enum Hello {
    /* ... */
}
```

```yaml
name: File Contains Enum
entity:
    items:
        -   name: Hello.java
            type: File
            qualified: Hello.java
            loc: file0
        -   name: Hello
            type: Enum
            loc: 1:13
relation:
    items:
        -   from: File:'Hello.java'
            to: Enum:'Hello'
            type: Contain
            loc: file0:1:13
```

###### File contains interface(s)

```java
//// Hello.java
public interface Hello {
    /* ... */
}
```

```yaml
name: File Contains Interface
entity:
    items:
        -   name: Hello.java
            type: File
            qualified: Hello.java
            loc: file0
        -   name: Hello
            type: Interface
            loc: 1:18
relation:
    items:
        -   from: File:'Hello.java'
            to: Interface:'Hello'
            type: Contain
            loc: file0:1:18
```

###### File contains annotation(s)

```java
//// Hello.java
@interface Hello {
    /* ... */
}
```

```yaml
name: File Contains Annotation
entity:
    items:
        -   name: Hello.java
            type : File
            qualified: Hello.java
            loc: file0
        -   name: Hello
            type : Annotation
            loc: 1:12
relation:
    items:
        -   from: File:'Hello.java'
            to: Annotation:'Hello'
            type: Contain
            loc: file0:1:12
```

###### File contains record

```java
//// Hello.java
public record User(int x, String y) { }
```

```yaml
name: File Contains Record
entity:
    items:
        -   name: Hello.java
            type : File
            qualified: Hello.java
            loc: file0
        -   name: User
            type : record
            loc: 5:15
relation:
    items:
        -   from: File:'Hello.java'
            to: Record:'User'
            type: Contain
            loc: file0:5:15
```