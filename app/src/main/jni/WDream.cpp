#include "WDream.h"

#include <GLES2/gl2.h>
#include "Renderer.h"

namespace dream {

    static WDream *s_WDream = nullptr;

    WDream* WDream::GetInstance()
    {
        if (s_WDream == nullptr) {
            s_WDream = new WDream();
            s_WDream->OnInit();
        }

        return s_WDream;
    }

    WDream::WDream()
    {
        _width = 0;
        _height = 0;
        _pFilterRenderer = new FilterRenderer();
        _pRenderer = _pFilterRenderer;
    }

    static void checkGlError(const char* op) {
        for (GLint error = glGetError(); error; error = glGetError()) {
//            LOGI("after %s() glError (0x%x)\n", op, error);
        }
    }
    void WDream::Render(){
        glClearColor(1.f, 1.f, 1.f, 1.0f);
        checkGlError("glClearColor");
        glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        checkGlError("glClear");
        glViewport(0,0,_width, _height);

        _pRenderer->Render();
    }

    void WDream::OnInit() {

    }

    void WDream::OnResume() {
    }

    void WDream::OnPause() {
    }

    void WDream::OnStop() {
    }

    void WDream::OnDestory() {
    }

    void WDream::End() {
    }

    void WDream::SetSize(int width, int height) {
        _width = width;
        _height = height;
        _pRenderer->OnSizeChanged(width, height);
    }

    void WDream::MainLoop(){
        Render();
    }
}
