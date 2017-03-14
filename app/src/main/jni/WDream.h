
#ifndef __WDREAM_H__
#define __WDREAM_H__

#include <stack>
#include <android/log.h>
#include "WDefine.h"

namespace dream{

    class Renderer;
    class FilterRenderer;

    class WDream
    {
    protected:
        Renderer *_pRenderer;
        FilterRenderer *_pFilterRenderer;
        int _width;
        int _height;

    public:
        static WDream* GetInstance();

        void OnInit();

        void OnResume();

        void OnPause();

        void OnStop();

        void OnDestory();

        void MainLoop();

        void Render();

        void End();

        void SetSize(int width, int height);

        inline FilterRenderer* GetFilterRenderer() {return _pFilterRenderer;}

    protected:
        WDream();
    };

}

#endif // __WDREAM_H__
