package bot;

import config.FollowReply;
import twitter4j.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TwitterStreamListener implements StatusListener
{

    private long lastStatusId;
    private TwitterBot bot;
    private Window window;

    public TwitterStreamListener(TwitterBot bot)
    {
        lastStatusId = -1;
        this.bot = bot;
        window = new Window();
    }

    @Override
    public void onStatus(Status status)
    {
        long currentId = status.getId();
        String currentText = status.getText();
        if(status.getUser().getId() == bot.follow &&
                currentId != bot.config.lastStatusReplied) {
            if (status.isRetweet()) {
                bot.logger.Log(String.format("Not responding to retweet: %s", currentText));
            }
            else {
                bot.config.lastStatusReplied = currentId;

                FollowReply reply = bot.config.followReplies.get(0);
                String statusStr = String.format("@%s ", reply.account) + reply.GetHashtags();
                StatusUpdate newStatus = new StatusUpdate(statusStr);
                newStatus.setInReplyToStatusId(currentId);

                if(window.CheckWindow())
                {
                    try {
                        //Stub();
                        Status updateStatus = bot.twitter.updateStatus(newStatus);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                        bot.logger.Log(e.toString());
                    }
                    bot.logger.Log(String.format("Responding to: %s", currentText));
                    bot.logger.Log(String.format("Responded with: %s", statusStr));
                    bot.config.WriteConfig(); //Update last replied in config
                }
                else
                {
                    bot.logger.Log("Exceeded window");
                }

            }
        }
    }

    private void Stub() throws TwitterException {}

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
        bot.logger.Log(e.toString());
    }

}

class Window
{
    private final int WINDOW_LENGTH_MIN = 15;
    private final int MAX_REQ_PER_WINDOW = 15;
    private int count;
    private Date intervalStart;
    public Window(){
        intervalStart = new Date();
        count = 0;
    }
    public Boolean CheckWindow()
    {
        Date currentTime = new Date();
        long elapsedMs = currentTime.getTime() - intervalStart.getTime();
        long elapsedMin = TimeUnit.MILLISECONDS.toMinutes(elapsedMs);
        if(elapsedMin > WINDOW_LENGTH_MIN) //next window starts
        {
            //Reset count and time
            intervalStart = currentTime;
            count = 0;
        }
        return count < MAX_REQ_PER_WINDOW && ++count < MAX_REQ_PER_WINDOW;
    }
}