package hms.hotelserver;

/**
 *
 * @author subin
 */

import java.io.*;
import java.util.*;

public class CustomersService {
    private final String CUSTOMER_FILE = "src/main/resources/customers.txt";
    private final String RES_FILE = "src/main/resources/reservations.txt";

    // 1. 고객 목록 전체 가져오기 
    public String getAllCustomers() {
        StringBuilder sb = new StringBuilder();
        File file = new File(CUSTOMER_FILE);

        if (!file.exists()) {
        return "FAIL|데이터 파일이 없습니다.";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    sb.append(line).append("/");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|파일 읽기 오류";
        }
        if (sb.length() == 0) {
        return "FAIL|저장된 고객 데이터가 없습니다.";
        }
        return "OK|" + sb.toString();
    }

    // 2. 예약 내역 기반 신규 고객 동기화
    public String syncNewCustomers() {
        Set<String> existingCustomers = new HashSet<>();
        List<String> newCustomers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 2) {
                    existingCustomers.add(data[0] + "|" + data[1]);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            return "FAIL|고객 파일 읽기 오류: " + e.getMessage();
        }

        // 예약 파일 읽어서 신규 고객 찾기
        File resFile = new File(RES_FILE);
        if (!resFile.exists()) return "FAIL|예약 파일이 존재하지 않습니다.";

        try (BufferedReader br = new BufferedReader(new FileReader(resFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 3) {
                    String name = data[1];
                    String phone = data[2];
                    String key = name + "|" + phone;

                    if (!existingCustomers.contains(key)) {
                        String newRecord = name + "|" + phone + "|" + "일반" + "|" + "0";
                        
                        newCustomers.add(newRecord);
                        existingCustomers.add(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|예약 파일 읽기 오류";
        }

        // 신규 고객 추가
        if (!newCustomers.isEmpty()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
                for (String customer : newCustomers) {
                    bw.write(customer);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "FAIL|고객 파일 쓰기 오류";
            }
        }
        
        return "SUCCESS";
    }

    // 3. 고객 등급 변경
    public String updateCustomerGrade(String targetName, String targetPhone, String newGrade) {
        List<String> allLines = new ArrayList<>();
        boolean isUpdated = false;

        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) return "FAIL|고객 파일이 없습니다.";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 4) {
                    String name = data[0];
                    String phone = data[1];

                    if (name.equals(targetName) && phone.equals(targetPhone)) {
                        data[2] = newGrade;
                        isUpdated = true;

                        String updatedLine = String.join("|", data);
                        allLines.add(updatedLine);
                    } else {
                        allLines.add(line); // 일치하지 않으면 그대로 유지
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|파일 읽기 오류";
        }

        if (isUpdated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // false: 덮어쓰기
                for (String line : allLines) {
                    bw.write(line);
                    bw.newLine();
                }
                return "SUCCESS";
            } catch (IOException e) {
                e.printStackTrace();
                return "FAIL|파일 쓰기 오류";
            }
        }

        return "FAIL|해당 고객을 찾을 수 없습니다.";
    }
}