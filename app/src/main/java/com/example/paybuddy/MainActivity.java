package com.example.paybuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class holds our activity. In this app we use fragments so this class will only start the app.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}