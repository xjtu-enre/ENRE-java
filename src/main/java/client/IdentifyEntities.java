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
import java.util.Map;

public class IdentifyEntities {

    private String project_path;
    private String project_name;

    public IdentifyEntities(String project_path, String project_name){
        this.project_path = PathUtil.unifyPath(project_path);
        this.project_name = project_name;
    }

    public String getProject_path() {
        return project_path;
    }

    public void setProject_path(String project_path) {
        this.project_path = PathUtil.unifyPath(project_path);
    }

    public void run(){

        FileUtil current = new FileUtil(this.getProject_path());
        //set the version of java
        ASTParser parser = ASTParser.newParser(AST.JLS_Latest);
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
        final ArrayList<CompilationUnitPair> pairs = new ArrayList<CompilationUnitPair>(current.getFileNameList().size());
        FileASTRequestor requester = new FileASTRequestor() {
            @Override
            public void acceptAST(String source, CompilationUnit ast) {
                pairs.add(new CompilationUnitPair(source, ast));
            }
        };

        parser.createASTs(current.getFileNameList().toArray(new String[0]), null, new String[0], requester, null);

        System.out.println("Parsing...");

        for(CompilationUnitPair pair : pairs){
//            if(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name).equals("src/main/java/run/halo/app/service/impl/OptionServiceImpl.java")){
//                pair.ast.accept(new EntityVisitor(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name), pair.ast));
//            }
            pair.ast.accept(new EntityVisitor(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name), pair.ast));
            System.out.println(PathUtil.getPathInProject(PathUtil.unifyPath(pair.source),this.project_name));
        }

        System.out.println("Entities identified successfully...");

    }

}
