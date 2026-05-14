package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Model.NotificationModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.NotificationHolder;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder>{

    ArrayList<NotificationModel> notificationModels;
    Context context;


    public NotificationAdapter(ArrayList<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout_notification, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

        holder.txt_notification.setText(notificationModels.get(position).getNotication_text());
        holder.txt_desc.setText(notificationModels.get(position).getMsgDescription());
        holder.txt_receiveDate.setText(notificationModels.get(position).getDateAgo());

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }
}
