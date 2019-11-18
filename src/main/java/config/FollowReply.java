package config;

import com.google.gson.annotations.Expose;

import java.util.Random;

public class FollowReply
{
    @Expose
    public String account;
    @Expose
    public String[] hashtags;

    private String GetHashtag()
    {
        Random random = new Random();
        return hashtags[random.nextInt(hashtags.length)];
    }
    public String GetHashtags()
    {
        return GetHashtags(5);
    }
    public String GetHashtags(int n)
    {
        if(n > hashtags.length)
        {
            n = hashtags.length;
        }

        StringBuilder builder = new StringBuilder();
        int i = 0;
        while(i < n)
        {
            String hashtag = GetHashtag();
            if(!builder.toString().contains(hashtag))
            {
                builder.append(hashtag + " ");
                i++;
            }
        }
        return builder.toString().trim();
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\tAccount: " + account + "\n");
        builder.append("\tHashtags: [");
        for(String tag : hashtags)
        {
            builder.append(tag + ", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("]");
        return builder.toString();
    }
}
