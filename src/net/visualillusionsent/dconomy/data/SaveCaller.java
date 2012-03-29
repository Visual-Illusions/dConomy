package net.visualillusionsent.dconomy.data;

/**
 * SaveCaller - Runnable to execute saving
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class SaveCaller implements Runnable{
    public void run() {
        DCoProperties.getDS().saveMaps();
    }
}
