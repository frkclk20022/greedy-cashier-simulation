import java.util.*;

public class Cashier {
    public List<String> tasks;        // Yapılan işlemler sıralı
    public Set<String> typeSet;       // Kaç farklı işlem türü yaptı
    public int totalCustomers;        // Kaç müşteriye baktı
    public double totalCost;          // Toplam maliyet
    public String lastType;           // Son işlem türü
    public int sameTypeCount;         // Aynı işlem türünü arka arkaya kaç kez yaptı

    public Cashier() {
        this.tasks = new ArrayList<>();
        this.typeSet = new HashSet<>();
        this.totalCustomers = 0;
        this.totalCost = 0.0;
        this.lastType = null;
        this.sameTypeCount = 0;
    }

    // Yeni bir işlem eklediğimizde çağrılacak
    public void addTransaction(String type, double cost) {
        tasks.add(type);
        typeSet.add(type);
        totalCustomers++;
        totalCost += cost;

        if (type.equals(lastType)) {
            sameTypeCount++;
        } else {
            sameTypeCount = 1;
            lastType = type;
        }
    }

    // Yardımcı fonksiyon: kaç farklı tür işlemi oldu
    public int getTransactionTypeCount() {
        return typeSet.size();
    }
}