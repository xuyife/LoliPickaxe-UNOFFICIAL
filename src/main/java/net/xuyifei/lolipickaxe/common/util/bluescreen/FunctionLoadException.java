package net.xuyifei.lolipickaxe.common.util.bluescreen;

public class FunctionLoadException extends RuntimeException {
    public FunctionLoadException() {
        super();
    }

    public FunctionLoadException(String functionName) {
        super(functionName);
    }
}
