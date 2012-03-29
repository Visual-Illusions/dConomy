package net.visualillusionsent.dconomy.messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.logging.Logger;

import net.visualillusionsent.dconomy.data.DCoProperties;

public class LoadMessages {
    private static Logger logger = Logger.getLogger("Minecraft");
    private static Properties dCoMess;
    private static NumberFormat displayform = new DecimalFormat("#,##0.00");
    
    private static void checkFile(){
        File messDir = new File("plugins/config/dConomy/Messages/");
        File messfile = new File("plugins/config/dConomy/Messages/dCoMessages.txt");
        if(!messDir.isDirectory()){
            messDir.mkdirs();
        }
        if(!messfile.exists()){
            try{
                InputStream in = DCoProperties.getDS().getClass().getClassLoader().getResourceAsStream("dCoMessages.txt");
                FileWriter out = new FileWriter("plugins/config/dConomy/Messages/dCoMessages.txt");
                int c;
                while ((c = in.read()) != -1){
                    out.write(c);
                }
                in.close();
                out.close();
            } 
            catch (IOException e) { }
        }
    }
    
    public static void LoadMessage(){
        logger.info("[dConomy] Attempting to Load Messages...");
        
        checkFile();
        
        dCoMess = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("plugins/config/dConomy/Messages/dCoMessages.txt");
            dCoMess.load(stream);
            stream.close();
        } 
        catch (IOException ex) {
            logger.warning("[dConomy] Unable to load Messages... Using defaults...");
            return;
        }
        
