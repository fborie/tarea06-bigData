package cl.bigData.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location
{
    @JsonProperty
    private double lat;
    @JsonProperty
    private double lon;

    @JsonCreator
    public Location(@JsonProperty("lat") double lat, @JsonProperty("lon") double lon)
    {
        this.lat = lat;
        this.lon = lon;
    }

}
