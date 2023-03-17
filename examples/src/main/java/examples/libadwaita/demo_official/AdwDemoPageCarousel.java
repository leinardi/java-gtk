package examples.libadwaita.demo_official;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.Carousel;
import ch.bailu.gtk.adw.ComboRow;
import ch.bailu.gtk.adw.EnumListItem;
import ch.bailu.gtk.gtk.Orientable;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Stack;
import ch.bailu.gtk.gtk.StringObject;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageCarousel extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageCarousel.class.getSimpleName());
    private final static long PARENT_TYPE = Bin.getTypeID();
    private final static int PARENT_OFFSET = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private static long type = 0;

    public AdwDemoPageCarousel(long self) {
        super(toCPointer(self));
        instance = new Instance(self);
    }

    @Structure.FieldOrder({"parent", "box", "carousel", "indicators_stack", "orientation_row", "indicators_row"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_OFFSET];
        public long box;
        public long carousel;
        public long indicators_stack;
        public long orientation_row;
        public long indicators_row;
    }

    private final Instance instance;


    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, 5*8, (__self, g_class, class_data) -> {

                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-carousel.ui");

                widgetClass.bindTemplateChildFull("box", true, PARENT_OFFSET);
                widgetClass.bindTemplateChildFull("carousel", true,PARENT_OFFSET + 8);
                widgetClass.bindTemplateChildFull("indicators_stack", true,PARENT_OFFSET + 16);
                widgetClass.bindTemplateChildFull("orientation_row", true,PARENT_OFFSET + 24);
                widgetClass.bindTemplateChildFull("indicators_row", true,PARENT_OFFSET + 32);

                widgetClass.bindTemplateCallback("get_orientation_name", new Callback() {
                    public long invoke(long item, long data) { return getOrientationName(item);}
                });
                widgetClass.bindTemplateCallback("notify_orientation_cb", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageCarousel(self).notifyOrientation();
                    }
                });
                widgetClass.bindTemplateCallback("get_indicators_name", new Callback() {
                    public long invoke(long obj) {
                        return getIndicatorsName(new StringObject(toCPointer(obj))).getCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("notify_indicators_cb", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageCarousel(self).notifyIndicators();
                    }
                });

                widgetClass.installAction("carousel.return", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageCarousel(widget.getCPointer()).carouselReturn());
            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }

    public static long getOrientationName(long item) {
        var enumListItem = new EnumListItem(Pointer.toCPointer(item));
        var value = enumListItem.getValue();

        if (value == Orientation.HORIZONTAL) {
            return new Str("Horizontal").getCPointer();
        } else if (value == Orientation.VERTICAL) {
            return new Str("Vertical").getCPointer();
        }
        return 0L;
    }

    public void notifyOrientation() {
        var box = new Orientable(toCPointer(instance.box));
        var carousel = new Orientable(toCPointer(instance.carousel));
        var orientation = new ComboRow(toCPointer(instance.orientation_row)).getSelected();

        carousel.setOrientation(orientation);
        box.setOrientation(1- orientation);
    }

    public static Str getIndicatorsName(StringObject value) {
        if (value.getString().toString().equals("dots")) {
            return new Str("Dots");
        }
        return new Str("Lines");
    }

    public void notifyIndicators() {
        var stringObject = new StringObject(new ComboRow(toCPointer(instance.indicators_row)).getSelectedItem().cast());
        var stack = new Stack(toCPointer(instance.indicators_stack));

        System.out.println(stringObject.getString());
        stack.setVisibleChildName(stringObject.getString());
    }

    public void carouselReturn() {
        var carousel = new Carousel(toCPointer(instance.carousel));
        carousel.scrollTo(carousel.getNthPage(0), true);
    }
}
