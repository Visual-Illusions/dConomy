
/**
* dConomy v1.x
* Copyright (C) 2011-2012 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>
*
* This file is part of dConomy.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.
*/

public class dCMessages {
	private dCData dCD;
	private PropertiesFile dCMess;
	private final String NL = System.getProperty("line.separator");
	
	/*Messages Start*/
	String
	/*Error Messages (100 Series)*/
	E101 = "<rose>You do not have enough <m> to complete transaction!",
	E102 = "<rose>Your bank does not have enough <m> to complete transaction!",
	E103 = "<rose>You did not enter a proper number!",
	E104 = "<rose>You do not have permission to use that command!",
	E105 = "<rose><acc> does not have the funds for this transaction!",
	E106 = "<rose>You didn't specify an amount!",
	E107 = "<rose>You do not have access to <acc>!",
	E108 = "<rose>Player not found!",
	E109 = "<rose>Could not find account for <p>!",
	E110 = "<rose><acc> does not exist!",
	E111 = "<rose><acc> is already in use!",
	E112 = "<rose>You didn't specify an owner!",
	E113 = "<rose>Invaild dConomy Command!",
	E114 = "<rose>You didn't specify a player!",
	E115 = "<rose>Account names cannot be longer than 32 Characters!",
	E116 = "<rose>You cannot make another withdraw for <xmin> Minutes!",
	E117 = "<rose>You cannot withdraw that amount at one time!",
	E118 = "<rose>You cannot pay yourself!",
	E119 = "<rose><p> is already a User!",
	E120 = "<rose>Player Not Specified!",
	E121 = "<rose>Joint Account User not specified!",
	E122 = "<rose><p> is already a Owner!",
	E123 = "<rose>Joint Account Owner not specified!",
	E124 = "<rose><p> wasn't a User!",
	E125 = "<rose><p> wasn't a Owner!",
	E126 = "<rose>Amount cannot be a negative!",
	E127 = "<rose>Balance cannot become negative!",
	
	/*Player Messages (200 Series)*/
	P201 = "<lightgreen>Your <lightblue>Account Balance<lightgreen> is <yellow><a> <m><lightgreen>.",
	P202 = "<lightgreen>Your <lightblue>Bank Balance<lightgreen> is <yellow><a> <m><lightgreen>.",
	P203 = "<lightgreen>Your have withdrawn <yellow><a> <m><lightgreen> from your <lightblue>Bank Account<lightgreen>.",
	P204 = "<lightgreen>Your have deposited <yellow><a> <m><lightgreen> into your <lightblue>Bank Account<lightgreen>.",
	P205 = "<lightgreen>Your new bank balance is <yellow><a> <m><lightgreen>.",
	P206 = "<lightgreen>Your new balance is <yellow><a> <m><lightgreen>.",
	P207 = "<lightgreen>You have sent <yellow><a> <m> to <gold><p><lightgreen>.",
	P208 = "<lightgreen>You have received <yellow><a> <m><lightgreen> from <gold><p><lightgreen>.",
	P209 = "<lightgreen>You are ranked <yellow><rank><lightgreen>.",
	P210 = "<gold><p><lightgreen> is ranked <yellow><rank><lightgreen>.",
	P211 = "<lightgreen>Your pay has been forwarded to JointAccount: <lightblue><acc><lightgreen>.",
	P212 = "<lightgreen>Payment has been sent from Joint Account: <lightblue><acc><lightgreen>.",
	P213 = "<lightgreen>Your pay has been forwarded to your <lightblue>Bank Account<lightgreen>.",
	P214 = "<lightgreen>Payment has been sent from your <lightblue>Bank Account<lightgreen>.",
	P215 = "<lightgreen>You have set up Pay Forwarding to Joint Account: <lightblue><acc><lightgreen>.",
	P216 = "<lightgreen>You have set up Pay Forwarding to your <lightblue>Bank Account<lightgreen>.",
	P217 = "<lightgreen>You have paid <amount> to Joint Account: <lightblue><acc><lightgreen>.",
	P218 = "<lightgreen>You have turned Pay Forwarding <rose>OFF<lightgreen>.",
	P219 = "<lightgreen>You have Pay Forwarding set to Joint Account: <lightblue><acc><lightgreen>.",
	P220 = "<lightgreen>You have Pay Forwarding set to your <lightblue> Bank Account<lightgreen>.",
	P221 = "<lightgreen>Pay Forwarding is not active.",
	P222 = "<gold><p>'s<lightgreen> Account Balance is <yellow><a> <m><lightgreen>.",
	P223 = "<gold><p>'s<lightgreen> Bank Balance is <yellow><a> <m><lightgreen>.",
	
