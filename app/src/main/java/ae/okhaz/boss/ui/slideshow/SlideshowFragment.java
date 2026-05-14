package ae.okhaz.boss.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Activitys.MainActivity;
import ae.okhaz.admin.R;

public class SlideshowFragment extends Fragment {

    View view;
    RecyclerView notification_recyclers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_slideshow, container, false);

         notification_recyclers = view.findViewById(R.id.notification_recyclers);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).CheckDeliveryMan();
    }
}