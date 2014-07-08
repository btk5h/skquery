package com.w00tmast3r.skquery.elements.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.elements.events.bukkit.ScriptOptionsEvent;
import com.w00tmast3r.skquery.elements.lang.FunctionEvent;
import com.w00tmast3r.skquery.elements.lang.RoutineEvent;
import com.w00tmast3r.skquery.util.custom.projectile.ItemProjectileHitEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventRegistry extends AbstractTask {

    @Override
    public void run() {
        registerEvent("evtEnchantItem", "Enchant", EnchantItemEvent.class, "enchant");
        EventValues.registerEventValue(EnchantItemEvent.class, ItemStack.class, new Getter<ItemStack, EnchantItemEvent>() {
            @Override
            public ItemStack get(EnchantItemEvent enchantItemEvent) {
                return enchantItemEvent.getItem();
            }
        }, 0);
        EventValues.registerEventValue(EnchantItemEvent.class, Player.class, new Getter<Player, EnchantItemEvent>() {
            @Override
            public Player get(EnchantItemEvent enchantItemEvent) {
                return enchantItemEvent.getEnchanter();
            }
        }, 0);

        registerEvent("evtSheepDye", "Sheep Dye", SheepDyeWoolEvent.class, "sheep dye");
        
        registerEvent("evtHorseJump", "Horse Jump", HorseJumpEvent.class, "horse jump");
        
        registerEvent("evtEditBook", "Book Editing", PlayerEditBookEvent.class, "[book] edit");
       
        registerEvent("evtToggleFly", "Flight Toggle", PlayerToggleFlightEvent.class, "[player] toggl(e|ing) (flight|fly)", "[player] (flight|fly) toggl(e|ing)");
       
        registerEvent("evtInventoryClick", "Inventory Click",  InventoryClickEvent.class, "inventory click");
        EventValues.registerEventValue(InventoryClickEvent.class, ItemStack.class, new Getter<ItemStack, InventoryClickEvent>() {
            @Override
            public ItemStack get(InventoryClickEvent inventoryClickEvent) {
                return inventoryClickEvent.getCurrentItem();
            }
        }, 0);

        registerEvent("evtMove", "Generic Move", PlayerMoveEvent.class, "any movement");

        registerEvent("evtPing", "Server Ping", ServerListPingEvent.class, "[server] [list] ping");

        registerEvent("evtSub", "*Subroutine", EvtCustom.class, RoutineEvent.class, "sub[routine] %string%");

        registerEvent("evtFunc", "*Function", EvtCustom.class, FunctionEvent.class, "func[tion] %string%");

        registerEvent("evtItemProjectileHit", "Item Projectile Hit", ItemProjectileHitEvent.class, "item [projectile] hit");
        EventValues.registerEventValue(ItemProjectileHitEvent.class, ItemStack.class, new Getter<ItemStack, ItemProjectileHitEvent>() {
            @Override
            public ItemStack get(ItemProjectileHitEvent itemProjectileHitEvent) {
                return itemProjectileHitEvent.getProjectile().getItemStack();
            }
        }, 0);
        EventValues.registerEventValue(ItemProjectileHitEvent.class, LivingEntity.class, new Getter<LivingEntity, ItemProjectileHitEvent>() {
            @Override
            public LivingEntity get(ItemProjectileHitEvent itemProjectileHitEvent) {
                return itemProjectileHitEvent.getShooter();
            }
        }, 0);
        EventValues.registerEventValue(ItemProjectileHitEvent.class, Location.class, new Getter<Location, ItemProjectileHitEvent>() {
            @Override
            public Location get(ItemProjectileHitEvent itemProjectileHitEvent) {
                return itemProjectileHitEvent.getProjectile().getLocation();
            }
        }, 0);

        registerEvent("evtBlockLanding", "Falling Block Land", EvtBlockLand.class, EntityChangeBlockEvent.class, "block land");
        EventValues.registerEventValue(EntityChangeBlockEvent.class, ItemStack.class, new Getter<ItemStack, EntityChangeBlockEvent>() {
            @Override
            public ItemStack get(EntityChangeBlockEvent entityChangeBlockEvent) {
                return entityChangeBlockEvent.getEntity() instanceof FallingBlock ? new ItemStack(((FallingBlock) entityChangeBlockEvent.getEntity()).getMaterial()) : null;
            }
        }, 0);
        EventValues.registerEventValue(EntityChangeBlockEvent.class, Entity.class, new Getter<Entity, EntityChangeBlockEvent>() {
            @Override
            public Entity get(EntityChangeBlockEvent entityChangeBlockEvent) {
                return entityChangeBlockEvent.getEntity() instanceof FallingBlock ? entityChangeBlockEvent.getEntity() : null;
            }
        }, 0);

        registerEvent("evtCloseInv", "Close Inventory", InventoryCloseEvent.class, "inventory [window] close");
        EventValues.registerEventValue(InventoryCloseEvent.class, Inventory.class, new Getter<Inventory, InventoryCloseEvent>() {
            @Override
            public Inventory get(InventoryCloseEvent inventoryCloseEvent) {
                return inventoryCloseEvent.getInventory();
            }
        }, 0);
        EventValues.registerEventValue(InventoryCloseEvent.class, Player.class, new Getter<Player, InventoryCloseEvent>() {
            @Override
            public Player get(InventoryCloseEvent inventoryCloseEvent) {
                return inventoryCloseEvent.getPlayer() instanceof Player ? (Player) inventoryCloseEvent.getPlayer() : null;
            }
        }, 0);

        registerEvent("evtVehicleBlockCollide", "Vehicle Collide With Block", VehicleBlockCollisionEvent.class, "vehicle (block collide|collide with block)");
        EventValues.registerEventValue(VehicleBlockCollisionEvent.class, Entity.class, new Getter<Entity, VehicleBlockCollisionEvent>() {
            @Override
            public Entity get(VehicleBlockCollisionEvent vehicleBlockCollisionEvent) {
                return vehicleBlockCollisionEvent.getVehicle();
            }
        }, 0);

        EventValues.registerEventValue(VehicleBlockCollisionEvent.class, Block.class, new Getter<Block, VehicleBlockCollisionEvent>() {
            @Override
            public Block get(VehicleBlockCollisionEvent vehicleBlockCollisionEvent) {
                return vehicleBlockCollisionEvent.getBlock();
            }
        }, 0);

        registerEvent("evtVehicleEntityCollide", "Vehicle Collide With Entity", VehicleBlockCollisionEvent.class, "vehicle (entity collide|collide with entity)");
        EventValues.registerEventValue(VehicleEntityCollisionEvent.class, Entity.class, new Getter<Entity, VehicleEntityCollisionEvent>() {
            @Override
            public Entity get(VehicleEntityCollisionEvent vehicleEntityCollisionEvent) {
                return vehicleEntityCollisionEvent.getVehicle();
            }
        }, 0);

        EventValues.registerEventValue(VehicleEntityCollisionEvent.class, Entity.class, new Getter<Entity, VehicleEntityCollisionEvent>() {
            @Override
            public Entity get(VehicleEntityCollisionEvent vehicleEntityCollisionEvent) {
                return vehicleEntityCollisionEvent.getEntity();
            }
        }, 0);

        registerEvent("evtScriptOptions", "Script Options Header", ScriptOptionsEvent.class, "script options");
    }
}
