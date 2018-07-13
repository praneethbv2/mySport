package com.praneeth.studio.mysport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef,teamRef,eventRef,indTeamRef,teamId;
    EditText Email;
    EditText Password,Fullname;
    Button reg;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        reg = findViewById(R.id.register);
        Email = findViewById(R.id.em);
        Password = findViewById(R.id.pass);
        Fullname = findViewById(R.id.fullname);
        toLogin = findViewById(R.id.textView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        firebaseDatabase = FirebaseDatabase.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user =firebaseAuth.getCurrentUser();
                if(user!=null) {
                    final String email = Email.getText().toString();
                    final String fullname = Fullname.getText().toString();


                    Map<String,String> dataToAdd = new HashMap<>();
                    dataToAdd.put("fullname",fullname);
                    dataToAdd.put("email",email);
                    userRef = firebaseDatabase.getReference("Users").child(user.getUid());

                    userRef.setValue(dataToAdd);


                    Intent intent = new Intent(RegisterActivity.this,welcomeScreen.class);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(RegisterActivity.this, "Not signed In", Toast.LENGTH_SHORT).show();

            }
        };











        //To go back to login page
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });

//Registering user
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = Email.getText().toString();
                String password = Password.getText().toString();
                final String fullname = Fullname.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Successfully created.",
                                            Toast.LENGTH_SHORT).show();

                                //pushing user data to database

                                    //userinfo playerdata = new userinfo();
                                    //playerdata.setEmail(email);
                                    //playerdata.setFullname(fullname);
                                    // dataToAdd.put(key,"true");


                                  //  startActivity(new Intent(RegisterActivity.this,welcomeScreen.class));
                                    //finish();
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });



            }
        });

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null) mAuth.removeAuthStateListener(authStateListener);
    }
}
