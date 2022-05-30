package client;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import util.CompilationUnitPair;
import util.FileUtil;
import util.PathUtil;
import visitor.EntityVisitor;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Map;

public class IdentifyEntities {

    private String project_path;
    private String project_name;
    private ArrayList<String> additional_path = new ArrayList<>();
    private String aidl_path = null;

    public IdentifyEntities(String project_path, String project_name){
        this.project_path = PathUtil.unifyPath(project_path);
        this.project_name = project_name;
    }

    public IdentifyEntities(String project_path, String project_name, String[] additional_path){
        this.project_path = PathUtil.unifyPath(project_path);
        this.project_name = project_name;
        for (String path: additional_path){
            this.additional_path.add(PathUtil.unifyPath(path));
        }
    }

    public IdentifyEntities(String project_path, String project_name, String aidl_path){
        this.project_path = PathUtil.unifyPath(project_path);
        this.project_name = project_name;
        this.aidl_path = PathUtil.unifyPath(aidl_path);
    }

    public IdentifyEntities(String project_path, String project_name, String aidl_path, String[] additional_path){
        this.project_path = PathUtil.unifyPath(project_path);
        this.project_name = project_name;
        this.aidl_path = PathUtil.unifyPath(aidl_path);
        for (String path: additional_path){
            this.additional_path.add(PathUtil.unifyPath(path));
        }
    }

    public String getProject_path() {
        return project_path;
    }

    public void setProject_path(String project_path) {
        this.project_path = PathUtil.unifyPath(project_path);
    }

    public String getAidl_path(){
        return aidl_path;
    }

    public ArrayList<String> getAdditional_path() {
        return additional_path;
    }

    public void run(){

        FileUtil current;
        if(this.getAidl_path() != null){
            current = new FileUtil(this.getProject_path(), this.getAidl_path());
        } else {
            current = new FileUtil(this.getProject_path());
        }

        ArrayList<String> whole_file_list = current.getFileNameList();
        if (!this.getAdditional_path().isEmpty()){
            for (String additionPath: this.getAdditional_path()){
                FileUtil addition;
                if(this.getAidl_path() != null){
                    addition = new FileUtil(additionPath, this.getAidl_path());
                } else {
                    addition = new FileUtil(additionPath);
                }
                whole_file_list.addAll(addition.getFileNameList());
            }
        }

        //set the version of java
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);

        //set the environment
        String[] sources = {this.getProject_path()};
        //String[] classpath = {"C:/Program Files/Java/jdk-11.0.9/lib/jrt-fs.jar"};
        String[] classpath = null;

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);

        parser.setUnitName(current.getCurrentProjectName());
        final ArrayList<CompilationUnitPair> pairs = new ArrayList<CompilationUnitPair>(whole_file_list.size());


        try{
            FileASTRequestor requester = new FileASTRequestor() {
                @Override
                public void acceptAST(String source, CompilationUnit ast) {
                    pairs.add(new CompilationUnitPair(source, ast));
                }
            };
            parser.createASTs(whole_file_list.toArray(new String[0]), null, new String[0], requester, null);
        }
        catch (NullPointerException e){
            for(String filePath: whole_file_list){
                parser.setSource(filePath.toCharArray());
                pairs.add(new CompilationUnitPair(filePath, (CompilationUnit)parser.createAST(null)));
            }
        }


        System.out.println("Parsing...");

        for(CompilationUnitPair pair : pairs){

            try{
                System.out.println(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name));
//                if("src/main/java/helloJDT/LauncherAccessibilityDelegate.java".equals(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name))){
//                    pair.ast.accept(new EntityVisitor(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name), pair.ast));
//                }
                pair.ast.accept(new EntityVisitor(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name), pair.ast));
            }
            catch (EmptyStackException e){
                e.printStackTrace();
                System.out.println("Empty Stack: "+ pair.source);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                System.out.println("Null Pointer: "+ pair.source);
            }
        }

        System.out.println("Entities identified successfully...");

    }

}
