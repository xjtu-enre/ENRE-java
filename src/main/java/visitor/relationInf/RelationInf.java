package visitor.relationInf;

import com.sun.xml.bind.v2.schemagen.xmlschema.Annotation;
import util.Configure;
import util.Tuple;
import entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RelationInf extends RelationInterface {


    @Override
    public String entityStatis() {
        int packageCount = 0;
        int fileCount = 0;
        int classCount = 0;
        int interfaceCount = 0;
        int enumCount = 0;
        int annotationCount = 0;
        int annotationMemberCount = 0;
        int methodCount = 0;
        int varCount = 0;


        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof PackageEntity) {
                packageCount ++;
            }
            else if(entity instanceof FileEntity) {
                fileCount ++;
            }
            else if(entity instanceof MethodEntity) {
                methodCount ++;
            }
            else if(entity instanceof ClassEntity ) {
                classCount ++;
            }
            else if(entity instanceof InterfaceEntity){
                interfaceCount ++;
            }
            else if(entity instanceof EnumEntity){
                enumCount ++;
            }
            else if(entity instanceof AnnotationEntity){
                annotationCount ++;
            }
            else if(entity instanceof AnnotationTypeMember){
                annotationMemberCount ++;
            }
            else if(entity instanceof VariableEntity){
                varCount ++;
            }

        }
        String str = Configure.NULL_STRING;
        str += ("Package: " + packageCount + "\n");
        str += ("File: " + fileCount + "\n");
        str += ("Class: " + classCount + "\n");
        str += ("Interface: " + interfaceCount + "\n");
        str += ("Enum: " + enumCount + "\n");
        str += ("Annotation: " + annotationCount + "\n");
        str += ("Annotation Member: " + annotationMemberCount + "\n");
        str += ("Method: " + methodCount + "\n");
        str += ("variable: " + varCount + "\n");
        return str;
    }

    @Override
    public String dependencyStatis() {
        Map<String, Integer> depMap = new HashMap<String, Integer>();
        depMap.put(Configure.RELATION_IMPORT, 0);
        depMap.put(Configure.RELATION_IMPLEMENT, 0);
        depMap.put(Configure.RELATION_INHERIT, 0);
        depMap.put(Configure.RELATION_CALL, 0);
        depMap.put(Configure.RELATION_SET, 0);
        depMap.put(Configure.RELATION_USE, 0);
        depMap.put(Configure.RELATION_PARAMETER, 0);
        depMap.put(Configure.RELATION_MODIFY, 0);
        depMap.put(Configure.RELATION_CALL_NON_DYNAMIC, 0);
        depMap.put(Configure.RELATION_CAST, 0);
        depMap.put(Configure.RELATION_ANNOTATE, 0);
        depMap.put(Configure.RELATION_OVERRIDE, 0);
        depMap.put(Configure.RELATION_REFLECT, 0);
        for (BaseEntity entity :singleCollect.getEntities()) {
            for (Tuple<String, Integer> re : entity.getRelation()) {
                if(re.getRelation().equals(Configure.RELATION_IMPORT) ||
                        re.getRelation().equals(Configure.RELATION_INHERIT) ||
                        re.getRelation().equals(Configure.RELATION_IMPLEMENT) ||
                        re.getRelation().equals(Configure.RELATION_SET) ||
                        re.getRelation().equals(Configure.RELATION_USE) ||
                        re.getRelation().equals(Configure.RELATION_CALL) ||
                        re.getRelation().equals(Configure.RELATION_PARAMETER) ||
                        re.getRelation().equals(Configure.RELATION_MODIFY) ||
                        re.getRelation().equals(Configure.RELATION_CALL_NON_DYNAMIC) ||
                        re.getRelation().equals(Configure.RELATION_CAST) ||
                        re.getRelation().equals(Configure.RELATION_ANNOTATE) ||
                        re.getRelation().equals(Configure.RELATION_OVERRIDE) ||
                        re.getRelation().equals(Configure.RELATION_REFLECT)
                ) {
                    int old = depMap.get(re.getRelation());
                    depMap.put(re.getRelation(), old + 1);
                }
            }
        }
        String str = Configure.NULL_STRING;
        for(Map.Entry<String, Integer> entry : depMap.entrySet()) {
            str += entry.getKey();
            str += ": ";
            str += Integer.toString(entry.getValue());
            str += "\n";
        }
        return str;
    }

    @Override
    public ArrayList<Tuple<String, String>> getImportDeps(String level) {
        ArrayList<Tuple<String, String>> deps = new ArrayList<>();
        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof FileEntity){
                for(Tuple<String, Integer> relation : entity.getRelation()){
                    if(relation.getRelation().equals(Configure.RELATION_IMPORT)){
                        int imported = relation.getId();
                        String classOrPackageName = singleCollect.getEntityById(imported).getQualifiedName();
                        Tuple<String,String> dep = new Tuple<>(entity.getQualifiedName(),classOrPackageName);
                        deps.add(dep);
                    }
                }
            }
        }
        return deps;
    }

    @Override
    public ArrayList<Tuple<String, String>> getImplementDeps(String level) {
        ArrayList<Tuple<String, String>> deps = new ArrayList<>();
        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof ClassEntity){
                for(Tuple<String, Integer> relation : entity.getRelation()){
                    if(relation.getRelation().equals(Configure.RELATION_IMPLEMENT)){
                        int imported = relation.getId();
                        String classOrPackageName = singleCollect.getEntityById(imported).getQualifiedName();
                        Tuple<String,String> dep = new Tuple<>(entity.getQualifiedName(),classOrPackageName);
                        deps.add(dep);
                    }
                }
            }
        }
        return deps;
    }

    @Override
    public ArrayList<Tuple<String, String>> getInheritDeps(String level) {
        ArrayList<Tuple<String, String>> deps = new ArrayList<>();
        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof ClassEntity || entity instanceof InterfaceEntity){
                for(Tuple<String, Integer> relation : entity.getRelation()){
                    if(relation.getRelation().equals(Configure.RELATION_INHERIT)){
                        int imported = relation.getId();
                        String classOrPackageName = singleCollect.getEntityById(imported).getQualifiedName();
                        Tuple<String,String> dep = new Tuple<>(entity.getQualifiedName(),classOrPackageName);
                        deps.add(dep);
                    }
                }
            }
        }
        return deps;
    }

    @Override
    public ArrayList<Tuple<String, String>> getMethodCalls(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getVariableSets(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getVariableUses(String level) {
        return null;
    }

    @Override
    public ArrayList<Tuple<String, String>> getMethodParas(String level) {
        ArrayList<Tuple<String, String>> deps = new ArrayList<>();
        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof MethodEntity){
                for(Tuple<String, Integer> relation : entity.getRelation()){
                    if(relation.getRelation().equals(Configure.RELATION_PARAMETER)){
                        int imported = relation.getId();
                        String classOrPackageName = singleCollect.getEntityById(imported).getQualifiedName();
                        Tuple<String,String> dep = new Tuple<>(entity.getQualifiedName(),classOrPackageName);
                        deps.add(dep);
                    }
                }
            }
        }
        return deps;
    }

    @Override
    public ArrayList<Tuple<String, String>> getMethodRets(String level) {
        ArrayList<Tuple<String, String>> deps = new ArrayList<>();
        for(BaseEntity entity : singleCollect.getEntities()) {
            if(entity instanceof MethodEntity){
                String retExpression = ((MethodEntity) entity).getReturnExpression();
                String retype = ((MethodEntity) entity).getReturnType();
                Tuple<String, String> dep = new Tuple<>(entity.getQualifiedName(),retype + " " + retExpression);

                deps.add(dep);
            }
        }
        return deps;
    }

}
