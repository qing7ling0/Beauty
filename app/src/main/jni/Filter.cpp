//
// Created by wuqingqing on 2017/3/9.
//

#include <GLES2/gl2ext.h>

#include "WDefine.h"
#include "Filter.h"
#include "GLProgram.h"
#include "Texture2D.h"


namespace dream {

    static const char* NO_FILTER_VERTEX_SHADER = ""
            "attribute vec4 position;\n"
            "attribute vec4 inputTextureCoordinate;\n"
            "varying vec2 textureCoordinate;\n"
            "void main()\n"
            "{\n"
            "    gl_Position = position;\n"
            "    textureCoordinate = inputTextureCoordinate.xy;\n"
            "}";

    static const char* NO_SURFACE_FILTER_FRAGMENT_SHADER = ""
            "#extension GL_OES_EGL_image_external : require\n"
            "precision mediump float;\n"
            "varying vec2 textureCoordinate;\n"
            "uniform samplerExternalOES inputImageTexture;\n"
            "void main()\n"
            "{\n"
            "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n"
            "}";

    static const char* NO_FILTER_FRAGMENT_SHADER = ""
            "precision mediump float;"
            "varying vec2 textureCoordinate;\n"
            "uniform sampler2D inputImageTexture;\n"
            "void main()\n"
            "{\n"
            "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n"
            "}";

    Filter::Filter()
    : _textureID(-1)
    , _width(0)
    , _height(0) {
    }

    Filter::~Filter() {}

    void Filter::OnSizeChange(int width, int height) {
        _width = width;
        _height = height;
    }


    FilterCameraInput::FilterCameraInput()
    : _pGLProgram(nullptr)
    , _frameBuffer(NO_TEXTURE)
    , _frameBufferTexture(NO_TEXTURE)
    , _frameHeight(0)
    , _frameWidth(0)
    , _cameraWidth(0)
    , _cameraHeight(0)
    , _cameraScaleType(ScaleType::CenterCrop){

    }

    FilterCameraInput::~FilterCameraInput() {
        if (nullptr != _pGLProgram) {
            delete(_pGLProgram);
            _pGLProgram = nullptr;
        }
    }

    void FilterCameraInput::Init() {

        Texture2D::getRotation(ROTATION_90, true, false, _vexCrood);

        _pGLProgram = new GLProgram();
        _pGLProgram->Init(NO_FILTER_VERTEX_SHADER, NO_SURFACE_FILTER_FRAGMENT_SHADER);

        _GLAttribPosition = glGetAttribLocation(_pGLProgram->GetID(), "position");
        _GLAttribTextureCoordinate = glGetAttribLocation(_pGLProgram->GetID(), "inputTextureCoordinate");
    }

    void FilterCameraInput::OnSizeChange(int width, int height) {
        Filter::OnSizeChange(width, height);
        if (_frameWidth != width || _frameHeight != height) {
            _frameWidth = width;
            _frameHeight = height;
            InitFrameTexture();
            InitCamera();
        }
    }

    void FilterCameraInput::InitCamera() {
        Texture2D::copyVexture(CUBE, _vexPosition, 8);
        if (_frameHeight==0 || _frameWidth==0 || _cameraHeight==0 || _cameraWidth==0) return;
        if (_cameraScaleType == ScaleType::FixXY) {
            return;
        } else if (_cameraScaleType == ScaleType::CenterInside) {
            float ratioW = _frameWidth * 1.f / _cameraWidth;
            float ratioH = _frameHeight * 1.f / _cameraHeight;
            float ratio = ratioW > ratioH ? ratioH : ratioW;
            ratioW = _cameraWidth * ratio / _frameWidth;
            ratioH = _cameraHeight * ratio / _frameHeight;
            _vexPosition[0] = _vexPosition[4] = _vexPosition[0] * ratioW;
            _vexPosition[2] = _vexPosition[6] = _vexPosition[2] * ratioW;
            _vexPosition[1] = _vexPosition[3] = _vexPosition[1] * ratioH;
            _vexPosition[5] = _vexPosition[7] = _vexPosition[5] * ratioH;

        } else if (_cameraScaleType == ScaleType::CenterCrop) {
            float ratioW = _frameWidth * 1.f / _cameraWidth;
            float ratioH = _frameHeight * 1.f / _cameraHeight;
            float ratio = ratioW > ratioH ? ratioW : ratioH;
            ratioW = _cameraWidth * ratio / _frameWidth;
            ratioH = _cameraHeight * ratio / _frameHeight;
            _vexPosition[0] = _vexPosition[4] = _vexPosition[0] * ratioW;
            _vexPosition[2] = _vexPosition[6] = _vexPosition[2] * ratioW;
            _vexPosition[1] = _vexPosition[3] = _vexPosition[1] * ratioH;
            _vexPosition[5] = _vexPosition[7] = _vexPosition[5] * ratioH;
        }
    }

