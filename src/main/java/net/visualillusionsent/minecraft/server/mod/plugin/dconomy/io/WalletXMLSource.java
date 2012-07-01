package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.utils.SystemUtils;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public final class WalletXMLSource implements dCoDataSource{

    private final Format xmlform = Format.getPrettyFormat().setExpandEmptyElements(false).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP);
    private final XMLOutputter outputter = new XMLOutputter(xmlform);
    private final SAXBuilder builder = new SAXBuilder();
    private final String wallet_Path = dCoBase.getProperties().getConfigurationDirectory().concat("wallets.xml");
    private FileWriter writer;

    @Override
    public final boolean load(){
        dCoBase.info("Loading Wallets...");
        File walletFile = new File(wallet_Path);
        Exception ex = null;
        int load = 0;
        if (!walletFile.exists()) {
            dCoBase.info("Wallets file not found. Creating...");
            Element wallets = new Element("wallets");
            Document root = new Document(wallets);
            wallets.addContent(new Comment("Modifing this file while server is running may cause issues!"));
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
                catch (IOException e) {}
                writer = null;
                if (ex != null) {
                    dCoBase.severe("Failed to create new Zones file...");
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
                    new UserWallet(wallet.getAttributeValue("user"), wallet.getAttribute("balance").getDoubleValue());
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
        return true;
    }

    @Override
    public final void saveAccount(Account account){
        synchronized (lock) {
            File walletFile = new File(wallet_Path);
            Exception ex = null;
            try {
                Document doc = builder.build(walletFile);
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                boolean found = false;
                for (Element wallet : wallets) {
                    String name = wallet.getChildText("name");
                    if (name.equals(account.getOwner())) {
                        wallet.getAttribute("balance").setValue(String.format("%.2f", account.getBalance()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Element newWallet = new Element("wallet");
                    newWallet.setAttribute("user", account.getOwner());
                    newWallet.setAttribute("balance", String.format("%.2f", account.getBalance()));
                    root.addContent(newWallet);
                }
                try {
                    writer = new FileWriter(walletFile);
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
                    catch (IOException e) {}
                    writer = null;
                    if (ex != null) {
                        dCoBase.severe("Failed to write to Wallets file...");
                        dCoBase.stacktrace(ex);
                    }
                }
            }
            catch (JDOMException jdomex) {
                dCoBase.severe("JDOM Exception while trying to save wallet for User:" + account.getOwner());
                dCoBase.stacktrace(jdomex);
            }
            catch (IOException ioex) {
                dCoBase.severe("Input/Output Exception while trying to save wallet for User:" + account.getOwner());
                dCoBase.stacktrace(ioex);
            }
        }
    }

    @Override
    public final void reloadAccount(Account account){
        synchronized (lock) {
            File walletFile = new File(wallet_Path);
            try {
                Document doc = builder.build(walletFile);
                Element root = doc.getRootElement();
                List<Element> wallets = root.getChildren();
                for (Element wallet : wallets) {
                    String name = wallet.getChildText("name");
                    if (name.equals(account.getOwner())) {
                        account.setBalance(wallet.getAttribute("balance").getDoubleValue());
                        break;
                    }
                }
            }
            catch (JDOMException jdomex) {
                dCoBase.severe("JDOM Exception while trying to reload wallet for User:" + account.getOwner());
                dCoBase.stacktrace(jdomex);
            }
            catch (IOException ioex) {
                dCoBase.severe("Input/Output Exception while trying to reload wallet for User:" + account.getOwner());
                dCoBase.stacktrace(ioex);
            }
        }
    }
}
