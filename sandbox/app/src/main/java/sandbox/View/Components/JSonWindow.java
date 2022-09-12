package sandbox.View.Components;

import java.io.File;
import java.nio.file.Paths;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;

import sandbox.View.json.JsonApplier;

public class JSonWindow extends ActiveWindow {
    @Override
    public void initWindow() {
        JsonApplier ja = new JsonApplier();
        String currentPath = Paths.get("").toAbsolutePath().toString() + File.separator;
        String jsWindow = "JsonLayout" + File.separator + "window.json";
        ja.applyJson(currentPath + jsWindow, this);
        String jsStyle = "JsonLayout" + File.separator + "style.json";
        ButtonCore btn = new ButtonCore("JSON");
        addItem(btn);
        ja.applyJson(currentPath + jsStyle, btn);
    }
}
