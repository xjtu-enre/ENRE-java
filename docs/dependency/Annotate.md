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

- Normal annotation

- Single member annotation