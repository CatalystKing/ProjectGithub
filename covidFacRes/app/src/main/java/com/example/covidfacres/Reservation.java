package com.example.covidfacres;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Reservation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editText;
    DatePickerDialog datePicker;
    private Spinner spinner, spinnerr;
    Button Done;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;

    int cnt=0;
    @Override
    public void onBackPressed() {
        cnt++;
        if(cnt==1){
            startActivity(new Intent(this, MainActivity.class));
            finish();

        //super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.spinner);
        spinnerr = findViewById(R.id.spinnercheck);
        Done = findViewById(R.id.confirmRes);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this, R.array.checkup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerr.setAdapter(adapterr);

        spinnerr.setOnItemSelectedListener(this);
        // initialising the calendar
        final Calendar calendar = Calendar.getInstance();

        // initialising the layout
        editText = findViewById(R.id.editTxt);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);

        // initialising the datepickerdialog
        datePicker = new DatePickerDialog(Reservation.this);

        // click on edittext to set the value
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(Reservation.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        editText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                // set maximum date to be selected as today
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());


                // show the dialog
                datePicker.show();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = editText.getText().toString().trim();
                String time = spinner.getSelectedItem().toString().trim();
                String typecheck = spinnerr.getSelectedItem().toString().trim();

                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = mStore.collection("users").document(userID);
                documentReference.update("date", date);
                documentReference.update("time", time);
                documentReference.update("typeOfAppointment", typecheck);


                Toast.makeText(Reservation.this,"Successfully Created an Appointment", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));







            }

        });





    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}