# Dependency: Import
A file imports other class, enum or package, or static imports method or var.
## Supported pattern
```yaml
name : Set
```
### Syntax : 
```yaml
ImportDeclaration:
    import [ static ] Name [ . * ] ;
```
### Examples : 
- Import declaration (unknown package)