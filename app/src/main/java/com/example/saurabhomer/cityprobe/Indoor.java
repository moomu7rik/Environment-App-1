package com.example.saurabhomer.cityprobe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

public class Indoor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);
    }

    public void name_of_institute(View view) {
        PopupMenu pm=new PopupMenu(this,findViewById(R.id.name));
        pm.inflate(R.menu.nameofinstitute);
        pm.show();
    }
}
