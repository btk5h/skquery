package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import java.io.File;
import java.io.IOException;


@Patterns("flush path %string% in %string%")
public class EffFlush extends Effect {

    private Expression<String> cfg, path;

    @Override
    protected void execute(Event event) {
        String c = cfg.getSingle(event);
        String p = path.getSingle(event);
        if(c == null || p == null) return;
        File file = new File(Skript.getInstance().getDataFolder() + "/scripts/" + c + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileConfiguration fcfg = YamlConfiguration.loadConfiguration(file);
        fcfg.set(p, null);
        try {
            fcfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "flush";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        path = (Expression<String>) expressions[1];
        cfg = (Expression<String>) expressions[2];
        return true;
    }
}
