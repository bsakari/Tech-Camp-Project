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

public class LoginActivity extends AppCompatActivity {
    EditText name,password;
    TextView info,reg,forgotPassword;
    Button login;
    int counter = 5;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.edtMail);
        password = findViewById(R.id.edtPass);
        info = findViewById(R.id.tvInfo);
        reg = findViewById(R.id.tvReg);
        login = findViewById(R.id.btnLogin);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
        forgotPassword = findViewById(R.id.tvForgot);
        //Move to reset password activity
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetpasswordActivity.class));
            }
        });
        info.setText("Number of attempts remaining"+5);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),PostingActivity.class));
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = name.getText().toString();
                String upass = password.getText().toString().trim();
                if (uname.isEmpty()||upass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fill all Inputs", Toast.LENGTH_SHORT).show();
                }else {
                    validate(uname,upass);
                }
            }
        });

    }

    public void validate(String userName,String userPassword){
        dialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                    emailVerification();
                }else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("Attempts remaining "+counter);
                    if (counter==0){
                        login.setEnabled(false);
                    }
                }
            }
        });
    }

    public void emailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag){
            finish();
            startActivity(new Intent(getApplicationContext(),PostingActivity.class));
        }else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
}
