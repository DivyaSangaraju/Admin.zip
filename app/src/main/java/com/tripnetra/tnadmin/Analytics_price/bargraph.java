package com.tripnetra.tnadmin.Analytics_price;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tripnetra.tnadmin.R;

import java.util.ArrayList;

public class bargraph extends AppCompatActivity {
    Bundle bundle;
    BarChart chart;
    String yvalue1,yvalue2,yvalue3,xvalue1,xvalue2,xvalue3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph);
        chart = findViewById(R.id.chart3);
        bundle=getIntent().getExtras();
        assert bundle != null;
        yvalue1=bundle.getString("v1");
        yvalue2=bundle.getString("v2");
        yvalue3=bundle.getString("v3");
        xvalue1 = bundle.getString("c1");
        xvalue2 = bundle.getString("c2");
        xvalue3 = bundle.getString("c3");
        barchat();

    }


    private void barchat(){

        int ns = Integer.valueOf(xvalue1);
        int ns1 = Integer.valueOf(xvalue2);
        int ns2 = Integer.valueOf(xvalue3);
        int[] valuess={ns,ns1,ns2};
        String[] labels={yvalue1,yvalue2,yvalue3};

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels1 = new ArrayList<String>();
        for(int i=0;i<valuess.length;i++){
            entries.add(new BarEntry(valuess[i], i));
            labels1.add(labels[i]);
        }
        BarDataSet dataset = new BarDataSet(entries, "Comparison Reports");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(labels1, dataset);
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.invalidate();


    }
}
