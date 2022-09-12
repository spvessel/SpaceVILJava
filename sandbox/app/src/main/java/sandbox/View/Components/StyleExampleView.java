package sandbox.View.Components;

import java.awt.Font;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import sandbox.View.Decorations.Palette;

public class StyleExampleView extends VerticalStack {
    public StyleExampleView() {
        // add style for our StyledComponent class to the current theme
        DefaultsService.getDefaultTheme().addDefaultCustomItemStyle(StyledComponent.class, getStyledComponentStyle());
    }

    @Override
    public void initElements() {
        super.initElements();
        // adding default StyledComponent
        addItems(new StyledComponent(), new StyledComponent());

        // changing default registered style for StyleExampleView
        // get style from current theme
        var styledComponentStyle = DefaultsService.getDefaultTheme().getThemeStyle(StyledComponent.class);
        if (styledComponentStyle != null) {
            styledComponentStyle.setAlignment(ItemAlignment.Right, ItemAlignment.VCenter);
            styledComponentStyle.setBorder(Palette.White, new CornerRadius(30, 3, 30, 3), 2);
            styledComponentStyle.background = Palette.Blue;
            styledComponentStyle.maxWidth = 280;
            styledComponentStyle.addItemState(ItemStateType.Hovered, new ItemState(Palette.WhiteGlass));
            var textStyle = styledComponentStyle.getInnerStyle("text");
            if (textStyle != null) {
                textStyle.setTextAlignment(ItemAlignment.Right, ItemAlignment.VCenter);
                textStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 24);
            }
        }

        // adding StyledComponent with changed global style
        addItems(new StyledComponent(), new StyledComponent());

        // setting style manualy for current item
        var manuallyStyledItem = new StyledComponent();

        // style: an easier way is to get the current style from the theme and change it
        var newStyle = DefaultsService.getDefaultStyle(StyledComponent.class);
        newStyle.background = Palette.Purple;
        newStyle.setBorder(Palette.White, new CornerRadius(30, 30, 30, 30), 2);
        //setting style
        manuallyStyledItem.setStyle(newStyle);

        addItem(manuallyStyledItem);
    }

    private class StyledComponent extends Prototype {
        private Label _text = new Label("StyledComponent");
        public StyledComponent() {
            setStyle(DefaultsService.getDefaultStyle(StyledComponent.class));
        }

        @Override
        public void initElements() {
            super.initElements();
            addItem(_text);
        }

        @Override
        public void setStyle(Style style) {
            if (style == null)
                return;
            super.setStyle(style);

            var innerStyle = style.getInnerStyle("text");
            if (innerStyle != null) {
                _text.setStyle(innerStyle);
            }
        }
    }

    public static Style getStyledComponentStyle() {
        Style style = new Style();
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        style.height = 60;
        style.setSpacing(0, 5);
        style.setMargin(100, 10, 100, 10);
        style.setPadding(5, 5, 5, 5);
        style.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.background = Palette.Green;
        style.setShadow(new Shadow(5, Palette.BlackShadow));
        style.setBorder(Palette.White, new CornerRadius(10, 20, 30, 0), 2);

        // inner styles: StyledComponent consist of Label
        // style for Label
        Style textStyle = new Style();
        textStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        textStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        textStyle.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        textStyle.height = 60;
        textStyle.foreground = Palette.Black;
        textStyle.background = Palette.Transparent;
        textStyle.font = DefaultsService.getDefaultFont(Font.BOLD, 24);
        style.addInnerStyle("text", textStyle);
        
        return style;
    }
}
