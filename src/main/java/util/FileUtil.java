package util;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {

    // collect the file path under the src dir
    private ArrayList<String> fileNameList = new ArrayList<>();
    private ArrayList<String> aidlList = new ArrayList<>();
    private String aidlPath;
    private String unifyPath;
    private String projectName;

    public FileUtil(String projectPath){
        this.unifyPath = PathUtil.unifyPath(projectPath);
        this.projectName = PathUtil.getLastStrByPathDelimiter(this.unifyPath);
        RecursiveFindFile(projectPath);
    }

    public FileUtil(String projectPath, String aidlPath){
        this.unifyPath = PathUtil.unifyPath(projectPath);
        this.projectName = PathUtil.getLastStrByPathDelimiter(this.unifyPath);
        this.aidlPath = PathUtil.unifyPath(aidlPath);
        collectAidl(this.aidlPath);
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
            if(project.getAbsolutePath().endsWith(".aidl")){
                String aidl2Java = searchAidl(PathUtil.getPathInProject(PathUtil.unifyPath(project.getAbsolutePath()), this.projectName));
                if (aidl2Java != null){
                    this.fileNameList.add(aidl2Java);
                    System.out.println("AIDL");
                    System.out.println(PathUtil.getPathInProject(PathUtil.unifyPath(project.getAbsolutePath()), this.projectName));
                    System.out.println(aidl2Java);
                }
            }
            return;
        }
        try{
            for(File eachFile : project.listFiles() ){
                if(eachFile.isFile()){
                    if(eachFile.getAbsolutePath().endsWith(".java")){
                        this.fileNameList.add(PathUtil.unifyPath(eachFile.getAbsolutePath()));
                    }
                    if(eachFile.getAbsolutePath().endsWith(".aidl")){
                        String aidl2Java = searchAidl(PathUtil.getPathInProject(PathUtil.unifyPath(eachFile.getAbsolutePath()), this.projectName));
                        if (aidl2Java != null){
                            this.fileNameList.add(aidl2Java);
                            System.out.println("AIDL");
                            System.out.println(PathUtil.getPathInProject(PathUtil.unifyPath(eachFile.getAbsolutePath()), this.projectName));
                            System.out.println(aidl2Java);
                        }
                    }
                }
                else if (eachFile.isDirectory()){
                    RecursiveFindFile(eachFile.getAbsolutePath());
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
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

    public void collectAidl(String aidlPath){
        File project = new File(aidlPath);

        if(project.isFile()){
            if(project.getAbsolutePath().endsWith(".java")){
                this.aidlList.add(PathUtil.unifyPath(project.getAbsolutePath()));
            }
        }
        for(File eachFile : project.listFiles()){
            if(eachFile.isFile()){
                if(eachFile.getAbsolutePath().endsWith(".java")){
                    this.aidlList.add(PathUtil.unifyPath(eachFile.getAbsolutePath()));
                }
            }
            else if (eachFile.isDirectory()){
                collectAidl(eachFile.getAbsolutePath());
            }
        }
    }

    public String searchAidl(String aidl){
        String[] aidlName = aidl.split("/");
        aidl = aidlName[aidlName.length-1];
        for(String aidlJava: this.aidlList){
            if(aidlJava.contains(aidl.replace("aidl", "java"))){
                return aidlJava;
            }
        }
        return null;
    }


}