	/*Joint Account Message (300 Series) */
	J301 = "<lightblue><acc><lightgreen> balance is <yellow><a> <m><lightgreen>.",
	J302 = "<lightblue><acc><lightgreen> new balance is <yellow><a> <m><lightgreen>.",
	J303 = "<lightgreen>You have withdrawn <yellow><a> <m> <lightgreen> from <lightblue><acc><lightgreen>.",
	J304 = "<lightgreen>You have deposited <yellow><a> <m> into <lightblue><acc><lightgreen>.",
	J305 = "<lightgreen>Created JointAccount <lightblue><acc><lightgreen>.",
	J306 = "<lightgreen>Added <gold><p><lightgreen> as Owner to Joint Account: <lightblue><acc><lightgreen>.",
	J307 = "<lightgreen>Removed <gold><p><lightgreen> as Owner from  Joint Account: <lightblue><acc><lightgreen>.",
	J308 = "<lightgreen>Deleted Joint Account: <lightblue><acc><lightgreen>.",
	J309 = "<lightgreen>Added <gold><p><lightgreen> as User to Joint Account: <lightblue><acc><lightgreen>.",
	J310 = "<lightgreen>Removed <gold><p><lightgreen> as User from Joint Account: <lightblue><acc><lightgreen>.",
	J311 = "<lightgreen>Max User Withdraw is <yellow><a> <m><lightgreen> per <purple><min> Minutes<lightgreen>.",
	J312 = "<lightgreen>You have set Max User Withdraw to <yellow><a> <m><lightgreen>.",
		
	/*Admin/Misc  (400 Series) */
	A401 = "<green>-----Top <yellow><rank> <green>Richest Players-----",
	A402 = "<green>   <rank>. <gold><p> <green>[<yellow><a> <m><green>]",
	A403 = "<rose>   No Top Players Yet!",
	A404 = "<gold><p>'s Account<lightgreen> has been reset to default balance.",
	A405 = "<gold><p>'s Account<lightgreen> has been set to <a> <m><lightgreen>.",
	A406 = "<lightgreen>You have deducted <yellow><a> <m><lightgreen> from <gold><p>' Account<lightgreen>.",
	A407 = "<yellow><a> <m><lightgreen> has been added to <gold><p>'s Account<lightgreen>.",
	A408 = "<gold><p>'s Bank<lightgreen> has been reset to 0.",
	A409 = "<gold><p>'s Bank<lightgreen> has been set to <a> <m><lightgreen>.",
	A410 = "<lightgreen>You have removed <yellow><a> <m><lightgreen> from <gold><p>' Bank<lightgreen>.",
	A411 = "<yellow><a> <m><lightgreen> has been added to <gold><p>'s Bank<lightgreen>.",
	A412 = "<lightgreen> Joint Account: <lightblue><a>'s Balance<lightgreen> has been reset to 0.",
	A413 = "<lightgreen> Joint Account: <lightblue><acc>'s Balance<lightgreen> has been set to <yellow><a> <m><lightgreen>.",
	A414 = "<yellow><a> <m><lightgreen> has been added to Joint Account: <lightblue><acc>'s Balance<lightgreen>.",
	A415 = "<yellow><a> <m><lightgreen> has been removed from Joint Account: <lightblue><acc>'s Balance<lightgreen>.",
		
