/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import model.AddNumbers;

/**
 *
 * @author Admin
 */
public class Tracking_System extends javax.swing.JDialog {

    HashMap<String, Integer> trackingStatusMap = new HashMap<>();
    HashMap<String, Integer> websiteMap = new HashMap<>();

    MainInterface mi;
    String cusID;

    private static String invoiceNumber;

    public static void setInvoiceNumber(String invoiceNumber) {
        Tracking_System.invoiceNumber = invoiceNumber;
    }

    public static String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Creates new form tracking_system
     */
    public Tracking_System(java.awt.Frame parent, boolean modal, HashMap<String, String> inDetailMap) {
        super(parent, modal);
        initComponents();
        jButton3.setEnabled(false);
        jButton1.setEnabled(true);

        try {
            FileHandler handler = new FileHandler("shop.log", true);
            handler.setFormatter(new SimpleFormatter());

            MainInterface.logger.addHandler(handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mi = (MainInterface) parent;
        this.setTitle("Source 1 Solutions - Tracking System");
        loadTrackingStatus();
        loadWebsites();
        loadTrackingDetails("", "DESC");
        jDateChooser1.getDateEditor().setEnabled(false);
        jDateChooser2.getDateEditor().setEnabled(false);

        if (mi.Irow != -1) {
            jTextField1.setText(inDetailMap.get("invoiceID"));
            jTextField4.setText(inDetailMap.get("cusName"));
            jTextField8.setText(inDetailMap.get("cusEmail"));

            cusID = inDetailMap.get("cusID");
        } else {
            jTextField1.setText("");
            jTextField4.setText("");
            jTextField8.setText("");
        }
    }

    private void loadTrackingStatus() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `shipping_status` ORDER BY `shipping_status_id` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("type"));
                trackingStatusMap.put(resultSet.getString("type"), resultSet.getInt("shipping_status_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWebsites() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `websites` ORDER BY `websites_id` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                websiteMap.put(resultSet.getString("name"), resultSet.getInt("websites_id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTrackingDetails(String number, String mode) {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `tracking` INNER JOIN `invoice` ON `tracking`.`invoice_invoice_id`=`invoice`.`invoice_id` "
                    + "INNER JOIN `customers` ON `tracking`.`customers_customer_id`=`customers`.`customer_id` "
                    + "INNER JOIN `websites` ON `tracking`.`websites_websites_id`=`websites`.`websites_id` "
                    + "INNER JOIN `shipping_status` ON `tracking`.`shipping_status_shipping_status_id`=`shipping_status`.`shipping_status_id` "
                    + "INNER JOIN `employee` ON `tracking`.`employee_employee_id`=`employee`.`employee_id`"
                    + "WHERE `tracking`.`tracking_no` LIKE '" + number + "%' OR `tracking`.`invoice_invoice_id` LIKE '" + number + "%'"
                    + " ORDER BY `tracking`.`tracking_update_date` " + mode + "");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("tracking.invoice_invoice_id"));
                vector.add(resultSet.getString("tracking.order_no"));
                vector.add(resultSet.getString("tracking.tracking_no"));
                vector.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                vector.add(resultSet.getString("websites.name"));
                vector.add(resultSet.getString("tracking.a_d_to_warehouse"));
                vector.add(resultSet.getString("tracking.a_d_to_customer"));
                vector.add(resultSet.getString("shipping_status.type"));
                vector.add(resultSet.getString("tracking.tracking_update_date"));
                vector.add(resultSet.getString("employee.first_name") + " " + resultSet.getString("employee.last_name"));

                model.addRow(vector);
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trackingSearch() {

        int sortIndex1 = jComboBox3.getSelectedIndex();

        if (sortIndex1 == 0) {
            loadTrackingDetails(jTextField6.getText(), "DESC");
        } else if (sortIndex1 == 1) {
            loadTrackingDetails(jTextField6.getText(), "ASC");
        }

    }

    private void trackingSystemReset() {
        jTable1.setEnabled(true);
        jTable1.clearSelection();
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField8.setText("");
        jTextField7.setText("");
        jTextField15.setText("");
        jTextArea1.setText("");
        jFormattedTextField1.setText("");
        jButton3.setEnabled(false);
        jButton1.setEnabled(true);

        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);

        jTable1.clearSelection();
        mi.addNumMap.clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 21)); // NOI18N
        jLabel1.setText("Tracking System");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Invoice No.");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Order No.");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Tracking No.");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Name");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Website Name");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("New Website Name");

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Arrival Date to Warehouse");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Arrival Date to Customer");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Status");

        IDateEditor dateEditor = jDateChooser1.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Received", "Delayed" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/plus-symbol-button (2).png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Warehouse Receipt");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Email");

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel61.setText("No. of Package");

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel62.setText("Item Weight(lbs)");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel63.setText("Description");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane11.setViewportView(jTextArea1);

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton5.setText("Add");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jTextField3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextField1)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel5))
                                .addGap(49, 49, 49)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField8)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 231, Short.MAX_VALUE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62)
                                    .addComponent(jLabel61))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel63)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel63)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel61))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel62))))
                        .addGap(0, 5, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel10))
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel7))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Order No", "Tracking No", "Customer Name", "Website", "A. D. to Warehouse", "A. D. to Customer", "Status", "Update Date", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Search for Tracking or Invoice No");

        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Update Date DESC", "Update Date ASC" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton4MouseReleased(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Sort by");

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3)
                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //Add new website Name
        String websiteName = jTextField5.getText();

        if (websiteName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter website name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `websites` WHERE `name`='" + websiteName + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Website already added", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    if (jComboBox1.getSelectedIndex() == 0) {
                        MySQL.execute("INSERT INTO `websites` (`name`) VALUES ('" + websiteName + "')");
                        JOptionPane.showMessageDialog(this, "New website added", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        int response = JOptionPane.showConfirmDialog(this, "Do you want to update this website?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                        if (response == JOptionPane.YES_NO_OPTION) {
                            MySQL.execute("UPDATE `websites` SET `name`='" + websiteName + "' WHERE `name`='" + String.valueOf(jComboBox1.getSelectedItem()) + "'");
                            JOptionPane.showMessageDialog(this, "Website Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }

                    }

                    loadWebsites();
                    jTextField5.setText("");

                    jComboBox1.setSelectedItem(websiteName);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //Update Trackin detalis
        String invoiceNo = jTextField1.getText();
        String orderNo = jTextField2.getText();
        String trackingeNo = jTextField3.getText();
        String receiptNo = jTextField7.getText();
        String cusName = jTextField4.getText();
        String cusEmail = jTextField8.getText();
        String website = String.valueOf(jComboBox1.getSelectedItem());
        String status = String.valueOf(jComboBox2.getSelectedItem());

        SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");

        if (invoiceNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter invoice number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (orderNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter order number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (cusName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (website.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select or add website.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Arrival Date to Warehouse.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Arrival Date to Customer.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox2.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select status.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet resultSet = MySQL.execute("SELECT * FROM `tracking` WHERE `tracking_no`='" + trackingeNo + "'");
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This tracking number already used.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    String aDToWarehouse = format1.format(jDateChooser1.getDate());
                    String aDToCustomer = format1.format(jDateChooser2.getDate());

                    int websitenameId = websiteMap.get(website);
                    int statusId = trackingStatusMap.get(status);

                    if (statusId == 1) {
                        Date date1 = new Date();
                        String stringDate1 = format1.format(date1);

                        MySQL.execute("INSERT INTO `tracking` (`tracking_no`,`order_no`,`warehouse_receipt`,`invoice_invoice_id`,`customers_customer_id`,`websites_websites_id`,"
                                + "`a_d_to_warehouse`,`a_d_to_customer`,`shipping_status_shipping_status_id`,`employee_employee_id`,`tracking_update_date`) "
                                + "VALUES ('" + trackingeNo + "','" + orderNo + "','" + receiptNo + "','" + invoiceNo + "','" + cusID + "','" + websitenameId + "',"
                                + "'" + aDToWarehouse + "','" + aDToCustomer + "','" + statusId + "','" + MainInterface.empID + "','" + stringDate1 + "')");

                        if (!mi.addNumMap.isEmpty()) {
                            for (AddNumbers addNumbers : mi.addNumMap.values()) {
                                MySQL.execute("INSERT INTO `otherordernumbers` (`invoice_invoice_id`,`order_number`,`tracking_number`) "
                                        + "VALUES ('" + invoiceNo + "','" + addNumbers.getOrderNumber() + "','" + addNumbers.getTrackingNumber() + "')");
                            }
                        }

                        loadTrackingDetails("", "DESC");
                        trackingSystemReset();
                        mi.loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");
                        mi.addNumMap.clear();

                    } else {
                        JOptionPane.showMessageDialog(this, "Select status in process.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } catch (Exception e) {
                MainInterface.logger.log(Level.WARNING, "Email empty tracking System", e);
                e.printStackTrace();
            }

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void packageReceivedEmailSend() {

        JOptionPane.showMessageDialog(this, "Please wait.....", "Waitting..", JOptionPane.INFORMATION_MESSAGE);

        final String username = "info@source1solutionsgy.com"; // Your email address
        final String password = "ggvdthtrlqbjqagc";       // Your email password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // or your SMTP server
        props.put("mail.smtp.port", "587");            // Use 465 for SSL

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Add recipient email addresses here

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setSubject("Your Package Has Arrived and Is Ready for Pickup!");
            //message.setText("Your Message");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            String emailText = "Hi " + jTextField4.getText() + "\n\n"
                    + "Your package has been received at our Miami Warehouse.\n\n\n"
                    + "Package details :\n\n"
                    //                    + "Warehouse receipt #: " + jTextField7.getText() + "\n"
                    + "Consignee Name : " + jTextField4.getText() + "\n"
                    + "Number of Packages : " + jTextField15.getText() + "\n"
                    + "Weight(lbs.) : " + jFormattedTextField1.getText() + "\n"
                    + "Description : " + jTextArea1.getText() + "\n\n\n"
                    + "Feel free to swing by whenever it's convenient for you. We can't wait to hand over your goodies!\n\n"
                    + "Best regards,";

            // Create the text part of the email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailText);
            multipart.addBodyPart(textPart);

            message.setContent(multipart);

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(jTextField8.getText()));
            Transport.send(message);

            //resetPackReceived();
            JOptionPane.showMessageDialog(this, "Email send success", "Successfull", JOptionPane.INFORMATION_MESSAGE);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (mi.Irow == -1) {

            String inID = jTextField1.getText();

            if (inID.length() == 8) {
                try {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice`"
                            + " INNER JOIN `customers` ON `invoice`.`customers_customer_id`=`customers`.`customer_id` "
                            + "WHERE `invoice`.`invoice_id`='" + inID + "'");

                    if (resultSet.next()) {
                        jTextField4.setText(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                        cusID = resultSet.getString("customers.customer_id");
                    } else {
                        JOptionPane.showMessageDialog(this, "This is not valid invoice.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jTextField4.setText("");
            }

        }

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        trackingSearch();
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        trackingSearch();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MousePressed

    private void jButton4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        trackingSystemReset();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String invoiceNo = jTextField1.getText();
        String orderNo = jTextField2.getText();
        String trackingeNo = jTextField3.getText();
        String receiptNo = jTextField7.getText();
        String cusName = jTextField4.getText();
        String cusEmail = jTextField8.getText();
        String website = String.valueOf(jComboBox1.getSelectedItem());
        String status = String.valueOf(jComboBox2.getSelectedItem());

        SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");

        if (invoiceNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter invoice number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (orderNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter order number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (cusName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (website.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select or add website.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Arrival Date to Warehouse.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select Arrival Date to Customer.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox2.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select status.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                String aDToWarehouse = format1.format(jDateChooser1.getDate());
                String aDToCustomer = format1.format(jDateChooser2.getDate());

                int websitenameId = websiteMap.get(website);
                int statusId = trackingStatusMap.get(status);

                Date date1 = new Date();
                String stringDate1 = format1.format(date1);

                if (!cusEmail.isEmpty()) {

                    if (jComboBox2.getSelectedIndex() == 2) {
                        //Send email pacage received
                        String noPac = jTextField15.getText();
                        String pacWeight = jFormattedTextField1.getText();
                        String desOfPac = jTextArea1.getText();

                        if (!cusEmail.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
                                + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")) {
                            JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (noPac.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter number of package.", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (pacWeight.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter package weight.", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (desOfPac.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Please enter description of package.", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {

                            try {
                                ResultSet resultSet2 = MySQL.execute("SELECT * FROM `package_received` WHERE `invoice_invoice_id`='" + invoiceNo + "'");
                                if (resultSet2.next()) {
                                    JOptionPane.showMessageDialog(this, "This email already sent.", "Warning", JOptionPane.WARNING_MESSAGE);
                                } else {

                                    MySQL.execute("INSERT INTO `package_received` (`invoice_invoice_id`,`customers_customer_id`,`no_of_packages`,"
                                            + "`package_weight`,`description`,`email_send`,`warehouse_receipt`) "
                                            + "VALUES ('" + invoiceNo + "','" + cusID + "','" + noPac + "','" + pacWeight + "','" + desOfPac + "','" + stringDate1 + "','" + receiptNo + "')");

                                    packageReceivedEmailSend();

                                    MySQL.execute("UPDATE `tracking` SET "
                                            + "`tracking_no`='" + trackingeNo + "',"
                                            + "`order_no`='" + orderNo + "',"
                                            + "`warehouse_receipt`='" + receiptNo + "',"
                                            + "`a_d_to_warehouse`='" + aDToWarehouse + "',"
                                            + "`a_d_to_customer`='" + aDToCustomer + "',"
                                            + "`shipping_status_shipping_status_id`='" + statusId + "',"
                                            + "`tracking_update_date`='" + stringDate1 + "' "
                                            + "WHERE `invoice_invoice_id`='" + invoiceNo + "'");

                                    if (!mi.addNumMap.isEmpty()) {
                                        for (AddNumbers addNumbers : mi.addNumMap.values()) {
                                            ResultSet r = MySQL.execute("SELECT * FROM `otherordernumbers` WHERE `order_number`='" + addNumbers.getOrderNumber() + "' AND `tracking_number`='" + addNumbers.getTrackingNumber() + "'");
                                            if (!r.next()) {
                                                MySQL.execute("INSERT INTO `otherordernumbers` (`invoice_invoice_id`,`order_number`,`tracking_number`) "
                                                        + "VALUES ('" + invoiceNo + "','" + addNumbers.getOrderNumber() + "','" + addNumbers.getTrackingNumber() + "')");
                                            }

                                        }
                                    }

                                    mi.loadReceivedPacDetails("");

                                    loadTrackingDetails("", "DESC");
                                    trackingSystemReset();
                                    mi.loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");

                                }
                            } catch (Exception e) {
                                MainInterface.logger.log(Level.WARNING, "Email not empty tracking System", e);
                                e.printStackTrace();
                            }

                        }

                        //packageReceivedEmailSend();
                    } else {
                        MySQL.execute("UPDATE `tracking` SET "
                                + "`tracking_no`='" + trackingeNo + "',"
                                + "`order_no`='" + orderNo + "',"
                                + "`warehouse_receipt`='" + receiptNo + "',"
                                + "`a_d_to_warehouse`='" + aDToWarehouse + "',"
                                + "`a_d_to_customer`='" + aDToCustomer + "',"
                                + "`shipping_status_shipping_status_id`='" + statusId + "',"
                                + "`tracking_update_date`='" + stringDate1 + "' "
                                + "WHERE `invoice_invoice_id`='" + invoiceNo + "'");

                        if (!mi.addNumMap.isEmpty()) {
                            for (AddNumbers addNumbers : mi.addNumMap.values()) {
                                ResultSet r = MySQL.execute("SELECT * FROM `otherordernumbers` WHERE `order_number`='" + addNumbers.getOrderNumber() + "' AND `tracking_number`='" + addNumbers.getTrackingNumber() + "'");
                                if (!r.next()) {
                                    MySQL.execute("INSERT INTO `otherordernumbers` (`invoice_invoice_id`,`order_number`,`tracking_number`) "
                                            + "VALUES ('" + invoiceNo + "','" + addNumbers.getOrderNumber() + "','" + addNumbers.getTrackingNumber() + "')");
                                }

                            }
                        }

                        loadTrackingDetails("", "DESC");
                        trackingSystemReset();
                        mi.loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");

                    }

                } else {
                    MySQL.execute("UPDATE `tracking` SET "
                            + "`tracking_no`='" + trackingeNo + "',"
                            + "`order_no`='" + orderNo + "',"
                            + "`warehouse_receipt`='" + receiptNo + "',"
                            + "`a_d_to_warehouse`='" + aDToWarehouse + "',"
                            + "`a_d_to_customer`='" + aDToCustomer + "',"
                            + "`shipping_status_shipping_status_id`='" + statusId + "',"
                            + "`tracking_update_date`='" + stringDate1 + "' "
                            + "WHERE `invoice_invoice_id`='" + invoiceNo + "'");

                    if (!mi.addNumMap.isEmpty()) {
                        for (AddNumbers addNumbers : mi.addNumMap.values()) {
                            ResultSet r = MySQL.execute("SELECT * FROM `otherordernumbers` WHERE `order_number`='" + addNumbers.getOrderNumber() + "' AND `tracking_number`='" + addNumbers.getTrackingNumber() + "'");
                            if (!r.next()) {
                                MySQL.execute("INSERT INTO `otherordernumbers` (`invoice_invoice_id`,`order_number`,`tracking_number`) "
                                        + "VALUES ('" + invoiceNo + "','" + addNumbers.getOrderNumber() + "','" + addNumbers.getTrackingNumber() + "')");
                            }

                        }
                    }

                    loadTrackingDetails("", "DESC");
                    trackingSystemReset();
                    mi.loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");

                }

            } catch (Exception e) {
                MainInterface.logger.log(Level.WARNING, "Email empty tracking System", e);
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int tRow = jTable1.getSelectedRow();
            jTable1.setEnabled(false);

            if (String.valueOf(jTable1.getValueAt(tRow, 7)).equals("Received") || String.valueOf(jTable1.getValueAt(tRow, 7)).equals("Delayed") || String.valueOf(jTable1.getValueAt(tRow, 7)).equals("Lost in Transit") || String.valueOf(jTable1.getValueAt(tRow, 7)).equals("Damaged")) {
                JOptionPane.showMessageDialog(this, "This tracking number already updated.", "Warning", JOptionPane.WARNING_MESSAGE);
                trackingSystemReset();
            } else {
                try {

                    ResultSet r = MySQL.execute("SELECT * FROM `otherordernumbers` WHERE `invoice_invoice_id`='" + String.valueOf(jTable1.getValueAt(tRow, 0)) + "'");
                    int index = 0;
                    while (r.next()) {
                        AddNumbers addNumbers = new AddNumbers();
                        addNumbers.setOrderNumber(r.getString("order_number"));
                        addNumbers.setTrackingNumber(r.getString("tracking_number"));

                        mi.addNumMap.put("item_" + index, addNumbers);
                        index++;
                    }

                    ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` "
                            + "INNER JOIN `invoice` ON `customers`.`customer_id`=`invoice`.`customers_customer_id` "
                            + "WHERE `invoice`.`invoice_id`='" + String.valueOf(jTable1.getValueAt(tRow, 0)) + "'");

                    if (resultSet.next()) {

                        cusID = resultSet.getString("customers.customer_id");

                        jButton3.setEnabled(true);
                        jButton1.setEnabled(false);

                        jTextField1.setText(String.valueOf(jTable1.getValueAt(tRow, 0)));
                        jTextField2.setText(String.valueOf(jTable1.getValueAt(tRow, 1)));
                        jTextField3.setText(String.valueOf(jTable1.getValueAt(tRow, 2)));
                        jTextField4.setText(String.valueOf(jTable1.getValueAt(tRow, 3)));
                        jTextField8.setText(resultSet.getString("customers.email"));

                        jComboBox1.setSelectedItem(String.valueOf(jTable1.getValueAt(tRow, 4)));

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat format1 = new SimpleDateFormat("MMM d, y");
                        Date date1;
                        Date date2;

                        Date parsedDate1 = inputFormat.parse(String.valueOf(jTable1.getValueAt(tRow, 5)));
                        String formattedDate1 = format1.format(parsedDate1);

                        Date parsedDate2 = inputFormat.parse(String.valueOf(jTable1.getValueAt(tRow, 6)));
                        String formattedDate2 = format1.format(parsedDate2);

                        date1 = format1.parse(formattedDate1);
                        jDateChooser1.setDate(date1);

                        date2 = format1.parse(formattedDate2);
                        jDateChooser2.setDate(date2);

                    } else {
                        JOptionPane.showMessageDialog(this, "This customer missing.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace(); // Handle the parsing exception appropriately
                }
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String inID = jTextField1.getText();
        if (inID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter invoice number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            NewOrderNumbers newOrderNumbers = new NewOrderNumbers(mi, true, inID);
            newOrderNumbers.setVisible(true);
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
