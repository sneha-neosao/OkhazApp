package ae.okhaz.boss.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.button.MaterialButton;
import ae.okhaz.admin.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Avinash on 07,December,2020
 */
public  class BottomSheetFragment extends BottomSheetDialogFragment {
    private ItemClickListener mListener;
    String m;
    DatePickerDialog.OnDateSetListener setstartListner, setendListner;
    Spinner status_spiner;
    TextView start_date_tv,end_date_tv,txt_reset;
    EditText edt_order_id;
    View contentView;
    CoordinatorLayout.Behavior behavior;
    int pos;
    String names="";
    SessionManagement sessionManagement;
    HashMap all, pending, processing, ontheway,delivered, cancelled,placed,accepted,readypickup;
    Spinner spinner_layout;
    MaterialButton btn_filter_apply;

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


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        contentView = View.inflate(getContext(), R.layout.item_filter_layout, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color)));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color)));
        sessionManagement = SessionManagement.getInstance(getContext());
        all = sessionManagement.getAllFilters();
        pending = sessionManagement.getPendingFilters();
        processing = sessionManagement.getProcessingFilters();
        ontheway = sessionManagement.getOntheWayFilters();
        delivered = sessionManagement.getDeliveredFilters();
        cancelled = sessionManagement.getCancelledFilters();
        placed = sessionManagement.getPlacedFilters();
        accepted = sessionManagement.getAcceptedFilters();
        readypickup = sessionManagement.getReadyPickupFilters();

        pos = getArguments().getInt("position");
        names = getArguments().getString("name");

        status_spiner = contentView.findViewById(R.id.status_spiner);
        end_date_tv = contentView.findViewById(R.id.end_date_tv);
        edt_order_id = contentView.findViewById(R.id.edt_order_id);
        start_date_tv = contentView.findViewById(R.id.start_date_tv);
       /* txt_reset = contentView.findViewById(R.id.txt_reset);*/
        btn_filter_apply = contentView.findViewById(R.id.btn_filter_apply);
        spinner_layout = contentView.findViewById(R.id.status_spiner);

      /*  status_spiner = contentView.findViewById(R.id.status_spiner);
        end_date_tv = contentView.findViewById(R.id.end_date_tv);
        edt_order_id = contentView.findViewById(R.id.edt_order_id);
        start_date_tv = contentView.findViewById(R.id.start_date_tv);
        txt_reset = contentView.findViewById(R.id.txt_reset);
        txt_apply = contentView.findViewById(R.id.txt_apply);
        spinner_layout = contentView.findViewById(R.id.spinner_layout);*/

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);

       /* if (pos == 0)
        {
            end_date_tv.setText(all.get(SessionManagement.KEY_All_FILTER_END_DATE).toString());
            start_date_tv.setText(all.get(SessionManagement.KEY_All_FILTER_START_DATE).toString());
            edt_order_id.setText(all.get(SessionManagement.KEY_All_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.VISIBLE);
        }
        else if (pos == 1)
        {
            end_date_tv.setText(placed.get(SessionManagement.KEY_PLACED_FILTER_END_DATE).toString());
            start_date_tv.setText(placed.get(SessionManagement.KEY_PLACED_FILTER_START_DATE).toString());
            edt_order_id.setText(placed.get(SessionManagement.KEY_PLACED_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 2)
        {
            end_date_tv.setText(pending.get(SessionManagement.KEY_PENDING_FILTER_END_DATE).toString());
            start_date_tv.setText(pending.get(SessionManagement.KEY_PENDING_FILTER_START_DATE).toString());
            edt_order_id.setText(pending.get(SessionManagement.KEY_PENDING_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 11)
        {
            end_date_tv.setText(processing.get(SessionManagement.KEY_PROCESSING_FILTER_END_DATE).toString());
            start_date_tv.setText(processing.get(SessionManagement.KEY_PROCESSING_FILTER_START_DATE).toString());
            edt_order_id.setText(processing.get(SessionManagement.KEY_PROCESSING_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }

        else if (pos == 4)
        {
            end_date_tv.setText(readypickup.get(SessionManagement.KEY_READYPICKUP_FILTER_END_DATE).toString());
            start_date_tv.setText(readypickup.get(SessionManagement.KEY_READYPICKUP_FILTER_START_DATE).toString());
            edt_order_id.setText(readypickup.get(SessionManagement.KEY_READYPICKUP_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 6)
        {
            end_date_tv.setText(ontheway.get(SessionManagement.KEY_ONTHEWAY_FILTER_END_DATE).toString());
            start_date_tv.setText(ontheway.get(SessionManagement.KEY_ONTHEWAY_FILTER_START_DATE).toString());
            edt_order_id.setText(ontheway.get(SessionManagement.KEY_ONTHEWAY_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 7)
        {
            end_date_tv.setText(delivered.get(SessionManagement.KEY_DELIVERED_FILTER_END_DATE).toString());
            start_date_tv.setText(delivered.get(SessionManagement.KEY_DELIVERED_FILTER_START_DATE).toString());
            edt_order_id.setText(delivered.get(SessionManagement.KEY_DELIVERED_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 3)
        {
            end_date_tv.setText(accepted.get(SessionManagement.KEY_ACCEPTED_FILTER_END_DATE).toString());
            start_date_tv.setText(accepted.get(SessionManagement.KEY_ACCEPTED_FILTER_START_DATE).toString());
            edt_order_id.setText(accepted.get(SessionManagement.KEY_ACCEPTED_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else if (pos == 9)
        {
            end_date_tv.setText(cancelled.get(SessionManagement.KEY_CANCELED_FILTER_END_DATE).toString());
            start_date_tv.setText(cancelled.get(SessionManagement.KEY_CANCELED_FILTER_START_DATE).toString());
            edt_order_id.setText(cancelled.get(SessionManagement.KEY_CANCELED_FILTER_ORDER_ID).toString());
            status_spiner.setSelection(pos);
            spinner_layout.setVisibility(View.GONE);
        }
        else {
            start_date_tv.setText("Select start Date");
            end_date_tv.setText("Select End date");
        }
*/

        setstartListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month+1;

                if (month >0 && month<10)
                {
                    m = "0"+ month;
                }
                else {
                    m = String.valueOf( month);
                }
                String dates =  year+"-"+m+"-"+dayOfMonth;
