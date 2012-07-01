package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;

public final class AccountingException extends RuntimeException{

    public AccountingException(String msg){
        super(msg);
    }

    public AccountingException(String msg, Throwable cause){
        super(msg, cause);
    }

    public AccountingException(Throwable cause){
        super(cause);
    }

    public final String getMessage(){
        return MessageTranslator.transMessage(super.getMessage());
    }

    private static final long serialVersionUID = 311216012013L;
}
