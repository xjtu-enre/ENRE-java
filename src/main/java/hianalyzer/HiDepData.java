package hianalyzer;

import util.Configure;

import java.util.HashMap;
import java.util.Map;

public class HiDepData {
    private static HiDepData ourInstance = new HiDepData();

    private Map<Integer, String[]> variables = new HashMap<Integer, String[]>();
    private Map<Integer, String[]> methods = new HashMap<Integer, String[]>();
    private Map<Integer, String[]> interfaces = new HashMap<Integer, String[]>();
    private Map<Integer, String[]> classes = new HashMap<Integer, String[]>();
    private Map<Integer, String[]> files = new HashMap<Integer, String[]>();
    private Map<Integer, String[]> packages = new HashMap<Integer, String[]>();

    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> variableDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> methodDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> interfaceDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> classDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> fileDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> packageDeps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();


    public static HiDepData getInstance() {
        return ourInstance;
    }

    private HiDepData() {
    }

    public void addEntity(int id, String name, String type, int parentId) {

        if(type.equals(Configure.BASIC_ENTITY_METHOD)) {
            addEntityOperation(methods, id, name, type, parentId);
        }
        else if(type.equals(Configure.BASIC_ENTITY_VARIABLE)) {
            addEntityOperation(variables, id, name, type, parentId);
        }
        else if(type.equals(Configure.BASIC_ENTITY_CLASS)) {
            addEntityOperation(classes, id, name, type, parentId);
        }
        else if(type.equals(Configure.BASIC_ENTITY_INTERFACE)) {
            addEntityOperation(interfaces, id, name, type, parentId);
        }
        else if(type.equals(Configure.BASIC_ENTITY_FILE)) {
            addEntityOperation(files, id, name, type, parentId);
        }
        else if(type.equals(Configure.BASIC_ENTITY_PACKAGE)) {
            addEntityOperation(packages, id, name, type, parentId);
        }
    }

    private void addEntityOperation(Map<Integer, String[]> entities, int id, String name, String type, int parentId) {
        String[] arr = new String[] {name, type, Integer.toString(parentId)};
        entities.put(id, arr);
    }


    public void addDep(String entityType, int id1, int id2, String depType, String primitiveType, int weight) {
        if(entityType.equals(Configure.BASIC_ENTITY_METHOD)) {
            addDepOperation(methodDeps, id1, id2, depType, primitiveType, weight);
        }
        else if (entityType.equals(Configure.BASIC_ENTITY_VARIABLE)) {
            addDepOperation(variableDeps, id1, id2, depType, primitiveType, weight);
        }
        else if (entityType.equals(Configure.BASIC_ENTITY_CLASS)) {
            addDepOperation(classDeps, id1, id2, depType, primitiveType, weight);
        }
        else if (entityType.equals(Configure.BASIC_ENTITY_INTERFACE)) {
            addDepOperation(interfaceDeps, id1, id2, depType, primitiveType, weight);
        }
        else if (entityType.equals(Configure.BASIC_ENTITY_FILE)) {
            addDepOperation(fileDeps, id1, id2, depType, primitiveType, weight);
        }
        else if (entityType.equals(Configure.BASIC_ENTITY_PACKAGE)) {
            addDepOperation(packageDeps, id1, id2, depType, primitiveType, weight);
        }

    }

    private void addDepOperation(Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps,
                                 int id1, int id2, String depType, String primitiveType, int weight) {
        if(!deps.containsKey(id1)) {
           deps.put(id1, new HashMap<Integer, Map<String, Map<String, Integer>>>());
        }
        if(!deps.get(id1).containsKey(id2)) {
            deps.get(id1).put(id2, new HashMap<String, Map<String, Integer>>());
        }
        if(!deps.get(id1).get(id2).containsKey(depType)) {
            deps.get(id1).get(id2).put(depType, new HashMap<String, Integer>());
        }
        if(!deps.get(id1).get(id2).get(depType).containsKey(primitiveType)) {
            deps.get(id1).get(id2).get(depType).put(primitiveType, 0);
        }
        int oldweight = deps.get(id1).get(id2).get(depType).get(primitiveType);
        deps.get(id1).get(id2).get(depType).put(primitiveType, weight + oldweight);
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getVariableDeps() {
        return variableDeps;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getMethodDeps() {
        return methodDeps;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getClassDeps() {
        return classDeps;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getInterfaceDeps() {
        return interfaceDeps;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getFileDeps() {
        return fileDeps;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getPackageDeps() {
        return packageDeps;
    }

    public Map<Integer, String[]> getMethods() {
        return methods;
    }

    public Map<Integer, String[]> getClasses() {
        return classes;
    }

    public Map<Integer, String[]> getFiles() {
        return files;
    }

    public Map<Integer, String[]> getPackages() {
        return packages;
    }

    public Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> getAllDeps() {
        Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
        deps.putAll(variableDeps);
        deps.putAll(methodDeps);
        deps.putAll(classDeps);
        deps.putAll(interfaceDeps);
        deps.putAll(fileDeps);
        deps.putAll(packageDeps);
        return deps;
    }

    public Map<Integer, String[]> getAllEntities() {
        Map<Integer, String[]> map = new HashMap<Integer, String[]>();
        map.putAll(variables);
        map.putAll(methods);
        map.putAll(classes);
        map.putAll(interfaces);
        map.putAll(files);
        map.putAll(packages);
        return map;
    }

}
