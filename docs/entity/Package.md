## Entity: Package

A `package entity` is a namespace which bundles types together and arranges file system so that compiler can find source files.

### Supported Patterns

```yaml
name: Package
```

#### Syntax: Package Definitions

```text
Package Declaration:
    [ Javadoc ] { Annotation } package Name ;
```

##### Examples

###### Package declaration (Single)

```java
//// foo/Main.java
package foo;
```

```yaml
name: Single Package Declaration
entity:
    type: Package
    items:
        -   name: foo
            qualified: foo
            loc: 1:9
```

###### Package declaration (Multiple)

```java
//// foo/bar/Main.java
package foo.bar;
```

```yaml
name: Multilayer Package Declaration
entity:
    type: Package
    items:
        -   name: foo
            qualified: foo
            loc: 1:9
        -   name: bar
            qualified: foo.bar
            loc: 1:13
```
