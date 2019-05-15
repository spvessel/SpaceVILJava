package com.spvessel.spacevil.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemRule;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.OpenEntryDialog.OpenDialogType;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.MouseArgs;

import java.awt.*;

public class FlowTest extends ActiveWindow {
    int count = 0;
    ContextMenu _context_menu;

    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(1000, 800);
        setWindowName("FlowTest");
        setWindowTitle("FlowTest");

        setMinSize(500, 100);
        // setBackground(45, 45, 45);
        // setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("FlowTest");
        addItem(title);
        // getWindow().eventKeyPress.add((sender, args) -> {
        // // System.out.println(getHandler().getFocusedItem());
        // });
        // getWindow().eventKeyRelease.add((sender, args) -> {
        // if (args.key == KeyCode.SPACE) {
        // btn5.eventMouseClick.execute(btn5, new MouseArgs());
        // }
        // });

        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, title.getHeight(), 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 10);
        layout.setBackground(255, 255, 255, 20);

        // adding toolbar
        addItem(layout);

        // Frame
        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(51, 51, 51);
        toolbar.setItemName("toolbar");
        toolbar.setHeight(40);
        toolbar.setPadding(10, 0, 10, 0);
        toolbar.setSpacing(-10, 0);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        toolbar.setContentAlignment(ItemAlignment.RIGHT);
        layout.addItem(toolbar);

        FreeArea flow = new FreeArea();
        flow.setPadding(6, 6, 6, 6);
        flow.setBackground(70, 70, 70);
        layout.addItem(flow);

        // btn add_at_center
        FloatItem flow_item = new FloatItem(this);
        flow_item.setPosition(200, 200);
        flow_item.setSize(300, 100);

        // btn add_at_begin
        ButtonCore btn1 = new ButtonCore();
        btn1.setBackground(13, 176, 255);
        btn1.setItemName("nameof(btn1)");
        btn1.setWidth(30);
        btn1.setHeight(30);
        btn1.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn1.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 125));
        btn1.addItemState(ItemStateType.HOVERED, hovered);
        btn1.eventMouseClick.add((sender, args) -> {
            // PopUpMessage pop = new PopUpMessage("Hello PopUpMessage!");
            // pop.show(this);
            MessageItem msg = new MessageItem("Choose one of this buttons", "Message:");
            ButtonCore btnDontSave = new ButtonCore("Do not save");
            btnDontSave.eventMouseClick.add((s, a) -> {
                System.out.println("btnDontSave is chosen");
            });
            msg.addUserButton(btnDontSave, 2); //id must be > 1
            msg.onCloseDialog.add(() -> {
                System.out.println(msg.getResult() + " " + msg.getUserButtonResult());
            });
            msg.show(this);

            // InputDialog id = new InputDialog("Input text", "Apply");
            // id.show(this);

            // LoadingScreen ls = new LoadingScreen();
            // ls.show(this);
        });
        btn1.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn1.setHoverRule(ItemRule.STRICT);

        ButtonCore btn2 = new ButtonCore();
        btn2.setBackground(121, 223, 152);
        btn2.setItemName("nameof(btn2)");
        btn2.setWidth(30);
        btn2.setHeight(30);
        btn2.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn2.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn2.addItemState(ItemStateType.HOVERED, hovered);
        btn2.eventMouseClick.add((sender, args) -> {
            InputDialog id = new InputDialog("title", "actionName", "defaultText");
            id.show(this);
        });
        btn2.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
        btn2.setHoverRule(ItemRule.STRICT);

        // btn add_at_end
        ButtonCore btn3 = new ButtonCore();
        btn3.setBackground(238, 174, 128);
        btn3.setItemName("nameof(btn3)");
        btn3.setWidth(30);
        btn3.setHeight(30);
        btn3.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn3.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn3.addItemState(ItemStateType.HOVERED, hovered);
        btn3.eventMouseClick.add((sender, args) -> {
            ResizableItem frame = new ResizableItem();
            frame.setPadding(10, 10, 10, 10);
            frame.setBackground(100, 100, 100);
            frame.setSize(300, 300);
            frame.setPosition(200, 200);
            flow.addItem(frame);
            PointsContainer graph = getPointsContainer();
            frame.addItem(graph);
        });
        btn3.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn3.setHoverRule(ItemRule.STRICT);

        ButtonCore btn4 = new ButtonCore();
        btn4.setBackground(187, 102, 187);
        btn4.setItemName("nameof(btn4)");
        btn4.setWidth(30);
        btn4.setHeight(30);
        btn4.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn4.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn4.addItemState(ItemStateType.HOVERED, hovered);
        AlbumSideList side = new AlbumSideList(this, Side.LEFT);
        // side.setAttachSide(Side.TOP);
        Album al1 = new Album("Album1", "C:\\");
        al1._topLayout.setItemName("topLayout1");
        Album al2 = new Album("Album2", "C:\\");
        al2._topLayout.setItemName("topLayout2");
        side.addAlbum(al1);
        side.addAlbum(al2);
        // side.addAlbum(new Album("Album2", "C:\\"));
        // side.addAlbum(new Album("Album3", "C:\\"));
        // side.addAlbum(new Album("Album4", "C:\\"));

        btn4.eventMouseClick.add((sender, args) -> {
            // flow.addItem(getBlockList());
            side.show();
            // PopUpMessage popUpInfo = new PopUpMessage(
            //         "\n" + "Age: " + "\n" + "Sex: " + "\n" + "Race: " + "\n" + "Class: ");
            // popUpInfo.setTimeOut(3000);
            // popUpInfo.setHeight(200);
            // popUpInfo.show(this);
        });
        btn4.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 0)));
        btn4.setHoverRule(ItemRule.STRICT);

        ButtonCore btn5 = new ButtonCore();
        btn5.setBackground(238, 174, 128);
        btn5.setItemName("nameof(btn5)");
        btn5.setWidth(30);
        btn5.setHeight(30);
        btn5.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btn5.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btn5.addItemState(ItemStateType.HOVERED, hovered);
        // btn5.setMargin(0, 0, 50, 0);
        btn5.eventMouseClick.add((sender, args) -> {
            // MessageItem msg = new MessageItem("Set TRUE?", "Message:");
            // msg.onCloseDialog.add(() -> {
            // System.out.println(msg.getResult());
            // });
            // msg.show(this);

            OpenEntryDialog opd = new OpenEntryDialog("Save File:", FileSystemEntryType.FILE, OpenDialogType.SAVE);
            opd.addFilterExtensions("Text files (*.txt);*.txt", "Images (*.png, *.bmp, *.jpg);*.png,*.bmp,*.jpg");
            opd.onCloseDialog.add(() -> {
                System.out.println(opd.getResult());
            });
            opd.setDefaultPath("D:\\");
            opd.show(this);
        });
        btn5.setCustomFigure(new CustomFigure(false, GraphicsMathService.getTriangle(30, 30, 0, 0, 180)));
        btn5.setHoverRule(ItemRule.STRICT);

        // adding buttons
        toolbar.addItems(btn1, btn2, btn3, btn4, btn5);

        // _context_menu.setWidth(110);
        MenuItem restore = new MenuItem("Build Tool");
        // ImageItem res = new ImageItem(
        // DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
        // EmbeddedImageSize.SIZE_32X32));
        // // res.setSize(16, 16);
        // // res.setBackground(0, 0, 0, 0);
        // // res.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // // res.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        // // restore.addItem(res);

        restore.eventMouseClick.add((sender, args) -> {
            flow.setHScrollOffset(0);
            flow.setVScrollOffset(0);
            System.out.println(restore.getSender().getItemName());
        });
        MenuItem x_plus = new MenuItem("X += 100");
        // x_plus.eventMouseClick += (sender, args) ->
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() + 10);
        // };
        MenuItem y_plus = new MenuItem("Build Tool");
        // y_plus.eventMouseClick += (sender, args) ->
        // {
        // flow.setVScrollOffset(flow.getVScrollOffset() + 10);
        // };
        MenuItem addition = new MenuItem("ADdition");
        // context
        _context_menu = new ContextMenu(this);// new ContextMenu(this, restore, x_plus, y_plus, addition);
        _context_menu.setItemName("Base");
        _context_menu.addItems(restore, x_plus, y_plus, addition);
        // _context_menu.setWidth(200);

        ContextMenu addition_menu = new ContextMenu(this);
        addition_menu.setItemName("Addition");
        addition_menu.setSize(110, 94);
        MenuItem x_minus = new MenuItem("X -= 100");
        // x_minus.eventMouseClick += (sender, args) ->
        // {
        // flow.setHScrollOffset(flow.getHScrollOffset() - 10);
        // };
        MenuItem y_minus = new MenuItem("Y -= 100");
         y_minus.eventMouseClick.add((sender, args) ->
         {
//            flow.setVScrollOffset(flow.getVScrollOffset() - 10);
            System.out.println("menu width " + _context_menu.getWidth());
         });
        MenuItem ex_addition = new MenuItem("addition");
        addition_menu.addItems(x_minus, y_minus, ex_addition);

