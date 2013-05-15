package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;

public abstract class dConomyCommand{
    private final int minArgs;

    protected dConomyCommand(int minArgs){
        this.minArgs = minArgs;
    }

    public final boolean parseCommand(Mod_Caller caller, String[] args){
        if (args == null || args.length <= 0 || caller == null) {
            return false;
        }
        String[] cmdArgs = adjustedArgs(args, 1);

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
        String[] toRet = new String[args.length - start];
        try {
            System.arraycopy(args, start, toRet, 0, toRet.length);
        }
        catch (IndexOutOfBoundsException ioobe) {
            return new String[0];
        }
        return toRet;
    }

    protected abstract void execute(Mod_Caller caller, String[] args);
}
