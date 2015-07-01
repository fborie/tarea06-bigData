package cl.bigData.repositories;

import cl.bigData.Entities.Tweet;

public interface TweetRepository{
    public Iterable<Tweet> findByUser(String user);
    public Iterable<Tweet> findByWordInText(String word);
    public Iterable<Tweet> findByHashTag(String hashTag);
    public Iterable<Tweet> findByLink(String link);
    public Iterable<Tweet> findByLocation(double lat, double lon, int radiusInKm);
}
