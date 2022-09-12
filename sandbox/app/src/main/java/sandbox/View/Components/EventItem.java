package sandbox.View.Components;

import java.util.HashMap;
import java.util.Map;
import com.spvessel.spacevil.BlankItem;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IInputEventArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.ScrollValue;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;
import sandbox.View.Decorations.Palette;

// Interface IDraggable and IMovable disables passage mouse click event
public class EventItem extends BlankItem {

    private Map<InputEventType, IInputEventArgs> currentEvents = new HashMap<>();
    private Label eventStateInfo = null;

    public EventItem(String name) {
        setItemName(name);
        setStyle(DefaultsService.getDefaultStyle(ButtonCore.class));
        setMargin(50, 50, 50, 50);
        setPadding(10, 10, 10, 10);
        setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        setBackground(Palette.GreenLight);
        setBorderRadius(2);
        addItemState(ItemStateType.Pressed, new ItemState(Palette.DarkGlass));
        effects().add(new Shadow(10, new Position(0, 3), new Size(0, 10), Palette.BlackShadow));

        eventStateInfo = new Label(name, false);
    }

    @Override
    public void initElements() {
        eventMousePress.add((sender, args) -> {
            currentEvents.remove(InputEventType.MouseRelease);
            currentEvents.put(InputEventType.MousePress, args);
            updateEventInfo();
        });
        eventMouseClick.add((sender, args) -> {
            currentEvents.remove(InputEventType.MouseDoubleClick);
            currentEvents.remove(InputEventType.MousePress);
            currentEvents.put(InputEventType.MouseRelease, args);
            updateEventInfo();
        });
        eventMouseDoubleClick.add((sender, args) -> {
            currentEvents.remove(InputEventType.MouseRelease);
            currentEvents.remove(InputEventType.MousePress);
            currentEvents.put(InputEventType.MouseDoubleClick, args);
            updateEventInfo();
        });
        eventMouseHover.add((sender, args) -> {
            currentEvents.remove(InputEventType.MouseLeave);
            currentEvents.put(InputEventType.MouseHover, args);
            updateEventInfo();
        });
        eventMouseLeave.add((sender, args) -> {
            currentEvents.remove(InputEventType.MouseHover);
            currentEvents.put(InputEventType.MouseLeave, args);
            updateEventInfo();
        });
        eventMouseMove.add((sender, args) -> {
            currentEvents.put(InputEventType.MouseMove, args);
            updateEventInfo();
        });
        eventScrollUp.add((sender, args) -> {
            currentEvents.put(InputEventType.MouseScroll, args);
            updateEventInfo();
        });
        eventScrollDown.add((sender, args) -> {
            MouseArgs margs = new MouseArgs();
            margs.scrollValue = new ScrollValue();
            margs.scrollValue.dY = args.scrollValue.dY * (-1);
            currentEvents.put(InputEventType.MouseScroll, margs);
            updateEventInfo();
        });
        eventKeyPress.add((sender, args) -> {
            currentEvents.remove(InputEventType.KeyRelease);
            currentEvents.put(InputEventType.KeyPress, args);
            updateEventInfo();
        });
        eventKeyRelease.add((sender, args) -> {
            currentEvents.remove(InputEventType.KeyPress);
            currentEvents.put(InputEventType.KeyRelease, args);
            updateEventInfo();
        });

        eventStateInfo.setFontSize(16);
        eventStateInfo.setForeground(Palette.Black);
        eventStateInfo.setTextAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        addItem(eventStateInfo);
    }

    private void updateEventInfo() {
        StringBuilder info = new StringBuilder(getItemName());
        if (currentEvents.containsKey(InputEventType.MousePress)) {
            info.append(" MP");
        }
        if (currentEvents.containsKey(InputEventType.MouseRelease)) {
            info.append(" MC");
        }
        if (currentEvents.containsKey(InputEventType.MouseDoubleClick)) {
            info.append(" MDC");
        }
        if (currentEvents.containsKey(InputEventType.MouseHover)) {
            info.append(" MH");
        }
        if (currentEvents.containsKey(InputEventType.MouseLeave)) {
            info.append(" ML");
        }
        if (currentEvents.containsKey(InputEventType.MouseMove)) {
            Position pos = ((MouseArgs) currentEvents.get(InputEventType.MouseMove)).position;
            info.append(" MM:" + pos.getX() + "," + pos.getY());
        }
        if (currentEvents.containsKey(InputEventType.MouseScroll)) {
            ScrollValue scroll = ((MouseArgs) currentEvents.get(InputEventType.MouseScroll)).scrollValue;
            info.append(" MS:" + scroll.dX + "," + scroll.dY);
        }
        if (currentEvents.containsKey(InputEventType.KeyPress)) {
            info.append(" KP");
        }
        if (currentEvents.containsKey(InputEventType.KeyRelease)) {
            info.append(" KR");
        }
        eventStateInfo.setText(info);
    }

    public void clear() {
        currentEvents.clear();
        updateEventInfo();
    }
}
