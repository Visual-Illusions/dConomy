import net.visualillusionsent.dconomy.Misc;

public class MiscImpl implements Misc{

    @Override
    public String matchPlayer(String name) {
        Player player = etc.getServer().matchPlayer(name);
        return player != null ? player.getName() : null;
    }

}
