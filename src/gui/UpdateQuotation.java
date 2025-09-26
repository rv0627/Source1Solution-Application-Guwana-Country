/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import java.awt.Rectangle;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.InvoiceItem;
import model.MySQL;
import model.QuotationItem;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.io.InputStream;
import java.net.URL;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author ravin
 */
public class UpdateQuotation extends javax.swing.JDialog {

    QuotationN Qi;
    private String selectedQutID;

    HashMap<String, QuotationItem> quotationItemMap = new HashMap<>();
    HashMap<String, Integer> shipModeMap = new HashMap<>();
    HashMap<String, Integer> paymentMethodMap = new HashMap<>();

    /**
     * Creates new form UpdateQuotation
     */
    public UpdateQuotation(java.awt.Frame parent, boolean modal, String qutID) {
        super(parent, modal);
        initComponents();
        jDateChooser2.getDateEditor().setEnabled(false);

        this.setTitle("Source 1 Solutions - New Quotation");
        Qi = (QuotationN) parent;

        if (Qi.row != -1) {
            selectedQutID = qutID;
            shippingMode();
            loadUpdateDetails();
        }
    }

    private void shippingMode() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `mode_of_shipment` ORDER BY `mode_of_shipment_id` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("mode"));
                shipModeMap.put(resultSet.getString("mode"), resultSet.getInt("mode_of_shipment_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loadInvoiceItem
    private void loadQuotationItem() {
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        double total = 0;

        for (QuotationItem quotationItem : quotationItemMap.values()) {
            Vector<String> vector = new Vector<>();
            vector.add(quotationItem.getDescription());
            vector.add(quotationItem.getQuantity());

            double itemTotal = Double.parseDouble(quotationItem.getQuantity()) * Double.parseDouble(quotationItem.getWebsiteTotal());
            total += itemTotal;
            vector.add(String.valueOf(itemTotal));

            dtm.addRow(vector);

        }

        for (int i = 0; i < dtm.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        jFormattedTextField3.setText(String.valueOf(total));

        calculate();

    }

    public double Grandtotal = 0;
    private double Tabletotal = 0;
    private double itemWeight = 0;
    private double handling = 0;
    private double duties = 0;
    private double taxes = 0;
    private double subtotal = 0;
    private double serviceCharge = 0;
    private double freight = 0;
    private double estimatedShipping = 0;
    private double ratePerLb;

    private void calculate() {
        if (!jFormattedTextField3.getText().isEmpty()) {
            Tabletotal = Double.parseDouble(jFormattedTextField3.getText());
            subtotal = Tabletotal * 220;
            jFormattedTextField4.setText(String.valueOf(subtotal));
        }

        if (!jFormattedTextField4.getText().isEmpty()) {
            serviceCharge = (subtotal / 100) * 10;
            jFormattedTextField5.setText(String.valueOf(serviceCharge));
        }

        if (jFormattedTextField6.getText().isEmpty()) {
            itemWeight = 0;
        } else {
            itemWeight = Double.parseDouble(jFormattedTextField6.getText());
        }
        freight = ((itemWeight * ratePerLb) * 220) + 990;
        double roundedFreight = Math.round(freight);
        jFormattedTextField7.setText(String.valueOf(roundedFreight));

        if (jFormattedTextField6.getText().isEmpty()) {
            handling = 0;
            jFormattedTextField8.setText("");
        } else {
            if (itemWeight == 1 || itemWeight == 2 || itemWeight == 3 || itemWeight == 4 || itemWeight == 5) {
                handling = 220;
                jFormattedTextField8.setText(String.valueOf(handling));
            } else if (itemWeight == 6 || itemWeight == 7 || itemWeight == 8 || itemWeight == 9 || itemWeight == 10) {
                handling = 550;
                jFormattedTextField8.setText(String.valueOf(handling));
            } else if (itemWeight == 11 || itemWeight == 12 || itemWeight == 13 || itemWeight == 14 || itemWeight == 15 || itemWeight == 16) {
                handling = 660;
                jFormattedTextField8.setText(String.valueOf(handling));
            } else if (itemWeight >= 17) {
                handling = 1100;
                jFormattedTextField8.setText(String.valueOf(handling));
            }
        }

        if (jFormattedTextField15.getText().isEmpty()) {
            duties = dut;
            jFormattedTextField9.setText(String.valueOf(duties));
        } else {
            duties = ((Double.parseDouble(jFormattedTextField4.getText()) / 100) * Double.parseDouble(jFormattedTextField15.getText()));
            double roundedDut = Math.round(duties);
            jFormattedTextField9.setText(String.valueOf(roundedDut));
//            duties = Double.parseDouble(jFormattedTextField9.getText());
        }

        if (jFormattedTextField16.getText().isEmpty()) {
            taxes = vat;
            jFormattedTextField10.setText(String.valueOf(taxes));
        } else {
            taxes = (((Double.parseDouble(jFormattedTextField4.getText()) + Double.parseDouble(jFormattedTextField9.getText())) / 100) * Double.parseDouble(jFormattedTextField16.getText()));
//            taxes = Double.parseDouble(jFormattedTextField10.getText());
            double roundedTax = Math.round(taxes);
            jFormattedTextField10.setText(String.valueOf(roundedTax));
        }

        estimatedShipping = (freight + handling + duties + taxes);
        double roundedEstimated = Math.round(estimatedShipping);
        jFormattedTextField11.setText(String.valueOf(roundedEstimated));

        Grandtotal = (subtotal + serviceCharge + estimatedShipping);
        double roundedGrandTotal = Math.round(Grandtotal);
        jFormattedTextField12.setText(String.valueOf(roundedGrandTotal));

    }

    private void resetItemDetails() {
        jFormattedTextField1.setText("");
        jTextArea2.setText("");
        jFormattedTextField2.setText("");
    }
    
    private double dut = 0;
    private double vat = 0;

    private void loadUpdateDetails() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `quotation` INNER JOIN `quotation_item`"
                    + "ON `quotation`.`quotation_id`=`quotation_item`.`quotation_quotation_id` "
                    + "INNER JOIN `customers` ON `quotation`.`customers_customer_id`=`customers`.`customer_id` "
                    + "INNER JOIN `employee` ON `quotation`.`employee_employee_id`=`employee`.`employee_id` "
                    + "WHERE `quotation`.`quotation_id`='" + selectedQutID + "'");

            while (resultSet.next()) {
                String description = resultSet.getString("quotation_item.description_of_item");
                String qty = resultSet.getString("quotation_item.quantity");
                String websiteTotal = resultSet.getString("quotation_item.website_total");

                ratePerLb = resultSet.getDouble("customers.rate_per_lb");

                jTextField1.setText(selectedQutID);
                jTextField2.setText(resultSet.getString("customers.mobile"));
                jTextField4.setText(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                jTextField5.setText(resultSet.getString("customers.email"));
                jTextField8.setText(resultSet.getString("customers.address_line1") + ", " + resultSet.getString("customers.address_line2") + ", " + resultSet.getString("customers.address_line3"));

                dut = resultSet.getDouble("quotation.duties");
                vat = resultSet.getDouble("quotation.taxes");
                jFormattedTextField6.setText(resultSet.getString("quotation.item_weight"));
                jFormattedTextField8.setText(resultSet.getString("quotation.handling"));
                jFormattedTextField9.setText(resultSet.getString("quotation.duties"));
                jFormattedTextField10.setText(resultSet.getString("quotation.taxes"));

                jDateChooser2.setDate(resultSet.getDate("quotation.estimated_date_of_arrival"));

                jComboBox2.setSelectedIndex(resultSet.getInt("quotation.mode_of_shipment_mode_of_shipment_id"));

                QuotationItem quotationItem = new QuotationItem();
                quotationItem.setDescription(description);
                quotationItem.setQuantity(qty);
                quotationItem.setWebsiteTotal(websiteTotal);

                if (quotationItemMap.get(description) == null) {
                    quotationItemMap.put(description, quotationItem);
                } else {

                    QuotationItem found = quotationItemMap.get(description);

                    int option = JOptionPane.showConfirmDialog(this, "Do you want to update the qty of product : " + description, "Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_NO_OPTION) {

                        found.setQuantity(String.valueOf(Double.parseDouble(found.getQuantity()) + Double.parseDouble(qty)));

                    }

                }

                loadQuotationItem();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jButton5 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jFormattedTextField9 = new javax.swing.JFormattedTextField();
        jFormattedTextField10 = new javax.swing.JFormattedTextField();
        jFormattedTextField11 = new javax.swing.JFormattedTextField();
        jFormattedTextField12 = new javax.swing.JFormattedTextField();
        jFormattedTextField15 = new javax.swing.JFormattedTextField();
        jFormattedTextField16 = new javax.swing.JFormattedTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel25 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 21)); // NOI18N
        jLabel3.setText("Update Quotation");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Quotation No.");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Mobile");

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Quantity of Items");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Name");

        jTextField4.setEditable(false);
        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Email");

        jTextField5.setEditable(false);
        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Website Total ($)");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setText("Add to Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Description of item");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Address");

        jTextField8.setEditable(false);
        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton5MouseReleased(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addGap(46, 46, 46)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jTextField1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jFormattedTextField1)))))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(jTextField4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedTextField2))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField8)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total of table ($)");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Subtotal Amount ($)");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Service Charge ($)");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Items Weight (lbs.)");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Freight ($)");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Handling ($)");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Duties (% / $)");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Taxes (% / $)");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Estimated Shipping ($)");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("GRAND TOTAL ($)");

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setText("Update Quotation");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setEditable(false);
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jFormattedTextField4.setEditable(false);
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField5.setEditable(false);
        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField6KeyReleased(evt);
            }
        });

        jFormattedTextField7.setEditable(false);
        jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField8.setEditable(false);
        jFormattedTextField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField8KeyReleased(evt);
            }
        });

        jFormattedTextField9.setEditable(false);
        jFormattedTextField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField9KeyReleased(evt);
            }
        });

        jFormattedTextField10.setEditable(false);
        jFormattedTextField10.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField10KeyReleased(evt);
            }
        });

        jFormattedTextField11.setEditable(false);
        jFormattedTextField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jFormattedTextField12.setEditable(false);
        jFormattedTextField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jFormattedTextField15.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField15KeyReleased(evt);
            }
        });

        jFormattedTextField16.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField16KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextField12, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jFormattedTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jFormattedTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jFormattedTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jFormattedTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description of Item", "Quantity of item", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable2);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Estimated time of arrival");

        IDateEditor dateEditor = jDateChooser2.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Select shipping mode");

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel22)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        String mobile = jTextField2.getText();

        if (mobile.length() > 4) {
            try {
                ResultSet resultset = MySQL.execute("SELECT * FROM `customers` WHERE `mobile`='" + mobile + "'");
                if (resultset.next()) {

                    jTextField4.setText(resultset.getString("first_name") + " " + resultset.getString("last_name"));
                    jTextField5.setText(resultset.getString("email"));
                    jTextField8.setText(resultset.getString("address_line1") + " " + resultset.getString("address_line2") + " " + resultset.getString("address_line3"));

                    ratePerLb = resultset.getDouble("rate_per_lb");

                } else {
                    jTextField4.setText("");
                    jTextField5.setText("");
                    jTextField8.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            jTextField4.setText("");
            jTextField5.setText("");
            jTextField8.setText("");
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //Add to invoice table

        String description = jTextArea2.getText();
        String qty = jFormattedTextField1.getText();
        String websiteTotal = jFormattedTextField2.getText();

        if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the quantity of item", "Message", JOptionPane.WARNING_MESSAGE);
        } else if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the description of item", "Message", JOptionPane.WARNING_MESSAGE);
        } else if (websiteTotal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the website total of item", "Message", JOptionPane.WARNING_MESSAGE);
        } else {

            QuotationItem quotationItem = new QuotationItem();
            quotationItem.setDescription(description);
            quotationItem.setQuantity(qty);
            quotationItem.setWebsiteTotal(websiteTotal);

            if (quotationItemMap.get(description) == null) {
                quotationItemMap.put(description, quotationItem);
            } else {

                QuotationItem found = quotationItemMap.get(description);

                int option = JOptionPane.showConfirmDialog(this, "Do you want to update the qty of product : " + description, "Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_NO_OPTION) {

                    found.setQuantity(String.valueOf(Double.parseDouble(found.getQuantity()) + Double.parseDouble(qty)));

                }

            }

            loadQuotationItem();
            resetItemDetails();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MousePressed

    private void jButton5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //Update
        String qutID = jTextField1.getText();
        String mobile = jTextField2.getText();
        String name = jTextField4.getText();
        String email = jTextField5.getText();
        String address = jTextField8.getText();

        String subTotalAmount = jFormattedTextField4.getText();
        String itemweight = jFormattedTextField6.getText();
        String handlingAmount = jFormattedTextField8.getText();
        String dutiesAmount = jFormattedTextField9.getText();
        String taxAmount = jFormattedTextField10.getText();
        String gradTotal = jFormattedTextField12.getText();
        String estimatedShippingAmount = jFormattedTextField11.getText();

        String tableTotal = jFormattedTextField3.getText();

        SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");

        String shippMode = String.valueOf(jComboBox2.getSelectedItem());

        if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select estimate date.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (shippMode.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select shipping method.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (tableTotal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Not added the quotation item.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (itemweight.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter item weight.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (handlingAmount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter handling amount.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dutiesAmount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter duties amount.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (taxAmount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter tax amount.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String estimatedDate = format1.format(jDateChooser2.getDate());
            int shippModeId = shipModeMap.get(shippMode);

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `email`='" + email + "' OR `mobile`='" + mobile + "'");
                if (resultSet.next()) {

                    //add to invoice
                    MySQL.execute("UPDATE `quotation` SET "
                            + "`estimated_date_of_arrival`='" + estimatedDate + "',"
                            + "`mode_of_shipment_mode_of_shipment_id`='" + shippModeId + "',"
                            + "`sub_total`='" + subTotalAmount + "',"
                            + "`item_weight`='" + itemweight + "',"
                            + "`handling`='" + handlingAmount + "',"
                            + "`duties`='" + dutiesAmount + "',"
                            + "`taxes`='" + taxAmount + "',"
                            + "`estimated_shipping`='" + estimatedShippingAmount + "',"
                            + "`grand_total`='" + gradTotal + "' "
                            + "WHERE `quotation_id`='" + selectedQutID + "'");

                    //add to invoice
                    //add to invoice Item
                    for (QuotationItem item : quotationItemMap.values()) {
                        ResultSet resultSet2 = MySQL.execute("SELECT * FROM `quotation_item` "
                                + "WHERE `description_of_item`='" + item.getDescription() + "' AND `website_total`='" + item.getWebsiteTotal() + "' AND `quotation_quotation_id`='" + selectedQutID + "'");

                        if (resultSet2.next()) {
                            MySQL.execute("UPDATE `quotation_item` SET "
                                    + "`description_of_item`='" + item.getDescription() + "',"
                                    + "`quantity`='" + item.getQuantity() + "',"
                                    + "`website_total`='" + item.getWebsiteTotal() + "' "
                                    + "WHERE `description_of_item`='" + resultSet2.getString("description_of_item") + "' AND `quotation_quotation_id`='" + selectedQutID + "'");
                        } else {
                            MySQL.execute("INSERT INTO `quotation_item` (`description_of_item`,`quantity`,`website_total`,`quotation_quotation_id`)"
                                    + "VALUES ('" + item.getDescription() + "','" + item.getQuantity() + "','" + item.getWebsiteTotal() + "','" + selectedQutID + "')");
                        }

                    }
                    //add to invoice Item

                    Date date1 = new Date();
                    String quotationRegisterDate = format1.format(date1);

                    InputStream reportStream = MainInterface.class.getResourceAsStream("/reports/source1solution_Quotation.jasper");
                    URL imageUrl = MainInterface.class.getResource("/img/IMG_report.jpg");

                    HashMap<String, Object> parameters = new HashMap<>();
                    parameters.put("Parameter1", jTextField2.getText());
                    parameters.put("Parameter2", jTextField4.getText());
                    parameters.put("Parameter3", jTextField5.getText());
                    parameters.put("Parameter4", jTextField1.getText());
                    parameters.put("Parameter5", jTextField8.getText());

                    parameters.put("Parameter6", quotationRegisterDate);
                    parameters.put("Parameter7", jFormattedTextField3.getText());
                    parameters.put("Parameter8", jFormattedTextField11.getText());
                    parameters.put("Parameter9", jFormattedTextField12.getText());

                    parameters.put("Parameter10", jFormattedTextField4.getText());
                    parameters.put("Parameter11", jFormattedTextField5.getText());

                    parameters.put("Parameter12", jFormattedTextField7.getText());
                    parameters.put("Parameter13", jFormattedTextField8.getText());
                    parameters.put("Parameter14", jFormattedTextField9.getText());
                    parameters.put("Parameter15", jFormattedTextField10.getText());

                    parameters.put("ImageFilePath", imageUrl.toString()); // Replace with the actual path

                    JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable2.getModel());

                    //JasperPrintManager.printReport(jasperPrint, true);
                    if (reportStream != null) {
                        // Jasper Report file found, proceed with generating the report
                        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                        // Close the stream after use
                        reportStream.close();

                        // Print the report
                        JasperPrintManager.printReport(jasperPrint, true);
                    } else {
                        // Jasper Report file not found, handle the situation gracefully
                        JOptionPane.showMessageDialog(this, "Jasper Report file not found. Unable to generate the report.", "Warning", JOptionPane.WARNING_MESSAGE);

                    }

                    //open report
                    JOptionPane.showMessageDialog(this, "Quotation Updated.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                    Qi.loadQuotations("", "quotation_id", "ASC");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Customer not registered. Please check customer details.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                MainInterface.logger.log(Level.WARNING, "New quotation print", e);
                //e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jFormattedTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField6KeyReleased
        // TODO add your handling code here:
        //2
        if (jFormattedTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items..", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            calculate();
        }
    }//GEN-LAST:event_jFormattedTextField6KeyReleased

    private void jFormattedTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField8KeyReleased
        // TODO add your handling code here:
        //3
        calculate();
    }//GEN-LAST:event_jFormattedTextField8KeyReleased

    private void jFormattedTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField9KeyReleased
        // TODO add your handling code here:
        //4
        calculate();
    }//GEN-LAST:event_jFormattedTextField9KeyReleased

    private void jFormattedTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField10KeyReleased
        // TODO add your handling code here:
        //5
        calculate();
    }//GEN-LAST:event_jFormattedTextField10KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        //remove Invoice Item
        if (evt.getClickCount() == 2) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to remove this item", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

                if (jTable2.getSelectedRow() != -1) {
                    int row = jTable2.getSelectedRow();
                    String description = String.valueOf(jTable2.getValueAt(row, 0));
                    String total = String.valueOf(jTable2.getValueAt(row, 2)); // Assuming total is in column 2

                    quotationItemMap.remove(description);

                    // Update the total by subtracting the removed item's total
                    double removedItemTotal = Double.parseDouble(total);
                    double currentTotal = Double.parseDouble(jFormattedTextField3.getText()) - removedItemTotal;
                    jFormattedTextField3.setText(String.valueOf(currentTotal));

                    model.removeRow(row);
                    resetItemDetails();

                }

                if (model.getRowCount() == 0) {
                    jFormattedTextField3.setText("");
                }

                calculate();
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jFormattedTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField15KeyReleased
        // TODO add your handling code here:
        if (jFormattedTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items..", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            calculate();
        }
    }//GEN-LAST:event_jFormattedTextField15KeyReleased

    private void jFormattedTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField16KeyReleased
        // TODO add your handling code here:
       if (jFormattedTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items..", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            calculate();
        }
    }//GEN-LAST:event_jFormattedTextField16KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField10;
    private javax.swing.JFormattedTextField jFormattedTextField11;
    private javax.swing.JFormattedTextField jFormattedTextField12;
    private javax.swing.JFormattedTextField jFormattedTextField15;
    private javax.swing.JFormattedTextField jFormattedTextField16;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
    private javax.swing.JFormattedTextField jFormattedTextField9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
