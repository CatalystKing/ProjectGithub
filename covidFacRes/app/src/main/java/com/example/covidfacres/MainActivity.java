package com.example.covidfacres;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    ViewFlipper flip;
    TextView name;
    String userId;
    Button Cov, Res, Tips, See ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        Cov = findViewById(R.id.Cov);
        Res= findViewById(R.id.Res);
        Tips = findViewById(R.id.Tips);
        See = findViewById(R.id.Dev);



        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("fullname"));


            }
        });

        Cov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cov =new Intent(MainActivity.this, CovAvoid.class);
                startActivity(cov);
            }
        });
        Res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent res =new Intent(MainActivity.this, Reservation.class);
                startActivity(res);
            }
        });
        Tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tip =new Intent(MainActivity.this, HealthTips.class);
                startActivity(tip);
            }
        });
        See.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dev =new Intent(MainActivity.this, SeeApt.class);
                startActivity(dev);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
                finish();

            case R.id.proFile:
                Intent in = new Intent(this,Profile.class);
                startActivity(in);
        }
        return true;

    }
    int counter = 0;
    @Override
    public void onBackPressed() {


        counter++;
        if (counter == 1) {
            Toast.makeText(MainActivity.this, "tap again to logout", Toast.LENGTH_SHORT).show();
        }
        if (counter == 2) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,login.class));
            finish();

        }


    }




}
