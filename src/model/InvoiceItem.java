/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ravin
 */
public class InvoiceItem {
    private String quantity;
    private String description;
    private String websiteTotal;

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the websiteTotal
     */
    public String getWebsiteTotal() {
        return websiteTotal;
    }

    /**
     * @param websiteTotal the websiteTotal to set
     */
    public void setWebsiteTotal(String websiteTotal) {
        this.websiteTotal = websiteTotal;
    }
}
