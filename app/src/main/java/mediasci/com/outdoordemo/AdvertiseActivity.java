package mediasci.com.outdoordemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.nio.ByteBuffer;

import mediasci.com.Util.CameraUtil;
import mediasci.com.Util.DBUtil;
import mediasci.com.Util.GPSTracker;
import mediasci.com.Util.SendImage;
import mediasci.com.models.Advertise;
import mediasci.com.models.AdvertiseDB;

public class AdvertiseActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String url = "http://192.168.1.236:8084/OutdoorsAds/rest/mobile/uploadSign";
    public static String token =
            "IoUDlBfRCKrNu9kl1h1Ilekj5WhY1nv5UJYbZb69VRyfcrb8H6tHhZ61hwSNT1Wu";
    double latitude = 0, longitude = 0;
    Bitmap bitmap;
    String gps_address, address, title, note, json;
    boolean revise;
    int click_type;
    Advertise advertise;
    private ImageView img_camera;
    private EditText et_gps_address, et_address, et_title, et_note;
    private Spinner spnr_type;
    private CheckBox chk_revise;
    private Button btn_send, btn_save;
    private int type = 1;
    private byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        SetupTools();
        GetImage();
    }

    private void SetupTools() {
        img_camera = (ImageView) findViewById(R.id.img_camera);
        et_gps_address = (EditText) findViewById(R.id.et_gps_address);
        et_address = (EditText) findViewById(R.id.et_address);
        et_title = (EditText) findViewById(R.id.et_title);
        et_note = (EditText) findViewById(R.id.et_note);
        spnr_type = (Spinner) findViewById(R.id.spnr_type);
        chk_revise = (CheckBox) findViewById(R.id.chk_revise);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_send.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        String[] typeItems = getResources().getStringArray(R.array.type_items);
        spnr_type.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1
                , typeItems));

        spnr_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                type = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void GetImage() {
        Bundle bundle = getIntent().getExtras();
        //if (bundle.containsKey("file")) {

        Uri file_uri = CameraUtil.fileUri; //(Uri) bundle.get("file");
        if (bundle.get("type") == MainActivity.Camera) {
            //BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images

            // options.inSampleSize = 5;// if less size increase

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            // getbitmap(file_uri);

        } else {
            try {
                //uriToBitmap(file_uri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        file_uri);
                // uriToBitmap(file_uri);
                // getbitmap(file_uri);
            } catch (Exception e) {
                Log.e("gallery_error", e + "");
            }


        }
        //  Log.e("image", "converted");

        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //img = stream.toByteArray();


        // convert bitmap to byte[] quick
        // ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        //bitmap.copyPixelsToBuffer(byteBuffer);
        //img = byteBuffer.array();

        Log.e("image", "view");
        img_camera.setImageBitmap(bitmap);

        //}

    }


   

    @Override
    public void onClick(View v) {

        if (v == btn_send) {
            click_type = 1;
        } else if (v == btn_save) {
            click_type = 2;
        }
        GetData();
    }

    private void GetData() {
        GPSTracker gpsTracker = new GPSTracker(this);
        gps_address = et_gps_address.getText().toString();
        address = et_address.getText().toString();
        title = et_title.getText().toString();
        note = et_note.getText().toString();
        revise = chk_revise.isChecked();

        // convert bitmap to byte
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        img = byteBuffer.array();
        //--------------------------

        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            Log.e("lat", latitude + "");
            longitude = gpsTracker.getLongitude();
            Log.e("lng", longitude + "");
        }
        SaveAds();

        if (click_type == 1) {
            // send multipart json
            json = DBUtil.BuildJson(advertise);
            new DataSending().execute();
        } else {
            AdvertiseDB.InsertAdvertise(this, advertise);
        }
    }

    public void SaveAds() {
        advertise = new Advertise();
        advertise.setGps_address(gps_address);
        advertise.setAddress(address);
        advertise.setTitle(title);
        advertise.setType(type);
        advertise.setNote(note);
        advertise.setRevise(revise ? 1 : 0);
        advertise.setImg(img);
        advertise.setLatitude(latitude);
        advertise.setLongitude(longitude);

    }

    public class DataSending extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SendImage.SendData(url, json, img);
            return null;
        }

    }
}
