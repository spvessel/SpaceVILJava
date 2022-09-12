package sandbox.View.Components;

import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ComboBox;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import sandbox.View.Decorations.Palette;

public class EventsAndRoutingView extends VerticalStack {
    private BlankItem a = null;
    private BlankItem b = null;
    private BlankItem c = null;

    @Override
    public void initElements() {
        a = new EventItem("A:");
        b = new EventItem("B:");
        c = new EventItem("C:");

        ComboBox lockEventModes = new ComboBox(
            makeOption("Pass ALL events", () -> {
                enableEvents(true);
            }),
            makeOption("Don't pass ALL events", () -> {
                enableEvents(false);
            }),
            makeOption("Pass only MousePress events", () -> {
                setPassEvents(InputEventType.MousePress);
            }),
            makeOption("Pass only MouseClick events", () -> {
                setPassEvents(InputEventType.MouseRelease);

            }),
            makeOption("Pass only MouseDoubleClick events", () -> {
                setPassEvents(InputEventType.MouseDoubleClick);
            }),
            makeOption("Pass only MouseHover events", () -> {
                setPassEvents(InputEventType.MouseHover);
            }),
            makeOption("Pass only MouseLeave events", () -> {
                setPassEvents(InputEventType.MouseLeave);
            }),
            makeOption("Pass only MouseMove events", () -> {
                setPassEvents(InputEventType.MouseMove);
            }),
            makeOption("Pass only MouseScroll events", () -> {
                setPassEvents(InputEventType.MouseScroll);
            }),
            makeOption("Pass only KeyPress events", () -> {
                setPassEvents(InputEventType.KeyPress);
            }),
            makeOption("Pass only KeyRelease events", () -> {
                setPassEvents(InputEventType.KeyRelease);
            })
        );
        lockEventModes.selectionChanged.add(() -> {
            a.clear();
            b.clear();
            c.clear();
        });
        lockEventModes.setMaxSize(500, 35);
        lockEventModes.setMargin(50, 0, 50, 0);
        lockEventModes.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

        Frame layout = new Frame();
        layout.setBackground(Palette.WhiteGlass);
        layout.setMargin(50, 50, 50, 50);
        
        addItems(ComponentsFactory.makeHeaderLabel("Events & Routing:"), lockEventModes, layout);
        layout.addItems(c);
        c.addItem(b);
        b.addItem(a);

        lockEventModes.setCurrentIndex(0);
    }

    private MenuItem makeOption(String name, Runnable action) {
        MenuItem item =  new MenuItem(name);
        item.eventMouseClick.add((sender, args) -> {
            action.run();
        });
        return item;
    }

    private void setPassEvents(InputEventType event) {
        a.setPassEvents(false);
        b.setPassEvents(false);
        c.setPassEvents(false);

        a.setPassEvents(true, event);
        b.setPassEvents(true, event);
        c.setPassEvents(true, event);
    }

    private void enableEvents(boolean value) {
        a.setPassEvents(value);
        b.setPassEvents(value);
        c.setPassEvents(value);
    }
}
