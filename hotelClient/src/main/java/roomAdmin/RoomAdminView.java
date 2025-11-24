package roomAdmin;

/**
 *
 * @author subin
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomAdminView extends JPanel {

    private JTextField tfRoomNum = new JTextField(10);
    private JComboBox<Room.Type> cbType = new JComboBox<>(Room.Type.values());
    
    private JTextField tfPrice = new JTextField(10);
    private JComboBox<Room.Status> cbStatus = new JComboBox<>(Room.Status.values());

    private JButton btnAdd = new JButton("객실 등록");
    private JButton btnUpdatePrice = new JButton("요금 수정");
    private JButton btnUpdateStatus = new JButton("상태 변경");
    private JButton btnRefresh = new JButton("새로고침");
    private JButton btnBack = new JButton("뒤로가기");

    private DefaultTableModel tableModel;
    private JTable table;

    public RoomAdminView() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.add(new JLabel("방번호:"));
        row1.add(tfRoomNum);
        row1.add(new JLabel("타입:"));
        row1.add(cbType);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row2.add(new JLabel("요금:"));
        row2.add(tfPrice);
        row2.add(new JLabel("상태:"));
        row2.add(cbStatus);
        
        inputPanel.add(row1);
        inputPanel.add(row2);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdatePrice);
        btnPanel.add(btnUpdateStatus);
        btnPanel.add(btnRefresh);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"객실번호", "타입", "1박요금", "상태"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public String getRoomNum() { return tfRoomNum.getText(); }
    public Room.Type getSelectedType() { return (Room.Type) cbType.getSelectedItem(); }
    public String getPriceStr() { return tfPrice.getText(); }
    public Room.Status getSelectedStatus() { return (Room.Status) cbStatus.getSelectedItem(); }

    public void setAddButtonListener(ActionListener listener) { btnAdd.addActionListener(listener); }
    public void setPriceButtonListener(ActionListener listener) { btnUpdatePrice.addActionListener(listener); }
    public void setStatusButtonListener(ActionListener listener) { btnUpdateStatus.addActionListener(listener); }
    public void setRefreshButtonListener(ActionListener listener) { btnRefresh.addActionListener(listener); }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void updateTable(List<Room> roomList) {
        tableModel.setRowCount(0);
        for (Room r : roomList) {
            Object[] row = {
                r.getRoomNumber(), 
                r.toString().split("\\|")[2].trim(),
                (int)r.getPrice(), 
                r.getStatus().getLabel()
            };
            tableModel.addRow(row);
        }
    }
}