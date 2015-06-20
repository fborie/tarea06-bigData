package cl.bigData.controllers;

import cl.bigData.Entities.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fjborie on 17-06-15.
 */

@RequestMapping("/")
@RestController
public class TweetController {

    @RequestMapping(value="tweets", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByWord(@RequestParam("q") String word){
        System.out.println("getTweetsByWord: " + word);
        return null;
    }

    @RequestMapping(value="hashtags", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByHashtag(@RequestParam("q") String hashTag){
        System.out.println("getTweetsByHashtag: "+hashTag);
        return null;
    }

    @RequestMapping(value="user", params = "q", method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByUser(@RequestParam("q") String user){
        System.out.println("getTweetsByUser: "+user);
        return null;
    }

    @RequestMapping(value="near", params = {"lat","lon","radius"}, method = RequestMethod.GET)
    public Iterable<Tweet> getTweetsByLocation(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("radius") double radius){
        System.out.println("getTweetsByLocation: "+String.valueOf(lat)+"-"+String.valueOf(lon)+"-"+String.valueOf(radius));
        return null;
    }

}
