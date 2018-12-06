package com.sismossv.sismossv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sismossv.sismossv.alertas.MyService;
import com.sismossv.sismossv.fragmentos.FragmentoInfo;
import com.sismossv.sismossv.fragmentos.FragmentoPrincipal;
import com.sismossv.sismossv.fragmentos.FragmentoSismos;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.this.setTitle("Principal");
                    selectedFragment = FragmentoPrincipal.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    MainActivity.this.setTitle("Info");
                    selectedFragment = FragmentoInfo.newInstance();
                    break;
                case R.id.navigation_notifications:
                    MainActivity.this.setTitle("Últimos 20 sismos");
                    selectedFragment = FragmentoSismos.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        this.setTitle("Principal");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, FragmentoPrincipal.newInstance());
        transaction.commit();
        startService(new Intent(this, MyService.class));
    }

}
