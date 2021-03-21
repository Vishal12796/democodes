package com.democodes.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.democodes.main.databinding.ActivityMainBinding;
import com.democodes.main.dynamickey.DynamicKeyActivity;
import com.democodes.main.filedownload.FileDownloadActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = MainActivity.this;
    private String TAG = getClass().getSimpleName();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        setListeners();
    }

    private void setListeners() {
        binding.btnDynamicKey.setOnClickListener(this);
        binding.btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDynamicKey:
                startActivity(new Intent(context, DynamicKeyActivity.class));
                break;
            case R.id.btnDownload:
                startActivity(new Intent(context, FileDownloadActivity.class));
                break;
        }
    }
}