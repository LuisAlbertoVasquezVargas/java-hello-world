import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.opendota.com/api/matches/8203479956"))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode players = root.get("players");
        Map<Integer, int[]> heroStats = new HashMap<>();
        
        for (JsonNode player : players) {
            int heroId = player.get("hero_id").asInt();
            int win = player.get("win").asInt();
            int[] stats = heroStats.getOrDefault(heroId, new int[]{0, 0});
            stats[0] += win;
            stats[1] += 1;
            heroStats.put(heroId, stats);
        }
        
        System.out.println("Win Rate per Hero:");
        for (Map.Entry<Integer, int[]> entry : heroStats.entrySet()) {
            int heroId = entry.getKey();
            int wins = entry.getValue()[0];
            int totalMatches = entry.getValue()[1];
            double winRate = (double) wins / totalMatches * 100;
            System.out.printf("Hero %d: %.2f%% (%d win(s) out of %d match(es))%n", heroId, winRate, wins, totalMatches);
        }
    }
}
