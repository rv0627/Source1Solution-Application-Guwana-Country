/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import com.mysql.cj.jdbc.Blob;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.MySQL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

/**
 *
 * @author Admin
 */
public class Customer_Registration extends javax.swing.JDialog {

    //public static Logger logger = Logger.getLogger("source1solution");
    /**
     * Creates new form Open
     */
    MainInterface mi;
    HashMap<String, String> updateDeatilMap = new HashMap<>();

    public Customer_Registration(java.awt.Frame parent, boolean modal, HashMap<String, String> cusUpdateMap) {
        super(parent, modal);
        initComponents();

        this.setTitle("Source 1 Solutions - Customer Registration");
        mi = (MainInterface) parent;

        generateCustomerID();
        loadAccountType();

        if (mi.row != -1) {
            updateDeatilMap = cusUpdateMap;

            jLabel1.setText("Update Customer");
            jButton2.setEnabled(false);
            updateDetailsLoad();

            jButton5.setEnabled(true);
        } else {
            jButton3.setEnabled(false);
//            jButton5.setEnabled(false);
        }

    }

    private String lastCustomerID;
    String customerID;

    private void generateCustomerID() {

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` ORDER BY `customer_id` DESC LIMIT 1");
            if (resultSet.next()) {
                lastCustomerID = resultSet.getString("customer_id");

            } else {
                lastCustomerID = "SOU0000";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Increment the last employee ID
        int numericPart = Integer.parseInt(lastCustomerID.substring(3));
        numericPart++;

        // Customize the employee ID format
        customerID = String.format("SOU%04d", numericPart);

        jTextField8.setText(customerID);
    }

    //loadAccountType
    HashMap<String, Integer> accountTypeMap = new HashMap<>();

    private void loadAccountType() {

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `account_type` ORDER BY `account_type_id` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("type"));
                accountTypeMap.put(resultSet.getString("type"), resultSet.getInt("account_type_id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reset() {

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField9.setText("");
        jTextField8.setText(customerID);

        jComboBox1.setSelectedIndex(0);
        jTextField10.setText("");
        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        jTextField12.setText("");
        jTextArea1.setText("");

        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
//        jButton5.setEnabled(false);

        jLabel1.setText("New Customer");

    }

    private void updateDetailsLoad() {
        jTextField8.setText(updateDeatilMap.get("cusID"));
        jTextField1.setText(updateDeatilMap.get("fname"));
        jTextField2.setText(updateDeatilMap.get("lname"));
        jTextField3.setText(updateDeatilMap.get("nic"));
        jTextField4.setText(updateDeatilMap.get("email"));
        jTextField5.setText(updateDeatilMap.get("mobile"));
        jTextField6.setText(updateDeatilMap.get("line1"));
        jTextField7.setText(updateDeatilMap.get("line2"));
        jTextField9.setText(updateDeatilMap.get("line3"));
        jTextField9.setText(updateDeatilMap.get("line3"));
        jFormattedTextField2.setText(updateDeatilMap.get("ratePerLb"));

        int typeId = Integer.parseInt(updateDeatilMap.get("accountTypeID"));

        jComboBox1.setSelectedIndex(typeId);

        jTextField10.setText(updateDeatilMap.get("taxNo"));
        jFormattedTextField1.setText(updateDeatilMap.get("credit"));
        jTextField12.setText(updateDeatilMap.get("uploadPath"));
        jTextArea1.setText(updateDeatilMap.get("note"));
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
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField10 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField12 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1040, 510));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 21)); // NOI18N
        jLabel1.setText("New Customer");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Account Type");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Tax Identification No");

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Credit Limit ($)");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Documents");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Upload ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField12.setEditable(false);
        jTextField12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Notes");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Register");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setText("View document");
        jButton5.setEnabled(false);
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
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(67, 67, 67)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(28, 28, 28)
                                .addComponent(jTextField10)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(147, 147, 147)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jLabel15)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Account #");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("First Name");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Last Name");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("National ID");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Email");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mobile");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Address Line 1");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Address Line 2");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Address Line 3");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField8.setEditable(false);
        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Rate per lb ($)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextField2)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addComponent(jTextField4)
                    .addComponent(jTextField5)
                    .addComponent(jTextField6)
                    .addComponent(jTextField7)
                    .addComponent(jTextField9)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed
    String filePath;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();

        // Customize the file chooser here (e.g., set default directory, file filter, etc.)
        //fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        //fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        // Show the file chooser dialog
        int returnValue = fileChooser.showOpenDialog(null);

        // Process the selected file (if any)
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
            jTextField12.setText(filePath);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    //downloadDocument
    private void downloadFile() {

        try {
            String workingDirectory = jTextField12.getText();
            File pdfFile = new File(workingDirectory);

            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this, "PDF file not found in the 'Customer Documents' folder.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening the PDF file.");
        }

    }
//downloadDocument

    String originalPath;
    String destinationFolder;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String fname = jTextField1.getText();
        String lname = jTextField2.getText();
        String nic = jTextField3.getText();
        String email = jTextField4.getText();
        String mobile = jTextField5.getText();
        String line1 = jTextField6.getText();
        String line2 = jTextField7.getText();
        String line3 = jTextField9.getText();
        String ratePerLb = jFormattedTextField2.getText();
        String accountType = String.valueOf(jComboBox1.getSelectedItem());
        String taxIdentifyNo = jTextField10.getText();
        String creditLimits = jFormattedTextField1.getText();
        String notes = jTextArea1.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!mobile.matches("^[0-9 -+]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (line1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Address.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (ratePerLb.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter rate per lb.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (accountType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select account type.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (creditLimits.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter credit limit.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

//             else if (filePath == null) {
//            JOptionPane.showMessageDialog(this, "Please select document.", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
//else if (!mobile.matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
//            JOptionPane.showMessageDialog(this, "Invalid mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
            if (!email.isEmpty()) {
                if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
                        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")) {
                    JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    try {

                        ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE (`first_name`='" + fname + "' AND `last_name`='" + lname + "') OR `email`='" + email + "'");

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(this, "Customer already registered", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {

                            Date date1 = new Date();
                            SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
                            String stringDate1 = format1.format(date1);

                            int accountTypeId = accountTypeMap.get(accountType);

                            if (filePath != null) {
                                // Get the current working directory where your application is running
                                File pdfFile = new File(filePath);
                                // Get or generate a unique identifier for the computer

                                // Replace "base/destination/folder" with the base destination folder
                                String baseDestinationFolder = System.getProperty("user.home") + File.separator + "customers documents";

                                destinationFolder = baseDestinationFolder + File.separator;

                                try {
                                    storePDF(pdfFile, destinationFolder);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                originalPath = String.valueOf(Original).replace("\\", "\\\\"); // Use a forward slash for path separation in resources

                                // Further processing or handling of the uploaded file
                            } else {
                                originalPath = "Not selected file";
                            }

                            MySQL.execute("INSERT INTO `customers` (`customer_id`,`first_name`,`last_name`,`nic`,`email`,`mobile`,`address_line1`,`address_line2`,`address_line3`,`rate_per_lb`,"
                                    + "`account_type_account_type_id`,`tax_identification_no`,`credit_limit`,`upload_document_path`,`notes`,`registered_date`,`employee_employee_id`)"
                                    + "VALUES ('" + customerID + "','" + fname + "','" + lname + "','" + nic + "','" + email + "','" + mobile + "','" + line1 + "','" + line2 + "','" + line3 + "','" + ratePerLb + "',"
                                    + "'" + accountTypeId + "','" + taxIdentifyNo + "','" + creditLimits + "','" + originalPath + "','" + notes + "','" + stringDate1 + "','" + MainInterface.empID + "')");

                            JOptionPane.showMessageDialog(this, "Customer registered.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                            generateCustomerID();
                            reset();

                            mi.loadCustomers1("", "customer_id", "ASC");
                        }

                    } catch (Exception e) {
                        MainInterface.logger.log(Level.WARNING, "Have Email customer registration.", e);
                        //e.printStackTrace();
                    }
                }
            } else {
                try {

                    ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE (`first_name`='" + fname + "' AND `last_name`='" + lname + "')");

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Customer already registered", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {

                        Date date1 = new Date();
                        SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
                        String stringDate1 = format1.format(date1);

                        int accountTypeId = accountTypeMap.get(accountType);

                        if (filePath != null) {
                            //uploadDocument
                            File pdfFile = new File(filePath);
                            // Get or generate a unique identifier for the computer

                            // Replace "base/destination/folder" with the base destination folder
                            String baseDestinationFolder = System.getProperty("user.home") + File.separator + "customers documents";

                            destinationFolder = baseDestinationFolder + File.separator;

                            try {
                                storePDF(pdfFile, destinationFolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            originalPath = String.valueOf(Original).replace("\\", "\\\\"); // Use a forward slash for path separation in resources

                        } else {
                            originalPath = "Not selected file";
                        }

                        MySQL.execute("INSERT INTO `customers` (`customer_id`,`first_name`,`last_name`,`nic`,`email`,`mobile`,`address_line1`,`address_line2`,`address_line3`,`rate_per_lb`,"
                                + "`account_type_account_type_id`,`tax_identification_no`,`credit_limit`,`upload_document_path`,`notes`,`registered_date`,`employee_employee_id`)"
                                + "VALUES ('" + customerID + "','" + fname + "','" + lname + "','" + nic + "','" + email + "','" + mobile + "','" + line1 + "','" + line2 + "','" + line3 + "','" + ratePerLb + "',"
                                + "'" + accountTypeId + "','" + taxIdentifyNo + "','" + creditLimits + "','" + originalPath + "','" + notes + "','" + stringDate1 + "','" + MainInterface.empID + "')");

                        JOptionPane.showMessageDialog(this, "Customer registered.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                        generateCustomerID();
                        reset();

                        mi.loadCustomers1("", "customer_id", "ASC");

                    }

                } catch (Exception e) {
                    MainInterface.logger.log(Level.WARNING, "Empty Email customer registration.", e);
                    //e.printStackTrace();
                }
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed


    private static Path Original;

    private static void storePDF(File pdfFile, String destinationFolder) throws IOException {
        // Create the destination folder if it doesn't exist
        File folder = new File(destinationFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Construct the destination path
        Path destinationPath = folder.toPath().resolve(pdfFile.getName());
        Original = destinationPath;
        // Copy the PDF file to the destination folder
        Files.copy(pdfFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // If you need to access the PDF file later, you can use the destinationPath
        // You can also perform additional operations on the stored file as needed
    }

    private void jButton4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MousePressed

    private void jButton4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //UpdateCustomer

        String cusID = jTextField8.getText();
        String fname = jTextField1.getText();
        String lname = jTextField2.getText();
        String nic = jTextField3.getText();
        String email = jTextField4.getText();
        String mobile = jTextField5.getText();
        String line1 = jTextField6.getText();
        String line2 = jTextField7.getText();
        String line3 = jTextField9.getText();
        String rateperLB = jFormattedTextField2.getText();
        String accountType = String.valueOf(jComboBox1.getSelectedItem());
        String taxIdentifyNo = jTextField10.getText();
        String creditLimits = jFormattedTextField1.getText();
        String notes = jTextArea1.getText();

        String path = jTextField12.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!mobile.matches("^[0-9 -+]+$")) {
            JOptionPane.showMessageDialog(this, "Invalid mobile.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (line1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Address.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rateperLB.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter rate per lb.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (accountType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select account type.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (creditLimits.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter credit limit.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

//            if (!email.isEmpty()) {
//                if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
//                        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")) {
//                    JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);
//                }
//            }
            if (!email.isEmpty()) {
                if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
                        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")) {
                    JOptionPane.showMessageDialog(this, "Invalid email", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    try {

                        ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `email`='" + email + "'");

                        boolean canUpdate = false;
                        if (resultSet.next()) {

                            if (resultSet.getString("customer_id").equals(cusID)) {
                                canUpdate = true;
                            } else {
                                JOptionPane.showMessageDialog(this, "Email already used", "Warning", JOptionPane.WARNING_MESSAGE);
                            }

                        } else {
                            canUpdate = true;
                        }

                        if (canUpdate) {
                            int accountTypeId = accountTypeMap.get(accountType);

                            if (!"Not selected file".equals(jTextField12.getText())) {
                                //uploadDocument
                                //String originalPath = null;

                                filePath = path;

                                File pdfFile = new File(filePath);
                                // Get or generate a unique identifier for the computer

                                // Replace "base/destination/folder" with the base destination folder
                                String baseDestinationFolder = System.getProperty("user.home") + File.separator + "customers documents";

                                destinationFolder = baseDestinationFolder + File.separator;

                                try {
                                    storePDF(pdfFile, destinationFolder);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                originalPath = String.valueOf(Original).replace("\\", "\\\\"); // Use a forward slash for path separation in resources

                            } else {
                                originalPath = "Not selected file";
                            }

                            MySQL.execute("UPDATE `customers` SET"
                                    + "`first_name`='" + fname + "',"
                                    + "`last_name`='" + lname + "',"
                                    + "`nic`='" + nic + "',"
                                    + "`email`='" + email + "',"
                                    + "`mobile`='" + mobile + "',"
                                    + "`address_line1`='" + line1 + "',"
                                    + "`address_line2`='" + line2 + "',"
                                    + "`address_line3`='" + line3 + "',"
                                    + "`rate_per_lb`='" + rateperLB + "',"
                                    + "`account_type_account_type_id`='" + accountTypeId + "',"
                                    + "`tax_identification_no`='" + taxIdentifyNo + "',"
                                    + "`credit_limit`='" + creditLimits + "',"
                                    + "`upload_document_path`='" + originalPath + "',"
                                    + "`notes`='" + notes + "'"
                                    + "WHERE `customer_id`='" + cusID + "'");

                            JOptionPane.showMessageDialog(this, "Customer Updated.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                            reset();
                            mi.loadCustomers1("", "customer_id", "ASC");
                        }

                    } catch (Exception e) {
                        MainInterface.logger.log(Level.WARNING, "Have Email customer Update.", e);
                        //e.printStackTrace();
                    }
                }
            } else {
                try {

                    ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `first_name`='" + fname + "' AND `last_name`='" + lname + "'");

                    boolean canUpdate = false;
                    if (resultSet.next()) {

                        if (resultSet.getString("customer_id").equals(cusID)) {
                            canUpdate = true;
                        } else {
                            JOptionPane.showMessageDialog(this, "Customer name already used", "Warning", JOptionPane.WARNING_MESSAGE);
                        }

                    } else {
                        canUpdate = true;
                    }

                    if (canUpdate) {
                        int accountTypeId = accountTypeMap.get(accountType);

                        if (!"Not selected file".equals(jTextField12.getText())) {
                            //uploadDocument
                            //String originalPath = null;

                            filePath = path;

                            File pdfFile = new File(filePath);
                            // Get or generate a unique identifier for the computer

                            // Replace "base/destination/folder" with the base destination folder
                            String baseDestinationFolder = System.getProperty("user.home") + File.separator + "customers documents";

                            destinationFolder = baseDestinationFolder + File.separator;

                            try {
                                storePDF(pdfFile, destinationFolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            originalPath = String.valueOf(Original).replace("\\", "\\\\"); // Use a forward slash for path separation in resources

                        } else {
                            originalPath = "Not selected file";
                        }

                        MySQL.execute("UPDATE `customers` SET"
                                + "`first_name`='" + fname + "',"
                                + "`last_name`='" + lname + "',"
                                + "`nic`='" + nic + "',"
                                + "`email`='" + email + "',"
                                + "`mobile`='" + mobile + "',"
                                + "`address_line1`='" + line1 + "',"
                                + "`address_line2`='" + line2 + "',"
                                + "`address_line3`='" + line3 + "',"
                                + "`rate_per_lb`='" + rateperLB + "',"
                                + "`account_type_account_type_id`='" + accountTypeId + "',"
                                + "`tax_identification_no`='" + taxIdentifyNo + "',"
                                + "`credit_limit`='" + creditLimits + "',"
                                + "`upload_document_path`='" + originalPath + "',"
                                + "`notes`='" + notes + "'"
                                + "WHERE `customer_id`='" + cusID + "'");

                        JOptionPane.showMessageDialog(this, "Customer Updated.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                        reset();
                        mi.loadCustomers1("", "customer_id", "ASC");
                    }

                } catch (Exception e) {
                    MainInterface.logger.log(Level.WARNING, "Not Email customer Update.", e);
                    //e.printStackTrace();
                }
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        downloadFile();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
