import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

	
	public static void main(String[] args) {
		

		List<InputParser.Scenario> scenarios = InputParser.parseInputFile("inputfrk.txt");
	    runGreedyAlgorithm(scenarios);
	
		
	}
///////////////////////////////////////////	
	public static double calculateCost(Cashier c, String type, int baseCost) {
	    if (c.tasks.isEmpty()) return 0.0;

	    double cost = 0.0;

	    // 1. Base Switching Cost
	    if (!type.equals(c.lastType)) {
	        cost += baseCost;
	    }

	    // 2. Cost Inflation
	    int inflationSteps = c.totalCustomers / 5;
	    cost += inflationSteps;

	    // 3. Exhaustion Rule (arka arkaya aynı türün 3.sü)
	    if (type.equals(c.lastType) && c.sameTypeCount == 2) {
	        cost *= 1.5;
	    }

	    // 4. Learning Curve
	    try {
	        int current = Integer.parseInt(type.replaceAll("[^0-9]", ""));
	        for (String past : c.typeSet) {
	            int pastType = Integer.parseInt(past.replaceAll("[^0-9]", ""));
	            if (pastType > current) {
	                cost *= 0.8;
	                break;
	            }
	        }
	    } catch (NumberFormatException e) {
	        // ignore
	    }

	    return cost;
	}

///////////////////////
	public static void runGreedyAlgorithm(List<InputParser.Scenario> scenarios) {
	    int scenarioNumber = 1;

	    // Çıktılar burada toplanacak
	    List<String> results = new ArrayList<>();

	    for (InputParser.Scenario scenario : scenarios) {
	        System.out.println("Scenario " + scenarioNumber + ":");

	        List<Cashier> cashiers = new ArrayList<>();
	        for (int i = 0; i < scenario.cashierCount; i++) {
	            cashiers.add(new Cashier());
	        }

	        boolean allAssigned = true;

	        for (String transaction : scenario.transactions) {
	            boolean assigned = assignTransactionToBestCashier(
	                    cashiers, transaction, scenario.baseCost, scenario.maxTypes
	            );

	            if (!assigned) {
	                results.add(String.format("%.2f", -1.00));
	                allAssigned = false;
	                break;
	            }
	        }

	        if (allAssigned) {
	            double totalCost = 0.0;
	            for (Cashier c : cashiers) {
	                totalCost += c.totalCost;
	            }
	            results.add(String.format("%.2f", totalCost));
	        }

	        scenarioNumber++;
	    }

	    // Çıktıyı dosyaya yaz
	    try (PrintWriter writer = new PrintWriter("output.txt")) {
	        for (String line : results) {
	            writer.println(line);
	        }
	    } catch (IOException e) {
	        System.err.println("Çıktı dosyasına yazılamadı: " + e.getMessage());
	    }
	}


////////////////////////////////
	
	public static boolean assignTransactionToBestCashier(
	        List<Cashier> cashiers,
	        String transactionType,
	        int baseCost,
	        int maxTypes
	) {
	    double minCost = Double.MAX_VALUE;
	    Cashier bestCashier = null;

	    System.out.println("İşlem: " + transactionType); // <-- Her işlem tipi için log başlığı

	    for (Cashier cashier : cashiers) {

	        // Tür limiti kontrolü
	        if (!cashier.typeSet.contains(transactionType)
	                && cashier.getTransactionTypeCount() >= maxTypes) {
	            System.out.println("  Kasiyer atlanıyor (fazla tür): " + cashier.typeSet);
	            continue;
	        }

	        // Maliyet hesapla
	        double cost = calculateCost(cashier, transactionType, baseCost);

	        // 🔍 LOG: Bu kasiyer için maliyet detayı
	        System.out.printf("  Kasiyer görevleri: %-40s | Son Tür: %-8s | Maliyet: %.2f\n",
	                cashier.tasks.toString(),
	                cashier.lastType,
	                cost);

	        if (cost < minCost) {
	            minCost = cost;
	            bestCashier = cashier;
	        }
	    }

	    System.out.println("  Seçilen kasiyer: " + (bestCashier != null ? bestCashier.tasks.toString() : "YOK"));
	    System.out.println("---------------------------------------------------");

	    // Uygun kasiyer bulunamadıysa
	    if (bestCashier == null) {
	        return false; // maliyet -1
	    }

	    // Görevi atama
	    bestCashier.addTransaction(transactionType, minCost);
	    return true;
	}
	
}