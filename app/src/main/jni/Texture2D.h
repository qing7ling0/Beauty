//
// Created by Administrator on 2017/3/10.
//

#ifndef LOOKLOOK_TEXTURE2D_H
#define LOOKLOOK_TEXTURE2D_H


#include <GLES2/gl2.h>
#include <string.h>

namespace dream {

    const static GLuint NO_TEXTURE = -1;

    const static unsigned char ROTATION_0     = 1;
    const static unsigned char ROTATION_90    = 2;
    const static unsigned char ROTATION_180   = 3;
    const static unsigned char ROTATION_270   = 4;

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

        static void getRotation(int rotation, bool flipH, bool flipV, float *vertex) {
            const float *_ver = nullptr;
            switch (rotation) {
                case ROTATION_0:
                    _ver = TEXTURE_NO_ROTATION;
                    break;
                case ROTATION_90:
                    _ver = TEXTURE_ROTATED_90;
                    break;
                case ROTATION_180:
                    _ver = TEXTURE_ROTATED_180;
                    break;
                case ROTATION_270:
                    _ver = TEXTURE_ROTATED_270;
                    break;
                default:
                    _ver = TEXTURE_NO_ROTATION;
                    break;
            }
            copyVexture(_ver, vertex, 8);
            if (flipH) {
                vertex[0] = flip(vertex[0]);
                vertex[2] = flip(vertex[2]);
                vertex[4] = flip(vertex[4]);
                vertex[6] = flip(vertex[6]);
            }
            if (flipV) {
                vertex[1] = flip(vertex[1]);
                vertex[3] = flip(vertex[3]);
                vertex[5] = flip(vertex[5]);
                vertex[7] = flip(vertex[7]);
            }
        }

        static float flip(float v) {
            if (v == 0.0f) return  1.0f;
            else return 0.0f;
        }

        static void copyVexture(const float *src, float *dest, int length) {
            for(int i=0; i<length; i++) {
                dest[i] = src[i];
            }
        }

    };

}
#endif //LOOKLOOK_TEXTURE2D_H
