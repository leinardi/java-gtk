package ch.bailu.gtk.wrapper;

public class ImpStr {
    public static native long createStr(String str);

    public static native String toString(long pointer);
}
