package cl.bigData.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by fjborie on 17-06-15.
 */
public class Tweet {

    @JsonProperty
    private String text;

    @JsonProperty
    private ArrayList<String> hashTags;

    @JsonProperty
    private ArrayList<String> links;

    @JsonProperty
    private String username;

    @JsonProperty
    private String createdAt;

    @JsonProperty
    private Double[] location;

    @JsonCreator
    public Tweet(@JsonProperty("text") String text, @JsonProperty("hashtags") ArrayList<String> hashTags, @JsonProperty("links") ArrayList<String> links,
                 @JsonProperty("user") String username, @JsonProperty("created_at") String createdAt, @JsonProperty("location") Double[] location){
        this.text = text;
        this.hashTags = hashTags;
        this.links = links;
        this.username = username;
        this.createdAt = createdAt;
        this.location = location;
    }
}
