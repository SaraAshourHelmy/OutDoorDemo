package mediasci.com.outdoordemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mediasci.com.Util.CameraUtil;
import mediasci.com.Util.DBUtil;
import mediasci.com.Util.SendImage;
import mediasci.com.Util.SharedUtil;
import mediasci.com.models.Advertise;
import mediasci.com.models.AdvertiseDB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_camera, tv_galley;
    private Button btn_upload, btn_update_all;
    public static int Gallery = 1, Camera = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedUtil sharedUtil = new SharedUtil(this);
        if (!SharedUtil.GetCheck()) {

            // call db
            DBUtil.SetupDatabase(this);

            // change check
            SharedUtil.SetCheck();
        }
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_update_all = (Button) findViewById(R.id.btn_update_all);
        btn_upload.setOnClickListener(this);
        btn_update_all.setOnClickListener(this);
    }

    private void SetupDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_choose);
        tv_camera = (TextView) dialog.findViewById(R.id.tv_camera);
        tv_galley = (TextView) dialog.findViewById(R.id.tv_gallery);

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
                dialog.dismiss();
            }
        });

        tv_galley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void OpenCamera() {

        CameraUtil.captureImage(this);
    }

    private void OpenGallery() {

        CameraUtil.PickImageGallery(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btn_upload) {
            SetupDialog();
        } else if (v == btn_update_all) {
            new UpdateAll().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtil.CAMERA_CAPTURE_IMAGE_REQUEST_CODE
                && resultCode == RESULT_OK) {

            // successfully captured the image
            // display it in image view
            Log.e("image", "get");
            MoveToAdvertise(Camera);

        } else if (requestCode == CameraUtil.GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.e("image", "get");
            CameraUtil.fileUri = selectedImage;
            MoveToAdvertise(Gallery);

        }
    }

    private void MoveToAdvertise(int type) {

        Intent intent = new Intent(this, AdvertiseActivity.class);
        //intent.putExtra("file", uri);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public class UpdateAll extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SendAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    public void SendAll() {
        ArrayList<Advertise> lst_ads = AdvertiseDB.GetAdvertise(MainActivity.this);
        Advertise advertise;
        for (int i = 0; i < lst_ads.size(); i++) {

            advertise = lst_ads.get(i);
            String json = DBUtil.BuildJson(advertise);
            Log.e("json", json);
            SendImage.SendData(AdvertiseActivity.url, json, advertise.getImg());

        }
    }

}
