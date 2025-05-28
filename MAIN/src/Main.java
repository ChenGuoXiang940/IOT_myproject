import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static long waitTime = 1;// 等待時間，單位為分鐘溫度警示閾值
    public static long count = 0;// 請求計數器
    public static void main(String[] args) {
        while (true) {
            try {
                URL url = new URL("http://localhost:8000/temperatures");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONArray temps = json.getJSONArray("temperatures");
                String timestamp = json.getString("timestamp");
                double sum = 0;
                double max = Double.MIN_VALUE;
                double min = Double.MAX_VALUE;

                System.out.println("感測器溫度資料：");
                for (int i = 0; i < temps.length(); i++) {
                    double temp = temps.getDouble(i);
                    System.out.printf("感測器 #%d: %.2f°C", i + 1, temp);
                    sum += temp;
                    max = Math.max(max, temp);
                    min = Math.min(min, temp);
                    if (temp > 35) {
                        System.out.printf("⚠️ 警示：溫度超過 35°C！");
                    }
                    System.out.println();
                }
                System.out.printf("統計結果：%n");
                System.out.printf("獲取時間: %s%n",timestamp);
                System.out.printf("平均溫度: %.2f°C%n", sum / temps.length());
                System.out.printf("最高溫度: %.2f°C%n", max);
                System.out.printf("最低溫度: %.2f°C%n", min);
                System.out.printf("請求計數: %d%n", ++count);
                // Wait for the specified interval before the next request
                Thread.sleep(waitTime * 60 * 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
