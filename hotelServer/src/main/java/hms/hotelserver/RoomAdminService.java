package hms.hotelserver;

/**
 *
 * @author subin
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomAdminService {
    private static final String ROOM_FILE = "src/main/resources/rooms.txt";
    private static final String SERVICE_FILE = "src/main/resources/service_usage.txt";
    private static List<String> roomLines = Collections.synchronizedList(new ArrayList<>());
    
    // 데이터를 확인했는지
    private static boolean isDataLoaded = false; 

    public String processRequest(String command, String[] parts) {
        ensureDataLoaded(); 

        switch (command) {
            case "ROOM_ADD":
                return addRoom(parts);
            
            case "ROOM_UPDATE_PRICE":
                return updatePrice(parts);
            
            case "ROOM_UPDATE_STATUS":
                return updateStatus(parts);
            
            case "ROOM_LIST":
                return getRoomListString();
            
           case "ROOMS_LOAD":
                int roomId = Integer.parseInt(parts[1]);
                return loadRoom(roomId); 
                
            case "ROOMS_SERVICE_USAGE":
                int roomIdService = Integer.parseInt(parts[1]);
                return loadRoomServices(roomIdService); 
                
                
            default:
                return "ERROR|알 수 없는 명령";
        }
    }
    
    private synchronized void ensureDataLoaded() {
        if (!isDataLoaded) {
            System.out.println("[RoomAdminService] 파일에서 데이터를 읽어옵니다...");
            loadRoomsFromFile();
            isDataLoaded = true;
        }
    }


    private String addRoom(String[] parts) {
        try {
            String roomNum = parts[1];
            for (String line : roomLines) {
                if (line.startsWith(roomNum + "|")) {
                    return "FALSE|이미 존재하는 객실 번호입니다.";
                }
            }
            String newLine = String.format("%s|%s|%s|%s", parts[1], parts[2], parts[3], parts[4]);
            roomLines.add(newLine);
            saveRoomsToFile();
            return "TRUE|객실 등록 성공";
        } catch (Exception e) {
            return "FALSE|등록 중 서버 오류";
        }
    }

    private String updatePrice(String[] parts) {
        String targetRoomNum = parts[1];
        String newPrice = parts[2];
        for (int i = 0; i < roomLines.size(); i++) {
            String line = roomLines.get(i);
            String[] data = line.split("\\|");
            if (data[0].equals(targetRoomNum)) {
                String updatedLine = String.format("%s|%s|%s|%s", data[0], data[1], newPrice, data[3]);
                roomLines.set(i, updatedLine);
                saveRoomsToFile();
                return "TRUE|가격 수정 완료";
            }
        }
        return "FALSE|객실 없음";
    }

    private String updateStatus(String[] parts) {
        String targetRoomNum = parts[1];
        String newStatus = parts[2];
        for (int i = 0; i < roomLines.size(); i++) {
            String line = roomLines.get(i);
            String[] data = line.split("\\|");
            if (data[0].equals(targetRoomNum)) {
                String updatedLine = String.format("%s|%s|%s|%s", data[0], data[1], data[2], newStatus);
                roomLines.set(i, updatedLine);
                saveRoomsToFile();
                return "TRUE|상태 변경 완료";
            }
        }
        return "FALSE|객실 없음";
    }

    private String getRoomListString() {
        if (roomLines.isEmpty()) return "EMPTY";
        StringBuilder sb = new StringBuilder();
        for (String line : roomLines) {
            sb.append(line).append("/");
        }
        return sb.toString();
    }

    private static void loadRoomsFromFile() {
        File file = new File(ROOM_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    roomLines.add(line);
                }
            }
            System.out.println("[RoomAdminService] 로드 완료: " + roomLines.size() + "건");
        } catch (Exception e) {
            System.out.println("[RoomAdminService] 파일 로드 실패");
        }
    }

    

    private static synchronized void saveRoomsToFile() {
        File file = new File(ROOM_FILE);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : roomLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        // ====================== 체크아웃용 ======================
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
}