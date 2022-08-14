## Entity: module

A `module entity' is a closely related set of packages and resources and a new module descriptor file.

### Supported Patterns

```yaml
name: Module
```

#### Syntax: Module Definitions

```text
ModuleDeclaration:
    {Annotation} [open] module Identifier {. Identifier} { {ModuleDirective} }

ModuleDirective:
    requires {RequiresModifier} ModuleName ;
    exports PackageName [to ModuleName {, ModuleName}] ;
    opens PackageName [to ModuleName {, ModuleName}] ;
    uses TypeName ;
    provides TypeName with TypeName {, TypeName} ;

RequiresModifier:
    (one of)
    transitive static
```

##### Examples

###### Module declaration 

```java
module moA {
    requires moB;
    exports packD;
    uses name;
}
```

```yaml
name: Module Declaration
entity:
    type: module
    items:
        -   name: moA
            qualified: moA
            loc: 1:8
        -   name: moB
            qualified: moB
            loc: 2:14
```