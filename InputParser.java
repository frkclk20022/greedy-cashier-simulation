import java.io.*;
import java.util.*;

public class InputParser {

    // Senaryo nesnesi tanımı
    public static class Scenario {
        int baseCost;
        int cashierCount;
        int maxTypes;
        List<String> transactions;

        public Scenario(int baseCost, int cashierCount, int maxTypes, List<String> transactions) {
            this.baseCost = baseCost;
            this.cashierCount = cashierCount;
            this.maxTypes = maxTypes;
            this.transactions = transactions;
        }
    }

    public static List<Scenario> parseInputFile(String dosyaAdi) {
        List<Scenario> scenarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            List<String> allLines = new ArrayList<>();

            // Dosyadaki tüm satırları oku
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    allLines.add(line.trim());
                }
            }

            // Her 4 satır bir senaryoyu oluşturur
            for (int i = 0; i + 3 < allLines.size(); i += 4) {
                int baseCost = Integer.parseInt(allLines.get(i));
                int cashierCount = Integer.parseInt(allLines.get(i + 1));
                int maxTypes = Integer.parseInt(allLines.get(i + 2));

                String[] parts = allLines.get(i + 3).split(",\\s*");
                List<String> transactions = Arrays.asList(parts);

                Scenario scenario = new Scenario(baseCost, cashierCount, maxTypes, transactions);
                scenarios.add(scenario);
            }

        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        return scenarios;
    }
}