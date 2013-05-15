package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.canarymod.logger.Logman;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.AccountTransactionEvent;
import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Bukkit_Server implements Mod_Server, Mod_User{

    private final Server serv;
    private final dConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>> transactions;

    public Bukkit_Server(Server serv, dConomy dCo){
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>>();
    }

    @Override
    public final Mod_User getUser(String name){
        Player player = serv.getPlayer(name);
        if (player != null) {
            return new Bukkit_User(player);
        }
        else {
            OfflinePlayer offplayer = serv.getOfflinePlayer(name);
            if (offplayer != null) {
                return new Bukkit_OfflineUser(offplayer);
            }
        }
        return null;
    }

    @Override
    public final Logger getServerLogger(){
        return dCo.getLogger();
    }

    @Override
    public final ModType getModType(){
        return ModType.BUKKIT;
    }

    @Override
    public final String getName(){
        return "SERVER";
    }

    @Override
    public final boolean isConsole(){
        return true;
    }

    @Override
    public void error(final String key, final Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().log(Level.FINE, MCChatForm.removeFormating(key));
        }
        else {
            getServerLogger().log(Level.FINE, MessageTranslator.transFormMessage(key, false, args));
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
    public boolean hasPermission(String perm){
        return true;
    }

    @Override
    public void newTransaction(AccountTransaction transaction){
        for (Class<?> clazz : transactions.keySet()) {
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionEvent event = (AccountTransactionEvent) clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Bukkit.getPluginManager().callEvent(event);
                    break;
                }
                catch (Exception ex) {
                    ((Logman) getServerLogger()).logStacktrace("Exception occured while calling AccountTransactionEvent", ex);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerTransactionHandler(Class<?> clazz, Class<? extends AccountTransaction> transaction){
        if (AccountTransactionEvent.class.isAssignableFrom(clazz)) {
            if (!transactions.containsKey(clazz)) {
                transactions.put((Class<? extends AccountTransactionEvent>) clazz, transaction);
            }
        }
    }

    public void deregisterTransactionHandler(Class<?> clazz){
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }
}
