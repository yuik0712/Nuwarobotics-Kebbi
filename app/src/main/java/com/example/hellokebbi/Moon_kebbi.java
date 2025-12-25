package com.example.hellokebbi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.facecontrol.UnityFaceManager;

public class Moon_kebbi extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;
    private boolean isNuwaApiReady = false;

    private UnityFaceManager faceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nuwa API 초기화
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);

        // 얼굴 매니저 (Unity)
        faceManager = UnityFaceManager.getInstance();

        // 이벤트 리스너 등록
        mRobotAPI.registerRobotEventListener(robotEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final RobotEventListener robotEventListener = new RobotEventListener() {

        @Override
        public void onWikiServiceStart() {
            Log.d(TAG, "Nuwa SDK Ready");
            isNuwaApiReady = true;

            speak("안녕하세요! 저는 케비입니다.");
            showExpression("happy");
            playMotion("wave");

            // 딜레이 후 달 탐사 시작
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMoonExploration();
                }
            }, 5000); // 5초 딜레이
        }


        @Override
        public void onWikiServiceStop() {
            isNuwaApiReady = false;
        }

        @Override
        public void onTouchEyes(int type, int touches) {
            if (type == 0) {
                showExpression("shy");
                speak("어머, 부끄러워요.");
            } else {
                showExpression("happy");
                speak("간지러워요!");
            }
        }

        @Override
        public void onFaceSpeaker(float direction) {
            if (Math.abs(direction) > 30) {
                showExpression("think");
            }
        }

        // 빈 구현들
        @Override public void onWikiServiceCrash() {}
        @Override public void onWikiServiceRecovery() {}
        @Override public void onStartOfMotionPlay(String s) {}
        @Override public void onPauseOfMotionPlay(String s) {}
        @Override public void onStopOfMotionPlay(String s) {}
        @Override public void onCompleteOfMotionPlay(String s) {}
        @Override public void onPlayBackOfMotionPlay(String s) {}
        @Override public void onErrorOfMotionPlay(int i) {}
        @Override public void onPrepareMotion(boolean b, String s, float v) {}
        @Override public void onCameraOfMotionPlay(String s) {}
        @Override public void onGetCameraPose(float v1, float v2, float v3,
                                              float v4, float v5, float v6,
                                              float v7, float v8, float v9,
                                              float v10, float v11, float v12) {}
        @Override public void onTouchEvent(int i, int i1) {}
        @Override public void onPIREvent(int i) {}
        @Override public void onTap(int i) {}
        @Override public void onLongPress(int i) {}
        @Override public void onWindowSurfaceReady() {}
        @Override public void onWindowSurfaceDestroy() {}
        @Override public void onRawTouch(int i, int i1, int i2) {}
        @Override public void onActionEvent(int i, int i1) {}
        @Override public void onDropSensorEvent(int i) {}
        @Override public void onMotorErrorEvent(int i, int i1) {}
    };

    // 달 탐사 시나리오
    private void startMoonExploration() {
        new Thread(() -> {
            try {
                // 1. 출발 멘트 + 얼굴 표정
                runOnUiThread(() -> {
                    showExpression("happy");
                    speak("그럼 이제 달 탐사를 시작해 볼게요.");
                });
                Thread.sleep(1500);

                // 2. 앞으로 이동
                runOnUiThread(() -> playMotion("forward"));
                mRobotAPI.move(100); // 이동은 블록킹
                Thread.sleep(500);   // 이동 후 약간 딜레이

                // 3. 거의 한 바퀴 회전
                runOnUiThread(() -> showExpression("think"));
                mRobotAPI.turn(359);
                Thread.sleep(500);

                // 4. 180도 회전
                mRobotAPI.turn(180);
                Thread.sleep(500);

                // 5. 완료 멘트 + 얼굴 표정
                runOnUiThread(() -> {
                    showExpression("very_happy");
                    speak("달 탐사가 완료되었습니다!");
                });
                Thread.sleep(1500);

                // 6. 축하 모션 + 얼굴
                runOnUiThread(() -> playMotion("cheer"));
                Thread.sleep(1500);

                // 7. 다시 앞으로 이동
                runOnUiThread(() -> showExpression("happy"));
                mRobotAPI.move(100);
                Thread.sleep(500);

                // 8. 마지막 회전
                mRobotAPI.turn(180);
                Thread.sleep(500);

                // 9. 마무리 설명 + 얼굴 표정
                runOnUiThread(() -> {
                    showExpression("sad");
                    speak("달에는 물과 공기가 없어서 생물이 살기 어려워요.");
                });
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (Exception ignored) {}
    }


    // 얼굴 제어
    private void showExpression(String exp) {
        if (!isNuwaApiReady) return;

        String motion;

        switch (exp) {
            case "happy": motion = "express_happy"; break;
            case "very_happy": motion = "express_very_happy"; break;
            case "shy": motion = "express_shy"; break;
            case "think": motion = "express_think"; break;
            case "sad": motion = "express_sad"; break;
            case "surprised": motion = "express_surprised"; break;
            default: motion = "express_happy";
        }

        Log.d(TAG, "표정 실행: " + motion);
        mRobotAPI.motionPlay(motion, false);
    }

    // 기본 TTS / Motion

    private void speak(String text) {
        if (isNuwaApiReady) mRobotAPI.startTTS(text);
    }

    private void playMotion(String name) {
        if (isNuwaApiReady) mRobotAPI.motionPlay(name, false);
    }
}