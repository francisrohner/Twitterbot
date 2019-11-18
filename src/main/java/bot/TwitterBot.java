package bot;

import exceptions.BotInitializationException;
import utils.Logger;
import config.BotConfig;
import utils.Utility;
import twitter4j.*;
import twitter4j.conf.Configuration;

public class TwitterBot
{

    public BotConfig config;
    public Twitter twitter;
    public Logger logger;
    public long follow;

    private Boolean initializationError;
    private TwitterStream twitterStream;
    private TwitterStreamListener twitterStreamHandler;

    public TwitterBot()
    {
        try
        {
            initializationError = false;
            Utility.EnsureDirExists("data");
            config = BotConfig.ReadConfig();
            Configuration twitterConfig = config.twitterAuth.BuildConfig();
            twitter = new TwitterFactory(twitterConfig).getInstance();
            twitterStream = new TwitterStreamFactory(twitterConfig).getInstance();
            twitterStreamHandler = new TwitterStreamListener(this);
            logger = new Logger(config.logPath);
        }
        catch(Exception ex)
        {
            initializationError = true;
            new Logger("log.txt").Log(ex.toString());
        }
    }

    public void Start() throws Exception
    {
        if(initializationError)
        {
            throw new BotInitializationException();
        }

        twitterStream.addListener(twitterStreamHandler);
        String account = config.followReplies.get(0).account;
        follow = twitter.showUser(account).getId();
        System.out.format("Received id %d for %s%n", follow, account);
        FilterQuery query = new FilterQuery(follow);
        twitterStream.filter(query);

    }


    public void Stop()
    {
        twitterStream.clearListeners();
        twitterStream.cleanUp();
        twitterStream.shutdown();
        twitterStream = null;
    }
}