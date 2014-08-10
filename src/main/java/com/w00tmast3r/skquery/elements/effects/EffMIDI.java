package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Description;
import com.w00tmast3r.skquery.api.Examples;
import com.w00tmast3r.skquery.api.Name;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.custom.note.MidiUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

@Name("Play MIDI")
@Description("Plays a file with the extention .mid to a player.")
@Examples("on join:;->play midi \"login\" to player")
@Patterns("play midi %string% to %players%")
public class EffMIDI extends Effect {

    private Expression<String> midi;
    private Expression<Player> players;

    @Override
    protected void execute(Event event) {
        String m = midi.getSingle(event);
        if(m == null) return;
        File f = new File(Skript.getInstance().getDataFolder().getAbsolutePath() + File.separator + Skript.SCRIPTSFOLDER + File.separator + m + ".mid");
        HashSet<Player> pList = new HashSet<Player>();
        if (f.exists()) {
            Collections.addAll(pList, players.getAll(event));
            MidiUtil.playMidiQuietly(f, pList);
        } else {
            Bukkit.getLogger().warning("Could not find midi file " + m + ".mid in the scripts folder");
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "midi";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        midi = (Expression<String>) expressions[0];
        players = (Expression<Player>) expressions[1];
        return true;
    }
}
