package sandbox.View.Components;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.Triangle;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IEffect;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.SubtractFigure;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import sandbox.View.Decorations.Palette;

public class EffectsView extends ListBox {
    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);
        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 10);

        addItems(
            ComponentsFactory.makeHeaderLabel("Shadow effect:"),
            new ShadowView(),
            ComponentsFactory.makeHeaderLabel("Subtract effect:"),
            new SubtractView(),
            ComponentsFactory.makeHeaderLabel("Border effect:"),
            new BorderView()
        );
    }

    class ShadowView extends HorizontalStack {
        @Override
        public void initElements() {
            super.initElements();

            setBackground(Palette.Glass);
            setHeightPolicy(SizePolicy.Fixed);
            setHeight(180);
            setContentAlignment(ItemAlignment.HCenter);
            setSpacing(50, 0);
            
            Ellipse ellipse = new Ellipse(32);
            ellipse.setBackground(Palette.Blue);
            ellipse.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            ellipse.setSize(100, 100);
            ellipse.effects().add(new Shadow(10, Palette.Blue));
            
            Triangle triangle = new Triangle();
            triangle.setBackground(Palette.Green);
            triangle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            triangle.setSize(100, 100);
            triangle.effects().add(new Shadow(10, new Position(20, 20), Palette.BlackShadow));
            
            Rectangle rectangle = new Rectangle(new CornerRadius(20));
            rectangle.setBackground(Palette.Purple);
            rectangle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            rectangle.setSize(100, 100);
            rectangle.effects().add(new Shadow(10, new Size(20, 20), Palette.BlackShadow));

            addItems(ellipse, rectangle, triangle);
        }
    }

    class SubtractView extends Frame {
        @Override
        public void initElements() {
            setBackground(Palette.Glass);
            setHeightPolicy(SizePolicy.Fixed);
            setHeight(200);

            int diameter = 100;

            IBaseItem cRed = getCircle(diameter, new Color(255, 94, 94));
            IBaseItem cGreen = getCircle(diameter, new Color(16, 180, 111));
            IBaseItem cBlue = getCircle(diameter, new Color(10, 162, 232));
            
            setCircleAlignment(cRed, ItemAlignment.Top);
            setCircleAlignment(cGreen, ItemAlignment.Left, ItemAlignment.Bottom);
            setCircleAlignment(cBlue, ItemAlignment.Right, ItemAlignment.Bottom);

            addItems(cRed, cGreen, cBlue);

            cRed.effects().add(getCircleEffect(cRed, cBlue));
            cGreen.effects().add(getCircleEffect(cGreen, cRed));
            cBlue.effects().add(getCircleEffect(cBlue, cGreen));

            cRed.effects().add(getCircleCenterEffect(cRed));
            cGreen.effects().add(getCircleCenterEffect(cGreen));
            cBlue.effects().add(getCircleCenterEffect(cBlue));

        }

        private IBaseItem getCircle(int diameter, Color color) {
            Ellipse circle = new Ellipse(64);
            circle.setSize(diameter, diameter);
            circle.setBackground(color);
            circle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            circle.effects().add(new Shadow(5, new Size(2, 2), Palette.BlackShadow));
            return circle;
        }

        private void setCircleAlignment(IBaseItem circle, ItemAlignment... alignment) {
            List<ItemAlignment> list = Arrays.asList(alignment);
    
            int offset = circle.getWidth() / 3;
    
            if (list.contains(ItemAlignment.Top)) {
                circle.setMargin(circle.getMargin().left, circle.getMargin().top - offset + 10, circle.getMargin().right,
                        circle.getMargin().bottom);
            }
    
            if (list.contains(ItemAlignment.Bottom)) {
                circle.setMargin(circle.getMargin().left, circle.getMargin().top, circle.getMargin().right,
                        circle.getMargin().bottom - offset);
            }
    
            if (list.contains(ItemAlignment.Left)) {
                circle.setMargin(circle.getMargin().left - offset, circle.getMargin().top, circle.getMargin().right,
                        circle.getMargin().bottom);
            }
    
            if (list.contains(ItemAlignment.Right)) {
                circle.setMargin(circle.getMargin().left, circle.getMargin().top, circle.getMargin().right - offset,
                        circle.getMargin().bottom);
            }
        }

        private IEffect getCircleEffect(IBaseItem circle, IBaseItem subtract) {
            int diameter = circle.getHeight();
            float scale = 1.1f;
            int diff = (int) (diameter * scale - diameter) / 2;
            int xOffset = subtract.getX() - circle.getX() - diff;
            int yOffset = subtract.getY() - circle.getY() - diff;
    
            SubtractFigure effect = new SubtractFigure(
                    new Figure(false, GraphicsMathService.getEllipse(diameter, diameter, 0, 0, 64)));
            effect.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            effect.setSizeScale(scale, scale);
            effect.setPositionOffset(xOffset, yOffset);
    
            return effect;
        }

        private SubtractFigure getCircleCenterEffect(IBaseItem circle) {
            float scale = 0.4f;
            int diameter = (int) (circle.getHeight() * scale);
            SubtractFigure effect = new SubtractFigure(
                    new Figure(true, GraphicsMathService.getEllipse(diameter, diameter, 0, 0, 64)));
            effect.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            return effect;
        }
    }

    class BorderView extends HorizontalStack {
        @Override
        public void initElements() {
            super.initElements();

            setBackground(Palette.Glass);
            setHeightPolicy(SizePolicy.Fixed);
            setHeight(180);
            setContentAlignment(ItemAlignment.HCenter);
            setSpacing(50, 0);

            BlankItem border1 = new BlankItem();
            border1.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border1.setSize(100, 100);
            border1.setBackground(Palette.Blue);
            border1.setBorder(new Border(Palette.Green, new CornerRadius(20), 10));
            border1.effects().add(new Shadow(8, new Position(0, 5), Palette.BlackShadow));
            
            BlankItem border2 = new BlankItem();
            border2.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border2.setSize(140, 100);
            border2.setBackground(Palette.Blue);
            border2.setBorder(new Border(Palette.Green, new CornerRadius(50, 10, 10, 50), 2));
            border2.effects().add(new Shadow(8, new Position(0, 5), Palette.BlackShadow));
            
            BlankItem border3 = new BlankItem();
            border3.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border3.setSize(100, 100);
            border3.setBackground(Palette.Blue);
            border3.setCustomFigure(new Figure(true, GraphicsMathService.getTriangle(76, 70, 12, 5, 0)));
            border3.setBorder(new Border(Palette.Green, new CornerRadius(50, 50, 50, 50), 4));
            border3.effects().add(new Shadow(10, new Position(0, 5), Palette.BlackShadow));

            addItems(border1, border2, border3);
        }
    }
}
