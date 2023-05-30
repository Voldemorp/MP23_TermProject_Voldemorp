package com.example.mp23_termproject_voldemorp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpDialog extends Dialog {
    //이메일 형식 확인
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && email.matches(EMAIL_REGEX);
    }


    //사용자의 이메일
    private String email;
    //사용자의 닉네임
    private String nickname;
    //사용자의 password
    private String password;

    //사용자의 뱃지 상태
    private boolean badge1 = true;
    private boolean badge2 = false;
    private boolean badge3 = false;
    private boolean badge4 = false;
    private boolean badge5 = false;
    private boolean badge6 =false;
    private boolean badge7 = false;
    private boolean badge8 = false;
    private boolean badge9 = false;
    private boolean badge10 = false;
    private boolean badge11 =false;
    private boolean badge12 = false;


    //이메일 중복확인 버튼을 눌렀는지 유무
    private boolean emailCheckButtonClicked = false;
    //닉네임 중복확인 버튼 눌렀는지 유무
    private boolean nicknameCheckButtonClicked=false;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;

    public class UserModel{
        public String nickName;

    }
        public class UserModel_badge{
        public String mainBadge = "badge1";
        public boolean badge1 = true;
        public boolean badge2 = false;
        public boolean badge3 = false;
        public boolean badge4 = false;
        public boolean badge5 = false;
        public boolean badge6 =false;
        public boolean badge7 = false;
        public boolean badge8 = false;
        public boolean badge9 = false;
        public boolean badge10 = false;
        public boolean badge11 =false;
        public boolean badge12 = false;
    }


    public class UserModel_restaurant{
        public String restaurantName;
    }
    //화면 터치시 키보드 내리기
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

        // 변경사항 적용 없이 로그인 창으로 돌아가는 버튼
        Button backToLoginBtn = (Button) findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //파이어베이스 설정
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //-------------이메일-------------

        //입력한 이메일의 사용가능 여부 결과 text
        TextView emailCheckedResult=(TextView) findViewById(R.id.signUpIsAvailableEmail);
        //이메일 중복확인 버튼
        Button emailCheckButton=(Button)findViewById(R.id.signUpCheckDuplicationEmailBtn);
        //이메일 입력하는 공간
        EditText emailEditText = findViewById(R.id.SignUpEmailField);


        //이메일 형식 실시간으로 검사
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 이 메소드는 텍스트 입력 변경 전에 호출됩니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //입력한 이메일을 임시 저장
                String insEmail = emailEditText.getText().toString().trim();
                if (!isValidEmail(insEmail)) {
                    emailCheckedResult.setText("올바른 이메일 형식이 아닙니다.");
                    //setTextColor 써서 고구마색으로 바꾸기
                    emailCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                }
                else{
                    emailCheckedResult.setText("");
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                // 이 메소드는 텍스트 입력 변경 후에 호출됩니다.
            }
        });

        //이메일 중복확인 버튼 눌렀을때 유효성 검사
        emailCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이메일 입력하는 공간
                EditText emailEditText = findViewById(R.id.SignUpEmailField);
                //입력한 이메일을 임시 저장
                String insEmail = emailEditText.getText().toString().trim();

                //클릭됨
                emailCheckButtonClicked=true;

                if (!isValidEmail(insEmail)) {
                    emailCheckedResult.setText("올바른 이메일 형식이 아닙니다");
                    //setTextColor 써서 고구마색으로 바꾸기
                    emailCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                }
                //[서버] '입력한 이메일과 같은 메일이 이미 데이터에 존재한다면'을 조건에 추가. 임의로 예시 넣어둠
                else{
                    firebaseAuth.fetchSignInMethodsForEmail(insEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean emailExists = !task.getResult().getSignInMethods().isEmpty();
                                    if (emailExists) {
                                        emailCheckedResult.setText("이미 존재하는 이메일입니다");
                                        // 고구마색으로 바꾸기
                                        emailCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                                    } else {
                                        emailCheckedResult.setText("사용 가능한 이메일입니다");
                                        // 노란색으로 바꾸기
                                        emailCheckedResult.setTextColor(Color.parseColor("#FFC93D"));
                                        // 데이터에 넣을 이메일
                                        email = insEmail;
                                    }
                                } else {
                                    // 오류 발생
                                    // TODO: 처리할 작업 수행
                                }
                            });
                }

            }
        });

        //-------------닉네임-------------

        //입력한 닉네임의 사용가능 여부 결과 text
        TextView nicknameCheckedResult=(TextView) findViewById(R.id.signUpIsAvailableName);
        //닉네임 중복확인 버튼
        Button nicknameCheckButton=(Button) findViewById(R.id.signUpCheckNameDuplicationBtn);
        //닉네임 입력하는 공간
        EditText nicknameEditText=(EditText)findViewById(R.id.signUpEditTextNameField);

        //닉네임 글자 수 실시간으로 검사
        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 이 메소드는 텍스트 입력 변경 전에 호출됩니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //입력한 닉네임 임시 저장
                String insNickname=nicknameEditText.getText().toString().trim();

                if(insNickname.length()>10){
                    nicknameCheckedResult.setText("10자 이내의 닉네임을 입력해주세요");
                    //고구마색으로 바꾸기
                    nicknameCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                }
                else{
                    nicknameCheckedResult.setText("");
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                // 이 메소드는 텍스트 입력 변경 후에 호출됩니다.
            }
        });

        //닉네임 중복확인 버튼 눌렀을때 유효성 검사
        nicknameCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //닉네임 입력하는 공간
                EditText nicknameEditText=(EditText)findViewById(R.id.signUpEditTextNameField);
                //입력한 닉네임 임시 저장
                String insNickname=nicknameEditText.getText().toString().trim();

                //클릭됨
                nicknameCheckButtonClicked=true;

                if(insNickname.length()>10){
                    nicknameCheckedResult.setText("10자 이내의 닉네임을 입력해주세요");
                    //고구마색으로 바꾸기
                    nicknameCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                }
                //[서버] '입력한 닉네임과 같은 닉네임이 이미 데이터에 존재한다면'을 조건에 추가. 임의로 예시 넣어둠
                else {
                    //Firebase Realtime Database의 "users" 레퍼런스를 가져옵니다.
//                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//
//                    usersRef.orderByChild("nickName").equalTo(insNickname).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                // 닉네임이 이미 존재함
//                                nicknameCheckedResult.setText("이미 존재하는 닉네임입니다");
//                                // 고구마색으로 바꾸기
//                                nicknameCheckedResult.setTextColor(Color.parseColor("#980D4D"));
//                            } else {
                                nicknameCheckedResult.setText("사용 가능한 닉네임입니다");
                                // 노란색으로 바꾸기
                                nicknameCheckedResult.setTextColor(Color.parseColor("#FFC93D"));
//                                // 데이터에 넣을 닉네임
//                                nickname = insNickname;
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            // 에러 처리 코드 추가
//                        }
//                    });
                    nickname = insNickname;
                }
            }
        });

        //-------------비밀번호-----------------

        //입력한 닉네임의 사용가능 여부 결과 text
        TextView passwordCheckedResult=(TextView) findViewById(R.id.signUpIsAvailablePw);

        //비밀번호 입력하는 공간
        EditText passwordEditText=(EditText) findViewById(R.id.signUpPwField);

        //비밀번호 글자수 실시간으로 검사
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 이 메소드는 텍스트 입력 변경 전에 호출됩니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //입력한 비밀번호 임시 저장
                String pw = passwordEditText.getText().toString().trim();

                if (pw.length() < 8 || pw.length() > 12) {
                    passwordCheckedResult.setText("비밀번호는 8자에서 12자 사이여야 합니다");
                    //고구마색으로 바꾸기
                    passwordCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                } else {
                    passwordCheckedResult.setText("사용가능한 비밀번호입니다");
                    //노란색으로 바꾸기
                    passwordCheckedResult.setTextColor(Color.parseColor("#FFC93D"));
                    //데이터에 넣을 password
                    password=pw;
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                // 이 메소드는 텍스트 입력 변경 후에 호출됩니다.

            }
        });


        //---------------비밀번호 확인---------------
        //비밀번호 다시 입력하는 공간
        EditText passwordConfirmEditText=(EditText) findViewById(R.id.signUpPwConfirmField);

        //비밀번호 일치여부 결과 text
        TextView passwordConfirmCheckedResult=(TextView) findViewById(R.id.signUpIsAvailablePwConfirm);

        //비밀번호 일치하는지 실시간으로 검사
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 이 메소드는 텍스트 입력 변경 전에 호출됩니다.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력한 비밀번호 저장
                String pwConfirm = passwordConfirmEditText.getText().toString().trim();
                if (!pwConfirm.equals(password)) {
                    passwordConfirmCheckedResult.setText("비밀번호가 일치하지 않습니다");
                    //고구마색으로 바꾸기
                    passwordConfirmCheckedResult.setTextColor(Color.parseColor("#980D4D"));
                } else {
                    passwordConfirmCheckedResult.setText("비밀번호가 일치합니다");
                    //노란색으로 바꾸기
                    passwordConfirmCheckedResult.setTextColor(Color.parseColor("#FFC93D"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 이 메소드는 텍스트 입력 변경 후에 호출됩니다.
            }
        });

        //----------동의------------
        //이용약관
        CheckBox useCheckBox=(CheckBox) findViewById(R.id.signUpAgreeUseCheckBox);
        //위치정보
        CheckBox locationCheckBox=(CheckBox) findViewById(R.id.signUpAgreeLocationCheckBox);


        //---------가입하기-----------

        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이메일 중복확인 버튼이 클릭되었는지 확인
                if (!(emailCheckButtonClicked=true&&emailCheckedResult.getText().toString().equals("사용 가능한 이메일입니다"))) {
                    Toast.makeText(context.getApplicationContext(), "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                //닉네임 중복확인 버튼이 클릭되었는지 확인
                else if(!(nicknameCheckButtonClicked=true&&nicknameCheckedResult.getText().toString().equals("사용 가능한 닉네임입니다"))){
                    Toast.makeText(context.getApplicationContext(), "닉네임을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                //비밀번호 제대로 입력되었는지 확인
                else if(!(passwordCheckedResult.getText().toString().equals("사용가능한 비밀번호입니다"))){
                    Toast.makeText(context.getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                //비밀번호 일치하는지 확인
                else if(!(passwordConfirmCheckedResult.getText().toString().equals("비밀번호가 일치합니다"))){
                    Toast.makeText(context.getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                //이용약관에 동의하는지 확인
                else if(!(useCheckBox.isChecked())){
                    Toast.makeText(context.getApplicationContext(), "이용약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                }

                //위치정보 제공에 동의하는지 확인
                else if(!(locationCheckBox.isChecked())){
                    Toast.makeText(context.getApplicationContext(), "위치정보 제공에 동의해주세요", Toast.LENGTH_SHORT).show();
                }
                //회원가입 성공
                else {
                    //[서버] 가입하기 버튼 누를시에 입력한 정보들 데이터에 저장(메일:email, 닉네임:nickname, 비밀번호:password)
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = firebaseAuth.getCurrentUser().getUid();

                                        // user 데이터 베이스 틀 생성
                                        UserModel userModel = new UserModel();
                                        //user badge 데이터 베이스룰 생성
                                        UserModel_badge userModel_badge = new UserModel_badge();
                                        //user badge 데이터 베이스를 생성
                                        UserModel_restaurant userModel_restaurant = new UserModel_restaurant();

                                        userModel.nickName = nickname;
                                        userModel_badge.mainBadge="badge1";
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
                                        userModel_restaurant.restaurantName = "";

                                        // 사용자의 닉네임을 "users" 경로에 저장
                                        mDatabase.getReference().child("users").child(userId).setValue(userModel);
                                        mDatabase.getReference().child("users").child(userId).child("badge").setValue(userModel_badge);


                                        Toast.makeText(context.getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                    else {
                                        // 회원가입 실패 시 처리할 내용
                                        Toast.makeText(context.getApplicationContext(), "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}