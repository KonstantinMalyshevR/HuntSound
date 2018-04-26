package com.hunt.huntsound;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


//Created by Developer on 04.04.18.

public class PrimaryActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.sliding_tabs) TabLayout tabLayout;

    MediaPlayer mediaPlayerLeft;
    MediaPlayer mediaPlayerRight;
    MediaPlayer mediaPlayerMono;
    //=============
    //=============
    //=============
    StereoFragmentInterface stereoCallback;
    interface StereoFragmentInterface {
        void setFileNameLeft(String value);
        void setFileNameRight(String value);
    }

    public void registerStereoCallback(StereoFragmentInterface callback) {
        this.stereoCallback = callback;
    }
    //=============

    MonoFragmentInterface monoCallback;
    interface MonoFragmentInterface{
        void setFileNameMono(String value);
    }
    public void registerMonoCallback(MonoFragmentInterface callback){
        this.monoCallback = callback;
    }
    //=============
    //=============
    //=============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        ButterKnife.bind(this);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mediaPlayerLeft = new MediaPlayer();
        mediaPlayerRight = new MediaPlayer();
        mediaPlayerMono = new MediaPlayer();

        setupViewPager();
        serviceSearcher();
    }

    //=====================
    //Автоматически запускает сервис  Foreground!
    private void serviceSearcher() {
        boolean tStartService = true;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);
        String serviceName = ServiceClass.class.getName();
        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            if (serviceName.equalsIgnoreCase(rsi.service.getClassName())) {
                tStartService = false;
                break;
            }
        }

        if (tStartService) {
            startService(new Intent(this, ServiceClass.class));
        }
    }

    //=====================
    public void onFragmentStereoCreate(){
        stereoCallback.setFileNameLeft("Выберите аудио файл");
        stereoCallback.setFileNameRight("Выберите аудио файл");
    }

    public void onFragmentMonoCreate(){
        monoCallback.setFileNameMono("Выберите аудио файл");
    }
    //===========
    //===========
    //===========
    public void permissionRequest(final int requestCode){

        if (ContextCompat.checkSelfPermission(PrimaryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PrimaryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(findViewById(android.R.id.content), "Для использования этой функции необходимо дать разрешение",
                        Snackbar.LENGTH_INDEFINITE).setAction("Ок",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(PrimaryActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(PrimaryActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
        } else {
            getAudio(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAudio(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Вы можете дать разрешение в Настройках приложения",
                    Snackbar.LENGTH_INDEFINITE).setAction("Перейти",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    //===========
    private void getAudio(int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg3");
        startActivityForResult(Intent.createChooser(intent, "Аудио файлы"), requestCode);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == Activity.RESULT_OK && data != null) {

            Uri audioFileUri = data.getData();

            String fileName = "";

            if(audioFileUri != null){

                String scheme = audioFileUri.getScheme();

                if (scheme.equals("file")) {
                    fileName = audioFileUri.getLastPathSegment();
                }

                if (reqCode == SupportClass.LEFT_DINAMIC) {
                    if(mediaPlayerMono.isPlaying()) {
                        mediaPlayerMono.stop();
                    }
                    mediaPlayerMono.reset();
                    monoCallback.setFileNameMono("Выберите аудио файл");

                    stereoCallback.setFileNameLeft(fileName);
                    initMediaPlayerLeft(audioFileUri);
                }

                if (reqCode == SupportClass.RIGHT_DINAMIC) {
                    if(mediaPlayerMono.isPlaying()) {
                        mediaPlayerMono.stop();
                    }
                    mediaPlayerMono.reset();
                    monoCallback.setFileNameMono("Выберите аудио файл");

                    stereoCallback.setFileNameRight(fileName);
                    initMediaPlayerRight(audioFileUri);
                }

                if (reqCode == SupportClass.MONO_DINAMIC) {
                    if(mediaPlayerLeft.isPlaying()) {
                        mediaPlayerLeft.stop();
                    }
                    mediaPlayerLeft.reset();
                    stereoCallback.setFileNameLeft("Выберите аудио файл");

                    if(mediaPlayerRight.isPlaying()) {
                        mediaPlayerRight.stop();
                    }
                    mediaPlayerRight.reset();
                    stereoCallback.setFileNameRight("Выберите аудио файл");

                    monoCallback.setFileNameMono(fileName);
                    initMediaPlayerMono(audioFileUri);
                }

            }else{
                SupportClass.ToastMessage(PrimaryActivity.this, "Файл не найден. Попробуйте выбрать другой файл");
            }
        }
    }

    //===========
    //===========
    private void initMediaPlayerLeft(Uri audioFileUri){

        try {
            if(mediaPlayerLeft.isPlaying()) {
                mediaPlayerLeft.stop();
            }
            mediaPlayerLeft.reset();
            mediaPlayerLeft.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayerLeft.setDataSource(PrimaryActivity.this, audioFileUri);

            mediaPlayerLeft.prepare();
            mediaPlayerLeft.setVolume(1.f, 0.f);

        } catch (IOException e) {
            SupportClass.ToastMessage(PrimaryActivity.this, "Ошибка!");
        } catch (IllegalStateException e1){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой");
        }catch (IllegalArgumentException e2){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой файл");
        }
    }

    public void mPLeftPlay(){
        if(mediaPlayerLeft != null){
            mediaPlayerLeft.start();
            mediaPlayerLeft.setLooping(true);
            SupportClass.ToastMessage(PrimaryActivity.this, "Play левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPLeftPause(){
        if(mediaPlayerLeft != null){
            mediaPlayerLeft.pause();
            SupportClass.ToastMessage(PrimaryActivity.this, "Pause левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPLeftStop(){
        if(mediaPlayerLeft != null){
            mediaPlayerLeft.stop();
            mediaPlayerLeft.reset();
            stereoCallback.setFileNameLeft("Выберите аудио файл");
            SupportClass.ToastMessage(PrimaryActivity.this, "Stop левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    //===========
    private void initMediaPlayerRight(Uri audioFileUri){

        try {
            if(mediaPlayerRight.isPlaying()) {
                mediaPlayerRight.stop();
            }
            mediaPlayerRight.reset();
            mediaPlayerRight.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayerRight.setDataSource(PrimaryActivity.this, audioFileUri);

            mediaPlayerRight.prepare();
            mediaPlayerRight.setVolume(0.f, 1.f);

        } catch (IOException e) {
            SupportClass.ToastMessage(PrimaryActivity.this, "Ошибка!");
        } catch (IllegalStateException e1){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой");
        }catch (IllegalArgumentException e2){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой файл");
        }
    }

    public void mPRightPlay(){
        if(mediaPlayerRight != null){
            mediaPlayerRight.start();
            mediaPlayerRight.setLooping(true);
            SupportClass.ToastMessage(PrimaryActivity.this, "Play правый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPRightPause(){
        if(mediaPlayerRight != null){
            mediaPlayerRight.pause();
            SupportClass.ToastMessage(PrimaryActivity.this, "Pause правый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPRightStop(){
        if(mediaPlayerRight != null){
            mediaPlayerRight.stop();
            mediaPlayerRight.reset();
            stereoCallback.setFileNameRight("Выберите аудио файл");
            SupportClass.ToastMessage(PrimaryActivity.this, "Stop правый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    //===========
    private void initMediaPlayerMono(Uri audioFileUri){

        try {
            if(mediaPlayerMono.isPlaying()) {
                mediaPlayerMono.stop();
            }
            mediaPlayerMono.reset();
            mediaPlayerMono.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayerMono.setDataSource(PrimaryActivity.this, audioFileUri);

            mediaPlayerMono.prepare();
            mediaPlayerMono.setVolume(1.f, 1.f);

        } catch (IOException e) {
            SupportClass.ToastMessage(PrimaryActivity.this, "Ошибка!");
        } catch (IllegalStateException e1){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой");
        }catch (IllegalArgumentException e2){
            SupportClass.ToastMessage(PrimaryActivity.this, "Не верный формат, выберите другой файл");
        }
    }

    public void mPMonoPlay(){
        if(mediaPlayerMono != null){
            mediaPlayerMono.start();
            mediaPlayerMono.setLooping(true);
            SupportClass.ToastMessage(PrimaryActivity.this, "Play левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPMonoPause(){
        if(mediaPlayerMono != null){
            mediaPlayerMono.pause();
            SupportClass.ToastMessage(PrimaryActivity.this, "Pause левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    public void mPMonoStop(){
        if(mediaPlayerMono != null){
            mediaPlayerMono.stop();
            mediaPlayerMono.reset();
            monoCallback.setFileNameMono("Выберите аудио файл");
            SupportClass.ToastMessage(PrimaryActivity.this, "Stop левый динамик");
        }else{
            SupportClass.ToastMessage(PrimaryActivity.this, "Выберите мелодию");
        }
    }

    //===========
    //===========
    //===========
    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StereoFragment(), "Стерео");
        adapter.addFragment(new MonoFragment(), "Моно");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
        } else {

            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            SupportClass.ToastMessage(PrimaryActivity.this, "Для выхода - нажмите еще раз");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    //===========
    //===========
    //===========
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.primary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(PrimaryActivity.this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}