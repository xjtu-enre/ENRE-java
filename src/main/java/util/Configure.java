package util;

import java.io.File;

public class Configure {

    private Configure() {}

    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";
    public static final String MAC = "mac";

    public static final String NULL_STRING = "";
    public static final String COMMA = ",";
    public static final String LEFT_PARENTHESES = "(";
    public static final String RIGHT_PARENTHESES = ")";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String STAR = "*";
    public static final String POINTER = "*";
    public static final String ONE_SPACE_STRING = " ";
    public static final String SEMI_COLON = ";";
    public static final String BLANK_IDENTIFIER = "_";
    public static final String SQUARE_BRACKETS = "[]";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String ELLIPSIS = "...";
    public static final String LEFT_CURLY_BRACE = "{";
    public static final String RIGHT_CURLY_BRACE = "}";
    public static final String STRING_COLON = ":";
    public static final String EQUAL = "=";

    public static final String BASIC_ENTITY_METHOD = "method";
    public static final String BASIC_ENTITY_CLASS = "class";
    public static final String BASIC_ENTITY_FILE = "file";
    public static final String BASIC_ENTITY_PACKAGE = "package";
    public static final String BASIC_ENTITY_INTERFACE = "interface";
    public static final String BASIC_ENTITY_VARIABLE = "variable";

    public static final String IMPLICIT_DEPENDENCY = "Implicit";
    public static final String EXPLICIT_DEPENDENCY = "Explicit";


    //file->package.class or package.*
    public static final String RELATION_IMPORT = "Import";
    //package.class or package->file
    public static final String RELATION_IMPORTED_BY = "Imported by";

    //field->method; method->method (method invocation)
    public static final String RELATION_CALL = "Call";
    public static final String RELATION_CALLED_BY = "Called by";

    //super.meth
    public static final String RELATION_CALL_NON_DYNAMIC = "Call non-dynamic";
    public static final String RELATION_CALLBY_NON_DYNAMIC = "Callby non-dynamic";

    //method->var
    public static final String RELATION_PARAMETER = "Parameter";
    public static final String RELATION_PARAMETERED_BY = "Parametered by";

    //method->var or expression
    public static final String RELATION_RETURN = "Return";
    public static final String RELATION_RETURNED_BY = "Returned by";

    //method->OperandVar
    public static final String RELATION_SET = "Set";
    public static final String RELATION_SETED_BY = "Seted by";

    //method->OperandVar
    public static final String RELATION_USE = "Use";
    public static final String RELATION_USED_BY = "Used by";

    //method->OperandVar
    public static final String RELATION_MODIFY = "Modify";
    public static final String RELATION_MODIFIED_BY = "Modified by";

    //class->class, interface->interface
    public static final String RELATION_INHERIT = "Inherit";
    public static final String RELATION_INHERITED_BY = "Inherited by";

    //class->interface
    public static final String RELATION_IMPLEMENT = "Implement";
    public static final String RELATION_IMPLEMENTED_BY = "Implemented by";

    public static final String RELATION_CONTAIN = "Contain";
    public static final String RELATION_CONTAINED_BY = "Contained by";

    public static final String RELATION_DEFINE = "Define";
    public static final String RELATION_DEFINED_BY = "Defined by";

    public static final String RELATION_CAST = "Cast";
    public static final String RELATION_CAST_BY = "Cast by";

    public static final String RELATION_ANNOTATE = "Annotate";
    public static final String RELATION_ANNOTATED_BY = "Annotated by";

    public static final String RELATION_EXTEND = "Extend";

    //public static final String RELATION_LEVEL_FILE = "File";
    public static final String RELATION_LEVEL_COMPONENT = "Component";

