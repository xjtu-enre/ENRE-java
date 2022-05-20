package TempOutput;

import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;
import entity.MethodEntity;
import entity.TypeEntity;
import entity.VariableEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;

import util.Tuple;
import util.Triple;

import java.io.*;
import java.util.*;

public class ProcessHidden {

    private ProcessHidden(){}

    private static ProcessHidden processHiddeninstance = new ProcessHidden();

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

    public static ProcessHidden getProcessHiddeninstance() {
        return processHiddeninstance;
    }

    static class HiddenEntity {

        String qualifiedName = null;
        ArrayList<String> hiddenApi = new ArrayList<>();
        ArrayList<String> parameter = new ArrayList<>();
        String rawType = null;
        String originalSignature = null;
        boolean isMatch = false;

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

        public void setOriginalSignature(String originalSignature) {
            this.originalSignature = originalSignature;
        }

        public String getOriginalSignature() {
            return originalSignature;
        }

        public boolean isMatch() {
            return isMatch;
        }

        public void setMatch(boolean match) {
            isMatch = match;
        }

        @Override
        public String toString() {
            return "hiddenEntity{" +
                    "qualifiedName='" + qualifiedName + '\'' +
                    ", hiddenApi='" + hiddenApi + '\'' +
                    ", parameter=" + parameter +
                    ", rawType='" + rawType + '\'' +
                    ", originalSignature='" + originalSignature + '\'' +
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
                methodName = "Class_Constructor";
//                return null;
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
                            entity.setOriginalSignature(e);
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
                                String entityQualifiedName = classQualifiedName+"."+methodName;
                                if (methodName.equals("Class_Constructor")){
                                    entityQualifiedName = classQualifiedName;
                                }
                                entity.setQualifiedName(entityQualifiedName);
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
                    }  else if ((s.equals("Boolean") && value.equals("boolean")) || (value.equals("Boolean") && s.equals("boolean"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Long") && value.equals("long")) || (value.equals("Long") && s.equals("long"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Byte") && value.equals("byte")) || (value.equals("Byte") && s.equals("byte"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Character") && value.equals("char")) || (value.equals("Character") && s.equals("char"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("double") && value.equals("Double")) || (value.equals("double") && s.equals("Double"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("float") && value.equals("Float")) || (value.equals("float") && s.equals("Float"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("short") && value.equals("Short")) || (value.equals("short") && s.equals("Short"))) {
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

    public String checkHidden(TypeEntity entity){
        if (this.result.containsKey(entity.getQualifiedName())){
            if (this.result.get(entity.getQualifiedName()).size() == 1){
                this.result.get(entity.getQualifiedName()).get(0).setMatch(true);
                return refactorHidden(this.result.get(entity.getQualifiedName()).get(0).getHiddenApi());
            }
            else {
                for (HiddenEntity hiddenEntity: this.result.get(entity.getQualifiedName())){
                    if (hiddenEntity.getOriginalSignature().contains("<clinit>")){
                        hiddenEntity.setMatch(true);
                        return refactorHidden(hiddenEntity.getHiddenApi());
                    }
                }
            }
        }
        return null;
    }

    public String checkHidden(MethodEntity entity, String parType){
        if (this.result.containsKey(entity.getQualifiedName())){
            if (this.result.get(entity.getQualifiedName()).size() == 1){
                this.result.get(entity.getQualifiedName()).get(0).setMatch(true);
                return refactorHidden(this.result.get(entity.getQualifiedName()).get(0).getHiddenApi());
            }
            else {
                for (HiddenEntity hiddenEntity: this.result.get(entity.getQualifiedName())){
                    if (entity.getRawType() != null && hiddenEntity.getRawType() != null){
                        if (entity.isConstructor()){
                            if (!hiddenEntity.getParameter().isEmpty() && comparePara(hiddenEntity.getParameter(), parType.split(" "))){
                                hiddenEntity.setMatch(true);
                                return refactorHidden(hiddenEntity.getHiddenApi());
                            }
                        } else if (entity.getRawType().equals(hiddenEntity.getRawType())){
                            if (!hiddenEntity.getParameter().isEmpty() && comparePara(hiddenEntity.getParameter(), parType.split(" "))){
                                hiddenEntity.setMatch(true);
                                return refactorHidden(hiddenEntity.getHiddenApi());
                            }
                        }

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
                    hiddenEntity.setMatch(true);
                    return refactorHidden(hiddenEntity.getHiddenApi());
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

    public String refactorHidden(ArrayList<String> hiddenApi){
        String hidden = "";
        for (String temp: hiddenApi){
            hidden = hidden.concat(temp+" ");
        }
        hidden = hidden.substring(0, hidden.length()-1);
        return hidden;
    }

    public String outputResult(){
        JSONObject obj=new JSONObject();
        List<JSONObject> subCategories = new ArrayList<>();
        for (ArrayList<HiddenEntity> hiddenEntities: this.getResult().values()){
            for (HiddenEntity hidden : hiddenEntities){
                if (!hidden.isMatch){
                    JSONObject current = new JSONObject();
                    current.put("signature", hidden.getOriginalSignature());
                    current.put("qualifiedName", hidden.getQualifiedName());
                    current.put("rawType", hidden.getRawType());
                    current.put("parameter", hidden.getParameter());
                    current.put("hiddenApi", hidden.getHiddenApi());
                    subCategories.add(current);
                }
            }
        }
        obj.accumulate("NotMatch", subCategories);
        return obj.toString();
    }

    public void checkMatch(String qualifiedName, String rawType, String parameterType){
        //check match
        if (ProcessHidden.getProcessHiddeninstance().getResult().containsKey(qualifiedName)){
            for (HiddenEntity hiddenEntity: ProcessHidden.getProcessHiddeninstance().getResult().get(qualifiedName)){
                if (rawType!=null && rawType.equals(hiddenEntity.getRawType())){
                    if (parameterType!=null){
                        String hiddenPars = "";
                        for (String par: hiddenEntity.getParameter()){
                            hiddenPars = hiddenPars.concat(JsonString.processRawType(par)+" ");
                        }
                        if (!hiddenPars.equals("")){
                            hiddenPars = hiddenPars.substring(0, hiddenPars.length()-1);
                        }
                        if (parameterType.equals(hiddenPars)){
                            hiddenEntity.setMatch(true);
                        }
                    } else {
                        hiddenEntity.setMatch(true);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ProcessHidden processHidden = ProcessHidden.getProcessHiddeninstance();
        processHidden.convertCSV("E:\\Android\\hiddenapi-flags.csv");
        FileReader in = new FileReader("base-enre-out\\base-out-with-hidden.json");
        JsonReader reader = new JsonReader(in);
        reader.beginObject();
        String rootName = null;
        while (reader.hasNext()){
            rootName = reader.nextName();
            if ("variables".equals(rootName)){
                System.out.println("Begin reading files...");
                reader.beginArray();
                while (reader.hasNext()){
                    reader.beginObject();
                    String k = null;
                    String qualifiedName = null;
                    String rawType = null;
                    String parameterType = null;
                    boolean isMatch = false;
                    while (reader.hasNext()){
                        k = reader.nextName();
                        switch (k) {
                            case "hidden":
                                isMatch = true;
                                reader.skipValue();
                                break;
                            case "qualifiedName":
                                qualifiedName = reader.nextString();
                                break;
                            case "rawType":
                                rawType = reader.nextString();
                                break;
                            case "parameter":
                                reader.beginObject();
                                while (reader.hasNext()){
                                    if (reader.nextName().equals("types")){
                                        parameterType = reader.nextString();
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                    if (isMatch){
                        processHidden.checkMatch(qualifiedName, rawType, parameterType);
                    }
                }
            } else {
                reader.beginObject();
                while (reader.hasNext()) {
                    System.out.println(reader.nextName() + ":" + reader.nextString());
                }
                reader.endObject();
            }
        }
        //output not match
        String fileName = "base-enre-out\\hidden-not-match.csv";
        Writer out = null;
        FileOutputStream fileOs = null;
        fileOs = new FileOutputStream(fileName);
        out = new OutputStreamWriter(fileOs, "GBK");
        //字符数组是头行
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("OriginalSignature", "processName", "processRawType", "processParameter").withQuote(null));
        List<Object> objects = new ArrayList<>();
        for (ArrayList<HiddenEntity> hiddenEntities : processHidden.getResult().values()) {
            for (HiddenEntity entity : hiddenEntities){
                if (!entity.isMatch()){
                    objects.add(entity.getOriginalSignature());
                    objects.add(entity.getQualifiedName());
                    objects.add(entity.getRawType());
                    objects.add(entity.getParameter());
                    //打印一行
                    printer.printRecord(objects);
                    //打印完后注意将数组clear掉
                    objects.clear();
                }
            }

        }
        out.flush();
    }
}
