package ae.okhaz.boss.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Avinash on 03,December,2020
 */
public class Place {

    public LatLng latlng;


    public Place(LatLng latlng) {
        this.latlng = latlng;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    @Override
    public String toString() {
        return "Place{" +
                "latlng=" + latlng +
                '}';
    }
}
