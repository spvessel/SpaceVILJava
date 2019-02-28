package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Style;

public class ListArea extends Prototype implements InterfaceVLayout {
    private Lock _lock = new ReentrantLock();

    public EventCommonMethod selectionChanged = new EventCommonMethod();
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    private int _step = 30;

    /**
     * ScrollBar moving step
     */
    public void setStep(int value) {
        _step = value;
    }

    public int getStep() {
        return _step;
    }

    private int _selection = -1;

    /**
     * @return Number of the selected item
     */
    public int getSelection() {
        return _selection;
    }

    private SelectionItem _selectionItem;

    /**
     * @return selected item
     */
    public InterfaceBaseItem getSelectionItem() {
        if (_selectionItem != null)
            return _selectionItem.getContent();
        return null;
    }

    SelectionItem getTrueSelection() {
        return _selectionItem;
    }

    /**
     * Set selected item by index
     */
    public void setSelection(int index) {
        _selection = index;
        _selectionItem = ((SelectionItem) getItems().get(index));
        _selectionItem.setToggled(true);
        unselectOthers(_selectionItem);
        if (_selectionItem.getContent() instanceof Prototype) {
            ((Prototype) _selectionItem.getContent()).setFocus();
        }
        selectionChanged.execute();
    }

    private void unselectOthers(SelectionItem sender) {
        List<InterfaceBaseItem> items = getItems();
        for (InterfaceBaseItem item : items) {
            if (!item.equals(sender)) {
                ((SelectionItem) item).setToggled(false);
            }
        }
    }

    /**
     * Unselect all items
     */
    public void unselect() {
        _selection = -1;
        if (_selectionItem != null) {
            _selectionItem.setToggled(false);
            _selectionItem = null;
        }
    }

    private boolean _isSelectionVisible = true;

    /**
     * Is selection changes view of the item or not
     */
    public void setSelectionVisibility(boolean visibility) {
        _isSelectionVisible = visibility;
        updateLayout();
    }

    public boolean getSelectionVisibility() {
        return _isSelectionVisible;
    }

    List<Integer> _list_of_visible_items = new LinkedList<>();

    private static int count = 0;

    /**
     * Constructs a ListArea
     */
    public ListArea() {
        setItemName("ListArea_" + count);
        count++;
        eventMouseClick.add(this::onMouseClick);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);
        eventMouseHover.add(this::onMouseHover);
        eventKeyPress.add(this::onKeyPress);
        // setStyle(DefaultsService.getDefaultStyle(ListArea.class));
    }

    private void onMouseClick(InterfaceItem sender, MouseArgs args) {

    }

    private void onMouseDoubleClick(InterfaceItem sender, MouseArgs args) {

    }

    private void onMouseHover(InterfaceItem sender, MouseArgs args) {

    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        int index = _selection;
        switch (args.key) {
        case UP:
            index--;
            if (index < 0)
                break;
            setSelection(index);
            break;
        case DOWN:
            index++;
            if (index >= super.getItems().size())
                break;
            setSelection(index);
            break;
        case ESCAPE:
            unselect();
            break;
        default:
            break;
        }
    }

    /**
     * If something changes when mouse hovered
     */
    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
    }

    Map<InterfaceBaseItem, SelectionItem> _mapContent = new HashMap<>();

    private SelectionItem getWrapper(InterfaceBaseItem item) {
        SelectionItem wrapper = new SelectionItem(item);
        // wrapper.setStyle(_selectedStyle);
        wrapper.eventMouseClick.add((sender, args) -> {
            int index = 0;
            _selectionItem = _mapContent.get(item);
            for (InterfaceBaseItem var : super.getItems()) {
                if (var.equals(_selectionItem)) {
                    _selection = index;
                    selectionChanged.execute();
                    return;
                }
                index++;
            }
        });
        return wrapper;
    }

    /**
     * Insert item into the ListArea by index
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        SelectionItem wrapper = getWrapper(item);
        super.insertItem(wrapper, index);
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    private Style _selectedStyle;

    /**
     * Add item to the ListArea
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        SelectionItem wrapper = getWrapper(item);
        super.addItem(wrapper);
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    /**
     * Remove item from the ListArea
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        super.removeItem(_mapContent.get(item));
        _mapContent.remove(item);
        updateLayout();
        itemListChanged.execute();
    }

    void removeAllItems() {
        _lock.lock();
        try {
            unselect();
            List<InterfaceBaseItem> list = new LinkedList<>(getItems());

            if (list == null || list.size() == 0)
                return;

            while (!list.isEmpty()) {
                super.removeItem(list.get(0));
                list.remove(0);
            }
            _mapContent.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Set Y position of the ListArea
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    // update content position
    private long _yOffset = 0;
    private long _xOffset = 0;

    /**
     * Vertical scroll offset in the ListArea
     */
    public long getVScrollOffset() {
        return _yOffset;
    }

    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    /**
     * Horizontal scroll offset in the ListArea
     */
    public long getHScrollOffset() {
        return _xOffset;
    }

    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    /**
     * Update all children and ListArea sizes and positions according to confines
     */
    public void updateLayout() {
        _list_of_visible_items.clear();

        long offset = (-1) * getVScrollOffset();
        int startY = getY() + getPadding().top;
        int index = -1;
        for (InterfaceBaseItem child : super.getItems()) {
            index++;
            if (!child.isVisible())
                continue;

            child.setX((-1) * (int) _xOffset + getX() + getPadding().left + child.getMargin().left);

            long child_Y = startY + offset + child.getMargin().top;
            offset += child.getHeight() + getSpacing().vertical;
            // top checking
            if (child_Y < startY) {
                child.setY((int) child_Y);
                if (child_Y + child.getHeight() <= startY) {
                    child.setDrawable(false);
                } else {
                    child.setDrawable(true);
                    _list_of_visible_items.add(index);
                }
                continue;
            }
            // bottom checking
            if (child_Y + child.getHeight() + child.getMargin().bottom > getY() + getHeight() - getPadding().bottom) {
                child.setY((int) child_Y);
                if (child_Y >= getY() + getHeight() - getPadding().bottom) {
                    child.setDrawable(false);
                } else {
                    child.setDrawable(true);
                    _list_of_visible_items.add(index);
                }
                continue;
            }

            child.setY((int) child_Y);
            child.setDrawable(true);
            _list_of_visible_items.add(index);

            // refactor
            child.setConfines();
        }
    }

    /**
     * Set style of the ListArea
     */
    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("selecteditem");
        if (inner_style != null) {
            _selectedStyle = inner_style;
        }
    }
}