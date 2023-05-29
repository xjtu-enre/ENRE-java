package hianalyzer;

import entity.*;
import entity.properties.Relation;
import util.Configure;
import util.Tuple;
import util.SingleCollect;

import java.util.Map;


public class HiDeper {
    private HiDepData hiDepData = HiDepData.getInstance();
    private SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();


    public void run() {
        for(BaseEntity entity : singleCollect.getEntities()) {
            buildEntity(entity.getId(), getEntityType(entity));
            buildDep(entity);
        }
    }


    private String getEntityType(BaseEntity entity) {
        String entityType = "";
        if(entity instanceof MethodEntity) {
            entityType = Configure.BASIC_ENTITY_METHOD;
        }
        else if(entity instanceof FileEntity) {
            entityType = Configure.BASIC_ENTITY_FILE;
        }
        else if(entity instanceof ClassEntity) {
            entityType = Configure.BASIC_ENTITY_CLASS;
        }
        else if(entity instanceof PackageEntity) {
            entityType = Configure.BASIC_ENTITY_PACKAGE;
        }
        return entityType;
    }


    private void buildEntity (int entityId, String type) {
        BaseEntity absEntity = singleCollect.getEntities().get(entityId);
        String name = absEntity.getName();
        hiDepData.addEntity(entityId, name, type, absEntity.getParentId());
    }

    /**
     * according to primitive relation to build hierarchical dependencies:
     *
     * function->function
     * class->class
     * file->file
     * folder->folder
     */
    private void buildDep(BaseEntity entity) {
        for (Relation relation : entity.getRelation()) {
            String primitiveType = relation.getKind();
            int id2 = relation.getToEntity();

            if (primitiveType.equals(Configure.RELATION_IMPLEMENT)
                    || primitiveType.equals(Configure.RELATION_INHERIT)) {
                primitiveType = Configure.RELATION_EXTEND;
            }

            if (primitiveType.equals(Configure.RELATION_SET)
                    || primitiveType.equals(Configure.RELATION_USE)
                    || primitiveType.equals(Configure.RELATION_CALL)
                    || primitiveType.equals(Configure.RELATION_PARAMETER)
                    || primitiveType.equals(Configure.RELATION_IMPLEMENT)
                    || primitiveType.equals(Configure.RELATION_INHERIT)) {
                buildHierModel(entity.getId(), id2, primitiveType);
            }
        }
    }


    private void buildHierModel(int id1, int id2, String primitiveType) {
        int func1 = findFunction(id1);
        int func2 = findFunction(id2);
        int class1 = findClass(id1);
        int class2 = findClass(id2);
        int file1 = findFile(id1);
        int file2 = findFile(id2);
        int folder1 = findFolder(id1);
        int folder2 = findFolder(id2);
        String depType = Configure.IMPLICIT_DEPENDENCY;

        if(func1 != -1 && func2 != -1 && func1 != func2) {
            if(func1 == id1 && func2 == id2) {
                depType = Configure.EXPLICIT_DEPENDENCY;
            }
            hiDepData.addDep(Configure.BASIC_ENTITY_METHOD, func1, func2, depType, primitiveType, 0);
            depType = Configure.IMPLICIT_DEPENDENCY;
        }
        if(class1 != -1 && class2 != -1 && class1 != class2) {
            if(class1 == id1 && class2 == id2) {
                depType = Configure.EXPLICIT_DEPENDENCY;
            }
            hiDepData.addDep(Configure.BASIC_ENTITY_CLASS, class1, class2, depType, primitiveType, 0);
            depType = Configure.IMPLICIT_DEPENDENCY;
        }
        if(file1 != -1 && file2!= -1 && file1 != file2) {
            if(file1 == id1 && file2 == id2) {
                depType = Configure.EXPLICIT_DEPENDENCY;
            }
            hiDepData.addDep(Configure.BASIC_ENTITY_FILE, file1, file2, depType, primitiveType, 0);
            depType = Configure.IMPLICIT_DEPENDENCY;
        }
        if(folder1 != -1 && folder2 != -1 && folder1 != folder2) {
            if(folder1 == id1 && folder2 == id2) {
                depType = Configure.EXPLICIT_DEPENDENCY;
            }
            hiDepData.addDep(Configure.BASIC_ENTITY_PACKAGE, folder1, folder2, depType, primitiveType, 0);
            depType = Configure.IMPLICIT_DEPENDENCY;

        }
    }

    private int findFunction(int id) {
        while(id != -1 && !(singleCollect.getEntities().get(id) instanceof MethodEntity)) {
            id = singleCollect.getEntities().get(id).getParentId();
        }
        if(id != -1 && singleCollect.getEntities().get(id) instanceof MethodEntity) {
            return id;
        }
        return -1;
    }


    private int findClass(int id) {
        while(id != -1 && !(singleCollect.getEntities().get(id) instanceof ClassEntity)) {
            id = singleCollect.getEntities().get(id).getParentId();
        }
        if(id != -1 && singleCollect.getEntities().get(id) instanceof ClassEntity) {
            return id;
        }
        return -1;
    }
    private int findFile(int id) {
        while(id != -1 && !(singleCollect.getEntities().get(id) instanceof FileEntity)) {
            id = singleCollect.getEntities().get(id).getParentId();
        }
        if(id != -1 && singleCollect.getEntities().get(id) instanceof FileEntity) {
            return id;
        }
        return -1;
    }

    private int findFolder(int id) {
        while(id != -1 && !(singleCollect.getEntities().get(id) instanceof PackageEntity)) {
            id = singleCollect.getEntities().get(id).getParentId();
        }
        if(id != -1 && singleCollect.getEntities().get(id) instanceof PackageEntity) {
            return id;
        }
        return -1;
    }

    public void tmpOutput() {
        Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps;

        System.out.println("function deps: ");
        deps = hiDepData.getMethodDeps();
        outputMap(deps);

        System.out.println("class deps: ");
        deps = hiDepData.getClassDeps();
        outputMap(deps);

        System.out.println("file deps: ");
        deps = hiDepData.getFileDeps();
        outputMap(deps);

        System.out.println("folder deps: ");
        deps = hiDepData.getPackageDeps();
        outputMap(deps);

    }

    private void outputMap(Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps) {
        for(Map.Entry<Integer, Map<Integer, Map<String, Map<String, Integer>>>> entry : deps.entrySet()) {
            int id1 = entry.getKey();
            for (Map.Entry<Integer, Map<String, Map<String, Integer>>> entry2 : entry.getValue().entrySet()) {
                int id2 = entry2.getKey();
                System.out.println(singleCollect.getEntities().get(id1).getName());
                System.out.println(singleCollect.getEntities().get(id2).getName());
                System.out.println(entry2.getValue());
                System.out.println("\n");
            }
        }
    }
}
