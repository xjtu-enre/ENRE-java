import {
  CodeBlock,
  CommentBlock, ConditionalStatement, ImportStatement,
  MethodDeclaration,
  MethodInvocationStatement, PackageDeclaration,
  Statement, ThrowStatement,
  VariableDeclaration
} from "./ASTTypes";

export const JUNIT_COMPARE_COLUMN = false;
export const JUNIT_MODIFIERS_TEST_METHOD = "public void";
export const JUNIT_MODIFIERS_TEST_CLASS = "public";
export const JUNIT_ANNOTATION_BEFORE = "Before";
export const JUNIT_ANNOTATION_AFTER = "After";
export const JUNIT_ANNOTATION_TEST = "Test";
export const JUNIT_INDENT_LEVEL_METHOD = 1;
export const JUNIT_INDENT_LEVEL_CLASS = 0;
export const JUNIT_INDENT_LEVEL_IMPORT = 0;
export const JUNIT_INDENT_LEVEL_PACKAGE = 0;
export const JUNIT_INDENT_LEVEL_MEMBER = 1;
export const JUNIT_INDENT_LEVEL_BLOCK = 2;
export const JUNIT_INDENT_LEVEL_BLOCK1 = 3;
export const JUNIT_INDENT_LEVEL_BLOCK2 = 4;

const anonymousNameMap = new Map<string, string>([
  ["<Anonymous as=\"Class\">", "Anonymous_Class"],
  ["<Anonymous as=\"Method\">", "Anonymous_Method"],
]);

const entityNameMap = new Map<string, string>([
  ["package", "Package"],
  ["file", "File"],
  ["class", "Class"],
  ["enum", "Enum"],
  ["enum constant", "EnumConstant"],
  ["annotation", "Annotation"],
  ["annotation member", "AnnotationMember"],
  ["interface", "Interface"],
  ["method", "Method"],
  ["module", "Module"],
  ["record", "Record"],
  ["type parameter", "TypeParameter"],
  ["variable", "Variable"],
]);

const relationNameMap = new Map<string, string>([
  ["import", "Import"],
  ["call", "Call"],
  ["parameter", "Parameter"],
  ["return", "Return"],
  ["set", "Set"],
  ["usevar", "UseVar"],
  ["modify", "Modify"],
  ["inherit", "Inherit"],
  ["implement", "Implement"],
  ["contain", "Contain"],
  ["define", "Define"],
  ["cast", "Cast"],
  ["annotate", "Annotate"],
  ["override", "Override"],
  ["reflect", "Reflect"],
  ["typed", "Typed"],
]);

const relationCategoryMap = new Map<string, string>([
  ["import", "RELATION_IMPORT"],
  ["call", "RELATION_CALL"],
  ["parameter", "RELATION_PARAMETER"],
  ["return", "RELATION_RETURN"],
  ["set", "RELATION_SET"],
  ["usevar", "RELATION_USE"],
  ["modify", "RELATION_MODIFY"],
  ["inherit", "RELATION_INHERIT"],
  ["implement", "RELATION_IMPLEMENT"],
  ["contain", "RELATION_CONTAIN"],
  ["define", "RELATION_DEFINE"],
  ["cast", "RELATION_CAST"],
  ["annotate", "RELATION_ANNOTATE"],
  ["override", "RELATION_OVERRIDE"],
  ["reflect", "RELATION_REFLECT"],
  ["typed", "RELATION_TYPED"],
]);

const entityCategoryMap = new Map<string, string>([
  ["package", "isPackage"],
  ["file", "isFile"],
  ["class", "isClass"],
  ["interface", "isInterface"],
  ["method", "isMethod"],
  ["variable", "isVariable"],
  ["enum", "isEnum"],
  ["enum constant", "isEnumCont"],
  ["module", "isModule"],
  ["annotation", "isAnnotation"],
  ["annotation member", "isAnnotationMem"],
  ["type parameter", "isTypeParameter"],
  ["record", "isRecord"],
]);

export function capFirst(str: string) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

export class JUnitBuilder {

  static buildOnlyContainsComment(
    entityOrRelation: "entity" | "relation",
    count: number,
    category: string
  ) {
    return new CommentBlock([
      `only contains ${count} ${category} ${entityOrRelation}(s)`
    ]);
  }

  static buildContainsComment(
    entityOrRelation: "entity" | "relation",
    name: string,
    category: string,
    negative: boolean = false,
  ) {
    let categoryString: string | undefined = undefined;
    if (entityOrRelation === "entity") {
      categoryString = entityNameMap.get(category);
    } else if (entityOrRelation === "relation") {
      categoryString = relationNameMap.get(category);
    }
    return new CommentBlock([
      `contains ${negative ? "no" : ""}${categoryString} ${entityOrRelation} ${name}`
    ]);
  }

