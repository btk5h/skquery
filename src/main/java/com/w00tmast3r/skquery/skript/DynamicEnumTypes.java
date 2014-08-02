package com.w00tmast3r.skquery.skript;

import ch.njol.skript.Skript;
import com.w00tmast3r.skquery.elements.expressions.ExprKeyString;
import org.bukkit.Bukkit;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;

public class DynamicEnumTypes {

    public static void register() {
        try {
            File dir = new File(Skript.getInstance().getDataFolder().getAbsolutePath() + File.separator + Skript.SCRIPTSFOLDER);
            assert dir.exists();
            for (File file : dir.listFiles()) {
                if (file != null && !file.isDirectory() && file.getName().substring(file.getName().lastIndexOf('.') + 1).equalsIgnoreCase("skt") ) {
                    String className = ExprKeyString.getKey(10, "a-zA-Z");
                    File out = new File(Skript.getInstance().getDataFolder().getParent(), className + ".jcskt");
                    out.createNewFile();
                    out.deleteOnExit();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(out));
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    writer.write("package com.w00tmast3r.skquery.content;");
                    writer.newLine();
                    writer.write("public enum " + className + " {");
                    writer.newLine();
                    String typeName = null;
                    boolean isFirstNode = true;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (typeName == null) {
                            typeName = line;
                        } else if (isFirstNode) {
                            writer.write(line.trim().toUpperCase().replaceAll(" ", "_"));
                            isFirstNode = false;
                        } else {
                            writer.write(", " + line.trim().toUpperCase().replaceAll(" ", "_"));
                        }
                    }
                    reader.close();
                    writer.newLine();
                    writer.write("}");
                    writer.flush();
                    writer.close();
                    compileAndRegister(out, className, typeName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compileAndRegister(File source, String className, String typeName) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            Bukkit.getLogger().severe("[skQuery] Could not find a tools.jar! Make sure you have the JDK installed.");
            return;
        }
        compiler.run(null, null, null, source.getPath());
        try {
            add(Class.forName("com.w00tmast3r.skquery.content." + className), typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void add(Class c, String typeName) {
        EnumClassInfo.create(c, typeName).register();
    }
}
