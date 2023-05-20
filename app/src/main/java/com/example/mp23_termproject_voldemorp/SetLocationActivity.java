package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class SetLocationActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    // 화면 터치시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (view != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    //첫번째 주소 설정하는 빈칸
    private EditText firstLocation;
    //두번째 주소 설정하는 빈칸
    private EditText secondLocation;
    //첫번째 주소 저장 버튼 눌렀는지 여부
    private Boolean firstSaveButtonClicked=false;
    //두번째 주소 저장 버튼 눌렀는지 여부
    private Boolean secondSaveButtonCLicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        //첫번째 주소 설정하는 빈칸
        firstLocation = findViewById(R.id.editTextTextPersonName2);

        firstLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(SetLocationActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","1");
                getLocationResult.launch(intent);
            }
        });

        //두번째 주소 설정하는 빈칸
        secondLocation = findViewById(R.id.editTextTextPersonName3);
        secondLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(SetLocationActivity.this,SetLocationNextActivity.class);
                intent.putExtra("order","2");
                getLocationResult.launch(intent);
            }
        });

        //첫번째 저장하기 버튼
        Button firstSaveButton=(Button) findViewById(R.id.button2);
        firstSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstSaveButtonClicked=true;
                //[서버] 첫번째 주소 db에 저장
            }


        });


        //두번째 저장하기 버튼
        Button secondSaveButton=(Button) findViewById(R.id.button);
        secondSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondSaveButtonCLicked=true;
                //[서버] 두번째 주소 db에 저장
            }
        });

        // 선택완료 버튼 눌렀을 때 이벤트 -> 메인 화면으로 화면 전환
        Button goToMainBtn = (Button) findViewById(R.id.setLocationSetDoneBtn);
        goToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 화면 전환 시 오른쪽에서 왼쪽으로 밀듯이 나타나는 애니메이션 적용
                overridePendingTransition(R.anim.slide_left_enter, R.anim.none);

                //첫번째 주소 입력후 저장하지 않았다면
                if(firstSaveButtonClicked==false&&!firstLocation.getText().toString().equals("첫번째 주소를 설정해주세요"))
                    Toast.makeText(SetLocationActivity.this,"첫번째 주소를 저장해주세요",Toast.LENGTH_SHORT).show();

                //두번째 주소 입력 후 저장하지 않았다면
                if((secondSaveButtonCLicked==false)&&!secondLocation.getText().toString().equals("두번째 주소를 설정해주세요"))
                    Toast.makeText(SetLocationActivity.this,"두번째 주소를 저장해주세요",Toast.LENGTH_SHORT).show();

                //서버 코딩후 주석 풀면 됨
//                //[서버] if db안의 첫번째 주소값이 비어있다면
//                Toast.makeText(SetLocationActivity.this,"첫번째 주소를 입력해주세요",Toast.LENGTH_SHORT).show();
//
//                //[서버] else if db안의 첫번째 주소값과 두번째 주소값이 같다면
//                Toast.makeText(SetLocationActivity.this, "서로 다른 주소를 입력해주세요",Toast.LENGTH_SHORT).show();
                //else
                Toast.makeText(SetLocationActivity.this,"주소 설정이 완료되었습니다",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

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
                                if(order.equals("1"))
                                    firstLocation.setText(data);
                                else if(order.equals("2"))
                                    secondLocation.setText(data);
                            }

                        }
                    }

            }
    );
}