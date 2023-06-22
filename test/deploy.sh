#! /usr/bin/env bash

export NODE_ENV="test"
ENRE_TS_REPO="$HOME/build/enre-ts"
ENRE_JAVA_REPO="$HOME/fun/enre-java"

exec_copy() {
    # copy modified files to ENRE-ts repo
    rsync "${ENRE_JAVA_REPO}/test/basic.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/basic.ts"
    rsync "${ENRE_JAVA_REPO}/test/raw.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/raw.ts"
    rsync "${ENRE_JAVA_REPO}/test/index.ts" "${ENRE_TS_REPO}/packages/enre-doc-meta-parser/src/case-meta/index.ts"
    rsync "${ENRE_JAVA_REPO}/test/cli.ts" "${ENRE_TS_REPO}/packages/enre-test-generator/src/cli.ts"
    rsync "${ENRE_JAVA_REPO}/test/tag-anonymous.ts" "${ENRE_TS_REPO}/packages/enre-naming/src/xml/tag-anonymous.ts"
    rsync -a "${ENRE_JAVA_REPO}/test/java/" "${ENRE_TS_REPO}/packages/enre-test-generator/src/java/"
}

exec_prebuild() {
    # clean up old generated files and docs directory
    rm -rf "$ENRE_TS_REPO/docs"
    rm -rf "$ENRE_TS_REPO/tests"
    rm -rf $ENRE_JAVA_REPO/src/test/java/client/*.java
    rm -rf "$ENRE_JAVA_REPO/src/test/resources/cases"

    # rsync `docs` directory to ENRE-ts repo
    rsync -a "$ENRE_JAVA_REPO/docs" "${ENRE_TS_REPO}"
}

exec_build() {
    # npm build
    cd $ENRE_TS_REPO && \
        npm install && \
        npm run build && \
        node --experimental-specifier-resolution=node packages/enre-test-generator/lib/cli.js
    cd -
    # rsync generated files to ENRE-JAVA
    rsync $ENRE_TS_REPO/tests/suites/*.java "$ENRE_JAVA_REPO/src/test/java/client/"
    rsync -a $ENRE_TS_REPO/tests/cases "$ENRE_JAVA_REPO/src/test/resources/"
}

exec_copy && exec_prebuild && exec_build
