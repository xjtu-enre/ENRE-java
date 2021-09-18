package writer;


public class DotUtil {

    public static final String FILTER_FUNC_CLASS_DEP = "function_class_dep";
    public static final String FILTER_DEFAULT_DEP = "all_dep";

    public static final String FILTER_NO_DEP = "node";
    public static final String FILTER_FUNCTION_DEP = "function_function_dep";
    public static final String FILTER_CLASS_DEP = "class_class_dep";
    public static final String FILTER_FILE_FOLDER_DEP = "ff_ff_dep";


    public static final String FOLDER_STYLE = "bold";
    public static final String FILE_STYLE = "dashed";
    public static final String CLASS_STYLE = "rounded";
    public static final String FUNCTION_STYLE = "solid";
    public static final String DEFAULT_STYLE = "filled";

    public static final String L_SQUARE_BRACKET = "[";
    public static final String R_SQUARE_BRACKET = "]";
    public static final String EQUAL = "=";
    public static final String STYLE = "style";
    public static final String LABLE = "label";
    public static final String DUMMY = "GRAPH_DUMMY";
    public static final String DUMMY_NODE_ATTRIBUTE = "[shape=point, style=invis]";

    public static final String DIGRAPH = "strict digraph";
    public static final String SUBGRAPH = "subgraph";
    public static final String CLUSTER = "cluster";
    public static final String BLANK = " ";
    public static final String L_LACE_BRACKET = "{";
    public static final String R_LACE_BRACKET = "}";
    public static final String SEMI_COLON = ";";
    public static final String COMMA = ",";
    public static final String NEWLINE = "\n";
    public static final String ARROW = " -> ";
    public static final String GRAPH_ATTRIBUTE = "compound = true;"
            + NEWLINE + "concentrate=true;"
            + NEWLINE + "node [shape=box];"
            + NEWLINE;
}
