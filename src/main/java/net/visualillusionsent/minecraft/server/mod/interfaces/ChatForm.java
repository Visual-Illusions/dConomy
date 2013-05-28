/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.interfaces;

public enum ChatForm {

    /**
     * <b>MARKER §</b>
     */
    MARKER('\u00A7'),

    /**
     * <FONT COLOR=000000><b>BLACK</b></FONT>
     */
    BLACK('0'),

    /**
     * <font color="000066"><b>DARK_BLUE</b></font>
     */
    DARK_BLUE('1'),

    /**
     * <font color="006600"><b>GREEN</b></font>
     */
    GREEN('2'),

    /**
     * <font color="006666"><b>TURQUOISE</b></font>
     */
    TURQUOISE('3'),

    /**
     * <font color="990000"><b>RED</b></font>
     */
    RED('4'),

    /**
     * <font color="540054"><b>PURPLE</b></font>
     */
    PURPLE('5'),

    /**
     * <font color="FF9933"><b>ORANGE</b></font>
     */
    ORANGE('6'),

    /**
     * <font color="CCCCCC"><b>LIGHT_GRAY</b></font>
     */
    LIGHT_GRAY('7'),

    /**
     * <font color="333333"><b>GRAY</b></font>
     */
    GRAY('8'),

    /**
     * <font color="2A2A7F"><b>BLUE</b></font>
     */
    BLUE('9'),

    /**
     * <font color="33FF33"><b>LIGHT_GREEN</b></font>
     */
    LIGHT_GREEN('A'),

    /**
     * <font color="00FFFF"><b>CYAN</b></font>
     */
    CYAN('B'),

    /**
     * <font color="FF0022"><b>LIGHT_RED</b></font>
     */
    LIGHT_RED('C'),

    /**
     * <font color="FF00FF"><b>PINK</b></font>
     */
    PINK('D'),

    /**
     * <font color="FFFF00"><b>YELLOW</b></font>
     */
    YELLOW('E'),

    /**
     * <font color="000000"><b>WHITE</b></font>
     */
    WHITE('F'),

    /**
     * <b>BOLD</b>
     */
    BOLD('L'),

    /**
     * <s>STRIKED</s>
     */
    STRIKED('M'),

    /**
     * <u>UNDERLINED</u>
     */
    UNDERLINED('N'),

    /**
     * <i>ITALIC</i>
     */
    ITALIC('O'),

    /**
     * RESET
     */
    RESET('R');

    private final char charCode;

    private ChatForm(char charCode){
        this.charCode = charCode;
    }

    public final String concat(String str){
        if (this == MARKER) {
            return stringValue().concat(str);
        }
        else {
            return MARKER.concat(stringValue().concat(str));
        }
    }

    /**
     * Returns the char value of the {@code MCChatForm}
     * 
     * @return char value of the {@code MCChatForm}
     */
    public final char charValue(){
        return charCode;
    }

    /**
     * Returns the {@code MCChatForm} as a {@link String}
     * 
     * @return String value of {@code MCChatForm}
     */
    public final String stringValue(){
        return String.valueOf(charCode);
    }

    /**
     * Returns a string of the {@code MCChatForm}<br>
     * If the {@code MCChatForm} is that other than {@code MARKER} then {@code MARKER} is appended to the front.
     * 
     * @return {@code MARKER} or {@code MARKER} + colorCode
     */
    public final String toString(){
        if (this == MARKER) {
            return stringValue();
        }
        else {
            return MARKER.concat(stringValue());
        }
    }

    /**
     * removes all color formating from a line
     * 
     * @param str
     * @return str with formating removed
     */
    public static final String removeFormating(String str){
        return str.replaceAll(MARKER.concat("[A-FL-NRa-fl-nr0-9]"), "");
    }
}
