@echo off

SET ENRE_REPO=../
SET TEST_REPO=%CD%\..\enre-java-test
git "submodule" "init"
git "submodule" "update"
pushd "%TEST_REPO%" && git "checkout" "main" && popd
IF "-d" "./%TEST_REPO%" (
  [ "-d" "./%TEST_REPO%/docs" "]" && DEL /S "./%TEST_REPO%/docs"
  [ "-d" "./%TEST_REPO%/tests" "]" && DEL /S "./%TEST_REPO%/tests"
  COPY  "%ENRE_REPO%\docs" "%TEST_REPO%\docs"
  [ "!" "-d" "%ENRE_REPO%/src/test/java/client" "]" && mkdir "-vp" "%ENRE_REPO%/src/test/java/client"
  [ "!" "-d" "%ENRE_REPO%/src/test/resources" "]" && mkdir "-vp" "%ENRE_REPO%/src/test/resources"
  pushd "%TEST_REPO%" && npm "install" && node "--experimental-specifier-resolution=node" "%TEST_REPO%/src/index.js" && popd && COPY  "%TEST_REPO%/tests/suites/*.java" "%ENRE_REPO%/src/test/java/client/" && COPY  "%TEST_REPO%/tests/cases" "%ENRE_REPO%/src/test/resources/"
)
