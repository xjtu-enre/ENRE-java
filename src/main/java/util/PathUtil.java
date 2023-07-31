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
        if (path.contains("//")){
            return path.replace("//", "/");
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
     * @param fullPath C:/.../project/src/java/pkg/file.java
     * @param projectName  project
     * @return pkg/file.java
     */
    public static String getPathInProject(String fullPath, String projectName){
        String[] temp;
        if(fullPath.contains(projectName)){
            temp = fullPath.split(projectName,2);
        } else {
            temp = fullPath.split(getLastStrByPathDelimiter(fullPath), 2);
        }
        if(temp[1].endsWith(".java") || temp[1].endsWith(".aidl")){
            return temp[1].substring(1);
        }
        return null;
    }

}
