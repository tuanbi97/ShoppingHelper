package com.tuanbi97.miniproject1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tuanbi97.miniproject1.CamActivity.CameraActivity;
import com.tuanbi97.miniproject1.MainMenu.MainMenu;
import com.tuanbi97.miniproject1.MainMenu.OnListener;
import com.tuanbi97.miniproject1.MapActivity.MapsActivity;
import com.tuanbi97.miniproject1.ShareActivity.ScrollingActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainWrapper;
    MainMenu mainMenu;
    OnListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainActivity();
        Data.createData();
    }

    private void initMainActivity() {
        setContentView(R.layout.activity_main);
        mainMenu = new MainMenu(this);
        mainWrapper = (LinearLayout) findViewById(R.id.mainwrapper);
        mainWrapper.addView(mainMenu);

        listener = new OnListener() {
            @Override
            public void onMenuClick(int menuId) {
                switch (menuId){
                    case 0:
                        Intent intentcam = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intentcam);
                        break;
                    case 1:
                        Intent intentmap = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intentmap);
                        break;
                    case 2:
                        Intent intentshare = new Intent(MainActivity.this, ScrollingActivity.class);
                        startActivity(intentshare);
                        break;
                }
            }
        };
        mainMenu.setListener(listener);
    }
}
