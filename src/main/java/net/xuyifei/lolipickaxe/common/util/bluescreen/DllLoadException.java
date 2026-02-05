package net.xuyifei.lolipickaxe.common.util.bluescreen;

public class DllLoadException extends RuntimeException {
    public DllLoadException() {
        super();
    }

    public DllLoadException(String dllName) {
        super(dllName);
    }
}