	/*Help (500 Series) */
	H501 = "<green>|--------------<gold>dConomy /money Help<green>--------------|",
	H502 = "<green>|----<rose><REQUIRED><green>----<lightblue>[Optional]<green>----<yellow>(Alias)<green>-|",
	H503 = "   <gold>/money<green>: Basic Command/Display account balance",
	H504 = "   <gold>pay <yellow>(-p) <rose><p> <a><green>: Pay Player Amount",
	H505 = "   <gold>rank <yellow>(-r) <lightblue>[p]<green>: Display Rank Position",
	H506 = "   <gold>top <yellow>(-t) <lightblue>[a]<green>: Display Richest Players",
	H507 = "   <gold>set <yellow>(-s) <rose><p> <a><green>: Set player's account to amount",
	H508 = "   <gold>reset <yellow>(-rt) <rose><p><green>: Reset players account to default",
	H509 = "   <gold>add <yellow>(-a) <rose><p> <a><green>: Add amount to player's account",
	H510 = "   <gold>remove <yellow>(-rm) <rose><p> <a><green>: Remove amount from player's account",
	H511 = "   <gold>auto <yellow>(-au) <green>: checks autodep account settings",
	H512 = "   <gold>setauto <yellow>(-sa) <rose><acc|OFF><green>: Changes or sets autodep account",
	H513 = "   <green>Use <gold>/bank ? <green>for help with Bank commands",
	H514 = "   <green>Use <gold>/joint ? <green>for help with Joint Accounts",
	H515 = "<green>|--------------<gold>dConomy /bank Help<green>--------------|",
	H516 = "   <gold>/bank<green>: Basic Command/Display bank balance",
	H517 = "   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from bank account",
	H518 = "   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into bank account",
	H519 = "   <gold>reset <yellow>(-rt) <rose><p><green>: reset player's bank to 0",
	H520 = "   <gold>set <yellow>(-s) <rose><p> <amount><green>: set player's bank to amount",
	H521 = "   <gold>add <yellow>(-a) <rose><p><green>: add amount to player's bank",
	H522 = "   <gold>remove <yellow>(-rm) <rose><p> <a><green>: remove amount from player's bank",
	H523 = "<green>|--------------<gold>dConomy /joint Help<green>--------------|",
	H524 = "   <gold>/joint <account><green>: Basic Command/Display JointAccount balance",
	H525 = "   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from Account",
	H526 = "   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into Account",
	H527 = "   <gold>pay <yellow>(-p) <rose><p><green>: pay Account",
	H528 = "   <gold>create <yellow>(-c) <green>: create Account",
	H529 = "   <gold>delete <yellow>(-del) <green>: deposit into Account",
	H530 = "   <gold>addowner <yellow>(-ao) <rose><p><green>: add owner to Account",
	H531 = "   <gold>removeowner <yellow>(-ro) <rose><p><green>: remove owner from Account",
	H532 = "   <gold>adduser <yellow>(-au) <rose><p><green>: add user to Account",
	H533 = "   <gold>removeuser <yellow>(-ru) <rose><p><green>: remove user from Account",
	H534 = "   <gold>setusermax <yellow>(-su) <rose><a><green>: set UserMaxWithdraw per set period",
	H535 = "   <gold>reset <yellow>(-rt) <rose><a><green>: reset Account to 0",
	H536 = "   <gold>set <yellow>(-s) <rose><a><green>: set Account to amount",
	H537 = "   <gold>add <yellow>(-a) <rose><a><green>: add amount to Account",
	H538 = "   <gold>remove <yellow>(-rm) <rose><a><green>: remove amount to Account",
	H539 = "   <gold>usermax <yellow>(-um) <rose><a><green>: checks UserMaxWithdraw per set period",
	H540 = "<green>|---- <p> = Player <a> = Amount <acc> = Account ----|",
	H541 = "   <green>Use <gold>/money ? admin <green>for help with Money Admin commands",
	H542 = "   <green>Use <gold>/bank ? admin <green>for help with Bank Admin commands",
	H543 = "   <green>Use <gold>/joint ? <green>for help with Joint Admin commands",
	H544 = "<green>|------------<gold>dConomy /money Admin Help<green>------------|",
	H545 = "<green>|------------<gold>dConomy /bank Admin Help<green>------------|",
	H546 = "<green>|------------<gold>dConomy /joint Admin Help<green>------------|",
		
