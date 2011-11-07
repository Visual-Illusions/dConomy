import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
* dCData v1.x
* Copyright (C) 2011 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@hotmail.com>
*
* This file is part of dConomy.
*
* dConomy is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* dConomy is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with dConomy.  If not, see <http://www.gnu.org/licenses/>.
*/

public class dCData {
	//Misc Stuff
	Logger log = Logger.getLogger("Minecraft");
	
	//PropertiesFiles
	PropertiesFile dCSettings;
	PropertiesFile dCMySQLConn;
	PropertiesFile dCMessage;
	PropertiesFile dCJointAccounts;
	PropertiesFile dCPayForwarding;
	PropertiesFile dCTimerreset;
	
	//Properties File Locations/Directories
	String dire = "plugins/config/dConomy/";
	String direJoint = "plugins/config/dConomy/JointAccounts/";
	String direTrans = "plugins/config/dConomy/TransactionLogs/";
	String direMessages = "plugins/config/dConomy/Messages/";
	String propsLoc = "dCSettings.ini";
	String MySQLLoc = "dCMySQLConn.ini";
	String FormatLoc = "dCMessageFormat.txt";
	String JointAcc = /*AccountName*/".txt";
	String TransLoc = /*date(day/month/year)*/".txt";
	String JUWDLoc = "JUWD.txt";
	String dCPF = "PayForwarding.txt";
	String timerresetloc = "dCTimerReser.DONOTEDIT";
	
	//Date stuff for logs
	Date date;
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
	
	//Settings
	PluginListener dCListener;
	boolean MySQL = false, CMySQL = false, Log = false;
	String DataBase = "jdbc:mysql://localhost:3306/minecraft", UserName = "root", Password= "root", MoneyName = "Voin$";
	double startingbalance = 0d;
	int Bankdelay = 60;
	double interest = 0.02;
	int JWDelay = 60;
	long breset = System.currentTimeMillis()+(Bankdelay*60*1000);
	long jwreset = System.currentTimeMillis()+(JWDelay*60*1000);
	dCTimer dCT;
	dCTimer.dCBankTimer dCBT;
	dCTimer.dCJointWithdrawDelayTimer dCJWDT;
	boolean iConvert = false;
	double JUMWA = 25;
	boolean aoc = false;
	
	//Error Messages (100 Series)
	String ErrorMessage101 = "<rose>You do not have enough <m> to complete transaction!";
	String ErrorMessage102 = "<rose>Your bank does not have enough <m> to complete transaction!";
	String ErrorMessage103 = "<rose>You did not enter a proper number!";
	String ErrorMessage104 = "<rose>You do not have permission to use that command!";
	String ErrorMessage105 = "<rose><acc> does not have the funds for this transaction!";
	String ErrorMessage106 = "<rose>You didn't specify an amount!";
	String ErrorMessage107 = "<rose>You do not have access to <acc>!";
	String ErrorMessage108 = "<rose>Player not found!";
	String ErrorMessage109 = "<rose>Could not find account for <p>!";
	String ErrorMessage110 = "<rose><acc> does not exist!";
	String ErrorMessage111 = "<rose><acc> is already in use!";
	String ErrorMessage112 = "<rose>You didn't specify an owner!";
	String ErrorMessage113 = "<rose>Invaild dConomy Command!";
	String ErrorMessage114 = "<rose>You didn't specify a player!";
	String ErrorMessage115 = "<rose>Account names cannot be longer than 32 Characters!";
	String ErrorMessage116 = "<rose>You cannot make another withdraw for <xmin> Minutes!";
	String ErrorMessage117 = "<rose>You cannot withdraw that amount at one time!";
	String ErrorMessage118 = "<rose>You cannot pay yourself!";
	String ErrorMessage119 = "<rose><p> is already a User!";
	String ErrorMessage120 = "<rose>Player Not Specified!";
	String ErrorMessage121 = "<rose>Joint Account User not specified!";
	String ErrorMessage122 = "<rose><p> is already a Owner!";
	String ErrorMessage123 = "<rose>Joint Account Owner not specified!";
	String ErrorMessage124 = "<rose><p> wasn't a User!";
	String ErrorMessage125 = "<rose><p> wasn't a Owner!";
	String ErrorMessage126 = "<rose>Amount cannot be a negative!";
	String ErrorMessage127 = "<rose>Balance cannot become negative!";
	
	//Player Messages (200 Series)
	String Message201 = "<lightgreen>Your <lightblue>Account Balance<lightgreen> is <yellow><a> <m><lightgreen>.";
	String Message202 = "<lightgreen>Your <lightblue>Bank Balance<lightgreen> is <yellow><a> <m><lightgreen>.";
	String Message203 = "<lightgreen>Your have withdrawn <yellow><a> <m><lightgreen> from your <lightblue>Bank Account<lightgreen>.";
	String Message204 = "<lightgreen>Your have deposited <yellow><a> <m><lightgreen> into your <lightblue>Bank Account<lightgreen>.";
	String Message205 = "<lightgreen>Your new bank balance is <yellow><a> <m><lightgreen>.";
	String Message206 = "<lightgreen>Your new balance is <yellow><a> <m><lightgreen>.";
	String Message207 = "<lightgreen>You have sent <yellow><a> <m> to <gold><p><lightgreen>.";
	String Message208 = "<lightgreen>You have received <yellow><a> <m><lightgreen> from <gold><p><lightgreen>.";
	String Message209 = "<lightgreen>You are ranked <yellow><rank><lightgreen>.";
	String Message210 = "<gold><p><lightgreen> is ranked <yellow><rank><lightgreen>.";
	String Message211 = "<lightgreen>Your pay has been forwarded to JointAccount: <lightblue><acc><lightgreen>.";
	String Message212 = "<lightgreen>Payment has been sent from Joint Account: <lightblue><acc><lightgreen>.";
	String Message213 = "<lightgreen>Your pay has been forwarded to your <lightblue>Bank Account<lightgreen>.";
	String Message214 = "<lightgreen>Payment has been sent from your <lightblue>Bank Account<lightgreen>.";
	String Message215 = "<lightgreen>You have set up Pay Forwarding to Joint Account: <lightblue><acc><lightgreen>.";
	String Message216 = "<lightgreen>You have set up Pay Forwarding to your <lightblue>Bank Account<lightgreen>.";
	String Message217 = "<lightgreen>You have paid <amount> to Joint Account: <lightblue><acc><lightgreen>.";
	String Message218 = "<lightgreen>You have turned Pay Forwarding <rose>OFF<lightgreen>.";
	String Message219 = "<lightgreen>You have Pay Forwarding set to Joint Account: <lightblue><acc><lightgreen>.";
	String Message220 = "<lightgreen>You have Pay Forwarding set to your <lightblue> Bank Account<lightgreen>.";
	String Message221 = "<lightgreen>Pay Forwarding is not active.";
	String Message222 = "<gold><p>'s<lightgreen> Account Balance is <yellow><a> <m><lightgreen>.";
	String Message223 = "<gold><p>'s<lightgreen> Bank Balance is <yellow><a> <m><lightgreen>.";
	
	//Joint Account Message (300 Series) //not sure why i split this...
	String Message301 = "<lightblue><acc><lightgreen> balance is <yellow><a> <m><lightgreen>.";
	String Message302 = "<lightblue><acc><lightgreen> new balance is <yellow><a> <m><lightgreen>.";
	String Message303 = "<lightgreen>You have withdrawn <yellow><a> <m> <lightgreen> from <lightblue><acc><lightgreen>.";
	String Message304 = "<lightgreen>You have deposited <yellow><a> <m> into <lightblue><acc><lightgreen>.";
	String Message305 = "<lightgreen>Created JointAccount <lightblue><acc><lightgreen>.";
	String Message306 = "<lightgreen>Added <gold><p><lightgreen> as Owner to Joint Account: <lightblue><acc><lightgreen>.";
	String Message307 = "<lightgreen>Removed <gold><p><lightgreen> as Owner from  Joint Account: <lightblue><acc><lightgreen>.";
	String Message308 = "<lightgreen>Deleted Joint Account: <lightblue><acc><lightgreen>.";
	String Message309 = "<lightgreen>Added <gold><p><lightgreen> as User to Joint Account: <lightblue><acc><lightgreen>.";
	String Message310 = "<lightgreen>Removed <gold><p><lightgreen> as User from Joint Account: <lightblue><acc><lightgreen>.";
	String Message311 = "<lightgreen>Max User Withdraw is <yellow><a> <m><lightgreen> per <purple><min> Minutes<lightgreen>.";
	String Message312 = "<lightgreen>You have set Max User Withdraw to <yellow><a> <m><lightgreen>.";
	
	//Admin/Misc  (400 Series)
	String Message401 = "<green>-----Top <yellow><rank> <green>Richest Players-----";
	String Message402 = "<green>   <rank>. <gold><p> <green>[<yellow><a> <m><green>]";
	String Message403 = "<rose>   No Top Players Yet!";
	String Message404 = "<gold><p>'s Account<lightgreen> has been reset to default balance.";
	String Message405 = "<gold><p>'s Account<lightgreen> has been set to <a> <m><lightgreen>.";
	String Message406 = "<lightgreen>You have deducted <yellow><a> <m><lightgreen> from <gold><p>' Account<lightgreen>.";
	String Message407 = "<yellow><a> <m><lightgreen> has been added to <gold><p>'s Account<lightgreen>.";
	String Message408 = "<gold><p>'s Bank<lightgreen> has been reset to 0.";
	String Message409 = "<gold><p>'s Bank<lightgreen> has been set to <a> <m><lightgreen>.";
	String Message410 = "<lightgreen>You have removed <yellow><a> <m><lightgreen> from <gold><p>' Bank<lightgreen>.";
	String Message411 = "<yellow><a> <m><lightgreen> has been added to <gold><p>'s Bank<lightgreen>.";
	String Message412 = "<lightgreen> Joint Account: <lightblue><a>'s Balance<lightgreen> has been reset to 0.";
	String Message413 = "<lightgreen> Joint Account: <lightblue><acc>'s Balance<lightgreen> has been set to <yellow><a> <m><lightgreen>.";
	String Message414 = "<yellow><a> <m><lightgreen> has been added to Joint Account: <lightblue><acc>'s Balance<lightgreen>.";
	String Message415 = "<yellow><a> <m><lightgreen> has been removed from Joint Account: <lightblue><acc>'s Balance<lightgreen>.";
	
