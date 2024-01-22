#! /usr/bin/env bash

ENRE_REPO="./"
TEST_REPO="./enre-java-test"

git submodule init
git submodule update

pushd $TEST_REPO && \
  git pull origin main && \
  git checkout main && \
  popd

if [ -d "./$TEST_REPO" ]; then
  [ -d "./$TEST_REPO/docs" ] && rm -rfv "./$TEST_REPO/docs"
  [ -d "./$TEST_REPO/tests" ] && rm -rfv "./$TEST_REPO/tests"
  cp -rv $ENRE_REPO/docs $TEST_REPO/docs
  [ ! -d "$ENRE_REPO/src/test/java/client" ] && mkdir -vp "$ENRE_REPO/src/test/java/client"
  [ ! -d "$ENRE_REPO/src/test/resources" ] && mkdir -vp "$ENRE_REPO/src/test/resources"
  pushd "$TEST_REPO" && \
    npm install && \
    node --experimental-specifier-resolution=node "./src/index.js" && \
    popd && \
    cp -rv $TEST_REPO/tests/suites/*.java "$ENRE_REPO/src/test/java/client/" && \
    cp -rv "$TEST_REPO/tests/cases" "$ENRE_REPO/src/test/resources/"
fi

