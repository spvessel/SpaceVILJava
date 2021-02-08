package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IEffect;
import com.spvessel.spacevil.Core.IImageItem;
import com.spvessel.spacevil.Core.IShadow;
import com.spvessel.spacevil.Core.ITextContainer;
import com.spvessel.spacevil.Core.ITextImage;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.ImageQuality;
import com.spvessel.spacevil.Flags.RedrawFrequency;

final class RenderProcessor {
    private float _intervalVeryLow = 1.0f;
    private float _intervalLow = 1.0f / 10.0f;
    private float _intervalMedium = 1.0f / 30.0f;
    private float _intervalHigh = 1.0f / 60.0f;
    private float _intervalUltra = 1.0f / 120.0f;
    private float _intervalAssigned = 1.0f / 15.0f;

    private RedrawFrequency _frequency = RedrawFrequency.Low;

    private Lock _locker = new ReentrantLock();

    VramVertex screenSquare;

    VramStorage<IImageItem, VramTexture> textureStorage = new VramStorage<>();
    VramStorage<ITextContainer, VramTexture> textStorage = new VramStorage<>();
    VramStorage<IBaseItem, VramVertex> vertexStorage = new VramStorage<>();

    VramEffectsStorage<IBaseItem, IEffect, VramTexture> borderStorage = new VramEffectsStorage<>();
    VramEffectsStorage<IBaseItem, IEffect, VramTexture> shadowStorage = new VramEffectsStorage<>();
    VramEffectsStorage<IBaseItem, IEffect, VramTexture> subtractStorage = new VramEffectsStorage<>();

    RenderProcessor() {
        screenSquare = new VramVertex();
    }

    void setFrequency(RedrawFrequency value) {
        _locker.lock();
        try {
            if (value == RedrawFrequency.VeryLow) {
                _intervalAssigned = _intervalVeryLow;
            } else if (value == RedrawFrequency.Low) {
                _intervalAssigned = _intervalLow;
            } else if (value == RedrawFrequency.Medium) {
                _intervalAssigned = _intervalMedium;
            } else if (value == RedrawFrequency.High) {
                _intervalAssigned = _intervalHigh;
            } else if (value == RedrawFrequency.Ultra) {
                _intervalAssigned = _intervalUltra;
            }
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
        } finally {
            _locker.unlock();
        }
    }

    float getCurrentFrequency() {
        _locker.lock();
        try {
            return _intervalAssigned;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            return _intervalLow;
        } finally {
            _locker.unlock();
        }
    }

    RedrawFrequency getRedrawFrequency() {
        _locker.lock();
        try {
            return _frequency;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            _frequency = RedrawFrequency.Low;
            return _frequency;
        } finally {
            _locker.unlock();
        }
    }

    void drawDirectVertex(Shader shader, List<float[]> vertex, float level, int x, int y, int w, int h, Color color,
            int type) {

        if (vertex == null || vertex.isEmpty())
            return;

        shader.useShader();
        VramVertex store = new VramVertex();
        store.genBuffers(vertex);
        store.sendColor(shader, color);
        store.sendUniform4f(shader, "position", new float[] { x, y, w, h });
        store.sendUniform1f(shader, "level", level);
        store.type = type;
        store.draw();
        store.clear();
    }

    void drawScreenRectangle(Shader shader, float level, int x, int y, int w, int h, Color color, int type) {
        shader.useShader();
        screenSquare.bind();
        screenSquare.sendColor(shader, color);
        screenSquare.sendUniform4f(shader, "position", new float[] { 0, 0, w, h });
        screenSquare.sendUniform1f(shader, "level", level);
        screenSquare.type = type;
        screenSquare.draw();
        screenSquare.unbind();
    }

    void drawFreshVertex(Shader shader, IBaseItem item, float level, int x, int y, int w, int h, Color color,
            int type) {

        vertexStorage.deleteResource(item);
        List<float[]> vertex = item.getTriangles();
        if (vertex == null || vertex.isEmpty())
            return;

        shader.useShader();

        VramVertex store = new VramVertex();
        store.genBuffers(vertex);
        store.sendColor(shader, color);
        store.sendUniform4f(shader, "position", new float[] { x, y, w, h });
        store.sendUniform1f(shader, "level", level);
        store.type = type;
        store.draw();

        vertexStorage.addResource(item, store);
    }

