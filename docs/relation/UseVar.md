## Dependency: UseVar

An entity uses a var in its scope, which could be a local var, a field or a parameter.

### Supported Patterns

```yaml
name: UseVar
```

#### Syntax: UseVar Definitions

```text
UseVar:
    class {
        var;
        method {
          var
        }
    }
```

##### Examples

###### Method Uses Local Var

```java
//// Hello.java
public class Hello {
    public void getter(int num) {
        int i = 0;
        if (num > 0) {
            i = i + num;
        } else {
            /* ... */
        }
    }
}
```

```yaml
name: Method Uses Local Var
entity:
    items:
        -   name: Hello
            type: Class
            loc: 1:14
        -   name: getter
            qualified: Hello.getter
            type: Method
            loc: 2:17
        -   name: num
            qualified: Hello.getter.num
            type: Variable
            loc: 2:28
        -   name: i
            qualified: Hello.getter.i
            type: Variable
            loc: 3:13
relation:
    type: UseVar
    extra: false
    items:
        -   from: Method:'getter'
            to: Variable:'num'
            loc: file0:4:13
        -   from: Method:'getter'
            to: Variable:'num'
            loc: file0:5:21
        -   from: Method:'getter'
            to: Variable:'i'
            loc: file0:5:17
```

###### Method Uses Field (By This)

```java
//// Hello.java
public class Hello {
    int i = 0;
    public void getter(int num) {
        if (num > 0) {
            this.i = this.i + num;
        } else {
            /* ... */
        }
    }
}
```

```yaml
name: Method Uses Field By This
entity:
    items:
        -   name: Hello
            type: Class
            loc: 1:14
        -   name: getter
            qualified: Hello.getter
            type: Method
            loc: 3:17
        -   name: num
            qualified: Hello.getter.num
            type: Variable
            loc: 3:28
        -   name: i
            qualified: Hello.i
            type: Variable
            loc: 2:9
relation:
    type: UseVar
    extra: false
    items:
        -   from: Method:'getter'
            to: Variable:'num'
            type: UseVar
            loc: file0:4:13
        -   from: Method:'getter'
            to: Variable:'i'
            type: UseVar
            loc: file0:5:27
        -   from: Method:'getter'
            to: Variable:'num'
            type: UseVar
            loc: file0:5:31
```

###### Method Uses Field

```java
//// Hello.java
public class Hello {
    int i = 0;
    public void getter(int num) {
        if (num > 0) {
            i = i + num;
        } else {
            /* ... */
        }
    }
}
```

```yaml
name: Method Uses Field
entity:
    items:
        -   name: Hello
            type: Class
            loc: 1:14
        -   name: getter
            qualified: Hello.getter
            type: Method
            loc: 3:17
        -   name: num
            qualified: Hello.getter.num
            type: Variable
            loc: 3:28
        -   name: i
            qualified: Hello.i
            type: Variable
            loc: 2:9
relation:
    type: UseVar
    extra: false
    items:
        -   from: Method:'getter'
            to: Variable:'num'
            type: UseVar
            loc: file0:4:13
        -   from: Method:'getter'
            to: Variable:'i'
            type: UseVar
            loc: file0:5:17
        -   from: Method:'getter'
            to: Variable:'num'
            type: UseVar
            loc: file0:5:21
```

###### Method Uses Parameter

```java
//// Hello.java
public class Hello {
    public String getter(int num) {
        if (num > 0) {
            return "positive";
        } else {
            return "negative";
        }
    }
}
```

```yaml
name: Method Uses Parameter
entity:
    items:
        -   name: Hello
            type: Class
            loc: 1:14
        -   name: getter
            qualified: Hello.getter
            type: Method
            loc: 2:19
        -   name: num
            qualified: Hello.getter.num
            type: Variable
            loc: 2:30
relation:
    items:
        -   from: Method:'getter'
            to: Variable:'num'
            type: UseVar
            loc: file0:3:13
```