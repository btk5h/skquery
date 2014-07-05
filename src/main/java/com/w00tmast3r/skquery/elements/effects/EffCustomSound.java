package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skaddonlib.util.Reflection;
import org.bukkit.Location;
import org.bukkit.event.Event;

import java.lang.reflect.InvocationTargetException;


@Patterns("play raw sound %string% at %locations% with pitch %number% volume %number%")
public class EffCustomSound extends Effect {

    private Expression<String> sound;
    private Expression<Location> loc;
    private Expression<Number> pit, vol;

    @Override
    protected void execute(Event event) {
        String s = sound.getSingle(event);
        Location[] l = loc.getAll(event);
        float p = pit.getSingle(event).floatValue();
        float v = vol.getSingle(event).floatValue();
        if(s == null || l == null) return;
        for(Location fl : l){
            try {
                Class craftWorldClass = Reflection.obcClass("CraftWorld");
                Object worldServer = craftWorldClass.getMethod("getHandle").invoke(fl.getWorld());
                worldServer.getClass().getMethod("makeSound", double.class, double.class, double.class, String.class, float.class, float.class).invoke(worldServer, fl.getX(), fl.getY(), fl.getZ(), s, p, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "sound";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        sound = (Expression<String>) expressions[0];
        loc = (Expression<Location>) expressions[1];
        pit = (Expression<Number>) expressions[3];
        vol = (Expression<Number>) expressions[2];
        return true;
    }
}
