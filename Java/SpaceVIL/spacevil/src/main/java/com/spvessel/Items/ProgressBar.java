package com.spvessel.Items;

import java.awt.*;
import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Flags.*;
import com.spvessel.Flags.SizePolicy;

public class ProgressBar extends VisualItem {
    static int count = 0;
    private TextLine _text_object;
    private Rectangle _rect;
    private int _maxValue = 100;
    private int _minValue = 0;
    private int _currentValue = 0;

    public ProgressBar() {
        setItemName("ProgressBar_" + count);
        count++;

        _text_object = new TextLine();
        _text_object.setItemName(getItemName() + "_text_object");
        _rect = new Rectangle();

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ProgressBar"));
    }

    @Override
    public void initElements() {
        // text
        setText("0%");
        addItems(_rect, _text_object);
    }

    public void setMaxValue(int value) {
        _maxValue = value;
    }

    public int getMaxValue() {
        return _maxValue;
    }

    public void setMinValue(int value) {
        _minValue = value;
    }

    public int getMinValue() {
        return _minValue;
    }

    public void setCurrentValue(int currentValue) {
        _currentValue = currentValue;
        updateProgressBar();
    }

    public int getCurrentValue() {
        return _currentValue;
    }

    private void updateProgressBar() {
        float AllLength = _maxValue - _minValue;
        float DonePercent;
        _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
        _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
        DonePercent = (_currentValue - _minValue) / AllLength;
        String text = Math.round(DonePercent * 100f) + "%";
        _text_object.setItemText(text);
        _rect.setWidth((int) Math.round(getWidth() * DonePercent));
    }

    // text init
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
    }

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    // Layout rules
    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    public void updateLayout() {
        updateProgressBar();
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("progressbar");
        if (inner_style != null) {
            _rect.setStyle(inner_style);
        }
    }
}