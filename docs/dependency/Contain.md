# Dependency: Contain
A Package contains files, or a file contains types.
## Supported pattern
```yaml
name: 
    - ClassDeclaration
    - InterfaceDeclaration
    - EnumDeclaration
    - AnnotationTypeDeclaration
```
### Syntax : 
```yaml
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
### Examples : 
- Package contains package
```java
package hello.pkg;
```
```yaml
name: Package contains package
entities:
    items:
        -   name: hello
            category : Package
            qualifiedName: hello
        -   name: pkg
            category : Package
            qualifiedName: hello.pkg
dependencies: 
        -   src: hello
            dest: pkg
            kind: Contain
```
- Package contains files
```java
package hello;


```
```yaml
name: Package contains package
entities:
    items:
        -   name: hello
            category : Package
            qualifiedName: hello
        -   name: pkg
            category : Package
            qualifiedName: hello.pkg
dependencies: 
        -   src: hello
            dest: pkg
            kind: Contain
```
- File contains class(es)
```java
//Hello.java
public class Hello{

}

class Test{

}
```
```yaml
name: File contains classes
entities:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Class
        -   name: Test
            category : Class
dependencies: 
        -   src: Hello
            dest: Hello/Class[0]
            kind: Contain
        -   src: Hello
            dest: Hello/Class[1]
            kind: Contain
```
- File contains enum(s)
```java
//Hello.java
public enum Hello{

}
```
```yaml
name: File contains enum
entities:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Enum
dependencies: 
        -   src: Hello
            dest: Hello/Enum[0]
            kind: Contain
```
- File contains interface(s)
```java
//Hello.java
public interface Hello{

}
```
```yaml
name: File contains interface
entities:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Interface
dependencies: 
        -   src: Hello
            dest: Hello/Interface[0]
            kind: Contain
```
- File contains annotation(s)
```java
//Hello.java
@interface Hello{

}
```
```yaml
name: File contains Annotation
entities:
    items:
        -   name: Hello.java
            category : File
            qualifiedName: Hello.java
        -   name: Hello
            category : Annotation
dependencies: 
        -   src: Hello
            dest: Hello/Annotation[0]
            kind: Contain
```