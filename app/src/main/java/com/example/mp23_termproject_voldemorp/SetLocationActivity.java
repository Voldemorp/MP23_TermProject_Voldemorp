package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetLocationActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    // Firebase authentication object
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // Get the current user
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    // Get the Firebase Database instance
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // Get the DatabaseReference for the "users" path
    DatabaseReference mDatabase = firebaseDatabase.getReference().child("users");

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

    // EditText for the first location input
    private EditText firstLocation;
    // EditText for the second location input
    private EditText secondLocation;
    // Boolean to check if the first save button is clicked
    private Boolean firstSaveButtonClicked = false;
    // Boolean to check if the second save button is clicked
    private Boolean secondSaveButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Make the status bar transparent and show the picture
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // EditText for the first location input
        firstLocation = findViewById(R.id.editTextTextPersonName2);
        firstLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the address search web view screen
                Intent intent = new Intent(SetLocationActivity.this, SetLocationNextActivity.class);
                intent.putExtra("order", "1");
                getLocationResult.launch(intent);
            }
        });

        // EditText for the second location input
        secondLocation = findViewById(R.id.editTextTextPersonName3);
        secondLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the address search web view screen
                Intent intent = new Intent(SetLocationActivity.this, SetLocationNextActivity.class);
                intent.putExtra("order", "2");
                getLocationResult.launch(intent);
            }
        });

        // First save button
        Button firstSaveButton = findViewById(R.id.button2);
        firstSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstSaveButtonClicked = true;

                // Check if the user is logged in before saving the address
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    String address1 = firstLocation.getText().toString();

                    // If the first address is not entered
                    if (address1.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter the first address", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save the user's address under the "users" path
                        mDatabase.child(userId).child("address1").setValue(address1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "The first address has been saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to save the first address", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You can save the address after logging in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Second save button
        Button secondSaveButton = findViewById(R.id.button);
        secondSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondSaveButtonClicked = true;

                // Check if the user is logged in before saving the address
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    String address2 = secondLocation.getText().toString();

                    // If the second address is not entered
                    if (address2.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter the second address", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save the user's address under the "users" path
                        mDatabase.child(userId).child("address2").setValue(address2)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "The second address has been saved", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to save the second address", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You can save the address after logging in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to Main Activity button click event for screen transition
        Button goToMainBtn = findViewById(R.id.setLocationSetDoneBtn);
        goToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply animation for screen transition from right to left
                overridePendingTransition(R.anim.slide_left_enter, R.anim.none);

                // If the first address is not saved
                if (!firstSaveButtonClicked) {
                    Toast.makeText(SetLocationActivity.this, "Please save the first address", Toast.LENGTH_SHORT).show();
                }
                // If the second address is not saved while it's entered
                else if (!secondSaveButtonClicked && !secondLocation.getText().toString().isEmpty()) {
                    Toast.makeText(SetLocationActivity.this, "Please save the second address", Toast.LENGTH_SHORT).show();
                }
                // Uncomment the following code after server implementation
                // [Server] If the first address in the database is empty
                // Toast.makeText(SetLocationActivity.this, "Please enter the first address", Toast.LENGTH_SHORT).show();

                // [Server] Else if the first address in the database is the same as the second address
                // Toast.makeText(SetLocationActivity.this, "Please enter different addresses", Toast.LENGTH_SHORT).show();

                else {
                    Toast.makeText(SetLocationActivity.this, "Address setting is complete", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> getLocationResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // The result from SetLocationNextActivity is received here (via setResult)
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        String order = result.getData().getStringExtra("order");
                        if (order != null) {
                            if (order.equals("1")) {
                                firstLocation.setText(data);
                            } else if (order.equals("2")) {
                                secondLocation.setText(data);
                            }
                        }
                    }
                }
            }
    );
}
