package hms.hotelserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckOutService {
    private static final String ROOM_FILE = "src/main/resources/rooms.txt";
    private static final String RES_FILE = "src/main/resources/reservations.txt";
    private static final String SERVICE_FILE = "src/main/resources/service_usage.txt";
    private ServiceServer serviceServer = new ServiceServer();
    
    public static String checkoutRoom(int roomId) {
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        // 1. 파일 읽기, 수정할 내용 메모리에 저장
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                // 데이터 형식이 맞고, 방 번호가 일치하면 수정
                if (parts.length >= 4 && Integer.parseInt(parts[0]) == roomId) {
                    String newLine = parts[0] + "|" + parts[1] + "|" + parts[2] + "|사용가능";
                    fileContent.add(newLine);
                    found = true;
                } else {
                    fileContent.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("[Server] 파일 읽기 실패: " + e.getMessage());
            return "FAIL|서버 파일 읽기 오류";
        }

        // 해당 방 번호를 못 찾은 경우
        if (!found) {
            return "FAIL|해당 방 번호를 찾을 수 없음";
        }

        // 2. 파일 덮어쓰기
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOM_FILE))) {
            for (String line : fileContent) {
                pw.println(line);
            }
            
            
        } catch (IOException e) {
            System.out.println("[Server] 파일 쓰기 실패: " + e.getMessage());
            return "FAIL|서버 파일 쓰기 오류";
        }
        
        deleteServiceRecords(roomId);
        return "OK|Checked Out"; 
    }
    
    // 체크아웃 후에 service_usage 삭제
    private static void deleteServiceRecords(int roomId) {
        File sFile = new File(SERVICE_FILE);
        if (!sFile.exists()) return;

        List<String> serviceContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(sFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    try {
                        int rId = Integer.parseInt(parts[0]);
                        // 방 번호가 일치하지 않는 데이터만 남김
                        if (rId != roomId) {
                            serviceContent.add(line);
                        }
                    } catch (NumberFormatException e) {
                        serviceContent.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[Server] 서비스 파일 읽기 실패: " + e.getMessage());
            return;
        }

        // 서비스 파일 덮어쓰기
        try (PrintWriter pw = new PrintWriter(new FileWriter(sFile))) {
            for (String line : serviceContent) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("[Server] 서비스 파일 업데이트 실패: " + e.getMessage());
        }
    }
    
    
    public String loadRoom(int roomId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (Integer.parseInt(data[0]) == roomId && data[3].equals("투숙중")) {
                    sb.append(line).append("#");
                }
            }
            return sb.length() > 0 ? "OK|" + sb.toString() : "EMPTY";
        } catch (Exception e) {
            return "ERROR|rooms.txt 읽기 실패";
        }
    }

    public String loadRoomServices(int roomId) {
        try (BufferedReader br = new BufferedReader(new FileReader(SERVICE_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (Integer.parseInt(data[0]) == roomId) {
                    sb.append(line).append("#");
                }
            }
            return sb.length() > 0 ? "OK|" + sb.toString() : "EMPTY";
        } catch (Exception e) {
            return "ERROR|service_usage.txt 읽기 실패";
        }
    }

    /*
    public String findReservation(String reservationId) {
        File file = new File(RES_FILE);
        if (!file.exists()) return "FAIL|예약 데이터 없음";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(reservationId + "|")) {
                    return "SUCCESS|" + line;
                }
            }
        } catch (IOException e) { e.printStackTrace(); return "FAIL|파일 오류"; }

        return "FAIL|예약 없음";
    }

    public String checkOut(String reservationId, String roomNumber, String actualCheckOutDate) {
        int serviceCost = serviceServer.getTotalServiceCost(reservationId);
        int stayCost = 50000;
        int extraFee = 0;

        String resLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(RES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(reservationId + "|")) { resLine = line; break; }
            }
        } catch (IOException e) { e.printStackTrace(); return "FAIL|예약 읽기 실패"; }

        if (resLine == null) return "FAIL|예약 없음";

        String[] data = resLine.split("\\|");
        String plannedCheckOut = data[5];

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date planned = sdf.parse(plannedCheckOut);
            Date actual = sdf.parse(actualCheckOutDate);
            if (actual.after(planned)) extraFee = 20000;
        } catch (Exception e) { e.printStackTrace(); return "FAIL|날짜 오류"; }

        int total = stayCost + serviceCost + extraFee;
        boolean roomUpdate = updateStatus(ROOM_FILE, roomNumber, 0, 3, "청소중");
        boolean resUpdate = updateStatus(RES_FILE, reservationId, 0, 6, "체크아웃 완료");

        if (roomUpdate && resUpdate) {
            return "SUCCESS|체크아웃 완료\n숙박료: " + stayCost +
                    "\n부대서비스: " + serviceCost +
                    "\n추가요금: " + extraFee +
                    "\n총 요금: " + total;
        } else {
            return "FAIL|체크아웃 상태 업데이트 실패";
        }
    }

    private boolean updateStatus(String filePath, String targetKey, int keyIndex, int targetIndex, String newValue) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > targetIndex && parts[keyIndex].equals(targetKey)) {
                    parts[targetIndex] = newValue;
                    found = true;
                    line = String.join("|", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) { e.printStackTrace(); return false; }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) { bw.write(line); bw.newLine(); }
            } catch (IOException e) { e.printStackTrace(); return false; }
        }

        return found;
    }
*/
    

}
