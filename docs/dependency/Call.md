# Dependency: Call
An entity calls other methods in its scope   
## Supported pattern
```yaml
name : MethodInvocation
```
### Syntax : 
```yaml
MethodInvocation:
     [ Expression . ]
         [  Type { , Type }  ]
         Identifier ( [ Expression { , Expression } ] )
```
### Examples : 
- Import declaration (unknown package)