
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

public class dCHook {
	PluginInterface dCBalance = new dCHook.dCBalance();
	dCData dCD;
	dCListener dCL;
	dCActionHandler dCAH;
	
	public dCHook(dConomy dCo){
		dCD = dCo.dCD;
		dCL = dCo.dCL;
		dCAH = dCL.dCAH;
	}

	public class dCBalance implements PluginInterface{
		public String getName(){
			return "dCBalance";
		}
		public int getNumParameters() {
			return 4;
		}
		public String checkParameters(Object[] os) {
			if ((os.length < 1) || (os.length > getNumParameters())) {
				return "Invalid amount of parameters.";
			}
			return null;
		}
		public Object run(Object[] os) {
			String cmd = (String)os[0];
			
			/** Gets Money Name
			 * 
			 * @param String cmd
			 *		- "MoneyName"
			 * 
			 * @returns String MoneyName
			 */
			if(cmd.equalsIgnoreCase("MoneyName")){
				return dCD.getMoneyName();
			}
			
			/** Gets Player Account Balance
			 * 
			 * @param String cmd
			 *		- "Account-Balance"
			 * @param String name
			 *		- Player's name
			 * 
			 * @returns Double balance
			 * 		- Player's Balance  -1 if no account balance found
			 */
			if(cmd.equalsIgnoreCase("Account-Balance")){
				double balance = -1;
				if (dCD.keyExists((String)os[1], "Account")){
					balance = dCD.getBalance((String)os[1], "Account");
				}
				return balance;
			}

			/** Get Player Bank Balance
			 * 
			 * @param String cmd
			 *		- "Bank-Balance"
			 * @param String name
			 *		- Player's name
			 * 
			 * @returns Double balance 
			 * 		- Player's Balance  -1 if no account balance found
			 */
			else if (cmd.equalsIgnoreCase("Bank-Balance")){
				double balance = -1;
				if (dCD.keyExists((String)os[1], "Account")){
					balance = dCD.getBalance((String)os[1], "Account");
				}
				return balance;
			}

			/** Deposit Money into Player's Account
			 * 
			 * @param String cmd
			 *		- "Account-Deposit"
			 * @param String name
			 *		- Player's name
			 * @param Double amount
			 *      -amount to be deposited
			 *      
			 * @returns
			 */
			else if (cmd.equalsIgnoreCase("Account-Deposit")){
				double newamount = 0;
				if (dCD.keyExists((String)os[1], "Account")){
					if ((Double)os[2] > 0){
						newamount = dCD.getBalance((String)os[1], "Account") + (Double)os[2];
						if(newamount < 0){ newamount = 0; }
						dCD.setBalance(newamount, (String)os[1], "Account");
					}
				}
			}

			/** Deposit Money into Player's Bank
			 * 
			 * @param String cmd
			 *		- "Bank-Deposit"
			 * @param String name
			 *		- Player's name
			 * @param Double amount
			 *      -amount to be deposited
			 */
			else if (cmd.equalsIgnoreCase("Bank-Deposit")){
				double newamount = 0;
				if (dCD.keyExists((String)os[1], "Bank")){
					if ((Double)os[2] > 0){
						newamount = dCD.getBalance((String)os[1], "Bank") + (Double)os[2];
						if(newamount < 0){ newamount = 0; }
						dCD.setBalance(newamount, (String)os[1], "Bank");
					}
				}
			}
			
			/** Withdraw Money from Player's Account
			 * 
			 * @param String cmd
			 *		- "Account-Withdraw"
			 * @param String name
			 *		- Player's name
			 * @param Double amount
			 *      -amount to be withdrawn
			 */
			else if (cmd.equalsIgnoreCase("Account-Withdraw")){
				double newamount = 0;
				if (dCD.keyExists((String)os[1], "Account")){
					if ((Double)os[2] > 0){
						newamount = dCD.getBalance((String)os[1], "Account") - (Double)os[2];
						if(newamount < 0){ newamount = 0; }
						dCD.setBalance(newamount, (String)os[1], "Account");
					}
				}
			}
			
			/** Withdraw Money from Player's Bank
			 * 
			 * @param String cmd
			 *		- "Bank-Withdraw"
			 * @param String name
			 *		- Player's name
			 * @param Double amount
			 *      -amount to be deposited
			 */
			else if (cmd.equalsIgnoreCase("Bank-Withdraw")){
				double newamount = 0;
				if (dCD.keyExists((String)os[1], "Bank")){
					if ((Double)os[2] > 0){
						newamount = dCD.getBalance((String)os[1], "Bank") - (Double)os[2];
						if(newamount < 0){ newamount = 0; }
						dCD.setBalance(newamount, (String)os[1], "Bank");
					}
				}
			}
			
			/**Check Joint Account Balance - UNCHECKED
			 *    - Will not check owner/user permissions
			 *    
			 * @param String cmd
			 *		- "Joint-Balance"
			 * @param String name
			 *		- Account name
			 * @return Double balance
			 *      - Joint Account Balance  -1 if account doesn't exist
			 */
			else if (cmd.equalsIgnoreCase("Joint-Balance-NC")){
				double balance = -1;
				if (dCD.JointkeyExists((String)os[1])){
					balance = dCD.getJointBalance((String)os[1]);
				}
				return balance;
			}
			
			/**Check Joint Account Balance
			 *      -Will check permissions
			 * 
			 * @param String cmd
			 *		- "Joint-Balance"
			 * @param String name
			 *		- Player's name
			 * @param String accname
			 *      - Account Name
			 * @return Double balance
			 *      - Joint Account Balance  
			 *     	   -Negative 1 (-1) if account doesn't exist 
			 *         -Negative 2 (-2) if player doesn't have permission
			 */
			else if (cmd.equalsIgnoreCase("Joint-Balance")){
				double balance = -1;
				if (dCD.JointkeyExists((String)os[2])){
					if (proceedUser((String)os[1], (String)os[2])){
						balance = dCD.getJointBalance((String)os[2]);
					}else{
						balance = -2;
					}
				}
				return balance;
			}
			
			/** Deposit Money into Joint Account - UNCHECKED
			 * 		- Will not check owner/user permissions
			 * 
			 * @param String cmd
			 *		- "Joint-Deposit"
			 * @param String name
			 *		- Account name
			 * @param Double amount
			 *      -amount to be withdrawn
			 */
			else if (cmd.equalsIgnoreCase("Joint-Deposit-NC")){
				double newamount = 0;
				if (dCD.JointkeyExists((String)os[1])){
					if ((Double)os[2] > 0){
						newamount = dCD.getJointBalance((String)os[1]) + (Double)os[2];
						if(newamount < 0){ newamount = 0; }
						dCD.setJointBalance(newamount, (String)os[1]);
					}
				}
			}
			
			/** Deposit Money into Joint Account
			 * 		- Will check owner/user permissions
			 * 
			 * @param String cmd
			 *		- "Joint-Deposit"
			 * @param String name
			 *		- Account name
			 * @param Double amount
			 *      -amount to be deposited
			 */
			else if (cmd.equalsIgnoreCase("Joint-Deposit")){
				double newamount = 0;
				if (dCD.JointkeyExists((String)os[3])){
					if ((Double)os[2] > 0){
						if (proceedUser((String)os[1], (String)os[2])){
							newamount = dCD.getJointBalance((String)os[1]) + (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setJointBalance(newamount, (String)os[3]);
						}
					}
				}
			}
			
			/** Withdraw Money from Joint Account UNCHECKED
			 * 
			 * @param String cmd
			 *		- "Joint-Withdraw"
			 * @param String name
			 *		- Account name
			 * @param Double amount
			 *      -Amount to be withdrawn
			 */
			else if (cmd.equalsIgnoreCase("Joint-Withdraw-NC")){
				double newamount = 0;
				if (dCD.JointkeyExists((String)os[1])){
					if ((Double)os[2] > 0){
						newamount = dCD.getJointBalance((String)os[1]) - (Double)os[2];
						dCD.setJointBalance(newamount, (String)os[1]);
					}
				}
			}
			
			/** Withdraw Money from Joint Account CHECKED
			 * 
			 * @param String cmd
			 *		- "Joint-Withdraw"
			 * @param String name
			 *		- Player name
			 * @param Double amount
			 *      -Amount to be withdrawn
			 * @param String AccountName
			 * 		-Account Name
			 */
			else if (cmd.equalsIgnoreCase("Joint-Withdraw")){
				double newamount = 0;
				if (dCD.JointkeyExists((String)os[3])){
					if ((Double)os[2] > 0){
						if (proceedUser((String)os[1], (String)os[2])){
							newamount = dCD.getJointBalance((String)os[1]) - (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setJointBalance(newamount, (String)os[3]);
						}
					}
				}
			}
			
			/** Give Money to a Player
			 *     - Will do all the PayForwarding Checks
			 * 
			 * @param String cmd
			 *		- "Player-Pay"
			 * @param String name
			 *		- Player Name
			 * @param Double amount
			 *      -Amount to Pay
			 */
			else if (cmd.equalsIgnoreCase("Player-Pay")){
				double newamount = 0;
				if ((Double)os[2] > 0){
					if(dCD.getPayForwardCheck((String)os[1])){
						String account = dCD.getPayForwardAccount((String)os[1]);
						if (!account.equalsIgnoreCase("Bank")){
							if(dCD.JointkeyExists(account)){
								if (proceedUser((String)os[1], account)){
									newamount = dCD.getJointBalance(account) + (Double)os[2];
									if(newamount < 0){ newamount = 0; }
									dCD.setJointBalance(newamount, account);
								}else{
									if (dCD.keyExists((String)os[1], "Account")){
										newamount = dCD.getBalance((String)os[1], "Account") + (Double)os[2];
										if(newamount < 0){ newamount = 0; }
										dCD.setBalance(newamount, (String)os[1], "Account");
									}
								}
							}	
						}else{
							newamount = dCD.getBalance((String)os[1], "Bank") + (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setBalance(newamount, (String)os[1], "Bank");
						}
					}else{
						if (dCD.keyExists((String)os[1], "Account")){
							newamount = dCD.getBalance((String)os[1], "Account") + (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setBalance(newamount, (String)os[1], "Account");
						}
					}
				}
			}
			
			/** Get Money from a Player
			 *     - Will do all the PayForwarding Checks (expect withdraw limits)
			 * 
			 * @param String cmd
			 *		- "Player-Pay"
			 * @param String name
			 *		- Player Name
			 * @param Double amount
			 *      -Amount to Pay
			 */
			else if (cmd.equalsIgnoreCase("Player-Charge")){
				double newamount = 0;
				if ((Double)os[2] > 0){
					if(dCD.getPayForwardCheck((String)os[1])){
						String account = dCD.getPayForwardAccount((String)os[1]);
						if (!account.equalsIgnoreCase("Bank")){
							if(dCD.JointkeyExists(account)){
								if (proceedUser((String)os[1], account)){
									newamount = dCD.getJointBalance(account) - (Double)os[2];
									if(newamount < 0){ newamount = 0; }
									dCD.setJointBalance(newamount, account);
								}else{
									if (dCD.keyExists((String)os[1], "Account")){
										newamount = dCD.getBalance((String)os[1], "Account") - (Double)os[2];
										if(newamount < 0){ newamount = 0; }
										dCD.setBalance(newamount, (String)os[1], "Account");
									}
								}
							}	
						}else{
							newamount = dCD.getBalance((String)os[1], "Bank") - (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setBalance(newamount, (String)os[1], "Bank");
						}
					}else{
						if (dCD.keyExists((String)os[1], "Account")){
							newamount = dCD.getBalance((String)os[1], "Account") - (Double)os[2];
							if(newamount < 0){ newamount = 0; }
							dCD.setBalance(newamount, (String)os[1], "Account");
						}
					}
				}
			}
			
			/** Get Balance of Forwarded Account from a Player
			 *     - Will do all the PayForwarding Checks (expect withdraw limits)
			 * 
			 * @param String cmd
			 *		- "Player-Balance"
			 * @param String name
			 *		- Player Name
			 */
			else if (cmd.equals("Player-Balance")){
				double balance = -1;
				if(dCD.getPayForwardCheck((String)os[1])){
					String account = dCD.getPayForwardAccount((String)os[1]);
					if (!account.equals("Bank")){
						if(dCD.JointkeyExists(account)){
							if (proceedUser((String)os[1], account)){
								balance = dCD.getJointBalance(account);
							}else{
								if (dCD.keyExists((String)os[1], "Bank")){
									balance = dCD.getBalance((String)os[1], "Bank");
								}
							}
						}	
					}else{
						balance = dCD.getBalance((String)os[1], "Bank");
					}
				}else{
					if (dCD.keyExists((String)os[1], "Account")){
						balance = dCD.getBalance((String)os[1], "Account");
					}
				}
				return balance;
			}
			
			/** Logs other Plugin Transactions
			 * 
			 * @param String cmd
			 *		- "Log"
			 * @param String Log Message
			 */
			
			else if (cmd.equalsIgnoreCase("Log")){
				dCD.LogTrans((String)os[1]);
			}
			
			return null;
		}
		
		/** Used to check Joint Account User Permissions
		 * 
		 * @param String player
		 *		- Player's Name
		 * @param String player
		 *		- Player's name
		 *
		 * @returns boolean
		 *      -True if player has permission to JointAccount
		 */
		public boolean proceedUser(String player, String jointaccountname){
			if (jointaccountname != null){
				if(dCD.JointkeyExists(jointaccountname)){
					Player p = etc.getServer().getPlayer(player);
					if (p != null){
						if (p.isAdmin()){
							return true;
						}else{
							if (dCAH.JointAccountOwnerCheck(player, jointaccountname)){
								return true;
							}else{
								if (dCAH.JointAccountUserCheck(player, jointaccountname)){
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
	}
}
