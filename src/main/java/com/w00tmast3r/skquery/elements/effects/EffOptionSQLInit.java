package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.SkriptParser;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.elements.effects.base.OptionsPragma;

import java.io.File;

@Patterns("$ init <.+>")
public class EffOptionSQLInit extends OptionsPragma {
    @Override
    protected void register(File executingScript, SkriptParser.ParseResult parseResult) {
        try {
            Class.forName(parseResult.regexes.get(0).group());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
