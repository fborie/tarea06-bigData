package cl.bigData.repositories;

import cl.bigData.Entities.Location;
import cl.bigData.Entities.Tweet;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.Query;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Repository
public class ElasticTweetRepository implements TweetRepository {

    private static final String INDEX = "twitter"; //me genera problemas con los fields nesteds en "tweet"
    private static final String TYPE = "tweet";


    private Client m_client;

    @PostConstruct
    public void setup(){
        // added: create an elasticSearch Transport Client 
        // to comunicate with the cluster to store the json objects

        TransportAddress address = new InetSocketTransportAddress("localhost", 9300);
        m_client = new TransportClient()
                                .addTransportAddress(address);

        //m_client = NodeBuilder.nodeBuilder().clusterName("twitter-cluster").node().client();
    }

    @PreDestroy
    public void tearDown(){
        m_client.close();
    }

    @Override
    public Iterable<Tweet> findByUser(String user) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        QueryBuilder qb = termQuery("user", user);

        //NO SE PORQUE NO FUNCIONA LA BUSQUEDA CON SCROLL
       /* SearchResponse scrollResp = client.prepareSearch(INDEX).setTypes(TYPE).addField("user").setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000)).setQuery(qb).setSize(50).execute().actionGet();
        System.out.println(user);
        System.out.println(scrollResp.getHits().getHits().length);
        for (SearchHit hit : scrollResp.getHits().getHits()) {
            tweets.add(extractTweetFromHit(hit));
        }*/
        SearchResponse response = getSearchResponse(qb);
        System.out.println(response.getHits().getHits().length);

        return extractTweetsFromHits(response.getHits().getHits());
    }

    @Override
    public Iterable<Tweet> findByWordInText(String word) {
        QueryBuilder qb = matchQuery("status", word);
        SearchResponse response = getSearchResponse(qb);
        System.out.println(response.getHits().getHits().length);

        return extractTweetsFromHits(response.getHits().getHits());
    }

    @Override
    public Iterable<Tweet> findByHashTag(String hashTag) {
        QueryBuilder qb = matchQuery("hashtags",hashTag);
        SearchResponse response = getSearchResponse(qb);
        return extractTweetsFromHits(response.getHits().getHits());
    }

    @Override
    public Iterable<Tweet> findByLink(String link) {
        QueryBuilder qb = matchQuery("links",link);
        SearchResponse response = getSearchResponse(qb);
        return extractTweetsFromHits(response.getHits().getHits());
    }

    @Override
    public Iterable<Tweet> findByLocation(float lat, float lon, float radius) {
        return null;
    }

    private SearchResponse getSearchResponse(QueryBuilder qb){
        return m_client.prepareSearch(INDEX).setTypes(TYPE)
                .addFields("user", "status", "links", "location.lat", "location.lon", "created_at", "hashtags")
                .setQuery(qb).setSize(50).execute().actionGet();
    }

    private ArrayList<Tweet> extractTweetsFromHits(SearchHit[] hits){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for(SearchHit hit: hits){
            tweets.add(extractTweetFromHit(hit));
        }
        return tweets;
    }

    private Tweet extractTweetFromHit(SearchHit hit){
        String user = hit.field("user").getValue();
        String status = hit.field("status").getValue();
        long createdAt = hit.field("createdAt").getValue();
        ArrayList<String> links = extractListFromField(hit.field("links").getValues());
        ArrayList<String> hashTags = extractListFromField(hit.field("hashtags").getValues());
        Location location = extractLocation(hit);
        return new Tweet(status,hashTags,links,user,createdAt,location);
    }

    private ArrayList<String> extractListFromField(List<Object> objects){
        ArrayList<String> arr = new ArrayList<String>();
        for (Object obj : objects){
            arr.add((String) obj);
        }
        return arr;
    }

    private Location extractLocation(SearchHit hit){
        double lat = hit.field("location.lat").getValue();
        double lon = hit.field("location.lon").getValue();
        return new Location(lat, lon);
    }
}
