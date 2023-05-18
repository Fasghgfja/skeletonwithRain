package at.qe.skeleton.model;

public enum MeasurementType {
    SOIL_MOISTURE("SOIL_MOISTURE"),
    HUMIDITY("HUMIDITY"),
    AIR_PRESSURE("AIR_PRESSURE"),
    TEMPERATURE("TEMPERATURE"),
    AIR_QUALITY("AIR_QUALITY"),
    LIGHT_INTENSITY("LIGHT_INTENSITY");

    private final String value;

    MeasurementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