	//Help (500 Series)
	String Message501 = "<green>|--------------<gold>dConomy /money Help<green>--------------|";
	String Message502 = "<green>|----<rose><REQUIRED><green>----<lightblue>[Optional]<green>----<yellow>(Alias)<green>-|";
	String Message503 = "   <gold>/money<green>: Basic Command/Display account balance";
	String Message504 = "   <gold>pay <yellow>(-p) <rose><p> <a><green>: Pay Player Amount";
	String Message505 = "   <gold>rank <yellow>(-r) <lightblue>[p]<green>: Display Rank Position";
	String Message506 = "   <gold>top <yellow>(-t) <lightblue>[a]<green>: Display Richest Players";
	String Message507 = "   <gold>set <yellow>(-s) <rose><p> <a><green>: Set player's account to amount";
	String Message508 = "   <gold>reset <yellow>(-rt) <rose><p><green>: Reset players account to default";
	String Message509 = "   <gold>add <yellow>(-a) <rose><p> <a><green>: Add amount to player's account";
	String Message510 = "   <gold>remove <yellow>(-rm) <rose><p> <a><green>: Remove amount from player's account";
	String Message511 = "   <gold>auto <yellow>(-au) <green>: checks autodep account settings";
	String Message512 = "   <gold>setauto <yellow>(-sa) <rose><acc|OFF><green>: Changes or sets autodep account";
	String Message513 = "   <green>Use <gold>/bank ? <green>for help with Bank commands";
	String Message514 = "   <green>Use <gold>/joint ? <green>for help with Joint Accounts";
	String Message515 = "<green>|--------------<gold>dConomy /bank Help<green>--------------|";
	String Message516 = "   <gold>/bank<green>: Basic Command/Display bank balance";
	String Message517 = "   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from bank account";
	String Message518 = "   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into bank account";
	String Message519 = "   <gold>reset <yellow>(-rt) <rose><p><green>: reset player's bank to 0";
	String Message520 = "   <gold>set <yellow>(-s) <rose><p> <amount><green>: set player's bank to amount";
	String Message521 = "   <gold>add <yellow>(-a) <rose><p><green>: add amount to player's bank";
	String Message522 = "   <gold>remove <yellow>(-rm) <rose><p> <a><green>: remove amount from player's bank";
	String Message523 = "<green>|--------------<gold>dConomy /joint Help<green>--------------|";
	String Message524 = "   <gold>/joint <account><green>: Basic Command/Display JointAccount balance";
	String Message525 = "   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from Account";
	String Message526 = "   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into Account";
	String Message527 = "   <gold>pay <yellow>(-p) <rose><p><green>: pay Account";
	String Message528 = "   <gold>create <yellow>(-c) <green>: create Account";
	String Message529 = "   <gold>delete <yellow>(-del) <green>: deposit into Account";
	String Message530 = "   <gold>addowner <yellow>(-ao) <rose><p><green>: add owner to Account";
	String Message531 = "   <gold>removeowner <yellow>(-ro) <rose><p><green>: add owner to Account";
	String Message532 = "   <gold>adduser <yellow>(-au) <rose><p><green>: add owner to Account";
	String Message533 = "   <gold>removeuser <yellow>(-ru) <rose><p><green>: remove owner from Account";
	String Message534 = "   <gold>setusermax <yellow>(-su) <rose><a><green>: set UserMaxWithdraw per set period";
	String Message535 = "   <gold>reset <yellow>(-rt) <rose><a><green>: reset Account to 0";
	String Message536 = "   <gold>set <yellow>(-s) <rose><a><green>: set Account to amount";
	String Message537 = "   <gold>add <yellow>(-a) <rose><a><green>: add amount to Account";
	String Message538 = "   <gold>remove <yellow>(-rm) <rose><a><green>: remove amount to Account";
	String Message539 = "   <gold>usermax <yellow>(-um) <rose><a><green>: checks UserMaxWithdraw per set period";
	String Message540 = "<green>|---- <p> = Player <a> = Amount <acc> = Account ----|";
	String Message541 = "   <green>Use <gold>/money ? admin <green>for help with Money Admin commands";
	String Message542 = "   <green>Use <gold>/bank ? admin <green>for help with Bank Admin commands";
	String Message543 = "   <green>Use <gold>/joint ? <green>for help with Joint Admin commands";
	String Message544 = "<green>|------------<gold>dConomy /money Admin Help<green>------------|";
	String Message545 = "<green>|------------<gold>dConomy /bank Admin Help<green>------------|";
	String Message546 = "<green>|------------<gold>dConomy /joint Admin Help<green>------------|";
	
	//Logging (600 Series)
	String Message601 = "<p1> paid <p2> <a>";
	String Message602 = "<p1> deposited <a> into their Bank";
	String Message603 = "<p1> withdrew <a> from their Bank Account";
	String Message604 = "<p1> withdrew <a> from Joint Account: <acc>";
	String Message605 = "<p1> deposited <a> from Joint Account: <acc>";
	String Message606 = "<p1> paid <a> to Joint Account: <acc>";
	String Message607 = "<p1> paid <p2> using Join Account: <acc>";
	String Message608 = "<p1> used PayForwarding to deposit <acc> directly into their Bank";
	String Message609 = "<p1> used PayForwarding connected to their Bank to pay <a> to <p2> ";
	String Message610 = "<p1> used PayForwarding connected to 'Joint Account: <acc>' to pay <p2>";
	String Message611 = "<p1> created Joint Account: <acc>";
	String Message612 = "<p1> deleted Joint Account: <acc>";
	String Message613 = "<p1> added <p2> as 'Owner' to Joint Account: <acc>";
	String Message614 = "<p1> veiwed Top <a> Richest Players";
	String Message615 = "<p1> added <p2> as 'User' to Joint Account: <acc>";
	String Message616 = "<p1> removed <p2> as 'Owner' to Joint Account: <acc>";
	String Message617 = "<p1> removed <p2> as 'User' to Joint Account: <acc>";
	String Message618 = "<p1> reset <p2> Account to default balance";
	String Message619 = "<p1> set <p2>'s Account to <a>";
	String Message620 = "<p1> added <a> to <p2>'s Account";
	String Message621 = "<p1> removed <a> from <p2>'s Account";
	String Message622 = "<p1> reset <p2>'s Bank to default balance";
	String Message623 = "<p1> set <p2>'s Bank Balance to <a>";
	String Message624 = "<p1> added <a> to <p2>'s Bank";
	String Message625 = "<p1> removed <a> from <p2>'s Bank";
	String Message626 = "<p1> reset 'Joint Account: <acc>' to default settings";
	String Message627 = "<p1> set 'Joint Account: <acc>' balance to <a>";
	String Message628 = "<p1> added <a> to Joint Account: <acc>";
	String Message629 = "<p1> remove <a> from Joint Account: <acc>";
	String Message630 = "<p1> added <p2> as Owner to Joint Account: <acc>";
	String Message631 = "<p1> used Pay Forwarding to deposit <a> into Joint Account: <acc>";
	String Message632 = "<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using Joint Account: <acc>";
	String Message633 = "<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using their Bank";
	String Message634 = "<p1> set Pay Forwarding to Joint Account: <acc>";
	String Message635 = "<p1> set Pay Forwarding to their Bank Account";
	String Message636 = "<p1> turned off Pay Forwarding";
	String Message637 = "<p1> set UserMaxWithdraw to <a> for Joint Account: <acc>";
	String Message638 = "<p1> viewed own rank";
	String Message639 = "<p1> viewed <p2>'s rank";
	
	//Number Formating
	NumberFormat numform = new DecimalFormat("0.00");
	NumberFormat displayform = new DecimalFormat("#,##0.00");
	
	//Misc
	String PFCS = "-PF-ON";
	String PFAS = "-PF-ACC";
	ArrayList<String> JUWD;
	
	//Data Initializer
	public dCData(){
		createDirectory();
		dCSettings = new PropertiesFile(dire+propsLoc);
		dCMySQLConn = new PropertiesFile(dire+MySQLLoc);
		dCMessage = new PropertiesFile(direMessages+FormatLoc);
		dCPayForwarding = new PropertiesFile(dire+dCPF);
		dCTimerreset = new PropertiesFile(dire+timerresetloc);
		loadSettings();
	}

