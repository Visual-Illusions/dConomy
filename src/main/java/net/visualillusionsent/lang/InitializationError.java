package net.visualillusionsent.lang;

public final class InitializationError extends Error{

    public InitializationError(String msg){
        super(msg);
    }

    public InitializationError(String msg, Throwable cause){
        super(msg, cause);
    }

    public InitializationError(Throwable cause){
        super(cause);
    }

    private static final long serialVersionUID = 2772175982743369746L;
}
