package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.logging.Level;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public final class dConomy extends Plugin{
    private dCoBase base;

    @Override
    public void disable(){
        base.cleanUp();
    }

    @Override
    public boolean enable(){
        try {
            base = new dCoBase(new Canary_Server(Canary.getServer(), this), this.getLogman());
            Canary.commands().registerCommands(new dConomyCommandListener(), this, false);
            Canary.hooks().registerListener(new dConomyCanaryAPIListener(), this);
            return true;
        }
        catch (InitializationError ierr) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", ierr.getCause());

        }
        catch (CommandDependencyException cdex) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", cdex.getCause());
        }
        return false;
    }
}
