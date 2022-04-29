# Dependency: Annotate
A self-defined annotation annotate different kinds of entities.
## Supported pattern
```yaml
name : 
    - MarkerAnnotation
    - NormalAnnotation
    - SingleMemberAnnotation
```
### Syntax : 
```yaml
MarkerAnnotation:
   @ TypeName

NormalAnnotation:
   @ TypeName ( [ MemberValuePair { , MemberValuePair } ] )

SingleMemberAnnotation:
   @ TypeName ( Expression  )
```
### Examples : 
- Marker annotation
```java
//MailController.java
public class MailController {

    private final MailService mailService;

    @DisableOnCondition
    public BaseResponse<String> testMail(@Valid @RequestBody MailParam mailParam) {
        mailService.sendTextMail(mailParam.getTo(), mailParam.getSubject(), mailParam.getContent());
        return BaseResponse.ok("已发送，请查收。若确认没有收到邮件，请检查服务器日志");
    }
}
```
```java
//DisableOnCondition.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableOnCondition {
    @AliasFor("mode")
    Mode value() default Mode.DEMO;

    @AliasFor("value")
    Mode mode() default Mode.DEMO;
}
```
```yaml
scenario: Marker annotation
entities:
    items:
        -   name: testMail
            category : Method
            qualifiedName: MailController.testMail
            modifiers: public
        -   name: DisableOnCondition
            category : Annotation
dependencies: 
        -   src: @DisableOnCondition/Annotation[0]
            dest: @MailController/Method[0]
            kind: Annotate
```
- Normal annotation
```java
//AdminController.java
public class AdminController {

    private final AdminService adminService;

    private final OptionService optionService;

    @CacheLock(autoDelete = false, prefix = "login_precheck")
    public LoginPreCheckDTO authPreCheck(@RequestBody @Valid LoginParam loginParam) {
        final User user = adminService.authenticate(loginParam);
        return new LoginPreCheckDTO(MFAType.useMFA(user.getMfaType()));
    }
}
```
```java
//CacheLock.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * Cache prefix, default is ""
     *
     * @return cache prefix
     */
    @AliasFor("value")
    String prefix() default "";

    /**
     * Alias of prefix, default is ""
     *
     * @return alias of prefix
     */
    @AliasFor("prefix")
    String value() default "";

    /**
     * Expired time, default is 5.
     *
     * @return expired time
     */
    long expired() default 5;

    /**
     * Time unit, default is TimeUnit.SECONDS.
     *
     * @return time unit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Delimiter, default is ':'
     *
     * @return delimiter
     */
    String delimiter() default ":";

    /**
     * Whether delete cache after method invocation.
     *
     * @return true if delete cache after method invocation; false otherwise
     */
    boolean autoDelete() default true;

    /**
     * Whether trace the request info.
     *
     * @return true if trace the request info; false otherwise
     */
    boolean traceRequest() default false;
}
```
```yaml
scenario: Normal annotation
entities:
    items:
        -   name: CacheLock
            category : Annotation
            modifiers: public
        -   name: authPreCheck
            category : Method
            modifiers: public
            qualifiedName: AdminController.authPreCheck
dependencies: 
        -   src: @CacheLock/Annotation[0]
            dest: @AdminController/Method[0]
            kind: Annotate
```
- Single member annotation
```java
//BaseController.java
public class BaseController {

    @CacheParam(10)
    private final AdminService adminService;
    
}
```
```java
//CacheParam.java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {

    long expired() default 5;
}
```
```yaml
scenario: Single member annotation
entities:
    items:
        -   name: CacheParam
            category : Annotation
            modifiers: public
        -   name: adminService
            category : Variable
            modifiers: private final
            qualifiedName: BaseController.adminService
dependencies: 
        -   src: @CacheParam/Annotation[0]
            dest: @BaseController/Variable[0]
            kind: Annotate
```