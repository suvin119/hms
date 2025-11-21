package hms.hotelserver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceServer {
    private static final String SERVICE_FILE = "src/main/resources/service_usage.txt";

    public String addServiceUsage(String reservationId, String serviceId, String quantity, String cost) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SERVICE_FILE, true))) {
            String line = reservationId + "|" + serviceId + "|" + quantity + "|" + cost;
            bw.write(line);
            bw.newLine();
            return "SUCCESS|서비스 추가 완료";
        } catch (IOException e) { e.printStackTrace(); return "FAIL|서비스 추가 실패"; }
    }

    public int getTotalServiceCost(String reservationId) {
        File file = new File(SERVICE_FILE);
        if (!file.exists()) return 0;

        int total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p[0].equals(reservationId)) total += Integer.parseInt(p[3]);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return total;
    }
}
