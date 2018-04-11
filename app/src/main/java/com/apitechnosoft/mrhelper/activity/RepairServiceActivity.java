package com.apitechnosoft.mrhelper.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.apitechnosoft.mrhelper.R;
import com.apitechnosoft.mrhelper.adapters.RepairServiceAdapter;
import com.apitechnosoft.mrhelper.adapters.SelectLocationAdapter;
import com.apitechnosoft.mrhelper.models.Locationreportdata;
import com.apitechnosoft.mrhelper.utilities.CompatibilityUtility;
import com.apitechnosoft.mrhelper.utilities.FontManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RepairServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_service);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
    }
    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(RepairServiceActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void initView(){
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");

        String locationstr="";
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            locationstr= extras.getString("Location");

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            ArrayList<Locationreportdata> locationList = new Gson().fromJson(locationstr, new TypeToken<ArrayList<Locationreportdata>>() {
            }.getType());

            RepairServiceAdapter mAdapter = new RepairServiceAdapter(RepairServiceActivity.this,locationList);
            recyclerView.setAdapter(mAdapter);
        }


    }
}