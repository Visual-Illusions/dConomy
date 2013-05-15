package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

public interface dCoDataSource{

    final Object lock = new Object();

    boolean load();

    void saveAccount(Account account);

    void reloadAccount(Account account);
}
