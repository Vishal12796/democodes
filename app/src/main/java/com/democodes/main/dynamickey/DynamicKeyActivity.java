package com.democodes.main.dynamickey;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.democodes.main.R;
import com.democodes.main.databinding.ActivityDynamicKeyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DynamicKeyActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = DynamicKeyActivity.this;
    private String TAG = getClass().getSimpleName();
    private ActivityDynamicKeyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_key);

        setListeners();
    }

    private void setListeners() {
        binding.btnConvert.setOnClickListener(this);
        binding.edtJson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConvert:
                convertJson();
                break;
            case R.id.btnClear:
                binding.edtJson.setText("");
                break;
        }
    }

    private void convertJson() {
        String jsonData = binding.edtJson.getText().toString().trim();
        if (jsonData.equals("")) {
            Toast.makeText(context, "Enter Json Data", Toast.LENGTH_LONG);
            return;
        }

        try {
            JSONObject responseObj = new JSONObject(jsonData);
            Iterator<String> iterator = responseObj.keys();
            ArrayList<String> objectList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();

            while (iterator.hasNext()) {
                String key = iterator.next();
//                Log.e("TAG", "key:" + key + " Value :" + responseObj.optString(key));
                objectList.add(responseObj.optString(key));
                jsonArray.put(responseObj.get(key));
            }

            Log.e(TAG, " Json Array : " + jsonArray.toString());
            binding.tvOutputJson.setText(jsonArray.toString(4));

        } catch (JSONException e) {
            Log.e(TAG, "Exception ex : " + e.getMessage());
            e.printStackTrace();
        }
    }
}