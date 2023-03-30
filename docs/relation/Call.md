## Dependency: Call

An entity calls other methods in its scope

### Supported Patterns

```yaml
name: Call
```

#### Syntax: Call Definitions

```text
MethodInvocation:
     [ Expression . ]
         [  Type { , Type }  ]
         Identifier ( [ Expression { , Expression } ] )
```

##### Examples

###### Method call (through ".")

```java
//// Foo.java
public class Foo{
    public void foo(){
        /* ... */
    }
}

class Bar{
    public void bar(){
        Foo mFoo = new Foo();
        mFoo.foo();
    }
}
```

```yaml
name: Method Dot Call Method
entity:
    items:
        -   name: Foo
            type: Class
            loc: 1:14
        -   name: Bar
            type: Class
            loc: 7:7
        -   name: foo
            type: Method
            qualified: Foo.foo
            loc: 2:17
        -   name: bar
            type: Method
            qualified: Bar.bar
            loc: 8:17
relation:
    items:
        -   from: method:'bar'
            to: method:'foo'
            type: Call
            loc: file0:10:14
```

###### Method call (not through ".")

```java
//// Foo.java
public class Foo{
    public void getHello(){
        /* ... */
    }
    
    public void print(){
        getHello();
    }
}
```

```yaml
name: Method Direct Call Method
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: getHello
            type : Method
            qualified: Foo.getHello
            loc: 2:17
        -   name: print
            type : Method
            qualified: Foo.print
            loc: 6:17
relation:
    items:
        -   from: Method:'print'
            to: Method:'getHello'
            type: Call
            loc: file0:7:9
```

###### Class call

```java
//// Foo.java
public class Foo{
    public Bar mBar = Bar.bar();
}

class Bar {
    public static Bar bar(){
        /* ... */
    }
    
}
```

```yaml
name: Class Call Method
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: Bar
            type : Class
            loc: 5:7
        -   name: bar
            type : Method
            qualified: Bar.bar
            loc: 6:23
        -   name: mBar
            type : Variable
            qualified: Foo.mBar
            loc: 2:16
relation:
    items:
        -   from: Class:'Foo'
            to: Method:'bar'
            type: Call
            loc: file0:2:23
```

###### Record call

```java
//// Foo.java
public record Foo(int y){
    public Bar mBar = Bar.bar();
}

record Bar(int x) {
    public static Bar bar(){
        /* ... */
    }    
}
```

```yaml
name: Record Call Method
entity:
    items:
        -   name: Foo
            type : Record
            loc: 1:15
        -   name: Bar
            type : Record
            loc: 5:15
        -   name: bar
            type : Method
            qualified: Bar.bar
            loc: 6:23
        -   name: mBar
            type : Variable
            qualified: Foo.mBar
            loc: 2:16
relation:
    items:
        -   from: Record:'Foo'
            to: Method:'bar'
            type: Call
            loc: file0:2:23
```

###### Method call (multiple methods)

```java
//// Foo.java
public class Foo {
    public Bar getBar(){
        Bar mBar = new Bar();
        return mBar;
    }
    
    public void foo(){
        getBar().bar();
    }
}

class Bar {
    public void bar(){
        /* ... */
    }
}
```

```yaml
name: Method Call Returned Method
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: Bar
            type : Class
            loc: 12:7
        -   name: foo
            type : Method
            qualified: Foo.foo
            loc: 7:17
        -   name: getBar
            type : Method
            qualified: Foo.getBar
            loc: 2:16
        -   name: bar
            type : Method
            qualified: Bar.bar
            loc: 13:17
relation:
    items:
        -   from: Method:'foo'
            to: Method:'bar'
            type: Call
            loc: file0:3:24
        -   from: Method:'foo'
            to: Method:'getBar'
            type: Call
            loc: file0:8:9
```

###### Method Call Super 

```java
//// Foo.java
public class Foo {
    public Bar getBar(){
        Bar mBar = new Bar();
        return mBar;
    }
    
    public void foo(){
        getBar().bar();
    }
}

class Bar extends Foo{
    public void bar(){
        /* ... */
    }
    
    public void getFoo(){
        super.foo();
    }
}
```

```yaml
name: Method Call Super 
entity:
    items:
        -   name: Foo
            type : Class
            loc: 1:14
        -   name: Bar
            type : Class
            loc: 12:7
        -   name: foo
            type : Method
            qualified: Foo.foo
            loc: 7:17
        -   name: getBar
            type : Method
            qualified: Foo.getBar
            loc: 2:16
        -   name: bar
            type : Method
            qualified: Bar.bar
            loc: 13:17
        -   name: getFoo
            type : Method
            qualified: Bar.getFoo
            loc: 17:17
relation:
    items:
        -   from: Method:'getFoo'
            to: Method:'foo'
            type: Call
            loc: file0:18:9
```