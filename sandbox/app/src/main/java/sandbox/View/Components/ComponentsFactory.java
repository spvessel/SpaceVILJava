package sandbox.View.Components;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.PopUpMessage;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.VerticalSplitArea;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Core.ICommonMethod;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import sandbox.View.Decorations.Palette;
import sandbox.View.Decorations.StyleFactory;

public class ComponentsFactory {
    private ComponentsFactory() {
    }

    public static Prototype makeTitleBar(String title, BufferedImage image) {
        TitleBar titleBar = new TitleBar(title);
        titleBar.effects().add(new Shadow(8));
        titleBar.setIcon(image, 24, 24);
        return titleBar;
    }

    public static Prototype makeVerticalStack(int spacing) {
        VerticalStack stack = new VerticalStack();
        stack.setBackground(55, 55, 55);
        stack.setSpacing(0, spacing);
        return stack;
    }

    public static Prototype makeFrame() {
        return new Frame();
    }

    public static VerticalSplitArea makeSplitArea(int position) {
        VerticalSplitArea splitArea = new VerticalSplitArea();
        splitArea.setSplitPosition(300);
        splitArea.setSplitColor(Palette.Gray);
        splitArea.SetSplitThickness(4);
        return splitArea;
    }

    public static ListBox makeList() {
        ListBox list = new ListBox();
        list.setBackground(50, 50, 50);
        list.setSelectionVisible(false);
        list.getArea().setPadding(10, 30, 10, 5);
        list.getArea().setSpacing(0, 16);
        list.setMinWidth(100);
        return list;
    }

    public static ButtonCore makeActionButton(String text, ICommonMethod action) {
        return makeActionButton(text, Palette.Orange, action);
    }

    public static ButtonCore makeActionButton(String text, Color color, ICommonMethod action) {
        ButtonCore button = new ButtonCore(text);
        button.setBackground(color);
        button.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        button.setMaxWidth(220);
        button.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        button.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
        button.eventMouseClick.add((sender, args) -> {
            action.execute();
        });
        return button;
    }

    public static Label makeHeaderLabel(String text) {
        Label label = new Label(text);
        label.setFontSize(24);
        label.setMargin(2, 30, 2, 0);
        label.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        label.setHeight(label.getTextHeight() + 20);
        label.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        return label;
    }
    
    public static Prototype makeFixedLabel(String text, int width, int height) {
        Label label = new Label(text);
        label.setFontSize(18);
        label.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        label.setSize(width, height);
        label.setBackground(Palette.GreenLight);
        label.setForeground(Palette.Black);
        label.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        label.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
        return label;
    }
    
    public static Prototype makeExpandedLabel(String text) {
        Label label = new Label(text);
        label.setFontSize(18);
        label.setBackground(Palette.GreenLight);
        label.setForeground(Palette.Black);
        label.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        label.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
        return label;
    }

    public static MenuItem makeMenuItem(String text, ICommonMethod action) {
        MenuItem mi = new MenuItem(text);
        mi.eventMouseClick.add((sender, args) -> {
            action.execute();
        });
        return mi;
    }

    public static ContextMenu makeContextMenu(CoreWindow handler, MenuItem ... items) {
        ContextMenu contextMenu = new ContextMenu(handler, items);
        contextMenu.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));
        return contextMenu;
    }

    public static MenuItem makeImagedMenuItem(String name, BufferedImage bitmap) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setStyle(StyleFactory.getMenuItemStyle());
        menuItem.setTextMargin(new Indents(25, 0, 0, 0));

        // Optionally: set an event on click
        menuItem.eventMouseClick.add((sender, args) -> {
            PopUpMessage popUpInfo = new PopUpMessage("You choosed a function:\n" + menuItem.getText());
            popUpInfo.setStyle(StyleFactory.getBluePopUpStyle());
            popUpInfo.setTimeOut(2000);
            popUpInfo.show(menuItem.getHandler());
        });

        // Optionally: add an image into MenuItem
        ImageItem img = new ImageItem(bitmap, false);
        img.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        img.setSize(20, 20);
        img.setAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        img.keepAspectRatio(true);
        menuItem.addItem(img);

        // Optionally: add a button into MenuItem
        ButtonCore infoBtn = new ButtonCore("?");
        infoBtn.setBackground(40, 40, 40);
        infoBtn.setWidth(20);
        infoBtn.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        infoBtn.setFontStyle(Font.BOLD);
        infoBtn.setForeground(210, 210, 210);
        infoBtn.setAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
        infoBtn.setMargin(0, 0, 10, 0);
        infoBtn.setBorderRadius(3);
        infoBtn.addItemState(ItemStateType.Hovered, new ItemState(new Color(10, 140, 210)));
        infoBtn.setPassEvents(false, InputEventType.MousePress, InputEventType.MouseRelease,
                InputEventType.MouseDoubleClick);
        infoBtn.isFocusable = false; // prevent focus this button
        infoBtn.eventMouseClick.add((sender, args) -> {
            PopUpMessage popUpInfo = new PopUpMessage("This is decorated MenuItem:\n" + menuItem.getText());
            popUpInfo.setStyle(StyleFactory.getDarkPopUpStyle());
            popUpInfo.setTimeOut(2000);
            popUpInfo.show(infoBtn.getHandler());
        });
        menuItem.addItem(infoBtn);

        return menuItem;
    }

    public static Ellipse makeDot() {
        Ellipse ellipse = new Ellipse(12);
        ellipse.setSize(8, 8);
        ellipse.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        ellipse.setMargin(10, 0, 0, 0);
        return ellipse;
    }

    public static Prototype makeVSpacer(int thickness) {
        BlankItem spacer = new BlankItem();
        spacer.setHeightPolicy(SizePolicy.Expand);
        spacer.setWidth(thickness);
        spacer.setBackground(Palette.WhiteGlass);
        return spacer;
    }

    public static Prototype makeHSpacer(int thickness) {
        BlankItem spacer = new BlankItem();
        spacer.setWidthPolicy(SizePolicy.Expand);
        spacer.setHeight(thickness);
        spacer.setBackground(Palette.WhiteGlass);
        return spacer;
    }
}
