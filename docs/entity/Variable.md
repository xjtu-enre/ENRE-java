# Entity: Variable

A `variable entity' is a container which stores values.

## Supported pattern

```yaml
name: Variable
```

### Syntax: Variable Definitions

```text
 VariableDeclarationExpression:
    { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment }

 VariableDeclarationStatement:
    { ExtendedModifier } Type VariableDeclarationFragment
        { , VariableDeclarationFragment } ;

 FieldDeclaration:
    [Javadoc] { ExtendedModifier } Type VariableDeclarationFragment
         { , VariableDeclarationFragment } ;

 VariableDeclarationFragment:
    Identifier { Dimension } [ = Expression ]

 SingleVariableDeclaration:
    { ExtendedModifier } Type {Annotation} [ ... ] Identifier { Dimension } [ = Expression ]
```

#### Examples:

* Field (global variable) declaration 

```java
class Foo {
    public int a;
}
```

```yaml
name: Field Declaration
entity:
    filter: Variable
    r:
        d: Var
        e: .
        s: Field
        u: .
    exact: true
    items:
        -   name : a
            qualifiedName : Foo.a
            global : true
```

* Variable Declaration Statement (single var)

```java
class Foo {
    public String getHello() {
        String a = "hello";
        return a;
    }
}
```

```yaml
name: Variable Declaration Statement
entity:
    filter: Variable
    r:
        d: Var
        e: .
        s: x
        u: .
    exact: true
    items:
        -   name : a
            qualifiedName : Foo.getHello.a
            global : false
```

* Variable Declaration Statement (multiple vars)

```java
class Foo{
    public String getHello() {
        String a, b, c;
        a = "hello";
        return a;
    }
}
```

```yaml
name: Multi Variable Declaration Statement
entity:
    filter: Variable
    r:
        d: Var
        e: .
        s: x
        u: .
    exact: true
    items:
        -   name : a
            category : Variable
            qualifiedName : Foo.getHello.a
            rawType : String
            global : false
        -   name : b
            category : Variable
            qualifiedName : Foo.getHello.b
            rawType : String
            global : false
        -   name : c
            category : Variable
            qualifiedName : Foo.getHello.c
            rawType : String
            global : false
```

* Single Variable Declaration (parameter)

```java
class Foo {
    public int max(int num1, int num2){
        return Math.max(num1, num2);
    }
}
```

```yaml
name: Parameter Declaration
entity:
    filter: Variable
    r:
        d: o/hidden
        e: .
        s: x
        u: Parameter
    exact: true
    items:
        -   name : num1
            qualifiedName : Foo.max.num1
            rawType : int
            global : false
        -   name : num2
            qualifiedName : Foo.max.num2
            rawType : int
            global : false
```

* Single Variable Declaration (catch exception)

```java
// Foo.java
public class Foo{
    public static void main(String args[]) {
        try{
            int a[] = new int[2];
            a[3] = 1;
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }
}
```

```yaml
name: Catch Parameter Declaration
entity:
    filter: Variable
    r:
        d: x
        e: .
        s: x
        u: Catch Parameter
    items:
        -   name: e
            qualifiedName: Foo.main.e
            global: false
```

* Single Variable Declaration (enhanced-for statement)

```java
// Foo.java
public class Foo{
    public ArrayList<Integer> printTest() {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer integer : this.test) {
            if (integer > this.getB()) {
                result.add(integer);
            }
        }
        return result;
    }
}
```

```yaml
name: For Loop Variable
entities:
    filter: Variable
    r:
        d: Var
        e: .
        s: x
        u: .
    items:
        -   name : integer
            qualifiedName : Foo.printTest.integer
            rawType : Integer
            global : false
```

* Variable Declaration Expression (for statement)

```java
// Foo.java
public class Foo{
    public void imply() {
        for (int i=0; i<10; i++) {
            System.out.println(i);
        }
    }
}
```

```yaml
name: Variable Declaration Expression
entities:
    filter: Variable
    r:
        d: Var
        e: .
        s: x
        u: .
    items:
        -   name : i
            qualifiedName : Foo.imply.i
            rawType : int
            global : false
```
