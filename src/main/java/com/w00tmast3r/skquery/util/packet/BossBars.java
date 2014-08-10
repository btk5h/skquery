package com.w00tmast3r.skquery.util.packet;

import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.*;

public class BossBars implements Listener {

    public static class BossBarProxy {

        private final UUID playerReference;

        public BossBarProxy(OfflinePlayer p) {
            playerReference = p.getUniqueId();
        }

        public Player getPlayer() {
            return Bukkit.getPlayer(playerReference);
        }

        public void removeBar() {
            removeStatusBar(getPlayer());
        }

        public void setText(String text) {
            setStatusBar(getPlayer(), text, getPercent());
        }

        public String getText() {
            return DRAGONS.containsKey(getPlayer()) ? DRAGONS.get(getPlayer()).name : "";
        }

        public void setPercent(float percent) {
            if (percent == 0) removeBar();
            else setStatusBar(getPlayer(), getText(), percent);
        }

        public float getPercent() {
            return DRAGONS.containsKey(getPlayer()) ? DRAGONS.get(getPlayer()).health / FakeDragon.MAX_HEALTH : 0;
        }

    }

    public static BossBars instance = null;

    static {
        if (instance == null) {
            instance = new BossBars();
            Bukkit.getPluginManager().registerEvents(instance, SkQuery.getInstance());
        }
    }