//                        dayOfMonth +"/"+ month +"/"+year;
                start_date_tv.setText(dates);

            }
        };

//        Theme_Holo_Dialog_MinWidth
//        Theme_DeviceDefault_Dialog_Alert

        start_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,setstartListner,year,month,date);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();


            }
        });


        setendListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month+1;
                if (month >0 && month<10)
                {
                    m = "0"+ month;
                }
                else {
                    m = String.valueOf( month);
                }
                String dates = year +"-"+m+"-"+dayOfMonth;
//                        dayOfMonth +"/"+ month +"/"+year;


                end_date_tv.setText(dates);


            }
        };

        end_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,setendListner,year,month,date);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
         behavior = params.getBehavior();

        btn_filter_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos == 0)
                {
                    sessionManagement.UpdateAllFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 1)
                {
                    sessionManagement.UpdatePlacedFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 2)
                {
                    sessionManagement.UpdatePendingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 11)
                {
                    sessionManagement.UpdateProcessingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 3)
                {
                    sessionManagement.UpdateAcceptedFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 6)
                {
                    sessionManagement.UpdateOntheWayFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 4)
                {
                    sessionManagement.UpdateReadyPickFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 7)
                {
                    sessionManagement.UpdateDeliveredFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 9)
                {
                    sessionManagement.UpdateCancelledFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }

                Intent intent = new Intent("filter");
                intent.putExtra("orderId",edt_order_id.getText().toString().trim());
                intent.putExtra("startdate",start_date_tv.getText().toString());
                intent.putExtra("endDate",end_date_tv.getText().toString());
                intent.putExtra("status",status_spiner.getSelectedItem().toString());
                intent.putExtra("type","filter");
                getContext().sendBroadcast(intent);

                dismiss();


            }
        });

    /*    txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_order_id.setText("");
                start_date_tv.setText("");
                end_date_tv.setText("");
                status_spiner.setSelection(pos);

                if (pos == 0)
                {
                    sessionManagement.UpdateAllFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 1)
                {
                    sessionManagement.UpdatePendingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 2)
                {
                    sessionManagement.UpdateProcessingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 3)
                {
                    sessionManagement.UpdateOntheWayFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 4)
                {
                    sessionManagement.UpdateDeliveredFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }
                else if (pos == 5)
                {
                    sessionManagement.UpdateCancelledFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                }

                Intent intent = new Intent("filter");
                intent.putExtra("orderId",edt_order_id.getText().toString().trim());
                intent.putExtra("startdate",start_date_tv.getText().toString());
                intent.putExtra("endDate",end_date_tv.getText().toString());
                intent.putExtra("status",status_spiner.getSelectedItem().toString());
                intent.putExtra("type","filter");
                getContext().sendBroadcast(intent);

               dismiss();
            }
        });*/

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public interface ItemClickListener {
        void onItemClick(String orderId, String startdate, String endDate, String status);
    }


}
