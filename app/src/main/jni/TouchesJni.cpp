#include "WDream.h"

#include <android/log.h>
#include <jni.h>



using namespace dream;

extern "C" {
    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeTouchesBegin(JNIEnv * env, jobject thiz, jint id, jfloat x, jfloat y) {
        //intptr_t idlong = id;
        //WDream::WDream::GetInstance()->getOpenGLView()->handleTouchesBegin(1, &idlong, &x, &y);
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeTouchesEnd(JNIEnv * env, jobject thiz, jint id, jfloat x, jfloat y) {
        //intptr_t idlong = id;
        //WDream::WDream::GetInstance()->getOpenGLView()->handleTouchesEnd(1, &idlong, &x, &y);
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeTouchesMove(JNIEnv * env, jobject thiz, jintArray ids, jfloatArray xs, jfloatArray ys) {
        int size = env->GetArrayLength(ids);
        jint id[size];
        jfloat x[size];
        jfloat y[size];

        env->GetIntArrayRegion(ids, 0, size, id);
        env->GetFloatArrayRegion(xs, 0, size, x);
        env->GetFloatArrayRegion(ys, 0, size, y);

        //intptr_t idlong[size];
//        for(int i = 0; i < size; i++)
//            idlong[i] = id[i];

        //WDream::WDream::GetInstance()->getOpenGLView()->handleTouchesMove(size, idlong, x, y);
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeTouchesCancel(JNIEnv * env, jobject thiz, jintArray ids, jfloatArray xs, jfloatArray ys) {
        int size = env->GetArrayLength(ids);
        jint id[size];
        jfloat x[size];
        jfloat y[size];

        env->GetIntArrayRegion(ids, 0, size, id);
        env->GetFloatArrayRegion(xs, 0, size, x);
        env->GetFloatArrayRegion(ys, 0, size, y);

//        intptr_t idlong[size];
//        for(int i = 0; i < size; i++)
//            idlong[i] = id[i];

        //WDream::WDream::GetInstance()->getOpenGLView()->handleTouchesCancel(size, idlong, x, y);
    }

#define KEYCODE_BACK 0x04
#define KEYCODE_MENU 0x52
#define KEYCODE_DPAD_UP 0x13
#define KEYCODE_DPAD_DOWN 0x14
#define KEYCODE_DPAD_LEFT 0x15
#define KEYCODE_DPAD_RIGHT 0x16
#define KEYCODE_ENTER 0x42
#define KEYCODE_PLAY  0x7e
#define KEYCODE_DPAD_CENTER  0x17
    
//
//    static std::unordered_map<int, dream::EventKeyboard::KeyCode> g_keyCodeMap = {
//        { KEYCODE_BACK , dream::EventKeyboard::KeyCode::KEY_ESCAPE},
//        { KEYCODE_MENU , dream::EventKeyboard::KeyCode::KEY_MENU},
//        { KEYCODE_DPAD_UP  , dream::EventKeyboard::KeyCode::KEY_DPAD_UP },
//        { KEYCODE_DPAD_DOWN , dream::EventKeyboard::KeyCode::KEY_DPAD_DOWN },
//        { KEYCODE_DPAD_LEFT , dream::EventKeyboard::KeyCode::KEY_DPAD_LEFT },
//        { KEYCODE_DPAD_RIGHT , dream::EventKeyboard::KeyCode::KEY_DPAD_RIGHT },
//        { KEYCODE_ENTER  , dream::EventKeyboard::KeyCode::KEY_ENTER},
//        { KEYCODE_PLAY  , dream::EventKeyboard::KeyCode::KEY_PLAY},
//        { KEYCODE_DPAD_CENTER  , dream::EventKeyboard::KeyCode::KEY_DPAD_CENTER},
//
//    };
    
    JNIEXPORT jboolean JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeKeyDown(JNIEnv * env, jobject thiz, jint keyCode) {
        //Director* pDirector = Director::getInstance();
        
//        auto iterKeyCode = g_keyCodeMap.find(keyCode);
//        if (iterKeyCode == g_keyCodeMap.end()) {
//            return JNI_FALSE;
//        }
        
        //dream::EventKeyboard::KeyCode cocos2dKey = g_keyCodeMap.at(keyCode);
        //dream::EventKeyboard event(cocos2dKey, false);
        //WDream::WDream::GetInstance()->getEventDispatcher()->dispatchEvent(&event);
        return JNI_TRUE;
        
    }}
