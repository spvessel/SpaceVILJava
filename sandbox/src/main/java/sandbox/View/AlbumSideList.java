package sandbox.View;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

public class AlbumSideList extends SideArea {
    private boolean _isInit = false;
    private List<Album> _list = new LinkedList<>();
    private ListBox _albumList = new ListBox();
    private ButtonCore _addButton = new ButtonCore();

    public AlbumSideList(CoreWindow handler, Side attachSide) {
        super(handler, attachSide);
    }

    @Override
    public void initElements() {
        super.initElements();
        if (_isInit)
            return;

        Label _name = new Label();
        _name.setFont(DefaultsService.getDefaultFont(Font.BOLD, 28));
        _name.setText("My Albums");
        _name.setMargin(0, 0, 0, 0);
        _name.setMargin(0, 25, 0, 0);
        _name.setPadding(10, 10, 10, 0);
        _name.setHeightPolicy(SizePolicy.Fixed);
        _name.setHeight(70);

        _albumList.setSelectionVisible(false);
        _albumList.setVScrollBarPolicy(VisibilityPolicy.AsNeeded);
        _albumList.setHScrollBarPolicy(VisibilityPolicy.Never);
        _albumList.setMargin(10, 100, 10, 50);
        _albumList.vScrollBar.setStyle(Style.getSimpleVerticalScrollBarStyle());

        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = Color.BLACK;
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        style.setSize(40, 40);
        style.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
        style.setTextAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
        style.setPadding(4, 4, 4, 4);
        style.setMargin(0, 0, 10, 10);
        style.setSpacing(0, 0);
        style.setBorder(new Border(new Color(0, 0, 0, 0), new CornerRadius(20), 0));
        style.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 100)));
        style.addItemState(ItemStateType.Pressed, new ItemState(new Color(0, 0, 0, 100)));
        _addButton.setStyle(style);

        CustomShape plus = new CustomShape(GraphicsMathService.getCross(30, 30, 4, 0));
        plus.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        plus.setBackground(100, 100, 100);
        plus.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

        addItems(_name, _albumList, _addButton);

        _addButton.addItem(plus);

        _addButton.eventMouseClick.add((sender, args) -> {
            hide();
            OpenEntryDialog browse = new OpenEntryDialog("Open Folder:", FileSystemEntryType.Directory,
                    OpenDialogType.Open);
            browse.onCloseDialog.add(() -> {
                Album album = new Album(new File(browse.getResult()).getName(), browse.getResult());
                addAlbum(album);
            });
            browse.show(getHandler());
        });

        if (_list.size() > 0)
            for (Album item : _list) {
                _albumList.addItem(item);
                item._expand.eventToggle.add((sender, args) -> {
                    _albumList.getWrapper(item).updateSize();
                    _albumList.getArea().updateLayout();
                });
            }

        _isInit = true;
    }

    public void addAlbum(Album album) {
        if (_isInit) {
            _albumList.addItem(album);
            album._expand.eventToggle.add((sender, args) -> {
                _albumList.getWrapper(album).updateSize();
                _albumList.getArea().updateLayout();
            });
        } else {
            _list.add(album);
        }
    }

    public void show() {
        super.show();
        _albumList.getArea().updateLayout();
    }

    public void show(IItem item, MouseArgs args) {
        super.show(item, args);
    }
}