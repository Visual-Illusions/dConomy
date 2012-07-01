package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;

public final class Canary_Console implements Mod_Caller{

    @Override
    public final String getName(){
        return "SERVER";
    }

    @Override
    public final void sendError(String key, Object... args){
        System.out.println(MessageTranslator.transFormMessage(key, false, args));
    }

    @Override
    public final void sendMessage(String key, Object... args){
        System.out.println(MessageTranslator.transFormMessage(key, false, args));
    }

    @Override
    public final boolean isConsole(){
        return true;
    }

    @Override
    public final ModType getModType(){
        return ModType.CANARY;
    }
}
