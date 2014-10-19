package com.w00tmast3r.skquery.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.sun.rowset.CachedRowSetImpl;
import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.skript.Dynamic;
import com.w00tmast3r.skquery.skript.LambdaCondition;
import com.w00tmast3r.skquery.skript.LambdaEffect;
import com.w00tmast3r.skquery.skript.Markup;
import com.w00tmast3r.skquery.util.ImageUtils;
import com.w00tmast3r.skquery.util.minecraft.JSONMessage;
import com.w00tmast3r.skquery.util.packet.particle.Particle;
import com.w00tmast3r.skquery.util.packet.particle.ParticleType;
import com.w00tmast3r.skquery.util.packet.particle.ParticleTypes;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Types extends AbstractTask {

    private static final Pattern BLOCKDUST = Pattern.compile("block\\s?dust(?:\\s|_)([0-9]+)(?:\\s|_)([0-9]+)");
    private static final Pattern BLOCKCRACK = Pattern.compile("block\\s?crack(?:\\s|_)([0-9]+)(?:\\s|_)([0-9]+)");
    private static final Pattern ICONCRACK = Pattern.compile("icon\\s?crack(?:\\s|_)([0-9]+)");
    private static final Pattern ICONCRACKDATA = Pattern.compile("icon\\s?crack(?:\\s|_)([0-9]+)(?:\\s|_)([0-9]+)");
    private static final Pattern RGB_COLOR = Pattern.compile("([0-9]{1,3})\\s*,\\s*([0-9]{1,3})\\s*,\\s*([0-9]{1,3})");

    @Override
    public void run() {
        Classes.registerClass(new ClassInfo<BufferedImage>(BufferedImage.class, "image")
                .parser(new Parser<BufferedImage>() {
                    @Override
                    public BufferedImage parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(BufferedImage image, int i) {
                        return image.toString();
                    }

                    @Override
                    public String toVariableNameString(BufferedImage image) {
                        return image.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                .serializer(new Serializer<BufferedImage>() {
                    @Override
                    public Fields serialize(BufferedImage image) throws NotSerializableException {
                        Fields f = new Fields();
                        try {
                            f.putObject("image", ImageUtils.imageToByteArray(image));
                            return f;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deserialize(BufferedImage image, Fields fieldContexts) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected BufferedImage deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        byte[] byteArray = (byte[]) fields.getPrimitive("image");
                        try {
                            return ImageUtils.byteArrayToImage(byteArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends BufferedImage> aClass) {
                        return false;
                    }
                }));

        Classes.registerClass(new ClassInfo<JSONMessage>(JSONMessage.class, "jsonmessage")
                .parser(new Parser<JSONMessage>() {
                    @Override
                    public JSONMessage parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(JSONMessage jsonMessage, int i) {
                        return jsonMessage.toOldMessageFormat();
                    }

                    @Override
                    public String toVariableNameString(JSONMessage jsonMessage) {
                        return jsonMessage.toOldMessageFormat();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<FireworkEffect>(FireworkEffect.class, "fireworkeffect")
                .parser(new Parser<FireworkEffect>() {
                    @Override
                    public FireworkEffect parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(FireworkEffect fireworkEffect, int i) {
                        return fireworkEffect.toString();
                    }

                    @Override
                    public String toVariableNameString(FireworkEffect fireworkEffect) {
                        return fireworkEffect.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<ParticleType>(ParticleType.class, "particletype")
                .after("string")
                .before("particletypes")
                .parser(new Parser<ParticleType>() {
                    @Override
                    public ParticleType parse(String s, ParseContext parseContext) {
                        try {
                            return new ParticleType(ParticleTypes.valueOf(s.replace(" ", "_").toUpperCase().trim()));
                        } catch (IllegalArgumentException e) {
                            Matcher blockdust = BLOCKDUST.matcher(s);
                            Matcher blockcrack = BLOCKCRACK.matcher(s);
                            Matcher iconcrack = ICONCRACK.matcher(s);
                            Matcher iconcrackdata = ICONCRACKDATA.matcher(s);
                            if (blockdust.matches()) {
                                return new ParticleType("blockdust_" + blockdust.group(1) + "_" + blockdust.group(2));
                            } else if (blockcrack.matches()) {
                                return new ParticleType("blockcrack_" + blockcrack.group(1) + "_" + blockcrack.group(2));
                            } else if (iconcrack.matches()) {
                                return new ParticleType("iconcrack_" + iconcrack.group(1));
                            } else if (iconcrackdata.matches()) {
                                return new ParticleType("iconcrack_" + iconcrackdata.group(1) + "_" + iconcrackdata.group(2));
                            }
                        }
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toString(ParticleType particleType, int i) {
                        return particleType.toString();
                    }

                    @Override
                    public String toVariableNameString(ParticleType particleType) {
                        return particleType.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<Particle>(Particle.class, "particle")
                .parser(new Parser<Particle>() {
                    @Override
                    public Particle parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(Particle particle, int i) {
                        return particle.toString();
                    }

                    @Override
                    public String toVariableNameString(Particle particle) {
                        return particle.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<Markup>(Markup.class, "markup")
                .parser(new Parser<Markup>() {
                    @Override
                    public Markup parse(String s, ParseContext parseContext) {
                        if (s.charAt(0) == '`' && s.charAt(s.length() - 1) == '`') {
                            return new Markup(s.substring(1, s.length() - 1));
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toString(Markup markup, int i) {
                        return markup.toString();
                    }

                    @Override
                    public String toVariableNameString(Markup markup) {
                        return markup.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                .serializer(new Serializer<Markup>() {
                    @Override
                    public Fields serialize(Markup markup) throws NotSerializableException {
                        Fields f = new Fields();
                        f.putObject("src", markup.toString());
                        return f;
                    }

                    @Override
                    public void deserialize(Markup markup, Fields fieldContexts) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected Markup deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        return new Markup((String) fields.getObject("src"));
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends Markup> aClass) {
                        return false;
                    }
                }));

        Classes.registerClass(new ClassInfo<CachedRowSetImpl>(CachedRowSetImpl.class, "queryresult")
                .parser(new Parser<CachedRowSetImpl>() {
                    @Override
                    public CachedRowSetImpl parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(CachedRowSetImpl rowSet, int i) {
                        return rowSet.toString();
                    }

                    @Override
                    public String toVariableNameString(CachedRowSetImpl rowSet) {
                        return rowSet.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                /*.serializer(new Serializer<ResultSet>() {
                    @Override
                    public Fields serialize(ResultSet resultSet) throws NotSerializableException {
                        return new Fields();
                    }

                    @Override
                    public void deserialize(ResultSet resultSet, Fields fieldContexts) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected ResultSet deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        return null;
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends ResultSet> aClass) {
                        return false;
                    }
                })*/);

        /*
        Classes.registerClass(new ClassInfo<BossBars.BossBarProxy>(BossBars.BossBarProxy.class, "bossbar")
                .parser(new Parser<BossBars.BossBarProxy>() {
                    @Override
                    public BossBars.BossBarProxy parse(String s, ParseContext parseContext) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(BossBars.BossBarProxy bar, int i) {
                        return bar.getText();
                    }

                    @Override
                    public String toVariableNameString(BossBars.BossBarProxy bar) {
                        return bar.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                .serializer(new Serializer<BossBars.BossBarProxy>() {
                    @Override
                    public Fields serialize(BossBars.BossBarProxy bar) throws NotSerializableException {
                        Fields f = new Fields();
                        f.putObject("id", bar.getPlayer().getUniqueId().toString());
                        return f;
                    }

                    @Override
                    public void deserialize(BossBars.BossBarProxy bar, Fields fieldContexts) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected BossBars.BossBarProxy deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        return new BossBars.BossBarProxy(Bukkit.getOfflinePlayer(UUID.fromString((String) fields.getObject("id"))));
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends BossBars.BossBarProxy> aClass) {
                        return false;
                    }
                }));
        */

        Classes.registerClass(new ClassInfo<LambdaCondition>(LambdaCondition.class, "predicate")
                .parser(new Parser<LambdaCondition>() {
                    @Override
                    public LambdaCondition parse(String s, ParseContext parseContext) {
                        if (s.length() > 2 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
                            Condition e = Condition.parse(s.substring(1, s.length() - 1), null);
                            if (e == null) {
                                Skript.error(s + " is not a valid lambda statement.", ErrorQuality.SEMANTIC_ERROR);
                            } else {
                                return new LambdaCondition(e);
                            }
                        }
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toString(LambdaCondition lambdaCondition, int i) {
                        return lambdaCondition.toString();
                    }

                    @Override
                    public String toVariableNameString(LambdaCondition lambdaCondition) {
                        return lambdaCondition.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<LambdaEffect>(LambdaEffect.class, "lambda")
                .parser(new Parser<LambdaEffect>() {
                    @Override
                    public LambdaEffect parse(String s, ParseContext parseContext) {
                        if (s.length() > 3 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
                            if ("void".equals(s.substring(1, s.length() - 1))) return new LambdaEffect(true);
                            Effect e = Effect.parse(s.substring(1, s.length() - 1), null);
                            if (e == null) {
                                Skript.error(s + " is not a valid lambda statement.", ErrorQuality.SEMANTIC_ERROR);
                            } else {
                                return new LambdaEffect(e);
                            }
                        }
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toString(LambdaEffect lambdaEffect, int i) {
                        return lambdaEffect.toString();
                    }

                    @Override
                    public String toVariableNameString(LambdaEffect lambdaEffect) {
                        return lambdaEffect.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                }));

        Classes.registerClass(new ClassInfo<Color>(Color.class, "rgbcolor")
                .parser(new Parser<Color>() {
                    @Override
                    public Color parse(final String s, final ParseContext context) {
                        Matcher m = RGB_COLOR.matcher(s);
                        if (m.matches()) {
                            int r = Integer.parseInt(m.group(1));
                            int g = Integer.parseInt(m.group(2));
                            int b = Integer.parseInt(m.group(3));
                            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) return null;
                            return Color.fromRGB(r, g, b);
                        }
                        ch.njol.skript.util.Color c =  ch.njol.skript.util.Color.byName(s);
                        if (c == null) return null;
                        return c.getBukkitColor();
                    }

                    @Override
                    public String toString(final Color c, final int flags) {
                        return c.toString();
                    }

                    @Override
                    public String toVariableNameString(final Color o) {
                        return o.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                .serializer(new Serializer<Color>() {
                    @Override
                    public Fields serialize(Color o) throws NotSerializableException {
                        Fields f = new Fields();
                        f.putPrimitive("rgb", o.asRGB());
                        return f;
                    }

                    @Override
                    public void deserialize(Color o, Fields f) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected Color deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        return Color.fromRGB(fields.getPrimitive("rgb", int.class));
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends Color> c) {
                        return false;
                    }
                }));

        Classes.registerClass(new ClassInfo<Dynamic>(Dynamic.class, "dynamic")
                .parser(new Parser<Dynamic>() {
                    @Override
                    public Dynamic parse(final String s, final ParseContext context) {
                        return null;
                    }

                    @Override
                    public String toString(final Dynamic d, final int flags) {
                        return d.toString();
                    }

                    @Override
                    public String toVariableNameString(final Dynamic o) {
                        return o.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }
                })
                .serializer(new Serializer<Dynamic>() {
                    @Override
                    public Fields serialize(Dynamic o) throws NotSerializableException {
                        Fields f = new Fields();
                        return f;
                    }

                    @Override
                    public void deserialize(Dynamic o, Fields f) throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    protected Dynamic deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                        return null;
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public boolean canBeInstantiated(Class<? extends Dynamic> c) {
                        return false;
                }
                }));
    }

}
