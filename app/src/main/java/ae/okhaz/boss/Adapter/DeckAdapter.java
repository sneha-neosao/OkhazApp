package ae.okhaz.boss.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

import java.util.ArrayList;

import ae.okhaz.boss.Model.CourseModal;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {

    private ArrayList<CourseModal> courseData;
    private Context context;

    public DeckAdapter(ArrayList<CourseModal> courseData, Context context) {
        this.courseData = courseData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModal model = courseData.get(position);
        holder.tvCourseName.setText(model.getCourseName());
        holder.tvCourseDuration.setText(model.getCourseDuration());
        holder.tvCourseTracks.setText(model.getCourseTracks());
        holder.ivCourse.setImageResource(model.getImgId());
    }

    @Override
    public int getItemCount() {
        return courseData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseDuration, tvCourseTracks;
        ImageView ivCourse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.idTVCourseName);
            tvCourseDuration = itemView.findViewById(R.id.idTVCourseDuration);
            tvCourseTracks = itemView.findViewById(R.id.idTVCourseTracks);
            ivCourse = itemView.findViewById(R.id.idIVCourse);
        }
    }
}
