package com.example.codingclubandroidappcodeforgame;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE=1;
    private EditText email;
    private EditText password;
    private EditText team;
    private Button login;
    private Button register;
    private EditText name;
    private CircleImageView image;
    private Uri imageUri;
    private StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("images");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        team=findViewById(R.id.register_favorite_team);
        email=(EditText)findViewById(R.id.register_email);
        name=(EditText)findViewById(R.id.register_name);
        password=(EditText)findViewById(R.id.register_possword);
        login=(Button)findViewById(R.id.back_to_login);
        register=(Button)findViewById(R.id.register_button);
        image=(CircleImageView) findViewById(R.id.register_image);
        imageUri=null;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(imageUri!=null){
                    final String username=name.getText().toString();
                    final String favteam=team.getText().toString();
                    final String userEmail=email.getText().toString();
                    String usserPassword=password.getText().toString();
                    if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(usserPassword) && !TextUtils.isEmpty(favteam)){
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(userEmail, usserPassword)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            final FirebaseUser user = mAuth.getCurrentUser();
                                            final StorageReference user_profile=mStorage.child(user+".jpg");
                                            user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            final String image_download=uri.toString();
                                                            //String token_id= FirebaseInstanceId.getInstance().getToken();
                                                            Map<String,Object> data=new HashMap<>();
                                                            data.put("name",username);
                                                            data.put("email",userEmail);
                                                            data.put("image",image_download);
                                                            //data.put("token_id",token_id);
                                                            data.put("favTeam",favteam);
                                                            firestore.collection("Users").document(mAuth.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    sendToMAin();
                                                                    progressBar.setVisibility(View.INVISIBLE);

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            Snackbar.make(v,task.getException().toString(),Snackbar.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please select profile image", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void sendToMAin(){
        Intent intent=new Intent(RegisterActivity.this,allMatchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else{
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            imageUri=data.getData();
            image.setImageURI(imageUri);
        }
    }
}
