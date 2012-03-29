package net.visualillusionsent.dconomy.data;
import java.util.Comparator;
import java.util.Map;

/**
 * dCValueComparator - Compares doubles to get the organized result
 * <p>
 * This Class's Code is a modification to:
 * <p>
 * ValueComparator - Compares integers to get the organized result
 * Copyright (C) 2010  Nijikokun <nijikokun@gmail.com>
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 *
 */
class dCValueComparator implements Comparator<Object> {

    Map<?, ?> base;

    public dCValueComparator(Map<?, ?> accounts) {
        this.base = accounts;
    }

    public int compare(Object a, Object b) {
        double ax = Double.parseDouble(String.valueOf(base.get(a)));
        double bx = Double.parseDouble(String.valueOf(base.get(b)));

        if (ax < bx) {
            return 1;
        } else if (ax == bx) {
            return 0;
        } else {
            return -1;
        }
    }
}