  static buildContainsRelationIndexComment(
    category: string,
    index: number,
    negative: boolean = false
  ) {
    return new CommentBlock([
      `contains ${negative ? "no" : ""}${relationNameMap.get(category)} relation described in index ${index}`
    ]);
  }

  static buildEntityFilterStmt(
    variableName: string,
    category: string,
    isFullName: boolean = false,
    name?: string | undefined,
    startLine?: number | undefined,
    startColumn?: number | undefined,
  ): VariableDeclaration {
    let entityName = name;
    if (name?.includes('"')) {
      entityName = name?.replaceAll('"', '\\"');
    }
    let initializer = `TestUtil.filter(entities, (x) -> singleCollect.${entityCategoryMap.get(category)}(x.getId())`;
    if (name) {
      initializer += ` && x.get${isFullName ? "Qualified" : ""}Name().equals("${entityName}")`;
    }
    if (startLine) {
      initializer += ` && x.getLocation().getStartLine() == ${startLine}`;
    }
    if (JUNIT_COMPARE_COLUMN && startColumn) {
      initializer += ` && x.getLocation().getStartColumn() == ${startColumn}`;
    }
    initializer += ")";
    return new VariableDeclaration("", "List<BaseEntity>", variableName, JUNIT_INDENT_LEVEL_BLOCK, initializer);
  }

  static buildRelationFilterStmt(
    variableName: string,
    category: string,
    fromIdString?: string,
    toIdString?: string,
    startLine?: number,
    startColumn?: number,
  ) {
    let initializer = `TestUtil.filter(relations, (x) -> x.getRelation().getKind().equals(Configure.${relationCategoryMap.get(category.toLowerCase())})`;
    if (fromIdString) {
      initializer += ` && x.getFromEntityId() == ${fromIdString}`;
    }
    if (toIdString) {
      initializer += ` && x.getToEntityId() == ${toIdString}`;
    }
    if (startLine) {
      initializer += ` && x.getRelation().getLocation().getStartLine() == ${startLine}`;
    }
    if (JUNIT_COMPARE_COLUMN && startColumn) {
      initializer += ` && x.getRelation().getLocation().getStartColumn() == ${startColumn}`;
    }
    initializer += ")";
    return new VariableDeclaration("", "List<RelationObj>", variableName, JUNIT_INDENT_LEVEL_BLOCK, initializer);
  }

  static buildAssertionStmt(
    assertionName: "assertEquals" | "assertArrayEquals",
    obj1: string,
    obj2: string,
  ): MethodInvocationStatement {
    let invokeArguments : Array<string> = [];
    invokeArguments.push(obj2, obj1);
    return new MethodInvocationStatement(
      `Assert.${assertionName}`,
      invokeArguments,
      JUNIT_INDENT_LEVEL_BLOCK,
    );
  }

  static buildOnlyContainsEntityMethodDeclaration(
    count: number,
    category: string,
  ): MethodDeclaration {
    let comment = this.buildOnlyContainsComment("entity", count, category);
    let methodName = `onlyContains${count}${category}Entity`;
    let methodParameters: Array<string> = [];
    let statements: Array<Statement> = [];
    let methodBody: CodeBlock = new CodeBlock(statements, JUNIT_INDENT_LEVEL_BLOCK);
    let variableName = "filteredEntities";
    statements.push(this.buildEntityFilterStmt(variableName, category));
    statements.push(this.buildAssertionStmt("assertEquals", `${variableName}.size()`, `${count}`));
    return new MethodDeclaration(
      comment,
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      methodParameters,
      methodBody,
      JUNIT_ANNOTATION_TEST,
      JUNIT_INDENT_LEVEL_METHOD,
    );
  }

  static buildOnlyContainsRelationMethodDeclaration(
    count: number,
    category: string,
  ): MethodDeclaration {
    let comment = this.buildOnlyContainsComment("relation", count, category);
    let methodName = `onlyContains${count}${category}Relation`;
    let statements: Array<Statement> = [];
    let methodBody: CodeBlock = new CodeBlock(statements, 1);
    let variableName = "filteredRelations";
    statements.push(this.buildRelationFilterStmt(variableName, category));
    statements.push(this.buildAssertionStmt("assertEquals", `${variableName}.size()`, `${count}`));
    return new MethodDeclaration(
      comment,
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      [],
      methodBody,
      JUNIT_ANNOTATION_TEST,
      JUNIT_INDENT_LEVEL_METHOD,
    );
  }

