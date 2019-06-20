package com.example.codingclubandroidappcodeforgame;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static Activity La;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser != null){
            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
            //finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(new Intent(LoginActivity.this,allMatchActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                finish();
            }
            else{
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);
        login=(Button)findViewById(R.id.login_button);
        register=(Button)findViewById(R.id.signup_btn);
        //=FirebaseAuth.getInstance();
        La=this;
        progressBar=(ProgressBar)findViewById(R.id.progressBar_login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                }
                else{
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_mail=email.getText().toString();
                String user_pass=password.getText().toString();
                if(!TextUtils.isEmpty(user_mail) && !TextUtils.isEmpty(user_pass)){
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(user_mail,user_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        //String token_id= FirebaseInstanceId.getInstance().getToken();
                                        String current_user=mAuth.getCurrentUser().getUid();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            startActivity(new Intent(LoginActivity.this,allMatchActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                        }
                                        else{
                                            startActivity(new Intent(LoginActivity.this,allMatchActivity.class));
                                        }

                                        /*Map<String,Object> data=new HashMap<>();
                                        //data.put("token_id",token_id);
                                        firestore.collection("Users").document(current_user).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar.setVisibility(View.GONE);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    startActivity(new Intent(LoginActivity.this,MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                                    finish();
                                                }
                                                else{
                                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                    finish();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });*/
                                    }
                                    else{
                                        String error=task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Email and password is required", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
