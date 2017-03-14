//
// Created by wuqingqing on 2017/3/9.
//

#include "Renderer.h"
#include "Filter.h"

namespace dream {

    void Renderer::Render() {

    }

    Filter* FilterRenderer::CreateFilter(int type) {
        Filter *pFilter = new FilterNormal();
        pFilter->Init();

        return pFilter;
    }

    FilterRenderer::FilterRenderer()
    : _pFilter(nullptr)
    , _pFilterCameraInput(nullptr)
    , _width(0)
    , _height(0){

        _pFilterCameraInput = new FilterCameraInput();
        _pFilterCameraInput->Init();
    }

    FilterRenderer::~FilterRenderer() {
        if (nullptr != _pFilter) {
            delete _pFilter;
            _pFilter = nullptr;
        }
    }

    void FilterRenderer::SetFilter(Filter *pFilter) {
        _pFilter = pFilter;
    }

    void FilterRenderer::SetFilterType(int type) {
        SetFilter(CreateFilter(type));
    }

    void FilterRenderer::OnSizeChanged(int width, int height) {
        _pFilterCameraInput->OnSizeChange(width, height);
        if (_pFilter) _pFilter->OnSizeChange(width, height);
    }

    void FilterRenderer::Render() {
        if (nullptr != _pFilter) {
            GLuint frameTexture = _pFilterCameraInput->RenderFrameTexture();
            _pFilter->SetTextureID(frameTexture);
            _pFilter->Render();
        } else {
            _pFilterCameraInput->Render();
        }
    }
}
