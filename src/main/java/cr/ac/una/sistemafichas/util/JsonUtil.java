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

//revisar
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class JsonUtil {

    //private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).setPrettyPrinting().create();

    public static <T> T read(String filePath, Class<T> tipo) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
           e.printStackTrace();
           return null;
        }
        
    }

    public static void write(String filePath, Object objeto) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (Writer writer = new FileWriter(filePath)) {gson.toJson(objeto, writer);
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo JSON: " + filePath);
            e.printStackTrace();
        }
    }
    
    public static <T> T read(String filePath, Type tipo) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("Error leyendo JSON: " + filePath);
            return null;
        }
    }
}