package com.w00tmast3r.skquery.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.util.ImageUtils;
import com.w00tmast3r.skquery.util.minecraft.JSONMessage;
import org.bukkit.FireworkEffect;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class Types extends AbstractTask {

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
    }
}