	/*Logging (600 Series)*/
	L601 = "<p1> paid <p2> <a>",
	L602 = "<p1> deposited <a> into their Bank",
	L603 = "<p1> withdrew <a> from their Bank Account",
	L604 = "<p1> withdrew <a> from Joint Account: <acc>",
	L605 = "<p1> deposited <a> from Joint Account: <acc>",
	L606 = "<p1> paid <a> to Joint Account: <acc>",
	L607 = "<p1> paid <p2> using Join Account: <acc>",
	L608 = "<p1> used PayForwarding to deposit <acc> directly into their Bank",
	L609 = "<p1> used PayForwarding connected to their Bank to pay <a> to <p2> ",
	L610 = "<p1> used PayForwarding connected to 'Joint Account: <acc>' to pay <p2>",
	L611 = "<p1> created Joint Account: <acc>",
	L612 = "<p1> deleted Joint Account: <acc>",
	L613 = "<p1> added <p2> as 'Owner' to Joint Account: <acc>",
	L614 = "<p1> veiwed Top <a> Richest Players",
	L615 = "<p1> added <p2> as 'User' to Joint Account: <acc>",
	L616 = "<p1> removed <p2> as 'Owner' to Joint Account: <acc>",
	L617 = "<p1> removed <p2> as 'User' to Joint Account: <acc>",
	L618 = "<p1> reset <p2> Account to default balance",
	L619 = "<p1> set <p2>'s Account to <a>",
	L620 = "<p1> added <a> to <p2>'s Account",
	L621 = "<p1> removed <a> from <p2>'s Account",
	L622 = "<p1> reset <p2>'s Bank to default balance",
	L623 = "<p1> set <p2>'s Bank Balance to <a>",
	L624 = "<p1> added <a> to <p2>'s Bank",
	L625 = "<p1> removed <a> from <p2>'s Bank",
	L626 = "<p1> reset 'Joint Account: <acc>' to default settings",
	L627 = "<p1> set 'Joint Account: <acc>' balance to <a>",
	L628 = "<p1> added <a> to Joint Account: <acc>",
	L629 = "<p1> remove <a> from Joint Account: <acc>",
	L630 = "<p1> added <p2> as Owner to Joint Account: <acc>",
	L631 = "<p1> used Pay Forwarding to deposit <a> into Joint Account: <acc>",
	L632 = "<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using Joint Account: <acc>",
	L633 = "<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using their Bank",
	L634 = "<p1> set Pay Forwarding to Joint Account: <acc>",
	L635 = "<p1> set Pay Forwarding to their Bank Account",
	L636 = "<p1> turned off Pay Forwarding",
	L637 = "<p1> set UserMaxWithdraw to <a> for Joint Account: <acc>",
	L638 = "<p1> viewed own rank",
	L639 = "<p1> viewed <p2>'s rank";
	/*END MESSAGES*/
		
	public dCMessages(dCData dCD, PropertiesFile dCMess){
		this.dCD = dCD;
		this.dCMess = dCMess;
	}
	
