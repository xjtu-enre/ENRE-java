# ENRE-Java
ENRE (ENtity Relationship Extractor) is a tool for extraction of code entity dependencies or relationships from source code. 

## Features

* Conforms to the latest Java progremming language specification of Oracle.

* Support for multirepo projects, the prerequisite is that these repos have the same package name management.

* Support for non-SDK restriction level matching of Android projects.

* Highly standardized, documentations are comprehensive and publicly available.

* Supports multiple usage patterns, including CLI and programmatic interfaces.

## Supported Language

|  Language  | Maximum Version |
|:----------:|:---------------:|
|    Java    |       17        |

## Documentation

Specifications on which kinds of entities and relations can be captured and any other details can be found in [docs](docs/README.md).

## Usage

### 1. Prepare the executable jar

The released jar of ENRE-Java is named as enre_java.jar

### 2. Set up Java environment

To execute enre_java.jar, you should set up Java environment: at least JDK 11 version. 

If the project is quite larger, such as base columns under Android, please increase the memory heap above 8G.

### 3. cmd usage

The usage command is:

```text
java -jar <executable> <lang> <dir> <project-name>
```

The detailed information of the parameter and option of the command is:

```text
Usage: enre_java [-hs] [-a=<aidl>] [-e=<external>] [-hd=<hidden>]
                 [-o=<outputFile>] [-d=<dir>]... <lang> <src> <projectName>
      <lang>          The lanauge of project files: []
      <src>           The directory to be analyzed
      <projectName>   The analyzed project file name
  -a, --aidl=<aidl>   If the analyzed project is an Android project which
                        contains .aidl files, please provide the corresponding .
                        java files which have the same relative path with the
                        original file
  -d, --dir=<dir>     The additional directories to be analyzed
  -e, --external=<external>
                      The third party APIs which need to identify
  -h, --help          Display this help and exit
      -hd, --hidden=<hidden>
                      The path of hiddenapi-flag.csv
  -o, --output=<outputFile>
                      The output file name, default is projectName-out
  -s, --slim          The slim output version, which removing the location and
                        external entity info.
```

To increase the memory heap, you can add -Xmx before -jar, like: 

```text
java -Xmx20G -jar <executable> <lang> <dir> <include-dir> <project-name>
```

- <executable> The executable jar package of ENRE-Java.
- <lang> The language of source code that will be analysed, now it must be java.
- <dir> The path of the source code that will be analysed.
- <project-name> A short alias name of the analysed source code project.

##### Example:

- Use enre_java.jar to analyse a demo project "halo_1.4.10" written in Java:

```text
# in windows platform
$java -jar enre_java.jar java demo-projects\halo_1.4.10 halo_1.4.10
```

After analysis, ENRE-Java finally outputs the resolved entities and dependencies in JSON files in current directory.

- Use enre_java.jar to analyse the `/base` directory under AOSP, also providing the corresponding `.aidl` path:

```text
# in windows platform
$java -jar enre_java.jar java ...\frameworks\base base -a <aidl-path>
```

- Use enre_java.jar to analyse the `/base` directory under AOSP, also providing several mirror paths which contain source files and share same project name:

```text
# in windows platform
$java -jar enre_java.jar java ...\frameworks\base base -d ...\base
```

## Testing

### Prerequisite

* Node.js 16~18

### Steps

ENRE-java is integrated with [ENRE-test](https://github.com/xjtu-enre/enre-test). All you need to do for performing unit test is running the following script:

1. Fetch the latest enre-java-test codebase locally:

```sh
python3 ./scripts/update_submodule.py
```

2. Generate test cases and suites:

```sh
python3 ./scripts/gen_tests.py
```
Test cases and `JUnit` java files will be generated under directory `src/test/resources/cases` and `src/test/java/client`.

you can execute all `JUnit` test cases by executing the following command in the project directory:

```sh
mvn clean test
```

or execute specific test case by passing class name:

```sh
mvn clean test -DTest=AClassDefinesAFieldTest
```

If you want to build the package without executing any test case:

```sh
mvn clean package assembly:single -DskipTests
```

