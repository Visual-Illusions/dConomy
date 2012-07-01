package net.visualillusionsent.minecraft.server.mod.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.utils.LocaleHelper;

public final class MessageTranslator extends LocaleHelper{

    private final static MessageTranslator $;
    static {
        $ = new MessageTranslator();
    }

    private MessageTranslator(){}

    public static final String transMessage(String key){
        return colorForm($.localeTranslate(key));
    }

    public static final String transFormMessage(String key, boolean color, Object... args){
        String toRet = $.localeTranslateMessage(key, args);
        if (color) {
            toRet = colorForm(toRet);
        }

        if (toRet.contains("$m")) {
            toRet = toRet.replace("$m", dCoBase.getProperties().getString("money.name"));
        }
        return toRet;
    }

    private static final String colorForm(String msg){
        return msg.replace("$c", MCChatForm.MARKER.stringValue());
    }
}
