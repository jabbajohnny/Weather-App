import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class App {
    public static void main(String[] args) {

        new GUI();

    }

    public static String getWeatherData(String city) throws Exception{

        String location = "https://community-open-weather-map.p.rapidapi.com/find?q=" + city + "&cnt=1&mode=null&lon=0&type=link%2C%20accurate&lat=0&units=metric";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(location))
                .header("x-rapidapi-key", readConfig())
                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static String getTemperature(String city) throws Exception {
        
        String data = getWeatherData(city);
        
        data = data.substring(data.indexOf("temp"));

        String[] arrayOfParametres = data.split(",");

        data = arrayOfParametres[0];

        double temperature = Double.parseDouble(data.substring(data.indexOf(":")+1));

        data = String.valueOf(temperature);

        return data;
    }

    public static boolean checkIfCityCorrect(String city) throws Exception {

        String data = getWeatherData(city);

        if(data.equals("{\"message\":\"accurate\",\"cod\":\"200\",\"count\":0,\"list\":[]}") || city.equals("") || !data.contains("temp")){
            return false;
        }

        return true;
    }

    public static String changeFormatOfCity(String city){

        int count = city.length();

        StringBuilder builder = new StringBuilder();

        city = city.toLowerCase(Locale.ROOT);

        for(int a = 0; a < count; a++){
            if(city.charAt(a) != 32){
                builder.append(city.charAt(a));
            }
        }

        return builder.toString();
    }

    public static String readConfig() throws IOException {
        FileInputStream input = new FileInputStream("config.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        return reader.readLine();
    }
}
