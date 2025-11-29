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
    private static final String MENU_FILE = "src/main/resources/menu.txt";
    private ServiceServer serviceServer = new ServiceServer();
    
    public static String checkoutRoom(int roomId) {
        List<String> fileContent = new ArrayList<>();
        boolean found = false;

        // 1. 파일 읽기, 수정할 내용 메모리에 저장
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

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
        updateReservationStatus(roomId);
        
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
    
    
    private static void updateReservationStatus(int roomId) {
        File resFile = new File(RES_FILE);
        if (!resFile.exists()) return;

        List<String> resContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(resFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                if (parts.length > 7) {
                    try {
                        int currentRoomId = Integer.parseInt(parts[7]); // 7번째가 방번호

                        if (currentRoomId == roomId && parts[6].equals("투숙중")) {
                            parts[6] = "완료"; 
                            
                            String newLine = String.join("|", parts);
                            resContent.add(newLine);
                        } else {
                            resContent.add(line);
                        }
                    } catch (NumberFormatException e) {
                        resContent.add(line);
                    }
                } else {
                    resContent.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println("[Server] 예약 파일 업데이트 실패: " + e.getMessage());
            return;
        }

        // 파일 덮어쓰기
        try (PrintWriter pw = new PrintWriter(new FileWriter(resFile))) {
            for (String line : resContent) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("[Server] 예약 파일 쓰기 실패: " + e.getMessage());
        }
    }
    
    
    public String loadRoom(int roomId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (Integer.parseInt(data[0]) == roomId && data[3].equals("투숙중")) {
                    String guestInfo = findGuestInfoByRoomId(roomId);
                    sb.append(line).append("|").append(guestInfo).append("#");
                }
            }
            return sb.length() > 0 ? "OK|" + sb.toString() : "EMPTY";
        } catch (Exception e) {
            return "ERROR|rooms.txt 읽기 실패";
        }
    }
    
    
    private String findGuestInfoByRoomId(int roomId) {
        File file = new File(RES_FILE);
        if (!file.exists()) return "알수없음|2025-01-01|2025-01-02"; // 기본값

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 7) {
                    int resRoomId = Integer.parseInt(parts[7]);
                    String status = parts[6];
                    
                    if (resRoomId == roomId && status.equals("투숙중")) {
                            return parts[1] + "|" + parts[3] + "|" + parts[4]; // 이름, 체크인 날짜, 체크아웃 날짜
                        }
                }
            }
        } catch (Exception e) {
            System.out.println("[Server] 고객 이름 조회 실패: " + e.getMessage());
        }
        return "알수없음|2025-01-01|2025-01-02";
    }

    
    public String loadRoomServices(int roomId) {
        try (BufferedReader br = new BufferedReader(new FileReader(SERVICE_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                
                try {
                    int rId = Integer.parseInt(data[0]);
                    
                    if (rId == roomId) {
                        String menuId = data[1];
                        
                        String menuName = findMenuNameById(menuId);
                        
                        // 101|1|2|4000|2025-11-29|콜라
                        sb.append(line).append("|").append(menuName).append("#");
                    }
                } catch (NumberFormatException ignored) {}
            }
            return sb.length() > 0 ? "OK|" + sb.toString() : "EMPTY";
        } catch (Exception e) {
            return "ERROR|사용 내역 읽기 실패";
        }
    }
    
    private String findMenuNameById(String menuId) {
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                // menu.txt 구조: [0]ID | [1]이름 | [2]가격
                if (parts.length >= 2 && parts[0].trim().equals(menuId.trim())) {
                    return parts[1].trim(); // 메뉴 이름 반환
                }
            }
        } catch (Exception ignored) {}
        return "알수없음(" + menuId + ")"; // 못 찾으면 ID라도 표시
    }
}
