package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    private SignUpDialog signUpDialog;
    Button loginBtn;
    EditText loginEmailEditText;
    EditText loginPasswordEditText;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    // Hide the keyboard when the screen is touched
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    // *-- Auto Login --* || If not logged out, automatically log in after signing up
    @Override
    public void onStart() {
        super.onStart();
        movePage(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase authentication and register buttons
        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.loginButton);
        loginEmailEditText = findViewById(R.id.editTextEmailAddress);
        loginPasswordEditText = findViewById(R.id.editTextPassword);

        // Make the status bar transparent and show the image
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Switch to sign-up screen when the sign-up button is clicked
        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(view -> {
            signUpDialog = new SignUpDialog(this);
            signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
            signUpDialog.show();
        });

        // Click event for login button
        Button loginBtn = (Button) findViewById(R.id.loginButton);

        //                   --- Log in ---
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Apply animation when switching screens (from left to right)
//                overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
                String email = loginEmailEditText.getText().toString().trim();
                String pwd = loginPasswordEditText.getText().toString().trim();

                // Exception handling for incomplete login information
                if(email.isEmpty()||pwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please check your login information",Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If login is successful, switch to the main screen
                                    if (task.isSuccessful()) {
                                        movePage(FirebaseAuth.getInstance().getCurrentUser());
                                    } else { // If login fails, display a toast message
                                        Toast.makeText(LoginActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    // *-- Move to the next page after successful login by passing user information --*
    public void movePage(FirebaseUser user) {
        if (user != null) {
            checkIfAddressExists();

            // Apply animation when switching screens (from left to right)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
        }
    }

    // *-- Check if the logged-in user's address information exists --*
    private void checkIfAddressExists() {
        // [Server] Retrieve the address information of the current logged-in user from the database using the user's Userid
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address1");

        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the address information "address1" (required address) exists
                if (dataSnapshot.exists()) {
                    // If the address is saved, move to the main screen
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // If the address is not saved, move to the location setting screen
                    Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling logic goes here
            }
        });
    }
}
