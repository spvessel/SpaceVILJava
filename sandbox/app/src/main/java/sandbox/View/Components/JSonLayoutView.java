package sandbox.View.Components;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Flags.ItemAlignment;

import sandbox.View.Decorations.Palette;

public class JSonLayoutView extends Frame {
    private ActiveWindow jsonWindow = null;
    @Override
    public void initElements() {
        super.initElements();

        ButtonCore jsonBtn = new ButtonCore("Run JSon based window");
        jsonBtn.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        jsonBtn.setSize(200, 46);
        jsonBtn.setBorderRadius(6);
        jsonBtn.setBackground(Palette.Purple);
        jsonBtn.eventMouseClick.add((sender, args) -> {
            if (jsonWindow == null) {
                jsonWindow = new JSonWindow();
            }
            jsonWindow.show();
        });

        addItem(jsonBtn);
    }
}
