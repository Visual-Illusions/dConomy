package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.logging.Logger;
import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.logger.CanaryLevel;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.AccountTransactionHook;
import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

public final class Canary_Server implements Mod_Server, Mod_Caller{

    private final Server serv;
    private final Plugin dCo;

    public Canary_Server(Server serv, Plugin dCo){
        this.serv = serv;
        this.dCo = dCo;
    }

    @Override
    public final Mod_User getUser(String name){
        Player player = serv.matchPlayer(name);
        if (player != null) {
            return new Canary_User(player);
        }
        else {
            OfflinePlayer offplayer = serv.getOfflinePlayer(name);
            if (offplayer != null) {
                return new Canary_OfflineUser(offplayer);
            }
        }
        return null;
    }

    @Override
    public final Logger getServerLogger(){
        return dCo.getLogman();
    }

    @Override
    public final void newTransaction(AccountTransaction transaction){
        Canary.hooks().callHook(new AccountTransactionHook(transaction));
    }

    @Override
    public final ModType getModType(){
        return ModType.CANARY;
    }

    @Override
    public Mod_Caller getServerUser(){
        return this;
    }

    @Override
    public final void error(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().log(CanaryLevel.NOTICE, MCChatForm.removeFormating(key));
        }
        else {
            getServerLogger().log(CanaryLevel.NOTICE, MessageTranslator.transFormMessage(key, false, args));
        }
    }

    @Override
    public final void message(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().info(MCChatForm.removeFormating(key));
        }
        else {
            getServerLogger().info(MessageTranslator.transFormMessage(key, false, args));
        }
    }

    @Override
    public String getName(){
        return "SERVER";
    }

    @Override
    public boolean isConsole(){
        return true;
    }
}
