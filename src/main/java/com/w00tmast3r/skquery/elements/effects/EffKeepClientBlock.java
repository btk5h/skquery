package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.*;
import com.w00tmast3r.skquery.util.BiValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

@Name("Server/Client Block")
@Description("Make a player see a block as another type of block. If the client recieves an update to the block, it will not revert like with ((EffClientBlock)the client block effect).")
@Examples("on click:;->make player see clicked block as air permanently")
@Dependency("ProtocolLib")
@Patterns({"make %players% see %blocks% as %itemtype% permanently",
    "restore updates to %blocks% for %offlineplayers%",
    "restore all updates"})
public class EffKeepClientBlock extends Effect {

    private Expression<Player> player;
    private Expression<OfflinePlayer> offlinePlayers;
    private Expression<Block> block;
    private Expression<ItemType> material;
    private int pattern;

    @Override
    protected void execute(Event event) {
        if (pattern == 0) {
            ItemType m = material.getSingle(event);
            if (m == null) return;
            Material mat = m.getRandom().getType();
            if (!mat.isBlock()) return;
            for (Player p : player.getAll(event)) {
                for (Block b : block.getAll(event)) {
                    p.sendBlockChange(b.getLocation(), mat, (byte) m.getRandom().getDurability());
                    if (!PacketListeners.clientRenders.containsKey(b.getLocation())) {
                        PacketListeners.clientRenders.put(b.getLocation(), new HashMap<UUID, BiValue<Material, Integer>>());
                    }
                    PacketListeners.clientRenders.get(b.getLocation()).put(p.getUniqueId(), new BiValue<>(mat, (int) m.getRandom().getDurability()));
                }
            }
        } else if (pattern == 1) {
            for (OfflinePlayer p : offlinePlayers.getAll(event)) {
                for (Block b : block.getAll(event)) {
                    if (PacketListeners.clientRenders.containsKey(b.getLocation())) {
                        PacketListeners.clientRenders.get(b.getLocation()).remove(p.getUniqueId());
                        if (PacketListeners.clientRenders.get(b.getLocation()).size() == 0) PacketListeners.clientRenders.remove(b.getLocation());
                        if (p.isOnline()) p.getPlayer().sendBlockChange(b.getLocation(), b.getType(), b.getData());
                    }
                }
            }
        } else {
            PacketListeners.clientRenders = new HashMap<>();
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "credits";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (i == 0) {
            player = (Expression<Player>) expressions[0];
            block = (Expression<Block>) expressions[1];
            material = (Expression<ItemType>) expressions[2];
        } else if (i == 1) {
            block = (Expression<Block>) expressions[0];
            offlinePlayers = (Expression<OfflinePlayer>) expressions[1];
        }
        pattern = i;
        return true;
    }

    public static class PacketListeners implements Listener {

        private static boolean hasInitialized = false;
        private static HashMap<Location, HashMap<UUID, BiValue<Material, Integer>>> clientRenders = new HashMap<>();

        static {
            if (!hasInitialized) {
                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(new PacketAdapter.AdapterParameteters().plugin(SkQuery.getInstance()).serverSide().types(PacketType.Play.Server.BLOCK_CHANGE)) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        StructureModifier<Integer> ints = event.getPacket().getIntegers();
                        Player p = event.getPlayer();
                        Location l = new Location(p.getWorld(), ints.read(0), ints.read(1), ints.read(2));
                        if (clientRenders.containsKey(l) && clientRenders.get(l).containsKey(p.getUniqueId())) {
                            event.getPacket().getBlocks().write(0, clientRenders.get(l).get(p.getUniqueId()).getFirst());
                            ints.write(3, clientRenders.get(l).get(p.getUniqueId()).getSecond());
                        }
                    }
                });
            }
            hasInitialized = true;
        }

    }
}
