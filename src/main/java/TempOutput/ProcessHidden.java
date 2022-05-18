package TempOutput;

import com.opencsv.CSVReader;
import entity.MethodEntity;
import entity.VariableEntity;
import util.Tuple;
import util.Triple;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessHidden {
    HashMap<String, ArrayList<HiddenEntity>> result = new HashMap<>();

    public void setResult(HashMap<String, ArrayList<HiddenEntity>> result) {
        this.result = result;
    }

    public void addEntity(String qualifiedName, HiddenEntity entity){
        if (this.result.containsKey(qualifiedName)){
            this.result.get(qualifiedName).add(entity);
        }else {
            ArrayList<HiddenEntity> hiddenEntities = new ArrayList<>();
            hiddenEntities.add(entity);
            this.result.put(qualifiedName, hiddenEntities);
        }
    }

    public HashMap<String, ArrayList<HiddenEntity>> getResult(){
        return this.result;
    }

    static class HiddenEntity {

        String qualifiedName = null;
        ArrayList<String> hiddenApi = new ArrayList<>();
        ArrayList<String> parameter = new ArrayList<>();
        String rawType = null;

        public String getQualifiedName() {
            return qualifiedName;
        }

        public void setQualifiedName(String qualifiedName) {
            this.qualifiedName = qualifiedName;
        }

        public ArrayList<String> getHiddenApi() {
            return hiddenApi;
        }

        public void addHiddenApi(String hiddenApi){
            this.hiddenApi.add(hiddenApi);
        }

        public void setHiddenApi(ArrayList<String> hiddenApis) {
            this.hiddenApi = hiddenApis;
        }

        public ArrayList<String> getParameter() {
            return parameter;
        }

        public void setParameter(ArrayList<String> parameter) {
            this.parameter = parameter;
        }

        public String getRawType() {
            return rawType;
        }

        public void setRawType(String rawType) {
            this.rawType = rawType;
        }

        @Override
        public String toString() {
            return "hiddenEntity{" +
                    "qualifiedName='" + qualifiedName + '\'' +
                    ", hiddenApi='" + hiddenApi + '\'' +
                    ", parameter=" + parameter +
                    ", rawType='" + rawType + '\'' +
                    '}';
        }
    }

    public String process_type_signature(String value){
        switch (value) {
            case "Z":
                return "boolean";
            case "B":
                return "byte";
            case "C":
                return "char";
            case "S":
                return "short";
            case "I":
                return "int";
            case "J":
                return "long";
            case "F":
                return "float";
            case "D":
                return "double";
            case "V":
                return "void";
        }
        if (value.startsWith("L")){
            String t = value.substring(1).replace("/", ".").replace("$", ".");
            if (t.endsWith(";")){
                t = t.substring(0, t.length()-1);
            }
            return t;
        }else if (value.contains("[")){
            return "list-"+process_type_signature(value.substring(1));
        }else {
            return "";
        }
//        else if (value.startsWith("Z") || value.startsWith("B") || value.startsWith("C") || value.startsWith("S")
//                || value.startsWith("I") || value.startsWith("J") || value.startsWith("F") || value.startsWith("D") ){
//            process_type_signature(value.substring(0, 1));
//            process_type_signature(value.substring(1));
//        }
    }

    public String process_class(String class_descriptor) {
        if (class_descriptor.contains("$$")){
            //pending
            return null;
        } else if (class_descriptor.contains("$")){
            return class_descriptor.substring(1).replace("$", ".").replace("/", ".").replace(";", "");
        } else {
            return class_descriptor.substring(1).replace("/", ".").replace(";", "");
        }
    }



    public Tuple<String, String> process_field(String field) {
        if (field.contains(":")){
            String fieldName = field.split(":",2)[0];
            String fieldType = process_type_signature(field.split(":", 2)[1]);
            return new Tuple<>(fieldName, fieldType);
        }
        return null;
    }


    public ArrayList<String> process_parameter( String parameter){
        ArrayList<String> result = new ArrayList<>();
        if (parameter.contains(";")){
            for (String par : parameter.split(";")){
                if (process_type_signature(par).equals("")){
                    for (char s : par.toCharArray()){
                        result.add(process_type_signature(String.valueOf(s)));
                        if (String.valueOf(s).equals("L")){
                            break;
                        }
                    }
                    if (par.contains("L")){
                        result.add(process_type_signature("L"+ par.split("L",2)[1]));
                    }
                } else {
                    result.add(process_type_signature(par));
                }
            }
        }
        return result;
    }

    public Triple<String, ArrayList<String>, String> process_method(String method) {
        String methodName;
        ArrayList<String> parameters;
        String returnType;
        if (method.contains("(")) {
            methodName = method.split("\\(")[0];
            if (methodName.equals("<init>")) {
                methodName = "Constructor";
            } else if (methodName.equals("<clinit>")) {
                // pending
                return null;
            }
            parameters = process_parameter(method.split("\\(")[1].split("\\)")[0]);
            returnType = process_type_signature(method.split("\\(")[1].split("\\)")[1]);
            return new Triple<>(methodName, parameters, returnType);
        }
        return null;
    }


    public void convertCSV(String csvPath) {
        HiddenEntity entity = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(csvPath));) {
            String[] rows = null;
            while ((rows = csvReader.readNext()) != null) {
                for (String e: rows){
                    if (!e.equals("")){
                        if (e.contains("->")){
                            if (entity != null && entity.qualifiedName != null){
                                this.addEntity(entity.qualifiedName, entity);
                            }
                            entity = new HiddenEntity();
                            String entityName = "";
                            if (process_class(e.split("->", 2)[0]) == null){
                                continue;
                            }
                            String classQualifiedName = process_class(e.split("->", 2)[0]);
                            if (e.split("->", 2)[1].contains("(")){
                                if (process_method(e.split("->", 2)[1]) == null){
                                    continue;
                                }
                                String methodName = process_method(e.split("->", 2)[1]).getLeft();
                                ArrayList<String> parameter = process_method(e.split("->", 2)[1]).getMiddle();
                                String returnType = process_method(e.split("->", 2)[1]).getRight();
                                if (methodName.equals("Constructor")){
                                    String[] temp = classQualifiedName.split("\\.");
                                    methodName = temp[temp.length - 1];
                                }
                                entity.setQualifiedName(classQualifiedName+"."+methodName);
                                entity.setParameter(parameter);
                                entity.setRawType(returnType);
                            } else {
                                String fieldName = process_field(e.split("->", 2)[1]).getL();
                                String fieldType = process_field(e.split("->", 2)[1]).getR();
                                entity.setRawType(fieldType);
                                entity.setQualifiedName(classQualifiedName+"."+fieldName);
                            }
                        }else {
                            if (entity != null && entity.qualifiedName != null){
                                entity.addHiddenApi(e);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean comparePara(List<String> para1, String[] para2){
        if (para1.size() == 0 && para2.length == 0){
            return true;
        }
        else if (para1.size() == para2.length){
            boolean flag = false;
            for (String s : para2) {
                for (String value : para1) {
                    if (s.equals(value)) {
                        flag = true;
                        break;
                    } else if ((s.equals("Integer") && value.equals("int")) || (value.equals("Integer") && s.equals("int"))){
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
            }
            return flag;
        }else {
            return false;
        }
    }

    public String checkHidden(MethodEntity entity, String parType){
        if (this.result.containsKey(entity.getQualifiedName())){
            if (this.result.get(entity.getQualifiedName()).size() == 1){
                return this.result.get(entity.getQualifiedName()).get(0).getHiddenApi().toString();
            }
            else {
                for (HiddenEntity hiddenEntity: this.result.get(entity.getQualifiedName())){
                    if (entity.getRawType() != null && hiddenEntity.getRawType() != null && entity.getRawType().equals(hiddenEntity.getRawType())){
//                        if (!hiddenEntity.getParameter().isEmpty() && comparePara(hiddenEntity.getParameter(), parType.split(" "))){
                            return hiddenEntity.getHiddenApi().toString();
//                        }
                    }
                    else {
                        System.out.println(entity.getQualifiedName());
                    }
                }
            }
        }
//        for (HiddenEntity hiddenEntity: this.getResult()){
//            if (entity.getQualifiedName().equals(hiddenEntity.getQualifiedName())
//                    && entity.getRawType().equals(hiddenEntity.getRawType())){
//                if (!hiddenEntity.getParameter().isEmpty() && comparePara(hiddenEntity.getParameter(), parType.split(" "))){
//                    return hiddenEntity.getHiddenApi().toString();
//                }
//            }
//        }
        return null;
    }

    public String checkHidden(VariableEntity entity){
        if (this.result.containsKey(entity.getQualifiedName())){
            for (HiddenEntity hiddenEntity: this.result.get(entity.getQualifiedName())){
                if (entity.getRawType().equals(hiddenEntity.getRawType())){
                    return hiddenEntity.getHiddenApi().toString();
                }
            }
        }
//        for (HiddenEntity hiddenEntity: this.getResult()){
//            if (entity.getQualifiedName().equals(hiddenEntity.getQualifiedName())
//                    && entity.getRawType().equals(hiddenEntity.getRawType())){
//                return hiddenEntity.getHiddenApi().toString();
//            }
//        }
        return null;
    }




}
