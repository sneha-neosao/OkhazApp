package ae.okhaz.boss.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Model.Comments;
import ae.okhaz.admin.R;

import java.util.ArrayList;



public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    ArrayList<Comments> commentsArrayList;
    Context context;

    public CommentsAdapter(ArrayList<Comments> commentsArrayList, Context context) {
        this.commentsArrayList = commentsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.tv_dates.setText(commentsArrayList.get(position).getPostedDate());
        holder.tv_status.setText(commentsArrayList.get(position).getMessage());

        holder.tv_users.setText(commentsArrayList.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_dates,tv_status,tv_users;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_dates = itemView.findViewById(R.id.tv_dates);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_users = itemView.findViewById(R.id.tv_users);
        }
    }
}
