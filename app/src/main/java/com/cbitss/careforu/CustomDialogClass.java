package com.cbitss.careforu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Harpreet on 1/3/2017.
 */

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    TextView textView;
    public Dialog d;
    int img=0;
    ImageView g1,g2,g3,b1,b2,b3;
    Context con;

    public CustomDialogClass(Activity a, Context c) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        con=c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        g1=(ImageView)findViewById(R.id.g1);
        g2=(ImageView)findViewById(R.id.g2);
        g3=(ImageView)findViewById(R.id.g3);
        b1=(ImageView)findViewById(R.id.b1);
        b2=(ImageView)findViewById(R.id.b2);
        b3=(ImageView)findViewById(R.id.b3);
        textView=(TextView)findViewById(R.id.dia_name) ;
        g1.setOnClickListener(this); g2.setOnClickListener(this); g3.setOnClickListener(this);
        b1.setOnClickListener(this);b2.setOnClickListener(this);b3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g1:
                img=R.drawable.girlone;
                break;
            case R.id.g2:
                img=R.drawable.girlthree;
                break;
            case R.id.g3:
                img=R.drawable.girlfour;
                break;
            case R.id.b1:
                img=R.drawable.boy;
                break;
            case R.id.b2:
                img=R.drawable.boytwo;
                break;
            case R.id.b3:
                img=R.drawable.boythree;
                break;
            default:
                break;
        }

        String s=textView.getText().toString();
        Intent in=new Intent(con,MainActivity.class);
        in.putExtra("id",img);
        in.putExtra("text",s);

        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(in);
        c.finish();

        dismiss();
    }

}