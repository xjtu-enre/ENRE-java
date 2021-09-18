package util;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {

    // collect the file path under the src dir
    private ArrayList<String> fileNameList = new ArrayList<>();
    private String unifyPath;
    private String projectName;

    public FileUtil(String projectPath){
        this.unifyPath = PathUtil.unifyPath(projectPath);
        this.projectName = PathUtil.getLastStrByPathDelimiter(this.unifyPath);
        RecursiveFindFile(projectPath);
    }

    /**
     * Acquire the file from outside to inside
     * so if packageA contains packageB, we get files in A first, then packageB
     * and the stack which record the entities' ids will record A first, then B.
     *
     * @param dir
     */
    public void RecursiveFindFile(String dir){
        //initialize the project according to the dir path
        File project = new File(dir);

        if(project.isFile()){
            if(project.getAbsolutePath().endsWith(".java")){
                this.fileNameList.add(PathUtil.unifyPath(project.getAbsolutePath()));
            }
            return;
        }
        for(File eachFile : project.listFiles() ){
            if(eachFile.isFile()){
                if(eachFile.getAbsolutePath().endsWith(".java")){
                    this.fileNameList.add(PathUtil.unifyPath(eachFile.getAbsolutePath()));
                }
            }
            else if (eachFile.isDirectory()){
                RecursiveFindFile(eachFile.getAbsolutePath());
            }
        }
    }

    public ArrayList<String> getFileNameList() {
        return fileNameList;
    }

    public String getCurrentProjectName() {
        return this.projectName;
    }

    public String getUnifyPath(){
        return this.unifyPath;
    }

    public String getProjectPath(){
        return this.unifyPath.split(projectName)[0];
    }

}
