## Entity: Variable

A `variable entity' is a container which stores values.

### Supported Patterns

```yaml
name: Variable
```

#### Syntax: Global Variable Definitions

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

##### Examples

###### Field (global variable) declaration 

```java
class Foo {
    public int a;
}
```

```yaml
name: Field Declaration
entity:
    type: Variable
    extra: true
    items:
        -   name: a
            qualified: Foo.a
            loc: 2:16
            global: true
```

###### Variable Declaration Statement (single var)

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
    type: Variable
    extra: true
    items:
        -   name: a
            qualified: Foo.getHello.a
            loc: 3:16
            global: false
```

#### Syntax: Non-global Variable Definitions

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

##### Examples

###### Variable Declaration Statement (single var)

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
    type: Variable
    extra: true
    items:
        -   name: a
            qualified: Foo.getHello.a
            loc: 3:16
            global: false
            rawType: String
```

###### Variable Declaration Statement (multiple vars)

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
    type: Variable
    extra: true
    items:
        -   name: a
            qualified: Foo.getHello.a
            loc: 3:16
            type: Variable
            rawType: String
            global: false
        -   name: b
            qualified: Foo.getHello.b
            loc: 3:19
            type: Variable
            rawType : String
            global: false
        -   name: c
            qualified: Foo.getHello.c
            loc: 3:22
            type: Variable
            rawType: String
            global: false
```

###### Single Variable Declaration (parameter)

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
    type: Variable
    extra: true
    items:
        -   name: num1
            qualified: Foo.max.num1
            loc: 2:24
            rawType: int
            global: false
        -   name: num2
            qualified: Foo.max.num2
            loc: 2:34
            rawType: int
            global: false
```

###### Single Variable Declaration (catch exception)

```java
//// Foo.java
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
    type: Variable
    items:
        -   name: e
            qualified: Foo.main.e
            loc: 6:48
            global: false
            rawType: ArrayIndexOutOfBoundsException
```

###### Single Variable Declaration (enhanced-for statement)

```java
//// Foo.java
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
entity:
    type: Variable
    items:
        -   name: integer
            qualified: Foo.printTest.integer
            loc: 4:22
            rawType: Integer
            global: false
```

###### Variable Declaration Expression (for statement)

```java
//// Foo.java
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
entity:
    type: Variable
    items:
        -   name: i
            qualified: Foo.imply.i
            loc: 3:18
            rawType: int
            global: false
```

### Properties

| Name     | Description                  |     Type     |     Default     |
| -------- | ---------------------------- |:------------:|:---------------:|
| RawType  | Indicates a variable's type. |  `'string'`  |   `undefined`   |
| Grobal   | Indicates a global field.    | `'boolean'`  |     `false`     |
| Modifier | Accessibility modifier.      | `'public'`\| `'protected'`\|`'private'` | `'public'`  |

