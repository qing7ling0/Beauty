//
// Created by wuqingqing on 2017/3/9.
//

#include "GLProgram.h"

namespace dream {

    void GLProgram::Init(const char* pVertexSource, const char* pFragmentSource) {
        _programID = CreateProgram(pVertexSource, pFragmentSource);
    }

    GLuint GLProgram::LoadShader(GLenum shaderType, const char* pSource) {

        //创建一个新shader

        int shader = glCreateShader(shaderType);

        //若创建成功则加载shader

        if (shader != 0) {

            //加载shader的源代码

            glShaderSource(shader, 1, &pSource, NULL);

            //编译shader

            glCompileShader(shader);

            GLint compiled = 0;
            glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
            if (!compiled) {
                GLint infoLen = 0;
                glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
                if (infoLen) {
                    char* buf = (char*) malloc(infoLen);
                    if (buf) {
                        glGetShaderInfoLog(shader, infoLen, NULL, buf);
                        //LOGE("Could not compile shader %d:\n%s\n", shaderType, buf);
                        free(buf);
                    }
                    glDeleteShader(shader);
                    shader = 0;
                }
            }

        }

        return shader;
    }

    GLuint GLProgram::CreateProgram(const char* pVertexSource, const char* pFragmentSource) {
        GLuint vertexShader = LoadShader(GL_VERTEX_SHADER, pVertexSource);
        if (!vertexShader) {
            return 0;
        }
        _vertShader = vertexShader;

        GLuint pixelShader = LoadShader(GL_FRAGMENT_SHADER, pFragmentSource);
        if (!pixelShader) {
            return 0;
        }
        _fragShader = pixelShader;

        GLuint program = glCreateProgram();
        if (program) {
            glAttachShader(program, vertexShader);
            glAttachShader(program, pixelShader);
            glLinkProgram(program);
            GLint linkStatus = GL_FALSE;
            glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
            if (linkStatus != GL_TRUE) {
                GLint bufLength = 0;
                glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
                if (bufLength) {
                    char* buf = (char*) malloc(bufLength);
                    if (buf) {
                        glGetProgramInfoLog(program, bufLength, NULL, buf);
                        free(buf);
                    }
                }
                glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    GLint GLProgram::GetAttribLocation(const std::string &attributeName) const {
        return glGetAttribLocation(_programID, attributeName.c_str());
    }

    GLint GLProgram::GetUniformLocation(const std::string &attributeName) const {
        return glGetUniformLocation(_programID, attributeName.c_str());
    }

    void GLProgram::BindAttribLocation(const std::string &attributeName, GLuint index) const {
        glBindAttribLocation(_programID, index, attributeName.c_str());
    }

    bool GLProgram::Link() {
        GLint status = GL_TRUE;
        glLinkProgram(_programID);

        if (_vertShader) {
            glDeleteShader(_vertShader);
        }

        if (_fragShader) {
            glDeleteShader(_fragShader);
        }

        _vertShader = _fragShader = 0;

        return (status == GL_TRUE);
    }

    void GLProgram::Use() {
        glUseProgram(_programID);
    }
}