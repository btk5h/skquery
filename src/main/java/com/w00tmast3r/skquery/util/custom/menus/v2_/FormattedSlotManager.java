package com.w00tmast3r.skquery.util.custom.menus.v2_;

import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.util.BiValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FormattedSlotManager implements Listener {

    private static final HashMap<UUID, BiValue<HashMap<Integer, SlotRule>, Event>> playerRules = new HashMap<>();
    private static final List<UUID> exempt = new ArrayList<>();

    public static HashMap<Integer, SlotRule> getRules(Player p) {
        return playerRules.containsKey(p.getUniqueId()) ? playerRules.get(p.getUniqueId()).getFirst() : new HashMap<Integer, SlotRule>();
    }

    public static void setRules(Event e, Player p, HashMap<Integer, SlotRule> slotRules) {
        playerRules.put(p.getUniqueId(), new BiValue<>(slotRules, e));
    }

    public static void exemptNextClose(Player p) {
        exempt.add(p.getUniqueId());
    }

    public static void addRule(Event e, Player player, int slot, SlotRule rule) {
        if (!playerRules.containsKey(player.getUniqueId())) {
            playerRules.put(player.getUniqueId(), new BiValue<HashMap<Integer, SlotRule>, Event>(new HashMap<Integer, SlotRule>(), null));
        }
        playerRules.get(player.getUniqueId()).getFirst().put(slot, rule);
        playerRules.get(player.getUniqueId()).setSecond(e);
    }

    public static void removeRule(Player player, int slot) {
        if (!playerRules.containsKey(player.getUniqueId())) {
            playerRules.put(player.getUniqueId(), new BiValue<HashMap<Integer, SlotRule>, Event>(new HashMap<Integer, SlotRule>(), null));
        }
        playerRules.get(player.getUniqueId()).getFirst().remove(slot);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        playerRules.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        final HashMap<Integer, SlotRule> map = getRules(p);
        if (event.isShiftClick() && map != null && map.size() > 0) event.setCancelled(true);
        assert map != null;
        if (playerRules.containsKey(p.getUniqueId()) && event.getSlotType() == InventoryType.SlotType.CONTAINER && map.get(event.getSlot()) != null) {
            event.setCancelled(true);
            SlotRule rule = playerRules.get(p.getUniqueId()).getFirst().get(event.getSlot());
            rule.run(playerRules.get(p.getUniqueId()).getSecond());
            if (rule.willClose()) {
                Bukkit.getScheduler().runTaskLater(SkQuery.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        p.getOpenInventory().close();
                    }
                }, 1);
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (exempt.contains(event.getPlayer().getUniqueId())) {
            exempt.remove(event.getPlayer().getUniqueId());
            return;
        }
        Bukkit.getScheduler().runTaskLater(SkQuery.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (playerRules.containsKey(event.getPlayer().getUniqueId())) {
                    if (playerRules.get(event.getPlayer().getUniqueId()) != null)
                        playerRules.get(event.getPlayer().getUniqueId()).getFirst().clear();
                    playerRules.get(event.getPlayer().getUniqueId()).setSecond(null);
                }
            }
        }, 1);
    }
}
