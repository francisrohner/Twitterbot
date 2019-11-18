package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Logger {
    private String logPath;
    public Logger(String filePath)
    {
        logPath = filePath;
    }
    public void Log(String text)
    {
        try {
            System.out.println(text);
            BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));
            writer.append(String.format("[%s]: ", Utility.GetTimeStr()));
            writer.append(text);
            writer.append("\n");
            writer.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
