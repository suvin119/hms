package customers;

/**
 *
 * @author subin
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomersController {
    private CustomersView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;


    public CustomersController() {
        this.view = new CustomersView();
        initListeners();
    }

    public CustomersView getView() {
        return view;
    }

    private void initListeners() {
        
        // 1. 목록 새로고침
        view.addLoadListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        
        //2. 신규 고객 등록
        view.addSyncListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = sendRequest("SYNC_NEW_CUSTOMERS");

                if (response.startsWith("SUCCESS")) {
                    view.showMessage("신규 고객 등록 및 동기화 완료!");
                    refreshData();
                } else {
                    view.showMessage("동기화 실패: " + response);
                }
            }
        });
        

       // 3. 등급 변경 리스너
        view.addEditGradeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = view.getSelectedRow();
                if (row == -1) {
                    view.showMessage("등급을 변경할 고객을 선택해주세요.");
                    return;
                }

                String name = view.getSelectedName();
                String phone = view.getSelectedPhone();

                // 등급 선택 팝업
                String[] grades = {"일반", "VIP", "블랙리스트"};
                String newGrade = (String) JOptionPane.showInputDialog(
                        view, "새로운 등급을 선택하세요:", "등급 변경",
                        JOptionPane.QUESTION_MESSAGE, null, grades, grades[0]);

                if (newGrade != null) {
                    // UPDATE_GRADE|이름|전화번호|새등급
                    String msg = "UPDATE_GRADE|" + name + "|" + phone + "|" + newGrade;
                    
                    String response = sendRequest(msg);

                    if (response.startsWith("SUCCESS")) {
                        view.showMessage("등급 변경 완료!");
                        refreshData();
                    } else {
                        view.showMessage("변경 실패: " + response);
                    }
                }
            }
        });

    }

    // 새로고침
    private void refreshData() {
        view.clearTable();
        
        List<String> responses = sendRequestForList("GET_CUSTOMERS");
        
        if (responses.isEmpty()) {
             return;
        }

        for (String line : responses) {
            // "이름|전화번호|등급|방문수" 파싱
            String[] data = line.split("\\|");
            if (data.length >= 4) {
                view.addRow(data[0], data[1], data[2], data[3]);
            }
        }
        
        if (responses.isEmpty()) {
            view.showMessage("데이터 불러오기 실패");
        }
    }

    
    private String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            out.println(msg);
            String response = in.readLine();
            return (response != null) ? response : "ERROR|응답 없음";
            
        } catch (IOException ex) {
            ex.printStackTrace();
            return "ERROR|서버 연결 실패";
        }
    }
    
    private List<String> sendRequestForList(String msg) {
        List<String> resultList = new ArrayList<>();

        String response = sendRequest(msg);

        if (response != null && response.startsWith("OK|")) {
            if (response.length() > 3) {
                String rawData = response.substring(3); 
                
                if (!rawData.isEmpty()) {
                    String[] rows = rawData.split("/");
                    for (String row : rows) {
                        resultList.add(row);
                    }
                }
            }
        }
        
        return resultList;
    }
}