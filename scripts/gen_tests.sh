#! /usr/bin/env bash

git submodule init
git submodule update
pushd enre-java-test && \
  git checkout main && \
  popd

if [ -d "./enre-java-test" ]; then
  [ -d "./enre-java-test/docs" ] && rm -rfv "./enre-java-test/docs"
  [ -d "./enre-java-test/tests" ] && rm -rfv "./enre-java-test/tests"
  cp -rv ./docs enre-java-test/docs
  [ ! -d "./src/test/java/client" ] && mkdir -vp "./src/test/java/client"
  [ ! -d "./src/test/resources" ] && mkdir -vp "./src/test/resources"
  pushd enre-java-test && \
    npm install && \
    node --experimental-specifier-resolution=node "src/index.js" && \
    popd && \
    cp -rv ./enre-java-test/tests/suites/*.java "./src/test/java/client/" && \
    cp -rv ./enre-java-test/tests/cases "./src/test/resources/"
fi

