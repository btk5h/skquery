package com.w00tmast3r.skquery.util.packet.particle;

public class ParticleType {

    private final String id;

    public ParticleType(String id) {
        this.id = id;
    }

    public ParticleType(ParticleTypes id) {
        this.id = id.getId();
    }

    public String getId() {
        return id;
    }
}
