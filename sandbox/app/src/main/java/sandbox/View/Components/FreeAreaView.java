package sandbox.View.Components;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.FreeArea;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.Primitive;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.ResizableItem;
import com.spvessel.spacevil.Triangle;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.ICommonMethod;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.SizePolicy;

import sandbox.View.Decorations.Palette;

public class FreeAreaView extends Frame {

    List<IBaseItem> content = new ArrayList<>();

    void unfocusContent() {
        if (!content.isEmpty()) {
            Prototype focusedItem = ((Prototype) content.get(content.size() - 1));
            focusedItem.setBorderThickness(0);
        }
    }

    @Override
    public void initElements() {
        super.initElements();

        FreeArea area = new FreeArea();
        area.setMargin(2, 2, 2, 2);
        area.setBackground(Palette.Transparent);
        area.eventMousePress.add((sender, args) -> {
            unfocusContent();
        });

        Toolbar toolbar = new Toolbar(area);
        addItems(area, toolbar);
    }

    private class Toolbar extends BlankItem {
        FreeArea view;
        ToolbarPalette palette = new ToolbarPalette();

        Toolbar(FreeArea view) {
            this.view = view;
            setBackground(Palette.GrayDark);
            setBorderRadius(3);
            setBorder(new Border(Palette.WhiteGlass, new CornerRadius(3), 1));
            effects().add(new Shadow(8, Palette.BlackShadow));
            setAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
            setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            setSize(256, 46);
            setMargin(0, 10, 0, 0);
            setPadding(6, 4, 4, 4);
            setPassEvents(false);
        }

        @Override
        public void initElements() {
            super.initElements();
            HorizontalStack layout = new HorizontalStack();
            layout.setSpacing(5, 0);
            addItem(layout);
            
            ToolbarButton btnTriangle = new ToolbarButton(new Triangle(), view);
            btnTriangle.action = () -> {
                unfocusContent();
                view.addItem(getContainer(applyLAF(new Triangle(), palette.getColor())));
            };
            ToolbarButton btnRectangle = new ToolbarButton(new Rectangle(), view);
            btnRectangle.action = () -> {
                unfocusContent();
                view.addItem(getContainer(applyLAF(new Rectangle(new CornerRadius(6)), palette.getColor())));
            };
            ToolbarButton btnEllipse = new ToolbarButton(new Ellipse(), view);
            btnEllipse.action = () -> {
                unfocusContent();
                view.addItem(getContainer(applyLAF(new Ellipse(64), palette.getColor())));
            };

            
            layout.addItems(
                btnTriangle,
                btnRectangle,
                btnEllipse,
                ComponentsFactory.makeFrame(),
                ComponentsFactory.makeVSpacer(1),
                palette
            );
        }

        private ResizableItem getContainer(Primitive item) {
            ResizableItem container = new ResizableItem() {
                @Override
                public void initElements() {
                    super.initElements();
                    addItem(item);
                }
            };
            container.setBackground(Palette.Transparent);
            container.setSize(200, 200);
            container.setPosition(100, 100);
            container.setPadding(5, 5, 5, 5);
            container.setBorder(new Border(Palette.WhiteGlass, new CornerRadius(3), 1));
            container.eventMousePress.add((s, a) -> {
                unfocusContent();
                content.remove(container);
                content.add(container);
                view.setContent(content);
                container.setBorderThickness(1);
            });
            content.add(container);
            return container;
        }

        private Primitive applyLAF(Primitive primitive, Color color) {
            primitive.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            primitive.setBackground(color);
            return primitive;
        }
    }

    private class ToolbarButton extends BlankItem {
        ICommonMethod action;
        Primitive icon;
        
        ToolbarButton(Primitive icon, FreeArea view) {
            this.icon = icon;
            icon.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            icon.setBackground(Palette.GrayLight);
            setBackground(Palette.GrayDark);
            setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            setSize(32, 32);
            setPadding(6, 6, 6, 6);
            setBorderRadius(3);
            ItemState hovered = new ItemState();
            hovered.background = Palette.Gray;
            hovered.border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            addItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState();
            pressed.background = Palette.Dark;
            pressed.border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            addItemState(ItemStateType.Pressed, pressed);

            eventMouseClick.add((sender, args) -> {
                action.execute();
            });
        }

        @Override
        public void initElements() {
            super.initElements();
            addItem(icon);
        }
    }

    private class ToolbarPalette extends BlankItem {
        private Primitive marker;

        ToolbarPalette() {
            setBackground(Palette.GrayDark);
            setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            setSize(32, 32);
            setPadding(6, 6, 6, 6);
            setBorderRadius(3);
            ItemState hovered = new ItemState();
            hovered.background = Palette.Gray;
            hovered.border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            addItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState();
            pressed.background = Palette.Dark;
            pressed.border = new Border(Palette.WhiteGlass, new CornerRadius(3), 1);
            addItemState(ItemStateType.Pressed, pressed);
            marker = new Ellipse();
            marker.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        }
        
        @Override
        public void initElements() {
            super.initElements();
            setColor(Palette.Blue);
            addItem(marker);

            ContextMenu menu = ComponentsFactory.makeContextMenu(
                getHandler(),
                ComponentsFactory.makeMenuItem("Red", () -> { setColor(Palette.RedLight); }),
                ComponentsFactory.makeMenuItem("Green", () -> { setColor(Palette.Green); }),
                ComponentsFactory.makeMenuItem("Blue", () -> { setColor(Palette.Blue); }),
                ComponentsFactory.makeMenuItem("Pink", () -> { setColor(Palette.Pink); }),
                ComponentsFactory.makeMenuItem("Purple", () -> { setColor(Palette.Purple); }),
                ComponentsFactory.makeMenuItem("Orange", () -> { setColor(Palette.Orange); })
            );
            menu.activeButton = MouseButton.ButtonLeft;
            setToolTip("Palette");

            eventMouseClick.add((sender, args) -> {
                args.position.setPosition(getX(), getY() + getHeight());
                menu.show(sender, args);
            });
        }

        void setColor(Color color) {
            marker.setBackground(color);
        }

        Color getColor() {
            return marker.getBackground();
        }
    }
}
