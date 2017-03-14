//
// Created by wuqingqing on 2017/3/9.
//

#ifndef LOOKLOOK_GLPROGRAM_H
#define LOOKLOOK_GLPROGRAM_H

#include <GLES2/gl2.h>
#include <string>

namespace dream {

    class GLProgram {

    protected:
        GLuint _programID;
        GLuint _vertShader;
        GLuint _fragShader;

    protected:
        GLuint LoadShader(GLenum shaderType, const char* pSource);
        GLuint CreateProgram(const char* pVertexSource, const char* pFragmentSource);

    public:
        void Init(const char* pVertexSource, const char* pFragmentSource);
        void BindAttribLocation(const std::string& attributeName, GLuint index) const;
        GLint GetAttribLocation(const std::string& attributeName) const;
        GLint GetUniformLocation(const std::string& attributeName) const;
        bool Link();
        void Use();

        inline GLuint GetID() { return _programID; }
    };

}
#endif //LOOKLOOK_GLPROGRAM_H
