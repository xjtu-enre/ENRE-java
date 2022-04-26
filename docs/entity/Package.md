# Entity : Package
A `package entity` is a namespace which bundles types together and arranges file system so that compiler can find source files.
## Supported pattern
```yaml
name : PackageDeclaration
```
### Syntax : Package Definitions
```yaml
Package Declaration:
    [ Javadoc ] { Annotation } package Name ;
```
### Examples : 
- Package declaration (Single)
```java
package hello;
```
```yaml
name: Package Declaration
entities:
    filter: package
    items:
        -   name: hello
            qualifiedName : hello
            id : 0
```
- Package declaration (Multiple)
```java
package hello.test;
```
```yaml
name: Package Declaration
entities:
    filter: package
    items:
        -   name: hello
            qualifiedName : hello
            id : 0
            childrenIds : [1]
        -   name: hello
            qualifiedName : hello
            id : 1
```
