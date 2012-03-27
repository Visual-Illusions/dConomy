package net.visualillusionsent.dconomy.messages;

import java.util.Properties;
import java.util.logging.Logger;

import net.visualillusionsent.dconomy.data.DCoProperties;

public class LoadMessages {
    private Logger logger = Logger.getLogger("Minecraft");
    private Properties dCMess;
    
    void LoadMessage(){
        //Get/Set ErrorMessages (100 Series)
        ErrorMessages.E101.setMess(parseMess(ErrorMessages.E101.plainMess(), "101-Player-NotEnoughMoney"));
        ErrorMessages.E102.setMess(parseMess(ErrorMessages.E102.plainMess(), "102-Bank-NotEnoughMoney"));
        ErrorMessages.E103.setMess(parseMess(ErrorMessages.E103.plainMess(), "103-NumberFormatError"));
        ErrorMessages.E104.setMess(parseMess(ErrorMessages.E104.plainMess(), "104-Command-NoPermission"));
        ErrorMessages.E105.setMess(parseMess(ErrorMessages.E105.plainMess(), "105-Joint-NotEnoughMoney"));
        ErrorMessages.E106.setMess(parseMess(ErrorMessages.E106.plainMess(), "106-AmountNotSpecified"));
        ErrorMessages.E107.setMess(parseMess(ErrorMessages.E107.plainMess(), "107-Joint-NoAccess"));
        ErrorMessages.E108.setMess(parseMess(ErrorMessages.E108.plainMess(), "108-PlayerNotFound"));
        ErrorMessages.E109.setMess(parseMess(ErrorMessages.E109.plainMess(), "109-NegativeEntered"));
        ErrorMessages.E110.setMess(parseMess(ErrorMessages.E110.plainMess(), "110-JointNotFound"));
        ErrorMessages.E111.setMess(parseMess(ErrorMessages.E111.plainMess(), "111-JointNameTaken"));
        ErrorMessages.E112.setMess(parseMess(ErrorMessages.E112.plainMess(), "112-Joint-OwnerNotSpecified"));
        ErrorMessages.E113.setMess(parseMess(ErrorMessages.E113.plainMess(), "113-InvalidCommand"));
        ErrorMessages.E114.setMess(parseMess(ErrorMessages.E114.plainMess(), "114-Joint-PlayerNotSpecified"));
        ErrorMessages.E115.setMess(parseMess(ErrorMessages.E115.plainMess(), "115-JointNameTooLong"));
        ErrorMessages.E116.setMess(parseMess(ErrorMessages.E116.plainMess(), "116-Joint-User-CannotWithdrawAgainYet"));
        ErrorMessages.E117.setMess(parseMess(ErrorMessages.E117.plainMess(), "117-Joint-User-WithdrawTooMuch"));
        ErrorMessages.E118.setMess(parseMess(ErrorMessages.E118.plainMess(), "118-Player-CannotPaySelf"));
        ErrorMessages.E119.setMess(parseMess(ErrorMessages.E119.plainMess(), "119-Player-AlreadyJointUser"));
        ErrorMessages.E120.setMess(parseMess(ErrorMessages.E120.plainMess(), "120-PlayerNotSpecified"));
        ErrorMessages.E121.setMess(parseMess(ErrorMessages.E121.plainMess(), "121-Joint-UserNotSpecified"));
        ErrorMessages.E122.setMess(parseMess(ErrorMessages.E122.plainMess(), "122-Joint-PlayerAlreadyOwner"));
        ErrorMessages.E123.setMess(parseMess(ErrorMessages.E123.plainMess(), "123-Joint-OwnerNotSpecified"));
        ErrorMessages.E124.setMess(parseMess(ErrorMessages.E124.plainMess(), "124-Joint-PlayerAlreadyNotUser"));
        ErrorMessages.E125.setMess(parseMess(ErrorMessages.E125.plainMess(), "125-Joint-PlayerAlreadyNotOwner"));
        ErrorMessages.E126.setMess(parseMess(ErrorMessages.E126.plainMess(), "126-NegativeNumber"));
        ErrorMessages.E127.setMess(parseMess(ErrorMessages.E127.plainMess(), "127-BalanceNegative"));
        logger.info("[dConomy] - ErrorMessages Loaded!");
        
        //Get/Set PlayerMessages (200 Series)
        PlayerMessages.P201.setMess(parseMess(PlayerMessages.P201.plainMess(), "201-Player-AccountBalance"));
        PlayerMessages.P202.setMess(parseMess(PlayerMessages.P202.plainMess(), "202-Player-BankBalance"));
        PlayerMessages.P203.setMess(parseMess(PlayerMessages.P203.plainMess(), "203-Player-BankWithdraw"));
        PlayerMessages.P204.setMess(parseMess(PlayerMessages.P204.plainMess(), "204-Player-BankDeposit"));
        PlayerMessages.P205.setMess(parseMess(PlayerMessages.P205.plainMess(), "205-Player-BankNewBalance"));
        PlayerMessages.P206.setMess(parseMess(PlayerMessages.P206.plainMess(), "206-Player-AccountNewBalance"));
        PlayerMessages.P207.setMess(parseMess(PlayerMessages.P207.plainMess(), "207-Player-SentPaymentTo"));
        PlayerMessages.P208.setMess(parseMess(PlayerMessages.P208.plainMess(), "208-Player-ReceivedPaymentFrom"));
        PlayerMessages.P209.setMess(parseMess(PlayerMessages.P209.plainMess(), "209-Player-RankSelf"));
        PlayerMessages.P210.setMess(parseMess(PlayerMessages.P210.plainMess(), "210-Player-RankOther"));
        PlayerMessages.P211.setMess(parseMess(PlayerMessages.P211.plainMess(), "211-Player-PayForwardJoint"));
        PlayerMessages.P212.setMess(parseMess(PlayerMessages.P212.plainMess(), "212-Player-PaymentSentFromJoint"));
        PlayerMessages.P213.setMess(parseMess(PlayerMessages.P213.plainMess(), "213-Player-PayForwardBank"));
        PlayerMessages.P214.setMess(parseMess(PlayerMessages.P214.plainMess(), "214-Player-PaymentSentFromBank"));
        PlayerMessages.P215.setMess(parseMess(PlayerMessages.P215.plainMess(), "215-Player-PayForwardSetJoint"));
        PlayerMessages.P216.setMess(parseMess(PlayerMessages.P216.plainMess(), "216-Player-PayForwardSetBank"));
        PlayerMessages.P217.setMess(parseMess(PlayerMessages.P217.plainMess(), "217-Player-PaymentSentToJoint"));
        PlayerMessages.P218.setMess(parseMess(PlayerMessages.P218.plainMess(), "218-Player-PayForwardTurnedOff"));
        PlayerMessages.P219.setMess(parseMess(PlayerMessages.P219.plainMess(), "219-Player-PayForwardingIsSetJoint"));
        PlayerMessages.P220.setMess(parseMess(PlayerMessages.P220.plainMess(), "220-Player-PayForwardingIsSetBank"));
        PlayerMessages.P221.setMess(parseMess(PlayerMessages.P221.plainMess(), "221-Player-PayForwardingNotActive"));
        PlayerMessages.P222.setMess(parseMess(PlayerMessages.P222.plainMess(), "222-Player-AccountCheck"));
        PlayerMessages.P223.setMess(parseMess(PlayerMessages.P223.plainMess(), "223-Player-BankCheck"));
        logger.info("[dConomy] - Player Messages Loaded!");
        
        //Get/Set JointMessages (300 Series)
        JointMessages.J301.setMess(parseMess(JointMessages.J301.plainMess(), "301-Joint-AccountBalance"));
        JointMessages.J302.setMess(parseMess(JointMessages.J302.plainMess(), "302-Joint-NewBalance"));
        JointMessages.J303.setMess(parseMess(JointMessages.J303.plainMess(), "303-Joint-Withdraw"));
        JointMessages.J304.setMess(parseMess(JointMessages.J304.plainMess(), "304-Joint-Deposit"));
        JointMessages.J305.setMess(parseMess(JointMessages.J305.plainMess(), "305-Joint-Created"));
        JointMessages.J306.setMess(parseMess(JointMessages.J306.plainMess(), "306-Joint-OwnerAdded"));
        JointMessages.J307.setMess(parseMess(JointMessages.J307.plainMess(), "307-Joint-OwnerRemoved"));
        JointMessages.J308.setMess(parseMess(JointMessages.J308.plainMess(), "308-Joint-AccountDeleted"));
        JointMessages.J309.setMess(parseMess(JointMessages.J309.plainMess(), "309-Joint-UserAdded"));
        JointMessages.J310.setMess(parseMess(JointMessages.J310.plainMess(), "310-Joint-UserRemoved"));
        JointMessages.J311.setMess(parseMess(JointMessages.J311.plainMess(), "311-Joint-MaxUserWithdrawCheck"));
        JointMessages.J312.setMess(parseMess(JointMessages.J312.plainMess(), "312-Joint-MaxUserWithdrawSet"));
        logger.info("[dConomy] - Joint Account Messages Loaded!");
        
        //Get/Set AdminMessages (400 Series)
        AdminMessages.A401.setMess(parseMess(AdminMessages.A401.plainMess(), "401-Rank-Top"));
        AdminMessages.A402.setMess(parseMess(AdminMessages.A402.plainMess(), "402-Rank-Sorting"));
        AdminMessages.A403.setMess(parseMess(AdminMessages.A403.plainMess(), "403-Rank-NoTop"));
        AdminMessages.A404.setMess(parseMess(AdminMessages.A404.plainMess(), "404-Admin-Player-Reset"));
        AdminMessages.A405.setMess(parseMess(AdminMessages.A405.plainMess(), "405-Admin-Player-Set"));
        AdminMessages.A406.setMess(parseMess(AdminMessages.A406.plainMess(), "406-Admin-Player-Remove"));
        AdminMessages.A407.setMess(parseMess(AdminMessages.A407.plainMess(), "407-Admin-Player-Add"));
        AdminMessages.A408.setMess(parseMess(AdminMessages.A408.plainMess(), "408-Admin-Bank-Reset"));
        AdminMessages.A409.setMess(parseMess(AdminMessages.A409.plainMess(), "409-Admin-Bank-Set"));
        AdminMessages.A410.setMess(parseMess(AdminMessages.A410.plainMess(), "410-Admin-Bank-Remove"));
        AdminMessages.A411.setMess(parseMess(AdminMessages.A411.plainMess(), "411-Admin-Bank-Add"));
        AdminMessages.A412.setMess(parseMess(AdminMessages.A412.plainMess(), "412-Admin-Joint-Reset"));
        AdminMessages.A413.setMess(parseMess(AdminMessages.A413.plainMess(), "413-Admin-Joint-Set"));
        AdminMessages.A414.setMess(parseMess(AdminMessages.A414.plainMess(), "414-Admin-Joint-Add"));
        AdminMessages.A415.setMess(parseMess(AdminMessages.A415.plainMess(), "415-Admin-Joint-Remove"));
        logger.info("[dConomy] - Admin/Misc Messages Loaded!");
        
        /*
        //Help Display (500 Series)
        H501 = parseString(H501, "501-Money-HelpOpen");
        H502 = parseString(H502, "502-RequiredOptionalAlias");
        H503 = parseString(H503, "503-Money-Base");
        H504 = parseString(H504, "504-Money-Pay");
        H505 = parseString(H505, "505-Money-Rank");
        H506 = parseString(H506, "506-Money-Top");
        H507 = parseString(H507, "507-Money-Set");
        H508 = parseString(H508, "508-Money-Reset");
        H509 = parseString(H509, "509-Money-Add");
        H510 = parseString(H510, "510-Money-Remove");
        H511 = parseString(H511, "511-Money-Auto");
        H512 = parseString(H512, "512-Money-SetAuto");
        H513 = parseString(H513, "513-Money-UseJointHelp");
        H514 = parseString(H514, "514-Money-UseBankHelp");
        H515 = parseString(H515, "515-Bank-HelpOpen");
        H516 = parseString(H516, "516-Bank-Base");
        H517 = parseString(H517, "517-Bank-Withdraw");
        H518 = parseString(H518, "518-Bank-Deposit");
        H519 = parseString(H519, "519-Bank-Reset");
        H520 = parseString(H520, "520-Bank-Set");
        H521 = parseString(H521, "521-Bank-Add");
        H522 = parseString(H522, "522-Bank-Remove");
        H523 = parseString(H523, "523-Joint-HelpOpen");
        H524 = parseString(H524, "524-Joint-Base");
        H525 = parseString(H525, "525-Joint-Withdraw");
        H526 = parseString(H526, "526-Joint-Deposit");
        H527 = parseString(H527, "527-Joint-Pay");
        H528 = parseString(H528, "528-Joint-Create");
        H529 = parseString(H529, "529-Joint-Delete");
        H530 = parseString(H530, "530-Joint-AddOwner");
        H531 = parseString(H531, "531-Joint-RemoveOwner");
        H532 = parseString(H532, "532-Joint-AddUser");
        H533 = parseString(H533, "533-Joint-RemoveUser");
        H534 = parseString(H534, "534-Joint-UserMax");
        H535 = parseString(H535, "535-Joint-Reset");
        H536 = parseString(H536, "536-Joint-Set");
        H537 = parseString(H537, "537-Joint-Add");
        H538 = parseString(H538, "538-Joint-Remove");
        H539 = parseString(H539, "539-Joint-UserMaxCheck");
        H540 = parseString(H540, "540-Help-PlayerAmountAccount");
        H541 = parseString(H541, "541-Help-UseMoneyAdmin");
        H542 = parseString(H542, "542-Help-UseBankAdmin");
        H543 = parseString(H543, "543-Help-UseJointAdmin");
        H544 = parseString(H544, "544-Help-MoneyAdminOpen");
        H545 = parseString(H545, "545-Help-BankAdminOpen");
        H546 = parseString(H546, "546-Help-JointAdminOpen");
        dCD.log.info("[dConomy] - Help Display Messages Loaded!");
        
        /*
        //Logging (600 Series)
        L601 = parseString(L601, "601-PlayerPayPlayer");
        L602 = parseString(L602, "602-PlayerDepositBank");
        L603 = parseString(L603, "603-PlayerWithdrawBank");
        L604 = parseString(L604, "604-PlayerWithdrawJoint");
        L605 = parseString(L605, "605-PlayerDepositJoint");
        L606 = parseString(L606, "606-PaidJoint");
        L607 = parseString(L607, "607-PlayerPaidWithJoint");
        L608 = parseString(L608, "608-PlayerPFBank");
        L609 = parseString(L609, "609-PlayerPFPayWithBank");
        L610 = parseString(L610, "610-PlayerPFPayWithJoint");
        L611 = parseString(L611, "611-CreateJoint");
        L612 = parseString(L612, "612-DeleteJoint");
        L613 = parseString(L613, "613-Joint-AddOwner");
        L614 = parseString(L614, "614-ViewTop");
        L615 = parseString(L615, "615-Joint-AddUser");
        L616 = parseString(L616, "616-Joint-RemoveOwner");
        L617 = parseString(L617, "617-Joint-RemoveUser");
        L618 = parseString(L618, "618-PAccount-Reset");
        L619 = parseString(L619, "619-PAccount-Set");
        L620 = parseString(L620, "620-PAccount-Add");
        L621 = parseString(L621, "621-PAccount-Remove");
        L622 = parseString(L622, "622-Bank-Reset");
        L623 = parseString(L623, "623-Bank-Set");
        L624 = parseString(L624, "624-Bank-Add");
        L625 = parseString(L625, "625-Bank-Remove");
        L626 = parseString(L626, "626-Joint-Reset");
        L627 = parseString(L627, "627-Joint-Set");
        L628 = parseString(L628, "628-Joint-Add");
        L629 = parseString(L629, "629-Joint-Remove");
        L630 = parseString(L630, "630-Joint-UserAdded");
        L631 = parseString(L631, "631-Player-PayForwardDepositJoint");
        L632 = parseString(L632, "632-Player-UsedPFtoPayJointUsingJoint");
        L633 = parseString(L633, "633-Player-UsedPFtoPayJointUsingBank");
        L634 = parseString(L634, "634-PlayerSetPFtoJoint");
        L635 = parseString(L635, "635-PlayerSetPFtoBank");
        L636 = parseString(L636, "636-Player-TurnPFOff");
        L637 = parseString(L637, "637-Player-SetJointMaxUserWithdraw");
        L638 = parseString(L638, "638-Player-ViewedOwnRank");
        L639 = parseString(L639, "639-Player-ViewedAnothersRank");
        dCD.log.info("[dConomy] - Logging Messages Loaded!"); */
    }
    