    //kinds of local block, which are inside function/method body.
    public static final String LOCAL_BLOCK_METHOD = "MethodBlock";
    public static final String LOCAL_BLOCK_UNNAMED_BLOCK = "UnknownBlock";
    public static final String LOCAL_BLOCK_FOR = "ForBlock";
    public static final String LOCAL_BLOCK_ENHANCED_FOR = "EnhancedForBlock";
    public static final String LOCAL_BLOCK_IF = "IfBlock";
    public static final String LOCAL_BLOCK_ELSE = "ElseBlock";
    public static final String LOCAL_BLOCK_SWITCH = "SwitchBlock";
    public static final String LOCAL_BLOCK_SWITCH_CASE_CLAUSE = "SwitchCaseClauseBlock"; //include default

    //kinds of annotation target type
    public static final String ANNOTATION_TYPE = "Another annotation";
    public static final String CONSTRUCTOR = "Constructor";
    public static final String FIELD = "Field";
    public static final String LOCAL_VARIABLE = "Local variable";
    public static final String METHOD = "Method";
    public static final String PACKAGE = "Package";
    public static final String PARAMETER = "Parameter";
    public static final String CLASS_TYPE = "Class";
    public static final String INTERFACE_TYPE = "Interface";
    public static final String ENUMERATION_TYPE = "Enumeration";

    //kinds of annotation retention
    public static final String SOURCE = "Source";
    public static final String CLASS = "Class";
    public static final String RUNTIME = "Runtime";

    private static Configure configure = new Configure();
    public static Configure getConfigureInstance() {
        return configure;
    }

    public static final String OS_DOT_NAME = "os.name";

    private String inputSrcPath;
    private String analyzedProjectName = "helloJDT";
    private String lang = "java";
    private String curr_pro_suffix = ".java";

    private String outputDotFile = analyzedProjectName + ".dot";
    private String outputCsvNodeFile = analyzedProjectName + "_node.csv";
    private String outputCsvEdgeFile = analyzedProjectName + "_edge.csv";
    private String outputJsonFile = analyzedProjectName  + "_dep.json";
    private String outputXmlFile = analyzedProjectName + "_dep.xml";
    private String attributeName = analyzedProjectName + "-sdsm";
    private String schemaVersion = "1.0";

    public void setDefault() {
        outputJsonFile = analyzedProjectName  + "_dep.json";
        outputDotFile = analyzedProjectName + ".dot";
        outputXmlFile = analyzedProjectName + "_dep.xml";
        outputCsvNodeFile = analyzedProjectName + "_node.csv";
        outputCsvEdgeFile = analyzedProjectName + "_edge.csv";
        attributeName = analyzedProjectName + "-sdsm";
    }

    public String getInputSrcPath() {
        return inputSrcPath;
    }

    public String getUnifiedInputSrcpath() {
        return PathUtil.unifyPath(inputSrcPath);
    }

    public void setInputSrcPath(String inputSrcPath) {
        this.inputSrcPath = inputSrcPath;
    }

    public String getAnalyzedProjectName() {
        return analyzedProjectName;
    }

    /**
     * os-relevant
     * @param analyzedProjectName project name
     */
    public void setAnalyzedProjectName(String analyzedProjectName) {
        //new File(analyzedProjectName + "-out").mkdir();
//        if(OsUtil.isWindows()) {
//            this.analyzedProjectName = analyzedProjectName + "-out\\" + analyzedProjectName;
//        }
//        if(OsUtil.isMac() || OsUtil.isLinux()) {
//            this.analyzedProjectName = analyzedProjectName + "-out/" + analyzedProjectName;
//        }
        this.analyzedProjectName = analyzedProjectName;
    }

    public String getOutputJsonFile() {
        return outputJsonFile;
    }

    public String getOutputCsvEdgeFile() {
        return outputCsvEdgeFile;
    }

    public String getOutputCsvNodeFile() {
        return outputCsvNodeFile;
    }

    public void setOutputJsonFile(String outputJsonFile) {
        this.outputJsonFile = outputJsonFile;
    }

    public String getOutputXmlFile() {
        return outputXmlFile;
    }

    public void setOutputXmlFile(String outputXmlFile) {
        this.outputXmlFile = outputXmlFile;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCurr_pro_suffix() {
        return curr_pro_suffix;
    }

    public String getOutputDotFile() {
        return outputDotFile;
    }

}