	public void loadMessages(){
		//Get/Set Error Messages
		E101 = parseString(E101, "101-Player-NotEnoughMoney");
		E102 = parseString(E102, "102-Bank-NotEnoughMoney");
		E103 = parseString(E103, "103-NumberFormatError");
		E104 = parseString(E104, "104-Command-NoPermission");
		E105 = parseString(E105, "105-Joint-NotEnoughMoney");
		E106 = parseString(E106, "106-AmountNotSpecified");
		E107 = parseString(E107, "107-Joint-NoAccess");
		E108 = parseString(E108, "108-PlayerNotFound");
		E109 = parseString(E109, "109-NegativeEntered");
		E110 = parseString(E110, "110-JointNotFound");
		E111 = parseString(E111, "111-JointNameTaken");
		E112 = parseString(E112, "112-Joint-OwnerNotSpecified");
		E113 = parseString(E113, "113-InvalidCommand");
		E114 = parseString(E114, "114-Joint-PlayerNotSpecified");
		E115 = parseString(E115, "115-JointNameTooLong");
		E116 = parseString(E116, "116-Joint-User-CannotWithdrawAgainYet");
		E117 = parseString(E117, "117-Joint-User-WithdrawTooMuch");
		E118 = parseString(E118, "118-Player-CannotPaySelf");
		E119 = parseString(E119, "119-Player-AlreadyJointUser");
		E120 = parseString(E120, "120-PlayerNotSpecified");
		E121 = parseString(E121, "121-Joint-UserNotSpecified");
		E122 = parseString(E122, "122-Joint-PlayerAlreadyOwner");
		E123 = parseString(E123, "123-Joint-OwnerNotSpecified");
		E124 = parseString(E124, "124-Joint-PlayerAlreadyNotUser");
		E125 = parseString(E125, "125-Joint-PlayerAlreadyNotOwner");
		E126 = parseString(E126, "126-NegativeNumber");
		E127 = parseString(E127, "127-BalanceNegative");
		dCD.log.info("[dConomy] - ErrorMessages Loaded!");
		
		//Get/Set Messages
		//Player Messages (200 Series)
		P201 = parseString(P201, "201-Player-AccountBalance");
		P202 = parseString(P202, "202-Player-BankBalance");
		P203 = parseString(P203, "203-Player-BankWithdraw");
		P204 = parseString(P204, "204-Player-BankDeposit");
		P205 = parseString(P205, "205-Player-BankNewBalance");
		P206 = parseString(P206, "206-Player-AccountNewBalance");
		P207 = parseString(P207, "207-Player-SentPaymentTo");
		P208 = parseString(P208, "208-Player-ReceivedPaymentFrom");
		P209 = parseString(P209, "209-Player-RankSelf");
		P210 = parseString(P210, "210-Player-RankOther");
		P211 = parseString(P211, "211-Player-PayForwardJoint");
		P212 = parseString(P212, "212-Player-PaymentSentFromJoint");
		P213 = parseString(P213, "213-Player-PayForwardBank");
		P214 = parseString(P214, "214-Player-PaymentSentFromBank");
		P215 = parseString(P215, "215-Player-PayForwardSetJoint");
		P216 = parseString(P216, "216-Player-PayForwardSetBank");
		P217 = parseString(P217, "217-Player-PaymentSentToJoint");
		P218 = parseString(P218, "218-Player-PayForwardTurnedOff");
		P219 = parseString(P219, "219-Player-PayForwardingIsSetJoint");
		P220 = parseString(P220, "220-Player-PayForwardingIsSetBank");
		P221 = parseString(P221, "221-Player-PayForwardingNotActive");
		P222 = parseString(P222, "222-Player-AccountCheck");
		P223 = parseString(P223, "223-Player-BankCheck");
		dCD.log.info("[dConomy] - Player Messages Loaded!");
		
		//Joint Account Messages (300 Series)
		J301 = parseString(J301, "301-Joint-AccountBalance");
		J302 = parseString(J302, "302-Joint-NewBalance");
		J303 = parseString(J303, "303-Joint-Withdraw");
		J304 = parseString(J304, "304-Joint-Deposit");
		J305 = parseString(J305, "305-Joint-Created");
		J306 = parseString(J306, "306-Joint-OwnerAdded");
		J307 = parseString(J307, "307-Joint-OwnerRemoved");
		J308 = parseString(J308, "308-Joint-AccountDeleted");
		J309 = parseString(J309, "309-Joint-UserAdded");
		J310 = parseString(J310, "310-Joint-UserRemoved");
		J311 = parseString(J311, "311-Joint-MaxUserWithdrawCheck");
	    J312 = parseString(J312, "312-Joint-MaxUserWithdrawSet");
		dCD.log.info("[dConomy] - Joint Account Messages Loaded!");
		
		//Admin/Misc Message (400 Series)
		A401 = parseString(A401, "401-Rank-Top");
		A402 = parseString(A402, "402-Rank-Sorting");
		A403 = parseString(A403, "403-Rank-NoTop");
		A404 = parseString(A404, "404-Admin-Player-Reset");
		A405 = parseString(A405, "405-Admin-Player-Set");
		A406 = parseString(A406, "406-Admin-Player-Remove");
		A407 = parseString(A407, "407-Admin-Player-Add");
		A408 = parseString(A408, "408-Admin-Bank-Reset");
		A409 = parseString(A409, "409-Admin-Bank-Set");
		A410 = parseString(A410, "410-Admin-Bank-Remove");
		A411 = parseString(A411, "411-Admin-Bank-Add");
		A412 = parseString(A412, "412-Admin-Joint-Reset");
		A413 = parseString(A413, "413-Admin-Joint-Set");
		A414 = parseString(A414, "414-Admin-Joint-Add");
		A415 = parseString(A415, "415-Admin-Joint-Remove");
		dCD.log.info("[dConomy] - Admin/Misc Messages Loaded!");
		
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
		dCD.log.info("[dConomy] - Logging Messages Loaded!");
	}
	
	private String parseString(String def, String key){
		String value = def;
		if(dCMess.containsKey(key)){
			value = dCMess.getString(key);
			if(value.equals("")){
				dCD.log.warning("[dConomy] - Value not found for "+key+NL+" Using default of "+def);
				value = def;
			}
		}
		else{
			dCD.log.warning("[dConomy] - Key: '"+key+"' not found."+NL+" Using default of "+def);
		}
		return value;
	}
}
