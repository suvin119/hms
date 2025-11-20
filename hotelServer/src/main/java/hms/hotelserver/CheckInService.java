package hms.hotelserver;

/**
 *
 * @author subin
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CheckInService {
    // 파일 경로 선언
    private static final String ROOM_FILE = "src/main/resources/rooms.txt";
    private static final String RES_FILE = "src/main/resources/reservations.txt";

    // 1. 예약 찾기
    public String findReservation(String targetId) {
        File file = new File(RES_FILE);
        if (!file.exists()) return "FAIL|데이터 파일이 없습니다.";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // 파일 끝까지 한 줄씩 읽음
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                // 예약번호 일치 & 상태가 '예약'인 경우만 조회
                if (data[0].equals(targetId) && data[6].equals("예약")) {
                    return "SUCCESS|" + line; // 성공 시 SUCCESS 와 라인 전체 반환
                }
            }
        } catch (IOException e) {
            return "ERROR|파일 읽기 오류";
        }
        return "FAIL|예약 정보를 찾을 수 없거나 이미 체크인 된 예약입니다.";
    }

    // 2. 체크인 확정
    public String confirmCheckIn(String reservationId, String roomNumber) {
         // 0번(방번호) 찾아서 3번(상태) 변경
        boolean roomUpdate = updateStatus(ROOM_FILE, roomNumber, 0, 3, "투숙중");
         // 0번(예약번호) 찾아서 6번(상태) 변경
        boolean resUpdate = updateStatus(RES_FILE, reservationId, 0, 6, "투숙중");

        // 두 파일 모두 수정에 성공했는지 확인
        if (roomUpdate && resUpdate) {
            return "SUCCESS|체크인 완료 (객실: " + roomNumber + ")";
        } else {
            return "ERROR|파일 수정 중 오류 발생 (객실번호나 예약번호 확인 필요)";
        }
    }

    // =========================================================
    // [핵심] 파일 내용을 수정해서 덮어쓰는 공통 메소드
    // keyIndex: 검색할 데이터 위치
    // targetIndex: 수정할 데이터 위치
    // =========================================================
    private boolean updateStatus(String filePath, String targetKey, int keyIndex, int targetIndex, String newValue) {
        List<String> lines = new ArrayList<>(); // 파일 전체 내용을 잠시 담아둘 메모리 리스트
        boolean found = false;

        // 1. 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                // 찾으려는 키값과 일치하면?
                if (parts.length > targetIndex && parts[keyIndex].equals(targetKey)) {
                    parts[targetIndex] = newValue; // 값 변경 (빈객실 -> 투숙중)
                    found = true;
                    
                    // 다시 문자열로 합치기 (파이프 | 로 연결)
                    line = String.join("|", parts);
                }
                lines.add(line); // 리스트에 담기
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 2. 파일 덮어쓰기
        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine(); // 엔터 처리
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        return found; // 찾아서 수정했으면 true, 못 찾았으면 false 반환
    }
}