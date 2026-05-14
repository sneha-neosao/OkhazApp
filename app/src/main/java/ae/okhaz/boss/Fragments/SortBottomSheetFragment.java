package ae.okhaz.boss.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import ae.okhaz.admin.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.util.HashMap;

/**
 * Created by Avinash on 07,December,2020
 */
public class SortBottomSheetFragment extends BottomSheetDialogFragment {

    RadioGroup radioGroup;
    int pos;
    String names="";

    SessionManagement sessionManagement;
    HashMap all, pending, processing, ontheway,delivered, cancelled;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, R.style.SheetDialog);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.item_sort_layout, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        pos = getArguments().getInt("position");
        names = getArguments().getString("name");

        sessionManagement = SessionManagement.getInstance(getContext());
        all = sessionManagement.getAllFilters();
        pending = sessionManagement.getPendingFilters();
        processing = sessionManagement.getProcessingFilters();
        ontheway = sessionManagement.getOntheWayFilters();
        delivered = sessionManagement.getDeliveredFilters();
        cancelled = sessionManagement.getCancelledFilters();

       // radioGroup = contentView.findViewById(R.id.sort_grp);

//        radioGroup.check(R.id.DOTR);

        if (pos == 0)
        {
            String ids = all.get(SessionManagement.KEY_All_SORT_ID).toString();
            SetRadios(ids);
        }
        else if (pos == 1)
        {
            String ids = pending.get(SessionManagement.KEY_PENDING_SORT_ID).toString();
            SetRadios(ids);
        }
        else if (pos == 2)
        {
            String ids = processing.get(SessionManagement.KEY_PROCESSING_SORT_ID).toString();
            SetRadios(ids);
        }
        else if (pos == 3)
        {
            String ids = ontheway.get(SessionManagement.KEY_ONTHEWAY_SORT_ID).toString();
            SetRadios(ids);
        }
        else if (pos == 4)
        {
            String ids =  delivered.get(SessionManagement.KEY_DELIVERED_SORT_ID).toString();
            SetRadios(ids);
        }
        else if (pos == 5)
        {
            String ids =  cancelled.get(SessionManagement.KEY_CANCELED_SORT_ID).toString();
            SetRadios(ids);

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.DOTR) {
                    send("DateOTR");
                    UpdateSessions("DOTR");

                } else if (i == R.id.DRTO) {
                    send("DateRTO");
                    UpdateSessions("DRTO");

                } else if (i == R.id.OOTR) {
                    send("OrderOTR");
                    UpdateSessions("OOTR");

                } else if (i == R.id.ORTO) {
                    send("OrderRTO");
                    UpdateSessions("ORTO");
                }
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void UpdateSessions(String dotr) {

        if (pos == 0)
        {
            sessionManagement.UpdateAllSort(dotr);
        }
        else if (pos == 1)
        {
            sessionManagement.UpdatePendingSort(dotr);
        }
        else if (pos == 2)
        {
            sessionManagement.UpdateProcessingSort(dotr);
        }
        else if (pos == 3)
        {
            sessionManagement.UpdateOntheWaySort(dotr);
        }
        else if (pos == 4)
        {
            sessionManagement.UpdateDeliveredSort(dotr);
        }
        else if (pos == 5)
        {
            sessionManagement.UpdateCancelSort(dotr);
        }
    }

    public void send(String msg)
    {
        Intent intent = new Intent("filter");
        intent.putExtra("btn",msg);
        intent.putExtra("type","sort");
        getContext().sendBroadcast(intent);

        dismiss();
    }


    public void SetRadios(String ids)
    {
        if (ids.equals("DOTR"))
        {
            radioGroup.check(R.id.DOTR);
        }
        else if (ids.equals("DRTO"))
        {
            radioGroup.check(R.id.DRTO);
        }
        else if (ids.equals("OOTR"))
        {
            radioGroup.check(R.id.OOTR);
        }
        else if (ids.equals("ORTO"))
        {
            radioGroup.check(R.id.ORTO);
        }
    }
}
