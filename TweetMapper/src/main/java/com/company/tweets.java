package com.company;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ananyapoddar on 26/02/15.
 */
public class tweets {

    public static void main(String[] args) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        final Count tweetCount = new Count();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {}

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if(status.getGeoLocation() != null) {
                    try
                    {
                        /*
                        tweetID,
                        +status.getId() + ", "+status.getUser().getScreenName()+", "+
                                status.getPlace().getCountry()+ ","+status.getText()+","+status.getGeoLocation().getLatitude()+
                                ","+status.getGeoLocation().getLongitude()+","+status.getCreatedAt()+","+ status.getText()+*/

                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                        System.out.print("latitude:" + status.getGeoLocation().getLatitude());
                        System.out.print("longitude:" + status.getGeoLocation().getLongitude());

                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetmap_db", "root", "");
                        Statement stmt = conn.createStatement();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String query = "INSERT INTO tweetStore (tweetID, user_screenname, location, latitude, longitude,created_date, TweetText)" +
                                "VALUES ("+status.getId()+",'"+status.getUser().getScreenName()+"', '"+
                                status.getPlace().getCountry()+ "',"+status.getGeoLocation().getLatitude()+
                                ","+status.getGeoLocation().getLongitude()+",'"+format.format(status.getCreatedAt())+"','"+ StringHelper.convertToUTF8(status.getText())+ "');";
                        stmt.execute(query);
                        tweetCount.incr();
                        System.out.println("COUNT : "+tweetCount.counter);
                    }
                    catch(Exception e)
                    {
                        System.out.println("ERROR");
                    }

                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }

}

final class Count
{
    int counter;

    public void incr()
    {
        counter++;
    }
}