package com.spvessel;

import com.spvessel.Core.*;
import com.spvessel.Decorations.*;
import com.spvessel.Flags.GeometryEventType;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.SizePolicy;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.*;
import java.util.HashMap;

public class VisualItem extends BaseItem {

    // common events
    public EventCommonMethodState eventFocusGet = new EventCommonMethodState();
    public EventCommonMethodState eventFocusLost = new EventCommonMethodState();
    public EventCommonMethodState eventResized = new EventCommonMethodState();
    public EventCommonMethodState eventDestroyed = new EventCommonMethodState();
    // mouse input
    public EventMouseMethodState eventMouseHover = new EventMouseMethodState();
    public EventMouseMethodState eventMouseClick = new EventMouseMethodState();
    public EventMouseMethodState eventMousePressed = new EventMouseMethodState();
    public EventMouseMethodState eventMouseRelease = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrag = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrop = new EventMouseMethodState();
    public EventMouseMethodState eventScrollUp = new EventMouseMethodState();
    public EventMouseMethodState eventScrollDown = new EventMouseMethodState();
    // keyboard input
    public EventKeyMethodState eventKeyPress = new EventKeyMethodState();
    public EventKeyMethodState eventKeyRelease = new EventKeyMethodState();
    // text input
    public EventInputTextMethodState eventTextInput = new EventInputTextMethodState();

    public VisualItem() {
        this("VisualItem_");
    }

    public VisualItem(String name) {
        ItemState base = new ItemState();
        base.background = getBackground();
        states.put(ItemStateType.BASE, base);

        // common default prop
        eventManager = new EventManager();
        setItemName(name);
    }

    // overrides
    @Override
    public void setWidth(int width) {
        int value = width - getWidth();
        if (value != 0) {
            super.setWidth(width);

            if (getParent() != null && getWidthPolicy() == SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceHLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceHLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(GeometryEventType.RESIZE_WIDTH, value);
        }
    }

    @Override
    public void setHeight(int height) {
        int value = height - getHeight();
        if (value != 0) {
            super.setHeight(height);

            if (getParent() != null && getHeightPolicy() == SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceVLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceVLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(GeometryEventType.RESIZE_HEIGHT, value);
        }
    }

    public void setPosition(int _x, int _y) {
        this.setX(_x);
        this.setY(_y);
    }

    @Override
    public void setX(int _x) {
        int value = _x - getX();
        if (value != 0) {
            super.setX(_x);
            eventManager.notifyListeners(GeometryEventType.MOVED_X, value);
        }
    }

    @Override
    public void setY(int _y) {
        int value = _y - getY();
        if (value != 0) {
            super.setY(_y);
            eventManager.notifyListeners(GeometryEventType.MOVED_Y, value);
        }
    }

    // item
    private Border _border = new Border();

    public Border getBorder() {
        return _border;
    }

    public void setBorder(Border border) {
        _border = border;
    }

    protected Map<ItemStateType, ItemState> states = new HashMap<ItemStateType, ItemState>();
    protected ItemStateType _state = ItemStateType.BASE;

    protected void setState(ItemStateType state) {
        _state = state;
        updateState();
    }

    private String _tooltip = "";

    public String getToolTip() {
        return _tooltip;
    }

    public void setToolTip(String text) {
        _tooltip = text;
    }

    // container
    private Spacing _spacing = new Spacing();

    public Spacing getSpacing() {
        return _spacing;
    }

    public void setSpacing(Spacing spacing) {
        _spacing = spacing;
    }

    public void setSpacing(int horizontal, int vertical) {
        _spacing = new Spacing(horizontal, vertical);
    }

    private Indents _padding = new Indents();

    public Indents getPadding() {
        return _padding;
    }

    public void setPadding(Indents padding) {
        _padding = padding;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        _padding = new Indents(left, top, right, bottom);
    }

    public EventManager eventManager = null;
    private List<BaseItem> _content = new LinkedList<BaseItem>();

    public List<BaseItem> getItems() {
        return _content;
    }

    protected void setContent(List<BaseItem> content) {
        _content = content;
    }

    public void addItems(BaseItem... items) {
        for (BaseItem item : items) {
            this.addItem(item);
        }
    }

    public void addItem(BaseItem item) {
        getHandler().engineLocker.lock();
        try {
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());
            addChildren(item);
            _content.add(item);
            ItemsLayoutBox.addItem(getHandler(), item, LayoutType.STATIC);
            // needs to force update all attributes
            item.updateGeometry();
            item.initElements();
            if (item instanceof VisualItem) {
                ((VisualItem) item).updateState();
            }
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            getHandler().engineLocker.unlock();
        }
    }

    public void insertItem(BaseItem item, int index) {
        getHandler().engineLocker.lock();
        try {
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());

            addChildren(item);

            if (index > _content.size())
                _content.add(item);
            else
                _content.add(index, item);

            try {
                ItemsLayoutBox.addItem(getHandler(), item, LayoutType.STATIC);
            } catch (Exception ex) {
                System.out.println(item.getItemName());
                throw ex;
            }

            // needs to force update all attributes
            item.updateGeometry();
            item.initElements();

            if (item instanceof VisualItem) {
                ((VisualItem) item).updateState();
            }
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            getHandler().engineLocker.unlock();
        }
    }

