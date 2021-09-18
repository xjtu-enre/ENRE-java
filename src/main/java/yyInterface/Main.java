package yyInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        String projectPath = "C:\\Users\\pc\\Desktop\\test_project\\sample-spring-microservices-advanced";
        String projectName = "sample-spring-microservices-advanced";
        Impl.identify(projectPath, projectName);
        writeToCSV(projectPath, projectName, "class");
        writeToCSV(projectPath, projectName, "structdep");
    }

    public static void writeToCSV(String projectpath, String projectName, String type) {
        String fileName = projectpath + File.separator + projectName + "-" + type + ".csv";
        File file = new File(fileName);
        // 创建文件，如果存在删除重新创建；创建好之后将抽取的数据读入
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            Writer out = new FileWriter(file);
            if (type.equals("class")) {
                writeClassFile(out);
            } else if (type.equals("structdep")) {
                writeStructdepFile(out);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeClassFile(Writer out) throws IOException {
        for (Map.Entry<String, Integer> entry : Impl.getClassInfo().entrySet()) {
            out.write(entry.getValue() - 1 + "," + entry.getKey() + "\n");
        }
    }

    private static void writeStructdepFile(Writer out) throws IOException {
        for (Map.Entry<String, Map<String, Integer>> entry1 : Impl.getStructure().entrySet()) {
            if (!Impl.getClassInfo().containsKey(entry1.getKey())) {
                continue;
            }
            for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
                if (Impl.getClassInfo().containsKey(entry2.getKey())) {
                    out.write(entry1.getKey()+","+entry2.getKey()+","+entry2.getValue()+"\n");
                }
            }
        }
    }
}
