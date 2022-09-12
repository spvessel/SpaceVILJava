package sandbox.View.Components;

import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.SelectionItem;
import com.spvessel.spacevil.TextView;
import com.spvessel.spacevil.Flags.SizePolicy;

public class TextContentListWrapper extends Frame {
    TextView textItem = null;
    ListBox list = null;

    public TextContentListWrapper(ListBox list, TextView textItem) {
        this.list = list;
        this.textItem = textItem;
    }

    public void setText(String text) {
        textItem.setText(text);
    }

    @Override
    public void initElements() {
        setBackground(0, 0, 0, 40);
        setHeightPolicy(SizePolicy.Fixed);
        addItem(textItem);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        int wrapperHeight = textItem.getTextHeight() + 20;
        setHeight(wrapperHeight);
        SelectionItem wrapper = list.getWrapper(this);
        if (wrapper != null) {
            wrapper.updateHeight();
            list.updateElements();
        }
    }
}
