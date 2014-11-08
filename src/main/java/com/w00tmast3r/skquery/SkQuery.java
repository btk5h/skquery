package com.w00tmast3r.skquery;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.w00tmast3r.skquery.db.ScriptCredentials;
import com.w00tmast3r.skquery.skript.DynamicEnumTypes;
import com.w00tmast3r.skquery.skript.SkqFileRegister;
import com.w00tmast3r.skquery.util.custom.menus.v2_.FormattedSlotManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkQuery extends JavaPlugin {

    private static SkQuery instance = null;
    private static SkriptAddon addonInstance = null;

    public SkQuery() {
        instance = this;
    }

    @Override
    public void onEnable() {
        DynamicEnumTypes.register();
        getDataFolder().mkdirs();
        addonInstance = Skript.registerAddon(this);
        Registration.enableSnooper();
        Bukkit.getPluginManager().registerEvents(new FormattedSlotManager(), this);
        SkqFileRegister.load();
    }

    @Override
    public void onDisable() {
        ScriptCredentials.clear();
    }

    public static SkQuery getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addonInstance;
    }
}
