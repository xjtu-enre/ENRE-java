package TempOutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class CreateFileUtil {
    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile(String filePath, String fileName, String jsonString) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            JsonFormatTool tool = new JsonFormatTool();

//            for(BaseEntity entity: SingleCollect.getSingleCollectInstance().getEntities()){
//                JSONObject jo = JSONObject.fromObject(entity);
//                String entityData = JSON.toJSONString(jo,SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
//                // 将格式化后的字符串写入文件
//                //write.write(tool.formatJson(jo.toString())+", ");
//                write.write(entityData);
//            }

            String result = jsonString;
//            write.write(tool.formatJson(result));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(result);
            String res = gson.toJson(je);
            write.write(res);

//            JSONObject object = JSONObject.fromObject(result);
//            System.out.println("---------outputFile------------");
//            try{
//            write.write(JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
//                    SerializerFeature.WriteDateUseDateFormat));
//                write.write(tool.formatJson(result));
//            }catch (OutOfMemoryError e){
//                write.write(jsonString);
//            }

            write.flush();
            write.close();

        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

}
