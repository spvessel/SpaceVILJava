package sandbox.View.Components;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.DialogWindow;
import com.spvessel.spacevil.InputBox;
import com.spvessel.spacevil.InputDialog;
import com.spvessel.spacevil.ListArea;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.MessageBox;
import com.spvessel.spacevil.MessageItem;
import com.spvessel.spacevil.OpenEntryBox;
import com.spvessel.spacevil.OpenEntryDialog;
import com.spvessel.spacevil.TextView;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.OpenDialogType;

import sandbox.View.Decorations.Palette;

public class WindowsAndDialogsView extends ListBox {

    @Override
    public void initElements() {
        super.initElements();

        setBackground(Palette.Transparent);
        setSelectionVisible(false);

        ListArea layout = getArea();
        layout.setPadding(30, 2, 30, 2);
        layout.setSpacing(0, 20);

        IBaseItem openWindow = ComponentsFactory.makeActionButton("Open Window", Palette.OrangeLight, () -> {
            ActiveWindow window = new ActiveWindow() {
                @Override
                public void initWindow() {
                    setPadding(10, 30, 10, 30);
                    setWindowTitle("Ordinary Window.");
                    setIcon(DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size64x64),
                            DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));
                    TextView text = new TextView();
                    text.setText("ActiveWindow is an ordinary window.");
                    addItem(text);
                }
            };
            window.show();
        });
        IBaseItem openDialogWindow = ComponentsFactory.makeActionButton("Open Dialog", Palette.Pink, () -> {
            DialogWindow window = new DialogWindow() {
                @Override
                public void initWindow() {
                    setPadding(10, 30, 10, 30);
                    setWindowTitle("Dialog Window.");
                    setIcon(DefaultsService.getDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size64x64),
                            DefaultsService.getDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32));
                    TextView text = new TextView();
                    text.setText(
                            "DialogWindow is a dialog implementation.\nThe parent window is locked until the dialog is closed.");
                    addItem(text);
                }
            };
            window.show();
        });
        IBaseItem openMessageBox = ComponentsFactory.makeActionButton("Open MessageBox", Palette.GreenLight, () -> {
            MessageBox messageBox = new MessageBox("This is the MessageBox.\nMessageBox is dialog window.", "Message:");
            messageBox.onCloseDialog.add(() -> {
                System.out.println("MessageBox result: " + messageBox.getResult());
            });
            messageBox.show();
        });
        IBaseItem openMessageItem = ComponentsFactory.makeActionButton("Open MessageItem", Palette.Blue, () -> {
            MessageItem messageItem = new MessageItem(
                    "This is the MessageItem.\nMessageItem is drawn on the current window.", "Message:");
            messageItem.onCloseDialog.add(() -> {
                System.out.println("MessageBox result: " + messageItem.getResult());
            });
            messageItem.show(getHandler());
        });
        IBaseItem openInputBox = ComponentsFactory.makeActionButton("Open InputBox", Palette.GreenLight, () -> {
            InputBox inputBox = new InputBox("Input text below:", "OK");
            inputBox.onCloseDialog.add(() -> {
                System.out.println("InputBox result: " + inputBox.getResult());
            });
            inputBox.show();
        });
        IBaseItem openInputDialog = ComponentsFactory.makeActionButton("Open InputDialog", Palette.Blue, () -> {
            InputDialog inputDialog = new InputDialog("Input text below:", "OK");
            inputDialog.onCloseDialog.add(() -> {
                System.out.println("InputDialog result: " + inputDialog.getResult());
            });
            inputDialog.show(getHandler());
        });
        IBaseItem openFileBrowserBox = ComponentsFactory.makeActionButton("Open OpenEntryBox", Palette.GreenLight, () -> {
            OpenEntryBox entryBox = new OpenEntryBox("Open File:", FileSystemEntryType.File, OpenDialogType.Save);
            entryBox.addFilterExtensions("Text files (*.txt);*.txt",
                    "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
            entryBox.onCloseDialog.add(() -> {
                System.out.println("OpenEntryBox result: " + entryBox.getResult());
            });
            entryBox.show();
        });
        IBaseItem openFileBrowserDialog = ComponentsFactory.makeActionButton("Open OpenEntryDialog", Palette.Blue, () -> {
            OpenEntryDialog entryDialog = new OpenEntryDialog("Open File:", FileSystemEntryType.File,
                    OpenDialogType.Save);
            entryDialog.addFilterExtensions("Text files (*.txt);*.txt",
                    "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
            entryDialog.onCloseDialog.add(() -> {
                System.out.println("OpenEntryDialog result: " + entryDialog.getResult());
            });
            entryDialog.show(getHandler());
        });

        addItems(ComponentsFactory.makeHeaderLabel("Common window implementations:"), openWindow, openDialogWindow,
                ComponentsFactory.makeHeaderLabel("Special window implementations:"), openMessageBox, openMessageItem,
                openInputBox, openInputDialog, ComponentsFactory.makeHeaderLabel("File browser window implementations:"),
                openFileBrowserBox, openFileBrowserDialog);
    }
}
