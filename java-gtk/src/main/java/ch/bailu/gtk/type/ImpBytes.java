package ch.bailu.gtk.type;

import ch.bailu.gtk.lib.jna.CLib;

class ImpBytes {
    public static long createBytes(int size) {
        var result = CLib.INST().malloc(size);
        CLib.INST().memset(result, 0, size);
        return result;
    }

    public static long createBytes(byte[] bytes) {
        long result = CLib.INST().malloc(bytes.length);

        com.sun.jna.Pointer pointer = Pointer.asJnaPointer(result);
        pointer.write(0, bytes, 0, bytes.length);
        return result;
    }

    public static byte getByte(long cPointer, int index) {
        com.sun.jna.Pointer pointer = Pointer.asJnaPointer(cPointer);
        return pointer.getByte(index);
    }

    public static void setByte(long cPointer, int index, byte value) {
        com.sun.jna.Pointer pointer = Pointer.asJnaPointer(cPointer);
        pointer.setByte(index, value);
    }

    public static byte[] toBytes(long cPointer, int start, int size) {
        byte[] result = new byte[size];

        com.sun.jna.Pointer pointer = Pointer.asJnaPointer(cPointer);
        pointer.read(start, result, 0, size);
        return result;
    }

    public static void setInt(long cPointer, int index, int value) {
        com.sun.jna.Pointer pointer = Pointer.asJnaPointer(cPointer);
        pointer.setInt(index, value);
    }
}
