package org.freewheelschedule.freewheel.common.model;

public enum TriggerType {
    BASIC("BASIC"),
    TIMED("TIMED"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    RESOURCE("RESOURCE");

    private final String value;

    public String getValue() {
        return value;
    }

    TriggerType(String value) {
        this.value = value;
    }
}
