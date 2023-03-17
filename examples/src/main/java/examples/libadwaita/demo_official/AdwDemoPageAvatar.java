package examples.libadwaita.demo_official;

import com.sun.jna.Structure;

import java.util.Random;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.Avatar;
import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.PreferencesRow;
import ch.bailu.gtk.gio.AsyncResult;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.Editable;
import ch.bailu.gtk.gtk.FileChooserDialog;
import ch.bailu.gtk.gtk.ListBox;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageAvatar extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageAvatar.class.getSimpleName());
    private final static long PARENT_TYPE = Bin.getTypeID();
    private final static int PARENT_OFFSET = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private static long type = 0;


    private final static String[] firstNames = {
                "Adam",
                "Adrian",
                "Anna",
                "Charlotte",
                "Frédérique",
                "Ilaria",
                "Jakub",
                "Jennyfer",
                "Julia",
                "Justin",
                "Mario",
                "Miriam",
                "Mohamed",
                "Nourimane",
                "Owen",
                "Peter",
                "Petra",
                "Rachid",
                "Rebecca",
                "Sarah",
                "Thibault",
                "Wolfgang",
    };
    private final static String[] lastNames = {
                "Bailey",
                "Berat",
                "Chen",
                "Farquharson",
                "Ferber",
                "Franco",
                "Galinier",
                "Han",
                "Lawrence",
                "Lepied",
                "Lopez",
                "Mariotti",
                "Rossi",
                "Urasawa",
                "Zwickelman",
    };

    public AdwDemoPageAvatar(Widget widget) {
        this(widget.getCPointer());    }

    public AdwDemoPageAvatar(TypeInstance self) {
        super(self.cast());
        initTemplate();
        this.instance = new Instance(getCPointer());
        var name = createRandomName();
        var editable = new Editable(toCPointer(instance.text));
        editable.setText(name);

        populateContacts();
        remove();
    }

    private static String createRandomName() {
        return firstNames[new Random().nextInt(firstNames.length)] + " " + lastNames[new Random().nextInt(lastNames.length)];
    }

    public AdwDemoPageAvatar(long self) {
        super(toCPointer(self));
        instance = new Instance(self);
    }


    @Structure.FieldOrder({"parent", "avatar", "text", "file_chooser_label", "contacts"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_OFFSET];
        public long avatar;
        public long text;
        public long file_chooser_label;
        public long contacts;
    }

    private final Instance instance;


    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, 4*8, (__self, g_class, class_data) -> {

                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-avatar.ui");

                widgetClass.bindTemplateChildFull("avatar", true, PARENT_OFFSET);
                widgetClass.bindTemplateChildFull("text", true,PARENT_OFFSET + 8);
                widgetClass.bindTemplateChildFull("file_chooser_label", true,PARENT_OFFSET + 16);
                widgetClass.bindTemplateChildFull("contacts", true,PARENT_OFFSET + 24);

                widgetClass.installAction("avatar.open", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageAvatar(widget).open());
                widgetClass.installAction("avatar.remove", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageAvatar(widget).remove());
                widgetClass.installAction("avatar.save", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageAvatar(widget).save());
            }, (__self, instance, g_class) -> new AdwDemoPageAvatar(instance));
        }
        return type;
    }


    public void populateContacts() {
        for (int i = 0; i < 30; i++) {
            var name = createRandomName();
            var contact = new ActionRow();
            var avatar = new Avatar(40, name, false);

            avatar.setMarginTop(12);
            avatar.setMarginBottom(12);

            new PreferencesRow(contact.cast()).setTitle(name);
            contact.addPrefix(avatar);
            new ListBox(toCPointer(instance.contacts)).append(contact);
        }
    }

    void avatarOpenDialog(FileChooserDialog dialog, AsyncResult asyncResult) {
        // TODO GTK FileDialog is in 4.10
    }

    void open() {
        System.out.println("Avatar Open");
    }

    void remove() {
        System.out.println("Avatar Remove");
    }

    void avatarSaveDialog(long dialog, long asyncResult) {

    }

    void save() {
        System.out.println("Avatar Save");
    }
}
