package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.KeyMods;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PasswordLine extends VisualItem implements InterfaceTextEditable, InterfaceDraggable {
    static int count = 0;

    private ButtonToggle _show_pwd_btn;
    private String _pwd = "";
    private String _hide_sign;
    private TextLine _text_object;
    private Rectangle _cursor;
    private int _cursor_position = 0;
    // private int _saveCurs = 0;
    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    // private int _cursorXMin = 0;
    private int _cursorXMax = Integer.MAX_VALUE;

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    /*
     * private final KeyCode BackspaceCode = KeyCode.BACKSPACE;// 14; private final
     * KeyCode DeleteCode = KeyCode.DELETE;// 339; private final KeyCode
     * LeftArrowCode = KeyCode.LEFT;// 331; private final KeyCode RightArrowCode =
     * KeyCode.RIGHT;// 333; private final KeyCode EndCode = KeyCode.END;// 335;
     * private final KeyCode HomeCode = KeyCode.HOME;// 327; private final KeyCode
     * ACode = KeyCode.A;// 30;
     */

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    public PasswordLine() {
        setItemName("PasswordLine_" + count);

        // _hide_sign = Encoding.UTF32.getString(BitConverter.getBytes(0x23fa)); //big
        // point
        // byte[] input = ByteBuffer.allocate(4).putInt(0x25CF).array();
        // _hide_sign = new String(input, Charset.forName("ISO-8859-1")); //
        // StandardCharsets.UTF_16);
        _hide_sign = "\u25CF"; // Encoding.UTF32.getString(BitConverter.getBytes(0x25CF)); //big point

        _text_object = new TextLine();
        _show_pwd_btn = new ButtonToggle();
        _show_pwd_btn.setItemName(getItemName() + "_marker");
        _cursor = new Rectangle();
        _selectedArea = new Rectangle();
        count++;

        eventKeyPress.add(this::onKeyPress); // (sender, args) -> onKeyPress(sender, args));
        eventTextInput.add(this::onTextInput); // (sender, args) -> onTextInput(sender, args));
        eventMousePressed.add(this::onMousePressed); // (sender, args) -> onMousePressed(sender, args));
        eventMouseDrag.add(this::onDragging); // (sender, args) -> onDragging(sender, args));

        ShiftValCodes = new LinkedList<>(Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.END, KeyCode.HOME));
        InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
                KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.PasswordLine"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.PasswordLine.class));
        _text_object.setTextAlignment(new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER)));
    }

    protected void onMousePressed(InterfaceItem sender, MouseArgs args) {
        // _saveCurs = _cursor_position;
        replaceCursorAccordingCoord(args.position.getX());
        if (_isSelect)
            unselectText();
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        replaceCursorAccordingCoord(args.position.getX());

        if (!_isSelect) {
            _isSelect = true;
            _selectFrom = _cursor_position;
        } else {
            _selectTo = _cursor_position;
            makeSelectedArea(_selectFrom, _selectTo);
        }
    }

    private void replaceCursorAccordingCoord(int realPos) {
        realPos -= getX() + getPadding().left + _text_object.getMargin().left;

        _cursor_position = coordXToPos(realPos);
        replaceCursor();
    }

    private int coordXToPos(int coordX) {
        int pos = 0;

        List<Integer> lineLetPos = _text_object.getLetPosArray();
        if (lineLetPos == null)
            return pos;

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + getLineXShift() <= coordX + 3)
                pos = i + 1;
            else
                break;
        }

        return pos;
    }

    private void showPassword(InterfaceItem sender) {
        setText(_pwd);
        replaceCursor();
        getHandler().setFocusedItem(this);
    }

    protected void onKeyPress(Object sender, KeyArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {

            if (!_isSelect && _justSelected) {
                _selectFrom = -1; // 0;
                _selectTo = -1; // 0;
                _justSelected = false;
            }

            if (args.mods != KeyMods.NO) {
                switch (args.mods) {
                case SHIFT:
                    if (ShiftValCodes.contains(args.key)) {
                        if (!_isSelect) {
                            _isSelect = true;
                            _selectFrom = _cursor_position;
                        }
                    }

                    break;

                case CONTROL:
                    if (args.key == KeyCode.A || args.key == KeyCode.a) {
                        _selectFrom = 0;
                        _cursor_position = getText().length();
                        replaceCursor();

                        _isSelect = true;
                    }
                    break;

                // alt, super ?
                }
            } else {
                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE) {
                    if (_isSelect)
                        cutText();
                    else {
                        if (args.key == KeyCode.BACKSPACE && _cursor_position > 0) // backspace
                        {
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursor_position - 1).toString());
                            _cursor_position--;
                            replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE && _cursor_position < getText().length()) // delete
                        {
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursor_position).toString());
                            replaceCursor();
                        }
                    }
                } else if (_isSelect && !InsteadKeyMods.contains(args.key))
                    unselectText();
            }

            if (args.key == KeyCode.LEFT && _cursor_position > 0) // arrow left
            {
                if (!_justSelected) {
                    _cursor_position--;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.RIGHT && _cursor_position < getText().length()) // arrow right
            {
                if (!_justSelected) {
                    _cursor_position++;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.END) // end
            {
                _cursor_position = getText().length();
                replaceCursor();
            }
            if (args.key == KeyCode.HOME) // home
            {
                _cursor_position = 0;
                replaceCursor();
            }

            if (_isSelect) {
                if (_selectTo != _cursor_position) {
                    _selectTo = _cursor_position;
                    makeSelectedArea(_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private int cursorPosToCoord(int cPos) {
        int coord = 0;
        if (_text_object.getLetPosArray() == null)
            return coord;
        int letCount = _text_object.getLetPosArray().size();

        if (cPos > 0)
            coord = _text_object.getLetPosArray().get(cPos - 1);

        if (getLineXShift() + coord < 0) // _cursorXMin)
            _text_object.setLineXShift(-coord); // _lineXShift + _text_object.getLetWidth(_cursor_position));
        if (getLineXShift() + coord > _cursorXMax)
            _text_object.setLineXShift(_cursorXMax - coord); // _lineXShift - _text_object.getLetWidth(_cursor_position
                                                             // - 1));

        return getLineXShift() + coord;
    }

    private void replaceCursor() {
        int pos = cursorPosToCoord(_cursor_position);
        _cursor.setX(getX() + getPadding().left + pos + _text_object.getMargin().left);
    }

    protected void onTextInput(InterfaceItem sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array(); // BitConverter.getBytes(args.character);
            String str = new String(input, Charset.forName("UTF-32"));// Charset.forName("UTF-32LE"));
                                                                      // //Encoding.UTF32.getString(input);

            if (_isSelect)
                unselectText();
            if (_justSelected)
                cutText();

            StringBuilder sb = new StringBuilder(getText());
            setText(sb.insert(_cursor_position, str).toString());

            _cursor_position++;
            replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes
        // _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    private synchronized void setText(String text) {
        textInputLock.lock();
        try {
            _pwd = text;
            if (_show_pwd_btn.isToggled()) {
                // setText(text);
                _text_object.setItemText(text);
            } else {
                StringBuilder txt = new StringBuilder();
                for (char item : text.toCharArray()) {
                    txt.append(_hide_sign);
                }
                _text_object.setItemText(txt.toString());
                // setText(txt.ToString());
            }

            // _text_object.setItemText(text);
            _text_object.checkXShift(_cursorXMax);
        } finally {
            textInputLock.unlock();
        }
    }

    private String getText() {
        return _pwd;// _text_object.getItemText();
    }

    public String getPassword() {
        return _pwd;
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

    public boolean isEditable() {
        return _isEditable;
    }

    public void set_isEditable(boolean value) {

        if (_isEditable == value)
            return;
        _isEditable = value;

        if (_isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _show_pwd_btn.getWidth() - _text_object.getMargin().left - _text_object.getMargin().right; // _cursorXMin;// ;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.checkXShift(_cursorXMax); // _text_object.setLineXShift();
    }

    public void initElements() {
        // _cursor.IsVisible = false;
        // adding
        _show_pwd_btn.setPassEvents(false);
        _show_pwd_btn.eventToggle.add((sender, args) -> showPassword(sender));
        addItems(_selectedArea, _text_object, _cursor, _show_pwd_btn);
        // getHandler().setFocusedItem(this);

        // _cursorXMin = getPadding().Left;
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _show_pwd_btn.getWidth() - _text_object.getMargin().left - _text_object.getMargin().right; // _cursorXMin;// ;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.setLineXShift();
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public int getTextHeight() {
        return _text_object.getHeight();
    }

    private void makeSelectedArea(int fromPt, int toPt) {
        fromPt = cursorPosToCoord(fromPt);
        toPt = cursorPosToCoord(toPt);

        if (fromPt == toPt) {
            _selectedArea.setWidth(0);
            return;
        }
        int fromReal = Math.min(fromPt, toPt);
        int toReal = Math.max(fromPt, toPt);
        int width = toReal - fromReal + 1;
        _selectedArea.setX(getX() + getPadding().left + fromReal + _text_object.getMargin().left);
        _selectedArea.setWidth(width);
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(0, 0);
    }

    private int nearestPosToCursor(double xPos) {
        List<Integer> endPos = _text_object.getLetPosArray();
        int pos = (int) endPos.stream().map(x -> Math.abs(x - xPos)).sorted().toArray()[0]; 
        return pos;
    }

    void setCursorPosition(double newPos) {
        _cursor_position = nearestPosToCursor(newPos);
    }

    private String cutText() // ������ �� ����������, ������, �����������, ��� �����
    {
        if (!_isEditable)
            return "";
        String str = getSelectedText();
        if (_selectFrom == _selectTo)
            return str;
        int fromReal = Math.min(_selectFrom, _selectTo);
        int toReal = Math.max(_selectFrom, _selectTo);
        StringBuilder sb = new StringBuilder(getText());
        setText(sb.delete(fromReal, toReal).toString()); // - fromReal
        _cursor_position = fromReal;
        replaceCursor();
        if (_isSelect)
            unselectText();
        _justSelected = false;
        return str;
    }

    private String getSelectedText() // ������ �� ����������, ������, �����������, ��� �����
    {
        if (_selectFrom == _selectTo)
            return "";
        String text = getText();
        int fromReal = Math.min(_selectFrom, _selectTo);
        int toReal = Math.max(_selectFrom, _selectTo);
        String selectedText = text.substring(fromReal, toReal); // - fromReal
        return selectedText;
    }

    public void clear() {
        setText("");
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

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("cursor");
        if (inner_style != null) {
            _cursor.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("showmarker");
        if (inner_style != null) {
            _show_pwd_btn.setStyle(inner_style);
        }
    }

    private int getLineXShift() {
        return _text_object.getLineXShift();
    }

}