    void FilterCameraInput::SetCameraSize(int width, int height) {
        if (_cameraWidth != width || _cameraHeight != height) {
            _cameraWidth = width;
            _cameraHeight = height;

            InitCamera();
        }
    }

    void FilterCameraInput::InitFrameTexture() {

        if(_frameBuffer != NO_TEXTURE)
            FreeFrameTexture();

        if (_frameBuffer == NO_TEXTURE) {
            _frameWidth = _width;
            _frameHeight = _height;

            glGenFramebuffers(1, &_frameBuffer);
            glGenTextures(1, &_frameBufferTexture);
            glBindTexture(GL_TEXTURE_2D, _frameBufferTexture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, _frameWidth, _frameHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, nullptr);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glBindFramebuffer(GL_FRAMEBUFFER, _frameBuffer);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, _frameBufferTexture, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }

    GLuint FilterCameraInput::RenderFrameTexture() {

        if(_frameBuffer == NO_TEXTURE) return NO_TEXTURE;

        _pGLProgram->Use();
        glViewport(0, 0, _frameWidth, _frameHeight);

        glBindFramebuffer(GL_FRAMEBUFFER, _frameBuffer);
        glVertexAttribPointer(_GLAttribPosition, 2, GL_FLOAT, false, 0, _vexPosition);
        glEnableVertexAttribArray(_GLAttribPosition);
        glVertexAttribPointer(_GLAttribTextureCoordinate, 2, GL_FLOAT, false, 0, _vexCrood);
        glEnableVertexAttribArray(_GLAttribTextureCoordinate);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, _textureID);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(_GLAttribPosition);
        glDisableVertexAttribArray(_GLAttribTextureCoordinate);
        glBindTexture(GL_TEXTURE_EXTERNAL_OES, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        return _frameBufferTexture;
    }
    
    void FilterCameraInput::FreeFrameTexture() {

        if (_frameBufferTexture != NO_TEXTURE) {
            glDeleteTextures(1, &_frameBufferTexture);
            _frameBufferTexture = NO_TEXTURE;
        }
        if (_frameBuffer != NO_TEXTURE) {
            glDeleteFramebuffers(1, &_frameBuffer);
            _frameBuffer = NO_TEXTURE;
        }
        _frameWidth = -1;
        _frameHeight = -1;
    }

    void FilterCameraInput::Render() {
        _pGLProgram->Use();

        glVertexAttribPointer(_GLAttribPosition, 2, GL_FLOAT, false, 0, CUBE);
        glEnableVertexAttribArray(_GLAttribPosition);
        glVertexAttribPointer(_GLAttribTextureCoordinate, 2, GL_FLOAT, false, 0, TEXTURE_NO_ROTATION);
        glEnableVertexAttribArray(_GLAttribTextureCoordinate);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, _textureID);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(_GLAttribPosition);
        glDisableVertexAttribArray(_GLAttribTextureCoordinate);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    FilterNormal::FilterNormal()
            : _pGLProgram(nullptr) {}

    FilterNormal::~FilterNormal() {
        if (nullptr != _pGLProgram) {
            delete(_pGLProgram);
            _pGLProgram = nullptr;
        }
    }

    void FilterNormal::Init() {
        _pGLProgram = new GLProgram();
        _pGLProgram->Init(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);

        _GLAttribPosition = glGetAttribLocation(_pGLProgram->GetID(), "position");
        _GLAttribTextureCoordinate = glGetAttribLocation(_pGLProgram->GetID(), "inputTextureCoordinate");
    }

    void FilterNormal::Render() {
        _pGLProgram->Use();

//        LOGE("FilterNormal::Render GL_TEXTURE_2D = %d" , _textureID);

        glVertexAttribPointer(_GLAttribPosition, 2, GL_FLOAT, false, 0, CUBE);
        glEnableVertexAttribArray(_GLAttribPosition);
        glVertexAttribPointer(_GLAttribTextureCoordinate, 2, GL_FLOAT, false, 0, TEXTURE_NO_ROTATION);
        glEnableVertexAttribArray(_GLAttribTextureCoordinate);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, _textureID);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(_GLAttribPosition);
        glDisableVertexAttribArray(_GLAttribTextureCoordinate);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}