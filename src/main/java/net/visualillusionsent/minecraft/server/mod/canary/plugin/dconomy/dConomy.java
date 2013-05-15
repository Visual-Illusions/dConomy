package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.logging.Level;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletTransactionHook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

public final class dConomy extends Plugin{
    private dCoBase base;

    @Override
    public final void disable(){
        base.cleanUp();
    }

    @Override
    public final boolean enable(){
        try {
            base = new dCoBase(new Canary_Server(Canary.getServer(), this), this.getLogman());
            WalletHandler.initialize();
            new dConomyCanaryAPIListener(this);
            new dConomyCanaryCommandListener(this);
            dCoBase.getServer().registerTransactionHandler(WalletTransactionHook.class, WalletTransaction.class);
            return true;
        }
        catch (InitializationError ierr) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", ierr.getCause());
        }
        catch (CommandDependencyException cdex) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", cdex.getCause());
        }
        catch (Exception ex) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", ex.getCause());
        }
        return false;
    }
}
