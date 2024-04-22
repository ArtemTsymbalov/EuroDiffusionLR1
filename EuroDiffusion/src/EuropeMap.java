import java.util.ArrayList;
import java.util.List;

public class EuropeMap {
    private final List<Country> countries;
    private final City[][] grid;
    // Конструктор класу, ініціалізує карту Європи з заданими даними країн
    public EuropeMap(List<Country> countriesData) {
        this.countries = new ArrayList<>();
        this.grid = new City[Config.GRID_SIZE + 2][Config.GRID_SIZE + 2];
        // Ініціалізація міст
        initializeGrid(countriesData);
        // Перевірка наявності зв'язків між країнам
        validateForeignNeighbours();
    }
    // Метод для ініціалізації матриці міст та їхніх сусідів на карті
    private void initializeGrid(List<Country> countriesData) {
        createCitiesInCountries(countriesData);
        setNeighboursForCities();
    }
    // Метод для перевірки наявності зв'язків між країнами
    private void validateForeignNeighbours() {
        if (countries.size() <= 1) {
            return;
        }
        for (Country country : countries) {
            if (!country.hasForeignNeighbours()) {
                throw new RuntimeException(country.getName() + " has no connection with other countries");
            }
        }
    }
    // Метод для створення міст у кожній країні
    private void createCitiesInCountries(List<Country> countriesData) {
        for (Country countryData : countriesData) {
            Country country = new Country(countryData.getName(), countryData.getLowerLeft(), countryData.getUpperRight());
            for (int x = country.getLowerLeft().x(); x <= countryData.getUpperRight().x(); x++) {
                for (int y = countryData.getLowerLeft().y(); y <= countryData.getUpperRight().y(); y++) {
                    City city = new City(country.getName(), countriesData);
                    grid[x][y] = city;
                    country.appendCity(city);
                }
            }
            countries.add(country);
        }
    }

    // Метод для встановлення сусідів для кожного міста
    private void setNeighboursForCities() {
        for (int x = 0; x <= Config.GRID_SIZE; x++) {
            for (int y = 0; y <= Config.GRID_SIZE; y++) {
                if (grid[x][y] != null) {
                    List<City> neighboursList = getNeighbours(x, y);
                    grid[x][y].setNeighbours(neighboursList);
                }
            }
        }
    }

    // Геттери
    public List<Country> getCountries() {
        return countries;
    }
    private List<City> getNeighbours(int x, int y) {
        List<City> neighbours = new ArrayList<>();
        if (grid[x][y + 1] != null) {
            neighbours.add(grid[x][y + 1]);
        }
        if (grid[x][y - 1] != null) {
            neighbours.add(grid[x][y - 1]);
        }
        if (grid[x + 1][y] != null) {
            neighbours.add(grid[x + 1][y]);
        }
        if (grid[x - 1][y] != null) {
            neighbours.add(grid[x - 1][y]);
        }
        return neighbours;
    }

    // Метод для симуляції розподілу ресурсів
    public void simulateEuroDiffusion() {
        // Якщо на карті знаходиться тільки одна країна, встановлюємо її як заповнену
        if (countries.size() == 1) {
            countries.getFirst().onlyCountryMode();
            return;
        }
        int day = 1;
        // Поки всі країни не будуть заповнені ресурсами, продовжуємо симуляцію
        while (!allCountriesFull()) {
            processCities();// Обробляємо розподіл ресурсів у містах
            updateCountries(day);// Оновлюємо стан країн на кінець дня
            day++;
        }
        countries.sort(null);// Сортуємо країни за днем, коли вони стали повністю заповненими
    }
    // Метод для перевірки, чи всі країни на карті заповнені ресурсами
    private boolean allCountriesFull() {
        for (Country country : countries) {
            if (!country.isFull()) {
                return false;
            }
        }
        return true;
    }
    // Метод для обробки розподілу ресурсів у містах на карті
    private void processCities() {
        // Проходимося по кожній клітинці на карті
        for (int x = 0; x <= Config.GRID_SIZE; x++) {
            for (int y = 0; y <= Config.GRID_SIZE; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].transferToNeighbours();
                }
            }
        }
        // Завершення розподілу ресурсів на кінець дня у кожному місті
        for (int x = 0; x <= Config.GRID_SIZE; x++) {
            for (int y = 0; y <= Config.GRID_SIZE; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].finalizeBalancePerDay();
                }
            }
        }
    }
    // Метод для оновлення стану країн на кінець дня
    private void updateCountries(int day) {
        for (Country country : countries) {
            country.checkFullness(day);
        }
    }

}
