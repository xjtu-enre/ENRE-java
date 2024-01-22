#! /usr/bin/env bash

git submodule init
git submodule update

if [ -d "./enre-java-test" ]; then
  cp -rv ./docs enre-java-test/docs
  [ ! -d "./src/test/java/client" ] && mkdir -p "./src/test/java/client"
  [ ! -d "./src/test/resources" ] && mkdir -p "./src/test/resources"
  pushd enre-java-test && \
    npm install && \
    node --experimental-specifier-resolution=node "src/index.js" && \
    popd && \
    cp -rv ./enre-java-test/tests/suites/*.java "./src/test/java/client/" && \
    cp -rv ./enre-java-test/tests/cases "./src/test/resources/"
fi

