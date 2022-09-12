package sandbox.View.Components;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.ButtonToggle;
import com.spvessel.spacevil.CheckBox;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.RadioButton;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.Palette;

public class ClickablesAndTogglesView extends ListBox {
    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);

        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 20);

        // Clickables
        ButtonCore button = new ButtonCore("ButtonCore");
        button.eventMouseClick.add((sender, args) -> {
            System.out.println("ButtonCore click!");
        });
        button.setSize(150, 70);
        button.setBackground(Palette.Green);
        button.setBorder(new Border(Palette.White, new CornerRadius(35), 2));
        button.effects().add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));

        BlankItem blankItem = new BlankItem();
        blankItem.eventMouseClick.add((sender, args) -> {
            System.out.println("BlankItem click!");
        });
        blankItem.setSize(150, 70);
        blankItem.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        blankItem.setBackground(Palette.OrangeLight);
        blankItem.setBorder(new Border(Palette.Pink, new CornerRadius(35), 4));
        blankItem.effects().add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));

        HorizontalStack clickables = new HorizontalStack();
        clickables.setHeightPolicy(SizePolicy.Fixed);
        clickables.setHeight(100);
        clickables.setSpacing(30, 0);
        clickables.setContentAlignment(ItemAlignment.HCenter);

        // Toggles
        ButtonToggle toggle = new ButtonToggle("ButtonToggle");
        toggle.eventMouseClick.add((sender, args) -> {
            System.out.println("ButtonToggle is toggled: " + toggle.isToggled());
        });
        toggle.setSize(150, 70);
        toggle.setBackground(Palette.Blue);
        toggle.setBorder(new Border(Palette.White, new CornerRadius(35), 2));
        toggle.effects().add(new Shadow(5, new Position(5, 5), Palette.BlackShadow));
        toggle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        HorizontalStack checkables = new HorizontalStack();
        checkables.setHeightPolicy(SizePolicy.Fixed);
        checkables.setHeight(150);
        checkables.setSpacing(30, 0);
        checkables.setContentAlignment(ItemAlignment.HCenter);

        VerticalStack checkBoxes = new VerticalStack();
        checkBoxes.setSpacing(0, 10);

        VerticalStack radioButtons = new VerticalStack();
        radioButtons.setSpacing(0, 10);

        addItems(ComponentsFactory.makeHeaderLabel("Examples of clickables:"), clickables,
                ComponentsFactory.makeHeaderLabel("Examples of toggles:"), toggle, checkables);
        clickables.addItems(button, blankItem);
        checkables.addItems(checkBoxes, radioButtons);
        checkBoxes.addItems(
            new CheckBox("OpenGL API"),
            new CheckBox("Vulkan API"),
            new CheckBox("DirectX API"),
            new CheckBox("Metal API")
        );
        radioButtons.addItems(
            new RadioButton("Java"),
            new RadioButton("Kotlin"),
            new RadioButton("C#"),
            new RadioButton("C/C++")
        );
    }
}
