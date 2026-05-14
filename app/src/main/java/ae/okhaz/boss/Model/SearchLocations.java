package ae.okhaz.boss.Model;;
/**
 * Created by Avinash on 18,December,2020
 */
public class SearchLocations {
    String name,vicinity;
    Double latitude,longitude;
    public SearchLocations(String name, String vicinity, Double latitude, Double longitude) {
        this.name = name;
        this.vicinity = vicinity;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }
    public String getVicinity() {
        return vicinity;
    }
    @Override
    public String toString() {
        return "SearchLocations{" +
                "name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}