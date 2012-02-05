import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

public class dCTimer{
	dCData dCD;
	int bdelay, jdelay;
	double binterest;
	Timer banktime;
	Timer JWDtime;
	Map<String, Double> accounts;
	
	public dCTimer(dCData dCD){
		this.dCD = dCD;
	}
	
	public void SetUpBT(int delay, double interest, long timerrest){
		bdelay = delay*60*1000;
		binterest = interest;
		banktime = new Timer();
		banktime.schedule(new dCTimerExpire(), timerrest);
	}
	
	public void SetUpJWDT(int delay, long timerrest){
		JWDtime = new Timer();
		jdelay = delay*60*1000;
		JWDtime.schedule(new dCJWDTimerExpire(), timerrest);
	}
	
	public void cancel(){
		banktime.cancel();
		JWDtime.cancel();
	}

	public class dCTimerExpire extends TimerTask {
		public void run(){
			accounts = new HashMap<String, Double>();
			try {
				accounts = dCD.returnMap("Bank");
			} catch (Exception e) {
				dCD.log.severe("[dConomy] - Unable to retrieve array of bank balances!");
			}
			for (String acc : accounts.keySet()){
				double balance = accounts.get(acc);
				double newbalance = balance + (balance*binterest);
				dCD.setBalance(newbalance, acc, "Bank");
			}
			dCD.log.info("[dConomy] - Bank Interest Paid!");
			dCD.SetReset("BankTimerResetTo", System.currentTimeMillis()+bdelay);
			if (etc.getLoader().getPlugin("dConomy").isEnabled()){
				banktime.schedule(new dCTimerExpire(), bdelay);
			}
		}
	}

	public class dCJWDTimerExpire extends TimerTask {
		public void run(){
			dCD.JointUserWithdrawDelayReset();
			dCD.log.info("[dConomy] - Joint User Withdraw Delay Reset!");
			dCD.SetReset("JointWithdrawTimerResetTo", System.currentTimeMillis()+jdelay);
			if (etc.getLoader().getPlugin("dConomy").isEnabled()){
				JWDtime.schedule(new dCJWDTimerExpire(), jdelay);
			}
		}
	}
}
