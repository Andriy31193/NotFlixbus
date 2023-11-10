package edu.itstep.myapplic05.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.itstep.myapplic05.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent integer = new Intent(MainActivity.this, BookTicketActivity.class);
        startActivity(integer);

    }
}