package sandbox.View.Components;

import com.spvessel.spacevil.ComboBox;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.FloatItem;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.InputDialog;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.PopUpMessage;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.SideArea;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.Palette;
import sandbox.View.Decorations.StyleFactory;

public class MenusPopupsAndTooltipsView extends ListBox {
    
    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);
        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 10);

        // ContextMenu
        MenuItem showSubmenu = ComponentsFactory.makeMenuItem("Show Submenu", () -> {
            System.out.println("Selected: Submenu");
        });
        ContextMenu contextMenu = ComponentsFactory.makeContextMenu(getHandler(), ComponentsFactory.makeMenuItem("MenuItem1", () -> {
            System.out.println("Selected: MenuItem1");
        }), ComponentsFactory.makeMenuItem("MenuItem2", () -> {
            System.out.println("Selected: MenuItem2");
        }), ComponentsFactory.makeMenuItem("MenuItem3", () -> {
            System.out.println("Selected: MenuItem3");
        }), ComponentsFactory.makeMenuItem("MenuItem4", () -> {
            System.out.println("Selected: MenuItem4");
        }), showSubmenu);

        ContextMenu subMenu = ComponentsFactory.makeContextMenu(getHandler(), ComponentsFactory.makeMenuItem("SubMenuItem1", () -> {
            System.out.println("Selected: SubMenuItem1");
        }), ComponentsFactory.makeMenuItem("SubMenuItem2", () -> {
            System.out.println("Selected: SubMenuItem2");
        }), ComponentsFactory.makeMenuItem("SubMenuItem3", () -> {
            System.out.println("Selected: SubMenuItem3");
        }), ComponentsFactory.makeMenuItem("SubMenuItem4", () -> {
            System.out.println("Selected: SubMenuItem4");
        }));

        showSubmenu.assignContextMenu(subMenu);

        HorizontalStack contextMenuContainer = new HorizontalStack();
        contextMenuContainer.setHeightPolicy(SizePolicy.Fixed);
        contextMenuContainer.setHeight(100);
        contextMenuContainer.setSpacing(30, 0);
        contextMenuContainer.setContentAlignment(ItemAlignment.HCenter);

        Prototype openContextMenuL = ComponentsFactory.makeActionButton("LeftClick", Palette.GreenLight, () -> {
            contextMenu.activeButton = MouseButton.ButtonLeft;
        });
        openContextMenuL.setBackground(Palette.OrangeLight);
        openContextMenuL.setToolTip("Left click to open the ContextMenu.");
        openContextMenuL.eventMouseClick.add((sender, args) -> {
            contextMenu.show(sender, args);
        });
        Prototype openContextMenuR = ComponentsFactory.makeActionButton("RightClick", Palette.GreenLight, () -> {
            contextMenu.activeButton = MouseButton.ButtonRight;
        });
        openContextMenuR.setBackground(Palette.Purple);
        openContextMenuR.setToolTip("Right click to open the ContextMenu.");
        openContextMenuR.eventMouseClick.add((sender, args) -> {
            contextMenu.show(sender, args);
        });
        Prototype openContextMenuPositioned = ComponentsFactory.makeActionButton("Positioned", Palette.GreenLight, () -> {
            contextMenu.activeButton = MouseButton.ButtonLeft;
        });
        openContextMenuPositioned.setToolTip("Left click to open the ContextMenu\nat the start/bottom of the button. ");
        openContextMenuPositioned.setBackground(Palette.Pink);
        openContextMenuPositioned.eventMouseClick.add((sender, args) -> {
            args.position.setPosition(openContextMenuPositioned.getX(),
                    openContextMenuPositioned.getY() + openContextMenuPositioned.getHeight());
            contextMenu.show(sender, args);
        });

        // ComboBox
        MenuItem filterItem = ComponentsFactory.makeImagedMenuItem("Open Filter Function Menu",
                DefaultsService.getDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size32x32));

        MenuItem recycleItem = ComponentsFactory.makeImagedMenuItem("Open Recycle Bin",
                DefaultsService.getDefaultImage(EmbeddedImage.RecycleBin, EmbeddedImageSize.Size32x32));

        MenuItem refreshItem = ComponentsFactory.makeImagedMenuItem("Refresh UI",
                DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));

        MenuItem addMenuItemItem = ComponentsFactory.makeImagedMenuItem("Add New Function...",
                DefaultsService.getDefaultImage(EmbeddedImage.Add, EmbeddedImageSize.Size32x32));

        // ComboBox
        ComboBox comboBox = new ComboBox(filterItem, recycleItem, refreshItem, addMenuItemItem);
        comboBox.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        comboBox.setText("Operations");
        comboBox.setStyle(StyleFactory.getComboBoxStyle());
        comboBox.setMargin(0, 35, 0, 35);

        // Change event for "addMenuItemItem" - add a new MenuItem into ComboBox
        addMenuItemItem.eventMouseClick.clear();
        addMenuItemItem.eventMouseClick.add((sender, args) -> {
            InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
            inDialog.onCloseDialog.add(() -> {
                if (inDialog.getResult() != "") {
                    comboBox.addItem(ComponentsFactory.makeImagedMenuItem(inDialog.getResult(),
                            DefaultsService.getDefaultImage(EmbeddedImage.Import, EmbeddedImageSize.Size32x32)));
                }
            });
            inDialog.show(addMenuItemItem.getHandler());
        });

        // Popups
        HorizontalStack popupsContainer = new HorizontalStack();
        popupsContainer.setHeightPolicy(SizePolicy.Fixed);
        popupsContainer.setHeight(100);
        popupsContainer.setSpacing(30, 0);
        popupsContainer.setContentAlignment(ItemAlignment.HCenter);

        Prototype openPopup = ComponentsFactory.makeActionButton("Popup", Palette.BlueLight, () -> {
            PopUpMessage popUpMessage = new PopUpMessage("Message: This is PopUpMesage item.");
            popUpMessage.setTimeOut(1500);
            popUpMessage.show(getHandler());
        });

        SideArea sideArea = new SideArea(getHandler(), Side.Right);
        sideArea.setAreaSize(350);
        Prototype openSideArea = ComponentsFactory.makeActionButton("SideArea", Palette.GreenLight, () -> {
            sideArea.setAttachSide(Side.Right);
            sideArea.show();
        });

        FloatItem floatItem = new FloatItem(getHandler());
        floatItem.setPassEvents(false);
        floatItem.setSize(300, 250);
        floatItem.setBackground(Palette.Green);
        floatItem.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
        Prototype openFloatItem = ComponentsFactory.makeActionButton("FloatItem", Palette.Orange, () -> {
            floatItem.show();
        });
        Prototype hideFloatItem = ComponentsFactory.makeActionButton("Close", Palette.Gray, () -> {
            floatItem.hide();
        });

        addItems(ComponentsFactory.makeHeaderLabel("Context menu implementation:"), contextMenuContainer,
                ComponentsFactory.makeHeaderLabel("ComboBox implementation:"), comboBox,
                ComponentsFactory.makeHeaderLabel("Popup implementations:"), popupsContainer);
        contextMenuContainer.addItems(openContextMenuL, openContextMenuPositioned, openContextMenuR);
        popupsContainer.addItems(openPopup, openSideArea, openFloatItem);

        // Decorate our ComboBox with a white dot
        comboBox.addItem(ComponentsFactory.makeDot());

        // Optionally: set start index (this method should only be called if ComboBox
        // has already been added)
        comboBox.setCurrentIndex(0);

        floatItem.addItem(hideFloatItem);
    }
}
