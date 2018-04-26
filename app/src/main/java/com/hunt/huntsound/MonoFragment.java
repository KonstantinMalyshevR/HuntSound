package com.hunt.huntsound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

//Created by Developer on 14.03.18.

public class MonoFragment extends Fragment implements PrimaryActivity.MonoFragmentInterface{

    @BindView(R.id.image1) ImageView imageView1;
    @BindView(R.id.buttonPause1) ImageButton buttonPause1;
    @BindView(R.id.buttonPlay1) ImageButton buttonPlay1;
    @BindView(R.id.buttonStop1) ImageButton buttonStop1;
    @BindView(R.id.textFileName1) TextView textFileName1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mono, container, false);

        ButterKnife.bind(this, view);

        final PrimaryActivity primaryActivity = (PrimaryActivity) getActivity();
        primaryActivity.registerMonoCallback(this);

        //==================
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.permissionRequest(SupportClass.MONO_DINAMIC);
            }
        });

        buttonPause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPMonoPause();
            }
        });

        buttonPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPMonoPlay();
            }
        });

        buttonStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPMonoStop();
            }
        });

        //==================
        primaryActivity.onFragmentMonoCreate();

        return view;
    }

    @Override
    public void setFileNameMono(String value) {
        textFileName1.setText(value);
    }
}