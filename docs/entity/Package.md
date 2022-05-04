# Entity: Package

A `package entity` is a namespace which bundles types together and arranges file system so that compiler can find source files.

## Supported pattern

```yaml
name: Package
```

### Syntax: Package Definitions

```text
Package Declaration:
    [ Javadoc ] { Annotation } package Name ;
```

#### Examples:

* Package declaration (Single)

```java
package foo;
```

```yaml
name: Single Package Declaration
entity:
    filter: Package
    r:
        d: .
        e: .
        s: .
        u: .
    items:
        -   name: foo
            qualifiedName: foo
```

* Package declaration (Multiple)

```java
package foo.bar;
```

```yaml
name: Multilayer Package Declaration
entity:
    filter: Package
    items:
        -   name: foo
            qualifiedName: foo
            r:
                d: x
                e: .
                s: .
                u: .
        -   name: bar
            qualifiedName: foo.bar
            r:
                d: .
                e: .
                s: .
                u: .
```
