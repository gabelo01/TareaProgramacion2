package cr.ac.una.sistemafichas.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class JsonUtil {

    
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).setPrettyPrinting().create();

    private static String dataPath ="data/";
    public static void setDataPath(String path){
        dataPath = path;
    }
    public static String getDataPath(){
        return dataPath;
    }
    private static String resolvePath(String filePath){
        if(filePath==null){
            return null;
        }
        if(Paths.get(filePath).isAbsolute()){
            return filePath;
        }
        return filePath.replace("data/",dataPath);
    }
    public static <T> T read(String filePath, Class<T> tipo) {
        
        if (filePath == null){
            return null;
        }
        
        try (Reader reader = new FileReader(resolvePath(filePath))){
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("Error leyendo JSON: " + filePath);
        return null;
        }
    }

    public static void write(String filePath, Object objeto) {
        try {
            String resolved = resolvePath(filePath);
            Files.createDirectories(Paths.get(resolved).getParent());
            try (Writer writer = new FileWriter(resolved)) {gson.toJson(objeto, writer);
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo JSON: " + filePath);
            e.printStackTrace();
        }
    }
    
    public static <T> T read(String filePath, Type tipo) {
        if (filePath == null){
            return null;
        }
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("Error leyendo JSON: " + filePath);
            return null;
        }
    }
}