package ae.okhaz.boss.Fragments.Admin;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import ae.okhaz.boss.Data.DashboardData;
import ae.okhaz.admin.R;

import java.util.ArrayList;

public class SalesFragment extends Fragment {
    LineChart status_chart;
    Typeface mTf;
    DashboardData dashboardData;
    TextView txt_lifetimeSale,txt_avgMonthlySale;
    public SalesFragment() {
        // Required empty public constructor
        dashboardData=DashboardData.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sales, container, false);

         mTf = Typeface.createFromAsset(getContext().getAssets(),"fonts/opensans_bold.ttf");
        //Typeface mTf = ResourcesCompat.getFont(getContext(), R.font.opensans_bold);
        status_chart=view.findViewById(R.id.status_chart);
        txt_lifetimeSale=view.findViewById(R.id.txt_lifetimeSale);
        txt_avgMonthlySale=view.findViewById(R.id.txt_avgMonthlySale);

        LineData data = getData(10, 100);
        data.setValueTypeface(mTf);
        data.setValueTextSize(6);
        data.setValueTextColor(R.color.black);
        setupChart(status_chart, data);
        return view;
    }

    private LineData getData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        //temporary random positions
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(R.color.colorprimary);
        set1.setCircleColor(R.color.colorprimary);
        set1.setHighLightColor(R.color.colorprimary);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }

    private LineData getDailySaleData() {

        ArrayList<Entry> values = new ArrayList<>();

        //temporary random positions
        for (int i = 1; i <= 24; i++) {
           // float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, DashboardData.getInstance().getHourlySaleByHr(i)));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DailySale");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(R.color.colorprimary);
        set1.setCircleColor(R.color.colorprimary);
        set1.setHighLightColor(R.color.colorprimary);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }



    @Override
    public void onResume() {
        super.onResume();
        dashboardData=DashboardData.getInstance();
        if(dashboardData!=null) {
            txt_lifetimeSale.setText(dashboardData.lifetimeSale);
            txt_avgMonthlySale.setText(dashboardData.avgMonthlySale);
        }

        if(DashboardData.getInstance().dailySaleList!=null)
        {
            LineData data = getDailySaleData();
            data.setValueTypeface(mTf);
            data.setValueTextSize(6);
            data.setValueTextColor(R.color.black);
            setupChart(status_chart, data);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setupChart(LineChart chart, LineData data) {

       // ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

       // chart.setBackgroundColor(R.color.black);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawAxisLine(true);

        chart.getXAxis().setTypeface(mTf);
        chart.getXAxis().setTextColor(R.color.black);

        // animate calls invalidate()...
        chart.animateX(2500);

    }

}