        logger.info("[dConomy] Loading Error Messages...");
        //Get/Set ErrorMessages (100 Series)
        ErrorMessages.E101.setMess(parseMess(ErrorMessages.E101.plainMess(), "101-NoPermission"));
        ErrorMessages.E102.setMess(parseMess(ErrorMessages.E102.plainMess(), "102-NumberFormatError"));
        ErrorMessages.E103.setMess(parseMess(ErrorMessages.E103.plainMess(), "103-NotEnoughArguments"));
        ErrorMessages.E104.setMess(parseMess(ErrorMessages.E104.plainMess(), "104-AccountNotFound"));
        ErrorMessages.E105.setMess(parseMess(ErrorMessages.E105.plainMess(), "105-JointAccountNotFound"));
        ErrorMessages.E106.setMess(parseMess(ErrorMessages.E106.plainMess(), "106-JointAccountNameTaken"));
        ErrorMessages.E107.setMess(parseMess(ErrorMessages.E107.plainMess(), "107-JointAccountNameInvalid"));
        ErrorMessages.E108.setMess(parseMess(ErrorMessages.E108.plainMess(), "108-JointAccountNoPermission"));
        ErrorMessages.E109.setMess(parseMess(ErrorMessages.E109.plainMess(), "109-JointAccountUserCannotWithdraw"));
        ErrorMessages.E110.setMess(parseMess(ErrorMessages.E110.plainMess(), "110-JointAccountUserWithdrawTooMuch"));
        ErrorMessages.E111.setMess(parseMess(ErrorMessages.E111.plainMess(), "111-JointAccountAlreadyUser"));
        ErrorMessages.E112.setMess(parseMess(ErrorMessages.E112.plainMess(), "112-JointAccountAlreadyOwner"));
        ErrorMessages.E113.setMess(parseMess(ErrorMessages.E113.plainMess(), "113-JointAccountAlreadyNotUser"));
        ErrorMessages.E114.setMess(parseMess(ErrorMessages.E114.plainMess(), "114-JointAccountAlreadyNotOwner"));
        ErrorMessages.E115.setMess(parseMess(ErrorMessages.E115.plainMess(), "115-AccountNotEnoughMoney"));
        ErrorMessages.E116.setMess(parseMess(ErrorMessages.E116.plainMess(), "116-BankNotEnoughMoney"));
        ErrorMessages.E117.setMess(parseMess(ErrorMessages.E117.plainMess(), "117-JointNotEnoughMoney"));
        ErrorMessages.E118.setMess(parseMess(ErrorMessages.E118.plainMess(), "118-Player-CannotPaySelf"));
        logger.info("[dConomy] ErrorMessages Loaded!");
        logger.info("[dConomy] Loading Account Messages...");
        //Get/Set Account Messages (200 Series)
        AccountMessages.A201.setMess(parseMess(AccountMessages.A201.plainMess(), "201-AccountBalance"));
        AccountMessages.A202.setMess(parseMess(AccountMessages.A202.plainMess(), "202-AccountNewBalance"));
        AccountMessages.A203.setMess(parseMess(AccountMessages.A203.plainMess(), "203-CheckAnotherAccount"));
        AccountMessages.A204.setMess(parseMess(AccountMessages.A204.plainMess(), "204-BankDeposit"));
        AccountMessages.A205.setMess(parseMess(AccountMessages.A205.plainMess(), "205-BankWithdraw"));
        AccountMessages.A206.setMess(parseMess(AccountMessages.A206.plainMess(), "206-JointWithdraw"));
        AccountMessages.A207.setMess(parseMess(AccountMessages.A207.plainMess(), "207-JointDeposit"));
        AccountMessages.A208.setMess(parseMess(AccountMessages.A208.plainMess(), "208-PayPlayer"));
        AccountMessages.A209.setMess(parseMess(AccountMessages.A209.plainMess(), "209-PayJoint"));
        AccountMessages.A210.setMess(parseMess(AccountMessages.A210.plainMess(), "210-PaymentSentFromBank"));
        AccountMessages.A211.setMess(parseMess(AccountMessages.A211.plainMess(), "211-PaymentSentFromJoint"));
        AccountMessages.A212.setMess(parseMess(AccountMessages.A212.plainMess(), "212-ReceivedPaymentFrom"));
        AccountMessages.A213.setMess(parseMess(AccountMessages.A213.plainMess(), "213-PaymentSentToBank"));
        AccountMessages.A214.setMess(parseMess(AccountMessages.A214.plainMess(), "214-PaymentSentToJoint"));
        AccountMessages.A215.setMess(parseMess(AccountMessages.A215.plainMess(), "215-PayForwardingSetToBank"));
        AccountMessages.A216.setMess(parseMess(AccountMessages.A216.plainMess(), "216-PayForwardingSetToJoint"));
        AccountMessages.A217.setMess(parseMess(AccountMessages.A217.plainMess(), "217-PayForwardingTurnedOff"));
        AccountMessages.A218.setMess(parseMess(AccountMessages.A218.plainMess(), "218-PayForwardingNotSet"));
        AccountMessages.A219.setMess(parseMess(AccountMessages.A219.plainMess(), "219-PayForwardingIsBank"));
        AccountMessages.A220.setMess(parseMess(AccountMessages.A220.plainMess(), "220-PayForwardingIsJoint"));
        AccountMessages.A221.setMess(parseMess(AccountMessages.A221.plainMess(), "221-RankSelf"));
        AccountMessages.A222.setMess(parseMess(AccountMessages.A222.plainMess(), "222-RankOther"));
        AccountMessages.A223.setMess(parseMess(AccountMessages.A223.plainMess(), "223-RankTopOpenner"));
        AccountMessages.A224.setMess(parseMess(AccountMessages.A224.plainMess(), "224-RankTopSort"));
        AccountMessages.A225.setMess(parseMess(AccountMessages.A225.plainMess(), "225-JointAccountCreated"));
        AccountMessages.A226.setMess(parseMess(AccountMessages.A226.plainMess(), "226-JointAccountDeleted"));
        AccountMessages.A227.setMess(parseMess(AccountMessages.A227.plainMess(), "227-JointAccountOwnerAdded"));
        AccountMessages.A228.setMess(parseMess(AccountMessages.A228.plainMess(), "228-JointAccountOwnerRemoved"));
        AccountMessages.A229.setMess(parseMess(AccountMessages.A229.plainMess(), "229-JointAccountUserAdded"));
        AccountMessages.A230.setMess(parseMess(AccountMessages.A230.plainMess(), "230-JointAccountUserRemoved"));
        AccountMessages.A231.setMess(parseMess(AccountMessages.A231.plainMess(), "231-JointAccountMaxUserWithdrawCheck"));
        AccountMessages.A232.setMess(parseMess(AccountMessages.A232.plainMess(), "232-JointAccountMaxUserWithdrawSet"));
        AccountMessages.A233.setMess(parseMess(AccountMessages.A233.plainMess(), "233-JointAccountMaxUserWithdrawDelaySet"));
        logger.info("[dConomy] Account Messages Loaded!");
        logger.info("[dConomy] Loading Admin Messages...");
        //Get/Set AdminMessages (300 Series)
        AdminMessages.A301.setMess(parseMess(AdminMessages.A301.plainMess(), "301-AccountReset"));
        AdminMessages.A302.setMess(parseMess(AdminMessages.A302.plainMess(), "302-AccountSet"));
        AdminMessages.A303.setMess(parseMess(AdminMessages.A303.plainMess(), "303-AccountRemove"));
        AdminMessages.A304.setMess(parseMess(AdminMessages.A304.plainMess(), "304-AccountAdd"));
        AdminMessages.A305.setMess(parseMess(AdminMessages.A305.plainMess(), "309-JointReset"));
        AdminMessages.A306.setMess(parseMess(AdminMessages.A306.plainMess(), "310-JointSet"));
        AdminMessages.A307.setMess(parseMess(AdminMessages.A307.plainMess(), "311-JointAdd"));
        AdminMessages.A308.setMess(parseMess(AdminMessages.A308.plainMess(), "312-JointRemove"));
        logger.info("[dConomy] Admin/Misc Messages Loaded!");
        logger.info("[dConomy] Loading Help Messages...");
        //Get/Set HelpMessages (500 Series)
        HelpMessages.H401.setMess(parseMess(HelpMessages.H401.plainMess(),"401-MoneyHelpOpen"));
        HelpMessages.H402.setMess(parseMess(HelpMessages.H402.plainMess(),"402-BankHelpOpen"));
        HelpMessages.H403.setMess(parseMess(HelpMessages.H403.plainMess(),"403-JointHelpOpen"));
        HelpMessages.H404.setMess(parseMess(HelpMessages.H404.plainMess(),"404-RequiredOptionalAlias"));
        HelpMessages.H405.setMess(parseMess(HelpMessages.H405.plainMess(),"405-PlayerAmountAccount"));
        HelpMessages.H406.setMess(parseMess(HelpMessages.H406.plainMess(),"406-UseMoneyHelp"));
        HelpMessages.H407.setMess(parseMess(HelpMessages.H407.plainMess(),"407-UseBankHelp"));
        HelpMessages.H408.setMess(parseMess(HelpMessages.H408.plainMess(),"408-UseJointHelp"));
        HelpMessages.H409.setMess(parseMess(HelpMessages.H409.plainMess(),"409-MoneyBase"));
        HelpMessages.H410.setMess(parseMess(HelpMessages.H410.plainMess(),"410-BankBase"));
        HelpMessages.H411.setMess(parseMess(HelpMessages.H411.plainMess(),"411-JointBase"));
        HelpMessages.H412.setMess(parseMess(HelpMessages.H412.plainMess(),"412-RankCommand"));
        HelpMessages.H413.setMess(parseMess(HelpMessages.H413.plainMess(),"413-RankTopCommand"));
        HelpMessages.H414.setMess(parseMess(HelpMessages.H414.plainMess(),"414-MoneyPayCommand"));
        HelpMessages.H415.setMess(parseMess(HelpMessages.H415.plainMess(),"415-MoneyAuto"));
        HelpMessages.H416.setMess(parseMess(HelpMessages.H416.plainMess(),"416-MoneySetAuto"));
        HelpMessages.H417.setMess(parseMess(HelpMessages.H417.plainMess(),"417-BankWithdraw"));
        HelpMessages.H418.setMess(parseMess(HelpMessages.H418.plainMess(),"418-BankDeposit"));
        HelpMessages.H419.setMess(parseMess(HelpMessages.H419.plainMess(),"419-JointWithdraw"));
        HelpMessages.H420.setMess(parseMess(HelpMessages.H420.plainMess(),"420-JointDeposit"));
        HelpMessages.H421.setMess(parseMess(HelpMessages.H421.plainMess(),"421-JointPay"));
        HelpMessages.H422.setMess(parseMess(HelpMessages.H422.plainMess(),"422-JointCreate"));
        HelpMessages.H423.setMess(parseMess(HelpMessages.H423.plainMess(),"423-JointDelete"));
        HelpMessages.H424.setMess(parseMess(HelpMessages.H424.plainMess(),"424-JointAddOwner"));
        HelpMessages.H425.setMess(parseMess(HelpMessages.H425.plainMess(),"425-JointRemoveOwner"));
        HelpMessages.H426.setMess(parseMess(HelpMessages.H426.plainMess(),"426-JointAddUser"));
        HelpMessages.H427.setMess(parseMess(HelpMessages.H427.plainMess(),"427-JointRemoveUser"));
        HelpMessages.H428.setMess(parseMess(HelpMessages.H428.plainMess(),"428-JointSetUserMax"));
        HelpMessages.H429.setMess(parseMess(HelpMessages.H429.plainMess(),"429-JointUserMaxCheck"));
        HelpMessages.H430.setMess(parseMess(HelpMessages.H430.plainMess(),"430-JointSetUserWithdrawDelay"));
        HelpMessages.H431.setMess(parseMess(HelpMessages.H431.plainMess(),"431-AccountSet"));
        HelpMessages.H432.setMess(parseMess(HelpMessages.H432.plainMess(),"432-AccountReset"));
        HelpMessages.H433.setMess(parseMess(HelpMessages.H433.plainMess(),"433-AccountAdd"));
        HelpMessages.H434.setMess(parseMess(HelpMessages.H434.plainMess(),"434-AccountRemove"));
        logger.info("[dConomy] Help Display Messages Loaded!");
        logger.info("[dConomy] Loading Logging Messages...");
        //Get/Set Logging (600 Series)
        LoggingMessages.L601.setMess(parseMess(LoggingMessages.L601.plainMess(), "601-PlayerPayPlayer"));
        LoggingMessages.L602.setMess(parseMess(LoggingMessages.L602.plainMess(), "602-PlayerDepositBank"));
        LoggingMessages.L603.setMess(parseMess(LoggingMessages.L603.plainMess(), "603-PlayerWithdrawBank"));
        LoggingMessages.L604.setMess(parseMess(LoggingMessages.L604.plainMess(), "604-PlayerWithdrawJoint"));
        LoggingMessages.L605.setMess(parseMess(LoggingMessages.L605.plainMess(), "605-PlayerDepositJoint"));
        LoggingMessages.L606.setMess(parseMess(LoggingMessages.L606.plainMess(), "606-PaidJoint"));
        LoggingMessages.L607.setMess(parseMess(LoggingMessages.L607.plainMess(), "607-PlayerPaidWithJoint"));
        LoggingMessages.L608.setMess(parseMess(LoggingMessages.L608.plainMess(), "608-PlayerPFBank"));
        LoggingMessages.L609.setMess(parseMess(LoggingMessages.L609.plainMess(), "609-PlayerPFPayWithBank"));
        LoggingMessages.L610.setMess(parseMess(LoggingMessages.L610.plainMess(), "610-PlayerPFPayWithJoint"));
        LoggingMessages.L611.setMess(parseMess(LoggingMessages.L611.plainMess(), "611-CreateJoint"));
        LoggingMessages.L612.setMess(parseMess(LoggingMessages.L612.plainMess(), "612-DeleteJoint"));
        LoggingMessages.L613.setMess(parseMess(LoggingMessages.L613.plainMess(), "613-Joint-AddOwner"));
        LoggingMessages.L614.setMess(parseMess(LoggingMessages.L614.plainMess(), "614-ViewTop"));
        LoggingMessages.L615.setMess(parseMess(LoggingMessages.L615.plainMess(), "615-Joint-AddUser"));
        LoggingMessages.L616.setMess(parseMess(LoggingMessages.L616.plainMess(), "616-Joint-RemoveOwner"));
        LoggingMessages.L617.setMess(parseMess(LoggingMessages.L617.plainMess(), "617-Joint-RemoveUser"));
        LoggingMessages.L618.setMess(parseMess(LoggingMessages.L618.plainMess(), "618-PAccount-Reset"));
        LoggingMessages.L619.setMess(parseMess(LoggingMessages.L619.plainMess(), "619-PAccount-Set"));
        LoggingMessages.L620.setMess(parseMess(LoggingMessages.L620.plainMess(), "620-PAccount-Add"));
        LoggingMessages.L621.setMess(parseMess(LoggingMessages.L621.plainMess(), "621-PAccount-Remove"));
        LoggingMessages.L622.setMess(parseMess(LoggingMessages.L622.plainMess(), "622-Bank-Reset"));
        LoggingMessages.L623.setMess(parseMess(LoggingMessages.L623.plainMess(), "623-Bank-Set"));
        LoggingMessages.L624.setMess(parseMess(LoggingMessages.L624.plainMess(), "624-Bank-Add"));
        LoggingMessages.L625.setMess(parseMess(LoggingMessages.L625.plainMess(), "625-Bank-Remove"));
        LoggingMessages.L626.setMess(parseMess(LoggingMessages.L626.plainMess(), "626-Joint-Reset"));
        LoggingMessages.L627.setMess(parseMess(LoggingMessages.L627.plainMess(), "627-Joint-Set"));
        LoggingMessages.L628.setMess(parseMess(LoggingMessages.L628.plainMess(), "628-Joint-Add"));
        LoggingMessages.L629.setMess(parseMess(LoggingMessages.L629.plainMess(), "629-Joint-Remove"));
        LoggingMessages.L630.setMess(parseMess(LoggingMessages.L630.plainMess(), "630-Joint-UserAdded"));
        LoggingMessages.L631.setMess(parseMess(LoggingMessages.L631.plainMess(), "631-Player-PayForwardDepositJoint"));
        LoggingMessages.L632.setMess(parseMess(LoggingMessages.L632.plainMess(), "632-Player-UsedPFtoPayJointUsingJoint"));
        LoggingMessages.L633.setMess(parseMess(LoggingMessages.L633.plainMess(), "633-Player-UsedPFtoPayJointUsingBank"));
        LoggingMessages.L634.setMess(parseMess(LoggingMessages.L634.plainMess(), "634-PlayerSetPFtoJoint"));
        LoggingMessages.L635.setMess(parseMess(LoggingMessages.L635.plainMess(), "635-PlayerSetPFtoBank"));
        LoggingMessages.L636.setMess(parseMess(LoggingMessages.L636.plainMess(), "636-Player-TurnPFOff"));
        LoggingMessages.L637.setMess(parseMess(LoggingMessages.L637.plainMess(), "637-Player-SetJointMaxUserWithdraw"));
        LoggingMessages.L638.setMess(parseMess(LoggingMessages.L638.plainMess(), "638-Player-ViewedOwnRank"));
        LoggingMessages.L639.setMess(parseMess(LoggingMessages.L639.plainMess(), "639-Player-ViewedAnothersRank"));
        logger.info("[dConomy] Logging Messages Loaded!");
        logger.info("[dConomy] Messages Loaded!");
    }
    
    private static String parseMess(String def, String key){
        String value = def;
        if(dCoMess.containsKey(key)){
            value = dCoMess.getProperty(key);
            if(value.equals("")){
                logger.warning("[dConomy] Value not found for '"+key+"' Using default of '"+def+"'");
                value = def;
            }
            else{
                value = parseColors(value);
            }
        }
        else{
            logger.warning("[dConomy] Key: '"+key+"' not found. Using default of '"+def+"'");
        }
        return value;
    }
    
    private static String parseColors(String mess){
        String newmess = mess;
        newmess = newmess.replace("<black>", "\u00A70");
        newmess = newmess.replace("<navy>", "\u00A71");
        newmess = newmess.replace("<green>", "\u00A72");
        newmess = newmess.replace("<blue>", "\u00A73");
        newmess = newmess.replace("<red>", "\u00A74");
        newmess = newmess.replace("<purple>", "\u00A75");
        newmess = newmess.replace("<gold>", "\u00A76");
        newmess = newmess.replace("<lightgray>", "\u00A77");
        newmess = newmess.replace("<gray>", "\u00A78");
        newmess = newmess.replace("<darkpurple>", "\u00A79");
        newmess = newmess.replace("<lightgreen>", "\u00A7a");
        newmess = newmess.replace("<lightblue>", "\u00A7b");
        newmess = newmess.replace("<rose>", "\u00A7c");
        newmess = newmess.replace("<lightpurple>", "\u00A7d");
        newmess = newmess.replace("<yellow>", "\u00A7e");
        newmess = newmess.replace("<white>", "\u00A7f");
        newmess = newmess.replace("<bold>", "\u00A7l");
        newmess = newmess.replace("<striked>", "\u00A7m");
        newmess = newmess.replace("<under>", "\u00A7n");
        newmess = newmess.replace("<italic>", "\u00A7o");
        newmess = newmess.replace("<reset>", "\u00A7r");
        return newmess;
    }
    
    protected static final String parseMessage(String message, String username, String type, double amount, int rank){
        String parsedMessage = message;
        
        //int x = (int)Math.floor((jwreset - System.currentTimeMillis()) / 1000);
        //int xm = (int)Math.floor(x / (60));
        //String mins = String.valueOf(JWDelay);
        
        parsedMessage = parsedMessage.replace("<m>", DCoProperties.getMoneyName());
        parsedMessage = parsedMessage.replace("<a>", String.valueOf(displayform.format(amount)));
        //parsedMessage = parsedMessage.replace("<min>", mins);
        //parsedMessage = parsedMessage.replace("<xmin>", String.valueOf(xm));
        
        if(username != null){
            parsedMessage = parsedMessage.replace("<p>", username);
        }
        
        if(type != null){
            parsedMessage = parsedMessage.replace("<acc>", type);
        }
        
        if(rank != -1){
            parsedMessage = parsedMessage.replace("<rank>", type);
        }
        
        return parsedMessage;
    }
    
    protected static final String parseAdminMessage(String message, String username, String type, double amount){
        String parsedMessage = message;
        
        parsedMessage = parsedMessage.replace("<m>", DCoProperties.getMoneyName());
        parsedMessage = parsedMessage.replace("<a>", String.valueOf(displayform.format(amount)));
        
        if(username != null){
            parsedMessage = parsedMessage.replace("<p>", username);
        }
        
        if(type != null){
            parsedMessage = parsedMessage.replace("<acc>", type);
        }
        
        return parsedMessage;
    }
    
    protected static final String parseError(String message, String arg){
        message = message.replace("<m>", DCoProperties.getMoneyName());
        if(arg != null){
            message = message.replace("<p>", arg);
            message = message.replace("<acc>", arg);
        }
        return message;
    }
    
    protected static final String parseLog(String message, String p1, String p2, String amount, String account){
        if(p1 != null){
            message = message.replace("<p1>", p1);
        }
        if(p2 != null){
            message = message.replace("<p2>", p2);
        }
        if(amount != null){
            message = message.replace("<a>", amount);
        }
        if(account != null){
            message = message.replace("<acc>", account);
        }
        return message;
    }
}
