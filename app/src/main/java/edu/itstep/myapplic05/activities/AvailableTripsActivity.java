package edu.itstep.myapplic05.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Objects;

import edu.itstep.myapplic05.R;


public class AvailableTripsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureWindowSettings();
        setContentView(R.layout.activity_available_trips);

        displayTripDetails();
        displayTotalPrice();

        setupBackButton();
    }

    private void configureWindowSettings() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void displayTripDetails() {
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        String destination = intent.getStringExtra("destination");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        int numOfPeople = intent.getIntExtra("numOfPeople", 1);

        TextView textViewTripDetails = findViewById(R.id.textViewTripDetails);
        String tripDetails = "Trip Details:\nSource: " + source + "\nDestination: " + destination +
                "\nDate: " + date  + "\nTime: " + time + "\nNumber of People: " + numOfPeople;
        textViewTripDetails.setText(tripDetails);
    }
    private void displayTotalPrice() {
        TextView textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        double basePrice = 50.0;
        double totalPrice = basePrice + (getNumOfPeople() * 10.0);
        String totalPriceString = "Total Price: $" + totalPrice;
        textViewTotalPrice.setText(totalPriceString);
    }
    private void setupBackButton() {
        AppCompatButton btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(x->onBackBtn());
    }


    private void onBackBtn() {
        Intent intent = new Intent(AvailableTripsActivity.this, BookTicketActivity.class);
        startActivity(intent);
    }

    private int getNumOfPeople() {
        Intent intent = getIntent();
        return intent.getIntExtra("numOfPeople", 1);
    }
}

