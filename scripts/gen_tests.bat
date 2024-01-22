@echo off

git "submodule" "init"
git "submodule" "update"
pushd "enre-java-test" && git "pull" "origin" "main" && git "checkout" "main" && popd
IF "-d" "%CD%\enre-java-test" (
  [ "-d" "%CD%\enre-java-test\docs" "]" && DEL /S "%CD%\enre-java-test\docs"
  [ "-d" "%CD%\enre-java-test\tests" "]" && DEL /S "%CD%\enre-java-test\tests"
  COPY  "%CD%\docs" "enre-java-test\docs"
  [ NOT "-d" "%CD%\src\test\java\client" "]" && mkdir "-vp" "%CD%\src\test\java\client"
  [ NOT "-d" "%CD%\src\test\resources" "]" && mkdir "-vp" "%CD%\src\test\resources"
  pushd "enre-java-test" && npm "install" && node "--experimental-specifier-resolution=node" "src\index.js" && popd && COPY  "./enre-java-test/tests/suites/*.java" "./src/test/java/client/" && COPY  "%CD%\enre-java-test\tests\cases" "./src/test/resources/"
)
