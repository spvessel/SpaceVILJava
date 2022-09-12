package sandbox;

import com.spvessel.spacevil.Common.CommonService;

import sandbox.View.MainWindow;

public class App {
    public static void main(String[] args) {
        System.out.println(CommonService.getSpaceVILInfo());
        new MainWindow().show();
    }
}
