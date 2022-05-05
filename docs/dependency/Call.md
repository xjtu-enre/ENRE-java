# Dependency: Call

An entity calls other methods in its scope

## Supported pattern

```yaml
name: Call
```

### Syntax: 

```text
MethodInvocation:
     [ Expression . ]
         [  Type { , Type }  ]
         Identifier ( [ Expression { , Expression } ] )
```

#### Examples:

* Method call (through ".")

```java
// Foo.java
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
            category : Class
        -   name: Bar
            category : Class
        -   name: foo
            category : Method
            qualifiedName: Foo.foo
        -   name: bar
            category : Method
            qualifiedName: Bar.bar
relation:
    items:
        -   src: file0/bar
            dest: file0/foo
            category: Call
```

* Method call (not through ".")

```java
//Foo.java
public class Foo{
    public Hello getHello(){
        /* ... */
    }
    
    public void print(){
        getHello();
    }
}
```

```yaml
name: Method Direct Method Call
entity:
    items:
        -   name: Hello
            category : Class
        -   name: Foo
            category : Class
        -   name: getHello
            category : Method
            qualifiedName: Hello.getHello
        -   name: getHello
            category : Method
            qualifiedName: Foo.getHello
        -   name: print
            category : Method
            qualifiedName: Foo.print
relation:
    items:
        -   src: file0/print
            dest: file0/getHello
            category: Call
```

* Class call

```java
// Foo.java
public class Foo{
    public Bar mBar = Bar.bar();
}

class Bar {
    public static void bar(){
        /* ... */
    }
    
}
```

```yaml
name: Class Call Method
entity:
    items:
        -   name: Foo
            category : Class
        -   name: Bar
            category : Class
        -   name: bar
            category : Method
            qualifiedName: Bar.bar
        -   name: mBar
            category : Variable
            qualifiedName: Foo.mBar
relation:
    items:
        -   src: file0/Foo
            dest: file0/bar
            category: Call
```

* Method call (multiple methods)

```java
// Foo.java
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
            category : Class
        -   name: Bar
            category : Class
        -   name: foo
            category : Method
            qualifiedName: Foo.foo
        -   name: getBar
            category : Method
            qualifiedName: Foo.getBar
        -   name: bar
            category : Method
            qualifiedName: Bar.bar
relation:
    items:
        -   src: file0/foo
            dest: file0/bar
            category: Call
        -   src: file0/foo
            dest: file0/getBar
            category: Call
```

* Method call (multiple methods)

```java
// Foo.java
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
name: Method Call Returned Method
entity:
    items:
        -   name: Foo
            category : Class
        -   name: Bar
            category : Class
        -   name: foo
            category : Method
            qualifiedName: Foo.foo
        -   name: getBar
            category : Method
            qualifiedName: Foo.getBar
        -   name: bar
            category : Method
            qualifiedName: Bar.bar
        -   name: getFoo
            category : Method
            qualifiedName: Bar.getFoo
relation:
    items:
        -   src: file0/getFoo
            dest: file0/foo
            category: Call
```