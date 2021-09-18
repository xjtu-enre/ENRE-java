package writer;

import entity.*;
import util.Configure;
import util.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * write (primitive dependencies)into graph dot
 */
public class DotWriter {

    private SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public void writeDot(String filter, String fileName) {

        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(out != null) {
            writeGraph(out, filter);
            out.close();
            System.out.println("Export " + fileName);
        }
    }




    private void writeGraph(PrintWriter out, String filter) {
        //digraph {
        out.println(DotUtil.DIGRAPH + DotUtil.BLANK + DotUtil.L_LACE_BRACKET);
        //compund = true;
        //node [shape=box];
        //edge [color=blue];
        out.println(DotUtil.GRAPH_ATTRIBUTE);

        ArrayList<Integer> roots = findRoot();
        //write nodes
        writeSubGraph(out, roots, filter);
        if(!filter.equals(DotUtil.FILTER_NO_DEP)) {
            writeEdge(out, filter);
        }

        //"} //graph end"
        out.println(DotUtil.R_LACE_BRACKET);
    }

    private void writeEdge(PrintWriter out, String filter) {
        String finalStr = "";
        for (BaseEntity entity : singleCollect.getEntities()) {
            int id1 = entity.getId();
            String id1Str = genNodeIdInEdge(id1);
            if(!isCaredEntity(id1, filter)) {
                continue;
            }
            for (Tuple<String, Integer> relation: entity.getRelation()) {
                if(isCaredEntity(relation.getId(), filter)
                        && isCaredRelation(relation.getRelation(), filter)) {
                    int id2 = relation.getId();
                    String id2Str = genNodeIdInEdge(id2);
                    finalStr += (id1Str + DotUtil.ARROW + id2Str + DotUtil.SEMI_COLON + DotUtil.NEWLINE);
                }
            }

        }
        out.println(finalStr);
    }


    private String genNodeIdInEdge(int id) {
        if(isLeaf(id)) {
            return Integer.toString(id);
        }
        else {
            return DotUtil.DUMMY + Integer.toString(id);
        }
    }

    private void writeSubGraph(PrintWriter out, ArrayList<Integer> childIds, String filter) {
        for (int id : childIds) {
            String style = getStyle(id);
            String label = singleCollect.getEntities().get(id).getName();
            String idStr = Integer.toString(id);
            String labelAttr = DotUtil.LABLE + DotUtil.EQUAL + "\"" + label + "\"";
            String styleAttr = DotUtil.STYLE + DotUtil.EQUAL + style;
            if (isLeaf(id) && isCaredEntity(id, filter)) {
                // is leaf
                genLeaf(out, idStr, labelAttr, styleAttr);
            } else {
                //is subgraph cluster
                if (isCaredEntity(id, filter)) {
                    genSubGraph(out, idStr, labelAttr, styleAttr);
                }

                ArrayList<Integer> newChildIds = singleCollect.getEntities().get(id).getChildrenIds();
                writeSubGraph(out, newChildIds, filter);
                if (isCaredEntity(id, filter)) {
                    out.println(DotUtil.R_LACE_BRACKET);
                }
            }
        }
    }

    private void genLeaf(PrintWriter out, String idStr, String labelAttr, String styleAttr) {
        //node1 [stypeattr, labelattr];
        String str = idStr + DotUtil.BLANK;
        str += DotUtil.L_SQUARE_BRACKET;
        str += (styleAttr + DotUtil.COMMA + labelAttr);
        str += DotUtil.R_SQUARE_BRACKET;
        str += DotUtil.SEMI_COLON;
        out.println(str);
    }

