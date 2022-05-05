# Dependency: Annotate

A self-defined annotation annotate different kinds of entities.

## Supported pattern

```yaml
name: Annotate
```

### Syntax: 

```text
MarkerAnnotation:
   @ TypeName

NormalAnnotation:
   @ TypeName ( [ MemberValuePair { , MemberValuePair } ] )

SingleMemberAnnotation:
   @ TypeName ( Expression  )
```

#### Examples:

* Marker annotation

```java
// MailController.java
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
            category : Method
            qualifiedName: MailController.testMail
            modifiers: public
        -   name: DisableOnCondition
            category : Annotation
relation:
    items:
        -   src: file0/DisableOnCondition
            dest: file0/testMail
            category: Annotate
            r:
                d: Annotation
                e: .
                s: Annotation Use
                u: x
```

* Normal annotation

```java
// MailController.java
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
            category : Method
            qualifiedName: MailController.testMail
            modifiers: public
        -   name: DisableOnCondition
            category : Annotation
relation: 
    items:
        -   src: file0/DisableOnCondition
            dest: file0/testMail
            category: Annotate
            r:
                d: Annotation
                e: .
                s: Annotation Use
                u: x
```

* Single member annotation

```java
// MailController.java
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
            category : Method
            qualifiedName: MailController.testMail
            modifiers: public
        -   name: DisableOnCondition
            category : Annotation
relation:
    items:
        -   src: file0/DisableOnCondition
            dest: file0/testMail
            category: Annotate
            r:
                d: Annotation
                e: .
                s: Annotation Use
                u: x
```
