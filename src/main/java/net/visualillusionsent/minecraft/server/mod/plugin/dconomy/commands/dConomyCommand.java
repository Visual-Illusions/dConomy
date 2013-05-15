package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;

public abstract class dConomyCommand{
    private final int minArgs;

    protected dConomyCommand(int minArgs){
        this.minArgs = minArgs;
    }

    public final boolean parseCommand(Mod_User caller, String[] args, boolean adjust){
        if (args == null || caller == null) {
            return false;
        }
        String[] cmdArgs = adjust ? adjustedArgs(args, 1) : args;

        if (cmdArgs.length < minArgs) {
            caller.error(MessageTranslator.transMessage("error.args"));
            return false;
        }
        else {
            execute(caller, cmdArgs);
            return true;
        }
    }

    private final String[] adjustedArgs(String[] args, int start){
        if (args.length == 0) {
            return args;
        }
        String[] toRet = new String[args.length - start];
        try {
            System.arraycopy(args, start, toRet, 0, toRet.length);
        }
        catch (IndexOutOfBoundsException ioobe) {
            return new String[0];
        }
        return toRet;
    }

    protected abstract void execute(Mod_User caller, String[] args);
}