	//Make the Directory if not exist and Add some order to the Messages File
	public void createDirectory(){
		File checkDir = new File(dire);
		File checkDireJoint = new File(direJoint);
		File checkDirLog = new File(direTrans);
		File checkDirMess = new File(direMessages);
		File PropsFile = new File(dire+propsLoc);
		File MessFile = new File(direMessages+FormatLoc);
		if (!checkDir.exists()){
			checkDir.mkdirs();
		}
		if (!checkDireJoint.exists()){
			checkDireJoint.mkdirs();
		}
		if (!checkDirLog.exists()){
			checkDirLog.mkdirs();
		}
		if (!checkDirMess.exists()){
			checkDirMess.mkdirs();
		}
		if(!PropsFile.exists()){
			try {
			    BufferedWriter out = new BufferedWriter(new FileWriter(PropsFile));
			    out.write("###dConomy Settings###"); out.newLine();
			    out.write("#New Account Starting Balance value in 0.00 format (if set to less than 0 will default to 0)#"); out.newLine();
			    out.write("Starting-Balance="+startingbalance); out.newLine();
			    out.write("#Name to call your currency#"); out.newLine();
			    out.write("Money-Name="+MoneyName); out.newLine();
			    out.write("#Set to true to use MySQL#"); out.newLine();
			    out.write("Use-MySQL="+MySQL); out.newLine();
			    out.write("#Set to true to use Canary's MySQL Connection (MySQL needs to be true)#"); out.newLine();
			    out.write("Use-Canary-MySQLConn="+CMySQL); out.newLine();
			    out.write("#Set to true to log transactions#"); out.newLine();
			    out.write("LogPayments="+Log); out.newLine();
			    out.write("#Bank Interest Percentage (0.02 = 0.02%) (if set to 0 or less will default to 0.02)#"); out.newLine();
			    out.write("Bank-InterestRate="+interest); out.newLine();
			    out.write("#Bank Interest Pay Delay (in minutes) - Set to 0 to disable"); out.newLine();
			    out.write("Bank-InterestPayDelay="+Bankdelay); out.newLine();
			    out.write("#Joint Account Users Withdraw delay(in minutes) - Set to 0 to disable#"); out.newLine();
			    out.write("JointUserWithdrawDelay="+JWDelay); out.newLine();
			    out.write("#Joint Account User Max Withdraw Default#"); out.newLine();
			    out.write("JointUserMaxWithdrawAmount="+JUMWA); out.newLine();
			    out.write("#Admin Only Check Another Players Balance - Set to false to allow all#"); out.newLine();
			    out.write("AOCAPB="+aoc); out.newLine();
			    out.write("#Set to true to convert iCo Balances to dCo Balances (reverts to false after convert runs)#"); out.newLine();
			    out.write("Convert-iConomy="+iConvert); out.newLine();
				out.write("###EOF###");
				out.close();
			} catch (IOException e) {
				log.severe("[dConomy] - Unable to create Properties File");
			}
		}
		if (!MessFile.exists()){
			//Keeping the Messages File from becoming a mess
			try {
			    BufferedWriter out = new BufferedWriter(new FileWriter(MessFile));
			    out.write("#########################################"); out.newLine();
			    out.write("############dConomy Messages#############"); out.newLine();
			    out.write("#########################################"); out.newLine();
			    out.write("####Error Messages (100 Series)####"); out.newLine();
			    out.write("101-Player-NotEnoughMoney="+ErrorMessage101); out.newLine();
			    out.write("102-Bank-NotEnoughMoney="+ErrorMessage102); out.newLine();
			    out.write("103-NumberFormatError="+ErrorMessage103); out.newLine();
			    out.write("104-Command-NoPermission="+ErrorMessage104); out.newLine();
			    out.write("105-Joint-NotEnoughMoney="+ErrorMessage105); out.newLine();
			    out.write("106-AmountNotSpecified="+ErrorMessage106); out.newLine();
			    out.write("107-Joint-NoAccess="+ErrorMessage107); out.newLine();
			    out.write("108-PlayerNotFound="+ErrorMessage108); out.newLine();
			    out.write("109-NegativeEntered="+ErrorMessage109); out.newLine();
			    out.write("110-JointNotFound="+ErrorMessage110); out.newLine();
			    out.write("111-JointNameTaken="+ErrorMessage111); out.newLine();
			    out.write("112-Joint-OwnerNotSpecified="+ErrorMessage112); out.newLine();
			    out.write("113-InvalidCommand="+ErrorMessage113); out.newLine();
			    out.write("114-Joint-PlayerNotSpecified="+ErrorMessage114); out.newLine();
			    out.write("115-JointNameTooLong="+ErrorMessage115); out.newLine();
			    out.write("116-Joint-User-CannotWithdrawAgainYet="+ErrorMessage116); out.newLine();
			    out.write("117-Joint-User-WithdrawTooMuch="+ErrorMessage117); out.newLine();
			    out.write("118-Player-CannotPaySelf="+ErrorMessage118); out.newLine();
			    out.write("119-Player-AlreadyJointUser="+ErrorMessage119); out.newLine();
			    out.write("120-PlayerNotSpecified="+ErrorMessage120); out.newLine();
			    out.write("121-Joint-UserNotSpecified="+ErrorMessage121); out.newLine();
			    out.write("122-Joint-PlayerAlreadyOwner="+ErrorMessage122); out.newLine();
			    out.write("123-Joint-OwnerNotSpecified="+ErrorMessage123); out.newLine();
			    out.write("124-Joint-PlayerAlreadyNotUser="+ErrorMessage124); out.newLine();
			    out.write("125-Joint-PlayerAlreadyNotOwner="+ErrorMessage125); out.newLine();
			    out.write("126-NegativeNumber="+ErrorMessage126);
			    out.write("127-BalanceNegative="+ErrorMessage127);
			    out.write("#########################################"); out.newLine();
			    out.write("####Player Messages (200 Series)####"); out.newLine();
			    out.write("201-Player-AccountBalance="+Message201); out.newLine();
			    out.write("202-Player-BankBalance="+Message202); out.newLine();
			    out.write("203-Player-BankWithdraw="+Message203); out.newLine();
			    out.write("204-Player-BankDeposit="+Message204); out.newLine();
			    out.write("205-Player-BankNewBalance="+Message205); out.newLine();
			    out.write("206-Player-AccountNewBalance="+Message206); out.newLine();
			    out.write("207-Player-SentPaymentTo="+Message207); out.newLine();
			    out.write("208-Player-ReceivedPaymentFrom="+Message208); out.newLine();
			    out.write("209-Player-RankSelf="+Message209); out.newLine();
			    out.write("210-Player-RankOther="+Message210); out.newLine();
			    out.write("211-Player-PayForwardJoint="+Message211); out.newLine();
			    out.write("212-Player-PaymentSentFromJoint="+Message212); out.newLine();
			    out.write("213-Player-PayForwardBank="+Message213); out.newLine();
			    out.write("214-Player-PaymentSentFromBank="+Message214); out.newLine();
			    out.write("215-Player-PayForwardSetJoint="+Message215); out.newLine();
			    out.write("216-Player-PayForwardSetBank="+Message216); out.newLine();
			    out.write("217-Player-PaymentSentToJoint="+Message217); out.newLine();
			    out.write("218-Player-PayForwardTurnedOff="+Message218); out.newLine();
			    out.write("219-Player-PayForwardingIsSetJoint="+Message219); out.newLine();
			    out.write("220-Player-PayForwardingIsSetBank="+Message220); out.newLine();
			    out.write("221-Player-PayForwardingNotActive="+Message221); out.newLine();
			    out.write("222-Player-AccountCheck="+Message222); out.newLine();
			    out.write("223-Player-BankCheck="+Message223); out.newLine();
			    out.write("#########################################"); out.newLine();
			    out.write("####Joint Account Messages (300 Series)####"); out.newLine();
			    out.write("301-Joint-AccountBalance="+Message301); out.newLine();
			    out.write("302-Joint-NewBalance="+Message302); out.newLine();
			    out.write("303-Joint-Withdraw="+Message303); out.newLine();
			    out.write("304-Joint-Deposit="+Message304); out.newLine();
			    out.write("305-Joint-Created="+Message305); out.newLine();
			    out.write("306-Joint-OwnerAdded="+Message306); out.newLine();
			    out.write("307-Joint-OwnerRemoved="+Message307); out.newLine();
			    out.write("308-Joint-AccountDeleted="+Message308); out.newLine();
			    out.write("309-Joint-UserAdded="+Message309); out.newLine();
			    out.write("310-Joint-UserRemoved="+Message310); out.newLine();
			    out.write("311-Joint-MaxUserWithdrawCheck="+Message311); out.newLine();
			    out.write("312-Joint-MaxUserWithdrawSet="+Message312); out.newLine();
			    out.write("#########################################"); out.newLine();
			    out.write("####Admin/Misc Account Messages (400 Series)####"); out.newLine();
			    out.write("401-Rank-Top="+Message401); out.newLine();
			    out.write("402-Rank-Sorting="+Message402); out.newLine();
			    out.write("403-Rank-NoTop="+Message403); out.newLine();
			    out.write("404-Admin-Player-Reset="+Message404); out.newLine();
			    out.write("405-Admin-Player-Set="+Message405); out.newLine();
			    out.write("406-Admin-Player-Remove="+Message406); out.newLine();
			    out.write("407-Admin-Player-Add="+Message407); out.newLine();
			    out.write("408-Admin-Bank-Reset="+Message408); out.newLine();
			    out.write("409-Admin-Bank-Set="+Message409); out.newLine();
			    out.write("410-Admin-Bank-Remove="+Message410); out.newLine();
			    out.write("411-Admin-Bank-Add="+Message411); out.newLine(); 
			    out.write("412-Admin-Joint-Reset="+Message412); out.newLine();
			    out.write("413-Admin-Joint-Set="+Message413); out.newLine();
			    out.write("414-Admin-Joint-Add="+Message414); out.newLine();
			    out.write("415-Admin-Joint-Remove="+Message415); out.newLine();
			    out.write("#########################################"); out.newLine();
			    out.write("####Help Display Messages (500 Series)####"); out.newLine();
			    out.write("501-Money-HelpOpen="+Message501); out.newLine();
			    out.write("502-RequiredOptionalAlias="+Message502); out.newLine();
			    out.write("503-Money-Base="+Message503); out.newLine();
			    out.write("504-Money-Pay="+Message504); out.newLine();
			    out.write("505-Money-Rank="+Message505); out.newLine();
			    out.write("506-Money-Top="+Message506); out.newLine();
			    out.write("507-Money-Set="+Message507); out.newLine();
			    out.write("508-Money-Reset="+Message508); out.newLine();
			    out.write("509-Money-Add="+Message509); out.newLine();
			    out.write("510-Money-Remove="+Message510); out.newLine();
			    out.write("511-Money-Auto="+Message511); out.newLine();
			    out.write("512-Money-SetAuto="+Message512); out.newLine();
			    out.write("513-Money-UseJointHelp="+Message513); out.newLine(); 
			    out.write("514-Money-UseBankHelp="+Message514); out.newLine();
			    out.write("515-Bank-HelpOpen="+Message515); out.newLine();
			    out.write("516-Bank-Base="+Message516); out.newLine(); 
			    out.write("517-Bank-Withdraw="+Message517); out.newLine();
			    out.write("518-Bank-Deposit="+Message518); out.newLine();
			    out.write("519-Bank-Reset="+Message519); out.newLine();
			    out.write("520-Bank-Set="+Message520); out.newLine();
			    out.write("521-Bank-Add="+Message521); out.newLine();
			    out.write("522-Bank-Remove="+Message522); out.newLine();
			    out.write("523-Joint-HelpOpen="+Message523); out.newLine();
			    out.write("524-Joint-Base="+Message524); out.newLine();
			    out.write("525-Joint-Withdraw="+Message525); out.newLine();
			    out.write("526-Joint-Deposit="+Message526); out.newLine();
			    out.write("527-Joint-Pay="+Message527); out.newLine();
			    out.write("528-Joint-Create="+Message528); out.newLine();
			    out.write("529-Joint-Delete="+Message529); out.newLine();
			    out.write("530-Joint-AddOwner="+Message530); out.newLine();
			    out.write("531-Joint-RemoveOwner="+Message531); out.newLine();
			    out.write("532-Joint-AddUser="+Message532); out.newLine();
			    out.write("533-Joint-RemoveUser="+Message533); out.newLine();
			    out.write("534-Joint-UserMax="+Message534); out.newLine();
			    out.write("535-Joint-Reset="+Message535); out.newLine();
			    out.write("536-Joint-Set="+Message536); out.newLine();
			    out.write("537-Joint-Add="+Message537); out.newLine();
			    out.write("538-Joint-Remove="+Message538); out.newLine();
			    out.write("539-Joint-UserMaxCheck="+Message539); out.newLine();
			    out.write("540-Help-PlayerAmountAccount="+Message540); out.newLine();
			    out.write("541-Help-UseMoneyAdmin="+Message541); out.newLine();
			    out.write("542-Help-UseBankAdmin="+Message542); out.newLine();
			    out.write("543-Help-UseJointAdmin="+Message543); out.newLine();
			    out.write("544-Help-MoneyAdminOpen="+Message544); out.newLine();
			    out.write("545-Help-BankAdminOpen="+Message545); out.newLine();
			    out.write("546-Help-JointAdminOpen="+Message546); out.newLine();
				out.write("#########################################"); out.newLine();
			    out.write("####Logging Messages (600 Series)####"); out.newLine();
			    out.write("601-PlayerPayPlayer="+Message601); out.newLine();
			    out.write("602-PlayerDepositBank="+Message602); out.newLine();
			    out.write("603-PlayerWithdrawBank="+Message603); out.newLine();
			    out.write("604-PlayerWithdrawJoint="+Message604); out.newLine();
			    out.write("605-PlayerDepositJoint="+Message605); out.newLine();
			    out.write("606-PaidJoint="+Message606); out.newLine();
			    out.write("607-PlayerPaidWithJoint="+Message607); out.newLine();
			    out.write("608-PlayerPFBank="+Message608); out.newLine();
			    out.write("609-PlayerPFPayWithBank="+Message609); out.newLine();
			    out.write("610-PlayerPFPayWithJoint="+Message610); out.newLine();
			    out.write("611-CreateJoint="+Message611); out.newLine();
			    out.write("612-DeleteJoint="+Message612); out.newLine();
			    out.write("613-Joint-AddOwner="+Message613); out.newLine();
			    out.write("614-ViewTop="+Message614); out.newLine();
			    out.write("615-Joint-AddUser="+Message615); out.newLine();
			    out.write("616-Joint-RemoveOwner="+Message616); out.newLine();
			    out.write("617-Joint-RemoveUser="+Message617); out.newLine();
			    out.write("618-PAccount-Reset="+Message618); out.newLine();
			    out.write("619-PAccount-Set="+Message619); out.newLine();
			    out.write("620-PAccount-Add="+Message620); out.newLine();
			    out.write("621-PAccount-Remove="+Message621); out.newLine();
			    out.write("622-Bank-Reset="+Message622); out.newLine();
			    out.write("623-Bank-Set="+Message623); out.newLine();
			    out.write("624-Bank-Add="+Message624); out.newLine(); 
			    out.write("625-Bank-Remove="+Message625); out.newLine();
			    out.write("626-Joint-Reset="+Message626); out.newLine();
			    out.write("627-Joint-Set="+Message627); out.newLine();
			    out.write("628-Joint-Add="+Message628); out.newLine();
			    out.write("629-Joint-Remove="+Message629); out.newLine();
			    out.write("630-Joint-UserAdded="+Message630); out.newLine();
			    out.write("631-Player-PayForwardDepositJoint="+Message631); out.newLine();
			    out.write("632-Player-UsedPFtoPayJointUsingJoint="+Message632); out.newLine();
			    out.write("633-Player-UsedPFtoPayJointUsingBank="+Message633); out.newLine();
			    out.write("634-PlayerSetPFtoJoint="+Message634); out.newLine();
			    out.write("635-PlayerSetPFtoBank="+Message635); out.newLine();
			    out.write("636-Player-TurnPFOff="+Message636); out.newLine();
			    out.write("637-Player-SetJointMaxUserWithdraw="+Message637); out.newLine();
			    out.write("638-Player-ViewedOwnRank="+Message638); out.newLine();
			    out.write("639-Player-ViewedAnothersRank="+Message639); out.newLine();
			    out.write("######EOF######");
			    out.close();
			} catch (IOException e) {
				log.severe("[dConomy] - Unable to create Messages File");
			}
		}
	}
	
