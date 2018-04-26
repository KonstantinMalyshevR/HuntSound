package com.hunt.huntsound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

//Created by Developer on 14.03.18.

public class StereoFragment extends Fragment implements PrimaryActivity.StereoFragmentInterface{

    @BindView(R.id.image1) ImageView imageView1;
    @BindView(R.id.buttonPause1) ImageButton buttonPause1;
    @BindView(R.id.buttonPlay1) ImageButton buttonPlay1;
    @BindView(R.id.buttonStop1) ImageButton buttonStop1;
    @BindView(R.id.textFileName1) TextView textFileName1;

    @BindView(R.id.image2) ImageView imageView2;
    @BindView(R.id.buttonPause2) ImageButton buttonPause2;
    @BindView(R.id.buttonPlay2) ImageButton buttonPlay2;
    @BindView(R.id.buttonStop2) ImageButton buttonStop2;
    @BindView(R.id.textFileName2) TextView textFileName2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stereo, container, false);

        ButterKnife.bind(this, view);

        final PrimaryActivity primaryActivity = (PrimaryActivity) getActivity();
        primaryActivity.registerStereoCallback(this);

        //==================
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.permissionRequest(SupportClass.LEFT_DINAMIC);
            }
        });

        buttonPause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPLeftPause();
            }
        });

        buttonPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPLeftPlay();
            }
        });

        buttonStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPLeftStop();
            }
        });

        //==================
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.permissionRequest(SupportClass.RIGHT_DINAMIC);
            }
        });

        buttonPause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPRightPause();
            }
        });

        buttonPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPRightPlay();
            }
        });

        buttonStop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryActivity.mPRightStop();
            }
        });

        primaryActivity.onFragmentStereoCreate();

        return view;
    }

    @Override
    public void setFileNameLeft(String value) {
        textFileName1.setText(value);
    }

    @Override
    public void setFileNameRight(String value) {
        textFileName2.setText(value);
    }
}