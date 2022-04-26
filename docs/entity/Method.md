# Entity : Method
`Method` is an entity inside the `class` to perform specific activity.
## Supported pattern
```yaml
name : MethodDeclaration
```
### Syntax : Method Definitions
```yaml
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
### Examples : 
- Method declaration
```java
package helloJDT.pkg.test;

public class Person {
    private String name;
    private int age;
    private static String msg="hello world";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person() {
    }

    private Person(String name) {
        this.name = name;
        System.out.println(age);
    }

    public void fun() {
        System.out.println("fun "+Person.msg);
    }

    public void fun(String name,int age) {
        System.out.println("我叫"+name+",今年"+age+"岁");
    }
}

```
```yaml
name: Package Declaration
entities:
    filter: package
    items:
        -   name: hello
            location : []
```

