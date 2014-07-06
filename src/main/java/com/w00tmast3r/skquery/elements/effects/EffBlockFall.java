package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;


@Patterns({"spawn falling block of %itemtype% at %location%",
        "spawn damaging falling block of %itemtype% at %location%",
        "spawn undroppable falling block of %itemtype% at %location%",
        "spawn damaging undroppable falling block of %itemtype% at %location%",
        "spawn undroppable damaging falling block of %itemtype% at %location%"})
public class EffBlockFall extends Effect {

    private Expression<ItemType> type;
    private Expression<Location> loc;
    boolean breaks, damages;

    @Override
    protected void execute(Event event) {
        ItemType t = type.getSingle(event);
        if(t == null) return;
        for(Location locs : loc.getArray(event)) {
            for(ItemStack i : t.getAll()){
                FallingBlock block = locs.getWorld().spawnFallingBlock(locs, i.getType(), (byte) i.getDurability());
                if (damages) {
                    try {
                        Object craftSand = Reflection.obcClass("entity.CraftFallingSand").getMethod("getHandle").invoke(block);
                        craftSand.getClass().getMethod("a", boolean.class).invoke(craftSand, true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                if (breaks) {
                    block.setDropItem(false);
                }
            }
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "falling block";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        type = (Expression<ItemType>) expressions[0];
        loc = (Expression<Location>) expressions[1];
        damages = (i == 1 || i == 3 || i == 4);
        breaks = (i == 2 || i == 3 || i == 4);
        return true;
    }
}
