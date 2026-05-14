package ae.okhaz.boss.Utils;

import ae.okhaz.boss.Model.Locations;
import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

/**
 * Created by Avinash on 03,December,2020
 */
public class SortPlaces  implements Comparator<Locations> {

    LatLng currentLoc;

    public SortPlaces(LatLng current){
        currentLoc = current;
    }
    @Override
    public int compare(final Locations place1, final Locations place2) {
        double lat1 = place1.getLat();
        double lon1 = place1.getLng();
        double lat2 = place2.getLat();
        double lon2 = place2.getLng();

        double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