    private void cascadeRemoving(BaseItem item, LayoutType type) {
        if (item instanceof VisualItem)// и если это действительно контейнер
        {
            VisualItem container = (VisualItem) item;// предполагаю что элемент контейнер
            List<BaseItem> tmp = container.getItems();
            while (tmp.size() > 0) {
                BaseItem child = container.getItems().get(0);
                // container.cascadeRemoving(child, type);
                // container.getItems().remove(child);
                // child.removeItemFromListeners();
                // ItemsLayoutBox.removeItem(getHandler(), child, type);

                container.removeItem(child);
                tmp.remove(child);
            }
        }
    }

    public void removeItem(BaseItem item) {
        getHandler().engineLocker.lock();
        try {
            LayoutType type;
            if (item instanceof InterfaceFloating) {
                cascadeRemoving(item, LayoutType.FLOATING);
                type = LayoutType.FLOATING;
            } else {
                cascadeRemoving(item, LayoutType.STATIC);
                type = LayoutType.STATIC;
            }

            // removing
            _content.remove(item);

            item.removeItemFromListeners();
            ItemsLayoutBox.removeItem(getHandler(), item, type);
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            getHandler().engineLocker.unlock();
        }
    }

    @Override
    protected void addEventListener(GeometryEventType type, BaseItem listener) {
        eventManager.subscribe(type, listener);
    }

    @Override
    protected void removeEventListener(GeometryEventType type, BaseItem listener) {
        eventManager.unsubscribe(type, listener);
    }

    public void addItemState(ItemStateType type, ItemState state) {
        if (states.containsKey(type)) {
            state.value = true;
            states.replace(type, state);
        } else {
            states.put(type, state);
        }
    }

    public ItemState getState(ItemStateType type) {
        if (states.containsKey(type))
            return states.get(type);
        return null;
    }

    public Map<ItemStateType, ItemState> getAllStates() {
        return states;
    }

    public void removeItemState(ItemStateType type) {
        if (type == ItemStateType.BASE)
            return;
        if (states.containsKey(type))
            states.remove(type);
    }

    public void removeAllItemStates() {
        List<ItemStateType> itemsToRemove = states.entrySet().stream().filter(i -> i.getKey() != ItemStateType.BASE)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        for (ItemStateType item : itemsToRemove)
            states.remove(item);
    }

