package edu.columbia.restservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ananyapoddar on 28/02/15.
 */

@Path("/twittermap")
public class TwitterService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TwitterData> getTweets(@QueryParam("limit") int limit, @QueryParam("hashtag") String hashtag){
        //@QueryParam("limit") int limit
        //int limit=0;
        List<TwitterData> dataList = new ArrayList<TwitterData>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {}
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetmap_db", "root", "");
            Statement stmt = conn.createStatement();
            String query;
            if(hashtag == null)
            {
                hashtag = "";
            }
            if(limit == 0){
                query = "SELECT * from tweetStore WHERE LOWER(TweetText) like '%" + hashtag + "%' ORDER BY created_date desc;";
            }else{
                query = "SELECT * from tweetStore WHERE LOWER(TweetText) like '%" + hashtag + "%' ORDER BY created_date desc LIMIT " + limit + ";";
            }
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                TwitterData inst = new TwitterData();
                inst.tweetID = rs.getLong("tweetID");
                inst.TweetText = rs.getString("TweetText");
                inst.created_Date = rs.getDate("created_date");
                inst.latitude = rs.getDouble("latitude");
                inst.longitude = rs.getDouble("longitude");
                inst.location = rs.getString("location");
                inst.screenName = rs.getString("user_screenname");
                dataList.add(inst);
            }
        }
        catch(Exception e)
        {

        }
        return dataList;
    }

    @GET
    @Path("/getalltweets")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTweets(){

        return "getalltweets";
    }


}


//query = "SELECT * from tweetStore ORDER BY created_date desc LIMIT 1;";