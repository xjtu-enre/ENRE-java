## Entity: Method

`Method` is an entity inside the `class` to perform specific activity.

### Supported Patterns

```yaml
name: Method
```
#### Syntax: Method Definitions

```text
MethodDeclaration:
    [ Javadoc ] { ExtendedModifier } [  TypeParameter { , TypeParameter } ] ( Type | void )
        Identifier (
            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
        ) { Dimension }
        [ throws Type { , Type } ]
        ( Block | ; )

ConstructorDeclaration: 
    [ Javadoc ] { ExtendedModifier } [  TypeParameter { , TypeParameter }  ]
        Identifier (
            [ ReceiverParameter , ] [ FormalParameter { , FormalParameter } ]
        ) { Dimension }
        [ throws Type { , Type } ]
        ( Block | ; )

CompactConstructorDeclaration:
    [ Javadoc ] ExtendedModifier { ExtendedModifier}
        Identifier
        ( Block | ; )
```

##### Examples

###### Method declaration

```java
class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

```yaml
name: Method Declaration
entity:
    type: Method
    items:
        -   name: getName
            qualified: helloJDT.pkg.test.Person.getName
            loc: 4:19
        -   name: setName
            qualified: helloJDT.pkg.test.Person.setName
            loc: 8:17
```
###### Generic Method declaration 

```java
//// Foo.java
public class Foo() {
    public <T> T show(T t){
        return t;
    }
}
```

```yaml
name: Generic Method Declaration 
entity:
    type: Method
    items:
        -   name: show
            loc: 2:14
```

#### Syntax: LambdaMethod Definitions

```text
LambdaExpression:
    LambdaParameters -> LambdaBody

LambdaParameters:
    ( [LambdaParameterList] )
    Identifier
LambdaParameterList:
    LambdaParameter {, LambdaParameter}
    Identifier {, Identifier}
LambdaParameter:
    {VariableModifier} LambdaParameterType VariableDeclaratorId
    VariableArityParameter
LambdaParameterType:
    UnannType
    var

LambdaBody:
    Expression
    Block
```

##### Examples

###### LambdaMethod declaration

```java
public class Main {
    public static void main(String[] args) {
        Calculator calculator = (Integer s1) -> s1 * 2;
    }
}
interface Calculator {
    public Integer calcIt(Integer s1);
}
```

```yaml
name: LambdaMethod Declaration
entity:
    type: Method
    items:
        -   name: <Anon Method>
            loc: 3:33:0
            Lambda: true
```

### Properties

| Name     | Description                | Type       | Default       |
| -------- | -------------------------- |------------|---------------|
| isStatic | Indicates a static method. | `boolean`  | false         |
| Modifier | Accessibility modifier.    | `'public'`\| `'protected'`\|`'private'` | `'public'` |
| isOverride | Indicates a override method. | `boolean`  | null      |
| isSetter | Indicates a setter method. | `boolean`  | null          |
| isGetter | Indicates a getter method. | `boolean`  | null          |
| isDelegator | Indicates a delegator method. | `boolean`  | null    |
| isRecursive | Indicates a recursive method. | `boolean`  | null    |
| isAssign | Indicates an assign method. | `boolean`       | null    |
| isSynchronized | Indicates a synchronized method. | `boolean` | null |
| isConstructor | Indicates a constructor method. | `boolean` | null |
| isCallSuper | Indicates a callSuper method. | `boolean` | null |
