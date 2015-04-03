/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2015 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.data.wallet;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.DataLock;
import net.visualillusionsent.minecraft.plugin.util.Tools;
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
import java.util.UUID;

public final class WalletXMLSource extends WalletDataSource {

    private final DataLock lock = new DataLock();
    private final Format xmlform = Format.getPrettyFormat().setExpandEmptyElements(false).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP);
    private final XMLOutputter outputter = new XMLOutputter(xmlform);
    private final SAXBuilder builder = new SAXBuilder();
    private final String wallet_Path = dCoBase.getProperties().getConfigurationDirectory().concat("wallets.xml");
    private FileWriter writer;
    private Document doc;

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
                doc = new Document(wallets);
                try {
                    writer = new FileWriter(wallet_Path);
                    outputter.output(doc, writer);
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
                        dCoBase.warning("Failure closing Wallets writer...");
                    }
                    if (ex != null) {
                        dCoBase.severe("Failed to create new Wallets file...");
                        dCoBase.stacktrace(ex);
                    }
                }
                if (ex != null) {
                    return false;
                }
            }
            else {
                try {
                    if (doc == null) {
                        doc = builder.build(walletFile);
                    }
                    Element root = doc.getRootElement();
                    List<Element> wallets = root.getChildren();
                    for (Element wallet : wallets) {
                        String owner = wallet.getAttributeValue("owner");
                        UUID ownerUUID;
                        if (Tools.isUserName(owner)) {
                            ownerUUID = dCoBase.getServer().getUUIDFromName(owner);
                            wallet.detach(); // Remove the old account
                        }
                        else {
                            ownerUUID = UUID.fromString(owner);
                        }
                        wallet_handler.addWallet(new UserWallet(ownerUUID, wallet.getAttribute("balance").getDoubleValue(), wallet.getAttribute("lockedOut").getBooleanValue(), this));
                        load++;
                    }
                }
                catch (JDOMException jdomex) {
                    dCoBase.severe("JDOM Exception while parsing Wallets file...");
                    dCoBase.stacktrace(jdomex);
                    return false;
                }
                catch (IOException ioex) {
                    dCoBase.severe("Input/Output Exception while parsing Wallets file...");
                    dCoBase.stacktrace(ioex);
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
                if (doc == null) {
                    doc = builder.build(walletFile);
                }
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                boolean found = false;
                for (Element wallet : wallets) {
                    String name = wallet.getAttributeValue("owner");
                    if (name.equals(account.getOwner().toString())) {
                        wallet.getAttribute("balance").setValue(String.format("%.2f", account.getBalance()));
                        wallet.getAttribute("lockedOut").setValue(String.valueOf(account.isLocked()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Element newWallet = new Element("wallet");
                    newWallet.setAttribute("owner", account.getOwner().toString());
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
                        //
                    }
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
                doc = builder.build(walletFile);
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                for (Element wallet : wallets) {
                    String name = wallet.getAttributeValue("owner");
                    if (name.equals(account.getOwner().toString())) {
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
