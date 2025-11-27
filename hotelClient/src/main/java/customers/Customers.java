/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customers;

/**
 *
 * @author subin
 */

public class Customers {
    private String name;
    private String phone;
    private String grade;
    private int visitCount;


    public Customers(String name, String phone, String grade, int visitCount) {
        this.name = name;
        this.phone = phone;
        this.grade = grade;
        this.visitCount = visitCount;
    }

    // 서버에서 받은 문자열(String) 방문횟수를 처리하기 위한 보조 생성자
    public Customers(String name, String phone, String grade, String visitCountStr) {
        this.name = name;
        this.phone = phone;
        this.grade = grade;
        try {
            this.visitCount = Integer.parseInt(visitCountStr);
        } catch (NumberFormatException e) {
            this.visitCount = 0;
        }
    }


    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getGrade() { return grade; }
    public int getVisitCount() { return visitCount; }

    public void setGrade(String grade) { this.grade = grade; }
    public void setVisitCount(int visitCount) { this.visitCount = visitCount; }


    public Object[] toRowData() {
        return new Object[] { name, phone, grade, visitCount };
    }
    
    @Override
    public String toString() {
        return "Customer [name=" + name + ", grade=" + grade + "]";
    }
}