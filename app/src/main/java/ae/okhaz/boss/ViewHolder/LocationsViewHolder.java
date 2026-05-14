package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

public class LocationsViewHolder extends RecyclerView.ViewHolder {
    public TextView location_nm,location_address,start_tv,done_tv;
    public LinearLayout navigations_item_lin;

    public LocationsViewHolder(@NonNull View itemView) {
        super(itemView);

        location_nm = itemView.findViewById(R.id.location_nm);
        location_address = itemView.findViewById(R.id.location_address);
        navigations_item_lin = itemView.findViewById(R.id.navigations_item_lin);
        start_tv = itemView.findViewById(R.id.start_tv);
        done_tv = itemView.findViewById(R.id.done_tv);
    }
}
