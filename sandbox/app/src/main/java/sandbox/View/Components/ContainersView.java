package sandbox.View.Components;

import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Tab;
import com.spvessel.spacevil.TabView;
import com.spvessel.spacevil.TreeItem;
import com.spvessel.spacevil.TreeView;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.WrapGrid;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Flags.TreeItemType;

public class ContainersView extends Frame {

    @Override
    public void initElements() {

        TabView layoutTabs = new TabView();
        layoutTabs.setTabPolicy(SizePolicy.Expand);

        Tab frameTab = new Tab("Frame");
        frameTab.setDraggable(false);

        Tab stackTab = new Tab("Stacks");
        Tab gridTab = new Tab("Grid");
        Tab wrapGridTab = new Tab("WrapGrid");
        Tab listTab = new Tab("List");
        Tab treeTab = new Tab("Tree");

        addItem(layoutTabs);
        layoutTabs.addTabs(frameTab, stackTab, gridTab, wrapGridTab, listTab, treeTab);

        // Frame
        Frame frame = new Frame();
        frame.setMargin(30, 30, 30, 30);
        layoutTabs.addItemToTab(frameTab, frame);
        fillFrame(frame);

        // Stacks
        HorizontalStack commonStack = new HorizontalStack();
        commonStack.setMargin(30, 30, 30, 30);
        commonStack.setSpacing(20, 0);
        layoutTabs.addItemToTab(stackTab, commonStack);
        VerticalStack vStack = new VerticalStack();
        vStack.setSpacing(0, 10);
        HorizontalStack hStack = new HorizontalStack();
        hStack.setSpacing(10, 0);
        commonStack.addItems(vStack, hStack);
        fillContainer(vStack, 5, SizePolicy.Expand);
        fillContainer(hStack, 5, SizePolicy.Expand);

        // Grid
        Grid grid = new Grid(3, 4);
        grid.setSpacing(20, 20);
        grid.setMargin(30, 30, 30, 30);
        layoutTabs.addItemToTab(gridTab, grid);
        fillGrid(grid);

        // WrapGrid
        HorizontalStack wrapGridContainer = new HorizontalStack();
        wrapGridContainer.setMargin(30, 30, 30, 30);
        wrapGridContainer.setSpacing(20, 0);
        layoutTabs.addItemToTab(wrapGridTab, wrapGridContainer);
        WrapGrid vWrapGrid = new WrapGrid(100, 70, Orientation.Horizontal);
        vWrapGrid.setStretch(true);
        vWrapGrid.getArea().setSpacing(10, 10);
        WrapGrid hWrapGrid = new WrapGrid(70, 100, Orientation.Vertical);
        hWrapGrid.getArea().setSpacing(10, 10);
        wrapGridContainer.addItems(vWrapGrid, hWrapGrid);
        fillContainer(vWrapGrid, 20, SizePolicy.Expand);
        fillContainer(hWrapGrid, 20, SizePolicy.Expand);

        // ListBox
        ListBox listBox = new ListBox();
        layoutTabs.addItemToTab(listTab, listBox);
        fillContainer(listBox, 50, SizePolicy.Fixed);

        // TreeView
        TreeView treeView = new TreeView();
        treeView.setMargin(10, 30, 5, 5);
        layoutTabs.addItemToTab(treeTab, treeView);
        treeView.setRootVisible(true);
        fillTreeView(treeView);
    }

    private void fillFrame(Frame frame) {
        Prototype leftTopItem = ComponentsFactory.makeFixedLabel("LeftTop", 200, 40);
        leftTopItem.setAlignment(ItemAlignment.Left, ItemAlignment.Top);

        Prototype centerTopItem = ComponentsFactory.makeFixedLabel("CenterTop", 200, 40);
        centerTopItem.setAlignment(ItemAlignment.HCenter, ItemAlignment.Top);

        Prototype rightTopItem = ComponentsFactory.makeFixedLabel("RightTop", 200, 40);
        rightTopItem.setAlignment(ItemAlignment.Right, ItemAlignment.Top);

        Prototype leftCenterItem = ComponentsFactory.makeFixedLabel("LeftCenter", 200, 40);
        leftCenterItem.setAlignment(ItemAlignment.Left, ItemAlignment.VCenter);

        Prototype centerItem = ComponentsFactory.makeFixedLabel("Center", 200, 40);
        centerItem.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        Prototype rightCenterItem = ComponentsFactory.makeFixedLabel("RifgtCenter", 200, 40);
        rightCenterItem.setAlignment(ItemAlignment.Right, ItemAlignment.VCenter);

        Prototype leftBottomItem = ComponentsFactory.makeFixedLabel("LeftBottom", 200, 40);
        leftBottomItem.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);

        Prototype centerBottomItem = ComponentsFactory.makeFixedLabel("CenterBottom", 200, 40);
        centerBottomItem.setAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);

        Prototype rightBottomItem = ComponentsFactory.makeFixedLabel("RightBottom", 200, 40);
        rightBottomItem.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);

        frame.addItems(leftTopItem, centerTopItem, rightTopItem, leftCenterItem, centerItem, rightCenterItem,
                leftBottomItem, centerBottomItem, rightBottomItem);
    }

    private void fillGrid(Grid grid) {
        for (int i = 0; i < grid.getColumnCount() * grid.getRowCount(); i++) {
            grid.addItem(ComponentsFactory.makeExpandedLabel("" + (i + 1)));
        }
    }

    private void fillContainer(Prototype container, int count, SizePolicy policy) {
        for (int i = 0; i < count; i++) {
            if (policy == SizePolicy.Expand) {
                container.addItem(ComponentsFactory.makeExpandedLabel("" + (i + 1)));
            } else {
                container.addItem(ComponentsFactory.makeFixedLabel("" + (i + 1), 200, 50));
            }
        }
    }

    private void fillTreeView(TreeView tree) {
        for (int i = 0; i < 10; i++) {
            TreeItem branch = new TreeItem(TreeItemType.Branch, "Branch " + (i + 1));
            tree.addItem(branch);
            for (int j = 0; j < 3; j++) {
                TreeItem leaf = new TreeItem(TreeItemType.Leaf, "Leaf " + (j + 1));
                branch.addItem(leaf);
            }
        }
    }
}
