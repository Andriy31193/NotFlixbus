package edu.itstep.myapplic05.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import edu.itstep.myapplic05.R;
import edu.itstep.myapplic05.utils.Utils;

public class BookTicketActivity extends AppCompatActivity {

    private List<String> destinationLocations;
    private AutoCompleteTextView autoCompleteSource, autoCompleteDestination;
    private EditText editTextDate;
    private Spinner spinnerTime;
    private int numOfPeople = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureWindowSettings();
        setContentView(R.layout.activity_book_ticket);

        setupViews();
        setupLocationAdapters();
        setupSwitchLocationsButton();
        setupTimeSpinner();
        setDefaultLocations();

        setupSearchButton();
        setupTextWatchers();
    }

    private void configureWindowSettings() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }
    private void setupViews() {
        autoCompleteSource = findViewById(R.id.autoCompleteSource);
        autoCompleteDestination = findViewById(R.id.autoCompleteDestination);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerTime = findViewById(R.id.spinnerTime);
    }
    private void setupLocationAdapters() {
        List<String> sourceLocations = populateLocationSpinners();
        destinationLocations = populateLocationSpinners();

        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sourceLocations);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, destinationLocations);
        autoCompleteSource.setAdapter(sourceAdapter);
        autoCompleteDestination.setAdapter(destinationAdapter);
    }
    private void setupSwitchLocationsButton() {
        AppCompatButton switchLocations = findViewById(R.id.btnSwitchLocations);
        switchLocations.setOnClickListener(this::switchLocations);
    }
    private void setupTimeSpinner() {
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.time_values,
                R.layout.spinner_dropdown_item
        );

        timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
    }
    private void setDefaultLocations() {
        setDefaultLocation(autoCompleteSource, destinationLocations.get(1));
        setDefaultLocation(autoCompleteDestination, destinationLocations.get(0));
    }
    private void setupSearchButton() {
        AppCompatButton btnSearch = findViewById(R.id.btnSearchBus);
        btnSearch.setOnClickListener(x->showTrips());
    }
    private void setupTextWatchers() {

        autoCompleteSource.addTextChangedListener(createTextWatcher(autoCompleteDestination));
        autoCompleteDestination.addTextChangedListener(createTextWatcher(autoCompleteSource));

        notifyAutoComplete();
    }



    private TextWatcher createTextWatcher(AutoCompleteTextView me) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                updateDestinationLocations(me,editable.toString());
            }
        };
    }
    private List<String> populateLocationSpinners() {
        String json = Utils.loadJSONFromAsset(this, "locations.json");
        Gson gson = new Gson();
        Type locationListType = new TypeToken<List<String>>() {}.getType();
        List<String> locationNames = gson.fromJson(json, locationListType);
        return locationNames != null ? locationNames : new ArrayList<>();
    }

    private void updateDestinationLocations(AutoCompleteTextView me, String editable) {
        if (destinationLocations.contains(editable)) {
            List<String> newLocations = new ArrayList<>(destinationLocations);
            newLocations.remove(editable);

            ArrayAdapter<String> destinationAdapter = (ArrayAdapter<String>) me.getAdapter();
            destinationAdapter.clear();
            destinationAdapter.addAll(newLocations);
            destinationAdapter.notifyDataSetChanged();
        }
    }
    private void setDefaultLocation(AutoCompleteTextView autoCompleteTextView, String defaultLocation) {
        autoCompleteTextView.setText(defaultLocation);
    }
    private void notifyAutoComplete() {
        updateDestinationLocations(autoCompleteDestination, autoCompleteSource.getText().toString());
        updateDestinationLocations(autoCompleteSource, autoCompleteDestination.getText().toString());
    }
    public void switchLocations(View view) {

        String fromText = autoCompleteSource.getText().toString();
        autoCompleteSource.setText(autoCompleteDestination.getText().toString());
        autoCompleteDestination.setText(fromText);

        notifyAutoComplete();
    }

    private void showTrips() {
        Intent intent = new Intent(BookTicketActivity.this, AvailableTripsActivity.class);
        intent.putExtra("source", autoCompleteSource.getText().toString());
        intent.putExtra("destination", autoCompleteDestination.getText().toString());
        intent.putExtra("date", editTextDate.getText().toString());
        intent.putExtra("numOfPeople", numOfPeople);
        intent.putExtra("time", spinnerTime.getSelectedItem().toString());
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) ->
                        editTextDate.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth), year, month, day);

        datePickerDialog.show();
    }

    public void incrementNumOfPeople(View view) {
        numOfPeople++;
        updateNumOfPeopleTextView();
    }
    public void decrementNumOfPeople(View view) {
        if (numOfPeople > 1) {
            numOfPeople--;
            updateNumOfPeopleTextView();
        }
    }
    private void updateNumOfPeopleTextView() {

        TextView textNumOfPeople = findViewById(R.id.textNumOfPeople);
        textNumOfPeople.setText(String.valueOf(numOfPeople));
    }
}
