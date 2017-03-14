//
// Created by wuqingqing on 2017/3/9.
//

#ifndef LOOKLOOK_FILTER_H
#define LOOKLOOK_FILTER_H

#include <cstdint>
#include <GLES2/gl2.h>

namespace dream {
    class GLProgram;

    class Filter {
    protected:
        GLuint _textureID;
        int _width;
        int _height;

    public:
        Filter();

        virtual ~Filter();

        virtual void Init() = 0;

        virtual void Render() = 0;

        virtual void OnSizeChange(int width, int height);

        inline void SetTextureID(GLuint textureID){ _textureID = textureID; }

        inline GLuint getTextureID() const { return _textureID; }

    };

    class FilterCameraInput : public Filter {

    protected:
        GLProgram *_pGLProgram;
        GLuint _GLAttribPosition;
        GLuint _GLAttribTextureCoordinate;

        GLuint _frameBuffer;
        GLuint _frameBufferTexture;
        GLuint _frameWidth;
        GLuint _frameHeight;

    public:
        FilterCameraInput();

        virtual ~FilterCameraInput();

        virtual void Init();

        virtual void Render();

        virtual void OnSizeChange(int width, int height);

        GLuint RenderFrameTexture();

        void InitFrameTexture();

        void FreeFrameTexture();
    };



    class FilterNormal : public Filter {

    protected:
        GLProgram *_pGLProgram;
        GLuint _GLAttribPosition;
        GLuint _GLAttribTextureCoordinate;

    public:
        FilterNormal();

        virtual ~FilterNormal();

        virtual void Init();

        virtual void Render();
    };

}
#endif //LOOKLOOK_FILTER_H
