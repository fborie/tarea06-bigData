package cl.bigData.controllers;

import cl.bigData.Entities.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cl.bigData.repositories.TweetRepository;

import java.util.ArrayList;

@RequestMapping("/")
@RestController
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @RequestMapping(value="tweets", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByWord(@RequestParam("q") String word){
        return tweetRepository.findByWordInText(word);
    }

    @RequestMapping(value="hashtags", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByHashtag(@RequestParam("q") String hashTag){
        return tweetRepository.findByHashTag(hashTag);
    }

    @RequestMapping(value="user", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByUser(@RequestParam("q") String user){
         return tweetRepository.findByUser(user);
    }

    @RequestMapping(value = "links", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByLink(@RequestParam("q") String link){
        return tweetRepository.findByLink(link);
    }

    @RequestMapping(value="near", params = {"lat","lon","radius"}, method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByLocation(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("radius") double radius){
        System.out.println("getTweetsByLocation: "+String.valueOf(lat)+"-"+String.valueOf(lon)+"-"+String.valueOf(radius));
        return null;
    }

}
