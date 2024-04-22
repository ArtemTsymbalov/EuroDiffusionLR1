import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Country implements Comparable<Country>{
    private final String name;
    private final List<City> cities;
    private boolean full;
    private int dayOfFull;
    private final Coordinate lowerLeft;
    private final Coordinate upperRight;

    // Конструктор класу Country
    public Country(String name, Coordinate lowerLeft, Coordinate upperRight) {
        this.name = name;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.cities = new ArrayList<>();
        this.full = false;
        this.dayOfFull = -1;
    }

    // Геттери і сеттери

    public Coordinate getLowerLeft() {
        return lowerLeft;
    }
    public Coordinate getUpperRight() {
        return upperRight;
    }
    public String getName() {
        return name;
    }

    public boolean isFull() {
        return full;
    }

    public int getDayOfFull() {
        return dayOfFull;
    }

    // Додавання міста до списку міст країни
    public void appendCity(City city) {
        cities.add(city);
    }

    // Перевірка на повноту країни
    public void checkFullness(int day) {
        if (full) {
            return;
        }
        for (City city : cities) {
            if (!city.isFull()) {
                return;
            }
        }
        full = true;
        dayOfFull = day;
    }

    public boolean hasForeignNeighbours() {
        for (City city : cities) {
            for (City neighbour : city.getNeighbours()) {
                if (!neighbour.getCountryName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    //Якщо одна країна то обчислення не будуть проводитись
    public void onlyCountryMode() {
        full = true;
        dayOfFull = 0;
    }

    // Метод порівняння для сортування країн за днем повної заповненості та назвою
    public int compareTo(Country other) {
        int compareByDay = Integer.compare(this.dayOfFull, other.dayOfFull);
        if (compareByDay == 0) {
            return this.name.compareTo(other.name);
        }
        return compareByDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return full == country.full && dayOfFull == country.dayOfFull && Objects.equals(name, country.name) && Objects.equals(cities, country.cities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cities, full, dayOfFull);
    }
}
