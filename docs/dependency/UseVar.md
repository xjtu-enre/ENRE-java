# Dependency: UseVar
An entity uses a var in its scope, which could be a local var, a field or a parameter.
## Supported pattern
```yaml
name : UseVar
```
### Syntax : 
```yaml
UseVar:
    class {
        var;
        method {
          var
        }
    }
```
### Examples : 
- Method Uses Local Var

- Method Uses Field

- Method Uses Parameter