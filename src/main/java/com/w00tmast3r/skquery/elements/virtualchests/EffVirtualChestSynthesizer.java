package com.w00tmast3r.skquery.elements.virtualchests;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skriptplus.SkriptPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Patterns("show %string% with %number% row[s] named %string% to %player%")
@ManualDoc(
        name = "Display Item Menu v1",
        description = "This is a deprecated effect. Avoid using this effect, use Display Item Menu v2 instead."
)
public class EffVirtualChestSynthesizer extends Effect implements Listener {

    private Expression<String> virtualInventorySerial, virtualInventoryName;
    private Expression<Number> rows;
    private Expression<Player> target;

    @Override
    protected void execute(Event event) {
        String vIS = virtualInventorySerial.getSingle(event);
        final String vIN = virtualInventoryName.getSingle(event);
        Number r1 = rows.getSingle(event);
        final Player t = target.getSingle(event);
        if(vIN == null || vIS == null || t == null || r1 == null) return;
        final int r = r1.intValue() * 9;
        final String[] invStr = vIS.split(";");
        final Inventory inv = Bukkit.createInventory(null, r, vIN);
        t.openInventory(inv);
        Bukkit.getScheduler().runTaskLater(SkriptPlus.me.getPlugin(), new Runnable() {
            public void run() {
                boolean isCurrentlyFlagging = true;
                String currentOperation = "";
                List<String>[] commands = new ArrayList[r];
                //String commands[] = new String[r];
                int node = 0;
                for(int i = 0; i < invStr.length; i++){
                    if(isCurrentlyFlagging){
                        currentOperation = invStr[i];
                        isCurrentlyFlagging = false;
                    } else {
                        if(currentOperation.equalsIgnoreCase("s")){        // node reassignment
                            if(Integer.parseInt(invStr[i]) - 1 > r){
                                Bukkit.getLogger().warning("Could not pass node assignment, will not attempt to allocate a slot id greater than rows.");
                                t.openInventory(Bukkit.createInventory(null, 9, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error"));
                                return;
                            } else {
                                node = Integer.parseInt(invStr[i]) - 1;
                            }
                        } else if(currentOperation.equalsIgnoreCase("i")){ // stack reassignment
                            inv.setItem(node, SkriptParser.parseLiteral(invStr[i], ItemType.class, ParseContext.DEFAULT).getSingle().getRandom());
                        } else if(currentOperation.equalsIgnoreCase("l")){ // lore reassignment
                            ItemStack stack = inv.getItem(node);
                            ItemMeta stackMeta = stack.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(Material.STONE) : stack.getItemMeta();
                            stackMeta.setLore(Arrays.asList(invStr[i].split("\\|\\|")));
                            stack.setItemMeta(stackMeta);
                        } else if(currentOperation.equalsIgnoreCase("n")){ // name reassignment
                            ItemStack stack = inv.getItem(node);
                            ItemMeta stackMeta = stack.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(Material.STONE) : stack.getItemMeta();
                            stackMeta.setDisplayName(invStr[i]);
                            stack.setItemMeta(stackMeta);
                        } else if(currentOperation.equalsIgnoreCase("c")){ // command reassignment
                            if(commands[node] == null) commands[node] = new ArrayList<String>();
                            commands[node].add(invStr[i]);
                            //commands[node] = invStr[i];
                        } else {
                            Bukkit.getLogger().warning("Could not parse node " + currentOperation + " : " + (i - 1));
                            t.openInventory(Bukkit.createInventory(null, 9, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error"));
                            return;
                        }
                        isCurrentlyFlagging = true;
                    }
                }
                for(ItemStack stack : inv.getContents()) {
                    if (stack != null) {
                        ItemMeta stackMeta = stack.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(Material.STONE) : stack.getItemMeta();
                        List<String> lore =  stackMeta.getLore() == null ? new ArrayList<String>() : stackMeta.getLore();
                        lore.add(ChatColor.translateAlternateColorCodes('&', "&0&0&0&0&0&0&0&0"));
                        stackMeta.setLore(lore);
                        stack.setItemMeta(stackMeta);
                    }
                }
                t.updateInventory();
                Bukkit.getPluginManager().registerEvents(new VirtualChestManager(vIN, t.getName(), commands), SkriptPlus.me.getPlugin());
            }
        }, 1);
    }

    @Override
    public String toString(Event event, boolean b) {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        virtualInventorySerial = (Expression<String>) expressions[0];
        rows = (Expression<Number>) expressions[1];
        virtualInventoryName = (Expression<String>) expressions[2];
        target = (Expression<Player>) expressions[3];
        return true;
    }
}
