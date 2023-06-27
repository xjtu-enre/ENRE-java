export const INDENT_SYMBOL = "\t";

export function getIndent(count: number) {
  return INDENT_SYMBOL.repeat(count);
}

export class FileDeclaration {
  packageDeclaration: PackageDeclaration;
  importStatements: Array<ImportStatement>;
  classDeclarations: Array<ClassDeclaration>;
  indentLevel: number;

  constructor(
    packageDeclarations: PackageDeclaration,
    importStatements: Array<ImportStatement>,
    classDeclarations: Array<ClassDeclaration>,
    indentLevel: number = 0,
  ) {
    this.packageDeclaration = packageDeclarations;
    this.importStatements = importStatements;
    this.classDeclarations = classDeclarations;
    this.indentLevel = indentLevel;
  }

  toString() {
    let packageString = this.packageDeclaration.toString();
    let importString = this.importStatements.map((x) => x.toString()).join("");
    let classString = this.classDeclarations.map((x) => x.toString()).join("");
    return `${packageString}\n${importString}\n${classString}`;
  }
}

export class ImportStatement implements Statement {
  name: string;
  indentLevel: number;

  constructor(name: string, indentLevel: number = 1) {
    this.name = name;
    this.indentLevel = indentLevel;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    return `${indentPrefix}import ${this.name};\n`;
  }
}

export class PackageDeclaration {
  name: string;
  indentLevel: number;

  constructor(name: string, indentLevel: number = 0) {
    this.name = name;
    this.indentLevel = indentLevel;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    return `${indentPrefix}package ${this.name};\n`;
  }
}

export class ClassDeclaration {
  modifiers: string | undefined;
  name: string;
  memberDeclarations: Array<VariableDeclaration>;
  methods: Array<MethodDeclaration>;
  indentLevel: number;

  constructor(modifiers: string | undefined,
              name: string,
              memberDeclaration: Array<VariableDeclaration>,
              methods: Array<MethodDeclaration>,
              indentLevel: number = 1) {
    this.modifiers = modifiers;
    this.name = name;
    this.memberDeclarations = memberDeclaration;
    this.methods = methods;
    this.indentLevel = indentLevel;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let modifierString = this.modifiers ? this.modifiers : "";
    let signaturePrefix = `${indentPrefix}${modifierString} class ${this.name} {\n`;
    let memberString = this.memberDeclarations.map((x) => x.toString()).join("");
    let methodString = this.methods.map((x) => x.toString()).join("");
    let signatureSuffix = `}\n`;
    return `${signaturePrefix}${memberString}\n${methodString}\n${signatureSuffix}`;
  }
}

export class MethodDeclaration {
  comment: CommentBlock | undefined;
  modifiers: string | undefined;
  name: string;
  parameters: Array<string>;
  body: CodeBlock | undefined;
  annotation: string | undefined;
  indentLevel: number;

  constructor(
    comment: CommentBlock | undefined,
    modifiers: string | undefined,
    name: string,
    parameters: Array<string>,
    body: CodeBlock | undefined,
    annotation: string | undefined,
    indentLevel: number = 1
  ) {
    this.comment = comment;
    this.modifiers = modifiers;
    this.name = name;
    this.parameters = parameters;
    this.body = body;
    this.annotation = annotation;
    this.indentLevel = indentLevel;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let parameterString = this.parameters ? this.parameters.join(", ") : "";
    let commentString = this.comment ? `${this.comment.toString()}\n` : "\n";
    let annotationString = this.annotation ? `${indentPrefix}@${this.annotation}\n` : "\n";
    let methodSignaturePrefix = `${indentPrefix}${this.modifiers} ${this.name}(${parameterString}) {\n`;
    let methodBodyString = this.body ? this.body.toString() : "\n";
    let methodSignatureSuffix = `${indentPrefix}}\n\n`;
    return `${commentString}${annotationString}${methodSignaturePrefix}${methodBodyString}${methodSignatureSuffix}`;
  }
}

export class Statement {
  indentLevel: number;

  constructor(indentLevel: number) {
    this.indentLevel = indentLevel;
  }

  toString(): string {
    return "";
  }
}

export class CommentBlock extends Statement {
  lines: Array<string>;

  constructor(lines: Array<string>, indentLevel: number = 1) {
    super(indentLevel);
    this.lines = lines;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let res = `${indentPrefix}/**\n`;
    for (const line of this.lines) {
      res += `${indentPrefix}  * ${line}\n`;
    }
    res += `${indentPrefix}  */`;
    return res;
  }
}

export class VariableDeclaration extends Statement {
  modifiers: string | undefined;
  variableType: string | undefined;
  name: string;
  initializer: string | undefined;

  constructor(
    modifiers: string | undefined,
    variableType: string | undefined,
    name: string,
    indentLevel: number,
    initializer?: string,
  ) {
    super(indentLevel);
    this.modifiers = modifiers;
    this.name = name;
    this.variableType = variableType;
    this.initializer = initializer;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let modifierString = this.modifiers ? `${this.modifiers} ` : "";
    let typeString = this.variableType ? `${this.variableType} ` : "";
    let initializerString = this.initializer ? ` = ${this.initializer}` : "";
    return `${indentPrefix}${modifierString}${typeString}${this.name}${initializerString};\n`;
  }
}

export class ThrowStatement extends Statement {
  exceptionClassName: string;
  message: string;

  constructor(exceptionClassName: string, message: string, indentLevel: number = 1) {
    super(indentLevel);
    this.exceptionClassName = exceptionClassName;
    this.message = message;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    return `${indentPrefix}throw new ${this.exceptionClassName}("${this.message}");\n`;
  }
}

export class CodeBlock {
  statements: Array<Statement>;
  indentLevel: number;

  constructor(statements: Array<Statement>, indentLevel: number = 1) {
    this.statements = statements;
    this.indentLevel = indentLevel;
  }

  toString() {
    return this.statements.map((x) => x.toString()).join("");
  }
}

export class ConditionalStatement extends Statement {
  condition: string;
  trueBranch: CodeBlock;
  falseBranch: CodeBlock;

  constructor(condition: string, trueBranch: CodeBlock, falseBranch: CodeBlock, indentLevel: number = 1) {
    super(indentLevel);
    this.condition = condition;
    this.trueBranch = trueBranch;
    this.falseBranch = falseBranch;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let res = `${indentPrefix}if (${this.condition}) {\n`;
    res += this.trueBranch.toString();
    if (this.falseBranch.statements.length > 0) {
      res += `${indentPrefix}} else {\n`;
      res += this.falseBranch.toString();
    }
    res += `${indentPrefix}}\n`;
    return res;
  }
}

export class MethodInvocationStatement extends Statement {
  invokeName: string;
  invokeArguments: Array<string>;

  constructor(invokeName: string, invokeArguments: Array<string>, indentLevel: number) {
    super(indentLevel);
    this.invokeName = invokeName;
    this.invokeArguments = invokeArguments;
  }

  toString() {
    let indentPrefix = getIndent(this.indentLevel);
    let argumentsString = this.invokeArguments.join(", ");
    return `${indentPrefix}${this.invokeName}(${argumentsString});\n`;
  }
}
