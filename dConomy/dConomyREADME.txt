dConomy - because you need to be bloody rich too
Copyrighted (c) 2011 Visual Illusions Entertainment
Authored by: darkdiplomat

Features:
	Joint/Shared Accounts
	Bank Accounts with interest gaining
	Ranking (Player Accounts Only)
	Pay Forwarding (use other accounts to pay and receive pay)
	Customizable Messages

Permissions:
	/money - use /money
	/bank - use /bank
	/joint - use Shared Accounts "/joint"
	/dcrank - use /money rank & /money top
	/dccreate - create Shared/Joint Accounts
	/dcauto - use Pay Forwarding
	/dcadmin - access to all commands

Commands:
	<REQUIRED>  [Optional] (Alias)
	<p> = Player <a> = Amount <acc> = Account
	
	/money - Basic Command/Display account balance
		help (?) - displays /money commands
		pay (-p) <p> <a> - Pay Player Amount
		rank (-r) [p] - Display Rank Position (requires "/dcrank" or "/dcadmin" permission)
		top (-t) [a] - Display Richest Players (requires "/dcrank" or "/dcadmin" permission)
		auto (-au) - checks autodep account settings (requires "/dcauto" permission)
		setauto (-sa) <acc|OFF> - Changes or sets autodep account (requires "/dcauto" or "/dcadmin" permission)
		help (?) - Displays Help
		
	/money help admin (? admin) - displays admin commands (requires "/dcadmin" permission)
		set (-s) <p> <a> - Set player's account to amount (requires "/dcadmin" permission)
		reset (-rt) <p> - Reset players account to default (requires "/dcadmin" permission)
		add (-a) <p> <a> - Add amount to player's account (requires "/dcadmin" permission)
		remove (-rm) <p> <a> - Remove amount from player's account (requires "/dcadmin" permission)
		
	
	/bank - Basic Command/Display bank balance
		withdraw (-w) <a> - withdraw from bank account
		deposit <yellow>(-d) <rose><a><green>: deposit into bank account";
		help (?) - displays /bank commands
		
	/bank help admin (? admin) - display admin commands (requires "/dcadmin" permission)
		reset <yellow>(-rt) <p><green> - reset player's bank to 0 (requires "/dcadmin" permission)
		set (-s) <p> <amount> - set player's bank to amount (requires "/dcadmin" permission)
		add <yellow>(-a) <p><green> - add amount to player's bank (requires "/dcadmin" permission)
		remove <yellow>(-rm) <rose><p> <a><green>: remove amount from player's bank (requires "/dcadmin" permission)
	
	/joint - Base Command
		help (?) - display commands
		<account> - displays account balance
			withdraw (-w) <a> - withdraw from Account
			deposit (-d) <a> - deposit into Account
			pay (-p) <p> - pay Account
			create (-c) - create Account
			delete (-del) - deposit into Account
			addowner (-ao) <p> - add owner to Account
			removeowner (-ro) <p> - add owner to Account
			adduser (-au) <p> - add owner to Account
			removeuser (-ru) <p> - remove owner from Account
			setusermax (-su) <a> - set UserMaxWithdraw per set period
			usermax (-um) <a> - checks UserMaxWithdraw per set period
			
	/joint help admin (? admin) - display admin commands
		<account>
			reset (-rt) <a> - reset Account to 0
			set (-s) <a> - set Account to amount
			add (-a) <a> - add amount to Account
			remove (-rm) <a> - remove amount to Account
			
	NOTE: ALL joint commands (except help) need an account specified as /joint <account>  ie: /joint <account> withdraw <amount>
	
Joint/Shared Accounts:
	Shared account will allow players to use the same account for thing but if a player is not an owner they will be limited on how much they 
	can withdraw at one time and how often they can withdraw
	Joint Account names cannot be longer than 32 Characters
	
	Owners/Admin Exempt
	
Configuration Files:
	plugins/config/dConomy
		dCSettings.ini - Basic Settings
			Starting-Balance = New Account Starting Balance value in 0.00 format (if set to less than 0 will default to 0)
			Money-Name = Name to call your currency
			Use-MySQL = Set to true to use MySQL
			Use-Canary-MySQLConn = Set to true to use Canary's MySQL Connection (MySQL needs to be true)
			LogPayments = Set to true to log transactions
			Bank-InterestRate = Bank Interest Percentage (0.02 = 0.02%) (if set to 0 or less will default to 0.02)
			Bank-InterestPayDelay = Bank Interest Pay Delay (in minutes) - Set to 0 to disable
			JointUserWithdrawDelay = Joint Account Users Withdraw delay(in minutes) - Set to 0 to disable
			JointUserMaxWithdrawAmount = Joint Account User Max Withdraw Default
			AOCAPB = Admin Only Check Another Players Balance - Set to false to allow all
			Convert-iConomy = Set to true to convert iCo Balances to dCo Balances (reverts to false after convert runs)
			
		dCMySQL.ini - If Using MySQL and not Canary's MySQL Connection
			UserName = Database User Name
			Password = Database Password
			DataBase = Database URL
			
		/Messages
			Should be pretty self explainatory as all settings are listed by function
				Color Codes
					<black> = Black	(§0)
					<blue> = Blue (§3)
					<darkpurple> = Dark Purple (§9)
					<gold> = Gold (§6)
					<gray> = Gray (§8)
					<green> = Green (§2)
					<lightblue> = LightBlue (§b)
					<lightgray> = Light Grey (§7)
					<red> = Red (§4)
					<lightgreen> = Light Green (§a)
					<lightpurple> = Light Purple (§d)
					<navy> = Navy (§1)
					<purple> = Purple (§5)
					<rose> = Rose (§c)
					<white> = White (§f)
					<yellow> = Yellow (§e)
					
				Other Tags
					<p> = Player
					<a> = Amount
					<m> = Money Name
					<acc> = Account Name
					<rank> = Rank #
					<min> = Minutes (For Joint Account User Withdraw)
					<xmin> = Minutes Until (For Joint Account User Withdraw)
					
				Logging Tags
					<p1> = Player preforming Action
					<p2> = Player receiving
					<acc> = Account Name
					<a> = Amount

