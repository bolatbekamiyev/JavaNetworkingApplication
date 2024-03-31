
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class JavaNetworkingApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        //Create URL for Coindesk api
        URL url = new URI("https://api.coindesk.com/v1/bpi/currentprice.json").toURL();

        //Open connection to api
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            //Establish connection
            connection.connect();
        } catch (IOException e) {
            //Throw runtime error, if there is problem with connection
            throw new RuntimeException(e);
        }

        //Check the connection success, if not, throw an error code
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error: " + connection.getResponseCode());
        }

        //Read JSON response from api
        StringBuilder output = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            output.append(scanner.nextLine());
        }
        scanner.close();

        //Parse JSON
        JsonObject object = JsonParser.parseString(output.toString()).getAsJsonObject();

        //Get updated time
        JsonObject time = object.getAsJsonObject("time");
        String updatedTime = time.get("updated").getAsString();

        //Get Bitcoin Price Index
        JsonObject BitcoinPriceIndex = object.getAsJsonObject("bpi");
        //Get USD
        JsonObject usd = BitcoinPriceIndex.getAsJsonObject("USD");
        double usdRate = usd.get("rate_float").getAsDouble();
        //Get GBP
        JsonObject gbp = BitcoinPriceIndex.getAsJsonObject("GBP");
        double gbpRate = gbp.get("rate_float").getAsDouble();
        //Get EUR
        JsonObject eur = BitcoinPriceIndex.getAsJsonObject("EUR");
        double eurRate = eur.get("rate_float").getAsDouble();

        //Print Bitcoin rates and updated time
        System.out.printf("Current Bitcoin Value (%s)\n", updatedTime);
        System.out.printf("US Dollar:\t\t\t\t %#,.2f\n", usdRate);
        System.out.printf("British Pounds:\t\t\t %#,.2f\n", gbpRate);
        System.out.printf("Euros:\t\t\t\t\t %#,.2f\n", eurRate);
    }
}