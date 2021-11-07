package com.example.covidfacres;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText fullName, E_mail, Pass_W, phoneNum;
    Button BtnSignUp;
    TextView BtnAlready;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    private Spinner spinner;




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView textView;

        fullName = findViewById(R.id.fullName);
        E_mail = findViewById(R.id.eMail);
        Pass_W = findViewById(R.id.passWord);
        phoneNum = findViewById(R.id.pHone);
        BtnSignUp = findViewById(R.id.signupBtn);
        BtnAlready = findViewById(R.id.alreadySignIn);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.spinner);



        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        textView =(TextView)findViewById(R.id.alreadySignIn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent= new Intent(signUp.this, login.class);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = E_mail.getText().toString().trim();
                String password = Pass_W.getText().toString().trim();
                String fullname = fullName.getText().toString().trim();
                String phone = phoneNum.getText().toString().trim();
                String Spinner = spinner.getSelectedItem().toString().trim();


                if (TextUtils.isEmpty(email)){
                    E_mail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Pass_W.setError("Password is Required");
                    return;

                }
                if (password.length() < 8 ){
                    Pass_W.setError("Password must be 8 characters long");
                    return;
                }
                if (phone.length() < 10){
                    phoneNum.setError("11 numbers required");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signUp.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullname", fullname);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("gender", Spinner);
                            user.put("date", "none");
                            user.put("time", "none");
                            user.put("typeOfAppointment", "none");

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),login.class));
                        }
                        else {
                            Toast.makeText(signUp.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gen, R.layout.select_iten);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}