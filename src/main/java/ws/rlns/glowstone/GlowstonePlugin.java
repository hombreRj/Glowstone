package ws.rlns.glowstone;

import org.bukkit.plugin.java.JavaPlugin;
import ws.rlns.glowstone.database.GlowstoneMongo;
import ws.rlns.glowstone.database.utils.Credentials;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class GlowstonePlugin extends JavaPlugin {


    private static final AtomicReference<GlowstoneMongo> DATABASE_REFERENCE = new AtomicReference<>();


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        if (!getConfig().getString("uri").isEmpty()) {
            DATABASE_REFERENCE.set(new GlowstoneMongo(new Credentials(
                    getConfig().getString("database.uri")
            ), getConfig().getString("database.database")));
        }else{
            DATABASE_REFERENCE.set(new GlowstoneMongo(new Credentials(
                    getConfig().getString("database.username"),
                    getConfig().getString("database.password"),
                    getConfig().getString("database.ip"),
                    getConfig().getInt("database.port")
            ), getConfig().getString("database.database")));
        }
    }

    @Override
    public void onDisable() {
        getDatabase().ifPresent(glowstoneMongo -> glowstoneMongo.getClient().close());
    }

    public static Optional<GlowstoneMongo> getDatabase(){
        return Optional.ofNullable(DATABASE_REFERENCE.get());
    }
}
