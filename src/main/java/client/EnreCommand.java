package client;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.List;

@Command(name = "enre_java")
public class EnreCommand {

    @Parameters(index = "0", defaultValue = "java", description = "The lanauge of project files: [${COMPLETION-CANDIDATES}]")
    private String lang;
    @Parameters(index = "1", description = "The directory to be analyzed")
    private String src;
    @Parameters(index = "2",  description = "The analyzed project file name")
    private String projectName;
    @Option(names = {"-d", "--dir"},  description = "The additional directories to be analyzed")
    private String[] dir = new String[] {};
    @Option(names = {"-a", "--aidl"},  description = "If the analyzed project is an Android project which contains .aidl files, " +
            "please provide the corresponding .java files which have the same relative path with the original file ")
    private String aidl;
    @Option(names = {"-hd", "--hidden"},  description = "The path of hiddenapi-flag.csv")
    private String hidden;
    @Option(names = {"-o", "--output"},  description = "The output file name, default is projectName-out")
    private String outputFile;
    @Option(names = {"-h","--help"}, usageHelp = true, description = "Display this help and exit")
    public boolean help;
    @Option(names = {"-k", "--sdkPath"},  description = "The sdk source code directory paths if target project is an android app, if you want the sdk library class to be analyzed as well.")
    private String[] sdkSourcePaths = new String[]{};
    @Option(names = {"-e","--external"}, description = "The third party APIs which need to identify")
    private String external;
    @Option(names = {"-s", "--slim"}, description = "The slim output version, which removing the location and external entity info. ")
    private boolean slim;

    public EnreCommand() {
    }
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public String getSrc() {
        return src;
    }
    public void setSrc(String src) {
        this.src = src;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setDir(String[] dir) {
        this.dir = dir;
    }

    public String[] getDir() {
        return dir;
    }

    public void setAidl(String aidl) {
        this.aidl = aidl;
    }

    public String getAidl() {
        return aidl;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String[] getSdkSourcePaths() {
        return sdkSourcePaths;
    }

    public void setSdkSourcePaths(String[] sdkSourcePaths) {
        this.sdkSourcePaths = sdkSourcePaths;
    }

    public String getHidden() {
        return hidden;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isSlim() {return slim;}

    public void setExternal(String external) {
        this.external = external;
    }

    public String getExternal() {
        return external;
    }

}
