package sandbox.View.Components;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Tab;
import com.spvessel.spacevil.TabView;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.WrapGrid;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import sandbox.View.Decorations.Palette;

public class PerformanceView extends VerticalStack {
    @Override
    public void initElements() {
        setPadding(2, 2, 2, 2);

        TabView layoutTabs = new TabView();
        layoutTabs.setTabPolicy(SizePolicy.Expand);

        Tab shadowTest = new Tab("ShadowTest");
        Tab wrapTest = new Tab("WrapTest");
        Tab wrapWrapTest = new Tab("WrapWrapTest");
        Tab stackTest = new Tab("StackTest");
        Tab gridTest = new Tab("GridTest");

        addItem(layoutTabs);
        layoutTabs.addTabs(shadowTest, wrapTest, wrapWrapTest, stackTest, gridTest);

        layoutTabs.addItemToTab(shadowTest, new ShadowTest());
        layoutTabs.addItemToTab(wrapTest, new WrapTest());
        layoutTabs.addItemToTab(wrapWrapTest, new WrapWrapTest());
        layoutTabs.addItemToTab(stackTest, new ListTest());
        layoutTabs.addItemToTab(gridTest, new GridTest());
    }

    private BlankItem getBlankItem(int index) {
        BlankItem blank = new BlankItem();
        blank.setItemName("Button_" + index);
        blank.setBackground(Palette.White);
        blank.setSize(8, 8);
        blank.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        blank.addItemState(ItemStateType.Hovered, new ItemState(Palette.Pink));
        blank.addItemState(ItemStateType.Pressed, new ItemState(Palette.DarkGlass));
        blank.setMargin(1, 1, 1, 1);
        
        return blank;
    }

    private void addManyItems(WrapGrid container, int count) {
        List<IBaseItem> content = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            content.add(getBlankItem(i + 1));
        }
        container.setListContent(content);
    }

    class WrapTest extends Frame {
        @Override
        public void initElements() {
            WrapGrid wrapGrid = new WrapGrid(9, 9, Orientation.Horizontal);
            wrapGrid.getArea().setSpacing(0, 0);
            wrapGrid.setBackground(Palette.Transparent);
            addItems(wrapGrid);
            addManyItems(wrapGrid, 1000);
        }
    }

    class WrapWrapTest extends Frame {
        @Override
        public void initElements() {
            WrapGrid wrapGrid = new WrapGrid(250, 250, Orientation.Horizontal);
            wrapGrid.getArea().setSpacing(0, 0);
            wrapGrid.setMargin(0, 0, 0, 30);
            addItems(wrapGrid);
            for (int i = 0; i < 8; i++) {
                WrapGrid w = new WrapGrid(10, 10, Orientation.Horizontal);
                w.getArea().setSpacing(0, 0);
                w.setMargin(2, 2, 2, 2);
                w.setBackground(Palette.Black);
                wrapGrid.addItem(w);
                addManyItems(w, 1000);
            }
        }
    }

    class ListTest extends Frame {
        @Override
        public void initElements() {
            setBackground(200, 200, 200);
            ListBox list = new ListBox();
            list.setSpacing(0, 0);
            addItems(list);
            int index = 0;
            for (int i = 0; i < 1000; i++) {
                IBaseItem blank = getBlankItem(++index);
                blank.setSize(14, 14);
                blank.effects().add(new Shadow(5, new Position(3, 2), new Size(10, 10), Palette.BlackShadow));
                list.addItem(blank);
            }
        }
    }

    class GridTest extends Frame {
        @Override
        public void initElements() {
            Grid grid = new Grid(20, 50);
            grid.setSpacing(2, 2);
            addItems(grid);
            int index = 0;
            for (int i = 0; i < 1000; i++) {
                grid.insertItem(getBlankItem(index++), i);
            }
        }
    }

    class ShadowTest extends Frame {
        Label infoOutput = null;

        @Override
        public void initElements() {
            setBackground(200, 200, 200);

            infoOutput = new Label("No button pressed");
            infoOutput.setHeight(25);
            infoOutput.setHeightPolicy(SizePolicy.Fixed);
            infoOutput.setBackground(80, 80, 80);
            infoOutput.setForeground(210, 210, 210);
            infoOutput.setFontSize(18);
            infoOutput.setFontStyle(Font.BOLD);
            infoOutput.setAlignment(ItemAlignment.Bottom);
            infoOutput.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            infoOutput.setPadding(0, 0, 0, 3);
            
            VerticalStack layout = new VerticalStack();
            layout.setSpacing(0, 0);
            addItems(layout);
            layout.addItem(infoOutput);

            int index = 0;
            for (int i = 0; i < 32; i++) {
                HorizontalStack stack = new HorizontalStack();
                stack.setHeightPolicy(SizePolicy.Fixed);
                stack.setHeight(22);
                stack.setSpacing(6, 0);
                stack.setContentAlignment(ItemAlignment.HCenter);
                layout.addItem(stack);
                for (int j = 0; j < 32; j++) {
                    BlankItem blank = getBlankItem(++index);
                    blank.eventMouseClick.add((sender, args) -> {
                        infoOutput.setText(blank.getItemName() + " was pressed");
                    });
                    // set size & shadow
                    blank.setSize(14, 14);
                    blank.effects().add(new Shadow(5, new Position(3, 2), new Size(10, 10), Palette.BlackShadow));
                    stack.addItem(blank);
                }
            }
        }
    }
}
