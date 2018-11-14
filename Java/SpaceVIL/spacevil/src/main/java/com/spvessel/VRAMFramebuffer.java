package com.spvessel;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

final class VRAMFramebuffer {
    public int FBO;
    public int texture;

    protected VRAMFramebuffer() {
    }

    protected void genFBO() {
        FBO = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FBO);
    }
    
    protected void genFBOTexture(int w, int h) {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_BGRA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0);
        // int[] draw_bufs = new int[] { GL_COLOR_ATTACHMENT0_EXT };
        // glDrawBuffers(draw_bufs);
    }

    protected void bindFBO() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FBO);
    }

    protected void unbindFBO() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    protected void clearFBO() {
        glDeleteFramebuffersEXT(FBO);
    }

    protected void clearTexture() {
        glDeleteTextures(texture);
    }
}