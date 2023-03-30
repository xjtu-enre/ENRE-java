## Dependency: Annotate

A self-defined annotation annotate different kinds of entities.

### Supported Patterns

```yaml
name: Annotate
```

#### Syntax: Annotate Definitions

```text
MarkerAnnotation:
   @ TypeName

NormalAnnotation:
   @ TypeName ( [ MemberValuePair { , MemberValuePair } ] )

SingleMemberAnnotation:
   @ TypeName ( Expression  )
```

##### Examples

###### Marker annotation

```java
//// MailController.java
@interface DisableOnCondition {
    int value() default 0;
}

public class MailController {
    @DisableOnCondition
    public void testMail() {
        /* ... */
    }
}
```

```yaml
name: Marker Annotation
entity:
    items:
        -   name: testMail
            qualified: MailController.testMail
            loc: 7:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 1:12
            type: Annotation
relation:
    type: Annotate
    extra: false
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:6:6
```

###### Normal annotation

```java
//// MailController.java
@interface DisableOnCondition {
    int value() default 0;
    String comment() default "";
}

public class MailController {
    @DisableOnCondition(value = 1, comment = "No need")      // Overrides the default value
    public void testMail() {
        /* ... */
    }
}
```

```yaml
name: Normal Annotation
entity:
    items:
        -   name: testMail
            qualified: MailController.testMail
            loc: 8:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 1:12
            type: Annotation
relation: 
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:7:6
            type: Annotate
```

###### Single member annotation

```java
//// MailController.java
@interface DisableOnCondition {
    int value() default 0;
}

public class MailController {
    @DisableOnCondition(1 + 1)      // Calculate and Override the default value
    public void testMail() {
        /* ... */
    }
}
```

```yaml
name: Single Member Annotation
entity:
    items:
        -   name: testMail
            qualified: MailController.testMail
            loc: 7:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 1:12
            type: Annotation
relation:
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:6:6
            type: Annotate
```
