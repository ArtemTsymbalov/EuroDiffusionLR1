import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Отримання списку випадків з вхідних даних
        List<List<Country>> cases;
        cases = InputParser.parseInput();
        // Перебір кожного випадку
        for (int i = 0; i < cases.size(); i++) {
            System.out.println("\nCase Number " + (i + 1));
            List<Country> countriesList = cases.get(i);
                // Створення карти Європи для поточного випадку та виконання симуляції
                EuropeMap europeMap = new EuropeMap(countriesList);
                europeMap.simulateEuroDiffusion();
                // Виведення стану кожної країни після симуляції
                for (Country country : europeMap.getCountries()) {
                    System.out.println(country.getName() + " " + country.getDayOfFull());
                }
        }
    }
}
