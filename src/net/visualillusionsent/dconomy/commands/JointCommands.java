package net.visualillusionsent.dconomy.commands;

import net.visualillusionsent.dconomy.*;

public enum JointCommands {
    JCMD1;
    
    private static ActionResult res = new ActionResult();
    
    private JointCommands(){ }
    
    private static boolean argcheck(int length, String[] args){
        return args.length >= length;
    }
    
    public ActionResult execute(User user, String[] args){
        return new ActionResult();
    }

}
