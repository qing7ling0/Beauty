#include <jni.h>
#include "WDream.h"

using namespace dream;

extern "C" {

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRenderer_nativeRender(JNIEnv* env) {
        WDream::GetInstance()->MainLoop();
    }
    
    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRenderer_nativeInit(JNIEnv* env, jint w, jint h) {
        WDream::GetInstance()->SetSize(w, h);
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRenderer_nativeOnSurfaceChanged(JNIEnv* env, jint w, jint h) {
        WDream::GetInstance()->SetSize(w, h);
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRenderer_nativeOnPause() {
//        if (Director::getInstance()->getOpenGLView()) {
//                Application::getInstance()->applicationDidEnterBackground();
 //               cocos2d::EventCustom backgroundEvent(EVENT_COME_TO_BACKGROUND);
 //               WDream::WDream::GetInstance()->getEventDispatcher()->dispatchEvent(&backgroundEvent);
 //       }
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRenderer_nativeOnResume() {
//        if (Director::getInstance()->getOpenGLView()) {
//            Application::getInstance()->applicationWillEnterForeground();
//            cocos2d::EventCustom foregroundEvent(EVENT_COME_TO_FOREGROUND);
//            WDream::WDream::GetInstance()->getEventDispatcher()->dispatchEvent(&foregroundEvent);
//        }
    }

}
