//
// Created by Administrator on 2017/3/10.
//

#ifndef LOOKLOOK_TEXTURE2D_H
#define LOOKLOOK_TEXTURE2D_H


#include <GLES2/gl2.h>

namespace dream {

    const static GLuint NO_TEXTURE = -1;

    const static float TEXTURE_NO_ROTATION[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };
    const static float TEXTURE_ROTATED_90[] = {
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
    };
    const static float TEXTURE_ROTATED_180[] = {
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    };
    const static float TEXTURE_ROTATED_270[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    const static float CUBE[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };

    class Texture2D {
    public:

    };

}
#endif //LOOKLOOK_TEXTURE2D_H
