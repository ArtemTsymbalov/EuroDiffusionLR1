import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    // Метод для розділення вхідних даних
    public static List<List<Country>> parseInput() {
        List<List<Country>> cases = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        // Перебір кожного випадку
        for (int i = 0; i < Config.MAX_COUNTRIES_AMOUNT; i++) {
            System.out.print("Enter the number of countries for case " + (i + 1) + ": ");
            int countriesLen = scanner.nextInt();
            scanner.nextLine();
            //Вихід з функції який призводить до завершення програми
            if (countriesLen == 0) {
                break;
            }
            // Валідація кількості країн
            validateNumberOfCountries(countriesLen, i);
            // Зчитування даних країн для поточного випадку
            List<Country> countriesList = readCountriesData(scanner, countriesLen);
            cases.add(countriesList);
        }
        scanner.close();
        return cases;
    }
    private static void validateNumberOfCountries(int countriesLen, int caseNumber) {
        if (countriesLen > Config.MAX_COUNTRIES_AMOUNT || countriesLen < 1) {
            throw new IllegalArgumentException("Error in input for case " + (caseNumber + 1) +
                    ": invalid amount of countries, there must be at least 1 or no more than 20 countries");
        }
    }
    private static List<Country> readCountriesData(Scanner scanner, int countriesLen) {
        List<Country> countriesList = new ArrayList<>();
        for (int j = 0; j < countriesLen; j++) {
            System.out.print("Enter country data for country " + (j + 1) + ": ");
            String line = scanner.nextLine();
            Country parsed = parseCountry(line);
            countriesList.add(parsed);
        }
        return countriesList;
    }

    // Метод для розділення даних країни з рядка
    private static Country parseCountry(String line) {
        String[] args = line.split(" ");
        if (args.length != Config.COUNTRY_DATA_LENGTH) {
            throw new IllegalArgumentException("Error at line {" + line + "}: invalid number of tokens");
        }
        // Валідація назви країни та її координат
        validateCountryName(args[0]);
        validateCountryCoordinates(args);

        int x1 = Integer.parseInt(args[1]);
        int y1 = Integer.parseInt(args[2]);
        int x2 = Integer.parseInt(args[3]);
        int y2 = Integer.parseInt(args[4]);

        return new Country(args[0], new Coordinate(x1, y1), new Coordinate(x2, y2));
    }

    private static void validateCountryName(String name) {
        Pattern namePattern = Pattern.compile("[A-Z][a-z]{1,24}$");
        Matcher matcher = namePattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Error: invalid country name - " + name);
        }
    }

    private static void validateCountryCoordinates(String[] args) {
        for (int i = 1; i < Config.COUNTRY_DATA_LENGTH; i++) {
            int coordinate = Integer.parseInt(args[i]);
            if (coordinate <= 0 || coordinate >= Config.GRID_SIZE+1) {
                throw new IllegalArgumentException("Error: invalid country coordinates - " + args[i]);
            }
        }
    }
}
