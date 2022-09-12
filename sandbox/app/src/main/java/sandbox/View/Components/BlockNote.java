package sandbox.View.Components;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.ButtonToggle;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.CustomShape;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.ResizableItem;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.VerticalScrollBar;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Flags.VisibilityPolicy;
import sandbox.View.Decorations.Palette;
import java.awt.Color;

public class BlockNote extends ResizableItem {

    private ButtonCore paletteBtn;
    private ButtonToggle lockToggableBtn;
    private TextArea textArea;
    private Label note;
    private ButtonCore closeBtn;
    private ContextMenu colorMenu;

    public BlockNote() {
        setPassEvents(false);
        setBackground(45, 45, 45);
        setPadding(4, 4, 4, 4);
        setBorderRadius(4);
        effects().add(new Shadow(10, new Position(3, 3), Palette.BlackShadow));

        paletteBtn = new ButtonCore();
        paletteBtn.setPassEvents(false);
        lockToggableBtn = new ButtonToggle();
        textArea = new TextArea();
        note = new Label();
    }

    @Override
    public void initElements() {
        paletteBtn.setAlignment(ItemAlignment.Right, ItemAlignment.Top);
        paletteBtn.setMargin(0, 40, 0, 0);
        paletteBtn.setSize(20, 15);
        paletteBtn.setBackground(255, 128, 128);
        paletteBtn.setBorderRadius(0);
        paletteBtn.setBorderRadius(3);
        CustomShape arrow = new CustomShape();
        arrow.setTriangles(GraphicsMathService.getTriangle(30, 30, 0, 0, 180));
        arrow.setBackground(50, 50, 50);
        arrow.setSize(14, 6);
        arrow.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        arrow.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        lockToggableBtn.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        lockToggableBtn.setSize(15, 15);
        lockToggableBtn.setBorderRadius(8);
        lockToggableBtn.eventToggle.add((sender, args) -> {
            isLocked = !isLocked;
            textArea.setEditable(!textArea.isEditable());
        });

        VerticalScrollBar vs = textArea.vScrollBar;
        vs.slider.handler.removeAllItemStates();
        vs.setArrowsVisible(false);
        vs.setBackground(151, 203, 255);
        vs.setPadding(0, 2, 0, 2);
        vs.slider.track.setBackground(0, 0, 0, 0);
        vs.slider.handler.setBorderRadius(3);
        vs.slider.handler.setBackground(80, 80, 80, 255);
        vs.slider.handler.setMargin(new Indents(5, 0, 5, 0));

        textArea.setBorderRadius(new CornerRadius(3));
        textArea.setWrapText(true);
        textArea.setHScrollBarPolicy(VisibilityPolicy.Never);
        textArea.setHeight(25);
        textArea.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        textArea.setBackground(151, 203, 255);
        textArea.setMargin(0, 60, 0, 0);

        note.setForeground(180, 180, 180);
        note.setHeight(25);
        note.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        note.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        note.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        note.setText("Add a Note:");
        note.setMargin(0, 30, 0, 0);

        closeBtn = new ButtonCore();
        closeBtn.setBackground(100, 100, 100);
        closeBtn.setItemName("Close_" + getItemName());
        closeBtn.setSize(10, 10);
        closeBtn.setMargin(0, 0, 0, 0);
        closeBtn.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        closeBtn.setAlignment(ItemAlignment.Top, ItemAlignment.Right);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 80));
        closeBtn.addItemState(ItemStateType.Hovered, hovered);
        closeBtn.setCustomFigure(new Figure(false, GraphicsMathService.getCross(10, 10, 3, 45)));
        closeBtn.eventMouseClick.add((sender, args) -> Dispose());

        addItems(lockToggableBtn, note, textArea, paletteBtn, closeBtn);

        paletteBtn.addItem(arrow);

        colorMenu = new ContextMenu(getHandler());
        colorMenu.setBackground(60, 60, 60);
        MenuItem red = new MenuItem("Red");
        red.setForeground(210, 210, 210);
        red.eventMouseClick.add((sender, args) -> {
            textArea.setBackground(255, 196, 196);
            textArea.vScrollBar.setBackground(textArea.getBackground());
        });
        MenuItem green = new MenuItem("Green");
        green.setForeground(210, 210, 210);
        green.eventMouseClick.add((sender, args) -> {
            textArea.setBackground(138, 255, 180);
            textArea.vScrollBar.setBackground(textArea.getBackground());
        });
        MenuItem blue = new MenuItem("Blue");
        blue.setForeground(210, 210, 210);
        blue.eventMouseClick.add((sender, args) -> {
            textArea.setBackground(151, 203, 255);
            textArea.vScrollBar.setBackground(textArea.getBackground());
        });
        MenuItem yellow = new MenuItem("Yellow");
        yellow.setForeground(210, 210, 210);
        yellow.eventMouseClick.add((sender, args) -> {
            textArea.setBackground(234, 232, 162);
            textArea.vScrollBar.setBackground(textArea.getBackground());
        });
        colorMenu.addItems(red, green, blue, yellow);
        paletteBtn.eventMouseClick.add((sender, args) -> colorMenu.show(sender, args));
        colorMenu.activeButton = MouseButton.ButtonLeft;
    }

    public void Dispose() {
        getParent().removeItem(this);
    }
}