package com.apitechnosoft.mrhelper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apitechnosoft.mrhelper.R;
import com.apitechnosoft.mrhelper.adapters.CompletedServiceAdapter;
import com.apitechnosoft.mrhelper.adapters.OnGoingBookingAdapter;
import com.apitechnosoft.mrhelper.circlecustomprogress.CircleDotDialog;
import com.apitechnosoft.mrhelper.database.DbHelper;
import com.apitechnosoft.mrhelper.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.mrhelper.framework.ServiceCaller;
import com.apitechnosoft.mrhelper.models.Bookservicelist;
import com.apitechnosoft.mrhelper.models.CompleteJobList;
import com.apitechnosoft.mrhelper.models.ContentMybooking;
import com.apitechnosoft.mrhelper.utilities.Contants;
import com.apitechnosoft.mrhelper.utilities.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class OldBookingFragment extends Fragment {


    public OldBookingFragment() {
        // Required empty public constructor
    }

    private Context context;
    private View view;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_old_booking, container, false);
        init();
        return view;
    }

    private void init() {
         recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void getCompleteService() {
        if (Utility.isOnline(context)) {
            String phone = Utility.getUserPhoneNo(context);
            if (phone != null) {
                final CircleDotDialog dotDialog = new CircleDotDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callCompletedService(phone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            parseCompleteData(result);
                        } else {
                            Utility.alertForErrorMessage("Data not Found!", context);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            }
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//off line msg....
        }
    }

    private void parseCompleteData(String result) {
        ContentMybooking data = new Gson().fromJson(result, ContentMybooking.class);
        if (data != null) {
            if (data.getCompleteJobList() != null) {
                List<CompleteJobList> list = new ArrayList<CompleteJobList>();
                for (CompleteJobList bookservicelist : data.getCompleteJobList()) {
                    list.add(bookservicelist);
                }
                CompletedServiceAdapter mAdapter = new CompletedServiceAdapter(context, list, false);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCompleteService();
    }

}
