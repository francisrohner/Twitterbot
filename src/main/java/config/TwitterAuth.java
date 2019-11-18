package config;

import com.google.gson.annotations.Expose;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth {

    @Expose
    public String consumerKey;
    @Expose
    public String consumerSecret;
    @Expose
    public String accessToken;
    @Expose
    public String accessTokenSecret;

    public Configuration BuildConfig()
    {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        return configurationBuilder.build();
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\tConsumerKey: " + consumerKey + "\n");
        builder.append("\tConsumerSecret: " + consumerSecret + "\n");
        builder.append("\tAccessToken: " + accessToken + "\n");
        builder.append("\tAccessTokenSecret: " + accessTokenSecret + "\n");
        return builder.toString();
    }
}