	//Load settings
	public void loadSettings(){
		//Get/Set Settings
		startingbalance = dCSettings.getDouble("Starting-Balance");
		MoneyName = dCSettings.getString("Money-Name");
		MySQL = dCSettings.getBoolean("Use-MySQL");
		CMySQL = dCSettings.getBoolean("Use-Canary-MySQLConn");
		Log = dCSettings.getBoolean("LogPayments");
		interest = dCSettings.getDouble("Bank-InterestRate");
		Bankdelay = dCSettings.getInt("Bank-InterestPayDelay");
		JWDelay = dCSettings.getInt("JointUserWithdrawDelay");
		breset = dCTimerreset.getLong("BankTimerResetTo");
		jwreset = dCTimerreset.getLong("JointWithdrawTimerResetTo");
		iConvert = dCSettings.getBoolean("Convert-iConomy");
		JUMWA = dCSettings.getDouble("JointUserMaxWithdrawAmount");
		aoc = dCSettings.getBoolean("AOCAPB");
		
		if (startingbalance < 0){
			startingbalance = 0;
		}
		
		log.info("[dConomy] - Settings loaded!");
		
		JUWD = new ArrayList<String>();
		dCT = new dCTimer();
		//Start Bank Interest Timer if Enabled
		if (interest > 0){
			interest = interest/100; //pull it down to correct %
		}else if (interest < 0){
			interest = 0.02/100;
		}
		if (Bankdelay > 0){
			dCBT = dCT.getdCBT();
			breset = breset - System.currentTimeMillis();
			if (breset < 0){
				breset = 1000;
			}
			dCBT.SetUpBT(this, Bankdelay, interest, breset);
			log.info("[dConomy] - Bank Interest Timer Started!");
		}
		//Start JointAccount User Withdraw Delay if Enabled
		if (JWDelay > 0){
			jwreset = jwreset - System.currentTimeMillis();
			if (jwreset < 0){
				jwreset = 1000;
			}
			dCJWDT = dCT.getdCJWDT();
			dCJWDT.SetUpJWDT(this, JWDelay, jwreset);
			log.info("[dConomy] - JointAccount User Withdraw Delay Timer Started!");
		}
		
		
		
		DataBase = dCMySQLConn.getString("DataBase", DataBase);
		UserName = dCMySQLConn.getString("UserName", UserName);
		Password = dCMySQLConn.getString("Password", Password);
		if(MySQL){
			log.info("[dConomy] - MySQL Setting Loaded!");
		}
		
		//Get/Set Error Messages
		ErrorMessage101 = dCMessage.getString("101-Player-NotEnoughMoney");
		ErrorMessage102 = dCMessage.getString("102-Bank-NotEnoughMoney");
		ErrorMessage103 = dCMessage.getString("103-NumberFormatError");
		ErrorMessage104 = dCMessage.getString("104-Command-NoPermission");
		ErrorMessage105 = dCMessage.getString("105-Joint-NotEnoughMoney");
		ErrorMessage106 = dCMessage.getString("106-AmountNotSpecified");
		ErrorMessage107 = dCMessage.getString("107-Joint-NoAccess");
		ErrorMessage108 = dCMessage.getString("108-PlayerNotFound");
		ErrorMessage109 = dCMessage.getString("109-NegativeEntered");
		ErrorMessage110 = dCMessage.getString("110-JointNotFound");
		ErrorMessage111 = dCMessage.getString("111-JointNameTaken");
		ErrorMessage112 = dCMessage.getString("112-Joint-OwnerNotSpecified");
		ErrorMessage113 = dCMessage.getString("113-InvalidCommand");
		ErrorMessage114 = dCMessage.getString("114-Joint-PlayerNotSpecified");
		ErrorMessage115 = dCMessage.getString("115-JointNameTooLong");
		ErrorMessage116 = dCMessage.getString("116-Joint-User-CannotWithdrawAgainYet");
		ErrorMessage117 = dCMessage.getString("117-Joint-User-WithdrawTooMuch");
		ErrorMessage118 = dCMessage.getString("118-Player-CannotPaySelf");
		ErrorMessage119 = dCMessage.getString("119-Player-AlreadyJointUser");
		ErrorMessage120 = dCMessage.getString("120-PlayerNotSpecified");
		ErrorMessage121 = dCMessage.getString("121-Joint-UserNotSpecified");
		ErrorMessage122 = dCMessage.getString("122-Joint-PlayerAlreadyOwner");
		ErrorMessage123 = dCMessage.getString("123-Joint-OwnerNotSpecified");
		ErrorMessage124 = dCMessage.getString("124-Joint-PlayerAlreadyNotUser");
		ErrorMessage125 = dCMessage.getString("125-Joint-PlayerAlreadyNotOwner");
		ErrorMessage126 = dCMessage.getString("126-NegativeNumber");
		ErrorMessage127 = dCMessage.getString("127-BalanceNegative");
		log.info("[dConomy] - ErrorMessages Loaded!");
		
		//Get/Set Messages
		//Player Messages (200 Series)
		Message201 = dCMessage.getString("201-Player-AccountBalance");
		Message202 = dCMessage.getString("202-Player-BankBalance");
		Message203 = dCMessage.getString("203-Player-BankWithdraw");
		Message204 = dCMessage.getString("204-Player-BankDeposit");
		Message205 = dCMessage.getString("205-Player-BankNewBalance");
		Message206 = dCMessage.getString("206-Player-AccountNewBalance");
		Message207 = dCMessage.getString("207-Player-SentPaymentTo");
		Message208 = dCMessage.getString("208-Player-ReceivedPaymentFrom");
		Message209 = dCMessage.getString("209-Player-RankSelf");
		Message210 = dCMessage.getString("210-Player-RankOther");
		Message211 = dCMessage.getString("211-Player-PayForwardJoint");
		Message212 = dCMessage.getString("212-Player-PaymentSentFromJoint");
		Message213 = dCMessage.getString("213-Player-PayForwardBank");
		Message214 = dCMessage.getString("214-Player-PaymentSentFromBank");
		Message215 = dCMessage.getString("215-Player-PayForwardSetJoint");
		Message216 = dCMessage.getString("216-Player-PayForwardSetBank");
		Message217 = dCMessage.getString("217-Player-PaymentSentToJoint");
		Message218 = dCMessage.getString("218-Player-PayForwardTurnedOff");
		Message219 = dCMessage.getString("219-Player-PayForwardingIsSetJoint");
		Message220 = dCMessage.getString("220-Player-PayForwardingIsSetBank");
		Message221 = dCMessage.getString("221-Player-PayForwardingNotActive");
		Message222 = dCMessage.getString("222-Player-AccountCheck");
		Message223 = dCMessage.getString("223-Player-BankCheck");
		log.info("[dConomy] - Player Messages Loaded!");
		
		//Joint Account Messages (300 Series)
		Message301 = dCMessage.getString("301-Joint-AccountBalance");
		Message302 = dCMessage.getString("302-Joint-NewBalance");
		Message303 = dCMessage.getString("303-Joint-Withdraw");
		Message304 = dCMessage.getString("304-Joint-Deposit");
		Message305 = dCMessage.getString("305-Joint-Created");
		Message306 = dCMessage.getString("306-Joint-OwnerAdded");
		Message307 = dCMessage.getString("307-Joint-OwnerRemoved");
		Message308 = dCMessage.getString("308-Joint-AccountDeleted");
		Message309 = dCMessage.getString("309-Joint-UserAdded");
		Message310 = dCMessage.getString("310-Joint-UserRemoved");
		Message311 = dCMessage.getString("311-Joint-MaxUserWithdrawCheck");
	    Message312 = dCMessage.getString("312-Joint-MaxUserWithdrawSet");
		log.info("[dConomy] - Joint Account Messages Loaded!");
		
		//Admin/Misc Message (400 Series)
		Message401 = dCMessage.getString("401-Rank-Top");
		Message402 = dCMessage.getString("402-Rank-Sorting");
		Message403 = dCMessage.getString("403-Rank-NoTop");
		Message404 = dCMessage.getString("404-Admin-Player-Reset");
		Message405 = dCMessage.getString("405-Admin-Player-Set");
		Message406 = dCMessage.getString("406-Admin-Player-Remove");
		Message407 = dCMessage.getString("407-Admin-Player-Add");
		Message408 = dCMessage.getString("408-Admin-Bank-Reset");
		Message409 = dCMessage.getString("409-Admin-Bank-Set");
		Message410 = dCMessage.getString("410-Admin-Bank-Remove");
		Message411 = dCMessage.getString("411-Admin-Bank-Add");
		Message412 = dCMessage.getString("412-Admin-Joint-Reset");
		Message413 = dCMessage.getString("413-Admin-Joint-Set");
		Message414 = dCMessage.getString("414-Admin-Joint-Add");
		Message415 = dCMessage.getString("415-Admin-Joint-Remove");
		log.info("[dConomy] - Admin/Misc Messages Loaded!");
		
		//Help Display (500 Series)
		Message501 = dCMessage.getString("501-Money-HelpOpen");
		Message502 = dCMessage.getString("502-RequiredOptionalAlias");
		Message503 = dCMessage.getString("503-Money-Base");
		Message504 = dCMessage.getString("504-Money-Pay");
		Message505 = dCMessage.getString("505-Money-Rank");
		Message506 = dCMessage.getString("506-Money-Top");
		Message507 = dCMessage.getString("507-Money-Set");
		Message508 = dCMessage.getString("508-Money-Reset");
		Message509 = dCMessage.getString("509-Money-Add");
		Message510 = dCMessage.getString("510-Money-Remove");
		Message511 = dCMessage.getString("511-Money-Auto");
		Message512 = dCMessage.getString("512-Money-SetAuto");
		Message513 = dCMessage.getString("513-Money-UseJointHelp");
		Message514 = dCMessage.getString("514-Money-UseBankHelp");
		Message515 = dCMessage.getString("515-Bank-HelpOpen");
		Message516 = dCMessage.getString("516-Bank-Base");
		Message517 = dCMessage.getString("517-Bank-Withdraw");
		Message518 = dCMessage.getString("518-Bank-Deposit");
		Message519 = dCMessage.getString("519-Bank-Reset");
		Message520 = dCMessage.getString("520-Bank-Set");
		Message521 = dCMessage.getString("521-Bank-Add");
		Message522 = dCMessage.getString("522-Bank-Remove");
		Message523 = dCMessage.getString("523-Joint-HelpOpen");
		Message524 = dCMessage.getString("524-Joint-Base");
		Message525 = dCMessage.getString("525-Joint-Withdraw");
		Message526 = dCMessage.getString("526-Joint-Deposit");
		Message527 = dCMessage.getString("527-Joint-Pay");
		Message528 = dCMessage.getString("528-Joint-Create");
		Message529 = dCMessage.getString("529-Joint-Delete");
		Message530 = dCMessage.getString("530-Joint-AddOwner");
		Message531 = dCMessage.getString("531-Joint-RemoveOwner");
		Message532 = dCMessage.getString("532-Joint-AddUser");
		Message533 = dCMessage.getString("533-Joint-RemoveUser");
		Message534 = dCMessage.getString("534-Joint-UserMax");
		Message535 = dCMessage.getString("535-Joint-Reset");
		Message536 = dCMessage.getString("536-Joint-Set");
		Message537 = dCMessage.getString("537-Joint-Add");
		Message538 = dCMessage.getString("538-Joint-Remove");
		Message539 = dCMessage.getString("539-Joint-UserMaxCheck");
		Message540 = dCMessage.getString("540-Help-PlayerAmountAccount");
		Message541 = dCMessage.getString("541-Help-UseMoneyAdmin");
		Message542 = dCMessage.getString("542-Help-UseBankAdmin");
		Message543 = dCMessage.getString("543-Help-UseJointAdmin");
		Message544 = dCMessage.getString("544-Help-MoneyAdminOpen");
		Message545 = dCMessage.getString("545-Help-BankAdminOpen");
		Message546 = dCMessage.getString("546-Help-JointAdminOpen");
		log.info("[dConomy] - Help Display Messages Loaded!");
		
		//Logging (600 Series)
		Message601 = dCMessage.getString("601-PlayerPayPlayer");
		Message602 = dCMessage.getString("602-PlayerDepositBank");
		Message603 = dCMessage.getString("603-PlayerWithdrawBank");
		Message604 = dCMessage.getString("604-PlayerWithdrawJoint");
		Message605 = dCMessage.getString("605-PlayerDepositJoint");
		Message606 = dCMessage.getString("606-PaidJoint");
		Message607 = dCMessage.getString("607-PlayerPaidWithJoint");
		Message608 = dCMessage.getString("608-PlayerPFBank");
		Message609 = dCMessage.getString("609-PlayerPFPayWithBank");
		Message610 = dCMessage.getString("610-PlayerPFPayWithJoint");
		Message611 = dCMessage.getString("611-CreateJoint");
		Message612 = dCMessage.getString("612-DeleteJoint");
		Message613 = dCMessage.getString("613-Joint-AddOwner");
		Message614 = dCMessage.getString("614-ViewTop");
		Message615 = dCMessage.getString("615-Joint-AddUser");
		Message616 = dCMessage.getString("616-Joint-RemoveOwner");
		Message617 = dCMessage.getString("617-Joint-RemoveUser");
		Message618 = dCMessage.getString("618-PAccount-Reset");
		Message619 = dCMessage.getString("619-PAccount-Set");
		Message620 = dCMessage.getString("620-PAccount-Add");
		Message621 = dCMessage.getString("621-PAccount-Remove");
		Message622 = dCMessage.getString("622-Bank-Reset");
		Message623 = dCMessage.getString("623-Bank-Set");
		Message624 = dCMessage.getString("624-Bank-Add");
		Message625 = dCMessage.getString("625-Bank-Remove");
		Message626 = dCMessage.getString("626-Joint-Reset");
		Message627 = dCMessage.getString("627-Joint-Set");
		Message628 = dCMessage.getString("628-Joint-Add");
		Message629 = dCMessage.getString("629-Joint-Remove");
		Message630 = dCMessage.getString("630-Joint-UserAdded");
		Message631 = dCMessage.getString("631-Player-PayForwardDepositJoint");
		Message632 = dCMessage.getString("632-Player-UsedPFtoPayJointUsingJoint");
	    Message633 = dCMessage.getString("633-Player-UsedPFtoPayJointUsingBank");
	    Message634 = dCMessage.getString("634-PlayerSetPFtoJoint");
	    Message635 = dCMessage.getString("635-PlayerSetPFtoBank");
	    Message636 = dCMessage.getString("636-Player-TurnPFOff");
	    Message637 = dCMessage.getString("637-Player-SetJointMaxUserWithdraw");
	   	Message638 = dCMessage.getString("638-Player-ViewedOwnRank");
	   	Message639 = dCMessage.getString("639-Player-ViewedAnothersRank");
		log.info("[dConomy] - Logging Messages Loaded!");
		
		if (MySQL){
			CreateTable();
		}
		
		if (iConvert){
			iConvertion();
		}
		
		if (JWDelay > 0){
			String filename= direJoint + JUWDLoc;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line;
				while ((line = reader.readLine()) != null) {
					JUWD.add(line);
				}
				reader.close();
			}catch (IOException e){
				log.severe("[dConomy] - Unable to Load JUWD Users");
			}
		}
		
	}
	
	public void SetReset(String type, long time){
		jwreset = time;
		dCTimerreset.setLong(type, time);
	}
	
	public boolean JointUserWithDrawDelayCheck(String name){
		return JUWD.contains(name);
	}
	
	public void JointUserWithdrawDelayAdd(String name){
		if (JWDelay > 0);
			JUWD.add(name);
			String filename= direJoint + JUWDLoc;
			File JUWDFile = new File(filename);
			if (!JUWDFile.exists()){ 
				try {
					JUWDFile.createNewFile();
				} catch (IOException e) {
					log.severe("[dConomy] - Unable to create new JUWD File!");
				} 
			}
			try {
				FileWriter fw = new FileWriter(filename,true);
				fw.write(name+System.getProperty("line.separator"));
				fw.close();  
			} catch (IOException e) {
				log.severe("[dConomy] - Unable to add user to JUWD Transaction!");
			}
	}
	
	public void JointUserWithdrawDelayReset(){
		JUWD.clear();
		String filename= direJoint + JUWDLoc;
		File JUWDFile = new File(filename);
		try {
			JUWDFile.createNewFile();
		} catch (IOException e) {
			log.severe("[dConomy] - Unable to create new JUWD File!");
		}
	}
	
	//Return Starting Balance for new players
	public double getStartingBalance(){
		double startbal = Double.valueOf((String.valueOf(numform.format(startingbalance))));
		return startbal;
	}
	
	public boolean JointAccountUserCheck(String player, String name){
		String users = getJointUsers(name);
		String[] usersplit = users.split(",");
		for (int i = 0; i < usersplit.length; i++){
			if (usersplit[i].equals(player)){
				return true;
			}
		}
		return false;
	}
	
	public boolean JointAccountOwnerCheck(String player, String name){
		String owners = getJointOwners(name);
		String[] ownersplit = owners.split(",");
		for (int i = 0; i < ownersplit.length; i++){
			if (ownersplit[i].equals(player)){
				return true;
			}
		}
		return false;
	}
	
	//Return MoneyName
	public String getMoneyName(){
		return MoneyName;
	}
	
	public Connection getSQLConn(){
		Connection conn = null;
		if (CMySQL){
			conn = etc.getSQLConnection();
		}else{
			try {
				conn = DriverManager.getConnection(DataBase, UserName, Password);
			} catch (SQLException e) {
				log.severe("[dConomy] - Unable to set MySQL Connection");
			}
		}
		return conn;
	}
	
	public void CreateTable(){
		String table1 = ("CREATE TABLE IF NOT EXISTS `dConomy` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Player` varchar(16) NOT NULL, `Account` DECIMAL(64,2) NOT NULL, `Bank` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
		String table2 = ("CREATE TABLE IF NOT EXISTS `dConomyJoint` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Name` varchar(32) NOT NULL, `Owners` text NOT NULL, `Users` text NOT NULL, `Balance` DECIMAL(64,2) NOT NULL, `UserMaxWithdraw` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
		String table3 = ("CREATE TABLE IF NOT EXISTS `dConomyLog` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Date` varchar(32) NOT NULL, `Time` varchar(32) NOT NULL, `Transaction` Text NOT NULL, PRIMARY KEY (`ID`))");
		Connection conn = getSQLConn();
		try{
			if (conn != null){
				conn.setAutoCommit(false);
				Statement st = conn.createStatement();
				st.executeUpdate(table1);
				st.executeUpdate(table2);
				st.executeUpdate(table3);
			}
		}catch (SQLException sqle) {
			log.severe("[dConomy] - Could not create the table");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				log.severe("[dConomy] - Could not close connection to SQL");
			}
		}
	}
	
	public boolean keyExists(String key, String type){
		if (MySQL){
			boolean exists = false;
			Connection conn = getSQLConn();
    		try{
    			PreparedStatement ps = conn.prepareStatement("SELECT * FROM dConomy WHERE Player = ?");
    			ps.setString(1, key);
    			ResultSet rs = ps.executeQuery();
    			if (rs.next()){
    				exists = true;
    			}
    		} catch (SQLException ex) {
				log.severe("[dConomy] - Unable to verify existance for " + key + "!");
			}finally{
				try{
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
    		return exists;
		}else{
			String file = getFile(type);
			PropertiesFile check = new PropertiesFile(dire+file);
			return check.containsKey(key);
		}
	}
	
	public boolean JointkeyExists(String name){
		if (MySQL){
			boolean exists = false;
			Connection conn = null;
    		try{
    			conn = getSQLConn();
    			PreparedStatement ps = conn.prepareStatement("SELECT * FROM dConomyJoint WHERE Name = ?");
    			ps.setString(1, name);
    			ResultSet rs = ps.executeQuery();
    			if (rs.next()){
    				exists = true;
    			}
    		} catch (SQLException ex) {
				log.severe("[dConomy] Unable to verify existance for JointAccount: " + name + "!");
			}finally{
				try{
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
    		return exists;
		}else{
			String jointloc = getJointFile(name);
			File joint = new File(jointloc);
			return joint.exists();
		}
	}

	public double getBalance(String player, String type){
		if (MySQL){
			String column = getMySQLColumn(type);
			double bal = 0;
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
    		ResultSet rs = null;
			try{
				ps = conn.prepareStatement("SELECT "+column+" FROM dConomy WHERE Player = ?");
				ps.setString(1, player);
    			rs = ps.executeQuery();
    			while (rs.next()){
    				bal = Double.parseDouble(rs.getString(column));
  	    	 	 }
			}catch (SQLException ex){
				log.severe("[dConomy] Unable to get '"+type+" Balance for " + player + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
			return bal;
    	}else{
    		String file = getFile(type);
			PropertiesFile acc = new PropertiesFile(dire+file);
    		return Double.parseDouble(String.valueOf(numform.format(acc.getDouble(player))));
		}
	}
	
	public void setBalance(double bal, String player, String type){
		double newbal = Double.parseDouble(String.valueOf(numform.format(bal)));
		if (MySQL){
			String column = getMySQLColumn(type);
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("UPDATE dConomy SET "+column+" = ? WHERE Player = ? LIMIT 1");
				ps.setDouble(1, newbal);
				ps.setString(2, player);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] - Unable to update '"+type+" Balance' for " + player + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			String file = getFile(type);
			PropertiesFile acc = new PropertiesFile(dire+file);
    		acc.setDouble(player, newbal);
		}
	}
	
	public void setInitialBalance(double bal, String player){
		double newbal = Double.parseDouble(String.valueOf(numform.format(bal)));
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("INSERT INTO dConomy (Player, Account, Bank) VALUES(?,?,?)");
				ps.setString(1, player);
				ps.setDouble(2, newbal);
				ps.setDouble(3, 0);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] - Unable to 'Create' new account for " + player + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			PropertiesFile acc = new PropertiesFile(dire+"dCAccounts.txt");
			PropertiesFile bank = new PropertiesFile(dire+"dCBank.txt");
    		acc.setDouble(player, newbal);
    		bank.setDouble(player, 0);
		}
	}
	
	public String getJointUsers(String name){
		String Users = "";
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				ps = conn.prepareStatement("SELECT Users FROM dConomyJoint WHERE Name = ?");
				ps.setString(1, name);
				rs = ps.executeQuery();
				while (rs.next()){
					Users = rs.getString("Users");
				}
			}catch (SQLException ex){
				log.severe("[dConomy] - Unable to get 'Users' for JointsAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
    	}else{
    		dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		Users = dCJointAccounts.getString("Users");
		}
		return Users;
	}
	
	public String getJointOwners(String name){
		String Owners = "";
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				ps = conn.prepareStatement("SELECT Owners FROM dConomyJoint WHERE Name = ?");
				ps.setString(1, name);
				rs = ps.executeQuery();
				while (rs.next()){
					Owners = rs.getString("Users");
				}
			}catch (SQLException ex){
				log.severe("[dConomy] - Unable to get 'Owners' for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
    	}else{
    		dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		Owners = dCJointAccounts.getString("Owners");
		}
		return Owners;
	}
	
	public double getJointBalance(String name){
		if (MySQL){
			double bal = 0;
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
    		ResultSet rs = null;
			try{
				ps = conn.prepareStatement("SELECT Balance FROM dConomyJoint WHERE Name = ?");
				ps.setString(1, name);
    			rs = ps.executeQuery();
    			while (rs.next()){
    				bal = Double.parseDouble(rs.getString("Balance"));
  	    	 	 }
			}catch (SQLException ex){
				log.severe("[dConomy] Unable to get Balance for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
			return bal;
    	}else{
    		dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		return Double.parseDouble(String.valueOf(numform.format(dCJointAccounts.getDouble("balance"))));
		}
	}
	
	public double getJointUserWithdrawMax(String name){
		if (MySQL){
			double bal = 0;
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
    		ResultSet rs = null;
			try{
				ps = conn.prepareStatement("SELECT UserMaxWithdraw FROM dConomyJoint WHERE Name = ?");
				ps.setString(1, name);
    			rs = ps.executeQuery();
    			while (rs.next()){
    				bal = Double.parseDouble(rs.getString("UserMaxWithdraw"));
  	    	 	 }
			}catch (SQLException ex){
				log.severe("[dConomy] Unable to get Balance for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
			return bal;
    	}else{
    		dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		return dCJointAccounts.getDouble("UserMaxWithdraw");
    	}
	}
	
	public void setJointBalance(double bal, String accName){
		double newbal = Double.parseDouble(String.valueOf(numform.format(bal)));
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("UPDATE dConomyJoint SET Balance = ? WHERE Name = ? LIMIT 1");
				ps.setDouble(1, newbal);
				ps.setString(2, accName);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] - Unable to update 'Balance' for JointAccount: " + accName + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			dCJointAccounts = new PropertiesFile(direJoint + accName + JointAcc);
    		dCJointAccounts.setDouble("balance", newbal);
		}
	}
	
	public void createJointAccount(String name, String owner){
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("INSERT INTO dConomyJoint (Name, Owners, Users, Balance, UserMaxWithdraw)VALUES (?,?,?,?,?)", 1);
				ps.setString(1, name);
				ps.setString(2, owner);
				ps.setString(3, ",");
				ps.setDouble(4, 0);
				ps.setDouble(5, JUMWA);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] Unable to create new JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
			dCJointAccounts.setString("Owners", owner);
			dCJointAccounts.setString("Users", "");
    		dCJointAccounts.setDouble("balance", 0);
    		dCJointAccounts.setDouble("UserMaxWithdraw", 25);
		}
	}
	
	public void deleteJointAccount(String accName){
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("DELETE FROM dConomyJoint WHERE Name = ?");
				ps.setString(1, accName);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] Unable to delete JointAccount: " + accName + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			File file = new File(direJoint + accName + JointAcc);
			file.delete();
		}
	}
	
	public void updateJointOwners(String name, String newOwners){
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("UPDATE dConomyJoint SET Owners = ? WHERE Name = ? LIMIT 1");
				ps.setString(1, newOwners);
				ps.setString(2, name);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] Unable to update 'Owners' for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		dCJointAccounts.setString("Owners", newOwners);
		}
	}
	
	public void updateJointUsers(String name, String newUsers){
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("UPDATE dConomyJoint SET Users = ? WHERE Name = ? LIMIT 1");
				ps.setString(1, newUsers);
				ps.setString(2, name);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] Unable to update 'Users' for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		dCJointAccounts.setString("Users", newUsers);
		}
	}
	
	public void updateJointMaxUserWithdraw(String name, double maxamount){
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("UPDATE dConomyJoint SET UserMaxWithdraw = ? WHERE Name = ? LIMIT 1");
				ps.setDouble(1, maxamount);
				ps.setString(2, name);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] Unable to update 'Users' for JointAccount: " + name + "!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
    		dCJointAccounts.setDouble("MaxUserWithdraw", maxamount);
		}
	}
	
	public String getFile(String type){
		String File = "";
		if (type.equalsIgnoreCase("Account")){
			File = "dCAccounts.txt";
		}else if (type.equalsIgnoreCase("Bank")){
			File = "dCBank.txt";
		}
		return File;
	}
	
	public String getJointFile(String name){
		String filename = direJoint + name + JointAcc;
		return filename;
	}
	
	public String getMySQLColumn(String type){
		String Column = "";
		if (type.equalsIgnoreCase("account")){
			Column = "Account";
		}else if (type.equalsIgnoreCase("bank")){
			Column = "Bank";
		}
		return Column;
	}
	
	public boolean getPayForwardCheck(String name){
		if(dCPayForwarding.keyExists(name+PFCS)){
			return dCPayForwarding.getBoolean(name+PFCS);
		}
		return false;
	}
	
	public String getPayForwardAccount(String name){
		if (dCPayForwarding.keyExists(name+PFAS)){
			return dCPayForwarding.getString(name+PFAS);
		}
		return "Account";
	}
	
	public void setPayForward(String name, boolean set){
		dCPayForwarding.setBoolean(name+PFCS, set);
	}
	
	public void setPayForwardAcc(String name, String acc){
		dCPayForwarding.setString(name+PFAS, acc);
	}
	
	public String getMessage(int code, String player, String type, double amount){
		switch (code){
		case 201: return parseMessage(Message201, player, type, amount);
		case 202: return parseMessage(Message202, player, type, amount);
		case 203: return parseMessage(Message203, player, type, amount);
		case 204: return parseMessage(Message204, player, type, amount);
		case 205: return parseMessage(Message205, player, type, amount);
		case 206: return parseMessage(Message206, player, type, amount);
		case 207: return parseMessage(Message207, player, type, amount);
		case 208: return parseMessage(Message208, player, type, amount);
		case 209: return parseMessage(Message209, player, type, amount);
		case 210: return parseMessage(Message210, player, type, amount);
		case 211: return parseMessage(Message211, player, type, amount);
		case 212: return parseMessage(Message212, player, type, amount);
		case 213: return parseMessage(Message213, player, type, amount);
		case 214: return parseMessage(Message214, player, type, amount);
		case 215: return parseMessage(Message215, player, type, amount);
		case 216: return parseMessage(Message216, player, type, amount);
		case 217: return parseMessage(Message217, player, type, amount);
		case 218: return parseMessage(Message218, player, type, amount);
		case 219: return parseMessage(Message219, player, type, amount);
		case 220: return parseMessage(Message220, player, type, amount);
		case 221: return parseMessage(Message221, player, type, amount);
		case 222: return parseMessage(Message222, player, type, amount);
		case 223: return parseMessage(Message223, player, type, amount);
		
		case 301: return parseMessage(Message301, player, type, amount);
		case 302: return parseMessage(Message302, player, type, amount);
		case 303: return parseMessage(Message303, player, type, amount);
		case 304: return parseMessage(Message304, player, type, amount);
		case 305: return parseMessage(Message305, player, type, amount);
		case 306: return parseMessage(Message306, player, type, amount);
		case 307: return parseMessage(Message307, player, type, amount);
		case 308: return parseMessage(Message308, player, type, amount);
		case 309: return parseMessage(Message309, player, type, amount);
		case 310: return parseMessage(Message310, player, type, amount);
		case 311: return parseMessage(Message311, player, type, amount);
		case 312: return parseMessage(Message312, player, type, amount);
		
		case 401: return parseMessage(Message401, player, type, amount).replace("§2[§fdCo§2]§f", "");
		case 402: return parseMessage(Message402, player, type, amount).replace("§2[§fdCo§2]§f", "");
		case 403: return parseMessage(Message403, player, type, amount).replace("§2[§fdCo§2]§f", "");
		case 404: return parseMessage(Message404, player, type, amount);
		case 405: return parseMessage(Message405, player, type, amount);
		case 406: return parseMessage(Message406, player, type, amount);
		case 407: return parseMessage(Message407, player, type, amount);
		case 408: return parseMessage(Message408, player, type, amount);
		case 409: return parseMessage(Message409, player, type, amount);
		case 410: return parseMessage(Message410, player, type, amount);
		case 411: return parseMessage(Message411, player, type, amount);
		case 412: return parseMessage(Message412, player, type, amount);
		case 413: return parseMessage(Message413, player, type, amount);
		case 414: return parseMessage(Message414, player, type, amount);
		case 415: return parseMessage(Message415, player, type, amount);
			
		default: return "MESSAGE LINE MISSING MC:"+code;
		}
	}
	
	public String getHelpMessage(int code){
		switch(code){
		case 501: return ColorCode(Message501);
		case 502: return ColorCode(Message502);
		case 503: return ColorCode(Message503);
		case 504: return ColorCode(Message504);
		case 505: return ColorCode(Message505);
		case 506: return ColorCode(Message506);
		case 507: return ColorCode(Message507);
		case 508: return ColorCode(Message508);
		case 509: return ColorCode(Message509);
		case 510: return ColorCode(Message510);
		case 511: return ColorCode(Message511);
		case 512: return ColorCode(Message512);
		case 513: return ColorCode(Message513);
		case 514: return ColorCode(Message514);
		case 515: return ColorCode(Message515);
		case 516: return ColorCode(Message516);
		case 517: return ColorCode(Message517);
		case 518: return ColorCode(Message518);
		case 519: return ColorCode(Message519);
		case 520: return ColorCode(Message520);
		case 521: return ColorCode(Message521);
		case 522: return ColorCode(Message522);
		case 523: return ColorCode(Message523);
		case 524: return ColorCode(Message524);
		case 525: return ColorCode(Message525);
		case 526: return ColorCode(Message526);
		case 527: return ColorCode(Message527);
		case 528: return ColorCode(Message528);
		case 529: return ColorCode(Message529);
		case 530: return ColorCode(Message530);
		case 531: return ColorCode(Message531);
		case 532: return ColorCode(Message532);
		case 533: return ColorCode(Message533);
		case 534: return ColorCode(Message534);
		case 535: return ColorCode(Message535);
		case 536: return ColorCode(Message536);
		case 537: return ColorCode(Message537);
		case 538: return ColorCode(Message538);
		case 539: return ColorCode(Message539);
		case 540: return ColorCode(Message540);
		case 541: return ColorCode(Message541);
		case 542: return ColorCode(Message542);
		case 543: return ColorCode(Message543);
		case 544: return ColorCode(Message544);
		case 545: return ColorCode(Message545);
		case 546: return ColorCode(Message546);
		default: return "HELP LINE MISSING HM:"+code;
		}
	}

	public String getErrorMessage(int code, String name){
		String type = "UNKNOWN";
		switch (code){
		case 101: return parseMessage(ErrorMessage101, name, type, 0);
		case 102: return parseMessage(ErrorMessage102, name, type, 0);
		case 103: return parseMessage(ErrorMessage103, name, type, 0);
		case 104: return parseMessage(ErrorMessage104, name, type, 0);
		case 105: return parseMessage(ErrorMessage105, type, name, 0);
		case 106: return parseMessage(ErrorMessage106, name, type, 0);
		case 107: return parseMessage(ErrorMessage107, name, type, 0);
		case 108: return parseMessage(ErrorMessage108, name, type, 0);
		case 109: return parseMessage(ErrorMessage109, name, type, 0);
		case 110: return parseMessage(ErrorMessage110, type,  name, 0);
		case 111: return parseMessage(ErrorMessage111, type, name, 0);
		case 112: return parseMessage(ErrorMessage112, name, type, 0);
		case 113: return parseMessage(ErrorMessage113, name, type, 0);
		case 114: return parseMessage(ErrorMessage114, name, type, 0);
		case 115: return parseMessage(ErrorMessage115, name, type, 0);
		case 116: return parseMessage(ErrorMessage116, name, type, 0);
		case 117: return parseMessage(ErrorMessage117, name, type, 0);
		case 118: return parseMessage(ErrorMessage118, name, type, 0);
		case 119: return parseMessage(ErrorMessage119, name, type, 0);
		case 120: return parseMessage(ErrorMessage120, name, type, 0);
		case 121: return parseMessage(ErrorMessage121, name, type, 0);
		case 122: return parseMessage(ErrorMessage122, name, type, 0);
		case 123: return parseMessage(ErrorMessage123, name, type, 0);
		case 124: return parseMessage(ErrorMessage124, name, type, 0);
		case 125: return parseMessage(ErrorMessage125, name, type, 0);
		case 126: return parseMessage(ErrorMessage126, name, type, 0);
		case 127: return parseMessage(ErrorMessage127, name, type, 0);
		default: return "ERROR CODE MISSING EC:"+code;
		}
	}
	
	public void Logging(int code, String p1, String p2, String amount, String account){
		if (Log){
			switch(code){
			case 601: LogTrans(parseLog(Message601, p1, p2, amount, account)+" (LC:601)"); return;
			case 602: LogTrans(parseLog(Message602, p1, p2, amount, account)+" (LC:602)"); return;
			case 603: LogTrans(parseLog(Message603, p1, p2, amount, account)+" (LC:603)"); return;
			case 604: LogTrans(parseLog(Message604, p1, p2, amount, account)+" (LC:604)"); return;
			case 605: LogTrans(parseLog(Message605, p1, p2, amount, account)+" (LC:605)"); return;
			case 606: LogTrans(parseLog(Message606, p1, p2, amount, account)+" (LC:606)"); return;
			case 607: LogTrans(parseLog(Message607, p1, p2, amount, account)+" (LC:607)"); return;
			case 608: LogTrans(parseLog(Message608, p1, p2, amount, account)+" (LC:608)"); return;
			case 609: LogTrans(parseLog(Message609, p1, p2, amount, account)+" (LC:609)"); return;
			case 610: LogTrans(parseLog(Message610, p1, p2, amount, account)+" (LC:610)"); return;
			case 611: LogTrans(parseLog(Message611, p1, p2, amount, account)+" (LC:611)"); return;
			case 612: LogTrans(parseLog(Message612, p1, p2, amount, account)+" (LC:612)"); return;
			case 613: LogTrans(parseLog(Message613, p1, p2, amount, account)+" (LC:613)"); return;
			case 614: LogTrans(parseLog(Message614, p1, p2, amount, account)+" (LC:614)"); return;
			case 615: LogTrans(parseLog(Message615, p1, p2, amount, account)+" (LC:615)"); return;
			case 616: LogTrans(parseLog(Message616, p1, p2, amount, account)+" (LC:616)"); return;
			case 617: LogTrans(parseLog(Message617, p1, p2, amount, account)+" (LC:617)"); return;
			case 618: LogTrans(parseLog(Message618, p1, p2, amount, account)+" (LC:618)"); return;
			case 619: LogTrans(parseLog(Message619, p1, p2, amount, account)+" (LC:619)"); return;
			case 620: LogTrans(parseLog(Message620, p1, p2, amount, account)+" (LC:620)"); return;
			case 621: LogTrans(parseLog(Message621, p1, p2, amount, account)+" (LC:621)"); return;
			case 622: LogTrans(parseLog(Message622, p1, p2, amount, account)+" (LC:622)"); return;
			case 623: LogTrans(parseLog(Message623, p1, p2, amount, account)+" (LC:623)"); return;
			case 624: LogTrans(parseLog(Message624, p1, p2, amount, account)+" (LC:624)"); return;
			case 625: LogTrans(parseLog(Message625, p1, p2, amount, account)+" (LC:625)"); return;
			case 626: LogTrans(parseLog(Message626, p1, p2, amount, account)+" (LC:626)"); return;
			case 627: LogTrans(parseLog(Message627, p1, p2, amount, account)+" (LC:627)"); return;
			case 628: LogTrans(parseLog(Message628, p1, p2, amount, account)+" (LC:628)"); return;
			case 629: LogTrans(parseLog(Message629, p1, p2, amount, account)+" (LC:629)"); return;
			case 630: LogTrans(parseLog(Message630, p1, p2, amount, account)+" (LC:630)"); return;
			case 631: LogTrans(parseLog(Message631, p1, p2, amount, account)+" (LC:631)"); return;
			case 632: LogTrans(parseLog(Message632, p1, p2, amount, account)+" (LC:632)"); return;
			case 633: LogTrans(parseLog(Message633, p1, p2, amount, account)+" (LC:633)"); return;
			case 634: LogTrans(parseLog(Message634, p1, p2, amount, account)+" (LC:634)"); return;
			case 635: LogTrans(parseLog(Message635, p1, p2, amount, account)+" (LC:635)"); return;
			case 636: LogTrans(parseLog(Message636, p1, p2, amount, account)+" (LC:636)"); return;
			case 637: LogTrans(parseLog(Message637, p1, p2, amount, account)+" (LC:637)"); return;
			case 638: LogTrans(parseLog(Message638, p1, p2, amount, account)+" (LC:638)"); return;
			case 639: LogTrans(parseLog(Message639, p1, p2, amount, account)+" (LC:639)"); return;
			default: LogTrans("UNKNOWN TRANSACTION HAPPENED! LC:" + code);
			}
		}
	}
	
	public String parseLog(String message, String p1, String p2, String amount, String account){
		message = message.replace("<p1>", p1);
		message = message.replace("<p2>", p2);
		message = message.replace("<a>", amount);
		message = message.replace("<acc>", account);
		return message;
	}
	
	public String parseMessage(String Message, String player, String type,  double amount){
		String parsedMessage = "";
		int x = (int)Math.floor((jwreset - System.currentTimeMillis()) / 1000);
		int xm = (int)Math.floor(x / (60));
		String mins = String.valueOf(JWDelay);
		parsedMessage = ColorCode(Message);
		parsedMessage = parsedMessage.replace("<p>", player);
		parsedMessage = parsedMessage.replace("<m>", MoneyName);
		parsedMessage = parsedMessage.replace("<a>", String.valueOf(displayform.format(amount)));
		parsedMessage = parsedMessage.replace("<acc>", type);
		parsedMessage = parsedMessage.replace("<rank>", type);
		parsedMessage = parsedMessage.replace("<min>", mins);
		parsedMessage = parsedMessage.replace("<xmin>", String.valueOf(xm));
		return "§2[§fdCo§2]§f "+parsedMessage;
	}
	
	public String ColorCode(String codereplace){
		codereplace = codereplace.replace("<black>", Colors.Black);
		codereplace = codereplace.replace("<blue>", Colors.Blue);
		codereplace = codereplace.replace("<darkpurple>", Colors.DarkPurple);
		codereplace = codereplace.replace("<gold>", Colors.Gold);
		codereplace = codereplace.replace("<gray>", Colors.Gray);
		codereplace = codereplace.replace("<green>", Colors.Green);
		codereplace = codereplace.replace("<lightblue>", Colors.LightBlue);
		codereplace = codereplace.replace("<lightgray>", Colors.LightGray);
		codereplace = codereplace.replace("<lightgreen>", Colors.LightGreen);
		codereplace = codereplace.replace("<lightpurple>", Colors.LightPurple);
		codereplace = codereplace.replace("<navy>", Colors.Navy);
		codereplace = codereplace.replace("<purple>", Colors.Purple);
		codereplace = codereplace.replace("<red>", Colors.Red);
		codereplace = codereplace.replace("<rose>", Colors.Rose);
		codereplace = codereplace.replace("<white>", Colors.White);
		codereplace = codereplace.replace("<yellow>", Colors.Yellow);
		return codereplace;
	}
	
	public String[] decolormessage(String codereplace){
		String[] messcheck = new String[2];
		String[] colorscount = codereplace.split("§");
		int i = colorscount.length;
		messcheck[1] = String.valueOf(i);
		codereplace = codereplace.replace(Colors.Black, "");
		codereplace = codereplace.replace(Colors.Blue, "");
		codereplace = codereplace.replace(Colors.DarkPurple, "");
		codereplace = codereplace.replace(Colors.Gold, "");
		codereplace = codereplace.replace(Colors.Gray, "");
		codereplace = codereplace.replace(Colors.Green, "");
		codereplace = codereplace.replace(Colors.LightBlue, "");
		codereplace = codereplace.replace(Colors.LightGray, "");
		codereplace = codereplace.replace(Colors.LightGreen, "");
		codereplace = codereplace.replace(Colors.LightPurple, "");
		codereplace = codereplace.replace(Colors.Navy, "");
		codereplace = codereplace.replace(Colors.Purple, "");
		codereplace = codereplace.replace(Colors.Red, "");
		codereplace = codereplace.replace(Colors.Rose, "");
		codereplace = codereplace.replace(Colors.White, "");
		codereplace = codereplace.replace(Colors.Yellow, "");
		messcheck[0] = codereplace;
		return messcheck;
	}
	
	public void LogTrans(String Action){
		date = new Date();
		if (MySQL){
			Connection conn = getSQLConn();
			PreparedStatement ps = null;
			try{
				ps = conn.prepareStatement("INSERT INTO dConomyLog (Date,Time,Transaction) VALUES(?,?,?)", 1);
				ps.setString(1, dateFormat.format(date));
				ps.setString(2, dateFormatTime.format(date));
				ps.setString(3, Action);
				ps.executeUpdate();
			} catch (SQLException ex) {
				log.severe("[dConomy] - Unable to Log Transaction!");
			}finally{
				try{
					if (ps != null){
						ps.close();
					}
					if (conn != null){
						conn.close();
					}
				}catch (SQLException sqle) {
					log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
		}else{
			String filename= direTrans + String.valueOf(dateFormat.format(date)) + TransLoc;
			File LogFile = new File(filename);
			if (!LogFile.exists()){ 
				try {
					LogFile.createNewFile();
				} catch (IOException e) {
					log.severe("[dConomy] - Unable to create new Log File!");
				} 
			}
			try {
				FileWriter fw = new FileWriter(filename,true);
				fw.write("["+dateFormatTime.format(date)+"] "+Action+ System.getProperty("line.separator"));
				fw.close();  
			} catch (IOException e) {
				log.severe("[dConomy] - Unable to Log Transaction!");
			}
		}
	}
	
	public void iConvertion(){
		String iCoDirLoc = "iConomy/";
		String iCoSettingsLoc = "iConomy/settings.properties";
		String iCoBalancesLoc = "iConomy/balances.properties";
		File iCoDir = new File(iCoDirLoc);
		File iCoSet = new File(iCoSettingsLoc);
		File iCoBalances = new File(iCoBalancesLoc);
		if (iCoDir.exists()){
			if (iCoSet.exists()){
				PropertiesFile iCoSettings = new PropertiesFile(iCoSettingsLoc);
				String iDataBase = iCoSettings.getString("db");
				String iUserName = iCoSettings.getString("user");
				String iPassword = iCoSettings.getString("pass");
				if (iCoSettings.getBoolean("use-mysql")){
					HashMap<String, Integer> iBalances = new HashMap<String, Integer>();
					Connection conn = null;
					PreparedStatement ps = null;
					ResultSet rs = null;
					try {
						conn = DriverManager.getConnection(iDataBase, iUserName, iPassword);
						ps = conn.prepareStatement("SELECT player,balance FROM iBalances ORDER BY player DESC");
						rs = ps.executeQuery();
						while (rs.next()) {
							iBalances.put(rs.getString("player"), Integer.valueOf(rs.getString("balance")));
						}
						} catch (SQLException ex) {
							log.severe("[dConomy] - Unable to Read iBalances from database!");
						} finally {
							try {
								if (ps != null) {
									ps.close();
								}

								if (conn != null) {
									conn.close();
								}
							} catch (SQLException ex) {
								log.severe("[dConomy] - Could not close connection to SQL!");
							}
						}
					for(String name : iBalances.keySet()){
						setInitialBalance(iBalances.get(name), name);
					}
					log.info("[dConomy] - iConomy Balances Converted!");
				}else{
					if(iCoBalances.exists()){
						try {
							BufferedReader reader = new BufferedReader(new FileReader(iCoBalances));
							String line;
							while ((line = reader.readLine()) != null) {
								if(!line.contains("#")){
									String[] account = line.split("=");
									setInitialBalance(Double.valueOf(account[1]), account[0]);
								}
							}
							reader.close();
							log.info("[dConomy] - iConomy Balances Converted!");
						}catch (IOException e){
							log.severe("[dConomy] - Unable to Read iConomy/balances.properties");
							return;
						}
					}
				}
			}
		}
		dCSettings.setBoolean("Convert-iConomy", false);
	}
	
	public Map<String, Double> returnMap(String type) throws Exception {
		String file = getFile(type);
		Map<String, Double> map = new HashMap<String, Double>();
		BufferedReader reader = new BufferedReader(new FileReader(dire+file));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0) {
				continue;
			}
			if (line.charAt(0) == '#') {
				continue;
			}
			int delimPosition = line.indexOf('=');
			String key = line.substring(0, delimPosition).trim();
			double value = Double.valueOf(line.substring(delimPosition + 1).trim());
			map.put(key, value);
		}
		reader.close();
		return map;
	}
}
