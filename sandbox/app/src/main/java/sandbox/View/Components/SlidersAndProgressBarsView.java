package sandbox.View.Components;

import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.HorizontalSlider;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.LoadingScreen;
import com.spvessel.spacevil.ProgressBar;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.VerticalSlider;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.WindowManager;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.RedrawFrequency;
import com.spvessel.spacevil.Flags.SizePolicy;
import java.util.concurrent.atomic.AtomicBoolean;
import sandbox.View.Decorations.Palette;

public class SlidersAndProgressBarsView extends ListBox {
    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);
        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 10);

        // Sliders
        Grid slidersContainer = new Grid(1, 2);
        slidersContainer.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        slidersContainer.setSize(430, 200);
        slidersContainer.setSpacing(30, 30);
        slidersContainer.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        VerticalStack horizontalSliders = new VerticalStack();
        horizontalSliders.setSpacing(0, 10);
        horizontalSliders.setContentAlignment(ItemAlignment.HCenter);
        horizontalSliders.setPadding(0, 50, 0, 50);
        HorizontalStack smoothHSliderContainer = new HorizontalStack();
        smoothHSliderContainer.setSpacing(10, 0);
        Label smoothHSliderValue = new Label("0.0");
        smoothHSliderValue.setMaxWidth(30);
        HorizontalSlider smoothHSlider = new HorizontalSlider();
        smoothHSlider.setMaxValue(10);
        smoothHSlider.eventValueChanged.add((sender) -> {
            smoothHSliderValue.setText(String.format("%.2f", smoothHSlider.getCurrentValue()));
        });
        HorizontalStack digitalHSliderContainer = new HorizontalStack();
        digitalHSliderContainer.setSpacing(10, 0);
        Label digitalHSliderValue = new Label("0.0");
        digitalHSliderValue.setMaxWidth(30);
        HorizontalSlider digitalHSlider = new HorizontalSlider();
        digitalHSlider.setMaxValue(10);
        digitalHSlider.setIgnoreStep(false);
        digitalHSlider.setStep(2);
        digitalHSlider.eventValueChanged.add((sender) -> {
            digitalHSliderValue.setText(String.format("%.1f", digitalHSlider.getCurrentValue()));
        });

        HorizontalStack verticalSliders = new HorizontalStack();
        verticalSliders.setSpacing(30, 0);
        verticalSliders.setContentAlignment(ItemAlignment.HCenter);
        verticalSliders.setPadding(50, 0, 50, 0);
        VerticalStack smoothVSliderContainer = new VerticalStack();
        smoothVSliderContainer.setSpacing(0, 10);
        Label smoothVSliderValue = new Label("0.0");
        smoothVSliderValue.setMaxHeight(30);
        VerticalSlider smoothVSlider = new VerticalSlider();
        smoothVSlider.setMaxValue(10);
        smoothVSlider.eventValueChanged.add((sender) -> {
            smoothVSliderValue.setText(String.format("%.2f", smoothVSlider.getCurrentValue()));
        });
        VerticalStack digitalVSliderContainer = new VerticalStack();
        digitalVSliderContainer.setSpacing(0, 10);
        Label digitalVSliderValue = new Label("0.0");
        digitalVSliderValue.setMaxHeight(30);
        VerticalSlider digitalVSlider = new VerticalSlider();
        digitalVSlider.setMaxValue(10);
        digitalVSlider.setIgnoreStep(false);
        digitalVSlider.setStep(2);
        digitalVSlider.eventValueChanged.add((sender) -> {
            digitalVSliderValue.setText(String.format("%.1f", digitalVSlider.getCurrentValue()));
        });

        // ProgressBars
        HorizontalStack progressBarContainer = new HorizontalStack();
        progressBarContainer.setSpacing(10, 0);
        progressBarContainer.setContentAlignment(ItemAlignment.HCenter);
        progressBarContainer.setPadding(150, 0, 150, 0);
        progressBarContainer.setHeightPolicy(SizePolicy.Fixed);
        progressBarContainer.setHeight(50);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        progressBar.setCurrentValue(50);
        progressBar.setMaxValue(100);
        ImageItem reloadIcon = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32), false);
        reloadIcon.keepAspectRatio(true);
        AtomicBoolean isReloading = new AtomicBoolean(false);
        Prototype reloadButton = ComponentsFactory.makeActionButton("", Palette.WhiteGlass, () -> {
            if (isReloading.get()) {
                return;
            }
            isReloading.set(true);
            WindowManager.setRenderFrequency(RedrawFrequency.Ultra);
            try {
                new Thread(() -> {
                    for (int i = 0; i <= progressBar.getMaxValue(); i++) {
                        progressBar.setCurrentValue(i);
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {}
                    }
                    WindowManager.setRenderFrequency(RedrawFrequency.Low);
                    isReloading.set(false);
                }).start();
            } catch (Exception e) {}
        });
        reloadButton.setBorderRadius(15);
        reloadButton.setMaxWidth(30);
        reloadButton.setPadding(4, 4, 4, 4);

        Prototype loadButton = ComponentsFactory.makeActionButton("LoadingScreen", () -> {
            if (isReloading.get()) {
                return;
            }
            LoadingScreen loadScreen = new LoadingScreen();
            loadScreen.show(getHandler());
            isReloading.set(true);
            try {
                new Thread(() -> {
                    for (int i = 0; i <= progressBar.getMaxValue(); i++) {
                        loadScreen.setValue(i);
                        try {
                            Thread.sleep(20);
                        } catch (Exception e) {}
                    }
                    isReloading.set(false);
                    loadScreen.setToClose();
                }).start();
            } catch (Exception e) {}
        });

        addItems(ComponentsFactory.makeHeaderLabel("Slider implementations:"), slidersContainer,
                ComponentsFactory.makeHeaderLabel("ProgressBar implementations:"), progressBarContainer, loadButton);
        slidersContainer.addItems(horizontalSliders, verticalSliders);
        horizontalSliders.addItems(smoothHSliderContainer, digitalHSliderContainer);
        smoothHSliderContainer.addItems(smoothHSlider, smoothHSliderValue);
        digitalHSliderContainer.addItems(digitalHSlider, digitalHSliderValue);
        verticalSliders.addItems(smoothVSliderContainer, digitalVSliderContainer);
        smoothVSliderContainer.addItems(smoothVSlider, smoothVSliderValue);
        digitalVSliderContainer.addItems(digitalVSlider, digitalVSliderValue);

        progressBarContainer.addItems(progressBar, reloadButton);
        reloadButton.addItem(reloadIcon);
    }
}
