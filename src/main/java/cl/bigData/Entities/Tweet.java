package cl.bigData.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.common.geo.GeoPoint;

import java.util.ArrayList;

public class Tweet {

    @JsonProperty
    private String status;

    @JsonProperty
    private ArrayList<String> hashtags;

    @JsonProperty
    private ArrayList<String> links;

    @JsonProperty
    private String user;

    @JsonProperty
    private long createdAt;

    @JsonProperty
    private GeoPoint location;

    @JsonCreator
    public Tweet(@JsonProperty("status") String status, @JsonProperty("hashtags") ArrayList<String> hashTags, @JsonProperty("links") ArrayList<String> links,
                 @JsonProperty("user") String user, @JsonProperty("createdAt") long createdAt, @JsonProperty("location") GeoPoint location){
        this.status = status;
        this.hashtags = hashTags;
        this.links = links;
        this.user = user;
        this.createdAt = createdAt;
        this.location = location;
    }
}
