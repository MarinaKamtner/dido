package com.marinakamtner.diamonddogs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by marina on 25.06.15.
 */
public class DetailActivity extends Activity {

    public final static String LATITUDE = "com.marinakamtner.diamonddogs.LATITUDE";
    public final static String LONGITUDE = "com.marinakamtner.diamonddogs.LONGITUDE";
    public final static String FINANZAMTNAME = "com.marinakamtner.diamonddogs.FINANZAMTNAME";
    public final static String FINANZAMTADDRESS = "com.marinakamtner.diamonddogs.FINANZAMTADDRESS   ";

    TextView name, address, openinghours, phone;
    ImageView img_fa;
    Button btn_showmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        final Finanzamt finanzamt = (Finanzamt) i.getParcelableExtra(MainActivity.OBJECT_FINANZAMT);

        name = (TextView) findViewById(R.id.detail_tv_name);
        name.setText(finanzamt.getName());

        img_fa = (ImageView) findViewById(R.id.detail_iv_map);
        new DownloadImageTask(img_fa, getApplicationContext())
                .execute(finanzamt.getPhotourl());
        img_fa.setScaleType(ImageView.ScaleType.FIT_XY);

        address = (TextView) findViewById(R.id.detail_tv_address);
        address.setText(finanzamt.getPostcode() + " "+finanzamt.getLocation() + ", "+finanzamt.getStreet());

        // newLine when ',':
        openinghours = (TextView) findViewById(R.id.detail_tv_openinghours);
        String openinghoursPlain = finanzamt.getOpeninghours();
        String openinghoursSep = "";
        boolean afterKomma = false; // after NewLine (Komma) dont take the Space in the String
        for(char c : openinghoursPlain.toCharArray()){
            if (c == ',') {
            openinghoursSep += System.getProperty("line.separator");
                afterKomma = true;
        } else {
                if (afterKomma && c == ' '){
                    afterKomma = false;
                } else {
                    openinghoursSep +=c;
                }
            }
        }
        openinghours.setText(openinghoursSep);

        phone = (TextView) findViewById(R.id.detail_tv_phone);
        phone.setText(finanzamt.getPhone());
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone.getText()));
                startActivity(intent);
            }
        });

        btn_showmap = (Button) findViewById(R.id.detail_btn_showmap);
        btn_showmap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra(LATITUDE, finanzamt.getLatitude());
                intent.putExtra(LONGITUDE, finanzamt.getLongitude());
                intent.putExtra(FINANZAMTNAME, finanzamt.getName());
                intent.putExtra(FINANZAMTADDRESS, finanzamt.getPostcode() + " "+finanzamt.getLocation() + ", "+finanzamt.getStreet());
                startActivity(intent);
            }
        });
    }
}