import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
* dCTimer v1.x - Handles dConomy Timers
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

public class dCTimer{
	public dCTimer(){
	}
	
	dCBankTimer dCBT = new dCBankTimer(this);
	dCJointWithdrawDelayTimer dCWDT = new dCJointWithdrawDelayTimer(this);
	
	public dCBankTimer getdCBT(){
		return dCBT;
	}
	
	public dCJointWithdrawDelayTimer getdCJWDT(){
		return dCWDT;
	}
 	
	public class dCBankTimer{
		Map<String, Double> accounts;
		int bdelay;
		double interest;
		dCData dCD;
		Timer banktime;
		
		public dCBankTimer(dCTimer dct){ }
		
		public void SetUpBT(dCData dCD, int delay, double interest, long timerrest){
			this.bdelay = delay*60*1000;
			this.interest = interest;
			this.dCD = dCD;
			banktime = new Timer();
			banktime.schedule(new dCTimerExpire(), timerrest);
		}
	
		public void cancel(){
			banktime.cancel();
			banktime.purge();
		}

		public class dCTimerExpire extends TimerTask {
			public dCTimerExpire(){ }
		
			public void run(){
				accounts = new HashMap<String, Double>();
				try {
					accounts = dCD.returnMap("bank");
				} catch (Exception e) {
					dCD.log.severe("[dConomy] - Unable to retrieve array of bank balances!");
				}
				for (String acc : accounts.keySet()){
					double balance = accounts.get(acc);
					double newbalance = balance + (balance*interest);
					dCD.setBalance(newbalance, acc, "bank");
				}
				dCD.log.info("[dConomy] - Bank Interest Paid!");
				dCD.SetReset("BankTimerResetTo", System.currentTimeMillis()+bdelay);
				if (etc.getLoader().getPlugin("dConomy").isEnabled()){
					banktime.schedule(new dCTimerExpire(), bdelay);
				}
			}
		}
	}
	
	public class dCJointWithdrawDelayTimer{
		Timer JWDtime;
		int jdelay;
		dCData dCD;
		
		public dCJointWithdrawDelayTimer(dCTimer dCT){ }
		
		public void SetUpJWDT(dCData dCD, int delay, long timerrest){
			this.dCD = dCD;
			JWDtime = new Timer();
			this.jdelay = delay*60*1000;
			JWDtime.schedule(new dCJWDTimerExpire(), timerrest);
		}
	
		public void cancel(){
			JWDtime.cancel();
			JWDtime.purge();
		}

		public class dCJWDTimerExpire extends TimerTask {
			public dCJWDTimerExpire(){ 
			}
		
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
}
