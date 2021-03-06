package cl.bigData.repositories;

import cl.bigData.Entities.Location;
import cl.bigData.Entities.Tweet;
import org.elasticsearch.action.percolate.TransportShardMultiPercolateAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
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
        //--ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        //--QueryBuilder qb = termQuery("user", user);

        //NO SE PORQUE NO FUNCIONA LA BUSQUEDA CON SCROLL
       /* SearchResponse scrollResp = client.prepareSearch(INDEX).setTypes(TYPE).addField("user").setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000)).setQuery(qb).setSize(50).execute().actionGet();
        System.out.println(user);
        System.out.println(scrollResp.getHits().getHits().length);
        for (SearchHit hit : scrollResp.getHits().getHits()) {
            tweets.add(extractTweetFromHit(hit));
        }*/

        //--SearchResponse response = getSearchResponse(qb);

        SearchRequest request =
                Requests.searchRequest("twitter")
                        .types("tweet")
                        .source("{ \"fields\" : [\"user\", \"status\", \"links\", \"location.lat\", \"location.lon\",\"createdAt\", \"hashtags\"],"
                                + "\"query\":{\"match\":{\"user\":\"" + user + "\"}}}");
        SearchResponse response = m_client.search(request).actionGet();

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
    public Iterable<Tweet> findByLocation(double lat, double lon, int radiusInKm) {

        SearchRequest request =
                Requests.searchRequest("twitter")
                        .types("tweet")
                        .source("{ \"fields\" : [\"user\", \"status\", \"links\", \"location.lat\", \"location.lon\",\"createdAt\", \"hashtags\"],"
                                + "\"query\":{\"filtered\":{\"filter\":{\"geo_distance\":{\"distance\": \"" +radiusInKm+"km\"," +
                                "\"location\":{\"properties\":{\"lat\": " + lat + ", \"lon\": " + lon + "}}}}}}");

        SearchResponse response = m_client.search(request).actionGet();

        System.out.println(response.getHits().getHits().length);

        return extractTweetsFromHits(response.getHits().getHits());

    }

    private SearchResponse getSearchResponse(QueryBuilder qb){
        return m_client.prepareSearch(INDEX).setTypes(TYPE)
                .addFields("user", "status", "links", "location.lat", "location.lon", "createdAt", "hashtags")
                .setQuery(qb).setSize(1000).execute().actionGet();
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

        ArrayList<String> links = new ArrayList<String>();
        if (hit.field("links") != null)
            links = extractListFromField(hit.field("links").getValues());

        ArrayList<String> hashTags = new ArrayList<String>();
        if (hit.field("hashtags") != null)
            hashTags = extractListFromField(hit.field("hashtags").getValues());

        GeoPoint location = new GeoPoint(0, 0);
        if (hit.field("location.lat") != null && hit.field("location.lon") != null)
            location = extractPositionsFromLocation(hit);

        return new Tweet(status,hashTags,links,user,createdAt,location);
    }

    private ArrayList<String> extractListFromField(List<Object> objects){
        ArrayList<String> arr = new ArrayList<String>();
        for (Object obj : objects){
            arr.add((String) obj);
        }
        return arr;
    }

    private GeoPoint extractPositionsFromLocation(SearchHit hit)
    {
        double latitude = hit.field("location.lat").getValue();
        double longitude = hit.field("location.lon").getValue();
        return new GeoPoint(latitude, longitude);
    }

}
