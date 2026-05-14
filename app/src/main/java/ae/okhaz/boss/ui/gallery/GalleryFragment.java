package ae.okhaz.boss.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ae.okhaz.boss.Activitys.MainActivity;
import ae.okhaz.admin.R;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.util.HashMap;

public class GalleryFragment extends Fragment {

    View root;
    TextView user_contact_txt,user_email_txt,user_id_txt,user_name_txt;
    SessionManagement sessionManagement;
    HashMap user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_gallery, container, false);
         user_contact_txt = root.findViewById(R.id.user_id_txt);
        user_email_txt = root.findViewById(R.id.user_email_txt);
        user_id_txt = root.findViewById(R.id.user_id_txt);
        user_name_txt = root.findViewById(R.id.user_name_txt);

        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();

        user_email_txt.setText(user.get(SessionManagement.KEY_EMAIL).toString());
        user_id_txt.setText(user.get(SessionManagement.KEY_DELIVERY_MAN).toString());
        user_name_txt.setText(user.get(SessionManagement.KEY_USER_NAME).toString());



        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).CheckDeliveryMan();
    }
}