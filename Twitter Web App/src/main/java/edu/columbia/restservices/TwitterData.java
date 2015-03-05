package edu.columbia.restservices;

import java.util.Date;

/**
 * Created by ananyapoddar on 28/02/15.
 */

//Creating my TwitterData object class
public class TwitterData {

    long tweetID;
    String screenName;
    String location;
    Double latitude;
    Double longitude;
    Date created_Date;
    String TweetText;

    TwitterData()
    {
        tweetID = 1;
        screenName = "Default Value";
        location = "India";
        latitude = 123456.0;
        longitude = 45667789.0;
        created_Date = new Date();
        TweetText = "My tweets..";
    }
}