//        addition.assignContextMenu(addition_menu);

        ContextMenu ex_menu = new ContextMenu(this);
        ex_menu.setSize(110, 64);
        ex_addition.assignContextMenu(ex_menu);
        flow.addContextMenu(_context_menu);

        // addItem(new StopMenu());

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.V)
                CommonService.setClipboardString("SetClipBoardString");
            if (args.key == KeyCode.C)
                System.out.println(CommonService.getClipboardString());
            if (args.key == KeyCode.F)
                System.out.println(WindowsBox.getCurrentFocusedWindow().getWindowName());
        });
    }

    // private ResizableItem getBlockList() {
    // BlockList block = new BlockList();
    // // ResizableItem block = new ResizableItem();
    // block.eventMouseClick.add((sender, args) -> {
    // // System.out.println(block.getX() + " " + block.getY());
    // });
    // block.setBackground(45, 45, 45);
    // // block.SetBackground(255, 181, 111);
    // block.setWidth(250);
    // block.setHeight(200);
    // block.setX(100);
    // block.setY(100);
    // return block;
    // }

    private PointsContainer getPointsContainer() {
        PointsContainer graph_points = new PointsContainer();
        graph_points.setPointColor(new Color(10, 255, 10));
        graph_points.setPointThickness(10.0f);
        graph_points.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        graph_points.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // List<float[]> crd = new List<float[]>();
        // crd.add(new float[3] { 100.0f, 0.0f, 0.0f });
        // crd.add(new float[3] { 50.0f, 100.0f, 0.0f });
        // crd.add(new float[3] { 150.0f, 100.0f, 0.0f });
        // graph_points.setPointsCoord(crd);
        graph_points.setPointsCoord(GraphicsMathService.getRoundSquare(300, 300, 50, 0, 0));
        // graph_points.setPointsCoord(GraphicsMathService.getTriangle(100, 100, 0,
        // 0,
        // 0));
        // graph_points.setWidth(300);
        // graph_points.setHeight(300);
        // graph_points.setX(200);
        // graph_points.setY(200);
        //
        // graph_points.setShapePointer(GraphicsMathService.getTriangle(graph_points.getPointThickness(),
        // graph_points.getPointThickness()));
        //
        graph_points.setShapePointer(GraphicsMathService.getCross(graph_points.getPointThickness(),
                graph_points.getPointThickness(), 2, 45));
        //
        // graph_points.setShapePointer(GraphicsMathService.getStar(graph_points.getPointThickness(),
        // graph_points.getPointThickness() / 2.0f));
        return graph_points;
    }

    public ButtonCore getButton(String name) {
        ButtonCore btn = new ButtonCore(name);
        btn.setMaxHeight(30);
        btn.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        return btn;
    }

}