import parser from '@enre/doc-parser';
import finder from '@enre/doc-path-finder';
import {error} from '@enre/logging';
import {promises as fs} from 'fs';
import clean from '../cleaner';
import {
  capFirst,
  JUNIT_INDENT_LEVEL_CLASS,
  JUNIT_INDENT_LEVEL_PACKAGE,
  JUNIT_MODIFIERS_TEST_CLASS,
  JUnitBuilder
} from "./JUnitBuilder";
import * as path from "path";
import {Statement, ClassDeclaration, FileDeclaration, MethodDeclaration} from "./ASTTypes";

// TODO: Cache md5 and only regenerate files that were modified

function getClassNameFromCaseName(caseName: string | undefined) {
  if (caseName) {
    return caseName.split("-").map((x) => capFirst(x)).join("");
  }
  return undefined;
}


export default async function (opt: any) {
  let prevGroupName: string | undefined = undefined;
  let prevCaseName: string | undefined = undefined;
  let tests: Array<MethodDeclaration> = [];

  await parser(
    await finder(opt),
    async (entry, groupMeta) => {
    },
    undefined,
    // build cases into filesystem
    async (entry, caseObj, groupMeta) => {
      const casePath = `tests/cases/_${groupMeta.name}/_${caseObj.assertion.name}`;
      try {
        await fs.mkdir(casePath);
      } catch (e) {
      }
      const filePathList = [];
      for (const file of caseObj.code) {
        filePathList.push(file.path);
        const fullPath = `${casePath}/${file.path}`;
        const parent = path.dirname(fullPath);
        try {
          await fs.mkdir(parent, {recursive: true});
        } catch (e) {
          console.log(e);
        }
        try {
          await fs.writeFile(`${casePath}/${file.path}`, file.content);
        } catch (e) {
          console.log(e);
        }
      }

      if (prevGroupName === undefined || prevCaseName === undefined) {
        prevGroupName = groupMeta.name;
        prevCaseName = caseObj.assertion.name;
      }
      // write previous cases into file
      if (tests.length !== 0) {
        let className = `${getClassNameFromCaseName(prevCaseName)}Test`;
        let classCase = new ClassDeclaration(
          JUNIT_MODIFIERS_TEST_CLASS,
          className,
          JUnitBuilder.buildClassMembers(),
          tests,
          JUNIT_INDENT_LEVEL_CLASS,
        );
        let fileCase = new FileDeclaration(
          JUnitBuilder.buildPackageStatement(),
          JUnitBuilder.buildImportStatements(),
          [classCase],
          JUNIT_INDENT_LEVEL_PACKAGE,
        );
        const ast = fileCase.toString();
        try {
          await fs.writeFile(`tests/suites/${className}.java`, ast);
        } catch (e) {
          console.error(e);
        }
      }


      prevGroupName = groupMeta.name;
      prevCaseName = caseObj.assertion.name;
      tests = [];
      if (groupMeta.name !== "END_OF_PROCESS") {
        await clean(groupMeta.name);
      }
      // ignored package.json

      // build specific test methods and push each method into accumulatedCaseMethods
      // `tests` stores all methods; each element of `tests` represents all statements in a single method
      tests = [];
      tests.push(JUnitBuilder.buildBeforeMethodDeclaration(groupMeta.name, caseObj.assertion.name));
      tests.push(JUnitBuilder.buildAfterMethodDeclaration());
      if (caseObj.assertion.entity) {
        const entity = caseObj.assertion.entity;
        if (!entity.extra) {
          if (entity.type) {
            // @ts-ignore
            const typedCount = entity.items.filter(i => !i.negative && (i.type === entity.type)).length;
            tests.push(JUnitBuilder.buildOnlyContainsEntityMethodDeclaration(typedCount, entity.type()));
          }
        }

        for (const ent of entity.items) {
          // `test` stores single/multiple assertions within a method
          let additionalAssertions: Array<Statement> = [];
          switch (ent.type) {
            case "package":
              break;
            case "file":
              break;
            case "class":
              break;
            case "enum":
              break;
            case "enum constant":
              break;
            case "annotation":
              break;
            case "annotation member":
              break;
            case "interface":
              break;
            case "method":
              if (ent.Lambda) {
                additionalAssertions.push(JUnitBuilder.buildAssertionStmt("assertEquals", "((MethodEntity)ent).isLambda()", ent.Lambda ?? false));
              }
              break;
            case "module":
              break;
            case "record":
              if (ent.Modifier) {
                additionalAssertions.push(JUnitBuilder.buildAssertionStmt("assertEquals", "((RecordEntity)ent).getModifier()", `"${ent.Modifier}"`));
              }
              break;
            case "type parameter":
              break;
            case "variable":
              if (ent.global) {
                additionalAssertions.push(JUnitBuilder.buildAssertionStmt("assertEquals", "((VariableEntity)ent).getGlobal()", ent.global ?? false));
              }
              if (ent.rawType) {
                additionalAssertions.push(JUnitBuilder.buildAssertionStmt("assertEquals", "((VariableEntity)ent).getRawType()", `"${ent.rawType}"`));
              }
              break;
            default:
              error(`Entity type '${ent.type}' unimplemented for testing`);
              continue;
          }
          tests.push(JUnitBuilder.buildContainsEntityMethodDeclaration(
            ent.type,
            ent.name.printableName,
            ent.negative,
            ent.qualified,
            additionalAssertions,
            ent.loc.start?.line,
            -1,
            -1,
            -1,
          ));
        }

        if (caseObj.assertion.relation) {
          const relation = caseObj.assertion.relation;
          if (!relation.extra) {
            if (relation.type) {
              // @ts-ignore
              const typedCount = relation.items.filter(i => !i.negative && (i.type === relation.type)).length;
              tests.push(JUnitBuilder.buildOnlyContainsRelationMethodDeclaration(typedCount, relation.type));
            }
          }

          for (const [index, rel] of relation.items.entries()) {
            let additionalAssertions: Array<Statement> = [];
            switch (rel.type) {
              case "import":
                break;
              case "inherit":
                break;
              case "implement":
                break;
              case "contain":
                break;
              case "call":
                break;
              case "parameter":
                break;
              case "typed":
                break;
              case "usevar":
                break;
              case "set":
                break;
              case "modify":
                break;
              case "annotate":
                break;
              case "cast":
                break;
              case "override":
                break;
              case "reflect":
                break;
              case "define":
                break;
              default:
                error(`Relation type '${rel.type}' unimplemented for testing`);
                continue;
            }
            // @ts-ignore
            tests.push(
              JUnitBuilder.buildContainsRelationIndexMethodDeclaration(
                rel.type,
                index,
                rel.negative,
                rel.from.isFullName,
                rel.to.isFullName,
                rel.from.type,
                rel.to.type,
                rel.from.name,
                rel.to.name,
                additionalAssertions,
                rel.from.predicates?.loc?.start?.line,
                rel.to.predicates?.loc?.start?.line,
                rel.loc.start?.line,
                -1,
              )
            );
          }
        }
      }
    },
    /java/,
    'java',
    false,
  );
}
