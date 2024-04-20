package org.example;

public class Rating {
    private String source;
    private float value;

    public Rating(String source, String strValue) {
        this.source = source;
        this.value = parseValue(strValue);
    }

    private float parseValue(String strValue) {
        try {
            if (strValue.endsWith("%")) {
                return Float.parseFloat(strValue.replace("%", "")) / 100.0f;
            } else if (strValue.contains("/")) {
                String[] parts = strValue.split("/");
                return Float.parseFloat(parts[0]) / Float.parseFloat(parts[1]);
            }
            return Float.parseFloat(strValue);

        } catch (NumberFormatException e) {
            return -1.0f;
        }
    }

    public String getSource() { return source; }
    public float getValue() { return value; }
    @Override
    public String toString(){
        return source + ": " + value;
    }
}
