package sandbox.View.Components;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import sandbox.View.Decorations.Palette;

import java.awt.Color;
import java.awt.Font;

public class VisualContact extends Prototype {
    private Label contactName = null;

    public VisualContact(String name) {
        setBackground(80, 80, 80);
        setAlignment(ItemAlignment.Top, ItemAlignment.Left);
        setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        setHeight(80);
        setBorderRadius(10);
        ItemState hover = new ItemState();
        hover.background = new Color(255, 255, 255, 15);
        addItemState(ItemStateType.Hovered, hover);
        setPadding(10, 5, 5, 5);
        setMargin(1, 1, 1, 1);
        effects().add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

        contactName = new Label(name);
    }

    @Override
    public void initElements() {
        Ellipse contactPhoto = new Ellipse();
        contactPhoto.setBackground(Palette.GreenLight);
        contactPhoto.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        contactPhoto.setSize(70, 70);
        contactPhoto.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        contactPhoto.quality = 32;
        contactPhoto.effects().add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

        // contact name
        contactName.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        contactName.setFontSize(16);
        contactName.setFontStyle(Font.BOLD);
        contactName.setBackground(255, 255, 255, 32);
        contactName.setForeground(210, 210, 210);
        contactName.setHeight(40);
        contactName.setHeightPolicy(SizePolicy.Fixed);
        contactName.setWidthPolicy(SizePolicy.Expand);
        contactName.setPadding(20, 0, 0, 0);
        contactName.setMargin(80, 0, 15, 10);
        contactName.setAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
        contactName.setBorderRadius(20);

        // contact close
        ButtonCore removeContactBtn = new ButtonCore();
        removeContactBtn.setBackground(40, 40, 40);
        removeContactBtn.setWidth(14);
        removeContactBtn.setWidthPolicy(SizePolicy.Fixed);
        removeContactBtn.setHeight(14);
        removeContactBtn.setHeightPolicy(SizePolicy.Fixed);
        removeContactBtn.setAlignment(ItemAlignment.Top, ItemAlignment.Right);
        removeContactBtn.setMargin(0, 5, 0, 0);
        removeContactBtn.setCustomFigure(new Figure(false, GraphicsMathService.getCross(14, 14, 4, 45)));
        removeContactBtn.addItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
        removeContactBtn.eventMouseClick.add((sender, args) -> disposeSelf());

        addItems(contactPhoto, contactName, removeContactBtn);
    }

    public void disposeSelf() {
        getParent().removeItem(this);
    }
}