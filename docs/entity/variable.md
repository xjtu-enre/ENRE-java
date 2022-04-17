# Entity : Variable
A `variable entity' is a container which stores values.
## Supported pattern
```yaml
name : VariableDeclaration
```
### Syntax : Variable Definitions
```yaml
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
### Examples : 
- Field (global variable) declaration 
```java
package hello;

class foo{
    public int a;
}
```
```yaml
name: Field Declaration
entities:
    filter: variable
    exact: true
    items:
        -   name : a
            category : Variable
            qualifiedName : hello.foo.a
            rawType : int
            modifiers : public
            global : true
```
- Variable Declaration Statement (single var)
```java
public class foo{
        
    public String getHello(){
        String a = "hello";
        return a;
    }
}
```
```yaml
name: Variable Declaration Statement
entities:
    filter: variable
    exact: true
    items:
        -   name : a
            category : Variable
            qualifiedName : foo.getHello.a
            rawType : String
            global : false
```
- Variable Declaration Statement (multiple vars)
```java
public class foo{
        
    public String getHello(){
        String a, b, c;
        a = "hello";
        return a;
    }
}
```
```yaml
name: Variable Declaration Statement
entities:
    filter: variable
    exact: true
    items:
        -   name : a
            category : Variable
            qualifiedName : foo.getHello.a
            rawType : String
            global : false
        -   name : b
            category : Variable
            qualifiedName : foo.getHello.b
            rawType : String
            global : false
        -   name : c
            category : Variable
            qualifiedName : foo.getHello.c
            rawType : String
            global : false
```
- Single Variable Declaration (parameter)
```java
class foo {
    
    public int max(int num1, int num2){
        return Math.max(num1, num2);
    }
}
```
```yaml
name: Single Variable Declaration
entities:
    filter: variable
    exact: true
    items:
        -   name : num1
            category : Variable
            qualifiedName : foo.max.num1
            rawType : int
            global : false
        -   name : num2
            category : Variable
            qualifiedName : foo.max.num2
            rawType : int
            global : false
```
- Single Variable Declaration (catch exception)
```java
public class foo{
    
     public static void main(String args[]){
          try{
             int a[] = new int[2];
             System.out.println("Access element three :" + a[3]);
          }catch(ArrayIndexOutOfBoundsException e){
             System.out.println("Exception thrown  :" + e);
          }
          System.out.println("Out of the block");
     }
}
```
```yaml
name: Single Variable Declaration
entities:
    filter: variable
    exact: false
    items:
        -   name : a
            category : Variable
            qualifiedName : foo.main.a
            rawType : int
            global : false
        -   name : e
            category : Variable
            qualifiedName : foo.main.e
            global : false
```
- Single Variable Declaration (enhanced-for statement)
```java
public class foo{

       public ArrayList<Integer> printTest(){
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
name: Single Variable Declaration
entities:
    filter: variable
    exact: true
    items:
        -   name : result
            category : Variable
            qualifiedName : foo.printTest.result
            rawType : ArrayList<Integer>
            global : false
        -   name : integer
            category : Variable
            qualifiedName : foo.printTest.integer
            rawType : Integer
            global : false
```
- Variable Declaration Expression (for statement)
```java
public class foo{

    public void imply() {
        for (int i=0; i<10; i++){
            System.out.println(i);
        }
    }
}
```
```yaml
name: Variable Declaration Expression
entities:
    filter: variable
    exact: true
    items:
        -   name : i
            category : Variable
            qualifiedName : foo.imply.i
            rawType : int
            global : false
```