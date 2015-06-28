package cl.bigData.repositories;

import cl.bigData.Entities.Tweet;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by fjborie on 24-06-15.
 */

@Repository
public class ElasticTweetRepository implements TweetRepository {

    @Override
    public Iterable<Tweet> findByUser(String user) {
        System.out.println("Se encontro al USUARIO!");
        return null;
    }

    @Override
    public Iterable<Tweet> findByWordInText(String word) {
        return null;
    }

    @Override
    public Iterable<Tweet> findByHashTag(String hashTag) {
        return null;
    }

    @Override
    public Iterable<Tweet> findByLink(String link) {
        return null;
    }

    @Override
    public Iterable<Tweet> findByLocation(float lat, float lon, float radius) {
        return null;
    }
}
