# Dependency: Modify

A variable, which is just set a value before, is modified by a method.

## Supported pattern

```yaml
name: Modify
```

### Syntax: 

```text
Assignment:
    class var = some_value;
    var = value;
```

#### Examples:

* Modify a field

```java
// Foo.java
public class Foo {
    String type = "class";
    
    public void changeType() {
        type = "method";
    }
}
```

```yaml
name: Modify Field
entity:
    items:
        -   name: Foo
            category : Class
        -   name: type
            category : Variable
            qualifiedName: Foo.type
        -   name: changeType
            category : Method
            qualifiedName: Foo.changeType
relation:
    items:
        -   src: file0/changeType
            dest: file0/type
            category: Modify
            r:
                d: Use
                e: UseVar
                s: Use
                u: Set
```

* Modify Local Var

```java
//Foo.java
public class Foo {
    public String changeType(int i) {
        String type = "num";
        if (i > 0){
            type = "positive";
        }else {
            type = "negative";
        }
        return type;
    }
}
```

```yaml
name: Modify Local Var
entity:
    items:
        -   name: Foo
            category : Class
        -   name: type
            category : Variable
            qualifiedName: Foo.changeType.type
        -   name: changeType
            category : Method
            qualifiedName: Foo.changeType
relation:
    items:
        -   src: file0/changeType
            dest: file0/type
            category: Modify
            r:
                d: Use
                e: x
                s: x
                u: Set
```
