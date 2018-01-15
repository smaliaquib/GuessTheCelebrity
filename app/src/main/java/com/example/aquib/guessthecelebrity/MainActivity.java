package com.example.aquib.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView BtnImg1;

    Button Btn1;
    Button Btn2;
    Button Btn3;
    Button Btn4;
    Button BtnGo;
    GridLayout Grid;

    TextView Txt;

    Random rand = new Random();

    int BtnRandom;

    String[] no1 = new String[100];
    String[] no2 = new String[100];

    public void Go(View view){


        Grid.setVisibility(View.VISIBLE);
        Txt.setVisibility(View.INVISIBLE);
        BtnGo.setVisibility(View.INVISIBLE);
        Guess();

    }

    public void Ans(View view){

        if(view.getTag().equals(String.valueOf(BtnRandom))){

            Toast.makeText(MainActivity.this,"Correct!!!",Toast.LENGTH_LONG).show();

        }else {

            Toast.makeText(MainActivity.this,"Wrong!!!",Toast.LENGTH_LONG).show();

        }

        Guess();

    }

    public void Guess(){


        GetBitmap GetBit = new GetBitmap();

        Bitmap image;

        int f = rand.nextInt(100);

        String ResultUrl = no1[f];
        String ResultName = no2[f];


        try {

            image = GetBit.execute(ResultUrl).get();

            BtnImg1.setImageBitmap(image);

            BtnRandom = rand.nextInt(4);

            String[] arr = new String[4];

            for(int i=0; i<4; i++) {
                if (i == BtnRandom) {

                    arr[i] = ResultName;

                } else {

                    int of = rand.nextInt(100);

                    if( of == f ){

                        arr[i] = no2[rand.nextInt(100)];

                    }

                    arr[i]=no2[rand.nextInt(100)];

                }

            }

            Btn1.setText(arr[0]);
            Btn2.setText(arr[1]);
            Btn3.setText(arr[2]);
            Btn4.setText(arr[3]);


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

    }

    class GetHtml extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection;

            try{

                url = new URL(urls[0]);
                urlConnection =(HttpURLConnection)url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){

                    char c = (char) data;

                    result +=c;

                    data = reader.read();


                }


            }catch (Exception e){

                e.printStackTrace();

            }

            return result;

        }
    }

    class GetBitmap extends AsyncTask<String,Void,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap image;
            URL url;
            HttpURLConnection urlconnection;

            try{

                url = new URL(urls[0]);
                urlconnection =(HttpURLConnection)url.openConnection();

                urlconnection.connect();

                InputStream in = urlconnection.getInputStream();

                image = BitmapFactory.decodeStream(in);

                return image;

            }catch (Exception e){

                e.printStackTrace();

            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn1 = (Button) findViewById(R.id.btn1);
        Btn2 = (Button) findViewById(R.id.btn2);
        Btn3 = (Button) findViewById(R.id.btn3);
        Btn4 = (Button) findViewById(R.id.btn4);
        BtnGo = (Button)findViewById(R.id.btnGo);

        Grid = (GridLayout)findViewById(R.id.gridLayout);

        Txt = (TextView)findViewById(R.id.textView2);

        BtnImg1 = (ImageView)findViewById(R.id.imageView);

        GetHtml GetHtml = new GetHtml();

        int c=0,d=0;

        try {

            String res = GetHtml.execute("http://www.posh24.se/kandisar").get();

            Pattern p = Pattern.compile(" src=\"(.*?)\" ");
            Matcher m = p.matcher( res );

            while(m.find()){

                no1[c]=m.group(1);
                c++;

            }

           // Log.i("First Url",first);  //same


            Pattern p1 = Pattern.compile("alt=\"(.*?)\"");
            Matcher m1 = p1.matcher( res );

            while(m1.find()){

                no2[d]=m1.group(1);
                d++;

            }

          //  Log.i("Second name",second); //got the same as per data
            
            
        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

    }
}
