package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private Button address1;
    private String addressData;
    private Button address2;
    private Drawable selectedDrawable;  // Variable to store the selected drawable image
    CircleImageView editProfileImageView; // ImageView for the profile badge at the top

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Profile badge ImageView at the top
        editProfileImageView = findViewById(R.id.editProfileImageView);

        // Load the selected image from SharedPreferences and set it
        loadSelectedImageFromSharedPreferences();

        // Make the status bar transparent and show the image
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Get the user's UID and nickname from the server
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference nickNameRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid).child("nickName");
        DatabaseReference mainBadgeRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid).child("badge").child("mainBadge");

        // *---- Change User Nickname ----*
        EditText EditNicknameText = (EditText)findViewById(R.id.EditNicknameText); // Nickname input field
        Button setNicknameBtn = (Button) findViewById(R.id.setNicknameBtn); // Nickname change button

        setNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editNickname = EditNicknameText.getText().toString().trim();
                nickNameRef.setValue(editNickname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Nickname successfully changed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to change nickname.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // *---- Change User Profile Main Badge ----*
        Button changeMainBadgeBtn = findViewById(R.id.changeMainBadgeBtn); // Change main badge button

        // ImageViews for each badge
        ImageView badgeForFirstSignUp = findViewById(R.id.badgeForFirstSignUp);
        ImageView badgeForMania = findViewById(R.id.badgeForMania);
        ImageView badgeForMaster = findViewById(R.id.badgeForMaster);
        ImageView badgeForFirstRecommend = findViewById(R.id.badgeForFirstRecommend);
        ImageView badgeForTenRecommend = findViewById(R.id.badgeForTenRecommend);
        ImageView badgeForHundredRecommend = findViewById(R.id.badgeForHundredRecommend);
        ImageView badgeForFirstVisit = findViewById(R.id.badgeForFirstVisit);
        ImageView badgeForTenVisit = findViewById(R.id.badgeForTenVisit);
        ImageView badgeForHundredVisit = findViewById(R.id.badgeForHundredVisit);
        ImageView badgeForFirstPhoto = findViewById(R.id.badgeForFirstPhoto);
        ImageView badgeForTenPhoto = findViewById(R.id.badgeForTenPhoto);
        ImageView badgeForHundredPhoto = findViewById(R.id.badgeForHundredPhoto);

        // Click listeners for each badge ImageView
        badgeForFirstSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Select the badge as the main badge
                selectedDrawable = badgeForFirstSignUp.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
                // Save the main badge name in the database
                mainBadgeRef.setValue("Newbie Hamster");
            }
        });
        badgeForMania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForMania.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
                mainBadgeRef.setValue("Master of This Area");
            }
        });
        // Continue with the other badges...

        // When the change main badge button is clicked
        changeMainBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change the profile badge ImageView to the selected badge
                if (selectedDrawable != null) {
                    editProfileImageView.setImageDrawable(selectedDrawable);
                    saveSelectedImageToSharedPreferences();
                    Toast.makeText(EditProfileActivity.this, "Main badge changed successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please select a badge.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // *---- Confirm Button ----*
        Button setBtn = (Button) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                intent.putExtra("address", addressData);
                System.out.println(addressData);
                startActivity(intent);
            }
        });

        // *---- Cancel Button ----*
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //------------Change Address------------
        address1= findViewById(R.id.button9);
        address2= findViewById(R.id.button10);
        address1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // Move to the address search WebView screen
                Intent intent=new Intent(EditProfileActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","3");
                getLocationResult.launch(intent);
            }
        });
        address2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // Move to the address search WebView screen
                Intent intent=new Intent(EditProfileActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","4");
                getLocationResult.launch(intent);
            }
        });
    }

    // ActivityResultLauncher to receive the result from SetLocationNextActivity
    private final ActivityResultLauncher<Intent> getLocationResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // The result from SetLocationNextActivity is received here (set by setResult)
                if (result.getResultCode() == RESULT_OK) {
                    // If data is not empty
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        String order = result.getData().getStringExtra("order");
                        if (order != null) {
                            if (order.equals("3")) {
                                addressData = data;
                            } else if (order.equals("4")) {
                                // Do something with the address2 data
                            }
                        }
                    }
                }
            }
    );

    // ----------------------Methods for Saving to SharedPreferences-----------------------

    // Method to save the selected badge image to SharedPreferences
    private void saveSelectedImageToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // "selectedImage" is the key to identify the saved image
        // Convert the image to Bitmap using drawableToBitmap() method and then convert the Bitmap to a string to save
        editor.putString("selectedImage", bitmapToString(drawableToBitmap(selectedDrawable)));
        editor.apply();
    }

    // Method to convert a Drawable to a Bitmap
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Method to convert a Bitmap to a string
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // ----------------------Methods for Loading from SharedPreferences-----------------------

    // Method to load the selected image from SharedPreferences and set it
    private void loadSelectedImageFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedImageString = preferences.getString("selectedImage", null);
        if (selectedImageString != null) {
            // Convert the string representation of the image back to a Bitmap and set it as a Drawable
            Bitmap bitmap = stringToBitmap(selectedImageString);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            editProfileImageView.setImageDrawable(drawable);
        }
    }

    // Method to convert a string to a Bitmap
    private Bitmap stringToBitmap(String imageString) {
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

}
