package com.democodes.main.filedownload;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.democodes.main.R;
import com.democodes.main.databinding.ActivityFileDownloadBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = FileDownloadActivity.this;
    private String TAG = getClass().getSimpleName();
    private ActivityFileDownloadBinding binding;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_download);

        setListeners();
    }

    private void setListeners() {
        binding.btnClear.setOnClickListener(this);
        binding.btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClear:
                binding.edtURL.setText("");
                break;
            case R.id.btnDownload:
                downloadFile();
                break;
        }
    }

    public void downloadFile() {

        String fileURL = binding.edtURL.getText().toString().trim();

//        fileURL = "https://bravatech.webclueslab.com/assets/certificates/11/S00038.pdf";

        if (fileURL.equals("")) {
            Toast.makeText(context, "Enter File URL", Toast.LENGTH_LONG);
            return;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            return;
        }

        new DownloadFileFromURL().execute(fileURL);


        ContextWrapper cw = new ContextWrapper(context);
        String fullPath = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream


                ContextWrapper cw = new ContextWrapper(context);
                String fullPath = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/temp.pdf";
//                File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

//                fullPath = context.getFilesDir().getPath() + "/temp.pdf";


                Log.e(TAG, " STORAGE PATH :  " + fullPath);

                OutputStream output = new FileOutputStream(fullPath);
//                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/temp.pdf");
//                OutputStream output = new FileOutputStream(fullPath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

        }

    }
}