    private String parseMess(String def, String key){
        String value = def;
        if(dCMess.containsKey(key)){
            value = dCMess.getProperty(key);
            if(value.equals("")){
                logger.warning("[dConomy] - Value not found for '"+key+"' Using default of '"+def+"'");
                value = def;
            }
            else{
                value = parseColors(value);
            }
        }
        else{
            logger.warning("[dConomy] - Key: '"+key+"' not found. Using default of '"+def+"'");
        }
        return value;
    }
    
    private String parseColors(String mess){
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
    
    protected static final String parseMessage(String message, String username, String type, double amount){
        String parsedMessage = message;
        
        //int x = (int)Math.floor((jwreset - System.currentTimeMillis()) / 1000);
        //int xm = (int)Math.floor(x / (60));
        //String mins = String.valueOf(JWDelay);
        
        parsedMessage = parsedMessage.replace("<m>", DCoProperties.getMoneyName());
        //parsedMessage = parsedMessage.replace("<a>", String.valueOf(displayform.format(amount)));
        //parsedMessage = parsedMessage.replace("<min>", mins);
        //parsedMessage = parsedMessage.replace("<xmin>", String.valueOf(xm));
        
        if(username != null){
            parsedMessage = parsedMessage.replace("<p>", username);
        }
        
        if(type != null){
            parsedMessage = parsedMessage.replace("<acc>", type);
            parsedMessage = parsedMessage.replace("<rank>", type);
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
}
