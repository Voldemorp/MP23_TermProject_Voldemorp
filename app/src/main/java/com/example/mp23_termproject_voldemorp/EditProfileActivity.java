package com.example.mp23_termproject_voldemorp;

import de.hdodenhof.circleimageview.CircleImageView;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;




public class EditProfileActivity extends AppCompatActivity {

    private Button address1;
    private Button address2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // 서버로부터 유저의 uid, 닉네임 받아옴
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference nickNameRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("nickName");


        //  ---- 사용자 닉네임 변경 ----
        EditText EditNicknameText = (EditText)findViewById(R.id.EditNicknameText);
        Button setNicknameBtn = (Button) findViewById(R.id.setNicknameBtn);


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


        //  ---- 사용자 프로필 대표 뱃지 변경 ----
            // MypageActivity에서 profileImageView의 ID를 전달받음

        CircleImageView editProfileImageView = findViewById(R.id.editProfileImageView);

        Button changeMainBadgeBtn = findViewById(R.id.changeMainBadgeBtn);

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

        final Drawable[] selectedDrawable = new Drawable[1]; // 선택한 drawable 이미지를 저장하는 변수

        // 각 뱃지 이미지뷰 클릭 이벤트
        badgeForFirstSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForFirstSignUp.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForMania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForMania.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForMaster.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForFirstRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForFirstRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForTenRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForTenRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForHundredRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForHundredRecommend.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForFirstVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForFirstVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForTenVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForTenVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForHundredVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForHundredVisit.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForFirstPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForTenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForTenPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });
        badgeForHundredPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 뱃지로 변경할 drawable 이미지 선택
                selectedDrawable[0] = badgeForHundredPhoto.getDrawable();
                editProfileImageView.setImageDrawable(selectedDrawable[0]);
            }
        });


        // 대표 뱃지 변경 버튼을 누르면
        changeMainBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상단 ImageView가 선택한 뱃지의 drawable로 변경
                if (selectedDrawable[0] != null) {
                    editProfileImageView.setImageDrawable(selectedDrawable[0]);
                    Toast.makeText(EditProfileActivity.this, "대표 뱃지 변경 완료 쥑", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(EditProfileActivity.this, "뱃지를 선택하시즥", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //  ---- 확인 버튼 ----
        Button setBtn = (Button) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상단 ImageView가 선택한 뱃지의 drawable로 변경
                if (selectedDrawable[0] != null) {
                    Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                    startActivity(intent);
                }
            }
        });

        //  ---- 취소 버튼 ----
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //------------주소 변경------------
        address1= findViewById(R.id.button9);
        address2= findViewById(R.id.button10);
        address1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(EditProfileActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","3");
                getLocationResult.launch(intent);
            }
        });
        address2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(EditProfileActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","4");
                getLocationResult.launch(intent);
            }
        });
    }

    private final ActivityResultLauncher<Intent> getLocationResult=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                //SetLocationNextActivity 로부터의 결과 값이 이곳으로 전달 된다(setResult에 의해)
                if(result.getResultCode()==RESULT_OK){

                    //data 비어있지 않다면
                    if(result.getData()!=null){
                        String data=result.getData().getStringExtra("data");
                        String order=result.getData().getStringExtra("order");
                        if(order!=null){
                            if(order.equals("3"))
                                address1.setText(data);
                            else if(order.equals("4"))
                                address2.setText(data);
                        }

                    }
                }

            }
    );
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





}