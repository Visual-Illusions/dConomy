import java.util.Comparator;
import java.util.Map;

/**
 * dCValueComparator - Compares Doubles to get the organized result
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
 * 
 * This Code is a modification to:
 * 
 * ValueComparator - Compares integers to get the organized result
 * Copyright (C) 2010  Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
