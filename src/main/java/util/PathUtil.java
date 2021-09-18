package util;

public class PathUtil {

    /**
     * To resolve the classpath or import statement.
     * To parse the file and module
     * @param str "a/b/c"
     * @return subStr "a/b"
     */
    public static String deleteLastStrByPathDelimiter(String str){
        String subStr;
        String[] components = str.split("/");
        subStr = components[0];
        for (int index = 1; index < components.length - 1; index++){
            subStr += "/";
            subStr += components[index];
        }
        return subStr;
    }

    /**
     * To get the last string in the classpath or import path
     *
     * @param str "a/b/c"
     * @return "c"
     */
    public static String getLastStrByPathDelimiter(String str){
        String[] components = str.split("/");
        return components[components.length - 1];
    }

    /**
     * Unify filepath into a same mode "a/b/c"
     *
     * @param path "a\\b\\c" or "a/b/c"
     * @return "a/b/c"
     */
    public static String unifyPath(String path){
        if (path.contains("\\")){
            String[] components = path.split("\\\\");
            return String.join("/", components);
        }
        return path;
    }

    /**
     * To resolve the classpath or import statement.
     * To parse the file and module
     * @param str "a.b.c"
     * @return subStr "a.b"
     */
    public static String deleteLastStrByDot(String str){
        String subStr;
        String[] components = str.split("\\.");
        subStr = components[0];
        for (int index = 1; index < components.length - 1; index++){
            subStr += ".";
            subStr += components[index];
        }
        return subStr;
    }

    /**
     * To get the last string in the classpath or import path
     *
     * @param str "a.b.c"
     * @return "c"
     */
    public static String getLastStrByDot(String str){
        String[] components = str.split("\\.");
        return components[components.length - 1];
    }

    /**
     * Get the path inside project
     * @param fullpath C:/.../project/src/java/pkg/file.java
     * @param projectName  project
     * @return pkg/file.java
     */
    public static String getPathInProject(String fullpath, String projectName){
        String[] temp;
        temp = fullpath.split(projectName,2);
        if(temp[1].endsWith(".java")){
            return temp[1].substring(1);
        }
        return null;
    }

}
