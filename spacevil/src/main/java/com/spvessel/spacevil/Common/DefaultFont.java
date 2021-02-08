package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.Flags.EmbeddedFont;

import java.awt.Font;
import java.io.InputStream;

final class DefaultFont {
    private static DefaultFont _instance;

    private DefaultFont() {}

    public static DefaultFont getInstance() {
        if (_instance == null) {
            _instance = new DefaultFont();
        }
        return _instance;
    }

    private InputStream privateFontCollection = null;
    private Font _default_font = null;
    private final int defFontSize = 12;

    public void setDefaultFont(Font font) {
        _default_font = font;
    }

    public void setDefaultFont(InputStream font_stream) {
        try {
            privateFontCollection = font_stream;
            Font font = Font.createFont(Font.TRUETYPE_FONT, privateFontCollection);
            _default_font = font.deriveFont(Font.PLAIN, defFontSize);
        } catch (Exception ex) {

        }
    }

    public Font getDefaultFont() {
        if (_default_font == null) {
            addFontFromMemory();
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, privateFontCollection);
                _default_font = font.deriveFont(Font.PLAIN, defFontSize);
            } catch (Exception ex) {

            }
        }
        return _default_font;
    }

    public Font getDefaultFont(int size) {
        if (size == 0) {
            size = defFontSize;
        }
        if (_default_font == null) {
            addFontFromMemory();
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, privateFontCollection);
                _default_font = font.deriveFont(Font.PLAIN, defFontSize);
            } catch (Exception ex) {

            }
        }
        return _default_font.deriveFont(Font.PLAIN, size);
    }

    public Font getDefaultFont(int style, int size) {
        if (size == 0) {
            size = defFontSize;
        }
        if (_default_font == null) {
            addFontFromMemory();
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, privateFontCollection);
                _default_font = font.deriveFont(Font.PLAIN, defFontSize);
            } catch (Exception ex) {

            }
        }
        return _default_font.deriveFont(style, size);
    }

    private void addFontFromMemory() {
        if (_embedded_font == null || _embedded_font == "")
            setDefaultEmbeddedFont(EmbeddedFont.Ubuntu);

        privateFontCollection = DefaultFont.class.getResourceAsStream(_embedded_font);
    }

    private String _embedded_font = null;

    public void setDefaultEmbeddedFont(EmbeddedFont font) {
        switch (font) {
            case Ubuntu:
                _embedded_font = "/fonts/Ubuntu-Regular.ttf";
                break;

            default:
                _embedded_font = "/fonts/Ubuntu-Regular.ttf";
                break;
        }
    }
}
