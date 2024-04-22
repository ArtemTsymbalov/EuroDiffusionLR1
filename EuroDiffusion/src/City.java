import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class City {
    private final String countryName;
    private final Map<String, Integer> balance;
    private final Map<String, Integer> balancePerDay;
    private List<City> neighbours;
    private boolean full;

    public City(String countryName, List<Country> countriesList) {
        this.countryName = countryName;
        this.balance = new HashMap<>();
        this.balancePerDay = new HashMap<>();
        // Ініціалізація балансу міста для кожної країни
        for (Country cityData : countriesList) {
            this.balance.put(cityData.getName(), 0);
            this.balancePerDay.put(cityData.getName(), 0);
        }
        // Встановлення початкового балансу для країни, до якої належить місто
        this.balance.put(countryName, Config.INITIAL_CITY_BALANCE);
        this.neighbours = null;
        this.full = false;
    }
    // Гететери і сетери
    public String getCountryName() {
        return countryName;
    }
    public List<City> getNeighbours() {
        return neighbours;
    }
    public void setNeighbours(List<City> neighbours) {
        this.neighbours = neighbours;
    }
    public boolean isFull() {
        return full;
    }

    // Передача балансу від цього міста до сусідніх
    public void transferToNeighbours() {
        for (String motif : this.balance.keySet()) {
            int balanceOfMotif = this.balance.get(motif);
            int amountToTransfer = balanceOfMotif / Config.REPRESENTATIVE_PORTION;
            if (amountToTransfer > 0) {
                for (City neighbour : this.neighbours) {
                    this.balance.put(motif, this.balance.get(motif) - amountToTransfer);
                    neighbour.addBalanceInMotif(motif, amountToTransfer);
                }
            }
        }
    }
    // Додавання балансу для певної країни
    public void addBalanceInMotif(String motif, int amount) {
        this.balancePerDay.put(motif, this.balancePerDay.getOrDefault(motif, 0) + amount);
    }
    // Завершення розподілу балансу на кожен день
    public void finalizeBalancePerDay() {
        // Додавання балансу за день до загального балансу
        for (String motif : this.balancePerDay.keySet()) {
            this.balance.put(motif, this.balance.get(motif) + this.balancePerDay.get(motif));
            this.balancePerDay.put(motif, 0);
        }
        // Перевірка, чи місто є повним
        if (!this.full) {
            for (String motif : this.balance.keySet()) {
                if (this.balance.get(motif) == 0) {
                    return;
                }
            }
            this.full = true;
        }
    }
}
