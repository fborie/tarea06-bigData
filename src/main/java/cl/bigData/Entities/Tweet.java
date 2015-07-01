package cl.bigData.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Tweet {

    @JsonProperty
    private String status;

    @JsonProperty
    private ArrayList<String> hashTags;

    @JsonProperty
    private ArrayList<String> links;

    @JsonProperty
    private String username;

    @JsonProperty
    private long createdAt;

    @JsonProperty
    private Location location;

    @JsonCreator
    public Tweet(@JsonProperty("status") String status, @JsonProperty("hashtags") ArrayList<String> hashTags, @JsonProperty("links") ArrayList<String> links,
                 @JsonProperty("user") String username, @JsonProperty("createdAt") long createdAt, @JsonProperty("location") Location location){
        this.status = status;
        this.hashTags = hashTags;
        this.links = links;
        this.username = username;
        this.createdAt = createdAt;
        this.location = location;
    }
}
