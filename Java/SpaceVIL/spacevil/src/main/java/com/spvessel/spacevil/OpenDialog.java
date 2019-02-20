package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

public abstract class OpenDialog extends DialogItem {

    private TitleBar titleBar;

    public OpenDialog() {
        setItemName("OpenDialog_" + count);
        count++;
        titleBar = new TitleBar();
    }

    @Override
    public void initElements() {
        // important!
        super.initElements();

        window.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        window.setMargin(50, 20, 50, 20);

        titleBar.getMaximizeButton().setVisible(false);

        // adding toolbar
        window.addItems(titleBar);

        titleBar.getCloseButton().eventMouseClick.clear();
        titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
    }

    @Override
    public void show(WindowLayout handler) {
        super.show(handler);
    }

    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    @Override
    public void setStyle(Style style) {

    }
}