  static buildRelationException(condition: string, key: "from" | 'to'): ConditionalStatement {
    return new ConditionalStatement(
      condition,
      new CodeBlock([new ThrowStatement(
          "RuntimeException",
          `Insufficient or wrong predicates to determine only one [${key}] entity`,
          JUNIT_INDENT_LEVEL_BLOCK1,
        )],
        JUNIT_INDENT_LEVEL_BLOCK1,
      ),
      new CodeBlock([], 1),
      JUNIT_INDENT_LEVEL_BLOCK,
    );
  }

  static buildContainsRelationIndexMethodDeclaration(
    relationCategory: string,
    index: number,
    negative: boolean = false,
    isFromFullName: boolean = false,
    isToFullName: boolean = false,
    fromCategory: string,
    toCategory: string,
    fromName: string,
    toName: string,
    additionalAssertions: Array<Statement>,
    fromStartLine?: number,
    toStartLine?: number,
    relStartLine?: number,
    relStartColumn?: number,
  ): MethodDeclaration {
    let comment = this.buildContainsRelationIndexComment(relationCategory, index, negative);
    let fromVariableName = "fromEntities";
    let toVariableName = "toEntities";
    let filteredRelationsName = "filteredRelations";
    let methodName = `contains${relationNameMap.get(relationCategory)}RelationDescribedInIndex${index}`;
    let statements: Array<Statement> = [
      this.buildEntityFilterStmt(fromVariableName, fromCategory, isFromFullName, fromName, fromStartLine),
      this.buildRelationException(`${fromVariableName}.size() != 1`, "from"),
      this.buildEntityFilterStmt(toVariableName, toCategory, isToFullName, toName, toStartLine),
      this.buildRelationException(`${toVariableName}.size() != 1`, "to"),
      this.buildRelationFilterStmt(
        filteredRelationsName,
        relationCategory.toLowerCase(),
        `${fromVariableName}.get(0).getId()`,
        `${toVariableName}.get(0).getId()`,
        relStartLine,
        relStartColumn,
      ),
      this.buildAssertionStmt("assertEquals", `${filteredRelationsName}.size()`, `${negative? '0' : '1'}`),
    ];
    if (additionalAssertions) {
      statements.push(...additionalAssertions)
    }
    let methodBody: CodeBlock = new CodeBlock(statements, JUNIT_INDENT_LEVEL_BLOCK);
    return new MethodDeclaration(
      comment,
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      [],
      methodBody,
      JUNIT_ANNOTATION_TEST,
      JUNIT_INDENT_LEVEL_METHOD,
    );
  }

  static buildContainsEntityMethodDeclaration(
    category: string,
    name: string,
    negative: boolean = false,
    qualified?: string,
    additionalAssertions?: Array<Statement>,
    startLine?: number,
    startColumn?: number,
    endLine?: number,
    endColumn?: number,
  ): MethodDeclaration {
    let comment = this.buildContainsComment("entity", name, category, negative);
    let variableName = "filteredEntities";
    let entityName = name;
    let titleName = name;
    if (anonymousNameMap.has(name)) {
      entityName = `${anonymousNameMap.get(name)}`;
      titleName = entityName;
    }
    if (name.includes(".")) {
      titleName = titleName.replaceAll(".", "_");
    }
    let methodName = `contains${entityNameMap.get(category)}Entity${titleName}${startLine?startLine:""}`;
    let statements: Array<Statement> = [
      this.buildEntityFilterStmt(variableName, category, false, entityName, startLine, startColumn),
      this.buildAssertionStmt("assertEquals", `${variableName}.size()`, "1"),
      new VariableDeclaration("", "BaseEntity", "ent", JUNIT_INDENT_LEVEL_BLOCK, `${variableName}.get(0)`),
    ];
    if (qualified) {
      let qualifiedName = qualified;
      if (qualified.includes('"')) {
        qualifiedName = qualified.replaceAll('"', '\\"');
      }
      if (anonymousNameMap.has(name)) {
        qualifiedName = qualified.replace(name, `${anonymousNameMap.get(name)}`);
      }
      statements.push(this.buildAssertionStmt("assertEquals", "ent.getQualifiedName()", `"${qualifiedName}"`));
    }
    if (additionalAssertions) {
      statements.push(...additionalAssertions);
    }
    let loc: Array<number> = [
      startLine? startLine : -1,
      startColumn? startColumn: -1,
      endLine? endLine: -1,
      endColumn? endColumn: -1,
    ];
    statements.push(new VariableDeclaration("", "int[]", "gt", JUNIT_INDENT_LEVEL_BLOCK, `{${loc[0]}, ${loc[1]}, ${loc[2]}, ${loc[3]}}`));
    statements.push(this.buildAssertionStmt("assertArrayEquals", "TestUtil.expandLocationArray(ent.getLocation(), gt)", "gt"));
    let methodBody = new CodeBlock(statements, JUNIT_INDENT_LEVEL_METHOD);
    return new MethodDeclaration(
      comment,
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      [],
      methodBody,
      JUNIT_ANNOTATION_TEST,
      JUNIT_INDENT_LEVEL_METHOD,
    );
  }

