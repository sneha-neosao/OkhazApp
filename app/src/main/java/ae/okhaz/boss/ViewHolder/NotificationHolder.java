package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;
public class NotificationHolder extends RecyclerView.ViewHolder{

   public TextView txt_notification,txt_desc,txt_receiveDate;
    public NotificationHolder(@NonNull View itemView) {
        super(itemView);

        txt_notification=itemView.findViewById(R.id.txt_notification);
        txt_desc=itemView.findViewById(R.id.txt_msgDescription);
        txt_receiveDate=itemView.findViewById(R.id.receiveDate);

    }
}
