package com.spvessel.spacevil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceImageItem;
import com.spvessel.spacevil.Core.InterfaceTextContainer;

/**
 * ItemsRefreshManager is a manager that allows you to add an item of a certain type to the queue for a forced refresh. 
 * It can be use with custom implementation of one of the types: shape, text, image. 
 * For example: a Bitmap change in an IImageItem implementation, a shape change in an IBaseItem implementation.
 */
public final class ItemsRefreshManager {

    private ItemsRefreshManager() {
    }

    private static Lock _lockShapeObject = new ReentrantLock();
    private static Lock _lockTextObject = new ReentrantLock();
    private static Lock _lockImageObject = new ReentrantLock();

    private static Set<InterfaceBaseItem> _shapes = new HashSet<>();
    private static Set<InterfaceTextContainer> _texts = new HashSet<>();
    private static Set<InterfaceImageItem> _images = new HashSet<>();

    private static VisualItem castVisualItem(InterfaceBaseItem item) {
        VisualItem vi = null;
        if (item instanceof VisualItem) {
            vi = (VisualItem) item;
        }
        return vi;
    }

    /**
     * Adding an IBaseItem implementation to the queue for a forced refresh.
     * <p> Tips: use this function only if you want to refresh shape of an item, 
     * ITextContainer and IImageItem are not shapes.
     * @param item An item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if adding is successfull. False: if an item is already in the refresh queue.
     */
    public static boolean setRefreshShape(InterfaceBaseItem item) {
        if (item instanceof InterfaceTextContainer
                // || !item.isVisible() || !item.isDrawable()
                // || (item.getBackground().getAlpha() == 0)
                ) {

            return false;
        }
        VisualItem vi = castVisualItem(item);

        _lockShapeObject.lock();
        try {
            if (vi == null) {
                return _shapes.add(item);
            }
            return _shapes.add(vi.prototype);
        } finally {
            _lockShapeObject.unlock();
        }
    }

    /**
     * Adding an ITextContainer implementation to the queue for a forced refresh.
     * <p> Tips: use this function only if you want to refresh text of an item, 
     * InterfaceBaseItem and InterfaceImageItem are not text.
     * @param item An item as com.spvessel.spacevil.Core.InterfaceTextContainer.
     * @return True: if adding is successfull. False: if an item is already in the refresh queue.
     */
    public static boolean setRefreshText(InterfaceTextContainer item) {
        _lockTextObject.lock();
        try {
            return _texts.add(item);
        } finally {
            _lockTextObject.unlock();
        }
    }

    /**
     * Adding an IImageItem implementation to the queue for a forced refresh.
     * <p> Tips: use this function only if you want to refresh image of an item, 
     * InterfaceBaseItem and InterfaceTextContainer are not images.
     * @param item An item as com.spvessel.spacevil.Core.InterfaceImageItem.
     * @return True: if adding is successfull. False: if an item is already in the refresh queue.
     */
    public static boolean setRefreshImage(InterfaceImageItem item) {
        _lockImageObject.lock();
        try {
            return _images.add(item);
        } finally {
            _lockImageObject.unlock();
        }
    }

    //IS SECTION
    static boolean isRefreshShape(InterfaceBaseItem item) {
        _lockShapeObject.lock();
        try {
            return _shapes.contains(item);
        } finally {
            _lockShapeObject.unlock();
        }
    }

    static boolean isRefreshText(InterfaceTextContainer item) {
        _lockTextObject.lock();
        try {
            return _texts.contains(item);
        } finally {
            _lockTextObject.unlock();
        }
    }

    static boolean isRefreshImage(InterfaceImageItem item) {
        _lockImageObject.lock();
        try {
            return _images.contains(item);
        } finally {
            _lockImageObject.unlock();
        }
    }

    // REMOVE SECTION
    static boolean removeShape(InterfaceBaseItem item) {
        _lockShapeObject.lock();
        try {
            return _shapes.remove(item);
        } finally {
            _lockShapeObject.unlock();
        }
    }

    static boolean removeText(InterfaceTextContainer item) {
        _lockTextObject.lock();
        try {
            return _texts.remove(item);
        } finally {
            _lockTextObject.unlock();
        }
    }

    static boolean removeImage(InterfaceImageItem item) {
        _lockImageObject.lock();
        try {
            return _images.remove(item);
        } finally {
            _lockImageObject.unlock();
        }
    }

    // static void printSizeOfShapes() {
    //     for (InterfaceBaseItem item : _shapes) {
    //         System.out.println(item.getItemName() + " " + (item instanceof Prototype) + " " + (item.getBackground().getAlpha() == 0));
    //     }
    //     System.out.println("Shapes: " + _shapes.size());
    //     System.out.println("Images: " + _images.size());
    //     System.out.println("Text: " + _texts.size());
    // }
}
