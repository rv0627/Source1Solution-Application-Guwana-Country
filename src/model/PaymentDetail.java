/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ravin
 */
public class PaymentDetail {
     private String wirNo;
    private String paidAmount;
    private String dueAmount;

    /**
     * @return the wirNo
     */
    public String getWirNo() {
        return wirNo;
    }

    /**
     * @param wirNo the wirNo to set
     */
    public void setWirNo(String wirNo) {
        this.wirNo = wirNo;
    }

    /**
     * @return the paidAmount
     */
    public String getPaidAmount() {
        return paidAmount;
    }

    /**
     * @param paidAmount the paidAmount to set
     */
    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * @return the dueAmount
     */
    public String getDueAmount() {
        return dueAmount;
    }

    /**
     * @param dueAmount the dueAmount to set
     */
    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }
}
