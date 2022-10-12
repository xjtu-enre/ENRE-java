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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
            loc: 10:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 4:12
            type: Annotation
relation:
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:9:5
            type: Annotate
```

###### Normal annotation

```java
//// MailController.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
            loc: 11:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 4:12
            type: Annotation
relation: 
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:10:5
            type: Annotate
```

###### Single member annotation

```java
//// MailController.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
            loc: 10:17
            type: Method
            modifiers: public
        -   name: DisableOnCondition
            loc: 4:12
            type: Annotation
relation:
    items:
        -   from: Annotation:'DisableOnCondition'
            to: Method:'testMail'
            loc: file0:9:5
            type: Annotate
```
