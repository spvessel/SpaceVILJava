package sandbox.View.Components;

import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.HorizontalSlider;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ImageQuality;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.ImageResources;
import sandbox.View.Decorations.Palette;

public class WorkWithImagesView extends VerticalStack {
    @Override
    public void initElements() {
        setPadding(50, 20, 50, 50);
        setSpacing(0, 20);

        ImageItem strechable = new ImageItem(GraphicsMathService.scaleBitmap(ImageResources.Art, 100, 100));
        strechable.setMaxHeight(500);
        strechable.setImageQuality(ImageQuality.Rough);

        ImageItem nonstrechable = new ImageItem(ImageResources.Art);
        nonstrechable.keepAspectRatio(true);
        nonstrechable.setMaxHeight(500);
        nonstrechable.setBackground(Palette.BlackShadow);

        ImageItem logo = new ImageItem(ImageResources.SpaveVILLogo, true);
        logo.keepAspectRatio(true);
        logo.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        logo.setSize(100, 100);
        logo.eventMouseClick.add((sender, args) -> {
            System.out.println("logo image click!");
        });
        logo.eventMouseHover.add((sender, args) -> {
            logo.setCursor(DefaultsService.getDefaultImage(EmbeddedImage.ArrowUp, EmbeddedImageSize.Size64x64), 32, 32);
        });
        logo.eventMouseLeave.add((sender, args) -> {
            logo.setCursor(DefaultsService.getDefaultCursor());
        });

        HorizontalSlider logoRotator = new HorizontalSlider();
        logoRotator.setStep(1);
        logoRotator.setMargin(25, 0, 25, 0);
        logoRotator.setMaxValue(360);
        logoRotator.eventValueChanged.add((sender) -> {
            logo.setRotationAngle(logoRotator.getCurrentValue());
        });

        addItems(ComponentsFactory.makeHeaderLabel("Image items implementation:"), strechable, nonstrechable,
                ComponentsFactory.makeHeaderLabel("Rotate image:"), logo, logoRotator);
    }
}
