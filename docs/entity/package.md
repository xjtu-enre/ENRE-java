# Entity : Package
A `package entity` is a namespace which bundles types together and arranges file system so that compiler can find source files.
## Supported pattern
```
name : PackageDeclaration
```
### Syntax : Package Definitions
```
Package Declaration:
    [ Javadoc ] { Annotation } package Name ;
```
### Examples : 
- Package declaration
```java
package hello;
```
```yaml
name: Package Declaration
entities:
    filter: package
    items:
        -   name: hello
            loc: [ 1, 1 ]
```