    private void genSubGraph(PrintWriter out, String idStr, String labelAttr, String styleAttr) {
        String dummy = DotUtil.DUMMY + idStr;
        String dummayAttr = dummy + DotUtil.BLANK + DotUtil.DUMMY_NODE_ATTRIBUTE + DotUtil.SEMI_COLON;
        idStr = DotUtil.CLUSTER + idStr;
        //subgraph {
        String str = DotUtil.SUBGRAPH + DotUtil.BLANK + idStr + DotUtil.BLANK + DotUtil.L_LACE_BRACKET;
        out.println(str);

        str = styleAttr + DotUtil.SEMI_COLON + labelAttr + DotUtil.SEMI_COLON;
        out.println(str);

        out.println(dummayAttr);
    }


    private boolean isLeaf (int id) {
        if(singleCollect.getEntities().get(id).getChildrenIds().isEmpty()) {
            return true;
        }
        return false;
    }

    private String getStyle(int id) {
        if(singleCollect.isMethod(id)) {
            return DotUtil.FUNCTION_STYLE;
        }
        if(singleCollect.isClass(id)) {
            return DotUtil.CLASS_STYLE;
        }
        if(singleCollect.isFile(id)) {
            return DotUtil.FILE_STYLE;
        }
        if(singleCollect.isPackage(id)) {
            return DotUtil.FOLDER_STYLE;
        }
        return DotUtil.DEFAULT_STYLE;
    }


    private ArrayList<Integer> findRoot() {
        ArrayList<Integer> rootes = new ArrayList<Integer>();
        for (BaseEntity entity : singleCollect.getEntities()) {
            int id = entity.getId();
            if(entity.getParentId() == -1 && (entity instanceof PackageEntity)) {
               rootes.add(id);
            }
        }
        return rootes;
    }



    private boolean isCaredRelation(String dep, String filter) {
        if(filter.equals(DotUtil.FILTER_FUNCTION_DEP) && dep.equals(Configure.RELATION_CALL)) {
            return true;
        }
        else if(filter.equals(DotUtil.FILTER_CLASS_DEP) &&
                (dep.equals(Configure.RELATION_INHERIT)
                        || dep.equals(Configure.RELATION_IMPLEMENT))
        ) {
            return true;
        }
        else if(filter.equals(DotUtil.FILTER_FILE_FOLDER_DEP) && dep.equals(Configure.RELATION_IMPORT)) {
            return true;
        }
        else if(filter.equals(DotUtil.FILTER_FUNC_CLASS_DEP) &&
                (dep.equals(Configure.RELATION_PARAMETER) || dep.equals(Configure.RELATION_RETURN))) {
            return true;
        }
        else if(filter.equals(DotUtil.FILTER_DEFAULT_DEP)) {
            if (dep.equals(Configure.RELATION_IMPORT)
                    || dep.equals(Configure.RELATION_IMPLEMENT)
                    || dep.equals(Configure.RELATION_INHERIT)
                    || dep.equals(Configure.RELATION_CALL)
                    || dep.equals(Configure.RELATION_PARAMETER)
                    || dep.equals(Configure.RELATION_RETURN)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCaredEntity(int id, String filter) {
        if(id == -1) {
            return false;
        }
        if(filter.equals(DotUtil.FILTER_NO_DEP) || filter.equals(DotUtil.FILTER_DEFAULT_DEP)) {
            if(singleCollect.isPackage(id)
                || singleCollect.isFile(id)
                || singleCollect.isClass(id)
                || singleCollect.isMethod(id)) {
                return true;
            }
        }
        else if(filter.equals(DotUtil.FILTER_FILE_FOLDER_DEP)) {
            if(singleCollect.isPackage(id) || singleCollect.isFile(id)) {
                return true;
            }
        }

        else if(filter.equals(DotUtil.FILTER_CLASS_DEP)) {
            if(singleCollect.isClass(id)) {
                return true;
            }
        }
        else if(filter.equals(DotUtil.FILTER_FUNC_CLASS_DEP)) {
            if(singleCollect.isClass(id) || singleCollect.isMethod(id)) {
                return true;
            }
        }
        else if(filter.equals(DotUtil.FILTER_FUNCTION_DEP)) {
            if(singleCollect.isMethod(id)) {
                return true;
            }
        }

        return false;
    }


}
