package com.spvessel.spacevil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.spvessel.spacevil.Core.IInputEventArgs;
import com.spvessel.spacevil.Flags.InputEventType;

final class ActionManagerAssigner {
    WindowLayout _layout;

    ActionManagerAssigner(WindowLayout layout) {
        _layout = layout;
    }

    void assignActionsForHoveredItem(InputEventType action, IInputEventArgs args, Prototype hoveredItem,
            List<Prototype> itemPyramid, boolean isOnlyHovered) {
        if (isOnlyHovered) {
            if (hoveredItem.isDisabled())
                return;

            EventTask task = new EventTask();
            task.item = hoveredItem;
            task.action = action;
            task.args = args;
            _layout.setEventTask(task);
        } else {
            goThroughItemPyramid(itemPyramid, action, args);
        }
        _layout.executePollActions();
    }

    void assignActionsForSender(InputEventType action, IInputEventArgs args, Prototype sender,
            List<Prototype> itemPyramid, boolean isPassUnder) {
        if (!sender.isDisabled()) {
            EventTask task = new EventTask();
            task.item = sender;
            task.action = action;
            task.args = args;
            _layout.setEventTask(task);
        }

        if (isPassUnder && sender.isPassEvents(action)) {
            if (itemPyramid != null) {
                goThroughItemPyramid(itemPyramid, action, args);
            }
        }
        _layout.executePollActions();
    }

    void assignActionsForItemPyramid(InputEventType action, IInputEventArgs args, Prototype sender,
            List<Prototype> itemPyramid) {
        if (sender.isPassEvents(action)) {
            if (itemPyramid != null) {
                goThroughItemPyramid(itemPyramid, action, args);
            }
        }
        _layout.executePollActions();
    }

    private void goThroughItemPyramid(List<Prototype> itemsList, InputEventType action, IInputEventArgs args) {
        Deque<Prototype> tmp = new ArrayDeque<Prototype>(itemsList);
        while (tmp.size() != 0) {

            Prototype item = tmp.pollLast();

            if (item.isDisabled())
                continue;

            EventTask t = new EventTask();
            t.item = item;
            t.action = action;
            t.args = args;
            _layout.setEventTask(t);

            if (!item.isPassEvents(action))
                break;
        }
    }
}