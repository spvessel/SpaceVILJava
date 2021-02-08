package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFreeLayout;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;
import java.util.LinkedList;

/**
 * Grid is a class that represents a grid type container. Each element is in a
 * specific grid cell.
 * <p>
 * Cells size based on items margins, sizes and size policies.
 * <p>
 * Grid cannot receive any events, so Grid is always in the
 * com.spvessel.spacevil.Flags.ItemStateType.BASE state.
 */
public class Grid extends Prototype implements IFreeLayout {
    private static int count = 0;

    /**
     * Default Grid constructor.
     */
    private Grid() {
        setItemName("Grid_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(Grid.class));
        isFocusable = false;
    }

    /**
     * Constructs a Grid with the given number of rows and number of columns.
     * 
     * @param rows    Number of rows.
     * @param columns Number of columns.
     */
    public Grid(int rows, int columns) {
        this();
        _rowCount = rows;
        _columnCount = columns;
        initCells();
    }

    private List<Cell> _cells;

    void initCells() {
        _cells = new LinkedList<>();
        for (int i = 0; i < _rowCount; i++) {
            for (int j = 0; j < _columnCount; j++) {
                _cells.add(new Cell(this, i, j));
            }
        }
    }

    /**
     * Setting a new grid format with the given number of rows and number of
     * columns.
     * 
     * @param rows    Number of rows.
     * @param columns Number of columns.
     */
    public void setFormat(int rows, int columns) {
        if (rows == _rowCount && columns == _columnCount)
            return;

        _rowCount = rows;
        _columnCount = columns;
        rearrangeCells();
    }

    private void rearrangeCells() {
        if (_cells == null) {
            initCells();
            return;
        }

        List<IBaseItem> items = new LinkedList<>();
        for (Cell cell : _cells)
            items.add(cell.getItem());
        initCells();
        int index = 0;
        for (IBaseItem item : items) {
            _cells.get(index).setItem(item);
            index++;
            if (_cells.size() == index)
                break;
        }
        updateLayout();
    }

    private int _rowCount = 1;

    /**
     * Setting a new count of the rows.
     * 
     * @param value Number of rows.
     */
    public void setRowCount(int value) {
        if (value != _rowCount)
            _rowCount = value;
        rearrangeCells();
    }

    /**
     * Getting current rows count in grid.
     * 
     * @return Current rows count.
     */
    public int getRowCount() {
        return _rowCount;
    }

    private int _columnCount = 1;

    /**
     * Setting a new count of the columns.
     * 
     * @param value Number of columns.
     */
    public void setColumnCount(int value) {
        if (value != _columnCount)
            _columnCount = value;
        // Need to initCells REFACTOR!
        rearrangeCells();
    }

    /**
     * Getting current columns count in grid.
     * 
     * @return Current columns count.
     */
    public int getColumnCount() {
        return _columnCount;
    }

    /**
     * Returns the cell by row and column number.
     * 
     * @param row    Number of cell row.
     * @param column Number of cell column.
     * @return Cell of the Grid as com.spvessel.spacevil.Cell.
     */
    public Cell getCell(int row, int column) {
        Cell cell = null;
        try {
            cell = _cells.get(column + row * _columnCount);
        } catch (Exception ex) {
            System.out.println("Cells row and column out of range.\n" + ex.toString());
            return cell;
        }
        return cell;
    }

    /**
     * Getting all cells as list.
     * 
     * @return Cells as List&lt;com.spvessel.spacevil.Cell&gt;
     */
    public List<Cell> getAllCells() {
        return _cells;
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Removing item from the Grid. if the removal was successful Cell becomes free.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
        boolean superBool = super.removeItem(item);
        for (Cell link : _cells) {
            if (link.getItem() == item) {
                link.setItem(null);
            }
        }
        // UpdateLayout();
        return superBool; // Предполагается, что в super и cells добавляется одновременно
    }

    /**
     * Removing item from the Grid by number of row and number of column. if the
     * removal was successful Cell becomes free.
     * 
     * @param row    Index of row.
     * @param column Index of column.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeItem(int row, int column) {
        if (row >= _rowCount || column >= _columnCount)
            return false;

        IBaseItem ibi = _cells.get(column + row * _columnCount).getItem();
        if (ibi != null) {
            if (super.removeItem(ibi)) {
                _cells.get(column + row * _columnCount).setItem(null);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove all items in the Grid.
     */
    @Override
    public void clear() {
        List<IBaseItem> list = getItems();
        while (!list.isEmpty()) {
            removeItem(list.get(0));
            list.remove(0);
        }
    }

    /**
     * Adding item to the Grid.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        // ignore if it is out of space, add in free cell, attach row and collumn
        // numbers
        for (Cell cell : _cells) {
            if (cell.getItem() == null) {
                super.addItem(item);
                cell.setItem(item);
                updateLayout();
                return;
            }
        }
    }

    /**
     * Inserting item into the Cell by row and column index.
     * 
     * @param item   Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param row    Row index.
     * @param column Column index.
     */
    public void insertItem(IBaseItem item, int row, int column) {
        if (row == _rowCount || column == _columnCount)
            return;
        super.addItem(item);

        removeItem(row, column);

        _cells.get(column + row * _columnCount).setItem(item);
        updateLayout();
    }

    /**
     * Inserting item into the Cell by cell index.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param index Cell index.
     */
    @Override
    public void insertItem(IBaseItem item, int index) {
        if (_columnCount == 0)
            return;
        int row, column;
        row = index / _columnCount;
        column = index - row * _columnCount;
        insertItem(item, row, column);
    }

    /**
     * Setting Grid width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the Grid.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Setting X coordinate of the left-top corner of the Grid.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    /**
     * Setting Grid height. If the value is greater/less than the maximum/minimum
     * value of the height, then the height becomes equal to the maximum/minimum
     * value.
     * 
     * @param height Height of the Grid.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    /**
     * Setting Y coordinate of the left-top corner of the Grid.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        updateLayout();
    }

    // TMP
    private int[] colWidth;
    private int[] rowHeight;

    int[] getColWidth() {
        return colWidth;
    }

    int[] getRowHeight() {
        return rowHeight;
    }

    private boolean _isUpdating = false;

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.IFreeLayout).
     */
    public void updateLayout() {

        List<IBaseItem> list = getItems();
        if (list == null || list.size() == 0 || _isUpdating)
            return;
        _isUpdating = true;

        int[] columns_width = getColumnsWidth();
        colWidth = columns_width;

        int[] rows_height = getRowsHeight();
        rowHeight = rows_height;

        int x_offset = 0;
        int y_offset = 0;
        for (int r = 0; r < _rowCount; r++) {
            int index = 0;
            for (int c = 0; c < _columnCount; c++) {
                index = c + r * _columnCount;

                IBaseItem item = _cells.get(index).getItem();

                _cells.get(index).setRow(r);
                _cells.get(index).setColumn(c);

                // 6
                _cells.get(index).setWidth(columns_width[c]);
                _cells.get(index).setHeight(rows_height[r]);
                _cells.get(index).setX(getX() + getPadding().left + x_offset);
                _cells.get(index).setY(getY() + getPadding().top + y_offset);

                if (item != null) {
                    if (item.getWidthPolicy() == SizePolicy.Expand)
                        item.setWidth(columns_width[c] - item.getMargin().left - item.getMargin().right);

                    if (item.getHeightPolicy() == SizePolicy.Expand)
                        item.setHeight(rows_height[r] - item.getMargin().top - item.getMargin().bottom);
                    item.setConfines();
                }

                // 7
                _cells.get(index).updateBehavior();
                x_offset += _cells.get(index).getWidth() + getSpacing().horizontal;
            }
            y_offset += _cells.get(index).getHeight() + getSpacing().vertical;
            x_offset = 0;
        }
        _isUpdating = false;
    }

    private int[] getRowsHeight() {
        int[] rows_height = new int[_rowCount];
        List<int[]> list_height = new LinkedList<>();

        int total_space = getHeight() - getPadding().top - getPadding().bottom;
        int free_space = total_space;
        int prefer_height = (total_space - getSpacing().vertical * (_rowCount - 1)) / _rowCount;
        int count = _rowCount;

        for (int r = 0; r < _rowCount; r++) {
            for (int c = 0; c < _columnCount; c++) {
                IBaseItem item = _cells.get(c + r * _columnCount).getItem();

                if (item == null || !item.isVisible() || !item.isDrawable()) {
                    list_height.add(new int[] { r, -1 });
                    continue;
                }

                if (item.getHeightPolicy() == SizePolicy.Fixed) {
                    list_height.add(new int[] { r, item.getHeight() + item.getMargin().top + item.getMargin().bottom });
                } else {
                    list_height.add(new int[] { r, 0 });
                }
            }
        }
        ///////////
        List<Integer[]> m_height = new LinkedList<>();
        for (int r = 0; r < _rowCount; r++) {
            int max = -10;
            for (int c = 0; c < _columnCount; c++) {
                if (list_height.get(c + r * _columnCount)[1] > max)
                    max = list_height.get(c + r * _columnCount)[1];
            }
            m_height.add(new Integer[] { r, max });
            if (max == -1) {
                count--;
                if (count == 0)
                    count++;
                prefer_height = (free_space - getSpacing().vertical * (count - 1)) / count;
            }
        }

        m_height.sort((li1, li2) -> li2[1].compareTo(li1[1]));

        for (Integer[] pair : m_height) {
            if (pair[1] == 0)
                pair[1] = prefer_height;
            else if (pair[1] < 0) {
                pair[1] = 0;
            } else {
                free_space -= pair[1];
                count--;
                if (count == 0)
                    count++;
                prefer_height = (free_space - getSpacing().vertical * (count - 1)) / count;
            }
        }

        m_height.sort((li1, li2) -> li1[0].compareTo(li2[0]));

        for (int i = 0; i < _rowCount; i++)
            rows_height[i] = m_height.get(i)[1];

        return rows_height;
    }

    private int[] getColumnsWidth() {
        int[] columns_width = new int[_columnCount];
        List<int[]> list_width = new LinkedList<>();

        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int prefer_width = (total_space - getSpacing().horizontal * (_columnCount - 1)) / _columnCount;
        int count = _columnCount;

        for (int c = 0; c < _columnCount; c++) {
            for (int r = 0; r < _rowCount; r++) {
                IBaseItem item = _cells.get(c + r * _columnCount).getItem();
                if (item == null || !item.isVisible() || !item.isDrawable()) {
                    list_width.add(new int[] { c, -1 });
                    continue;
                }

                if (item.getWidthPolicy() == SizePolicy.Fixed) {
                    list_width.add(new int[] { c, item.getWidth() + item.getMargin().left + item.getMargin().right });
                } else {
                    list_width.add(new int[] { c, 0 });
                }
            }
        }
        //////////
        List<Integer[]> m_width = new LinkedList<>();
        for (int c = 0; c < _columnCount; c++) {
            int max = -10;
            for (int r = 0; r < _rowCount; r++) {
                if (list_width.get(r + c * _rowCount)[1] > max)
                    max = list_width.get(r + c * _rowCount)[1];
            }
            m_width.add(new Integer[] { c, max });
            if (max == -1) {
                count--;
                if (count == 0)
                    count++;
                prefer_width = (free_space - getSpacing().horizontal * (count - 1)) / count;
            }
        }

        m_width.sort((li1, li2) -> li2[1].compareTo(li1[1]));

        for (Integer[] pair : m_width) {
            if (pair[1] == 0)
                pair[1] = prefer_width;
            else if (pair[1] < 0) {
                pair[1] = 0;
            } else {
                free_space -= pair[1];
                count--;
                if (count == 0)
                    count++;
                prefer_width = (free_space - getSpacing().horizontal * (count - 1)) / count;
            }
        }

        m_width.sort((li1, li2) -> li1[0].compareTo(li2[0]));

        for (int i = 0; i < _columnCount; i++)
            columns_width[i] = m_width.get(i)[1];

        return columns_width;
    }
}