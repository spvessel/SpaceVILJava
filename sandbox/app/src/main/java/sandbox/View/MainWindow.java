package sandbox.View;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.VerticalSplitArea;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Common.DisplayService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.MSAA;
import java.awt.image.BufferedImage;
import java.util.List;
import sandbox.View.Components.ClickablesAndTogglesView;
import sandbox.View.Components.ComponentsFactory;
import sandbox.View.Components.FreeAreaView;
import sandbox.View.Components.JSonLayoutView;
import sandbox.View.Components.MenusPopupsAndTooltipsView;
import sandbox.View.Components.PerformanceView;
import sandbox.View.Components.SlidersAndProgressBarsView;
import sandbox.View.Components.StyleExampleView;
import sandbox.View.Components.ContainersView;
import sandbox.View.Components.CustomElementsView;
import sandbox.View.Components.EffectsView;
import sandbox.View.Components.EventsAndRoutingView;
import sandbox.View.Components.TextAndInputView;
import sandbox.View.Components.WindowsAndDialogsView;
import sandbox.View.Components.WorkWithImagesView;
import sandbox.View.Decorations.ImageResources;

public class MainWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        setParameters("Sandbox", "Sandbox");
        isBorderHidden = true;
        isCentered = true;
        setAntiAliasingQuality(MSAA.MSAA8x);
        setPreferredSize(1200, 800);
        setBackground(32, 32, 32);
        BufferedImage iBig = DefaultsService.getDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size64x64);
        BufferedImage iSmall = DefaultsService.getDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size32x32);
        setIcon(iBig, iSmall);
        ImageResources.loadImages();

        Prototype layout = ComponentsFactory.makeVerticalStack(2);
        Prototype title = ComponentsFactory.makeTitleBar(getWindowTitle(), iSmall);
        VerticalSplitArea splitArea = ComponentsFactory.makeSplitArea(300);
        ListBox modes = ComponentsFactory.makeList();
        Prototype viewArea = ComponentsFactory.makeFrame();

        // adding
        addItem(layout);
        layout.addItems(title, splitArea);
        splitArea.setLeftItem(modes);
        splitArea.setRightItem(viewArea);

        setView(viewArea, new WindowsAndDialogsView());

        modes.addItems(
            ComponentsFactory.makeActionButton("Windows & Dialogs", () -> {
                setView(viewArea, new WindowsAndDialogsView());
            }),
            ComponentsFactory.makeActionButton("Containers", () -> {
                setView(viewArea, new ContainersView());
            }),
            ComponentsFactory.makeActionButton("Clickables & Toggles", () -> {
                setView(viewArea, new ClickablesAndTogglesView());
            }),
            ComponentsFactory.makeActionButton("Text & Input", () -> {
                setView(viewArea, new TextAndInputView());
            }),
            ComponentsFactory.makeActionButton("Menus, Popups & Tooltips", () -> {
                setView(viewArea, new MenusPopupsAndTooltipsView());
            }),
            ComponentsFactory.makeActionButton("Sliders & ProgressBars", () -> {
                setView(viewArea, new SlidersAndProgressBarsView());
            }),
            ComponentsFactory.makeActionButton("Work with images", () -> {
                setView(viewArea, new WorkWithImagesView());
            }),
            ComponentsFactory.makeActionButton("Custom Elements", () -> {
                setView(viewArea, new CustomElementsView());
            }),
            ComponentsFactory.makeActionButton("Events & Routing", () -> {
                setView(viewArea, new EventsAndRoutingView());
            }),
            ComponentsFactory.makeActionButton("Effects", () -> {
                setView(viewArea, new EffectsView());
            }),
            ComponentsFactory.makeActionButton("Styling", () -> {
                setView(viewArea, new StyleExampleView());
            }),
            ComponentsFactory.makeActionButton("FreeArea", () -> {
                setView(viewArea, new FreeAreaView());
            }),
            ComponentsFactory.makeActionButton("JSon Layout", () -> {
                setView(viewArea, new JSonLayoutView());
            }),
            ComponentsFactory.makeActionButton("Performance", () -> {
                long time = System.nanoTime();
                setView(viewArea, new PerformanceView());
                System.out.println("Performance: " + ((System.nanoTime() - time) / 1000000) + " ms");
            })
        );
    }

    private void setView(Prototype area, Prototype view) {
        List<IBaseItem> items = area.getItems();
        if (!items.isEmpty() && items.get(0).getClass() == view.getClass()) {
            return;
        }
        area.clear();
        area.addItem(view);
    }

    private void setPreferredSize(int width, int height) {
        // right scaling
        int windowWidth = width, windowHeight = height;
        Scale displayScale = DisplayService.getDisplayDpiScale();
        int displayWidth = (int) (DisplayService.getDisplayWidth() * displayScale.getXScale());
        int displayHeight = (int) (DisplayService.getDisplayHeight() * displayScale.getYScale());
        if (windowWidth * displayScale.getXScale() > displayWidth)
            windowWidth = (int) (windowWidth * 0.80);
        if (windowHeight * displayScale.getYScale() > displayHeight)
            windowHeight = (int) (windowHeight * 0.80);

        setSize(windowWidth, windowHeight);
    }
}
