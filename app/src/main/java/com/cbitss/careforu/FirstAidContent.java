package com.cbitss.careforu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
//import static com.cbitss.careforu.ImageStorage.saveToSdCard;
//import static com.cbitss.careforu.R.id.imageView;

public class FirstAidContent extends AppCompatActivity {
    WebView html;
    String html_text,updated,title,text,photo;
    int id;
    List <String> images = new ArrayList<>();
    ProgressDialog loading;
    Toolbar toolbar;
    Bitmap bitmap;
    boolean offline;
    ImageView back;
    TextView toolbar_title,save;
    File sdcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_content);
        html = (WebView) findViewById(R.id.fcontent_web);
        toolbar = (Toolbar) findViewById(R.id.fac_toolbar);
        setSupportActionBar(toolbar);
        save = (TextView) toolbar.findViewById(R.id.save_offline);
        back = (ImageView) toolbar.findViewById(R.id.back_button);
        Intent intent = getIntent();
        offline = intent.getExtras().getBoolean("offline");
        toolbar_title = (TextView)toolbar.findViewById(R.id.fac_title);
        id = intent.getExtras().getInt("pid");
        title = intent.getExtras().getString("title");
        toolbar_title.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(offline)
        {
            File path = getExternalFilesDir(null);
            html.loadUrl("file://"+path+"/tips_"+id+".html");
            save.setVisibility(View.GONE);
        }
        else
        {
            text = intent.getExtras().getString("text");
            photo = intent.getExtras().getString("photo");
            html_text = intent.getExtras().getString("html");

            //html.loadUrl("file:///storage/emulated/0/.CareForU/kuch.html");
            html.loadData(html_text,"text/html", "UTF-8");
            save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    images.clear();
                    sdcard = getExternalFilesDir(null);
                    File file = new File(sdcard+"/tips_"+id+".html");
                    if(file.exists())
                        Toast.makeText(FirstAidContent.this, "Already Exists..", Toast.LENGTH_SHORT).show();
                    else
                    {
                        // Updating string with new image ids
                        updated = replace(id+"");
                        loading = ProgressDialog.show(FirstAidContent.this, "Saving Data...","Please Wait...",true,true);
                        ArrayList ar=new ArrayList();
                        ar.add(id);
                        ar.add(title);
                        ar.add(text);
                        DatabaseHandler dh= new DatabaseHandler(getApplicationContext());
                        dh.addTips(ar);
                        //Saving images

                        for(int i=0;i<images.size();i++) {
                            save(i);
                        }
                        try {
                            savehtml("tips_"+id+".html");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //loading.dismiss();
                        //save();
                    }
                }
            });
        }

    }



    private void save(final int counter) {

        class SaveHtml extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getApplicationContext(), "Saving Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(counter==images.size()-1)
                {
                    loading.dismiss();
                    Toast.makeText(FirstAidContent.this,"Successfully Saved", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                int i = Integer.parseInt(params[0]);
                    Log.d(i+"Checker",images.get(i));
                    String path = images.get(i);
                    URL url = null;
                    try {
                        url = new URL(path);
                        URLConnection conn = null;
                        conn = url.openConnection();
                        bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String stored = null;
                    File file = new File(sdcard,id+"_"+i+".jpg") ;
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        stored = "success";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(FirstAidContent.this,i+" image downloaded", Toast.LENGTH_SHORT).show();
                return null;
                }

            }
        final SaveHtml sh = new SaveHtml();
        sh.execute(counter+"");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if ( sh.getStatus() == AsyncTask.Status.RUNNING ) {
                    sh.cancel(true);
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        },15000 );
        }

    private void savehtml(String filename) throws IOException {
        File path = getExternalFilesDir(null);
        Log.d("path is",path+"");
        File file = new File(path, filename);
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(updated.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
            //Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    private void getImages()
    {
         class GetImages extends AsyncTask<Object, Object, Object> {
            private String requestUrl, imagename_;
            private Bitmap bitmap ;
            private FileOutputStream fos;
             GetImages(String requestUrl,String _imagename_) {
                this.requestUrl = requestUrl;
                this.imagename_ = _imagename_ ;
            }

            @Override
            protected Object doInBackground(Object... objects) {
                try {
                    URL url = new URL(requestUrl);
                    URLConnection conn = url.openConnection();
                    bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                } catch (Exception ex) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(!ImageStorage.checkifImageExists(imagename_))
                {
                    String a = ImageStorage.saveToSdCard(bitmap, imagename_);
                    Toast.makeText(FirstAidContent.this,a, Toast.LENGTH_SHORT).show();
                }
            }
        }
        GetImages getImages = new GetImages("http://i.imgur.com/N3GkQ4v.jpg","hello2.jpg");
        getImages.execute();
    }

}

class ImageStorage {


    public static String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory() ;

        File folder = new File(sdcard.getAbsoluteFile(), "/.CareForU");//the dot makes this directory hidden to the user
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename) ;
        Log.d("Check buddy:",file+"");
        if (file.exists())
        {
            Log.d("Hogya","Yes");
            return stored ;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d("Check yaar",stored);
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/.CareForU/"+imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }
    public static boolean checkifImageExists(String imagename)
    {
        Bitmap b = null ;
        File file = ImageStorage.getImage("/"+imagename);
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    } */
    public String replace(String id)
    {
        images.add(photo);
        int start,end;
        StringBuilder s =new  StringBuilder(html_text);
        String findStr = "src";
        int i = 0;
        int count,count_img=0;
        String n=id+"_";
        while(i != s.length())
        {
            count=0;
            i= s.indexOf(findStr,i);
            if(i!=-1)
            {i += 3;count_img++;}
            else
                break;

            while(i != s.length())
            {
                if(s.charAt(i) =='"')
                {
                    start=++i;

                    while(s.charAt(i++)!='"')
                    {count++;}

                    end=i-1;
                    i-=(count-6);
                    images.add(s.substring(start,end));
                   // Log.d("Check:",a);
                    s.replace(start,end,n+count_img+".jpg");
                    break;
                }
                else
                    i++;
            }
        }
//        Toast.makeText(this, images+"", Toast.LENGTH_LONG).show();
        return s.toString();
    }
}