    void drawStoredVertex(Shader shader, IBaseItem item, float level, int x, int y, int w, int h, Color color,
            int type) {

        VramVertex store = vertexStorage.getResource(item);
        if (store == null) {
            ItemsRefreshManager.setRefreshShape(item);
            return;
        }

        shader.useShader();
        store.bind();
        store.sendColor(shader, color);
        store.sendUniform4f(shader, "position", new float[] { x, y, w, h });
        store.sendUniform1f(shader, "level", level);
        store.type = type;
        store.draw();
    }

    void drawFreshVertex(Shader shader, IBaseItem item, float[] vertex, float level, int x, int y, int w, int h,
            Color color, int type) {
        vertexStorage.deleteResource(item);

        if (vertex == null || vertex.length == 0)
            return;

        shader.useShader();

        VramVertex store = new VramVertex();
        store.genBuffers(vertex);
        store.sendColor(shader, color);
        store.sendUniform4f(shader, "position", new float[] { x, y, w, h });
        store.sendUniform1f(shader, "level", level);
        store.type = type;
        store.draw();
        // store.unbind();

        vertexStorage.addResource(item, store);
    }

    void drawFreshText(Shader shader, ITextContainer item, ITextImage printer, Scale scale, float w, float h,
            float level, float[] color) {

        textStorage.deleteResource(item);

        if (printer.isEmpty())
            return;

        shader.useShader();
        VramTexture store = new VramTexture();
        store.genBuffers(0, printer.getWidth() / scale.getXScale(), 0, printer.getHeight() / scale.getYScale(), true);
        store.genTexture(printer.getWidth(), printer.getHeight(), printer.getBytes(), ImageQuality.Smooth);
        textStorage.addResource(item, store);

        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { printer.getXOffset(), printer.getYOffset(), w, h });
        store.sendUniform1f(shader, "level", level);
        store.sendUniform4f(shader, "rgb", color);
        store.draw();
        store.unbind();
    }

    void drawStoredText(Shader shader, ITextContainer item, ITextImage printer, float w, int h, float level,
            float[] color) {

        VramTexture store = textStorage.getResource(item);
        if (store == null) {
            ItemsRefreshManager.setRefreshText(item);
            return;
        }
        shader.useShader();
        store.bindVboIbo();
        store.bind();
        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { printer.getXOffset(), printer.getYOffset(), w, h });
        store.sendUniform1f(shader, "level", level);
        store.sendUniform4f(shader, "rgb", color);
        store.draw();
        store.unbind();
    }

    VramTexture drawDirectShadow(Shader shader, float level, float[] weights, int res, int fboTexture, float x, float y,
            float w, float h, int width, int height) {

        shader.useShader();
        VramTexture store = new VramTexture();
        store.genBuffers(0, width, 0, height);
        store.texture = fboTexture;
        store.bind(fboTexture);
        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { x, y, width, height });
        store.sendUniform1f(shader, "level", level);
        store.sendUniform1fv(shader, "weights", 11, weights);
        store.sendUniform2fv(shader, "frame", new float[] { width, height });
        store.sendUniform1f(shader, "res", res / 10.0f);
        store.sendUniform2fv(shader, "point", new float[] { x, y });
        store.sendUniform2fv(shader, "size", new float[] { w, h });
        store.draw();
        return store;
    }

    void drawRawShadow(Shader shader, float level, int fboTexture, float x, float y, float w, float h, int width,
            int height) {
        VramTexture store = new VramTexture();
        store.genBuffers(0, w, 0, h);
        store.texture = fboTexture;
        store.bind(fboTexture);

        shader.useShader();
        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { x, y, width, height });
        store.sendUniform1f(shader, "level", level);
        store.sendUniform1i(shader, "overlay", 0);
        store.sendUniform1f(shader, "alpha", 0);

        store.draw();
        store.clear();
    }

    void drawFreshShadow(Shader shader, IBaseItem item, IShadow shadow, float level, int fboTexture, float x, float y,
            float w, float h, int width, int height) {

        shadowStorage.deleteResource(item, shadow);

        VramTexture store = new VramTexture();
        store.genBuffers(0, w, 0, h);
        store.texture = fboTexture;
        store.bind(fboTexture);

        shadowStorage.addResource(item, shadow, store);

        shader.useShader();
        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { x, y, width, height });
        store.sendUniform1f(shader, "level", level);
        // store.sendUniform1fv(shader, "weights", 11, weights);
        // store.sendUniform2fv(shader, "frame", new float[] { width, height });
        // store.sendUniform1f(shader, "res", res / 10.0f);
        // store.sendUniform2fv(shader, "point", xy);
        // store.sendUniform2fv(shader, "size", wh);
        // store.sendUniform1i(shader, "applyBlur", 1);
        store.sendUniform1i(shader, "overlay", 0);
        store.sendUniform1f(shader, "alpha", 0);

        store.draw();
        store.unbind();
        // store.clear();
    }

    void drawStoredShadow(Shader shader, IBaseItem item, IShadow shadow, float level, float x, float y, int width,
            int height) {

        VramTexture store = shadowStorage.getResources(item).get(shadow);
        if (store == null) {
            return;
        }

        shader.useShader();
        store.bindVboIbo();
        store.bind(store.texture);
        store.sendUniformSample2D(shader, "tex");
        store.sendUniform4f(shader, "position", new float[] { x, y, width, height });
        store.sendUniform1f(shader, "level", level);
        // store.sendUniform1fv(shader, "weights", 11, weights);
        // store.sendUniform2fv(shader, "frame", new float[] { store.storedWidth,
        // store.storedHeight });
        // store.sendUniform1f(shader, "res", res / 10.0f);
        // store.sendUniform2fv(shader, "point", xy);
        // store.sendUniform2fv(shader, "size", wh);
        // store.sendUniform1i(shader, "applyBlur", 0);
        store.sendUniform1i(shader, "overlay", 0);
        store.sendUniform1f(shader, "alpha", 0);

        store.draw();
        store.unbind();
    }

    void drawTextureAsIs(Shader shader, IImageItem image, float ax, float ay, float aw, float ah, int iw, int ih,
            int width, int height, float level) {

        BufferedImage bmp = image.getImage();
        if (bmp == null)
            return;

        shader.useShader();
        VramTexture store = new VramTexture();
        store.genBuffers(0, aw, 0, ah);
        store.genTexture(iw, ih, bmp, image.getImageQuality());
        store.sendUniformSample2D(shader, "tex");

        if (image.isColorOverlay()) {
            float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                    (float) image.getColorOverlay().getGreen() / 255.0f,
                    (float) image.getColorOverlay().getBlue() / 255.0f,
                    (float) image.getColorOverlay().getAlpha() / 255.0f };
            store.sendUniform1i(shader, "overlay", 1);
            store.sendUniform4f(shader, "rgb", argb);
        } else
            store.sendUniform1i(shader, "overlay", 0);

        store.sendUniform4f(shader, "position", new float[] { ax, ay, width, height });
        store.sendUniform1f(shader, "level", level);
        store.sendUniform1f(shader, "alpha", image.getRotationAngle());
        store.draw();
        store.clear();
    }

    void drawFreshTexture(IImageItem image, Shader shader, float ax, float ay, float aw, float ah, int iw, int ih,
            int width, int height, float level) {

        textureStorage.deleteResource(image);
        BufferedImage bmp = image.getImage();
        if (bmp == null)
            return;
        // byte[] buffer = image.getPixMapImage();
        // if (buffer == null) {
        // return;
        // }

        shader.useShader();
        VramTexture tex = new VramTexture();
        tex.genBuffers(0, aw, 0, ah);
        tex.genTexture(iw, ih, bmp, image.getImageQuality());
        // tex.genTexture(iw, ih, buffer);
        textureStorage.addResource(image, tex);

        ItemsRefreshManager.removeImage(image);

        tex.sendUniformSample2D(shader, "tex");
        if (image.isColorOverlay()) {
            float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                    (float) image.getColorOverlay().getGreen() / 255.0f,
                    (float) image.getColorOverlay().getBlue() / 255.0f,
                    (float) image.getColorOverlay().getAlpha() / 255.0f };
            tex.sendUniform1i(shader, "overlay", 1);
            tex.sendUniform4f(shader, "rgb", argb);
        } else
            tex.sendUniform1i(shader, "overlay", 0);

        tex.sendUniform4f(shader, "position", new float[] { ax, ay, width, height });
        tex.sendUniform1f(shader, "level", level);
        tex.sendUniform1f(shader, "alpha", image.getRotationAngle());
        tex.draw();
        tex.unbind();
    }

    void drawStoredTexture(IImageItem image, Shader shader, float ax, float ay, int width, int height, float level) {
        VramTexture tex = textureStorage.getResource(image);
        if (tex == null) {
            ItemsRefreshManager.setRefreshImage(image);
            return;
        }

        shader.useShader();
        tex.bindVboIbo();
        tex.bind();
        tex.sendUniformSample2D(shader, "tex");
        if (image.isColorOverlay()) {
            float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                    (float) image.getColorOverlay().getGreen() / 255.0f,
                    (float) image.getColorOverlay().getBlue() / 255.0f,
                    (float) image.getColorOverlay().getAlpha() / 255.0f };
            tex.sendUniform1i(shader, "overlay", 1);
            tex.sendUniform4f(shader, "rgb", argb);
        } else
            tex.sendUniform1i(shader, "overlay", 0);

        tex.sendUniform4f(shader, "position", new float[] { ax, ay, width, height });
        tex.sendUniform1f(shader, "level", level);
        tex.sendUniform1f(shader, "alpha", image.getRotationAngle());
        tex.draw();
        tex.unbind();
    }

    static List<float[]> getFullWindowRectangle(int w, int h) {
        List<float[]> vertex = new LinkedList<>();
        // vertex.add(new float[] { -1.0f, 1.0f });
        // vertex.add(new float[] { -1.0f, -1.0f });
        // vertex.add(new float[] { 1.0f, -1.0f });
        // vertex.add(new float[] { 1.0f, -1.0f });
        // vertex.add(new float[] { 1.0f, 1.0f });
        // vertex.add(new float[] { -1.0f, 1.0f });
        vertex.add(new float[] { 0, 0 });
        vertex.add(new float[] { 0, h });
        vertex.add(new float[] { w, h });
        vertex.add(new float[] { 0, 0 });
        vertex.add(new float[] { w, h });
        vertex.add(new float[] { w, 0 });
        return vertex;
    }

    void flushResources() {
        textureStorage.flush();
        textStorage.flush();
        vertexStorage.flush();

        borderStorage.flush();
        shadowStorage.flush();
        subtractStorage.flush();
    }

    void clearResources() {
        vertexStorage.clear();
        textureStorage.clear();
        textStorage.clear();

        borderStorage.clear();
        shadowStorage.clear();
        subtractStorage.clear();
        //
        screenSquare.clear();
    }

    <T> void freeResource(T resource) {
        if (resource instanceof ITextContainer) {
            ITextContainer text = (ITextContainer) resource;
            ItemsRefreshManager.removeText(text);
            textStorage.addForFlushing(text);
        }
        if (resource instanceof IImageItem) {
            IImageItem image = (IImageItem) resource;
            ItemsRefreshManager.removeImage(image);
            textureStorage.addForFlushing(image);
        }
        if (resource instanceof IBaseItem) {
            IBaseItem item = (IBaseItem) resource;
            ItemsRefreshManager.removeShape(item);

            vertexStorage.addForFlushing(item);

            borderStorage.addForFlushing(item);
            shadowStorage.addForFlushing(item);
            subtractStorage.addForFlushing(item);
        }
    }
}