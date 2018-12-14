package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Common.DefaultsService;

import java.awt.*;
import java.util.*;
import java.util.List;

abstract class TextItem extends Primitive {
    private List<Float> _alphas;
    private List<Float> _interCoords;
    private float[] _coordinates;
    private float[] _colors;
    private String _itemText = "";

    private Font _font = DefaultsService.getDefaultFont();

    private static int count = 0;
    private int queueCapacity = 512;

    TextItem() {
        setItemName("TextItem_" + count);
        setBackground(new Color(0, 0, 0, 0));
        setWidthPolicy(SizePolicy.EXPAND);
        setHeightPolicy(SizePolicy.EXPAND);
        count++;
        undoQueue = new ArrayDeque<>();
        redoQueue = new ArrayDeque<>();
    }

    TextItem(String text, Font font) {
        this();
        _itemText = text;
        _font = font;
    }

    TextItem(String text, Font font, String name) {
        this(text, font);
        setItemName(name);
    }

    protected void setRealCoords(List<Float> realCoords) {
        _coordinates = toGL(realCoords);
    }

    protected void setAlphas(List<Float> alphas) {
        _alphas = alphas;
        //setColor(alphas);
    }

    String getItemText() {
        return _itemText;
    }

    void setItemText(String itemText) {
        if (!_itemText.equals(itemText)) {
            if (isUndo) {
                if (redoQueue.size() > queueCapacity)
                    redoQueue.pollLast();

                redoQueue.addFirst(_itemText);
                isUndo = false;
            }
            else {
                if (undoQueue.size() > queueCapacity)
                    undoQueue.pollLast();

                undoQueue.addFirst(_itemText);
            }
            _itemText = itemText;
            updateData();
        }
    }

    private boolean isUndo = false;
    private ArrayDeque<String> undoQueue;
    void undo() {
        String tmpText = undoQueue.pollFirst();
        if (tmpText != null) {
            isUndo = true;
            setItemText(tmpText);
        }
    }

    private ArrayDeque<String> redoQueue;
    void redo() {
        String tmpText = redoQueue.pollFirst();
        if (tmpText != null)
            setItemText(tmpText);
    }

    Font getFont() {
        // if(_font == null)
        // _font = DefaultsService.GetDefaultFont();
        return _font;
    }

    void setFont(Font font) {
        // if (!_font.Equals(font))
        if (_font != font) {
            _font = font;
            updateData();
        }
    }

    void setFontSize(int size) {
        if (_font.getSize() != size) {
            _font = new Font(_font.getFamily(), _font.getStyle(), size);
            updateData();
        }
    }

    void setFontStyle(int style) {
        if (_font.getStyle() != style) {
            _font = new Font(_font.getFamily(), style, _font.getSize());
            updateData();
        }
    }

    void setFontFamily(String font_family) {
        if (_font.getFamily() != font_family) {
            _font = new Font(font_family, _font.getStyle(), _font.getSize());
            updateData(); // _criticalFlag = true;
        }
    }

    public abstract void updateData();

    //protected abstract void updateCoords();

    float[] getCoordinates() {
        return _coordinates;
    }

    // public float[] getColors() {
    //     return _colors;
    // }

    private float[] toGL(List<Float> coord) {
        float[] outCoord = new float[coord.size()];
        float f;
        float x0 = getX();
        float y0 = getY();
        float windowH = getHandler().getHeight() / 2f;
        float windowW = getHandler().getWidth() / 2f;

        for (int i = 0; i < coord.size(); i += 3) {
            f = coord.get(i);
            f += x0;
            f = f / windowW - 1.0f;
            outCoord[i] = f;

            f = coord.get(i + 1);
            f += y0;
            f = -(f / windowH - 1.0f);
            outCoord[i + 1] = f;

            f = coord.get(i + 2);
            outCoord[i + 2] = f;
        }

        return outCoord;
    }

    private Color _foreground = Color.BLACK; // default

    public Color getForeground() {
        return _foreground;
    }

    public void setForeground(Color foreground) {
        if (!_foreground.equals(foreground)) {
            _foreground = foreground;
            //setColor(_alphas); // _colorFlag = true;
        }
    }

    public void setForeground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;

        setForeground(new Color(r, g, b, 255));
    }

    public void setForeground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        setForeground(new Color(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public void setForeground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
    }

    // private void setColor(List<Float> alphas) {
    //     if (alphas == null)
    //         return;
    //     _colors = new float[alphas.size() * 4];

    //     Color col = getForeground();
    //     float r = col.getRed() * 1f / 255f;
    //     float g = col.getGreen() * 1f / 255f;
    //     float b = col.getBlue() * 1f / 255f;
    //     // Console.WriteLine(r + " " + g + " " + b);
    //     int inc = 0;
    //     for (float f : alphas) {
    //         float tmp = f;
    //         float one = r;
    //         float two = g;
    //         float three = b;

    //         if (tmp > 1)
    //             tmp = 1;
    //         _colors[inc] = one;
    //         inc++;
    //         _colors[inc] = two;
    //         inc++;
    //         _colors[inc] = three;
    //         inc++;
    //         _colors[inc] = tmp;
    //         inc++;
    //     }
    // }

    private List<ItemAlignment> _textAlignment = new LinkedList<>();

    public List<ItemAlignment> getTextAlignment() {
        return _textAlignment;
    }

    public void setTextAlignment(ItemAlignment... value) {
        setTextAlignment(Arrays.asList(value));
    }

    public void setTextAlignment(List<ItemAlignment> list) {
        if (!_textAlignment.equals(list)) {
            _textAlignment = list;
            //updateCoords(); // _coordsFlag = true;
        }
    }

    public float[] shape() {
        return getCoordinates();
    }
}