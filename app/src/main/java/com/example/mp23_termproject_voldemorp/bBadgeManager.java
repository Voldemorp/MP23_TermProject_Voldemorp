//package com.example.mp23_termproject_voldemorp;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//
//public class bBadgeManager {
//    private static bBadgeManager instance;
//    private Map<String, BadgeData> badgeMap;
//
//    private bBadgeManager() {
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        // [서버] 유저의 총 방문수 불러옴
//        DatabaseReference maxPortNumRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("max_portNum");
//        // [서버] 유저의 총 추천수 불러옴
//        DatabaseReference userTotalLikeRef = FirebaseDatabase.getInstance().getReference().child("userTotalLike");
//
////        // 테스트 끝나고 지우기
////        int userTotalLike = 10;
////        int visitCount = 100;
//
//        badgeMap = new HashMap<>();
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
//// ValueEventListener를 등록합니다.
//        recommendationRef.addValueEventListener(recommendationListener);
//
//
//
//
//
//        // 뱃지 4: 첫 추천
//        if()
//        badgeMap.put("badge1", new BadgeData(userTotalLike >= 10, "뱃지1 획득!", "추천수가 10 이상인 경우에 획득하는 뱃지입니다."));
//
//        // 뱃지2: 방문수가 100 이상일 때
//        badgeMap.put("badge2", new BadgeData(visitCount >= 100, "뱃지2 획득!", "방문수가 100 이상인 경우에 획득하는 뱃지입니다."));
//
//        // 뱃지3: 방문수가 10일 때
//        badgeMap.put("badge3", new BadgeData(visitCount == 10, "뱃지3 획득!", "방문수가 10인 경우에 획득하는 뱃지입니다."));
//
//        // ...
//        // 나머지 뱃지에 대한 획득 조건 및 팝업 내용 설정
//    }
//
//    public static synchronized bBadgeManager getInstance() {
//        if (instance == null) {
//            instance = new bBadgeManager();
//        }
//        return instance;
//    }
//
//    public BadgeData getBadgeData(String badgeId) {
//        return badgeMap.get(badgeId);
//    }
//}
//
//
