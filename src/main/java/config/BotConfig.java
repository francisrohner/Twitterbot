package config;

import utils.Utility;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class BotConfig
{
    @Expose
    public long lastStatusReplied;
    @Expose
    public String logPath;
    @Expose
    public TwitterAuth twitterAuth;
    @Expose
    public List<FollowReply> followReplies;

    private Utility<BotConfig> utility;

    public BotConfig()
    {
        utility = new Utility<BotConfig>(BotConfig.class);
    }

    public static void main(String[] args)
    {
        Utility.EnsureDirExists("debug");
        try {
            System.out.println(ReadConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean WriteConfig()
    {
         try
         {
             utility.WriteObjectToFile("data/config.json", this);
             return true;
         } catch(Exception ex)
         {
             ex.printStackTrace();
         }
         return false;
    }

    public static BotConfig ReadConfig() throws Exception
    {
        BotConfig ret =  new Utility<BotConfig>(BotConfig.class).ReadObjectFromFile("data/config.json");
        if(ret.logPath == null || ret.logPath.isEmpty())
        {
            ret.logPath = "log.txt";
        }
        return ret;
    }

    public static void CreateEmptyConfig()
    {
        try {
            BotConfig empty = new BotConfig();
            empty.lastStatusReplied = -1L;
            empty.logPath = "log.txt";
            empty.followReplies = new ArrayList<FollowReply>();
            FollowReply reply = new FollowReply();
            reply.account = "--fill--";
            reply.hashtags = new String[] {"#--example--"};
            empty.followReplies.add(reply);
            empty.twitterAuth = new TwitterAuth();
            empty.twitterAuth.consumerKey = "--fill--";
            empty.twitterAuth.consumerSecret = "--fill--";
            empty.twitterAuth.accessToken = "--fill--";
            empty.twitterAuth.accessTokenSecret = "--fill--";

            new Utility<BotConfig>(BotConfig.class).WriteObjectToFile("data/config.json", empty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("LastStatusReplied: " + lastStatusReplied + "\n");
        builder.append("LogPath: " + logPath + "\n");
        builder.append("TwitterAuth:\n");
        builder.append(twitterAuth.toString());
        builder.append("FollowReplies:\n");
        for(FollowReply reply : followReplies)
        {
            builder.append(reply.toString());
        }
        return builder.toString();
    }

}


