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
name: Method Declaration
entities:
    filter: method
    items:
        -   name: getName
            location : [7, 4, 9, 4]
            rawType: String
            qualifiedName: helloJDT.pkg.test.Person.getName
            category: Method
            modifiers: public
        -   name: setName
            location : [11, 4, 13, 4]
            rawType: void
            qualifiedName: helloJDT.pkg.test.Person.setName
            category: Method
            modifiers: public
            parameter:
                - types: String
                - names: name
        -   name: getAge
            location : [15, 4, 17, 4]
            rawType: int
            qualifiedName: helloJDT.pkg.test.Person.getAge
            category: Method
            modifiers: public
        -   name: setAge
            location : [19, 4, 21, 4]
            rawType: void
            qualifiedName: helloJDT.pkg.test.Person.setAge
            category: Method
            modifiers: public
            parameter:
                - types: int
                - names: age
        -   name: Person
            location : [23, 4, 24, 4]
            rawType: helloJDT.pkg.test.Person.Person
            qualifiedName: helloJDT.pkg.test.Person.Person
            category: Method
            modifiers: public
        -   name: Person
            location : [26, 4, 29, 4]
            rawType: helloJDT.pkg.test.Person.Person
            qualifiedName: helloJDT.pkg.test.Person.Person
            category: Method
            modifiers: private
            parameter:
                - types: String
                - names: name
        -   name: fun
            location : [31, 4, 33, 4]
            rawType: void
            qualifiedName: helloJDT.pkg.test.Person.fun
            category: Method
            modifiers: public
        -   name: fun
            location : [35, 4, 37, 4]
            rawType: void
            qualifiedName: helloJDT.pkg.test.Person.fun
            category: Method
            modifiers: public
            parameter:
                - types: String int
                - names:name age
```