    @Override
    public void setBackground(Color color) {
        getState(ItemStateType.BASE).background = color;
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b, int a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b, float a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    // common properties
    private boolean _pass_events = true;

    public boolean getPassEvents() {
        return _pass_events;
    }

    public void setPassEvents(boolean value) {
        if (_pass_events == value)
            return;
        _pass_events = value;
    }

    private boolean _disabled;

    public boolean isDisabled() {
        return _disabled;
    }

    public void setDisabled(boolean value) {
        if (_disabled == value)
            return;
        _disabled = value;
        updateState();
    }

    private boolean _hover;

    public boolean isMouseHover() {
        return _hover;
    }

    public void setMouseHover(boolean value) {
        if (_hover == value)
            return;
        _hover = value;
        updateState();
    }

    private boolean _pressed;

    public boolean isMousePressed() {
        return _pressed;
    }

    public void setMousePressed(boolean value) {
        if (_pressed == value)
            return;
        _pressed = value;
        updateState();
    }

    private boolean _focused;
    public boolean isFocusable = true;

    public boolean isFocused() {
        return _focused;
    }

    public void setFocused(boolean value) {
        if (isFocusable) {
            if (_focused == value)
                return;
            _focused = value;
            updateState();
        }
    }

    public void setFocus() {
        getHandler().setFocusedItem(this);
    }

    @Override
    protected void updateInnersDrawable(boolean value) {
        for (BaseItem item : _content) {
            item.setVisible(value);
        }
    }

    protected void updateState() {
        super.setBackground(getState(_state).background);

        _border.setFill(getState(_state).border.getFill());
        Border br = getState(_state).border;
        if (br.getThickness() > 0) {
            if (br.getRadius() < 0)
                _border.setRadius(getState(ItemStateType.BASE).border.getRadius());
            else
                _border.setRadius(getState(_state).border.getRadius());
            _border.setThickness(getState(_state).border.getThickness());
        }

        if (getState(_state).shape != null)
            isCustom = getState(_state).shape;

        // mixing
        if (isDisabled() && states.containsKey(ItemStateType.DISABLED)) {
            super.setBackground(GraphicsMathService.mixColors(getState(_state).background,
                    getState(ItemStateType.DISABLED).background));

            _border.setFill(GraphicsMathService.mixColors(getState(_state).border.getFill(),
                    getState(ItemStateType.DISABLED).border.getFill()));
            br = getState(ItemStateType.DISABLED).border;
            if (br.getThickness() > 0) {
                if (br.getRadius() < 0)
                    _border.setRadius(getState(ItemStateType.BASE).border.getRadius());
                else
                    _border.setRadius(getState(ItemStateType.DISABLED).border.getRadius());
                _border.setThickness(getState(ItemStateType.DISABLED).border.getThickness());
            }
            return;
        }

        if (isFocused() && states.containsKey(ItemStateType.FOCUSED)) {
            super.setBackground(GraphicsMathService.mixColors(getState(_state).background,
                    getState(ItemStateType.FOCUSED).background));
            _border.setFill(GraphicsMathService.mixColors(getState(_state).border.getFill(),
                    getState(ItemStateType.FOCUSED).border.getFill()));
            br = getState(ItemStateType.FOCUSED).border;
            if (br.getThickness() > 0) {
                if (br.getRadius() < 0)
                    _border.setRadius(getState(ItemStateType.BASE).border.getRadius());
                else
                    _border.setRadius(getState(ItemStateType.FOCUSED).border.getRadius());
                _border.setThickness(getState(ItemStateType.FOCUSED).border.getThickness());
            }
        }

        if (isMouseHover() && states.containsKey(ItemStateType.HOVERED)) {
            super.setBackground(GraphicsMathService.mixColors(getState(_state).background,
                    getState(ItemStateType.HOVERED).background));
            _border.setFill(GraphicsMathService.mixColors(getState(_state).border.getFill(),
                    getState(ItemStateType.HOVERED).border.getFill()));
            br = getState(ItemStateType.HOVERED).border;
            if (br.getThickness() > 0) {
                if (br.getRadius() < 0)
                    _border.setRadius(getState(ItemStateType.BASE).border.getRadius());
                else
                    _border.setRadius(getState(ItemStateType.HOVERED).border.getRadius());
                _border.setThickness(getState(ItemStateType.HOVERED).border.getThickness());
            }
        }

        if (isMousePressed() && states.containsKey(ItemStateType.PRESSED)) {
            super.setBackground(GraphicsMathService.mixColors(getState(_state).background,
                    getState(ItemStateType.PRESSED).background));
            _border.setFill(GraphicsMathService.mixColors(getState(_state).border.getFill(),
                    getState(ItemStateType.PRESSED).border.getFill()));
            br = getState(ItemStateType.PRESSED).border;
            if (br.getThickness() > 0) {
                if (br.getRadius() < 0)
                    _border.setRadius(getState(ItemStateType.BASE).border.getRadius());
                else
                    _border.setRadius(getState(ItemStateType.PRESSED).border.getRadius());
                _border.setThickness(getState(ItemStateType.PRESSED).border.getThickness());
            }
        }
    }

    // public Pointer _mouse_ptr = new Pointer();

    public boolean getHoverVerification(float xpos, float ypos) {
        switch (HoverRule) {
        case LAZY:
            return lazyHoverVerification(xpos, ypos);
        case STRICT:
            return strictHoverVerification(xpos, ypos);
        default:
            return false;
        }
    }

    private boolean strictHoverVerification(float xpos, float ypos) {
        List<float[]> tmp = updateShape();
        if (tmp == null)
            return false;

        float Ax, Ay, Bx, By, Cx, Cy, Px, Py, m, l;
        boolean result = false;

        for (int point = 0; point < tmp.size(); point += 3) {
            Px = xpos;
            Py = ypos;
            Ax = tmp.get(point)[0];
            Ay = tmp.get(point)[1];
            Bx = tmp.get(point + 1)[0];
            By = tmp.get(point + 1)[1];
            Cx = tmp.get(point + 2)[0];
            Cy = tmp.get(point + 2)[1];

            Bx = Bx - Ax;
            By = By - Ay;
            Cx = Cx - Ax;
            Cy = Cy - Ay;
            Px = Px - Ax;
            Py = Py - Ay;
            Ax = 0;
            Ay = 0;

            m = (Px * By - Bx * Py) / (Cx * By - Bx * Cy);
            if (m >= 0) {
                l = (Px - m * Cx) / Bx;
                if (l >= 0 && (m + l) <= 1) {
                    result = true;
                    // _mouse_ptr.setPosition(xpos, ypos);
                    return result;
                }
            }
        }

        // _mouse_ptr.clear();
        return result;
    }

    private boolean lazyHoverVerification(float xpos, float ypos) {
        // if(this instanceof ContextMenu)
        // {
        // System.out.println("context menu");
        // System.out.println(
        // _confines_x_0 + " " +
        // _confines_x_1 + " " +
        // _confines_y_0 + " " +
        // _confines_y_1 + " "
        // );
        // }
        boolean result = false;
        float minx = getX();
        float maxx = getX() + getWidth();
        float miny = getY();
        float maxy = getY() + getHeight();

        if (_confines_x_0 > minx)
            minx = _confines_x_0;

        if (_confines_x_1 < maxx)
            maxx = _confines_x_1;

        if (_confines_y_0 > miny)
            miny = _confines_y_0;

        if (_confines_y_1 < maxy)
            maxy = _confines_y_1;

        if (xpos >= minx && xpos <= maxx && ypos >= miny && ypos <= maxy) {
            result = true;
            // _mouse_ptr.setPosition(xpos, ypos);
        }
        // else {
        // _mouse_ptr.clear();
        // }
        return result;
    }

    public CustomFigure isCustom = null;

    @Override
    public List<float[]> makeShape() {
        if (isCustom != null) {
            setTriangles(isCustom.getFigure());
            if (getState(ItemStateType.BASE).shape == null)
                getState(ItemStateType.BASE).shape = isCustom;

            if (isCustom.isFixed())
                return GraphicsMathService.toGL(isCustom.updatePosition(getX(), getY()), getHandler());
            else
                return GraphicsMathService.toGL(updateShape(), getHandler());
        }
        setTriangles(GraphicsMathService.getRoundSquare(getWidth(), getHeight(), getBorder().getRadius(), getX(), getY()));
        // System.out.println(getItemName() + " " + getTriangles().size());
        return GraphicsMathService.toGL(this, getHandler());
    }

    // style
    private boolean _is_style_set = false;

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        _is_style_set = true;

        setBackground(style.background);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
        setSize(style.width, style.height);
        setMinSize(style.minWidth, style.minHeight);
        setMaxSize(style.maxWidth, style.maxHeight);
        setAlignment(style.alignment);
        setPosition(style.x, style.y);
        setPadding(style.padding);
        setSpacing(style.spacing);
        setMargin(style.margin);
        _border.setRadius(style.borderRadius);
        _border.setThickness(style.borderThickness);
        _border.setFill(style.borderFill);
        setVisible(style.isVisible);
        removeAllItemStates();

        ItemState core_state = new ItemState(style.background);
        core_state.border.setRadius(style.borderRadius);
        core_state.border.setThickness(style.borderThickness);
        core_state.border.setFill(style.borderFill);

        for (Map.Entry<ItemStateType, ItemState> state : style.getAllStates().entrySet()) {
            addItemState(state.getKey(), state.getValue());
        }
        if (style.shape != null) {
            isCustom = new CustomFigure(style.isFixedShape, style.shape);
            core_state.shape = isCustom;
        }
        addItemState(ItemStateType.BASE, core_state);
    }

    @Override
    public Style getCoreStyle() {
        Style style = new Style();
        style.setSize(getWidth(), getHeight());
        style.setSizePolicy(getWidthPolicy(), getHeightPolicy());
        style.background = getBackground();
        style.minWidth = getMinWidth();
        style.minHeight = getMinHeight();
        style.maxWidth = getMaxWidth();
        style.maxHeight = getMaxHeight();
        style.x = getX();
        style.y = getY();
        style.padding = new Indents(getPadding().left, getPadding().top, getPadding().right, getPadding().bottom);
        style.margin = new Indents(getMargin().left, getMargin().top, getMargin().right, getMargin().bottom);
        style.spacing = new Spacing(getSpacing().horizontal, getSpacing().vertical);
        style.alignment = getAlignment();
        style.borderFill = _border.getFill();
        style.borderRadius = _border.getRadius();
        style.borderThickness = _border.getThickness();
        style.isVisible = getVisible();
        if (isCustom != null) {
            style.shape = isCustom.getFigure();
            style.isFixedShape = isCustom.isFixed();
        }
        for (Map.Entry<ItemStateType, ItemState> state : states.entrySet()) {
            style.addItemState(state.getKey(), state.getValue());
        }

        return style;
    }
}