    private BossBars() {
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeStatusBar(event.getPlayer());
    }


    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeStatusBar(event.getPlayer());
    }

    private static PlayerMap<FakeDragon> DRAGONS = new PlayerMap<FakeDragon>();

    /**
     * Checks to see if the player is currently being displayed a status bar via fake Ender Dragon.
     * <br><br>
     * This may sometimes return a false positive.  Specifically, if a player is sent a fake dragon, and
     * subsequently logs off and back on and the bar is not restored, the record of the dragon will remain
     * here even though the client no longer has the entity.  To avoid this, be sure to remove the bar
     * manually using {@link #removeStatusBar(Player)} when the player leaves the server
     * ({@link org.bukkit.event.player.PlayerQuitEvent} and {@link org.bukkit.event.player.PlayerKickEvent})
     *
     * @param player a player
     * @return true if this API has a record of the player being sent a bar
     */
    public static boolean hasStatusBar(Player player){
        return DRAGONS.containsKey(player) && DRAGONS.get(player) != null;
    }

    /**
     * Removes a player's status bar by destroying their fake dragon (if they have one).
     *
     * @param player a player
     */
    public static void removeStatusBar(Player player){
        if(hasStatusBar(player)){
            Reflection.sendPacket(DRAGONS.get(player).getDestroyPacket(), player);
            DRAGONS.remove(player);
        }
    }

    /**
     * Sets a player's status bar to display a specific message and fill amount.  The fill amount is in
     * decimal percent (i.e. 1 = 100%, 0 = 0%, 0.5 = 50%, 0.775 = 77.5%, etc.).
     * <br><br>
     * <code>text</code> is limited to 64 characters, and <code>percent</code> must be greater than zero
     * and less than or equal to one.  If either argument is outside its constraints, it will be quietly
     * trimmed to match.
     *
     * @param player a player
     * @param text some text with 64 characters or less
     * @param percent a decimal percent in the range (0,1]
     */
    public static void setStatusBar(Player player, String text, float percent) {

        FakeDragon dragon = DRAGONS.containsKey(player) ? DRAGONS.get(player) : null;

        if(text.length() > 64)
            text = text.substring(0, 63);
        if(percent > 1.0f)
            percent = 1.0f;
        if(percent < 0.05f)
            percent = 0.05f;

        if (text.isEmpty() && dragon != null)
            removeStatusBar(player);

        if (dragon == null) {
            dragon = new FakeDragon(player.getLocation().add(0, -200, 0), text, percent);
            Reflection.sendPacket(dragon.getSpawnPacket(), player);
            DRAGONS.put(player, dragon);
        } else {
            dragon.setName(text);
            dragon.setHealth(percent);
            Reflection.sendPacket(dragon.getMetaPacket(dragon.getWatcher()), player);
            Reflection.sendPacket(dragon.getTeleportPacket(player.getLocation().add(0, -200, 0)), player);
        }

    }

    /**
     * Removes the status bar for all players on the server.  See {@link #removeStatusBar(Player)}.
     */
    public static void removeAllStatusBars(){
        for(Player each : Bukkit.getOnlinePlayers())
            removeStatusBar(each);
    }

    /**
     * Sets the status bar for all players on the server.  See {@link #setStatusBar(Player, String, float)}.
     * @param text some text with 64 characters or less
     * @param percent a decimal percent in the range (0,1]
     */
    public static void setAllStatusBars(String text, float percent){
        for(Player each : Bukkit.getOnlinePlayers())
            setStatusBar(each, text, percent);
    }

    private static class FakeDragon {

        private static final int MAX_HEALTH = 200;
        private int id;
        private int x;
        private int y;
        private int z;
        private int pitch = 0;
        private int yaw = 0;
        private byte xV = 0;
        private byte yV = 0;
        private byte zV = 0;
        private float health;
        private boolean visible = false;
        private String name;
        private Object world;
        private Object dragon;

        public FakeDragon(Location loc, String name, float percent) {
            this.name = name;
            this.x = loc.getBlockX();
            this.y = loc.getBlockY();
            this.z = loc.getBlockZ();
            this.health = percent * MAX_HEALTH;
            this.world = Reflection.getHandle(loc.getWorld());
        }

        public void setHealth(float percent) {
            this.health = percent / MAX_HEALTH;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getSpawnPacket() {
            Class<?> Entity = Reflection.nmsClass("Entity");
            Class<?> EntityLiving = Reflection.nmsClass("EntityLiving");
            Class<?> EntityEnderDragon = Reflection.nmsClass("EntityEnderDragon");

            try{
                dragon = EntityEnderDragon.getConstructor(Reflection.nmsClass("World")).newInstance(world);

                Reflection.getMethod(EntityEnderDragon, "setLocation", double.class, double.class, double.class, float.class, float.class).invoke(dragon, x, y, z, pitch, yaw);
                Reflection.getMethod(EntityEnderDragon, "setInvisible", boolean.class).invoke(dragon, visible);
                Reflection.getMethod(EntityEnderDragon, "setCustomName", String.class ).invoke(dragon, name);
                Reflection.getMethod(EntityEnderDragon, "setHealth", float.class).invoke(dragon, health);

                Reflection.getField(Entity, "motX").set(dragon, xV);
                Reflection.getField(Entity, "motY").set(dragon, yV);
                Reflection.getField(Entity, "motZ").set(dragon, zV);

                this.id = (Integer) Reflection.getMethod(EntityEnderDragon, "getId").invoke(dragon);

                Class<?> packetClass = Reflection.nmsClass("PacketPlayOutSpawnEntityLiving");
                return packetClass.getConstructor(EntityLiving).newInstance(dragon);
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        public Object getDestroyPacket(){
            try{
                Class<?> packetClass = Reflection.nmsClass("PacketPlayOutEntityDestroy");
                return packetClass.getConstructor(int[].class).newInstance(new Object[]{new int[]{id}});
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        public Object getMetaPacket(Object watcher){
            try{
                Class<?> watcherClass = Reflection.nmsClass("DataWatcher");
                Class<?> packetClass = Reflection.nmsClass("PacketPlayOutEntityMetadata");
                return packetClass.getConstructor(int.class, watcherClass, boolean.class).newInstance(id, watcher, true);
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        public Object getTeleportPacket(Location loc){
            try{
                Class<?> packetClass = Reflection.nmsClass("PacketPlayOutEntityTeleport");
                return packetClass.getConstructor(int.class, int.class, int.class, int.class, byte.class, byte.class).newInstance(
                        this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360));
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        public Object getWatcher(){
            Class<?> Entity = Reflection.nmsClass("Entity");
            Class<?> DataWatcher = Reflection.nmsClass("DataWatcher");

            try{
                Object watcher = DataWatcher.getConstructor(Entity).newInstance(dragon);
                Method a = Reflection.getMethod(DataWatcher, "a", int.class, Object.class);
                a.invoke(watcher, 0, visible ? (byte) 0 : (byte) 0x20);
                a.invoke(watcher, 6, (Float) health);
                a.invoke(watcher, 7, (Integer) 0);
                a.invoke(watcher, 8, (Byte) (byte) 0);
                a.invoke(watcher, 10, name);
                a.invoke(watcher, 11, (Byte) (byte) 1);
                return watcher;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

    }

    /**
     * A class that attempts to overcome the traditional aversion to using org.bukkit.entity.Player as
     * the key type for a Map, namely that Player references can be quite large and we don't want to
     * keep them around after they're gone unless necessary.
     * <br><br>
     * This class is externally typed with {@link org.bukkit.entity.Player} as the key type, but internally
     * uses {@link java.lang.String} as the key type, using the player's name.
     * <br><br>
     * In addition to this memory-saving measure, this map also allows the contents to be accessed through
     * either the player's name or the player object itself, meaning no more hassle with {@link Player#getName()}
     * or {@link Bukkit#getPlayer(String)} when you want to pull out of a map.
     *
     * @author AmoebaMan
     *
     * @param <V> whatever you want to store
     */
    private static class PlayerMap<V> implements Map<Player, V>{

        private final V defaultValue;
        private final Map<String, V> contents;

        public PlayerMap(){
            contents = new HashMap<String, V>();
            defaultValue = null;
        }

        public void clear() {
            contents.clear();
        }

        public boolean containsKey(Object key) {
            if(key instanceof Player)
                return contents.containsKey(((Player) key).getName());
            return key instanceof String && contents.containsKey(key);
        }

        public boolean containsValue(Object value){
            return contents.containsValue(value);
        }

        @Nonnull
        public Set<Entry<Player, V>> entrySet() {
            Set<Entry<Player, V>> toReturn = new HashSet<Entry<Player, V>>();
            for(String name : contents.keySet())
                toReturn.add(new PlayerEntry(Bukkit.getPlayer(name), contents.get(name)));
            return toReturn;
        }

        public V get(Object key) {
            V result = null;
            if(key instanceof Player)
                result = contents.get(((Player) key).getName());
            if(key instanceof String)
                result = contents.get(key);
            return (result == null) ? defaultValue : result;
        }

        public boolean isEmpty(){
            return contents.isEmpty();
        }

        @Nonnull
        public Set<Player> keySet(){
            Set<Player> toReturn = new HashSet<Player>();
            for(String name : contents.keySet())
                toReturn.add(Bukkit.getPlayer(name));
            return toReturn;
        }

        public V put(Player key, V value) {
            if(key == null)
                return null;
            return contents.put(key.getName(), value);
        }

        public void putAll(@Nonnull Map<? extends Player, ? extends V> map) {
            for(Entry<? extends Player, ? extends V> entry : map.entrySet())
                put(entry.getKey(), entry.getValue());
        }

        public V remove(Object key) {
            if(key instanceof Player)
                return contents.remove(((Player) key).getName());
            if(key instanceof String)
                return contents.remove(key);
            return null;
        }

        public int size() {
            return contents.size();
        }

        @Nonnull
        public Collection<V> values() {
            return contents.values();
        }

        public String toString(){
            return contents.toString();
        }

        public class PlayerEntry implements Map.Entry<Player, V>{

            private Player key;
            private V value;

            public PlayerEntry(Player key, V value){
                this.key = key;
                this.value = value;
            }

            public Player getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }

            public V setValue(V value) {
                V toReturn = this.value;
                this.value = value;
                return toReturn;
            }
        }
    }
}