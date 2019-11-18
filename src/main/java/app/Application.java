package app;

import bot.TwitterBot;
import config.BotConfig;
import exceptions.BotInitializationException;
import utils.Utility;
import java.io.*;

public class Application {

    public static void main(String[] args) {

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        TwitterBot bot = new TwitterBot();

        System.out.println(String.format("Running from Jar: %s", Utility.IsJar() ? "true" : "false"));
        System.out.println("--Bot Starting--");
        try {
            bot.Start();
        }
        catch(BotInitializationException ex)
        {
            if(Utility.IsJar())
            {
                try {
                    Utility.WriteResource("data/config.json", "config.json");
                    bot = new TwitterBot();
                    bot.Start(); //Can start bot now
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Created Default Config");
            }
            else {
                System.out.println("Created Empty Config");
                BotConfig.CreateEmptyConfig();
                System.out.println("No configuration file was found, please modify the one created on data/");
                return;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return;
        }
        System.out.println("--Bot Started--");

        Boolean exit = false;
        while(!exit)
        {
            String input = null;
            try {
                input = console.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            exit = input != null && input.equalsIgnoreCase("exit");
        }
        bot.Stop();
        System.out.println("--Bot Exited--");
    }
}



