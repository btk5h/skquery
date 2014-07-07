package com.w00tmast3r.skquery;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.util.SimpleLiteral;
import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.util.IterableEnumeration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Registration {

    /**
     * Allow skQuery to iterate through your plugin source and reflectively
     * register your classes. This method MUST be called from within your
     * plugin, for safety reasons.
     */
    @CallerSensitive
    public static void enableSnooper() {
        final Class caller = Reflection.getCallerClass();
        final URL callerLocation = caller.getProtectionDomain().getCodeSource().getLocation();
        Bukkit.getLogger().info("[skQuery] Snooping enabled from " + caller.getCanonicalName());
        try {
            File src;
            try {
                src = new File(callerLocation.toURI());
            } catch (URISyntaxException e) {
                src = new File(callerLocation.getPath());
            }
            PluginDescriptionFile desc = SkQuery.getInstance().getPluginLoader().getPluginDescription(src);
            Bukkit.getLogger().info("[skQuery] Locating classes from " + desc.getName() + "...");
            try {
                ArrayList<Class> classes = new ArrayList<Class>();
                JarFile jar = new JarFile(src);
                for (JarEntry e : new IterableEnumeration<JarEntry>(jar.entries())) {
                    if (e.getName().endsWith(".class")) {
                        String className = e.getName().replace('/', '.').substring(0, e.getName().length() - 6);
                        try {
                            Class c = Class.forName(className, false, caller.getClassLoader());
                            if (Effect.class.isAssignableFrom(c)
                                    || Condition.class.isAssignableFrom(c)
                                    || Expression.class.isAssignableFrom(c)
                                    || SimpleExpression.class.isAssignableFrom(c)
                                    || PropertyExpression.class.isAssignableFrom(c)
                                    || SimplePropertyExpression.class.isAssignableFrom(c)
                                    || SimpleLiteral.class.isAssignableFrom(c)
                                    || AbstractTask.class.isAssignableFrom(c)) {
                                classes.add(c);
                            }
                        } catch (ClassNotFoundException error) {
                            error.printStackTrace();
                        } catch (NoClassDefFoundError ignored) {
                        } catch (ExceptionInInitializerError ignored) {}
                    }
                }
                Bukkit.getLogger().info("[skQuery] Finished snooping of " + desc.getName() + " with " + classes.size() + " classes.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
        }
    }
}
