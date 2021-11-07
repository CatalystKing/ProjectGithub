package com.example.covidfacres;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SeeApt extends AppCompatActivity {
    TextView myName, myEmail, myPhone, myDate, myTime, myType;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_apt);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        myName = findViewById(R.id.myName);
        myEmail =findViewById(R.id.myEmail);
        myPhone =findViewById(R.id.myPhone);
        myDate =findViewById(R.id.myDate);
        myTime =findViewById(R.id.myTime);
        myType =findViewById(R.id.myType);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                myName.setText(value.getString("fullname"));
                myEmail.setText(value.getString("email"));
                myPhone.setText(value.getString("phone"));
                myDate.setText(value.getString("date"));
                myTime.setText(value.getString("time"));
                myType.setText(value.getString("typeOfAppointment"));


            }
        });

    }
}