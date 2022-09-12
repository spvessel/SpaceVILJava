package sandbox.View.Components;

import java.awt.Font;
import java.io.File;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.PasswordLine;
import com.spvessel.spacevil.SpinItem;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.TextEdit;
import com.spvessel.spacevil.TextView;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.Palette;

public class TextAndInputView extends ListBox {
    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);
        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 10);

        Label simpleLabel = new Label("Label - Font: Ubuntu, Size: 12, Style: Regular.");
        simpleLabel.setHeightPolicy(SizePolicy.Fixed);
        simpleLabel.setHeight(simpleLabel.getTextHeight() + 6);
        simpleLabel.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        Label italicLabel = new Label("Label - Font: Ubuntu, Size: 16, Style: Italic.");
        italicLabel.setFontSize(16);
        italicLabel.setFontStyle(Font.ITALIC);
        italicLabel.setHeightPolicy(SizePolicy.Fixed);
        italicLabel.setHeight(italicLabel.getTextHeight() + 6);
        italicLabel.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        Label boldLabel = new Label("Label - Font: Ubuntu, Size: 16, Style: Bold.");
        boldLabel.setFontSize(16);
        boldLabel.setFontStyle(Font.BOLD);
        boldLabel.setHeightPolicy(SizePolicy.Fixed);
        boldLabel.setHeight(boldLabel.getTextHeight() + 6);
        boldLabel.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        Font amaticLocalFont = null;
        try {
            amaticLocalFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/AmaticSC-Regular.ttf"))
                    .deriveFont(30f);

        } catch (Exception e) {
        }
        Label amaticLabel = new Label(
                "Label - Font: AmaticSC, Size: 30, Style: Regular.\nAlmost before we knew it, we had left the ground.");
        amaticLabel.setFont(amaticLocalFont);
        amaticLabel.setHeightPolicy(SizePolicy.Fixed);
        amaticLabel.setHeight(amaticLabel.getTextHeight() + 6);
        amaticLabel.setTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        TextView textView = new TextView();
        textView.setHeightPolicy(SizePolicy.Fixed);
        textView.setText(getTextForElements());

        SpinItem spinItem = new SpinItem();
        spinItem.setPassEvents(false);
        spinItem.setAccuracy(1);
        spinItem.setMaxWidth(400);
        spinItem.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        spinItem.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

        TextEdit textEdit = new TextEdit();
        textEdit.setPassEvents(false);
        textEdit.setSubstrateText("Login:");
        textEdit.setMaxWidth(400);
        textEdit.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        textEdit.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

        PasswordLine passwordLine = new PasswordLine();
        passwordLine.setPassEvents(false);
        passwordLine.setSubstrateText("Password:");
        passwordLine.setMaxWidth(400);
        passwordLine.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        passwordLine.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

        TextArea textArea = new TextArea(getTextForElements());
        textArea.setPassEvents(false);
        textArea.setWrapText(true);
        textArea.setHeightPolicy(SizePolicy.Fixed);
        textArea.setMaxWidth(400);
        textArea.setHeight(200);
        textArea.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        textArea.effects().add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

        addItems(ComponentsFactory.makeHeaderLabel("Label implementation:"), simpleLabel, italicLabel, boldLabel, amaticLabel,
                ComponentsFactory.makeHeaderLabel("TextView implementation:"), textView,
                ComponentsFactory.makeHeaderLabel("Text input elements:"), spinItem, textEdit, passwordLine, textArea);
    }

    private String getTextForElements() {
        return "The quick brown fox jumps over the lazy dog. " + "The five boxing wizards jump quickly. "
                + "Pack my box with five dozen liquor jugs.\n" + "Jived fox nymph grabs quick waltz. "
                + "Glib jocks quiz nymph to vex dwarf. " + "Sphinx of black quartz, judge my vow.";
    }
}
