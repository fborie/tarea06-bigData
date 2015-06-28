package cl.bigData.repositories;

import cl.bigData.Entities.Tweet;

/**
 * Created by fjborie on 24-06-15.
 */
public interface TweetRepository{
    public Iterable<Tweet> findByUser(String user);
    public Iterable<Tweet> findByWordInText(String word);
    public Iterable<Tweet> findByHashTag(String hashTag);
    public Iterable<Tweet> findByLink(String link);
    public Iterable<Tweet> findByLocation(float lat, float lon, float radius);
}
