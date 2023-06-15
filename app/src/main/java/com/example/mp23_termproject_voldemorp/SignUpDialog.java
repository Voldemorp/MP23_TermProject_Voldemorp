package com.example.mp23_termproject_voldemorp;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpDialog extends Dialog {
    // Regular expression for email format validation
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && email.matches(EMAIL_REGEX);
    }

    // User's email
    private String email;
    // User's nickname
    private String nickname;
    // User's password
    private String password;

    // User's badge status
    private boolean badge1 = true;
    private boolean badge2 = false;
    private boolean badge3 = false;
    private boolean badge4 = false;
    private boolean badge5 = false;
    private boolean badge6 = false;
    private boolean badge7 = false;
    private boolean badge8 = false;
    private boolean badge9 = false;
    private boolean badge10 = false;
    private boolean badge11 = false;
    private boolean badge12 = false;

    private int max_portNum = 0;
    private int userTotalLike = 0;

    // Indicates whether the email check button is clicked
    private boolean emailCheckButtonClicked = false;
    // Indicates whether the nickname check button is clicked
    private boolean nicknameCheckButtonClicked = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;

    public class UserModel {
        public String nickName;
        public int max_portNum;
        public int userTotalLike;
    }

    public class UserModel_badge {
        public String mainBadge = "badge1";
        public boolean badge1 = true;
        public boolean badge2 = false;
        public boolean badge3 = false;
        public boolean badge4 = false;
        public boolean badge5 = false;
        public boolean badge6 = false;
        public boolean badge7 = false;
        public boolean badge8 = false;
        public boolean badge9 = false;
        public boolean badge10 = false;
        public boolean badge11 = false;
        public boolean badge12 = false;
    }

    public class UserModel_restaurant {
        public String restaurantName;
    }

    // Hide the keyboard when the screen is touched
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public SignUpDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_sign_up);

        // Button to go back to the login screen without applying any changes
        Button backToLoginBtn = (Button) findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //-------------Email-------------

        // Text to display the availability result of the entered email
        TextView emailCheckedResult = (TextView) findViewById(R.id.signUpIsAvailableEmail);
        // Button to check email duplication
        Button emailCheckButton = (Button) findViewById(R.id.signUpCheckDuplicationEmailBtn);
        // EditText to input email
        EditText emailEditText = findViewById(R.id.SignUpEmailField);

        // Real-time email format validation
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Temporary storage for the entered email
                String insEmail = emailEditText.getText().toString().trim();
                if (!isValidEmail(insEmail)) {
                    emailCheckedResult.setText("Invalid email format.");
                } else {
                    emailCheckedResult.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed.
            }
        });

        // Check email duplication button click event
        emailCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText to input email
                EditText emailEditText = findViewById(R.id.SignUpEmailField);
                // Temporary storage for the entered email
                String insEmail = emailEditText.getText().toString().trim();

                // Mark as clicked
                emailCheckButtonClicked = true;

                if (!isValidEmail(insEmail)) {
                    emailCheckedResult.setText("Invalid email format.");
                } else {
                    firebaseAuth.fetchSignInMethodsForEmail(insEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                                    if (emailExists) {
                                        emailCheckedResult.setText("Email already exists.");
                                    } else {
                                        emailCheckedResult.setText("Email is available.");
                                        email = insEmail; // Store the email for later use
                                    }
                                } else {
                                    // Error occurred
                                    // TODO: Handle the error
                                }
                            });
                }
            }
        });

        //-------------Nickname-------------

        // Text to display the availability result of the entered nickname
        TextView nicknameCheckedResult = (TextView) findViewById(R.id.signUpIsAvailableName);
        // Button to check nickname duplication
        Button nicknameCheckButton = (Button) findViewById(R.id.signUpCheckNameDuplicationBtn);
        // EditText to input nickname
        EditText nicknameEditText = (EditText) findViewById(R.id.signUpEditTextNameField);

        // Real-time nickname length validation
        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Temporary storage for the entered nickname
                String insNickname = nicknameEditText.getText().toString().trim();

                if (insNickname.length() > 10) {
                    nicknameCheckedResult.setText("Nickname should be within 10 characters.");
                } else {
                    nicknameCheckedResult.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed.
            }
        });

        // Check nickname duplication button click event
        nicknameCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText to input nickname
                EditText nicknameEditText = (EditText) findViewById(R.id.signUpEditTextNameField);
                // Temporary storage for the entered nickname
                String insNickname = nicknameEditText.getText().toString().trim();

                // Mark as clicked
                nicknameCheckButtonClicked = true;

                if (insNickname.length() > 10) {
                    nicknameCheckedResult.setText("Nickname should be within 10 characters.");
                } else {
                    nicknameCheckedResult.setText("Nickname is available.");
                    nickname = insNickname; // Store the nickname for later use
                }
            }
        });

        //-------------Password-----------------

        // Text to display the password availability result
        TextView passwordCheckedResult = (TextView) findViewById(R.id.signUpIsAvailablePw);
        // EditText to input password
        EditText passwordEditText = (EditText) findViewById(R.id.signUpPwField);

        // Real-time password length validation
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Temporary storage for the entered password
                String pw = passwordEditText.getText().toString().trim();

                if (pw.length() < 8 || pw.length() > 12) {
                    passwordCheckedResult.setText("Password should be between 8 and 12 characters.");
                } else {
                    passwordCheckedResult.setText("Password is valid.");
                    password = pw; // Store the password for later use
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed.
            }
        });

        //---------------Password Confirmation---------------
        // EditText to input password again
        EditText passwordConfirmEditText = (EditText) findViewById(R.id.signUpPwConfirmField);
        // Text to display the password confirmation result
        TextView passwordConfirmCheckedResult = (TextView) findViewById(R.id.signUpIsAvailablePwConfirm);

        // Real-time password confirmation validation
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Entered password confirmation
                String pwConfirm = passwordConfirmEditText.getText().toString().trim();
                if (!pwConfirm.equals(password)) {
                    passwordConfirmCheckedResult.setText("Passwords do not match.");
                } else {
                    passwordConfirmCheckedResult.setText("Passwords match.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed.
            }
        });

        //----------Agreements------------
        // Terms of use
        CheckBox useCheckBox = (CheckBox) findViewById(R.id.signUpAgreeUseCheckBox);
        // Location information
        CheckBox locationCheckBox = (CheckBox) findViewById(R.id.signUpAgreeLocationCheckBox);

        //---------Sign Up-----------

        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the email check button is clicked and the email is available
                if (!(emailCheckButtonClicked && emailCheckedResult.getText().toString().equals("Email is available."))) {
                    Toast.makeText(context.getApplicationContext(), "Please check your email.", Toast.LENGTH_SHORT).show();
                }
                // Check if the nickname check button is clicked and the nickname is available
                else if (!(nicknameCheckButtonClicked && nicknameCheckedResult.getText().toString().equals("Nickname is available."))) {
                    Toast.makeText(context.getApplicationContext(), "Please check your nickname.", Toast.LENGTH_SHORT).show();
                }
                // Check if the password is entered correctly
                else if (!(passwordCheckedResult.getText().toString().equals("Password is valid."))) {
                    Toast.makeText(context.getApplicationContext(), "Please check your password.", Toast.LENGTH_SHORT).show();
                }
                // Check if the password confirmation matches the password
                else if (!(passwordConfirmCheckedResult.getText().toString().equals("Passwords match."))) {
                    Toast.makeText(context.getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                // Check if the terms of use are agreed upon
                else if (!(useCheckBox.isChecked())) {
                    Toast.makeText(context.getApplicationContext(), "Please agree to the terms of use.", Toast.LENGTH_SHORT).show();
                }
                // Check if the location information is agreed upon
                else if (!(locationCheckBox.isChecked())) {
                    Toast.makeText(context.getApplicationContext(), "Please agree to provide location information.", Toast.LENGTH_SHORT).show();
                }
                // Sign up success
                else {
                    // Store the entered information in the database (email: email, nickname: nickname, password: password)
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = firebaseAuth.getCurrentUser().getUid();

                                        // Create a user database template
                                        UserModel userModel = new UserModel();
                                        // Create a user badge database template
                                        UserModel_badge userModel_badge = new UserModel_badge();

                                        userModel.nickName = nickname;
                                        userModel.max_portNum = max_portNum;
                                        userModel.userTotalLike = userTotalLike;
                                        userModel_badge.mainBadge = "badge1";
                                        userModel_badge.badge1 = badge1;
                                        userModel_badge.badge2 = badge2;
                                        userModel_badge.badge3 = badge3;
                                        userModel_badge.badge4 = badge4;
                                        userModel_badge.badge5 = badge5;
                                        userModel_badge.badge6 = badge6;
                                        userModel_badge.badge7 = badge7;
                                        userModel_badge.badge8 = badge8;
                                        userModel_badge.badge9 = badge9;
                                        userModel_badge.badge10 = badge10;
                                        userModel_badge.badge11 = badge11;
                                        userModel_badge.badge12 = badge12;

                                        // Save the user's nickname in the "users" path
                                        mDatabase.getReference().child("users").child(userId).setValue(userModel);
                                        mDatabase.getReference().child("users").child(userId).child("badge").setValue(userModel_badge);

                                        Toast.makeText(context.getApplicationContext(), "Registration is completed.", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        // Handle registration failure
                                        Toast.makeText(context.getApplicationContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}
