//
// Created by wuqingqing on 2017/3/9.
//

#ifndef LOOKLOOK_RENDERER_H
#define LOOKLOOK_RENDERER_H

namespace dream {

    class Filter;
    class FilterCameraInput;

    class Renderer {

    public:
        Renderer(){}
        virtual ~Renderer(){}

        virtual void Render();

        virtual void OnSizeChanged(int width, int height) = 0;

    };


    class FilterRenderer : public Renderer {

    protected:
        Filter *_pFilter;
        FilterCameraInput *_pFilterCameraInput;

        int _width;
        int _height;

    public:
        static Filter* CreateFilter(int type);

        FilterRenderer();

        ~FilterRenderer();

        void SetFilter(Filter *pFilter);

        void SetFilterType(int type);

        void OnSizeChanged(int width, int height);

        inline Filter* GetFilter() { return _pFilter; }

        inline FilterCameraInput* GetFilterCameraInput() { return _pFilterCameraInput; }

        virtual void Render();

    };

}
#endif //LOOKLOOK_RENDERER_H
