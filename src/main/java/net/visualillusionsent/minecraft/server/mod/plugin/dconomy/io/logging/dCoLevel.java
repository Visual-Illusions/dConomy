package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.logging;

import java.util.logging.Level;

public final class dCoLevel extends Level{

    private static final long serialVersionUID = 210434042012L;
    private static int baselvl = 15000;
    private static final String RD = "DCONOMY-DEBUG-";
    public static final dCoLevel //
                    STACKTRACE = new dCoLevel(RD.concat("STACKTRACE"), genLevel()), //
                    GENERAL = new dCoLevel(RD.concat("GENERAL"), genLevel());

    protected dCoLevel(String name, int intvalue){
        super(name, intvalue);
    }

    private final static int genLevel(){
        ++baselvl;
        return baselvl;
    }
}
