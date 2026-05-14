package ae.okhaz.boss.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {


    public static ConnectivityReciverListner connectivityReciverListner;


    public NetworkChangeReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworks = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetworks != null && activeNetworks.isConnected();

        if (connectivityReciverListner != null)
        {
            connectivityReciverListner.OnNetworkChange(isConnected);
        }
    }


    public interface ConnectivityReciverListner
    {
        void OnNetworkChange(boolean isConnected);
    }
}