  static buildClassMembers() {
    let res: Array<VariableDeclaration> = [
      new VariableDeclaration(
        "private final",
        "SingleCollect",
        "singleCollect",
        JUNIT_INDENT_LEVEL_MEMBER,
        "SingleCollect.getSingleCollectInstance()",
      ),
      new VariableDeclaration(
        "private",
        "Map<Integer, ArrayList<Tuple<Integer, Relation>>>",
        "relationMap",
        JUNIT_INDENT_LEVEL_MEMBER,
        undefined,
      ),
      new VariableDeclaration(
        "private",
        "ArrayList<BaseEntity>",
        "entities",
        JUNIT_INDENT_LEVEL_MEMBER,
        undefined,
      ),
      new VariableDeclaration(
        "private",
        "List<RelationObj>",
        "relations",
        JUNIT_INDENT_LEVEL_MEMBER,
        undefined,
      ),
    ];
    return res;
  }

  static buildImportStatements() {
    let res: Array<ImportStatement> = [
      new ImportStatement("entity.BaseEntity", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("entity.MethodEntity", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("entity.VariableEntity", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("entity.RecordEntity", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("entity.properties.Relation", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("org.junit.Assert", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("org.junit.Before", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("org.junit.After", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("org.junit.Test", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("picocli.CommandLine", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("util.*", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("java.util.ArrayList", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("java.util.List", JUNIT_INDENT_LEVEL_IMPORT),
      new ImportStatement("java.util.Map", JUNIT_INDENT_LEVEL_IMPORT),
    ];
    return res;
  }

  static buildPackageStatement() {
    return new PackageDeclaration("client");
  }

  static buildBeforeMethodDeclaration(groupName: string, caseName: string) {
    let methodName = "execute";
    let statements: Array<Statement> = [
      new VariableDeclaration(undefined, "String", "groupName", JUNIT_INDENT_LEVEL_BLOCK, `"${groupName}"`),
      new VariableDeclaration(undefined, "String", "caseName", JUNIT_INDENT_LEVEL_BLOCK, `"${caseName}"`),
      new VariableDeclaration(undefined, "String[]", "args", JUNIT_INDENT_LEVEL_BLOCK, `{ "java", String.format("src/test/resources/cases/_%s/_%s/", groupName, caseName), String.format("_%s", caseName) }`),
      new VariableDeclaration(undefined, "TemplateWork", "work", JUNIT_INDENT_LEVEL_BLOCK, "new TemplateWork()"),
      new VariableDeclaration(undefined, undefined, "relationMap", JUNIT_INDENT_LEVEL_BLOCK, "work.execute(CommandLine.populateCommand(new EnreCommand(), args))"),
      new VariableDeclaration(undefined, undefined, "relations", JUNIT_INDENT_LEVEL_BLOCK, "TestUtil.getRelations(relationMap)"),
      new VariableDeclaration(undefined, undefined, "entities", JUNIT_INDENT_LEVEL_BLOCK, "singleCollect.getEntities()"),
    ];
    let methodBody = new CodeBlock(statements, JUNIT_INDENT_LEVEL_BLOCK);
    return new MethodDeclaration(
      new CommentBlock([
        "execute ENRE-JAVA and get generated entities and relations before every test cases"
      ]),
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      [],
      methodBody,
      JUNIT_ANNOTATION_BEFORE,
      JUNIT_INDENT_LEVEL_METHOD,
    );
  }

  static buildAfterMethodDeclaration() {
    let methodName = "clear";
    let statements: Array<Statement> = [
      new MethodInvocationStatement("singleCollect.clear", [], JUNIT_INDENT_LEVEL_BLOCK),
    ];
    let methodBody = new CodeBlock(statements, JUNIT_INDENT_LEVEL_BLOCK);
    return new MethodDeclaration(
      new CommentBlock([
        "clear ENRE-JAVA result in memory"
      ]),
      JUNIT_MODIFIERS_TEST_METHOD,
      methodName,
      [],
      methodBody,
      JUNIT_ANNOTATION_AFTER,
    )
  }
}
