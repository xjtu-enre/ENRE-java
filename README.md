# ENRE-Java

> Entities and dependencies extractor for Java projects based on @Eclipse JDT/parser

ENRE (ENtity Relationship Extractor) is a tool for extraction of code entity dependencies or relationships from source code. The resolved entity types include: 

| Entity Type | Description                                           |
| ----------- | ----------------------------------------------------- |
| Package     | Collect related Files                                 |
| File        | The .java files which save the whole java information |
| Class       | Like a constructor of objects                         |
| Enum        | Like a class only included fixed constants            |
| Annotation  | To get program information while running through it   |
| Interface   | A way to achieve Abstract in Java                     |
| Method      | To perform specific activity                          |
| Variable    | A container which stores values                       |

The resolved dependency types include:

| Dependency type | Description                                                  |
| --------------- | ------------------------------------------------------------ |
| Import          | A file imports other class, enum or package, or static imports method or var |
| Inherit         | A class inherits the other class                             |
| Implement       | A class implement an interface                               |
| Contain         | A package contains files, a file contain classes, enums and other types, etc. |
| Call            | A method calls other methods                                 |
| Parameter       | A method needs parameters to use                             |
| Return          | A method needs to return some results                        |
| Use             | A method use variables which are initialized                 |
| Set             | A method set some variables                                  |
| Modify          | A method modify variables which have been set                |
| Annotate        | A annotation annotate entities                               |
| Cast            | A method cast another type to a variable                     |
| Override        | A method which has the same name, return type and parameter type of the super method |
| Reflect         | A entity which call Class.forname("...") method to get a specific type. |

# Usage

## 1. Prepare the executable jar

The released jar of ENRE-Java is named as enre_java.jar

## 2. Set up Java environment

To execute enre_java.jar, you should set up Java environment: at least JDK 11 version. 

If the project is quite larger, such as base columns under Android, please increase the memory heap above 8G.

## 3. cmd usage

The usage command is:

```java
java -jar <executable> <lang> <dir> <include-dir> <project-name>
```

To increase the memory heap, you can add -Xmx before -jar, like: 

```
java -Xmx8192m -jar <executable> <lang> <dir> <include-dir> <project-name>
```

- <executable> The executable jar package of ENRE-Java.
- <lang> The language of source code that will be analysed, now it must be java.
- <dir> The path of the source code that will be analysed.
- <include-dir> Now it just be null is fine.
- <project-name> A short alias name of the analysed source code project.

#### Example:

Use enre_java.jar to analyse a demo project "halo_1.4.10" written in Java:

```
# in windows platform
$java -jar enre_java.jar java demo-projects\halo_1.4.10 null halo_1.4.10
```

After analysis, ENRE-Java finally outputs the resolved entities and dependencies in JSON files in current directory.


