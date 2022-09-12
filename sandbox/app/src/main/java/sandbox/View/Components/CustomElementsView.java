package sandbox.View.Components;

import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.Palette;

public class CustomElementsView extends ListBox {
    @Override
    public void initElements() {
        super.initElements();
        setBackground(Palette.Transparent);
        setSelectionVisible(false);
        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 10);

        HorizontalStack blockNotes = new HorizontalStack();
        blockNotes.setBackground(Palette.Glass);
        blockNotes.setHeightPolicy(SizePolicy.Fixed);
        blockNotes.setHeight(350);
        blockNotes.setContentAlignment(ItemAlignment.HCenter);
        blockNotes.setSpacing(20, 0);
        blockNotes.setPadding(20, 20, 20, 20);

        BlockNote bn1 = new BlockNote();
        bn1.isLocked = true;
        bn1.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        bn1.setWidth(300);
        BlockNote bn2 = new BlockNote();
        bn2.isLocked = true;
        bn2.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        bn2.setWidth(300);

        addItems(ComponentsFactory.makeHeaderLabel("Custom element - BlockNote:"), blockNotes,
                ComponentsFactory.makeHeaderLabel("Custom element - VisualContact:"), new VisualContact("Jonh Smith"),
                new VisualContact("Rebecca Starling"), new VisualContact("Tom the Cat"));
        blockNotes.addItems(bn1, bn2);
    }
}
