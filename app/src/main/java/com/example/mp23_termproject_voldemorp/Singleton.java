package com.example.mp23_termproject_voldemorp;

public class Singleton {
    private static Singleton instance;
    private static boolean isFirstPopupShown;

    // 외부에서 인스턴스 생성을 막음
    private Singleton() {
        // 초기화 코드
        isFirstPopupShown = false;
    }

    // 인스턴스에 접근할 수 있는 메서드를 생성
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // isFirstPopupShown 변수에 대한 getter 및 setter 메서드 추가
    public static boolean isFirstPopupShown() {
        return isFirstPopupShown;
    }

    public static void setFirstPopupShown(boolean value) {
        isFirstPopupShown = value;
    }
}
