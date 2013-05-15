package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;
import net.canarymod.logger.CanaryLevel;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.AccountTransactionHook;
import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

public class Canary_Server implements Mod_Server, Mod_User{

    private final Server serv;
    private final Plugin dCo;
    private final ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, Plugin dCo){
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>>();
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
    public final ModType getModType(){
        return ModType.CANARY;
    }

    @Override
    public final String getName(){
        return "SERVER";
    }

    @Override
    public boolean hasPermission(String perm){
        return true;
    }

    @Override
    public final boolean isConsole(){
        return true;
    }

    @Override
    public void error(final String key, final Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().log(CanaryLevel.NOTICE, MCChatForm.removeFormating(key));
        }
        else {
            getServerLogger().log(CanaryLevel.NOTICE, MessageTranslator.transFormMessage(key, false, args));
        }
    }

    @Override
    public void message(final String key, final Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().info(MCChatForm.removeFormating(key));
        }
        else {
            getServerLogger().info(MessageTranslator.transFormMessage(key, false, args));
        }
    }

    @Override
    public void newTransaction(AccountTransaction transaction){
        for (Class<?> clazz : transactions.keySet()) {
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionHook hook = (AccountTransactionHook) clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Canary.hooks().callHook(hook);
                    break;
                }
                catch (Exception ex) {
                    ((Logman) getServerLogger()).logStacktrace("Exception occured while calling AccountTransactionHook", ex);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerTransactionHandler(Class<?> clazz, Class<? extends AccountTransaction> transaction){
        if (AccountTransactionHook.class.isAssignableFrom(clazz)) {
            if (!transactions.containsKey(clazz)) {
                transactions.put((Class<? extends AccountTransactionHook>) clazz, transaction);
            }
        }
    }

    @Override
    public void deregisterTransactionHandler(Class<?> clazz){
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }
}