package net.visualillusionsent.lang;

/**
 * Initialization Error<br>
 * thrown if something causes the program to no be able to initialize
 * 
 * @author Jason (darkdiplomat)
 */
public final class InitializationError extends Error{

    /**
     * Constructs a new Initialization Error with a message
     * 
     * @param msg
     *            the message to add
     */
    public InitializationError(String msg){
        super(msg);
    }

    /**
     * Constructs a new Initialization Error with a message and cause
     * 
     * @param msg
     *            the message to add
     * @param cause
     *            the {@link Throwable} cause for the error
     */
    public InitializationError(String msg, Throwable cause){
        super(msg, cause);
    }

    /**
     * Constructs a new Initialization Error with a cause
     * 
     * @param cause
     *            the {@link Throwable} cause for the error
     */
    public InitializationError(Throwable cause){
        super(cause);
    }

    private static final long serialVersionUID = 2772175982743369746L;
}
