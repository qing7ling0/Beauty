//
// Created by Administrator on 2017/3/10.
//

#ifndef LOOKLOOK_WDEFINE_H
#define LOOKLOOK_WDEFINE_H

#include <android/log.h>

#define  LOG_TAG    "libgl2jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

namespace dream {
    enum ScaleType {
        CenterInside,
        FixXY,
        CenterCrop
    };
}


#endif //LOOKLOOK_WDEFINE_H
