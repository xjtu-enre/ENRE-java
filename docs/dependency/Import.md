# Dependency: Import
A file imports other class, enum or package, or static imports method or var.
## Supported pattern
```yaml
name : ImportDeclaration
```
### Syntax : 
```yaml
ImportDeclaration:
    import [ static ] Name [ . * ] ;
```
### Examples : 
- Import declaration (unknown package)
```java
// file1.java
class Foo {
    public void doThings(){}
    public int failed(){}
}
```
```java
//file2.java
import Foo;

class Bar extends Foo { 
    // Overriding Foo.doThings
    public void doThings(){
    
    }
       
}

```
```yaml
name: Import Class
entities:
    items:
        -   name: Foo
            category : Class
            loc: file1/[ 2, 0, 5, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: Bar
            category : Class
            loc: file2/[ 2, 0, 8, 0 ]
            rawType: Bar
            qualifiedName: Bar
dependencies: 
        -   src: file2
            dest: file1/@class[0]
            kind: import
```
- Import class (known package)
```java
//file1
package test_package2;

import test_package1.Name;

public class Hello {
    public static void main(String[] args){
        Name name = new Name();
        System.out.println("Hello "+ name.getIt());

    }
}
```
```java
//file2
package test_package1;

public class Name {
    public String getIt(){
        return "Java World";
    }
}
```
```yaml
name: Import Class
entities:
    items:
        -   name: Name
            category : Class
            loc: file2/[ 4, 0, 8, 0 ]
            rawType: test_package1.Name
            qualifiedName: test_package1.Name
        -   name: Hello.java
            category : File
            qualifiedName: test_package2.Hello.java
dependencies: 
        -   src: file1
            dest: file2/Class[0]
            kind: import
```
- Import Static Var
```java
// file1.java
class Foo {
    public static final String MSG = "msg";

    public void doThings(){}
    public int failed(){}
}
```
```java
//file2.java
import Foo.MSG;
import Foo;

class Bar extends Foo { 
    // Overriding Foo.doThings
    public void doThings(){
        System.out.println(MSG);
    }
       
}
```
```yaml
name: Import Static Var
entities:
    items:
        -   name: Foo
            category : Class
            loc: file1/[ 2, 0, 7, 0 ]
            rawType: Foo
            qualifiedName: Foo
        -   name: MSG
            category : Variable
            rawType: String
            qualifiedName: Foo.MSG
        -   name: Bar
            category : Class
            loc: file2/[ 2, 0, 11, 0 ]
            rawType: Bar
            qualifiedName: Bar
dependencies: 
        -   src: file2
            dest: file1/Variable[0]
            kind: import
```
- Import On Demand
```java
package helloJDT.pkg;
```
```java
//HelloJDT.java
package helloJDT;

import helloJDT.pkg.*;
import java.lang.reflect.Method;

public class HelloJDT implements JDTpkg_2 {
    String str1 = "hello JDT";
    String str2 ;

    public HelloJDT(String name){
        this.str2 = name;
    }

    public void hello(){
        System.out.println(this.str1);
    }

    public void hello(String name){
        System.out.println("This is " + name);
    }
}
```
```yaml
name: Import on demand
entities:
    items:
        -   name: HelloJDT.java
            category : File
            qualifiedName: helloJDT.HelloJDT.java
        -   name: helloJDT.pkg
            category : Package
            qualifiedName: helloJDT.pkg
dependencies: 
        -   src: HelloJDT.java
            dest: helloJDT.pkg
            kind: import
```
- Import Enum
```java
//BasePostMinimalDTO.java
package run.halo.app.model.dto.post;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.halo.app.model.dto.base.OutputConverter;
import run.halo.app.model.entity.BasePost;
import run.halo.app.model.enums.PostEditorType;
import run.halo.app.model.enums.PostStatus;

@Data
@ToString
@EqualsAndHashCode
public class BasePostMinimalDTO implements OutputConverter<BasePostMinimalDTO, BasePost> {

    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private PostEditorType editorType;

    private Date updateTime;

    private Date createTime;

    private Date editTime;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;
}
```
```java
//PostStatus.java
package run.halo.app.model.enums;

public enum PostStatus implements ValueEnum<Integer> {

    /**
     * Published status.
     */
    PUBLISHED(0),

    /**
     * Draft status.
     */
    DRAFT(1),

    /**
     * Recycle status.
     */
    RECYCLE(2),

    /**
     * Intimate status
     */
    INTIMATE(3);

    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

```
```yaml
name: Import enum
entities:
    items:
        -   name: BasePostMinimalDTO.java
            category : File
            qualifiedName: run.halo.app.model.dto.post.BasePostMinimalDTO.java
        -   name: PostStatus
            category : Enum
            qualifiedName: run.halo.app.model.enums.PostStatus
            rawType: run.halo.app.model.enums.PostStatus
            loc: PostStatus/[ 4, 0, 41, 0 ]
            modifiers: public
dependencies: 
        -   src: BasePostMinimalDTO.java
            dest: PostStatus.java/Enum[0]
            kind: import
```
- Import Annotation
```java
//CacheParam.java
package run.halo.app.cache.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cache parameter annotation.
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {

}
```
```java
//JournalController.java
package run.halo.app.controller.content.api;

import static org.springframework.data.domain.Sort.Direction.DESC;

import run.halo.app.cache.lock.CacheLock;
import run.halo.app.cache.lock.CacheParam;
import run.halo.app.model.dto.BaseCommentDTO;
import run.halo.app.model.dto.JournalDTO;
import run.halo.app.model.dto.JournalWithCmtCountDTO;
import run.halo.app.model.entity.Journal;
import run.halo.app.model.entity.JournalComment;
import run.halo.app.model.enums.CommentStatus;
import run.halo.app.model.enums.JournalType;
import run.halo.app.model.params.JournalCommentParam;
import run.halo.app.model.vo.BaseCommentVO;
import run.halo.app.model.vo.BaseCommentWithParentVO;
import run.halo.app.model.vo.CommentWithHasChildrenVO;
import run.halo.app.service.JournalCommentService;
import run.halo.app.service.JournalService;
import run.halo.app.service.OptionService;

/**
 * Content journal controller.
 */
@RestController("ApiContentJournalController")
@RequestMapping("/api/content/journals")
public class JournalController {

    private final JournalService journalService;

    private final JournalCommentService journalCommentService;

    private final OptionService optionService;

    public JournalController(JournalService journalService,
        JournalCommentService journalCommentService,
        OptionService optionService) {
        this.journalService = journalService;
        this.journalCommentService = journalCommentService;
        this.optionService = optionService;
    }

    @GetMapping
    @ApiOperation("Lists journals")
    public Page<JournalWithCmtCountDTO> pageBy(
        @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
        Page<Journal> journals = journalService.pageBy(JournalType.PUBLIC, pageable);
        return journalService.convertToCmtCountDto(journals);
    }

    @GetMapping("{journalId:\\d+}")
    @ApiOperation("Gets a journal detail")
    public JournalDTO getBy(@PathVariable("journalId") Integer journalId) {
        Journal journal = journalService.getById(journalId);
        return journalService.convertTo(journal);
    }

    @GetMapping("{journalId:\\d+}/comments/top_view")
    public Page<CommentWithHasChildrenVO> listTopComments(
        @PathVariable("journalId") Integer journalId,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return journalCommentService.pageTopCommentsBy(journalId, CommentStatus.PUBLISHED,
            PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @GetMapping("{journalId:\\d+}/comments/{commentParentId:\\d+}/children")
    public List<BaseCommentDTO> listChildrenBy(@PathVariable("journalId") Integer journalId,
        @PathVariable("commentParentId") Long commentParentId,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        // Find all children comments
        List<JournalComment> postComments = journalCommentService
            .listChildrenBy(journalId, commentParentId, CommentStatus.PUBLISHED, sort);
        // Convert to base comment dto
        return journalCommentService.convertTo(postComments);
    }

    @GetMapping("{journalId:\\d+}/comments/tree_view")
    @ApiOperation("Lists comments with tree view")
    public Page<BaseCommentVO> listCommentsTree(@PathVariable("journalId") Integer journalId,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return journalCommentService
            .pageVosBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @GetMapping("{journalId:\\d+}/comments/list_view")
    @ApiOperation("Lists comment with list view")
    public Page<BaseCommentWithParentVO> listComments(@PathVariable("journalId") Integer journalId,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return journalCommentService.pageWithParentVoBy(journalId,
            PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @PostMapping("comments")
    @ApiOperation("Comments a post")
    @CacheLock(autoDelete = false, traceRequest = true)
    public BaseCommentDTO comment(@RequestBody JournalCommentParam journalCommentParam) {

        // Escape content
        journalCommentParam.setContent(HtmlUtils
            .htmlEscape(journalCommentParam.getContent(), StandardCharsets.UTF_8.displayName()));
        return journalCommentService.convertTo(journalCommentService.createBy(journalCommentParam));
    }

    @PostMapping("{id:\\d+}/likes")
    @ApiOperation("Likes a journal")
    @CacheLock(autoDelete = false, traceRequest = true)
    public void like(@PathVariable("id") @CacheParam Integer id) {
        journalService.increaseLike(id);
    }
}
```
```yaml
name: Import annotation
entities:
    items:
        -   name: JournalController.java
            category : File
            qualifiedName: run.halo.app.controller.content.api.JournalController.java
        -   name: CacheParam
            category : Annotation
            qualifiedName: run.halo.app.cache.lock.CacheParam
            rawType: run.halo.app.cache.lock.CacheParam
            loc: CacheParam/[ 11, 0, 23, 0 ]
            modifiers: public
dependencies: 
        -   src: JournalController
            dest: CacheParam.java/Annotation[0]
            kind: import
```