package utils;

import app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.reflect.Modifier.TRANSIENT;

public class Utility<T> {

    public static String GetTimeStr()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    public static Boolean IsJar()
    {
        return Application.class.getResource("Application.class").getPath().contains("jar");
    }

    public static String ReadFile(String filePath) throws IOException
    {
        String ret = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return ret;
    }

    public static void WriteFile(String filePath, String content) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    public static void WriteResource(String filePath, String resourceName) throws Exception
    {
        WriteFile(filePath, ReadResource(resourceName));
    }
    public static String ReadResource(String resourceName) throws Exception
    {
        InputStream stream = Application.class.getClassLoader().getResourceAsStream(String.format("data/%s", resourceName));
        //byte[] buf = new byte[stream.available()];
        //stream.read(buf);
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void EnsureDirExists(String name)
    {
        try
        {
            File directory = new File(name);
            if (! directory.exists()) {
                directory.mkdirs();
            }
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


    private Class<T> type;
    public Utility(Class<T> type) { this.type = type; }
    public <T> T ReadObjectFromFile(String filePath) throws Exception
    {
        return (T) new Gson().fromJson(ReadFile(filePath), type);
    }
    public <T> void WriteObjectToFile(String filePath, T obj) throws Exception
    {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .excludeFieldsWithModifiers(TRANSIENT) // STATIC|TRANSIENT in the default configuration
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(obj);
        WriteFile(filePath, json);
    }
}
