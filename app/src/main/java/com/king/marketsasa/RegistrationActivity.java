package com.king.marketsasa;

import android.app.ProgressDialog;
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

public class RegistrationActivity extends AppCompatActivity {
    EditText email,password;
    Button register;
    TextView login;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.edtMail);
        password = findViewById(R.id.edtPass);
        register = findViewById(R.id.btnReg);
        login = findViewById(R.id.tvLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering...");

        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    //Register user
                    String user_email = email.getText().toString();
                    String user_password = password.getText().toString().trim();
                    dialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){
                                sendEmailVerification();
                            }else {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validate(){
        boolean result = false;
        String mail = email.getText().toString();
        String pass = password.getText().toString().trim();
        if (mail.isEmpty()||pass.isEmpty()){
            Toast.makeText(this, "Fill in all Inputs", Toast.LENGTH_SHORT).show();
        }else if (pass.length()<6){
            Toast.makeText(this, "Password must be Six or More Characters", Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }
        return  result;
    }

    public void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this, "Registered!. Email verification sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else {
                        Toast.makeText(RegistrationActivity.this, "Registratin Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
