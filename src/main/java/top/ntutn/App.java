package top.ntutn;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;

/**
 * Hello world!
 *
 */
public class App {
    /**
     * 递归便利目录下所有文件
     * @param path
     * @return
     */
    private static List<String> getAllFiles(File path){
        List<String> res=new LinkedList<>();
        File[] files=path.listFiles();
        for (File file : files) {
            if(file.isDirectory()){
                res.addAll(getAllFiles(file));
            }else{
                res.add(file.getAbsolutePath());
            }
        }
        return res;
    }

    /**
     * 重新生成数据文件
     * @param path
     * @param dataFile
     */
    private static void initData(String path,File dataFile) {
        List<String> picList=getAllFiles(new File(path));
        Collections.shuffle(picList);//洗牌
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("top", picList.size()-1);
        for (int i = 0; i < picList.size(); i++) {
            jsonObject.put(Integer.toString(i), picList.get(i));
        }
        try {
            FileUtils.writeStringToFile(dataFile, jsonObject.toJSONString(), "UTF-8");
        } catch (IOException e) {
            System.err.println("写数据文件失败："+e.toString());
        }
    }

    /**
     * 设置壁纸
     * @param url
     */
    private static void setWallpicture(String url,String setCommand) {
        try {
            Runtime.getRuntime().exec(setCommand.replace("$pic", url));
        } catch (IOException e) {
            System.err.println("设置壁纸失败："+e.toString());
        }
    }

    public static void main(String[] args) {
        File dataFile=new File("data.json");
        File configFile=new File("config.json");
        String config="";
        String path="";
        String setCommand="";
        try {
            config = FileUtils.readFileToString(configFile, "UTF-8");
        } catch (IOException e) {
            System.err.println("读取配置文件失败："+e.toString());
            return;
        }

        JSONObject jsonObjectConfig=JSON.parseObject(config);
        path=jsonObjectConfig.getString("picpath");
        setCommand=jsonObjectConfig.getString("setcommand");

        if(!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                System.err.println("创建数据文件失败："+e.toString());
                return;
            }
            initData(path,dataFile);
        }

        String data="";
        try {
            data = FileUtils.readFileToString(dataFile, "UTF-8");
        } catch (IOException e) {
            System.err.println("读取数据文件失败："+e.toString());
            return;
        }

        JSONObject jsonObject=JSON.parseObject(data);
        String url=jsonObject.getString(jsonObject.getInteger("top").toString());
        setWallpicture(url,setCommand);

        jsonObject.put("top", jsonObject.getInteger("top")-1);
        
        if(jsonObject.getInteger("top")<=0){
            initData(path,dataFile);
            return;
        }

        try {
            FileUtils.writeStringToFile(dataFile, jsonObject.toJSONString(), "UTF-8");
        } catch (IOException e) {
            System.err.println("写数据文件失败："+e.toString());
        }
    }
}
