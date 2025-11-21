package checkOut;

import UnitServices.ServiceDAO;
import java.awt.event.*;

public class CheckOutController {
    private CheckOutView view;
    private ServiceDAO dao;

    public CheckOutController(CheckOutView view, ServiceDAO dao) {
        this.view = view;
        this.dao = dao;
        initController();
    }

    private void initController() {
        view.btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { calculateCheckOut(); }
        });
    }

    private void calculateCheckOut() {
        String reservationIdStr = view.tfReservationId.getText().trim();
        String roomNumber = view.tfRoomNumber.getText().trim();
        String checkOutDate = view.tfCheckOutDate.getText().trim();

        if (reservationIdStr.isEmpty() || roomNumber.isEmpty() || checkOutDate.isEmpty()) {
            view.taInfo.setText("예약ID, 객실번호, 체크아웃 날짜를 모두 입력하세요.");
            return;
        }

        try {
            int reservationId = Integer.parseInt(reservationIdStr);
            int serviceCost = dao.getTotalServiceCost(reservationId);

            int stayCost = 50000;
            int extraFee = 0;
            int total = stayCost + serviceCost + extraFee;

            StringBuilder sb = new StringBuilder();
            sb.append("체크아웃 요금 계산 결과:\n");
            sb.append("숙박료: ").append(stayCost).append("원\n");
            sb.append("부대서비스: ").append(serviceCost).append("원\n");
            sb.append("추가 요금: ").append(extraFee).append("원\n");
            sb.append("총 결제 금액: ").append(total).append("원");

            view.taInfo.setText(sb.toString());

        } catch (NumberFormatException ex) {
            view.taInfo.setText("예약ID는 숫자로 입력해야 합니다.");
        }
    }
}
