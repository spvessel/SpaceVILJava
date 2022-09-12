package com.spvessel.spacevil;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FontEngine {
    static String _preloadDefFile = "./somefile.dat";

    private static Map<Font, Alphabet> fonts = new HashMap<>();

    private static Lock fontLock = new ReentrantLock();

    static List<Alphabet.ModifyLetter> getModifyLetters(String text, Font font)
    {
        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }

        return fonts.get(font).makeTextNew(text);
    }

    static Alphabet.FontDimensions getFontDims(Font font) {
        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }
        Alphabet a = fonts.get(font);
        return a.fontDims;
    }

    static boolean savePreloadFont(Font font) {
        if (!fonts.containsKey(font)) {
            fontLock.lock();
            try {
                if (!fonts.containsKey(font))
                    fonts.put(font, new Alphabet(font));
            } finally {
                fontLock.unlock();
            }
        }

        fonts.get(font).addMoreLetters(); //Заполнить весь алфавит
        //Сохранить в файл или куда там
        return true;
    }
}