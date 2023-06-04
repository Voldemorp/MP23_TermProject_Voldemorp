package com.example.mp23_termproject_voldemorp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import de.hdodenhof.circleimageview.CircleImageView;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import java.io.ByteArrayOutputStream;


public class EditProfileActivity extends AppCompatActivity {

    private Drawable selectedDrawable;  // 선택한 drawable 이미지를 저장하는 변수
    CircleImageView editProfileImageView; // 상단 프로필 뱃지 ImageView



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 상단 프로필 뱃지 ImageView
        editProfileImageView = findViewById(R.id.editProfileImageView);

        // 이미지를 SharedPreferences에서 가져와서 설정
        loadSelectedImageFromSharedPreferences();

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 서버로부터 유저의 uid, 닉네임 받아옴
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference nickNameRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("nickName");


        //  *---- 사용자 닉네임 변경 ----*
        EditText EditNicknameText = (EditText)findViewById(R.id.EditNicknameText); // 닉네임 입력창
        Button setNicknameBtn = (Button) findViewById(R.id.setNicknameBtn); // 닉네임 변경 버튼


        setNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editNickname = EditNicknameText.getText().toString().trim();
                nickNameRef.setValue(editNickname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "닉네임이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "닉네임이 변경되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        // *---- 사용자 프로필 대표 뱃지 변경 ----*
        Button changeMainBadgeBtn = findViewById(R.id.changeMainBadgeBtn); // 대표 뱃지 변경 버튼

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


        // 각 뱃지 이미지뷰 클릭 이벤트
        badgeForFirstSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 대표 뱃지로 변경할 뱃지 선택
                selectedDrawable = badgeForFirstSignUp.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForMania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForMania.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForMaster.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForFirstRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForFirstRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForTenRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForTenRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForHundredRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForHundredRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForFirstVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForFirstVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForTenVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForTenVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForHundredVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForHundredVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForFirstPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForTenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForTenPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });
        badgeForHundredPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDrawable = badgeForHundredPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable);
            }
        });


        // 대표 뱃지 변경 버튼을 누르면
        changeMainBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 프로필 뱃지 ImageView가 선택한 뱃지로 변경
                if (selectedDrawable != null) {
                    editProfileImageView.setImageDrawable(selectedDrawable);
                    saveSelectedImageToSharedPreferences();
                    Toast.makeText(EditProfileActivity.this, "대표 뱃지 변경 완료 쥑", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(EditProfileActivity.this, "뱃지를 선택하시즥", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //  *---- 확인 버튼 ----*
        Button setBtn = (Button) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                    startActivity(intent);
            }
        });
        //  *---- 취소 버튼 ----*
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });


    }



    // ----------------------SharedPreferences 저장하기 위한 Methods-----------------------

    // 선택한 뱃지를 SharedPreferences에 저장하는 메서드
    private void saveSelectedImageToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // "selectedImage"는 저장할 이미지를 식별하기 위한 키
        // 이미지를 Drawable 형태로 저장하기 위해 drawableToBitmap() 메서드를 사용하고, Bitmap을 문자열로 변환하여 저장
        editor.putString("selectedImage", bitmapToString(drawableToBitmap(selectedDrawable)));
        editor.apply();
    }

    // Drawable을 Bitmap으로 변환하는 메서드
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

    // Bitmap을 문자열로 변환하는 메서드
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // ----------------------SharedPreferences 불러오기 위한 Methods-----------------------

    // SharedPreferences에서 이미지를 가져와서 설정하는 메서드
    private void loadSelectedImageFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedImageString = preferences.getString("selectedImage", null);
        if (selectedImageString != null) {
            // 문자열로 저장된 이미지를 Bitmap으로 변환하여 Drawable로 설정
            Bitmap bitmap = stringToBitmap(selectedImageString);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            editProfileImageView.setImageDrawable(drawable);
        }
    }

    // 문자열을 Bitmap으로 변환하는 메서드
    private Bitmap stringToBitmap(String imageString) {
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

}