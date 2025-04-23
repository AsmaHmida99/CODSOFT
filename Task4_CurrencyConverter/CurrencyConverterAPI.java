import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

public class CurrencyConverterAPI {


    private static final String API_URL = "https://open.er-api.com/v6/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.print("Enter base currency (e.g., USD): ");
            String baseCurrency = scanner.next().toUpperCase();


            System.out.print("Enter target currency (e.g., EUR): ");
            String targetCurrency = scanner.next().toUpperCase();


            System.out.print("Enter amount to convert: ");
            double amount = scanner.nextDouble();


            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

            if (exchangeRate != -1) {

                double convertedAmount = amount * exchangeRate;
                System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
            } else {
                System.out.println("Failed to fetch exchange rate. Please check the inputs or try again later.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Méthode pour récupérer le taux de change entre deux devises via l’API.
     *
     * @param baseCurrency   Code de la devise de base.
     * @param targetCurrency Code de la devise cible.
     * @return Le taux de change ou -1 si une erreur s'est produite.
     */
    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {

            String urlString = API_URL + baseCurrency;
            URL url = new URL(urlString);


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();


                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject rates = jsonResponse.getJSONObject("rates");


                if (rates.has(targetCurrency)) {
                    return rates.getDouble(targetCurrency);
                } else {
                    System.out.println("Target currency not found in the API response.");
                    return -1;
                }
            } else {
                System.out.println("API request failed with response code: " + responseCode);
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
            return -1;
        }
    }
}
