package visitor.relationInf;

import entity.BaseEntity;
import entity.FileEntity;
import util.*;

import java.util.ArrayList;

public abstract class RelationInterface {

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public abstract String entityStatis();

    public abstract String dependencyStatis();

    public ArrayList<Tuple<String, String>> getDepByType(String level, String depType) {
        if(depType.equals(Configure.RELATION_IMPLEMENT)) {
            return getImplementDeps(level);
        }
        if(depType.equals(Configure.RELATION_INHERIT)) {
            return getInheritDeps(level);
        }
        if(depType.equals(Configure.RELATION_SET)) {
            return getVariableSets(level);
        }
        if(depType.equals(Configure.RELATION_USE)) {
            return getVariableUses(level);
        }
        if(depType.equals(Configure.RELATION_PARAMETER)) {
            return getMethodParas(level);
        }
        if(depType.equals(Configure.RELATION_RETURN)) {
            return getMethodRets(level);
        }
        if(depType.equals(Configure.RELATION_CALL)) {
            return getMethodCalls(level);
        }
        if(depType.equals(Configure.RELATION_IMPORT)) {
            return getImportDeps(level);
        }
        return null;

    }


    public ArrayList<String> getAllFiles() {
        ArrayList<String> files = new ArrayList<String>();
        for (BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof FileEntity) {
                String fileName = entity.getQualifiedName();
                files.add(fileName);
            }
        }
        return files;
    }

    public abstract ArrayList<Tuple<String, String>> getImportDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getImplementDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getInheritDeps(String level);
    public abstract ArrayList<Tuple<String, String>> getMethodCalls(String level);
    public abstract ArrayList<Tuple<String, String>> getVariableSets(String level);
    public abstract ArrayList<Tuple<String, String>> getVariableUses(String level);
    public abstract ArrayList<Tuple<String, String>> getMethodParas(String level);
    public abstract ArrayList<Tuple<String, String>> getMethodRets(String level);
}
