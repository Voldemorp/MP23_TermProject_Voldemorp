//package com.example.mp23_termproject_voldemorp;
//import android.content.Context;
//import android.app.AlertDialog;
//
//public class RecommendationPopup {
//
//    public static class PopupManager {
//        private static PopupManager instance;
//        private boolean isConditionMet;
//
//        private PopupManager() {
//            // private constructor to enforce singleton pattern
//        }
//
//        public static synchronized PopupManager getInstance() {
//            if (instance == null) {
//                instance = new PopupManager();
//            }
//            return instance;
//        }
//
//        public void setConditionMet(boolean conditionMet) {
//            isConditionMet = conditionMet;
//        }
//
//        public boolean isConditionMet() {
//            return isConditionMet;
//        }
//
//        public void showPopup(Context context) {
//            if (isConditionMet) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                // 팝업 창 설정 및 표시 로직
//                // ...
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        }
//    }
//
//}
