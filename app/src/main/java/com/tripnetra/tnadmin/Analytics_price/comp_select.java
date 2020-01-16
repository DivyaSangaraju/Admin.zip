package com.tripnetra.tnadmin.Analytics_price;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tripnetra.tnadmin.R;

public class comp_select extends AppCompatActivity {

    TextView hotl,tour,darshan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comp_select);
        hotl=findViewById(R.id.hcr);
        tour=findViewById(R.id.tcr);
        darshan=findViewById(R.id.dcr);
        hotl.setOnClickListener(view -> {
            Intent in = new Intent(this, ComparisonActivity.class);
            startActivity(in);
        });
        tour.setOnClickListener(view -> {
            Intent in = new Intent(this, Tour_Range.class);
            startActivity(in);
        });
        darshan.setOnClickListener(view -> {
            Intent in = new Intent(this, Darshan_Range.class);
            startActivity(in);
        });

    }
}
