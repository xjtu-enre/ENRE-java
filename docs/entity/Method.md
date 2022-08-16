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
public <T> T show(T t){
    return t;
}
```

```yaml
name: Generic Method Declaration 
entity:
    type: Method
    items:
        -   name: show
            loc: 1:14
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
        -   name: <Anonymous as="Method">
            loc: 3:33:0
            Lambda: true
```