package com.example.mp23_termproject_voldemorp;

import android.app.AlertDialog;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;


public class BadgeManager {
    private static BadgeManager instance;
    private Map<String, BadgeData> badgeMap;

    private BadgeManager() {
        // [서버] 유저의 방문수 불러와서 visitNum으로 불러옴
        // [서버] 유저DB 추천수 불러옴
        int userTotalLike = 10;
        int visitCount = 100;

        badgeMap = new HashMap<>();
        // 뱃지1: 추천수가 10 이상일 때
        badgeMap.put("badge1", new BadgeData(userTotalLike >= 10, "뱃지1 획득!", "추천수가 10 이상인 경우에 획득하는 뱃지입니다."));

        // 뱃지2: 방문수가 100 이상일 때
        badgeMap.put("badge2", new BadgeData(visitCount >= 100, "뱃지2 획득!", "방문수가 100 이상인 경우에 획득하는 뱃지입니다."));

        // 뱃지3: 방문수가 10일 때
        badgeMap.put("badge3", new BadgeData(visitCount == 10, "뱃지3 획득!", "방문수가 10인 경우에 획득하는 뱃지입니다."));

        // ...
        // 나머지 뱃지에 대한 획득 조건 및 팝업 내용 설정
    }

    public static synchronized BadgeManager getInstance() {
        if (instance == null) {
            instance = new BadgeManager();
        }
        return instance;
    }

    public BadgeData getBadgeData(String badgeId) {
        return badgeMap.get(badgeId);
    }
}


