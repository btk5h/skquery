package com.w00tmast3r.skquery.elements.packet;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


@Patterns("make %players% see %block% as %itemtype%")
public class EffClientBlock extends Effect {

    private Expression<Player> player;
    private Expression<Block> block;
    private Expression<ItemType> material;

    @Override
    protected void execute(Event event) {
        Player[] ps = player.getAll(event);
        Block b = block.getSingle(event);
        ItemType m = material.getSingle(event);
        if(ps == null || b == null || m == null) return;
        Material mat = m.getRandom().getType();
        if(!mat.isBlock()) return;
        for(Player p : ps){
            p.sendBlockChange(b.getLocation(), mat, (byte) m.getRandom().getDurability());
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "credits";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        block = (Expression<Block>) expressions[1];
        material = (Expression<ItemType>) expressions[2];
        return true;
    }
}
