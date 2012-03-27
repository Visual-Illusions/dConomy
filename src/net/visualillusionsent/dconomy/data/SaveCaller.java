package net.visualillusionsent.dconomy.data;

public class SaveCaller implements Runnable{
    public void run() {
        DCoProperties.getDS().saveMaps();
    }
}
