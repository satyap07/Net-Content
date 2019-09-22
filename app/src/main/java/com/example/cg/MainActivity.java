package com.example.cg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> photos=new ArrayList<String>();
    ArrayList<String> names=new ArrayList<String>();
    int count;
    ImageView imageView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    int correctanswer;
    int array[]= new int[4];

    public void changePicture(View view){
        if(view.getTag().toString().equals(Integer.toString(correctanswer))){
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "incorrect "+"it is "+names.get(count), Toast.LENGTH_SHORT).show();
        }
        mainFunction();

    }

    public class downloadContent extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try{
                String result="";
                URL url=new URL(urls[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream in=httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return "fail";
            }
        }
    }

    public class downloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in= urlConnection.getInputStream();
                Bitmap mybitmap= BitmapFactory.decodeStream(in);
                return mybitmap;


            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
    void mainFunction(){
        downloadImage imagetask=new downloadImage();
        try{
            Random random= new Random();
            count=random.nextInt(30);
            Bitmap myImage= imagetask.execute(photos.get(count)).get();
            imageView.setImageBitmap(myImage);
            correctanswer=random.nextInt(4);
            for(int i=0;i<4;i++){
                if(i==correctanswer)
                    array[i]=count;
                else {
                    int incorrect = random.nextInt(30);
                    while(incorrect==count){
                        incorrect=random.nextInt(30);
                    }
                    array[i]=incorrect;
                }

            }
            button0.setText(names.get(array[0]));
            button1.setText(names.get(array[1]));
            button2.setText(names.get(array[2]));
            button3.setText(names.get(array[3]));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);
        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        downloadContent obj=new downloadContent();
        try{
            String result=obj.execute("http://www.posh24.se/kandisar").get();
            String[] requiredResult=result.split("<div class=\"sidebarContainer\">");
            Pattern p=Pattern.compile("<img src=\"(.*?)\"");
            Matcher m=p.matcher(requiredResult[0]);
            while(m.find()){
                photos.add(m.group(1));
            }
            p=Pattern.compile("alt=\"(.*?)\"");
            m=p.matcher(requiredResult[0]);
            while(m.find()){
                names.add(m.group(1));
            }

        }catch (Exception e){
            e.printStackTrace();
            }

        mainFunction();
    }
}
