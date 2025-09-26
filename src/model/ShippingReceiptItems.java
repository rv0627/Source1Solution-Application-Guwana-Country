/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ravin
 */
public class ShippingReceiptItems {
    private String Item;
    private String Qty;
    private String Rate;
    private String Amount;

    /**
     * @return the Item
     */
    public String getItem() {
        return Item;
    }

    /**
     * @param Item the Item to set
     */
    public void setItem(String Item) {
        this.Item = Item;
    }

    /**
     * @return the Qty
     */
    public String getQty() {
        return Qty;
    }

    /**
     * @param Qty the Qty to set
     */
    public void setQty(String Qty) {
        this.Qty = Qty;
    }

    /**
     * @return the Rate
     */
    public String getRate() {
        return Rate;
    }

    /**
     * @param Rate the Rate to set
     */
    public void setRate(String Rate) {
        this.Rate = Rate;
    }

    /**
     * @return the Amount
     */
    public String getAmount() {
        return Amount;
    }

    /**
     * @param Amount the Amount to set
     */
    public void setAmount(String Amount) {
        this.Amount = Amount;
    }
}