iConomy to dConomy Conversion:
	Enable dConomy to allow it to make the settings files
	in dCSettings set iConomy-Convert to true
	Reload dConomy
	Conversion process will happen
	Note: If iConomy was using MySQL it will use iConomy's MySQL settings from the iConomy Settings file to find the iBalances Table
	
Logging Transactions:
	FlatFile: Log files will be named by date in dd-MM-YYYY format and inside actions are prefixed with the Time (will make a new file each day
	MySQL: They will be stored in the dConomy-Log table as Date|Time|Action

MySQL Things:
	There are 3 tables for dConomy which should be created automaticly when MySQL is turned on and configured properly
		dConomy - Player Account and Bank balances
		dConomy-Joint - Joint Account settings/balances
		dConomy-Log - Transaction logs (if enabled)
	
FlatFile Things:
	Joint Accounts are stored in the JointAccounts folder and files are named by Account Name
	Player Account are stored in the main dConomy folder under dCAccounts.txt
	Player Bank Accounts are stored in the main dConomy folder under dCBank.txt
	dCTimerReser.DONOTEDIT - Timer resets for Bank Interest and Joint Account Withdraw Delay  DO NOT EDIT THIS UNLESS YOU KNOW WHAT YOU ARE DOING!
	
API:
	Get Money Name
		(String) etc.getLoader().callCustomHook("dCBalance", new Object[] { "MoneyName" };
		
	Get Player's Account Balance - Returns -1 if player doesn't have an account
		(Double) etc.getLoader().callCustomHook("dCBalance", new Object[] { "Account-Balance", PlayerName};
		
	Get Player's Bank Balnce - Returns -1 if player doesn't have a bank account
		(Double) etc.getLoader().callCustomHook("dCBalance", new Object[] { "Bank-Balance", PlayerName };
		
	Deposit Money into Player's Account - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Account-Deposit", PlayerName, (Double)Amount };
		
	Deposit Money into Player's Bank - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Bank-Deposit", PlayerName, (Double) Amount };
		
	Withdraw Money From Player's Account - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Account-Withdraw", PlayerName, (Double)Amount };
		
	Withdraw Money From Player's Bank - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Bank-Withdraw", PlayerName, (Double) Amount };
		
	Get Joint Account Balance (Ownership/User Not Checked) Returns -1 if Account Not Found - 
		(Double)etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Balance-NC", AccountName };
		
	Get Joint Account Balance (Ownership/User Checked) Returns -1 if Account Not Found and -2 if Player doesn't have permission to Account - 
		(Double)etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Balance", PlayerName, AccountName };
		
	Deposit Money into Joint Account (Ownership/User Not Checked) - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Deposit-NC", AccountName, (Double)Amount };
		
	Deposit Money into Joint Account (Ownership/User Checked) - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Deposit", PlayerName, (Double) Amount, AccountName };
		
	Withdraw Money from Joint Account (Ownership/User Not Checked) - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Withdraw-NC", AccountName, (Double)Amount };
		
	Deposit Money into Joint Account (Ownership/User Checked) - 
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Joint-Withdraw", PlayerName, (Double) Amount, AccountName };
		
	Pay money to Player (Checks For Pay Forwarding) [RECOMMENDED TO USE]
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Player-Pay", PlayerName, (Double) Amount};
	
	Charge money from Player (Checks For Pay Forwarding) [RECOMMENDED TO USE]
		etc.getLoader().callCustomHook("dCBalance", new Object[] { "Player-Charge", PlayerName, (Double) Amount};

	Check Player Balance (Checks For Pay Forwarding) [RECOMMENDED TO USE]
		(Double)etc.getLoader().callCustomHook("dCBalance", new Object[] { "Player-Balance", PlayerName};

Other Things:
	PayForwarding is all stored in the PayForwarding.txt regardless if using FlatFile or MySQL (just easier to do it that way)
	JUWD.txt in the JointAccounts Folder - used to store if a player has already made a withdraw during the set time period (FlatFile and MySQL)
	
Legal Stuffs:	
	The Ranking System in dConomy is originally from iConomy 1.x with minor edits for use in dConomy
	iConomy 1.x and the iConomy Source Code is released under the GNU General Public License v2 and is Copyrighted (C) 2010 Nijikokun
	
	dConomy is free software released under the GNU General Public License v3 (Full license included in .jar file under License.txt)
	Source will be availible on github.com and is included in the .jar file.
	
Special Thanks:
	Nijikokun - For making iConomy and releasing as Open Source/Free Software
	damagefilter - For making the sweet dConomy logo

Download:
dConomy http://bit.ly/uuWSrm
dropbox backup - http://bit.ly/tHDxmO
source (not yet availible outside of the jar file)