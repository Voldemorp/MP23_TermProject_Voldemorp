//package com.example.mp23_termproject_voldemorp;
//
//import android.app.AlertDialog;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class BadgeManager {
//    private void showBadgePopup() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View popupView = getLayoutInflater().inflate(R.layout.dialog_badge_alert, null);
//
//        TextView nameOfNewBadge = popupView.findViewById(R.id.nameOfNewBadge);
//        ImageView badgeImage = popupView.findViewById(R.id.badgeImage);
//        Button closeButton = popupView.findViewById(R.id.closeButton);
//
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        DatabaseReference maxPortNumRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("max_portNum");
//        DatabaseReference userTotalLikeRef = FirebaseDatabase.getInstance().getReference().child("userTotalLike");
//
//
//
//
//        // [서버] 추천수의 변경감지
//        ValueEventListener recommendationListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int newRecommendationCount = dataSnapshot.getValue(int.class);
//
//                // 뱃지4: 추천수가 1로 변경되면
//                if (newRecommendationCount == 1) {
//                    showPopup("소심한 햄즥이", "추천수가 1로 변경되어 뱃지1을 획득하였습니다.");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // 에러 처리 로직을 구현합니다.
//            }
//        };
//
//        // 팝업 표시
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//}
//
//
//
//