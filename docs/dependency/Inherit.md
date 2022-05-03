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
public abstract class AbstractStringCacheStore extends AbstractCacheStore<String, String> {
    
}
```
```yaml
name: Class extends one super class
entities:
    items:
        -   name: InMemoryCacheStore
            category : Class
            qualifiedName: InMemoryCacheStore
            modifiers: public
            File: InMemoryCacheStore.java
        -   name: AbstractStringCacheStore
            category : Class
            qualifiedName: AbstractStringCacheStore
            modifiers: public abstract
            File: AbstractStringCacheStore.java
dependencies:
    items:
        -   src: InMemoryCacheStore/Class[0]
            dest: AbstractStringCacheStore/Class[0]
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
public abstract class AbstractCacheStore<K, V> implements CacheStore<K, V> {
    
}
```
```yaml
name: Class extends one parameterized type
entities:
    items:
        -   name: AbstractCacheStore
            category : Class
            qualifiedName: AbstractCacheStore
            modifiers: public abstract
            File: InMemoryCacheStore.java
        -   name: AbstractStringCacheStore
            category : Class
            qualifiedName: AbstractStringCacheStore
            modifiers: public abstract
            File: AbstractStringCacheStore.java
dependencies:
    items:
        -   src: AbstractStringCacheStore/Class[0]
            dest: AbstractCacheStore/Class[0]
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
entities:
    items:
        -   name: CrudService
            category : Interface
            qualifiedName: CrudService
            modifiers: public
        -   name: JournalService
            category : Interface
            qualifiedName: JournalService
            modifiers: public
dependencies:
    items:
        -   src: JournalService/Interface[0]
            dest: CrudService/Interface[0]
            category: inherit
```
- Interface extends multiple super interfaces
```java
//JournalService.java
public interface JournalService extends CrudService<Journal, Integer>, BaseService {
    
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
entities:
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
dependencies:
    items:
        -   src: JournalService/Interface[0]
            dest: CrudService/Interface[0]
            category: inherit
        -   src: JournalService/Interface[0]
            dest: BaseService/Interface[0]
            category: inherit
```