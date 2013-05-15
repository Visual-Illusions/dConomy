package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public final class ServerWallet extends Wallet{

    private final boolean maxAlways;

    public ServerWallet(boolean maxAlways){
        super("SERVER", maxAlways ? 999999999999999999D : dCoBase.getProperties().getDouble("server.balance"));
        this.maxAlways = maxAlways;
    }

    @Override
    public final double getBalance(){
        return maxAlways ? 999999999999999999D : super.getBalance();
    }

    @Override
    public final double deposit(double add){
        return maxAlways ? 999999999999999999D : super.deposit(add);
    }

    @Override
    public final double deposit(String add){
        return maxAlways ? 999999999999999999D : super.deposit(add);
    }

    @Override
    public final double debit(double remove){
        return maxAlways ? 999999999999999999D : super.debit(remove);
    }

    @Override
    public final double debit(String remove){
        return maxAlways ? 999999999999999999D : super.debit(remove);
    }

    @Override
    public final double setBalance(double set){
        return maxAlways ? 999999999999999999D : super.setBalance(set);
    }

    @Override
    public final double setBalance(String set){
        return maxAlways ? 999999999999999999D : super.setBalance(set);
    }

    protected final void save(){
        if (!maxAlways) {
            dCoBase.getProperties().setServerBalance(getBalance());
        }
    }
}
