#include <jni.h>
#include "WDream.h"
#include "Renderer.h"
#include "Filter.h"

using namespace dream;

extern "C" {

    JNIEXPORT void JNICALL
    Java_com_lq_beauty_base_opengl_WRendererBase_nativeRender(JNIEnv* env, jobject instance) {
        WDream::GetInstance()->MainLoop();
    }
    
    JNIEXPORT void JNICALL
    Java_com_lq_beauty_base_opengl_WRendererBase_nativeInit(JNIEnv* env, jobject instance, jint w, jint h) {
        WDream::GetInstance()->SetSize(w, h);
    }

    JNIEXPORT void JNICALL
    Java_com_lq_beauty_base_opengl_WRendererBase_nativeOnSurfaceChanged(JNIEnv* env, jobject instance, jint w, jint h) {
        WDream::GetInstance()->SetSize(w, h);
    }

    JNIEXPORT void JNICALL
    Java_com_lq_beauty_app_camera_render_CameraRender_setTextureID(JNIEnv *env, jobject instance, jint id) {

        WDream::GetInstance()->GetFilterRenderer()->GetFilterCameraInput()->SetTextureID(id);
    }

    JNIEXPORT void JNICALL
    Java_com_lq_beauty_app_camera_render_CameraRender_setFilterType(JNIEnv *env, jobject instance,
                                                                       jint type) {
        WDream::GetInstance()->GetFilterRenderer()->SetFilterType(type);

    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeOnPause() {
//        if (Director::getInstance()->getOpenGLView()) {
//                Application::getInstance()->applicationDidEnterBackground();
 //               cocos2d::EventCustom backgroundEvent(EVENT_COME_TO_BACKGROUND);
 //               WDream::WDream::GetInstance()->getEventDispatcher()->dispatchEvent(&backgroundEvent);
 //       }
    }

    JNIEXPORT void JNICALL Java_com_lq_beauty_base_opengl_WRendererBase_nativeOnResume() {
//        if (Director::getInstance()->getOpenGLView()) {
//            Application::getInstance()->applicationWillEnterForeground();
//            cocos2d::EventCustom foregroundEvent(EVENT_COME_TO_FOREGROUND);
//            WDream::WDream::GetInstance()->getEventDispatcher()->dispatchEvent(&foregroundEvent);
//        }
    }

}
