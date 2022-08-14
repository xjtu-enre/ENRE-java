## Entity:Record

A `record entity' is a restricted kind of class that defines a simple aggregate of values.

### Supported Patterns

```yaml
name : Record
```

#### Syntax: Record Definitions

```text
RecordDeclaration:
      {ClassModifier} record TypeIdentifier [TypeParameters] RecordHeader [ClassImplements] RecordBody

RecordHeader:
      ( [RecordComponentList] )

RecordComponentList:
      RecordComponent { RecordComponent,}

RecordComponent:
      {RecordComponentModifier} UnannType Identifier
      VariableArityRecordComponent

VariableArityRecordComponent:
      {RecordComponentModifier} UnannType {Annotation} Identifier...

RecordComponentModifier:
      Annotation

RecordBody:
      { {RecordBodyDeclaration} }

RecordBodyDeclaration:
      ClassBodyDeclaration
      CompactConstructorDeclaration
```

##### Examples

###### Record declaration 

```java
public record User(int x, String y) { }
```

```yaml
name: Record Declaration
entity:
    type: Record
    items:
        -   name: User
            qualified: User
            loc: 1:15
```

###### Record declaration In Classes 

```java
public class foo {
    public record User(int x, String y) { }
} 
```

```yaml
name: Record Declaration
entity:
    type: Record
    items:
        -   name: User
            qualified: foo.User
            loc: 2:19
```