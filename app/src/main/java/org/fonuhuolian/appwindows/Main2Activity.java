package org.fonuhuolian.appwindows;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.fonuhuolian.xappwindows.XNotifactionWindow;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        new XNotifactionWindow(Main2Activity.this, "无理由", true).start();
    }

}
