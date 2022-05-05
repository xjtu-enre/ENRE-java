# Dependency: Contain

A Package contains files, or a file contains types.

## Supported pattern

```yaml
name: Contain
```

### Syntax: 

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

#### Examples:

* Package contains package

```java
package hello.pkg;
```

```yaml
name: Package Contains Package
entity:
    items:
        -   name: hello
            category : Package
            qualifiedName: hello
        -   name: pkg
            category : Package
            qualifiedName: hello.pkg
relation:
    items:
        -   src: file0/hello
            dest: fil0/pkg
            category: Contain
            r:
                d: x
                e: .
                s: x
                u: Declare
```

- Package contains files
```java
// Hello.java
package pkg;

public class Hello {

}
```

```yaml
name: Package contains Files
entity:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: pkg
            category : Package
            qualifiedName: pkg
relation:
    items:A
        -   src: file0/pkg
            dest: file0
            category: Contain
            r:
                d: x
                e: .
                s: x
                u: x
```

* File contains class(es)

```java
// Hello.java
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
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Class
        -   name: Test
            category : Class
relation:
    r:
        d: x
        e: .
        s: x
        u: Define
    items:
        -   src: file0
            dest: file0/Hello
            category: Contain
        -   src: file0
            dest: file0/Test
            category: Contain
```

* File contains enum(s)

```java
// Hello.java
public enum Hello {
    /* ... */
}
```

```yaml
name: File Contains Enum
entity:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Enum
relation:
    r:
        d: x
        e: .
        s: x
        u: Define
    items:
        -   src: file0
            dest: file0/Hello
            category: Contain
```

* File contains interface(s)

```java
// Hello.java
public interface Hello {
    /* ... */
}
```

```yaml
name: File Contains Interface
entity:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Interface
relation:
    r:
        d: x
        e: .
        s: x
        u: Define
    items:
        -   src: file0
            dest: file0/Interface[0]
            category: Contain
```

* File contains annotation(s)

```java
// Hello.java
@interface Hello {
    /* ... */
}
```

```yaml
name: File Contains Annotation
entity:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Annotation
relation:
    r:
        d: x
        e: .
        s: x
        u: Define
    items:
        -   src: file0
            dest: file0/Hello
            category: Contain
```
