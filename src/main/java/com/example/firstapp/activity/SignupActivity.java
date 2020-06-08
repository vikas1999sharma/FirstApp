package com.example.firstapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstapp.R;
import com.example.firstapp.RegisterInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextFullName;
    private EditText editTextEmail, editTextPswd, editUserName, editTextConfirmpswd;
    private Button signUpbtn;
    private TextView loginText;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    ImageView img;
    TextView slogan,text;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editUserName = (EditText) findViewById(R.id.username);
        editTextEmail = (EditText) findViewById(R.id.txtEmail);
        editTextPswd = (EditText) findViewById(R.id.firstPswd);
        editTextConfirmpswd = (EditText) findViewById(R.id.confirmPassword);
        signUpbtn = (Button) findViewById(R.id.btnSign);
        loginText = (TextView) findViewById(R.id.lnkLogin);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("RegisterInformation");
        //loginText.setOnClickListener(new View.OnClickListener() {
        //@Override
        // public void onClick(View v) {
        //Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        //  startActivity(intent);
        //    finish();
        //  }
        //});
        signUpbtn.setOnClickListener(this);
        loginText.setOnClickListener(this);

    }

    private Boolean validateFullName() {
        String val = editTextFullName.getText().toString().trim();
        if (val.isEmpty()) {
            editTextFullName.setError("Field cannot be empty");
            return false;
        } else {
            editTextFullName.setError(null);
            return true;
        }
    }

    private Boolean validateUserName() {
        String val = editUserName.getText().toString().trim();

        if (val.isEmpty()) {
            editTextFullName.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 15) {
            editUserName.setError("Username too long");
            return false;
        } else {
            editTextFullName.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = editTextEmail.getText().toString().trim();
if(val.isEmpty())
{
    editTextEmail.setError("Email Field cannot be empty");
    return false;
}
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            editTextEmail.setError("Please provide valid address");
            return false;
        } else {
            editTextEmail.setError("Invalid Email Address");editTextFullName.setEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = editTextPswd.getText().toString().trim();
        if (val.isEmpty()) {
            editTextPswd.setError("Password field cannot be empty");
            return false;
        } else if(val.length() < 6){
            editTextPswd.setError("Length is atleast 6");
            return false;
        }
        else
        {
            return false;
        }
    }
    private Boolean validateConfirmPassword() {
        String val = editTextConfirmpswd.getText().toString().trim();
        if (!editTextPswd.equals(editTextConfirmpswd)) {
            Toast.makeText(SignupActivity.this,"Password is not matched",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            editTextConfirmpswd.setError(null);
            editTextConfirmpswd.setEnabled(false);
            return true;

        }
    }




    @Override
    public void onClick(View v) {
        if (v == signUpbtn) {
            registerUser();
        }
        if (v == loginText) {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPswd.getText().toString().trim();
        final String confirmPassword = editTextPswd.getText().toString().trim();
        final String fullName = editTextFullName.getText().toString().trim();
        final String UserName = editUserName.getText().toString().trim();

        if(!validateUserName() | !validateFullName()|!validateEmail()|!validateConfirmPassword()|!validatePassword()) {
        return;
        }
        else {
            progressDialog.setTitle("Creating a Account");
            progressDialog.setMessage("Registering User........");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        RegisterInformation registerInformation = new RegisterInformation(
                                fullName,
                                UserName,
                                email,
                                password,
                                confirmPassword
                        );
                        FirebaseDatabase.getInstance().getReference("RegisterInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(registerInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SignupActivity.this, "Successfully Logged", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                progressDialog.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(SignupActivity.this, "Could not Register...please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure want to exit Register form ?");
        builder.setCancelable(true);
        builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
