# ENRE-JAVA testing framework

## About

* Using [ENRE testing framework within ENRE-ts](https://github.com/xjtu-enre/enre-ts), we are able to build our own ENRE testing framework based on different programming languages and generate test code based on certain test framework.
* For instance, we will generate test code based on `JUnit` for ENRE-JAVA.

## Installation && Build

The whole testing framework is built based on ENRE-ts, so we need to clone the ENRE-ts repo and modify some of its files.

Since ENRE-ts's testing framework does not provide multi-language support on its own, we have to modify its files manually. So if ENRE-ts's code updated, we need to sync its updates by ourselves.

Here are the steps required to generate test source code files:

1. Clone ENRE-ts repo to `${ENRE_TS_REPO}` directory:

```sh
git clone https://github.com/xjtu-enre/enre-ts.git ${ENRE_TS_REPO}
```

2. Clean up existing files:

```sh
rm -rf "${ENRE_TS_REPO}/docs"
rm -rf "${ENRE_TS_REPO}/tests"
rm -rf ${ENRE_JAVA_REPO}/src/test/java/client/*.java
rm -rf "${ENRE_JAVA_REPO}/src/test/resources/cases"
```

3. Copy some source code files and ENRE-java's docs directory to your ENRE-ts repo directory:

```sh
rsync "${ENRE_JAVA_REPO}/test/basic.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/basic.ts"
rsync "${ENRE_JAVA_REPO}/test/raw.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/raw.ts"
rsync "${ENRE_JAVA_REPO}/test/index.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/index.ts"
rsync "${ENRE_JAVA_REPO}/test/cli.ts" "${ENRE_TS_REPO}/packages/enre-test-generator/src/cli.ts"
rsync "${ENRE_JAVA_REPO}/test/tag-anonymous.ts" "${ENRE_TS_REPO}/packages/enre-naming/src/xml/tag-anonymous.ts"
rsync -a "${ENRE_JAVA_REPO}/test/java/" "${ENRE_TS_REPO}/packages/enre-test-generator/src/java/"
rsync -a "${ENRE_JAVA_REPO}/docs" "${ENRE_TS_REPO}/"
```

4. cd into your ENRE-ts directory and build ENRE-ts:

```sh
cd ${ENRE_TS_REPO}
npm install
npm run build
```

5. Generate tests directory (check packages/enre-test-generator/lib/cli.js for further cli parameters):

```sh
node --experimental-specifier-resolution=node packages/enre-test-generator/lib/cli.js
```

6. rsync generated files to ENRE-JAVA repo:

```sh
mkdir -p "${ENRE_JAVA_REPO}/src/test/java/client/"
rsync ${ENRE_TS_REPO}/tests/suites/*.java "${ENRE_JAVA_REPO}/src/test/java/client/"
rsync -a ${ENRE_TS_REPO}/tests/cases "${ENRE_JAVA_REPO}/src/test/resources/"
```

7. Then you can execute tests in command line under ENRE-JAVA repo directory:

```sh
mvn clean test
```

or execute tests in IDE.

If you want to execute single test case in command line:

```sh
mvn clean test -Dtest="${TEST_CLASS_QUALIFIED_NAME}"
```

If you want to build jar package without executing tests:

```sh
mvn clean package assembly:single -DskipTests
```

[jsonexport](./jsonexport.ts) is used to extract groundtruth from YAML code block to json files.

## Issues

### blockrange/identifier range

* ENRE-workflow standards the line number of ENRE platform to be identifier range, so does ENRE-JAVA's docs. However ENRE-JAVA's actual line number extracted is using blockrange, so the line number of result and docs do not match.
* Now ENRE-JAVA's test framework only compares the `startLine` between result and ground truth.

### Unimplemented entities/relations

* There are some entities in ENRE-JAVA remain unimplemented yet.
* `Lambda` in `Method` Entity
* `Record` entity
* `Module` entity

### `startLine` of `Package` entity

* Every `startLine` of `package` entity in result is `0`, however in docs/realiy, `startLine` of `startLine` should be `1`.

### Lacking `UseVar` relations

* `MethodUseParameterTest`

### Lacking `Modify` relations

* `ModifyLocalVarTest`

### `Variable` entity's rawType should be qualifiedName or name?

* `VariableDeclarationStatementTest`: `String` or `java.lang.String`?
* `ForLoopVariableTest`: `Integer` or `java.lang.Integer`?


