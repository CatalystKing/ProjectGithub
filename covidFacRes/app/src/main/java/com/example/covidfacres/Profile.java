package com.example.covidfacres;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView fullName, eMail, pHone, genDer;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        fullName = findViewById(R.id.Nametxt);
        eMail = findViewById(R.id.Emailtxt);
        pHone= findViewById(R.id.Phonetxt);
        genDer= findViewById(R.id.Gendertxt);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                pHone.setText(value.getString("phone"));
                fullName.setText(value.getString("fullname"));
                eMail.setText(value.getString("email"));
                genDer.setText(value.getString("gender"));

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
                Intent intent = new Intent(Profile.this, login.class);
                startActivity(intent);
                finish();
                break;


            case R.id.proFile:
                Intent in = new Intent(this,Profile.class);
                startActivity(in);
                break;
        }
        return true;


    }

}