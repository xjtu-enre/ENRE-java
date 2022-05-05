# Dependency: Inherit
A class extends one super (abstract) class, or an interface extends single or multiple super interfaces.
## Supported pattern
```yaml
name: extends
```
### Syntax : 
```yaml
Class Declaration:
      [ Javadoc ] { ExtendedModifier } class Identifier
                        [ < TypeParameter { , TypeParameter } > ]
                        [ extends Type ]
                        [ implements Type { , Type } ]
                        [ permits Type { , Type } ]
                        { { ClassBodyDeclaration | ; } }
```
### Examples : 
- Class extends one super class
```java
//InMemoryCacheStore.java
public class InMemoryCacheStore extends AbstractStringCacheStore {
    
}
```
```java
//AbstractStringCacheStore.java
public abstract class AbstractStringCacheStore {
    
}
```
```yaml
name: Class extends one super class
entity:
    items:
        -   name: InMemoryCacheStore
            category : Class
            qualifiedName: InMemoryCacheStore
            modifiers: public
        -   name: AbstractStringCacheStore
            category : Class
            qualifiedName: AbstractStringCacheStore
            modifiers: public abstract
relation:
    items:
        -   src: file0/Class[0]
            dest: file1/Class[0]
            category: inherit
```
- Class extends one parameterized type
```java
//AbstractStringCacheStore.java
public abstract class AbstractStringCacheStore extends AbstractCacheStore<String, String> {
    
}
```
```java
//AbstractCacheStore.java
public abstract class AbstractCacheStore<K, V> {
    
}
```
```yaml
name: Class extends one parameterized type
entity:
    items:
        -   name: AbstractCacheStore
            category : Class
            qualifiedName: AbstractCacheStore
            modifiers: public abstract
        -   name: AbstractStringCacheStore
            category : Class
            qualifiedName: AbstractStringCacheStore
            modifiers: public abstract
relation:
    items:
        -   src: file0/Class[0]
            dest: file1/Class[0]
            category: inherit
```
- Interface extends one super interface
```java
//JournalService.java
public interface JournalService extends CrudService<Journal, Integer> {
    
}
```
```java
//CrudService.java
public interface CrudService<D, I> {
    
}
```
```yaml
name: Interface extends one super type
entity:
    items:
        -   name: CrudService
            category : Interface
            qualifiedName: CrudService
            modifiers: public
        -   name: JournalService
            category : Interface
            qualifiedName: JournalService
            modifiers: public
relation:
    items:
        -   src: file0/Interface[0]
            dest: file1/Interface[0]
            category: inherit
```
- Interface extends multiple super interfaces
```java
//JournalService.java
public interface JournalService extends CrudService<String, Integer>, BaseService {
    
}
```
```java
//CrudService.java
public interface CrudService<D, I> {
    
}
```
```java
//BaseService.java
public interface BaseService {
    
}
```
```yaml
name: Interface extends one super type
entity:
    items:
        -   name: CrudService
            category : Interface
            modifiers: public
        -   name: JournalService
            category : Interface
            modifiers: public
        -   name: BaseService
            category : Interface
            modifiers: public
relation:
    items:
        -   src: file0/Interface[0]
            dest: file1/Interface[0]
            category: inherit
        -   src: file0/Interface[0]
            dest: file2/Interface[0]
            category: inherit
```