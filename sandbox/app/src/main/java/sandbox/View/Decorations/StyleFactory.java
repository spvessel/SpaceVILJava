package sandbox.View.Decorations;

import java.awt.Color;

import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;

public final class StyleFactory {
    private StyleFactory() {
    }

    public static Style getMenuItemStyle() {
        // get current style of an item and change it
        Style style = Style.getMenuItemStyle();
        style.setBackground(255, 255, 255, 7);
        style.foreground = new Color(210, 210, 210);
        style.border.setRadius(new CornerRadius(3));
        style.addItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
        return style;
    }

    public static Style getComboBoxDropDownStyle() {
        // get current style of an item and change it
        Style style = Style.getComboBoxDropDownStyle();
        style.setBackground(50, 50, 50);
        style.setBorder(new Border(new Color(100, 100, 100), new CornerRadius(0, 0, 5, 5), 1));
        style.setShadow(new Shadow(10, new Position(3, 3), Palette.BlackShadow));
        return style;
    }

    public static Style getComboBoxStyle() {
        // get current style of an item and change it
        Style style = Style.getComboBoxStyle();
        style.setBackground(45, 45, 45);
        style.setForeground(210, 210, 210);
        style.setMaxSize(400, 40);
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setBorder(new Border(new Color(255, 181, 111), new CornerRadius(10, 0, 0, 10), 2));
        style.setShadow(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

        // Note: every complex item has a few inner styles for its children
        // for example ComboBox has drop down area, selection item, drob down button
        // (with arrow)
        // Replace inner style
        style.removeInnerStyle("dropdownarea");
        Style dropDownAreaStyle = getComboBoxDropDownStyle(); // get our own style
        style.addInnerStyle("dropdownarea", dropDownAreaStyle);

        // Change inner style
        Style selectionStyle = style.getInnerStyle("selection");
        if (selectionStyle != null) {
            selectionStyle.border.setRadius(new CornerRadius(10, 0, 0, 10));
            selectionStyle.setBackground(0, 0, 0, 0);
            selectionStyle.setPadding(25, 0, 0, 0);
            selectionStyle.addItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
        }

        // Change inner style
        Style dropDownButtonStyle = style.getInnerStyle("dropdownbutton");
        if (dropDownButtonStyle != null)
            dropDownButtonStyle.border.setRadius(new CornerRadius(0, 0, 0, 10));

        return style;
    }

    public static Style getBluePopUpStyle() {
        // get current style of an item and change it
        Style style = Style.getPopUpMessageStyle();
        style.setBackground(10, 162, 232);
        style.setForeground(0, 0, 0);
        style.height = 60;
        style.border.setRadius(new CornerRadius(12));
        style.setAlignment(ItemAlignment.Bottom, ItemAlignment.Right);
        style.setMargin(0, 0, 50, 50);
        style.setShadow(new Shadow(5, new Position(3, 3), Palette.DarkGlass));

        // Change inner style
        Style closeButtonStyle = style.getInnerStyle("closebutton");
        if (closeButtonStyle != null) {
            closeButtonStyle.setBackground(10, 10, 10, 255);
            closeButtonStyle.addItemState(ItemStateType.Hovered, new ItemState(Palette.White));
        }

        return style;
    }

    public static Style getDarkPopUpStyle() {
        // get current style of an item and change it
        Style style = Style.getPopUpMessageStyle();
        style.height = 60;
        style.setAlignment(ItemAlignment.Bottom, ItemAlignment.Right);
        style.setMargin(0, 0, 50, 50);
        style.setShadow(new Shadow(5, new Position(3, 3), Palette.DarkGlass));
        return style;
    }
}