/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.data.wallet;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.utils.SystemUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class WalletXMLSource extends WalletDataSource {

    private final Format xmlform = Format.getPrettyFormat().setExpandEmptyElements(false).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP);
    private final XMLOutputter outputter = new XMLOutputter(xmlform);
    private final SAXBuilder builder = new SAXBuilder();
    private final String wallet_Path = dCoBase.getProperties().getConfigurationDirectory().concat("wallets.xml");
    private FileWriter writer;

    public WalletXMLSource(WalletHandler wallet_handler) {
        super(wallet_handler);
    }

    @Override
    public final boolean load() {
        dCoBase.info("Loading Wallets...");
        synchronized (lock) {
            File walletFile = new File(wallet_Path);
            Exception ex = null;
            int load = 0;
            if (!walletFile.exists()) {
                dCoBase.info("Wallets file not found. Creating...");
                Element wallets = new Element("wallets");
                Document root = new Document(wallets);
                try {
                    writer = new FileWriter(wallet_Path);
                    outputter.output(root, writer);
                }
                catch (IOException e) {
                    ex = e;
                }
                finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                    catch (IOException e) {
                    }
                    writer = null;
                    if (ex != null) {
                        dCoBase.severe("Failed to create new Wallets file...");
                        dCoBase.stacktrace(ex);
                        return false;
                    }
                }
            }
            else {
                try {
                    Document doc = builder.build(walletFile);
                    Element root = doc.getRootElement();
                    List<Element> wallets = root.getChildren();
                    for (Element wallet : wallets) {
                        wallet_handler.addWallet(new UserWallet(wallet.getAttributeValue("owner"), wallet.getAttribute("balance").getDoubleValue(), wallet.getAttribute("lockedOut").getBooleanValue(), this));
                        load++;
                    }
                }
                catch (JDOMException jdomex) {
                    dCoBase.severe("JDOM Exception while parsing Wallets file...");
                    dCoBase.stacktrace(ex);
                    return false;
                }
                catch (IOException e) {
                    dCoBase.severe("Input/Output Exception while parsing Wallets file...");
                    dCoBase.stacktrace(ex);
                    return false;
                }
            }
            dCoBase.info(String.format("Loaded %d Wallets...", load));
        }
        return true;
    }

    @Override
    public final boolean saveAccount(Wallet account) {
        boolean success = true;
        synchronized (lock) {
            File walletFile = new File(wallet_Path);
            try {
                Document doc = builder.build(walletFile);
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                boolean found = false;
                for (Element wallet : wallets) {
                    String name = wallet.getAttributeValue("owner");
                    if (name.equals(account.getOwner())) {
                        wallet.getAttribute("balance").setValue(String.format("%.2f", account.getBalance()));
                        wallet.getAttribute("lockedOut").setValue(String.valueOf(account.isLocked()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Element newWallet = new Element("wallet");
                    newWallet.setAttribute("owner", account.getOwner());
                    newWallet.setAttribute("balance", String.format("%.2f", account.getBalance()));
                    newWallet.setAttribute("lockedOut", String.valueOf(account.isLocked()));
                    root.addContent(newWallet);
                }
                try {
                    writer = new FileWriter(walletFile);
                    outputter.output(root, writer);
                }
                catch (IOException ex) {
                    dCoBase.severe("Failed to write to Wallets file...");
                    dCoBase.stacktrace(ex);
                    success = false;
                }
                finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                    catch (IOException e) {
                    }
                    writer = null;
                }
            }
            catch (JDOMException jdomex) {
                dCoBase.severe("JDOM Exception while trying to save wallet for User:" + account.getOwner());
                dCoBase.stacktrace(jdomex);
                success = false;
            }
            catch (IOException ioex) {
                dCoBase.severe("Input/Output Exception while trying to save wallet for User:" + account.getOwner());
                dCoBase.stacktrace(ioex);
                success = false;
            }
        }
        return success;
    }

    @Override
    public final boolean reloadAccount(Wallet account) {
        boolean success = true;
        synchronized (lock) {
            File walletFile = new File(wallet_Path);
            try {
                Document doc = builder.build(walletFile);
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                for (Element wallet : wallets) {
                    String name = wallet.getAttributeValue("owner");
                    if (name.equals(account.getOwner())) {
                        account.setBalance(wallet.getAttribute("balance").getDoubleValue());
                        account.setLockOut(wallet.getAttribute("lockedOut").getBooleanValue());
                        break;
                    }
                }
            }
            catch (JDOMException jdomex) {
                dCoBase.severe("JDOM Exception while trying to reload wallet for User:" + account.getOwner());
                dCoBase.stacktrace(jdomex);
                success = false;
            }
            catch (IOException ioex) {
                dCoBase.severe("Input/Output Exception while trying to reload wallet for User:" + account.getOwner());
                dCoBase.stacktrace(ioex);
                success = false;
            }
            catch (AccountingException aex) {
                dCoBase.severe("Accounting Exception while trying to reload wallet for User:" + account.getOwner());
                dCoBase.stacktrace(aex);
                success = false;
            }
        }
        return success;
    }
}
