package com.example.kieykouch.wevotethis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kieykouch on 3/11/16.
 */
public class result extends AppCompatActivity {
    private  Data dc;
    private ArrayList<Apoliticians> all;
    TextView locationx;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        locationx = (TextView) findViewById(R.id.current_location_city);

        Bundle extras = getIntent().getExtras();
        String pass = "";
        if (extras != null){
            pass = (String) extras.get("zip");
            if (!pass.equals("")){
                locationx.setText("Zipcode: "+pass);
            }
        }

        populateViewList();
    }



    private void populateViewList() {
        ArrayAdapter<Apoliticians> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Apoliticians> {

        public MyListAdapter() {
            super(result.this, R.layout.info, Data.getList_poli());
        }


        public View getView(final int position, final View convertView, final ViewGroup parent){
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.info, parent, false);
            }

            Data dc = Data.getInstance();
            final Apoliticians currentInfo = dc.getList_poli().get(position);

            final ImageView repImage = (ImageView) itemView.findViewById(R.id.imageView7);
            repImage.setVisibility(View.GONE);
            ImageButton website1 = (ImageButton) itemView.findViewById(R.id.web_1);
            ImageButton more1 = (ImageButton) itemView.findViewById(R.id.more_1);

            TextView name = (TextView) itemView.findViewById(R.id.textView23);
            TextView party = (TextView) itemView.findViewById(R.id.textView24);
            TextView twitt = (TextView) itemView.findViewById(R.id.textView25);
            TextView email = (TextView) itemView.findViewById(R.id.email);


            name.setText(currentInfo.getName());
            party.setText(currentInfo.getParty());
            twitt.setText("");

            String mylasttwitebaby = currentInfo.getLast_tweet();
            if (mylasttwitebaby.equals("")){

            }
            else{
                twitt.setText(mylasttwitebaby);
            }

            if (currentInfo.getParty().equals("Republican")){
                name.setTextColor(Color.parseColor("#0025f6"));
                party.setTextColor(Color.parseColor("#0025f6"));
            }
            else{
                name.setTextColor(Color.parseColor("#ff0000"));
                party.setTextColor(Color.parseColor("#ff0000"));
            }

            final String theiremail = currentInfo.getEmail();
            String htmlString = "<u>"+theiremail+"</u>";
            email.setText(Html.fromHtml(htmlString));

            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", theiremail);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(result.this, "Copy " + theiremail, Toast.LENGTH_LONG).show();
                }
            });

            website1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(currentInfo.getWebsite()));
                    startActivity(intent);
                }
            });

            more1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent current1 = new Intent(result.this, detail.class);
                    current1.putExtra("Who", Integer.toString(position));
                    startActivity(current1);
                }
            });

            new Photo_politicians((ImageView) repImage).execute(currentInfo.getPhotourl());
            repImage.setVisibility(View.VISIBLE);
            return itemView;
        }
    }

}
