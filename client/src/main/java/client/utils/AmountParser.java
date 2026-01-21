package client.utils;

public class AmountParser {

    public Double parseAmount(String raw){
        if (raw == null) return null;

        String s = raw.trim();
        if (s.isEmpty()) return null;

        s = s.replace(",", ".");

        if (s.contains("/")){
            String[] parts = s.split("/");
            if (parts.length != 2) return null;
            try {
                double num = Double.parseDouble(parts[0].trim());
                double denom = Double.parseDouble(parts[1].trim());
                if (denom == 0) return null;
                double v = num / denom;
                return v > 0 ? v : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        try {
            double v = Double.parseDouble(s);
            return v > 0 ? v : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
