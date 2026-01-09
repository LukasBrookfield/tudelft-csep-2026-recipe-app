package client.utils;

public class ScaleFactorParser {

    public double parse(String raw) {
        if (raw == null) return 1.0;

        String s = raw.trim();
        if (s.isEmpty()) return 1.0;

        // if someone writes "x" too
        if (s.endsWith("x") || s.endsWith("X") || s.endsWith("×")) {
            s = s.substring(0, s.length() - 1).trim();
        }

        s = s.replace(",", ".");

        // if someone writes as a percentage
        if (s.endsWith("%")) {
            String p = s.substring(0, s.length() - 1).trim().trim();
            try {
                double v = Double.parseDouble(p) / 100.0;
                return v > 0 ? v : 1.0;
            } catch (NumberFormatException e) {
                return 1.0;
            }
        }

        // if someone writes a fraction
        if (s.contains("/")) {
            String[] parts = s.split("/");
            if (parts.length != 2) return 1.0;
            try {
                double num = Double.parseDouble(parts[0].trim());
                double denom = Double.parseDouble(parts[1].trim());
                double v = num / denom;
                return v > 0 ? v : 1.0;
            } catch (NumberFormatException e) {
                return 1.0;
            }
        }
        try {
            double value = Double.parseDouble(s);
            if (value <= 0) return 1.0;
            return value;
        } catch (NumberFormatException e) {
            return 1.0;
        }
    }
}