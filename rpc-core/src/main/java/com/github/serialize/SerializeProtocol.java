package com.github.serialize;

import lombok.Getter;

@Getter
public enum SerializeProtocol {
    JACKSON("jackson");

    private final String serializeProtocol;

    SerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    @Override
    public String toString() {
        return serializeProtocol;
    }

}
