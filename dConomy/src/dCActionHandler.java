import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;


/**
* dCActionHandler v1.x
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

public class dCActionHandler {
	dCData dCD;
	Server server;
	String A = "Account";
	String B = "Bank";
	String S = "SERVER";
	
	public dCActionHandler(){
		server = etc.getServer();
		dCD = dConomy.dCD;
	}
	
	public void PlayerMessage(String playernom, String message){
		String Message1 = message;
		String[] decolor = dCD.decolormessage(message);
		String Message2 = null;
		String sims = "§";
		Character sim = sims.charAt(0);
		if (decolor[0].length() > 60){
			int sub1 = Integer.valueOf(decolor[1])+60;
			Message1 = message.substring(0, sub1+1);
			if(Message1.charAt(sub1) == sim){
				Message2 = "§"+message.substring(sub1+1);
			}else{
				int c = Message1.lastIndexOf("§");
				Character color = Message1.charAt(c+1);
				Message2 = "§"+color+message.substring(sub1+1);
			}
		}
		Player player = server.matchPlayer(playernom);
		if(player != null){
			if(player.isConnected()){
				player.sendMessage(Message1);
				if(Message2 != null){
					player.sendMessage(Message2);
				}
			}
		}
	}
	
	//Send Balance Message
	public boolean DisplayBalance(String player, int type, String jointname, boolean admin){
		String other = NameMatcher(jointname);
		switch (type){
		case 1:
			PlayerMessage(player, dCD.getMessage(201, "", "", dCD.getBalance(player, A)));
			return true;
		case 2:
			PlayerMessage(player, dCD.getMessage(202, "", "", dCD.getBalance(player, B)));
			return true;
		case 3:
			if(!dCD.JointkeyExists(jointname)){ return Error(110, player, jointname);}
			if (!admin){
				if (!JointAccountOwnerCheck(player, jointname)){
					if (!JointAccountUserCheck(player, jointname)){ 
						return Error(107, player, jointname);
					}
				}
			}
			PlayerMessage(player, dCD.getMessage(301, player, jointname, dCD.getJointBalance(jointname)));
			return true;
		case 4:
			if(dCD.aoc && !admin){ return Error(104, player, jointname); }
			if (other != null){
				if (!dCD.keyExists(other, A)){
					return Error(109, player, jointname); 
				}else{
					jointname = other;
				}
			}else{
				if (!dCD.keyExists(jointname, A)){
					return Error(109, player, jointname); 
				}
			}
			PlayerMessage(player, dCD.getMessage(222, jointname, "", dCD.getBalance(jointname, A)));
			return true;
		case 5:
			if(dCD.aoc && !admin){ return Error(104, player, ""); }
			if (other != null){
				if (!dCD.keyExists(other, A)){
					return Error(109, player, jointname); 
				}else{
					jointname = other;
				}
			}else{
				if (!dCD.keyExists(jointname, A)){
					return Error(109, player, jointname); 
				}
			}
			PlayerMessage(player, dCD.getMessage(223, jointname, "", dCD.getBalance(jointname, B)));
			return true;
		default: return false;
		}
	}
	
	public boolean BankDeposit(String player, String amount){
		double deposit = 0;
		try{
			deposit = Double.parseDouble(amount);
		}catch(NumberFormatException nfe){
			return Error(103, player, "");
		}
		if (deposit < 0){ return Error(126, player, ""); }
		double newAcc = dCD.getBalance(player, A) - deposit;
		if (newAcc < 0){ return Error(101, player, ""); }
		double newBank = dCD.getBalance(player, B) + deposit;
		dCD.setBalance(newBank, player, B);
		dCD.setBalance(newAcc, player, A);
		PlayerMessage(player, dCD.getMessage(204, "", "", deposit));
		PlayerMessage(player, dCD.getMessage(206, "", "", newAcc));
		PlayerMessage(player, dCD.getMessage(205, "", "", newBank));
		dCD.Logging(602, player, "", amount, "");
		return true;
	}
	
	public boolean BankWithdraw(String player, String amount){
		double withdraw = 0;
		try{
			withdraw = Double.parseDouble(amount);
		}catch(NumberFormatException nfe){
			return Error(103, player, "");
		}
		if (withdraw < 0){ return Error(126, player, ""); }
		double newBank = dCD.getBalance(player, B) - withdraw;
		if (newBank < 0){ return Error(103, player, ""); }
		double newAcc = dCD.getBalance(player, A) + withdraw;
		dCD.setBalance(newBank, player, B);
		dCD.setBalance(newAcc, player, A);
		PlayerMessage(player, dCD.getMessage(203, "", "", withdraw));
		PlayerMessage(player, dCD.getMessage(206, "", "", newAcc));
		PlayerMessage(player, dCD.getMessage(205, "", "", newBank));
		dCD.Logging(603, player, "", amount, "");
		return true;
	}
	
	public boolean JointWithdraw(String player, String name, String amount, boolean admin){
		if(!dCD.JointkeyExists(name)){ return Error(110, player, name);}
		boolean owner = false;
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				if (!JointAccountUserCheck(player, name)){
					return Error(107, player, name);
				}
			}else{
				owner = true;
			}
		}
		double withdraw = 0;
		try{
			withdraw = Double.parseDouble(amount);
		}catch(NumberFormatException nfe){
			return Error(103, player, "");
		}
		if (withdraw < 0){ return Error(126, player, name); }
		if (((!owner)&&(!admin)) && (withdraw > dCD.getJointUserWithdrawMax(name))){ return Error(117, player, ""); }
		if (dCD.JointUserWithDrawDelayCheck(name+":"+player)){ return Error(116, player, ""); }
		double newJo = dCD.getJointBalance(name) - withdraw;
		if (newJo < 0){ return Error(105, player, name);}
		double newAcc = dCD.getBalance(player, A) + withdraw;
		dCD.setJointBalance(newJo, name);
		dCD.setBalance(newAcc, player, A);
		PlayerMessage(player, dCD.getMessage(303, "", name, withdraw));
		PlayerMessage(player, dCD.getMessage(206, "", "", newAcc));
		PlayerMessage(player, dCD.getMessage(302, "", name, newJo));
		if ((!owner)&&(!admin)){
			dCD.JointUserWithdrawDelayAdd(name+":"+player);
		}
		dCD.Logging(602, player, "", amount, "");
		return true;
	}
	
	public boolean JointDeposit(String player, String name, String amount, boolean admin){
		if(!dCD.JointkeyExists(name)){ return Error(110, player, name);}
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				if (!JointAccountUserCheck(player, name)){
					return Error(107, player, name);
				}
			}
		}
		double deposit = 0;
		try{
			deposit = Double.parseDouble(amount);
		}catch(NumberFormatException nfe){
			return Error(103, player, "");
		}
		if (deposit < 0){ return Error(126, player, "");}
		double newAcc = dCD.getBalance(player, A) - deposit;
		if (newAcc < 0){ return Error(101, player, "");}
		double newJo = dCD.getJointBalance(name) + deposit;
		dCD.setJointBalance(newJo, name);
		dCD.setBalance(newAcc, player, A);
		PlayerMessage(player, dCD.getMessage(304, "", name, deposit));
		PlayerMessage(player, dCD.getMessage(201, player, "", newAcc));
		PlayerMessage(player, dCD.getMessage(302, player, name, newJo));
		dCD.Logging(605, player, "", amount, name);
		return true;
	}
	
	public boolean PayPlayer(String sender, String receiver, String amount, boolean admin){
		double change = 0;
		String other = NameMatcher(receiver);
		if (other != null){
			if (!dCD.keyExists(other, A)){
				return Error(109, sender, receiver); 
			}else{
				receiver = other;
			}
		}else{
			if (!dCD.keyExists(receiver, A)){
				return Error(109, sender, receiver); 
			}
		}
		try{
			change = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			return Error(103, sender, "");
		}
		if (change < 0){ return Error(126, sender, ""); }
		if (sender.equals(receiver)){ return Error(118, sender, ""); }
		if (dCD.getPayForwardCheck(sender)){
			if (dCD.getPayForwardCheck(receiver)){
				return PayForward(sender, receiver, dCD.getPayForwardAccount(sender), dCD.getPayForwardAccount(receiver), change, admin);
			}else{
				return PayForward(sender, receiver, dCD.getPayForwardAccount(sender), A, change, admin);
			}
		}else if (dCD.getPayForwardCheck(receiver)){
			return PayForward(sender, receiver, A, dCD.getPayForwardAccount(receiver), change, admin);
		}
		double balanceSender = dCD.getBalance(sender, A);
		double balanceReceiver = dCD.getBalance(receiver, A);
		double deduct = balanceSender - change;
		if (deduct < 0){ return Error(101, sender, "");}
		double deposit = balanceReceiver + change;
		dCD.setBalance(deduct, sender, A);
		dCD.setBalance(deposit, receiver, A);
		PlayerMessage(sender, dCD.getMessage(207, receiver, "", change));
		PlayerMessage(sender, dCD.getMessage(206, sender, "", deduct));
		PlayerMessage(receiver, dCD.getMessage(208, sender, "", change));
		PlayerMessage(receiver, dCD.getMessage(206, receiver, "", deposit));
		dCD.Logging(601, sender, receiver, amount, "");
		return true;
	}
	
	public boolean CreateJointAccount(String player, String name){
		if(name.length() > 32){
			if (player.equals(S)){
				return ConsoleError(115, name);
			}else{
				return Error(115, player, name);
			}
		}
		if(dCD.JointkeyExists(name)){ 
			if (player.equals(S)){
				return ConsoleError(111, name);
			}else{
				return Error(111, player, name);
			}
		}
		if (player.equals(S)){
			dCD.createJointAccount(name, "");
			String[] decolor = dCD.decolormessage(dCD.getMessage(305, "", name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(611, player, "", "", name);
		}else{
			dCD.createJointAccount(name, player);
			PlayerMessage(player, dCD.getMessage(305, player, name, 0));
			dCD.Logging(611, player, "", "", name);
		}
		return true;
	}
	
	public boolean DeleteJointAccount(String player, String name, boolean admin){
		if(!dCD.JointkeyExists(name)){
			if (player.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, player, name);
			}
		}
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				return Error (107, player, name);
			}
		}
		double JoBal = dCD.getJointBalance(name);
		String Owns = dCD.getJointOwners(name);
		Owns = Owns + dCD.getJointUsers(name);
		DistributeMoney(Owns, JoBal);
		dCD.deleteJointAccount(name);
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(308, "", name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(612, player, "", "", name);
		}else{
			PlayerMessage(player, dCD.getMessage(308, player, name, 0));
			dCD.Logging(612, player, "", "", name);
		}
		return true;
	}
	
	public boolean AddJointUser(String player, String user, String name, boolean admin){
		if(!dCD.JointkeyExists(name)){
			if (player.equals(S)){
				return ConsoleError(110, "");
			}else{
				return Error(110, player, name);
			}
		}
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				return Error (107, player, name);
			}
		}
		String other = NameMatcher(user);
		if (other != null){
			user = other;
		}else{
			if (!dCD.keyExists(user, A)){
				if (player.equals(S)){
					return ConsoleError(109, user);
				}else{
					return Error(109, player, user);
				}
			}
		}
		if (JointAccountOwnerCheck(user, name)){
			if(player.equals(S)){
				return ConsoleError(122, user);
			}else{
				return Error(122, player, user);
			}
		}
		String users = dCD.getJointUsers(name);
		StringBuffer n = new StringBuffer();
		if(users != null){
			String[] usersplit = users.split(",");
			for (int i = 0; i < usersplit.length; i++){
				if (usersplit[i].equals(user)){
					if (player.equals(S)){
						return ConsoleError(119, user);
					}else{
						return Error(119, player, user);
					}
				}else{
					n.append(usersplit[i]+",");
				}
			}
		}
		n.append(user);
		String newusers = n.toString();
		dCD.updateJointUsers(name, newusers);
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(309, user, name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(615, player, user, "", name);
		}else{
			PlayerMessage(player, dCD.getMessage(309, user, name, 0));
			dCD.Logging(615, player, user, "", name);
		}
		return true;
	}
	
	public boolean AddJointOwner(String player, String own, String name, boolean admin){
		if(!dCD.JointkeyExists(name)){ return Error(110, player, name); }
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				return Error (107, player, name);
			}
		}
		String other = NameMatcher(own);
		if (other != null){
			own = other;
		}else{
			if (!dCD.keyExists(own, A)){
				if(player.equals(S)){
					return ConsoleError(109, "");
				}else{
					return Error(109, player, own);
				}
			}
		}
		if (JointAccountOwnerCheck(own, name)){
			if (player.equals(S)){
				return ConsoleError(122, own);
			}else{
				return Error(122, player, own);
			}
		}
		String owners = dCD.getJointOwners(name);
		String[] ownsplit = owners.split(",");
		StringBuffer n = new StringBuffer();
		for (int i = 0; i < ownsplit.length; i++){
			if (ownsplit[i].equals(own)){
				if (player.equals(S)){
					return ConsoleError(122, own);
				}else{
					return Error(122, player, own);
				}
			}else{
				n.append(ownsplit[i]+",");
			}
		}
		n.append(own);
		dCD.updateJointOwners(name, n.toString());
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(306, own, name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(613, player, own, "", name);
		}else{
			PlayerMessage(player, dCD.getMessage(306, own, name, 0));
			dCD.Logging(613, player, own, "", name);
		}
		return true;
	}
	
	public boolean RemoveJointOwner(String player, String name, String own, boolean admin){
		if(!dCD.JointkeyExists(name)){
			if (player.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, player, name);
			}
		}
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				return Error (107, player, name);
			}
		}
		String other = NameMatcher(own);
		if (other != null){
			own = other;
		}else{
			if (!dCD.keyExists(own, A)){
				if (player.equals(S)){
					return ConsoleError(109, own);
				}else{
					return Error(109, player, own);
				}
			}
		}
		if (!JointAccountOwnerCheck(player, name)){
			if(player.equals(S)){
				return ConsoleError(125, own);
			}else{
				return Error(125, player, own);
			}
		}
		String owners = dCD.getJointOwners(name);
		String[] ownsplit = owners.split(",");
		StringBuffer n = new StringBuffer();
		if(owners != null){
			for (int i = 0; i < ownsplit.length; i++){
				if (!ownsplit[i].equals(own)){
					n.append(ownsplit[i]+",");
				}
			}
		}
		dCD.updateJointOwners(name, n.toString());
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(307, own, name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(616, player, own, "", name);
		}else{
			PlayerMessage(player, dCD.getMessage(307, own, name, 0));
			dCD.Logging(616, player, own, "", name);
		}
		return true;
	}
	
	public boolean RemoveJointUser(String player, String name, String user, boolean admin){
		if(!dCD.JointkeyExists(name)){ return Error(110, player, name);}
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				return Error (107, player, name);
			}
		}
		String other = NameMatcher(user);
		if (other != null){
			user = other;
		}else{
			if (!dCD.keyExists(user, A)){
				if (player.equals(S)){
					return ConsoleError(109, user);
				}else{
					return Error(109, player, user);
				}
			}
		}
		if (!JointAccountUserCheck(user, name)){
			if(player.equals(S)){
				return ConsoleError(124, user);
			}else{
				return Error(124, player, user);
			}
		}
		String users = dCD.getJointUsers(name);
		StringBuffer n = new StringBuffer();
		if(users != null){
			String[] usersplit = users.split(",");
			for (int i = 0; i < usersplit.length; i++){
				if (!usersplit[i].equals(user)){
					n.append(usersplit[i]+",");
				}
			}
		}
		dCD.updateJointUsers(name, n.toString());
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(310, user, name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(617, player, user, "", name);
		}else{
			PlayerMessage(player, dCD.getMessage(310, user, name, 0));
			dCD.Logging(617, player, user, "", name);
		}
		return true;
	}
	
	public boolean PayPlayerWithJoint(String sender, String name, String receiver, String amount, boolean admin){
		double pay = 0;
		try{
			pay = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			return Error(103, sender, "");
		}
		if (pay < 0){ return Error(126, sender, ""); }
		String other = NameMatcher(receiver);
		if (other != null){
			if (!dCD.keyExists(other, A)){
				return Error(109, sender, receiver); 
			}else{
				receiver = other;
			}
		}else{
			if (!dCD.keyExists(receiver, A)){
				return Error(109, sender, receiver); 
			}
		}
		if (sender.equals(receiver)){ return Error(118, sender, ""); }
		if (!dCD.JointkeyExists(name)){return Error(110, sender, name);}
		if (!admin){
			if (!JointAccountOwnerCheck(sender, name)){
				if (!JointAccountUserCheck(sender, name)){
					return Error(107, sender, name);
				}
			}else{
				admin = true;
			}
		}
		if ((!admin) && (pay > dCD.getJointUserWithdrawMax(name))){ return Error(117, sender, ""); }
		if (dCD.JointUserWithDrawDelayCheck(name+":"+sender)){ return Error(116, sender, ""); }
		if (dCD.getPayForwardCheck(receiver)){ return PayForwardingJoint(receiver, sender, name, pay, false, admin); }
		double balanceJoint = dCD.getJointBalance(name);
		double balancepayee = dCD.getBalance(receiver, A);
		double deduct = balanceJoint - pay;
		if (deduct < 0){ return Error(101, sender, "");}
		double deposit = balancepayee + pay;
		dCD.setJointBalance(deduct, name);
		dCD.setBalance(deposit, receiver, A);
		PlayerMessage(sender, dCD.getMessage(212, receiver, name, pay));
		PlayerMessage(sender, dCD.getMessage(302, sender, name, deduct));
		PlayerMessage(receiver, dCD.getMessage(208, sender, "", pay));
		PlayerMessage(receiver, dCD.getMessage(206, receiver, "", deposit));
		dCD.Logging(607, sender, receiver, amount, name);
		return true;
	}
	
	public boolean PayJointAccount(String sender, String name, String amount, boolean admin){
		double pay = 0;
		try{
			pay = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			return Error(103, sender, "");
		}
		if (pay < 0){ return Error(126, sender, ""); }
		if (!dCD.JointkeyExists(name)){return Error(110, sender, name);}
		if (dCD.getPayForwardCheck(sender)){ return PayForwardingJoint(sender, "", name, pay, true, admin); }
		double balanceJoint = dCD.getJointBalance(name);
		double balancepayer = dCD.getBalance(sender, A);
		double deduct = balancepayer - pay;
		if (deduct < 0){ return Error(101, sender, "");}
		double deposit = balanceJoint + pay;
		dCD.setJointBalance(deposit, name);
		dCD.setBalance(deduct, sender, A);
		PlayerMessage(sender, dCD.getMessage(217, sender, name, pay));
		PlayerMessage(sender, dCD.getMessage(206, sender, "", deposit));
		dCD.Logging(606, sender, "", amount, name);
		return true;
	}
	
	public String NameMatcher(String partName){
		Player player = etc.getServer().matchPlayer(partName);
		if (player != null){
			return player.getName();
		}
		return null;
	}

	public boolean PayForward(String sender, String receiver, String senderaccount, String receiveraccount, double amount, boolean admin){
		String payto = receiveraccount;
		String payfrom = senderaccount;
		String[] checkto = PayForwarding(receiver, payto);
		double balanceReceiver = Double.valueOf(checkto[0]);
		payto = checkto[1];
		String[] checkfrom = PayForwarding(sender, payfrom);
		double balanceSender = Double.valueOf(checkfrom[0]);
		payfrom = checkfrom[1];
		double deduct = balanceSender - amount;
		if (deduct < 0){ return Error(101, sender, "");}
		if (!(payfrom.equals(A) || payfrom.equals(B))){
			if ((!admin) && (deduct > dCD.getJointUserWithdrawMax(payfrom))){ return Error(117, sender, ""); }
			if (dCD.JointUserWithDrawDelayCheck(senderaccount+":"+sender)){ return Error(116, sender, ""); }
		}
		double deposit = balanceReceiver + amount;
		PayForwardBalanceChange(sender, payfrom, deduct);
		PayForwardBalanceChange(receiver, payto, deposit);
		if (!payfrom.equals(A)){
			if (!payfrom.equals(B)){
				PlayerMessage(sender, dCD.getMessage(212, "", payfrom, 0));
				PlayerMessage(sender, dCD.getMessage(207, receiver, "", amount));
				PlayerMessage(sender, dCD.getMessage(302, "", payfrom, deduct));
				dCD.Logging(610, sender, receiver, String.valueOf(amount), payfrom);
			}else{
				PlayerMessage(sender, dCD.getMessage(214, "", "", amount));
				PlayerMessage(sender, dCD.getMessage(207, receiver, "", amount));
				PlayerMessage(sender, dCD.getMessage(205, "", "", deduct));
				dCD.Logging(609, sender, receiver, String.valueOf(amount), "");
			}
		}else{
			PlayerMessage(sender, dCD.getMessage(207, receiver, "", amount));
			PlayerMessage(sender, dCD.getMessage(206, "", "", deduct));
			dCD.Logging(601, sender, receiver, String.valueOf(amount), "");
		}
		if (!payto.equals(A)){
			if (!payfrom.equals(B)){
				PlayerMessage(receiver, dCD.getMessage(208, sender, "", amount));
				PlayerMessage(receiver, dCD.getMessage(211, "", payto, 0));
				PlayerMessage(receiver, dCD.getMessage(302, "", payto, deposit));
				dCD.Logging(631, sender, receiver, String.valueOf(amount), payto);
			}else{
				PlayerMessage(receiver, dCD.getMessage(208, sender, "", amount));
				PlayerMessage(receiver, dCD.getMessage(213, "", "", 0));
				PlayerMessage(receiver, dCD.getMessage(205, "", "", deposit));
				dCD.Logging(608, sender, receiver, String.valueOf(amount), payto);
			}
		}else{
			PlayerMessage(receiver, dCD.getMessage(208, sender, "", amount));
			PlayerMessage(receiver, dCD.getMessage(206, "", "", deposit));
		}
		return true;
	}
	
	public boolean PayForwardingJoint(String player, String p2, String name, double amount, boolean tojoint, boolean admin){
		String pacc = dCD.getPayForwardAccount(player);
		String[] checkto = PayForwarding(player, pacc);
		double jbalance = dCD.getJointBalance(name);
		double pbalance = Double.valueOf(checkto[0]);
		pacc = checkto[1];
		if(tojoint){ //Pay Joint Account
			double pdeduct = pbalance - amount;
			if (pdeduct < 0){
				if (!pacc.equals(A)){
					if (!pacc.equals(B)){
						return Error(105, player, "");
					}else{
						return Error(102, player, "");
					}
				}else{
					return Error(101, player, "");
				}
			}
			if ((!(pacc.equals(A) || pacc.equals(B))) && (!admin) && (pdeduct > dCD.getJointUserWithdrawMax(name))){ return Error(117, player, ""); }
			if (dCD.JointUserWithDrawDelayCheck(name+":"+player)){ return Error(116, player, ""); }
			double jdeposit = jbalance + amount;
			PayForwardBalanceChange(player, pacc, pdeduct);
			PayForwardBalanceChange("", name, jdeposit);
			if (!pacc.equals(A)){
				if (!pacc.equals(B)){
					PlayerMessage(player, dCD.getMessage(212, "", pacc, 0));
					PlayerMessage(player, dCD.getMessage(217, "", name, amount));
					PlayerMessage(player, dCD.getMessage(302, "", name, pdeduct));
					dCD.Logging(632, player, pacc, String.valueOf(amount), name);
				}else{
					PlayerMessage(player, dCD.getMessage(214, "", "", amount));
					PlayerMessage(player, dCD.getMessage(217, "", name, amount));
					PlayerMessage(player, dCD.getMessage(205, "", "", pdeduct));
					dCD.Logging(633, player, "", String.valueOf(amount), name);
				}
			}else{
				PlayerMessage(player, dCD.getMessage(217, "", name, amount));
				PlayerMessage(player, dCD.getMessage(205, "", "", pdeduct));
				dCD.Logging(606, player, "", String.valueOf(amount), name);
			}
		}else{ //Pay Player With Joint
			double pdeposit = pbalance + amount;
			double jdeduct = jbalance - amount;
			if(jdeduct < 0){
				if (!pacc.equals(A)){
					if (!pacc.equals(B)){
						return Error(105, player, "");
					}else{
						return Error(102, player, "");
					}
				}else{
					return Error(101, player, "");
				}
			}
			PayForwardBalanceChange(player, pacc, pdeposit);
			PayForwardBalanceChange("", name, jdeduct);
			PlayerMessage(player, dCD.getMessage(212, p2, pacc, 0));
			PlayerMessage(p2, dCD.getMessage(208, player, "", 0));
			dCD.Logging(607, player, p2, String.valueOf(amount), name);
		}
		return false;
	}
	
	public boolean PayForwardingSet(String player, String name){
		if(name.equalsIgnoreCase(B)){
			dCD.setPayForward(player, true);
			dCD.setPayForwardAcc(player, B);
			PlayerMessage(player, dCD.getMessage(220, player, B, 0));
			dCD.Logging(635, player, "", "", "");
		}else if (name.equalsIgnoreCase(A)){
			dCD.setPayForward(player, false);
			PlayerMessage(player, dCD.getMessage(221, player, "", 0));
			dCD.Logging(636, player, "", "", "");
		}else{
			if (dCD.JointkeyExists(name)){
				if (JointAccountOwnerCheck(player, name)){
					dCD.setPayForward(player, true);
					dCD.setPayForwardAcc(player, name);
					PlayerMessage(player, dCD.getMessage(219, player, name, 0));				
				}else if (JointAccountUserCheck(player, name)){
					dCD.setPayForward(player, true);
					dCD.setPayForwardAcc(player, name);
					PlayerMessage(player, dCD.getMessage(219, player, name, 0));
					dCD.Logging(634, player, "", "", name);
				}else{
					return Error(107, player, name);
				}
			}else{
				return Error(110, player, name);
			}
		}
		return true;
	}
	
	public String[] PayForwarding(String player, String name){
		String[] check = new String[3];
		double balance = 0;
		if (!name.equals(A)){
			if (!name.equals(B)){
				if (dCD.JointkeyExists(name)){
					if(JointAccountOwnerCheck(player, name)){
						balance = dCD.getBalance(player, name);
						check[1] = name;
					}else if (JointAccountUserCheck(player, name)){
						balance = dCD.getBalance(player, name);
						check[1] = name;
					}else{
						PlayerMessage(player, dCD.getErrorMessage(107, name));
						PlayerMessage(player, dCD.getMessage(218, player, "", 0));
						dCD.setPayForward(player, false);
						balance = dCD.getBalance(player, A);
						check[1] = A;
					}
				}
			}else{
				balance = dCD.getBalance(player, B);
				check[1] = B;
			}
		}else{
			balance = dCD.getBalance(player, A);
			check[1] = A;
		}
		check[0] = String.valueOf(balance);
		return check;
	}
	
	public void PayForwardBalanceChange(String player, String name, double amount){
		if (name.equals(A)){
			dCD.setBalance(amount, player, A);
		}else if (name.equals(B)){
			dCD.setBalance(amount, player, B);
		}else{
			dCD.setJointBalance(amount, name);
		}
	}
	
	public boolean JointAccountOwnerCheck(String player, String name){
		String owners = dCD.getJointOwners(name);
		String[] ownsplit = owners.split(",");
		for (int i = 0; i < ownsplit.length; i++){
			if (ownsplit[i].equals(player)){
				return true;
			}
		}
		return false;
	}
	
	public boolean JointAccountUserCheck(String player, String name){
		String users = dCD.getJointUsers(name);
		String[] usersplit = users.split(",");
		for (int i = 0; i < usersplit.length; i++){
			if (usersplit[i].equals(player)){
				return true;
			}
		}
		return false;
	}
	
	public void DistributeMoney(String owners, double balance){
		String[] ownsplit = owners.split(",");
		double distribute = balance / ownsplit.length;
		for (int i = 0; i < ownsplit.length; i++){
			double newbal = dCD.getBalance(ownsplit[i], "bank") + distribute;
			dCD.setBalance(newbal, ownsplit[i], "bank");
		}
	}
	
	public boolean ResetPlayer(String admin, String player, String type){
		String other = NameMatcher(player);
		if (other != null){
			if (!dCD.keyExists(other, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}else{
				player = other;
			}
		}else{
			if (!dCD.keyExists(player, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, "");
				}
			}
		}
		dCD.setBalance(dCD.getStartingBalance(), player, type);
		if (type.equals(A)){
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(404, player, "", 0));
				dCD.log.info(decolor[0]);
				dCD.Logging(618, admin, player, "", "");
			}else{
				PlayerMessage(admin, dCD.getMessage(404, player, "", 0));
				dCD.Logging(618, admin, player, "", "");
			}
		}else{
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(408, player, "", 0));
				dCD.log.info(decolor[0]);
				dCD.Logging(622, admin, player, "", "");
			}else{
				PlayerMessage(admin, dCD.getMessage(408, player, "", 0));
				dCD.Logging(622, admin, player, "", "");
			}
		}
		return true;
	}
	
	public boolean SetPlayerBalance(String admin, String player, String type, String amount){
		String other = NameMatcher(player);
		if (other != null){
			if (!dCD.keyExists(other, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}else{
				player = other;
			}
		}else{
			if (!dCD.keyExists(player, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}
		}
		double balance = 0;
		try{
			balance = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(103, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(103, admin, type);
			}
		}
		if (balance < 0){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(126, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(126, admin, ""); 
			}
		}
		dCD.setBalance(balance, player, type);
		if (type.equals(A)){
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(405, player, "", balance));
				dCD.log.info(decolor[0]);
				dCD.Logging(619, admin, player, "", "");
			}else{
				PlayerMessage(admin, dCD.getMessage(405, player, "", balance));
				dCD.Logging(619, admin, player, "", "");
			}
		}else{
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(409, player, "", balance));
				dCD.log.info(decolor[0]);
				dCD.Logging(623, admin, player, "", "");
			}else{
				PlayerMessage(admin, dCD.getMessage(409, player, "", balance));
				dCD.Logging(623, admin, player, "", "");
			}
		}
		return true;
	}
	
	public boolean RemovePlayerBalance(String admin, String player, String type, String amount){
		String other = NameMatcher(player);
		if (other != null){
			if (!dCD.keyExists(other, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}else{
				player = other;
			}
		}else{
			if (!dCD.keyExists(player, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}
		}
		double deduction = 0;
		try{
			deduction = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(103, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(103, admin, ""); 
			}
		}
		if (deduction < 0){ 
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(126, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(126, admin, ""); 
			}
		}
		double newbal = dCD.getBalance(player, type) - deduction;
		if (newbal < 0){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(127, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(127, admin, ""); 
			}
		}
		dCD.setBalance(newbal, player, type);
		if (type.equals(A)){
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(406, player, "", deduction));
				dCD.log.info(decolor[0]);
				dCD.Logging(621, admin, player, amount, "");
			}else{
				PlayerMessage(admin, dCD.getMessage(406, player, "", deduction));
				dCD.Logging(621, admin, player, amount, "");
			}
		}else{
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(410, player, "", deduction));
				dCD.log.info(decolor[0]);
				dCD.Logging(625, admin, player, amount, "");
			}else{
				PlayerMessage(admin, dCD.getMessage(410, player, "", deduction));
				dCD.Logging(625, admin, player, amount, "");
			}
		}
		return true;
	}
	
	public boolean AddPlayerBalance(String admin, String player, String type, String amount){
		String other = NameMatcher(player);
		if (other != null){
			if (!dCD.keyExists(other, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}else{
				player = other;
			}
		}else{
			if (!dCD.keyExists(player, type)){
				if(admin.equals(S)){
					return ConsoleError(109, player);
				}else{
					return Error(109, admin, player);
				}
			}
		}
		double deposit = 0;
		try{
			deposit = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(103, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(103, admin, ""); 
			}
		}
		if (deposit < 0){
			if (admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getErrorMessage(126, ""));
				dCD.log.info(decolor[0]);
			}else{
				return Error(126, admin, ""); 
			}
		}
		double newbal = dCD.getBalance(player, type) + deposit;
		dCD.setBalance(newbal, player, type);
		if (type.equals(A)){
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(407, player, "", deposit));
				dCD.log.info(decolor[0]);
				dCD.Logging(620, admin, player, amount, "");
			}else{
				PlayerMessage(admin, dCD.getMessage(407, player, "", deposit));
				dCD.Logging(620, admin, player, amount, "");
			}
		}else{
			if(admin.equals(S)){
				String[] decolor = dCD.decolormessage(dCD.getMessage(411, player, "", deposit));
				dCD.log.info(decolor[0]);
				dCD.Logging(624, admin, player, amount, "");
			}else{
				PlayerMessage(admin, dCD.getMessage(411, player, "", deposit));
				dCD.Logging(624, admin, player, amount, "");
			}
		}
		return true;
	}
	
	public boolean resetJointBalance(String admin, String name){
		if (!dCD.JointkeyExists(name)){
			if(admin.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, admin, name);
			}
		}
		dCD.setJointBalance(0, name);
		if(admin.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(412, "", name, 0));
			dCD.log.info(decolor[0]);
			dCD.Logging(626, admin, "", "", name);
		}else{
			PlayerMessage(admin, dCD.getMessage(412, "", name, 0));
			dCD.Logging(626, admin, "", "", name);
		}
		return true;
	}
	
	public boolean setJointBalance(String admin, String name, String amount){
		double balance = 0;
		try{
			balance = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if(admin.equals(S)){
				return ConsoleError(103, "");
			}else{
				return Error(103, admin, "");
			}
		}
		if (balance < 0){ 
			if(admin.equals(S)){
				return ConsoleError(126, "");
			}else{
				return Error(126, admin, "");
			}
		}
		if (!dCD.JointkeyExists(name)){
			if(admin.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, admin, name);
			}
		}
		dCD.setJointBalance(balance, name);
		if(admin.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(413, "", name, balance));
			dCD.log.info(decolor[0]);
			dCD.Logging(615, admin, "", amount, name);
		}else{
			PlayerMessage(admin, dCD.getMessage(413, "", name, balance));
			dCD.Logging(627, admin, "", amount, name);
		}
		return true;
	}
	
	public boolean addJointBalance(String admin, String name, String amount){
		double deposit = 0;
		try{
			deposit = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if(admin.equals(S)){
				return ConsoleError(103, "");
			}else{
				return Error(103, admin, "");
			}
		}
		if (deposit < 0){
			if(admin.equals(S)){
				return ConsoleError(126, "");
			}else{
				return Error(126, admin, "");
			}
		}
		if (!dCD.JointkeyExists(name)){
			if(admin.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, admin, name);
			}
		}
		double newbal = dCD.getJointBalance(name) + deposit;
		dCD.setJointBalance(newbal, name);
		if(admin.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(414, "", name, deposit));
			dCD.log.info(decolor[0]);
			dCD.Logging(628, admin, "", amount, name);
		}else{
			PlayerMessage(admin, dCD.getMessage(414, "", name, deposit));
			dCD.Logging(628, admin, "", amount, name);
		}
		return true;
	}
	
	public boolean removeJointBalance(String admin, String name, String amount){
		double deduct = 0;
		try{
			deduct = Double.parseDouble(amount);
		}catch (NumberFormatException nfe){
			if(admin.equals(S)){
				return ConsoleError(103, "");
			}else{
				return Error(103, admin, "");
			}
		}
		if (deduct < 0){
			if(admin.equals(S)){
				return ConsoleError(126, "");
			}else{
				return Error(126, admin, "");
			}
		}
		if (!dCD.JointkeyExists(name)){
			if(admin.equals(S)){
				return ConsoleError(110, name);
			}else{
				return Error(110, admin, name);
			}
		}
		double newbal = dCD.getJointBalance(name) - deduct;
		if (newbal < 0){
			if(admin.equals(S)){
				return ConsoleError(127, "");
			}else{
				return Error(127, admin, "");
			}
		}
		dCD.setJointBalance(newbal, name);
		if (admin.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(415, "", name, deduct));
			dCD.log.info(decolor[0]);
			dCD.Logging(623, admin, "", amount, name);
		}else{
			PlayerMessage(admin, dCD.getMessage(415, "", name, deduct));
			dCD.Logging(623, admin, "", amount, name);
		}
		return true;
	}
	
	public boolean CheckPayForwarding(String player){
		boolean active = dCD.getPayForwardCheck(player);
		String accfor = dCD.getPayForwardAccount(player);
		if (active){
			if (accfor != null){
				if (accfor.equals(B)){
					PlayerMessage(player, dCD.getMessage(220, "", accfor, 0));
				}else{
					PlayerMessage(player, dCD.getMessage(219, "", accfor, 0));
				}
			}else{
				PlayerMessage(player, dCD.getMessage(218, "", "", 0));
				dCD.setPayForward(player, false);
			}
		}else{
			PlayerMessage(player, dCD.getMessage(218, "", "", 0));
		}
		return true;
	}
	
	public boolean checkJointUserMaxWithdraw(String player, String name, boolean admin){
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				if (!JointAccountUserCheck(player, name)){
					return Error(107, player, name);
				}
			}
		}
		double max = dCD.getJointUserWithdrawMax(name);
		if(player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(311, "", name, max));
			dCD.log.info(decolor[0]);
		}else{
			PlayerMessage(player, dCD.getMessage(311, "", name, max));
		}
		return true;
	}
	
	public boolean setJointUserMaxWithdraw(String player, String name, String Amount, boolean admin){
		if (!admin){
			if (!JointAccountOwnerCheck(player, name)){
				if (!JointAccountUserCheck(player, name)){
					return Error(107, player, name);
				}
			}
		}
		double newamount = 0;
		try{
			newamount = Double.parseDouble(Amount);
		}catch (NumberFormatException nfe){
			if(player.equals(S)){
				return ConsoleError(103, "");
			}else{
				return Error(103, player, "");
			}
		}
		if (newamount < 0){ return Error(127, player, ""); }
		dCD.updateJointMaxUserWithdraw(name, newamount);
		if (player.equals(S)){
			String[] decolor = dCD.decolormessage(dCD.getMessage(312, "", name, newamount));
			dCD.log.info(decolor[0]);
			dCD.Logging(637, player, "", Amount, name);
		}else{
			PlayerMessage(player, dCD.getMessage(312, "", name, newamount));
			dCD.Logging(637, player, "", Amount, name);
		}
		return true;
	}
	
	public boolean MoneyHelp(String player, boolean rank, boolean admin, boolean autodep, boolean bank, boolean joint){
		PlayerMessage(player, dCD.getHelpMessage(501));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(503));
		PlayerMessage(player, dCD.getHelpMessage(504));
		if (rank || admin){
			PlayerMessage(player, dCD.getHelpMessage(505));
			PlayerMessage(player, dCD.getHelpMessage(506));
		}
		if (autodep || admin){
			PlayerMessage(player, dCD.getHelpMessage(511));
			PlayerMessage(player, dCD.getHelpMessage(512));
		}
		if (bank || admin){
			PlayerMessage(player, dCD.getHelpMessage(513));
		}
		if (joint || admin){
			PlayerMessage(player, dCD.getHelpMessage(514));
		}
		if (admin){
			PlayerMessage(player, dCD.getHelpMessage(541));
		}
		return true;
	}
	
	public boolean BankHelp(String player, boolean admin){
		PlayerMessage(player, dCD.getHelpMessage(515));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(516));
		PlayerMessage(player, dCD.getHelpMessage(517));
		PlayerMessage(player, dCD.getHelpMessage(518));
		if (admin){
			PlayerMessage(player, dCD.getHelpMessage(542));
		}
		return true;
	}
	
	public boolean JointHelp(String player, boolean admin, boolean create){
		PlayerMessage(player, dCD.getHelpMessage(523));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(524));
		PlayerMessage(player, dCD.getHelpMessage(525));
		PlayerMessage(player, dCD.getHelpMessage(526));
		PlayerMessage(player, dCD.getHelpMessage(527));
		PlayerMessage(player, dCD.getHelpMessage(530));
		PlayerMessage(player, dCD.getHelpMessage(531));
		PlayerMessage(player, dCD.getHelpMessage(532));
		PlayerMessage(player, dCD.getHelpMessage(533));
		PlayerMessage(player, dCD.getHelpMessage(534));
		if (create){
			PlayerMessage(player, dCD.getHelpMessage(528));
			PlayerMessage(player, dCD.getHelpMessage(529));
		}
		if (admin){
			PlayerMessage(player, dCD.getHelpMessage(542));
		}
		return true;
	}
	
	public boolean MoneyAdminHelp(String player){
		PlayerMessage(player, dCD.getHelpMessage(544));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(507));
		PlayerMessage(player, dCD.getHelpMessage(508));
		PlayerMessage(player, dCD.getHelpMessage(509));
		PlayerMessage(player, dCD.getHelpMessage(510));
		return true;
	}
	
	public boolean BankAdminHelp(String player){
		PlayerMessage(player, dCD.getHelpMessage(545));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(519));
		PlayerMessage(player, dCD.getHelpMessage(520));
		PlayerMessage(player, dCD.getHelpMessage(521));
		PlayerMessage(player, dCD.getHelpMessage(522));
		return true;
	}
	
	public boolean JointAdminHelp(String player){
		PlayerMessage(player, dCD.getHelpMessage(546));
		PlayerMessage(player, dCD.getHelpMessage(502));
		PlayerMessage(player, dCD.getHelpMessage(540));
		PlayerMessage(player, dCD.getHelpMessage(535));
		PlayerMessage(player, dCD.getHelpMessage(536));
		PlayerMessage(player, dCD.getHelpMessage(537));
		PlayerMessage(player, dCD.getHelpMessage(538));
		return true;
	}
	
	public boolean ConsoleMoneyHelp(){
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(501)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(502)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(540)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(507)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(508)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(509)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(510)));
		return true;
	}
	
	public boolean ConsoleBankHelp(){
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(515)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(502)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(540)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(519)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(520)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(521)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(522)));
		return true;
	}
	
	public boolean ConsoleJointHelp(){
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(523)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(502)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(540)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(535)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(536)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(537)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(538)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(528)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(529)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(530)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(531)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(532)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(533)));
		dCD.log.info(ConsoleDeColor(dCD.getHelpMessage(534)));
		return true;
	}
	
	public String ConsoleDeColor(String message){
		String[] decolorize = dCD.decolormessage(message);
		message = decolorize[0];
		return message;
	}
	
	public boolean Error(int errorcode, String playernom, String name){
		Player player = server.matchPlayer(playernom);
		if (player != null){
			if (player.isConnected()){
				switch (errorcode){
				case 101:
					player.sendMessage(dCD.getErrorMessage(101, name));
					return true;
				case 102:
					player.sendMessage(dCD.getErrorMessage(102, name));
					return true;
				case 103:
					player.sendMessage(dCD.getErrorMessage(103, name));
					return true;
				case 104:
					player.sendMessage(dCD.getErrorMessage(104, name));
					return true;
				case 105:
					player.sendMessage(dCD.getErrorMessage(105, name));
					return true;
				case 106:
					player.sendMessage(dCD.getErrorMessage(106, name));
					return true;
				case 107:
					player.sendMessage(dCD.getErrorMessage(107, name));
					return true;
				case 108:
					player.sendMessage(dCD.getErrorMessage(108, name));
					return true;
				case 109:
					player.sendMessage(dCD.getErrorMessage(109, name));
					return true;
				case 110:
					player.sendMessage(dCD.getErrorMessage(110, name));
					return true;
				case 111:
					player.sendMessage(dCD.getErrorMessage(111, name));
					return true;
				case 112:
					player.sendMessage(dCD.getErrorMessage(112, name));
					return true;
				case 113:
					player.sendMessage(dCD.getErrorMessage(113, name));
					return true;
				case 114:
					player.sendMessage(dCD.getErrorMessage(114, name));
					return true;
				case 115:
					player.sendMessage(dCD.getErrorMessage(115, name));
					return true;
				case 116:
					player.sendMessage(dCD.getErrorMessage(116, name));
					return true;
				case 117:
					player.sendMessage(dCD.getErrorMessage(117, name));
					return true;
				case 118:
					player.sendMessage(dCD.getErrorMessage(118, name));
					return true;
				case 119:
					player.sendMessage(dCD.getErrorMessage(119, name));
					return true;
				case 120:
					player.sendMessage(dCD.getErrorMessage(120, name));
					return true;
				case 121:
					player.sendMessage(dCD.getErrorMessage(121, name));
					return true;
				case 122:
					player.sendMessage(dCD.getErrorMessage(122, name));
					return true;
				case 123:
					player.sendMessage(dCD.getErrorMessage(123, name));
					return true;
				case 124:
					player.sendMessage(dCD.getErrorMessage(124, name));
					return true;
				case 125:
					player.sendMessage(dCD.getErrorMessage(125, name));
					return true;
				case 126:
					player.sendMessage(dCD.getErrorMessage(126, name));
					return true;
				case 127:
					player.sendMessage(dCD.getErrorMessage(127, name));
					return true;
				default: 
					player.sendMessage("Unknown Error Code:"+errorcode);
					return true;
				}
			}
		}
		return true;
	}
	
	public boolean ConsoleError(int errorcode, String name){
		String[] decolored;
		switch (errorcode){
		case 101:
			decolored = dCD.decolormessage(dCD.getErrorMessage(101, name));
			dCD.log.info(decolored[0]);
			return true;
		case 102:
			decolored = dCD.decolormessage(dCD.getErrorMessage(102, name));
			dCD.log.info(decolored[0]);
			return true;
		case 103:
			decolored = dCD.decolormessage(dCD.getErrorMessage(103, name));
			dCD.log.info(decolored[0]);
			return true;
		case 104:
			decolored = dCD.decolormessage(dCD.getErrorMessage(104, name));
			dCD.log.info(decolored[0]);
			return true;
		case 105:
			decolored = dCD.decolormessage(dCD.getErrorMessage(105, name));
			dCD.log.info(decolored[0]);
			return true;
		case 106:
			decolored = dCD.decolormessage(dCD.getErrorMessage(106, name));
			dCD.log.info(decolored[0]);
			return true;
		case 107:
			decolored = dCD.decolormessage(dCD.getErrorMessage(107, name));
			dCD.log.info(decolored[0]);
			return true;
		case 108:
			decolored = dCD.decolormessage(dCD.getErrorMessage(108, name));
			dCD.log.info(decolored[0]);
			return true;
		case 109:
			decolored = dCD.decolormessage(dCD.getErrorMessage(109, name));
			dCD.log.info(decolored[0]);
			return true;
		case 110:
			decolored = dCD.decolormessage(dCD.getErrorMessage(110, name));
			dCD.log.info(decolored[0]);
			return true;
		case 111:
			decolored = dCD.decolormessage(dCD.getErrorMessage(111, name));
			dCD.log.info(decolored[0]);
			return true;
		case 112:
			decolored = dCD.decolormessage(dCD.getErrorMessage(112, name));
			dCD.log.info(decolored[0]);
			return true;
		case 113:
			decolored = dCD.decolormessage(dCD.getErrorMessage(113, name));
			dCD.log.info(decolored[0]);
			return true;
		case 114:
			decolored = dCD.decolormessage(dCD.getErrorMessage(114, name));
			dCD.log.info(decolored[0]);
			return true;
		case 115:
			decolored = dCD.decolormessage(dCD.getErrorMessage(115, name));
			dCD.log.info(decolored[0]);
			return true;
		case 116:
			decolored = dCD.decolormessage(dCD.getErrorMessage(116, name));
			dCD.log.info(decolored[0]);
			return true;
		case 117:
			decolored = dCD.decolormessage(dCD.getErrorMessage(117, name));
			dCD.log.info(decolored[0]);
			return true;
		case 118:
			decolored = dCD.decolormessage(dCD.getErrorMessage(118, name));
			dCD.log.info(decolored[0]);
			return true;
		case 119:
			decolored = dCD.decolormessage(dCD.getErrorMessage(119, name));
			dCD.log.info(decolored[0]);
			return true;
		case 120:
			decolored = dCD.decolormessage(dCD.getErrorMessage(120, name));
			dCD.log.info(decolored[0]);
			return true;
		case 121:
			decolored = dCD.decolormessage(dCD.getErrorMessage(121, name));
			dCD.log.info(decolored[0]);
			return true;
		case 122:
			decolored = dCD.decolormessage(dCD.getErrorMessage(122, name));
			dCD.log.info(decolored[0]);
			return true;
		case 123:
			decolored = dCD.decolormessage(dCD.getErrorMessage(123, name));
			dCD.log.info(decolored[0]);
			return true;
		case 124:
			decolored = dCD.decolormessage(dCD.getErrorMessage(124, name));
			dCD.log.info(decolored[0]);
			return true;
		case 125:
			decolored = dCD.decolormessage(dCD.getErrorMessage(125, name));
			dCD.log.info(decolored[0]);
			return true;
		case 126:
			decolored = dCD.decolormessage(dCD.getErrorMessage(126, name));
			dCD.log.info(decolored[0]);
			return true;
		case 127:
			decolored = dCD.decolormessage(dCD.getErrorMessage(127, name));
			dCD.log.info(decolored[0]);
			return true;
		default: 
			dCD.log.info("Unknown Error Code:"+errorcode);
			return true;
		}
	}
	
	/**
	* Ranking code derived from iConomy / iListen.java & MySQL.java
	* 
	* iConomy v1.x
	* Copyright (C) 2010 Nijikokun <nijikokun@gmail.com>
	*
	* This program is free software: you can redistribute it and/or modify
	* it under the terms of the GNU General Public License as published by
	* the Free Software Foundation, either version 2 of the License, or
	* (at your option) any later version.
	*
	* This program is distributed in the hope that it will be useful,
	* but WITHOUT ANY WARRANTY; without even the implied warranty of
	* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	* GNU General Public License for more details.
	*
	* You should have received a copy of the GNU General Public License
	* along with this program. If not, see <http://www.gnu.org/licenses/>.
	*/
	public boolean RankTop(String player, String num){
		int amount = 0;
		try{
			amount = Integer.parseInt(num);
		}catch (NumberFormatException nfe){
			amount = 5;
		}
		if (amount > 20){ amount = 20; }
		int i = 1;
		if (dCD.MySQL) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = dCD.getSQLConn();
				ps = conn.prepareStatement("SELECT Player,Account FROM dConomy ORDER BY Account DESC LIMIT 0,?");
				ps.setInt(1, amount);
				rs = ps.executeQuery();

				PlayerMessage(player, dCD.getMessage(401, "", String.valueOf(amount), 0));

				if (rs != null) {
					while (rs.next()) {
						PlayerMessage(player, dCD.getMessage(402, rs.getString("Player"), String.valueOf(i), Double.valueOf(rs.getString("Account"))));
						i++;
					}
				} else {
					PlayerMessage(player, dCD.getMessage(403, player, "", 0));
				}
			} catch (SQLException ex) {
				dCD.log.severe("[dConomy] - Unable to grab the top players from the database!");
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
					if (conn != null) {
						conn.close();
					}
				}catch (SQLException ex) {
					dCD.log.severe("[dConomy] - Could not close connection to SQL");
				}
			}
			dCD.Logging(614, player, "", String.valueOf(amount), "");
			return true;
		} else {
			Map<String, Double> accounts;
			TreeMap<String, Double> sortedAccounts = null;
			dCValueComparator bvc = null;

			try {
				accounts = dCD.returnMap("account");
				bvc = new dCValueComparator(accounts);
				sortedAccounts = new TreeMap<String, Double>(bvc);
				sortedAccounts.putAll(accounts);
				
			} catch (Exception ex) {
				dCD.log.severe("[dConomy] - Unable to retrieve array of balances!");
			}
			
			PlayerMessage(player, dCD.getMessage(401, "", String.valueOf(amount), 0));
			
			if (sortedAccounts.size() < 1) {
				PlayerMessage(player, dCD.getMessage(403, player, "", 0));
				return true;
			}

			if (amount > sortedAccounts.size()) {
				amount = sortedAccounts.size();
			}

			for (String key : sortedAccounts.keySet()) {
				String name = key;
				double balance = sortedAccounts.get(name);

				if (i <= amount) {
					PlayerMessage(player, dCD.getMessage(402, name, String.valueOf(i), balance));
				} else {
					break;
				}

				i++;
			}
		}
		dCD.Logging(614, player, "", String.valueOf(amount), "");
		return true;
	}
	
	public boolean RankPlayer(String player, String Other, boolean self){
		int i = 1;
		if (!self){
			String p2 = NameMatcher(Other);
			if (p2 != null){
				if (!dCD.keyExists(p2, A)){
					return Error(109, player, Other); 
				}else{
					Other = p2;
				}
			}else{
				if (!dCD.keyExists(player, A)){
					return Error(109, player, Other); 
				}
			}
		}
		if (dCD.MySQL) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = dCD.getSQLConn();
				ps = conn.prepareStatement("SELECT Player,Account FROM dConomy ORDER BY Account DESC");
				rs = ps.executeQuery();

				while (rs.next()) {
					if (self) {
						if (rs.getString("Player").equalsIgnoreCase(player)) {
							PlayerMessage(player, dCD.getMessage(209, player, String.valueOf(i), 0));
							dCD.Logging(638, player, "", "", "");
							break;
						}
					} else {
						if (rs.getString("player").equalsIgnoreCase(Other)) {
							PlayerMessage(player, dCD.getMessage(210, Other, String.valueOf(i), 0));
							dCD.Logging(638, player, Other, "", "");
							break;
						}
					}
					i++;
				}
				} catch (SQLException ex) {
					dCD.log.severe("[dConomy] - Unable to grab the ranking from database!");
				} finally {
					try {
						if (ps != null) {
							ps.close();
						}

						if (conn != null) {
							conn.close();
						}
					} catch (SQLException ex) {
						dCD.log.severe("[dConomy] - Could not close connection to SQL!");
					}
				}
			return true;
		} else {
			Map<String, Double> accounts;
			TreeMap<String, Double> sortedAccounts = null;
			dCValueComparator bvc = null;

			try {
				accounts = dCD.returnMap("account");
				bvc = new dCValueComparator(accounts);
				sortedAccounts = new TreeMap<String, Double>(bvc);
				sortedAccounts.putAll(accounts);
				
			} catch (Exception ex) {
				dCD.log.severe("[dConomy] - Unable to retrieve array of balances!");
			}
			
			for (Object key : sortedAccounts.keySet()) {
    			String name = (String) key;
    			if (self) {
    				if (name.equalsIgnoreCase(player)) {
    					PlayerMessage(player, dCD.getMessage(209, player, String.valueOf(i), 0));
    					dCD.Logging(638, player, "", "", "");
    					break;
    				}
    			} else {
    				if (name.equalsIgnoreCase(Other)) {
    					PlayerMessage(player, dCD.getMessage(210, Other, String.valueOf(i), 0));
    					dCD.Logging(638, player, Other, "", "");
    					break;
    				}
    			}

    			i++;
    		}
    	}
		return true;
    }
}
