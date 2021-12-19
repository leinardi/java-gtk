package ch.bailu.gtk.bridge;

import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Int64;

public class Bytes extends ch.bailu.gtk.glib.Bytes {
    public Bytes(CPointer pointer) {
        super(pointer);
    }

    public Bytes(byte[] bytes) {
        super(new CPointer(ImpBytes.createFromArray(bytes)));
    }

    public Bytes(ch.bailu.gtk.type.Bytes bytes) {
        super(createFromWrapper(bytes));
    }


    private static CPointer createFromWrapper(ch.bailu.gtk.type.Bytes bytes) {
        bytes.throwIfNull();
        if (bytes.getSize() < 1) {
            bytes.throwNullPointerException("size == " + bytes.getSize());
        }
        return new CPointer(ImpBytes.createFromWrapper(bytes.getCPointer(), bytes.getSize()));
    }


    private ch.bailu.gtk.type.Bytes toBytesWrapper() {
        var size = new Int64();
        var data = getData(size);
        var result = new ch.bailu.gtk.type.Bytes(new CPointer(data.getCPointer()), (int) size.get());
        size.destroy();
        return result;
    }

    public byte[] toBytes(int start, int end) {
        return toBytesWrapper().toBytes(start, end);
    }

    public byte[] toBytes() {
        return toBytesWrapper().toBytes();
    }

    public byte getByte(int i) {
        return toBytesWrapper().getByte(i);
    }
}
