/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.sun.mail.smtp.SMTPSendFailedException;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import javax.mail.util.ByteArrayDataSource;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ExcelSheetData;
import model.MySQL;
import model.ShippingReceiptItems;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.ImageIcon;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import model.AddNumbers;
import model.PaymentDetail;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
 *
 * @author ravin
 */
public class MainInterface extends javax.swing.JFrame {

    public static Logger logger = Logger.getLogger("source1solution");

    HashMap<String, AddNumbers> addNumMap = new HashMap<>();
    /**
     * Creates new form MainInterface
     */
    public static String emp;
    public static String empID;

    public MainInterface(String employee) {
        initComponents();

        this.setTitle("Source 1 Solutions - Management");
        ImageIcon imageIcon = new ImageIcon(MainInterface.class.getResource("/img/t3.png"));
        this.setIconImage(imageIcon.getImage());

        emp = employee;
        empID = SignIn.getEmployeeID();

        jLabel25.setText(employee);

        if (employee.equals("Sobas Ramdyal")) {
            jButton21.setEnabled(true);
        } else {
            jButton21.setEnabled(false);
        }

        loadCustomers1("", "customer_id", "ASC");
        jDateChooser1.getDateEditor().setEnabled(false);
        jDateChooser2.getDateEditor().setEnabled(false);
        jDateChooser3.getDateEditor().setEnabled(false);

    }

    public void ennableFalseMi() {
        this.setEnabled(false);
    }

    public void ennableTrueMi() {
        this.setEnabled(true);
    }

//    private void removeRowTable() {
//        try {
//            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` "
//                    + "INNER JOIN `tracking` ON WHERE `invoice_invoice_id`=''");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //customerTab
    public void loadCustomers1(String cusName, String column, String method) {

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `first_name` LIKE '" + cusName + "%' OR `last_name` LIKE '" + cusName + "%' ORDER BY `" + column + "` " + method + "");

            DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            model1.setRowCount(0);

            DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
            model2.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer seCellRenderer = new DefaultTableCellRenderer();
            seCellRenderer.setHorizontalAlignment(JLabel.RIGHT);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("customer_id"));
                vector.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("credit_limit"));
                vector.add(resultSet.getString("registered_date"));

                model1.addRow(vector);
                model2.addRow(vector);
            }

            for (int i = 0; i < 1; i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            for (int i = 3; i < model1.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            for (int i = 0; i < 1; i++) {
                jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            for (int i = 3; i < model1.getColumnCount(); i++) {
                jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void customerSearch() {

        int sortIndex1 = jComboBox1.getSelectedIndex();
        int sortIndex2 = jComboBox2.getSelectedIndex();

        if (sortIndex1 == 0) {
            loadCustomers1(jTextField1.getText(), "customer_id", "ASC");
        } else if (sortIndex1 == 1) {
            loadCustomers1(jTextField1.getText(), "customer_id", "DESC");
        } else if (sortIndex1 == 2) {
            loadCustomers1(jTextField1.getText(), "registered_date", "ASC");
        } else if (sortIndex1 == 3) {
            loadCustomers1(jTextField1.getText(), "registered_date", "DESC");
        }

        if (jPanel10.isVisible()) {
            if (sortIndex2 == 0) {
                loadCustomers1(jTextField2.getText(), "customer_id", "ASC");
            } else if (sortIndex2 == 1) {
                loadCustomers1(jTextField2.getText(), "customer_id", "DESC");
            } else if (sortIndex2 == 2) {
                loadCustomers1(jTextField2.getText(), "registered_date", "ASC");
            } else if (sortIndex2 == 3) {
                loadCustomers1(jTextField2.getText(), "registered_date", "DESC");
            }
        }

    }

    private void admintabReset() {
        jTextField1.grabFocus();
        jTable1.clearSelection();
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        row = -1;
        jTable1.setEnabled(true);
        jButton1.setEnabled(true);

        loadCustomers1("", "customer_id", "ASC");
    }

    private void customertabReset() {
        jTextField2.grabFocus();
        jTable2.clearSelection();
        jTextField2.setText("");
        jComboBox2.setSelectedIndex(0);
        row = -1;
        jTable2.setEnabled(true);
        jButton2.setEnabled(true);

        loadCustomers1("", "customer_id", "ASC");
    }
    //customerTab

    //transactionTab
    public void loadInvoices(String join, String invoiceNo, String column, String method) {
        try {

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            DefaultTableModel trackingModel = (DefaultTableModel) jTable4.getModel();
            trackingModel.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            if (join.equals("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`")) {
                ResultSet resultSet2 = MySQL.execute("SELECT * FROM `invoice` "
                        + "INNER JOIN `employee` ON `invoice`.`employee_employee_id`=`employee`.`employee_id`"
                        + "INNER JOIN `customers` ON `invoice`.`customers_customer_id`=`customers`.`customer_id` "
                        + "" + join + " WHERE `tracking`.`invoice_invoice_id` IS NULL AND `invoice_id` LIKE '" + invoiceNo + "%' ORDER BY `" + column + "` " + method + "");

                while (resultSet2.next()) {
                    Vector<String> v = new Vector<>();
                    v.add(resultSet2.getString("invoice.invoice_id"));
                    v.add(resultSet2.getString("customers.customer_id"));
                    v.add(resultSet2.getString("customers.first_name") + " " + resultSet2.getString("customers.last_name"));
                    v.add(resultSet2.getString("customers.email"));
                    v.add(resultSet2.getString("invoice.grandTotal"));
                    v.add(resultSet2.getString("invoice.invoice_date"));
                    v.add(resultSet2.getString("employee.first_name") + " " + resultSet2.getString("employee.last_name"));

                    trackingModel.addRow(v);
                }

            } else {
                ResultSet resultSet1 = MySQL.execute("SELECT * FROM `invoice` "
                        + "INNER JOIN `employee` ON `invoice`.`employee_employee_id`=`employee`.`employee_id`"
                        + "INNER JOIN `customers` ON `invoice`.`customers_customer_id`=`customers`.`customer_id` "
                        + "" + join + " WHERE `invoice_id` LIKE '" + invoiceNo + "%' ORDER BY `" + column + "` " + method + "");

                while (resultSet1.next()) {
                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet1.getString("invoice.invoice_id"));
                    vector.add(resultSet1.getString("customers.customer_id"));
                    vector.add(resultSet1.getString("customers.first_name") + " " + resultSet1.getString("customers.last_name"));
                    vector.add(resultSet1.getString("invoice.grandTotal"));
                    vector.add(resultSet1.getString("invoice.invoice_date"));
                    vector.add(resultSet1.getString("employee.first_name") + " " + resultSet1.getString("employee.last_name"));

                    model.addRow(vector);
                }
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                jTable3.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            for (int i = 0; i < trackingModel.getColumnCount(); i++) {
                jTable4.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invoiceSearch() {

        int sortIndex1 = jComboBox3.getSelectedIndex();
        int sortIndex2 = jComboBox4.getSelectedIndex();

        if (sortIndex1 == 0) {
            loadInvoices("", jTextField3.getText(), "invoice_id", "ASC");
        } else if (sortIndex1 == 1) {
            loadInvoices("", jTextField3.getText(), "invoice_id", "DESC");
        } else if (sortIndex1 == 2) {
            loadInvoices("", jTextField3.getText(), "invoice_date", "ASC");
        } else if (sortIndex1 == 3) {
            loadInvoices("", jTextField3.getText(), "invoice_date", "DESC");
        }

        if (jPanel12.isVisible()) {
            if (sortIndex2 == 0) {
                loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", jTextField4.getText(), "invoice_id", "ASC");
            } else if (sortIndex2 == 1) {
                loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", jTextField4.getText(), "invoice_id", "DESC");
            } else if (sortIndex2 == 2) {
                loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", jTextField4.getText(), "invoice_date", "ASC");
            } else if (sortIndex2 == 3) {
                loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", jTextField4.getText(), "invoice_date", "DESC");
            }
        }

    }

    private void transactionTabReset() {

        jTextField3.setText("");
        jComboBox3.setSelectedIndex(0);
        jTable3.clearSelection();
        loadInvoices("", "", "invoice_id", "ASC");

        jButton7.grabFocus();

    }
    //transactionTab

    //Tracking Tab
    private void trackingTabReset() {

        Irow = -1;
        jTextField4.setText("");
        jComboBox4.setSelectedIndex(0);
        jTable4.clearSelection();
        loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");
        jButton10.grabFocus();

    }
    //Tracking Tab

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton10 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jLabel67 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel39 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton34 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel43 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel32 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton28 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jTextField9 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel24 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel52 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jButton32 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jPanel57 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel66 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton30 = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton35 = new javax.swing.JButton();

        jMenuItem1.setText("Print Shipping receipt");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMenuItem4.setText("Open customer account");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        jMenuItem2.setText("Remove paid amount");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Delete invoice");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 614));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/IMG2.jpg"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Admin");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Customer");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Transaction");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Tracking");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Reports");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("Payments");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Expenses");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Uploads");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Notify");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Package Received");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/dashboard (7).png"))); // NOI18N
        jLabel13.setText(" ");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/customer.png"))); // NOI18N
        jLabel14.setText(" ");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/transfer.png"))); // NOI18N
        jLabel15.setText(" ");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delivery.png"))); // NOI18N
        jLabel16.setText(" ");
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/report.png"))); // NOI18N
        jLabel17.setText(" ");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/payment-method.png"))); // NOI18N
        jLabel18.setText(" ");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/expenses.png"))); // NOI18N
        jLabel19.setText(" ");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cloud-computing.png"))); // NOI18N
        jLabel20.setText(" ");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/notify.png"))); // NOI18N
        jLabel21.setText(" ");
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/receive.png"))); // NOI18N
        jLabel22.setText(" ");
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Logout");
        jLabel23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/profile-user.png"))); // NOI18N

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout (1).png"))); // NOI18N
        jLabel35.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel35))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setLayout(new java.awt.CardLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel27.setText("Admin Panel");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(794, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer No", "Customer Name", "Email", "Mobile", "Credit Limit ($)", "Register Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Search from Customer Name");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Customer ID ASC", "Customer ID DESC", "Date ASC", "Date DESC" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("New Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton21.setText("Employee Registration");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton22MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton22MouseReleased(evt);
            }
        });
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel55.setText("Sort by");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel55))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel55))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel6, "card2");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel28.setText("Customer Tab");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer No", "Customer Name", "Email", "Mobile", "Credit Limit ($)", "Register Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        jScrollPane2.setViewportView(jTable2);

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("Search from Customer Name");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Customer ID ASC", "Customer ID DESC", "Date ASC", "Date DESC" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("New Customer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setText("New Transaction");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setText("Quotations");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setText("Delete");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton23MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton23MouseReleased(evt);
            }
        });
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel56.setText("Sort by");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 248, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel56))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jComboBox2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel10, "card3");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel38.setText("Transaction Tab");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Customer ID", "Customer Name", "Grand Total ($)", "Date of invoice", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel39.setText("Search from Invoice Id");

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invoice ID ASC", "Invoice ID DESC", "Date ASC", "Date DESC" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setText("New Transaction");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setText("Quotations");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton25MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton25MouseReleased(evt);
            }
        });
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel57.setText("Sort by");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButton25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel11, "card4");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel40.setText("Tracking Tab");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Customer ID", "Customer Name", "Customer Email", "Grand Total ($)", "Date of invoice", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel41.setText("Search from Invoice No");

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invoice ID ASC", "Invoice ID DESC", "Date ASC", "Date DESC" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton10.setText("View Tracking List");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setText("Order List");

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton26MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton26MouseReleased(evt);
            }
        });
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel67.setText("Sort by");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel67)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel12, "card5");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel43.setText("Reports Tab");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addContainerGap(799, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton11.setText("Service Charge Report");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton12.setText("Shipping Report");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton13.setText("Number of Active Customers Report");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton14.setText("Changes to Transaction Report");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton15.setText("Invoice Report");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton16.setText("Quotations Report");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton17.setText("Daily Report for all Transactions");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton18.setText("Payment Received Report");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(205, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(298, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel13, "card6");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel29.setText("Payments Tab");

        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel59.setText("Search from Customer");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField12))
                .addContainerGap())
        );

        jTable5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Customer Name", "Paid Amount", "Due Payment", "Status", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel46.setText("Search from Customer Name");

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setText("Payment Completed");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setText("Search for Invoice No / W. Receipt No.");

        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)))
                    .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice / Warehouse Rec No", "Customer Name", "Paid Amount", "Due Payment", "Status", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jTable9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Warehouse Rec#", "Customer Name", "Paid Amount", "Due Payment", "Status", "CUS ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable9MouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jTable9);

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel48.setText("Search for Warehouse Rec No");

        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel44.setText("Search from Invoice No");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel45.setText("More to Pay");

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton36.setText("Payment ");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton37.setText("Payment ");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jButton37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                            .addComponent(jButton36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48))
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44)
                        .addComponent(jLabel45))
                    .addComponent(jButton33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton36, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton37, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel14, "card7");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel30.setText("Expenses Tab");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Gas Expenses ($)", "Transportation Expenses ($)", "Purchases ($)", "Rent ($)", "Miscellaneous ($)", "Freight Forwarding Charges ($)", "Total ($)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable7);

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("Day to day Expenses");

        jButton20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton20.setText("New Expenses");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setText("From");

        IDateEditor dateEditor = jDateChooser2.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setText("To");

        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jButton28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton28.setText("Find");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton31MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton31MouseReleased(evt);
            }
        });
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31)))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("The Sum of the selected date range ($)");

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel15, "card8");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel33.setText("Update Shipping");

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setText("Select your Excel Sheet");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField10.setEditable(false);

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton9.setText("Upload & Send Email Shipping Receipt");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton19.setText("View All Uploaded Details");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel24.setText("Please wait for sending emails.......");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel36.setText("0 %");

        jButton27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton27.setText("Print Receipt");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton29MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton29MouseReleased(evt);
            }
        });
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ezgif-3-c098635d67.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10))
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36))
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField10)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addGap(18, 40, Short.MAX_VALUE)
                        .addComponent(jLabel36))
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(251, 251, 251))
        );

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel16, "card9");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel34.setText("Notify Tab");

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Warehouse Receipt #", "Customer Name", "Description", "Total", "Status", "Sent date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable8);

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel52.setText("Search from customer name");

        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setText("Sent Shipping receipt");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel54.setText("Filter by date");

        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.addPropertyChangeListener("foreground", event -> {
                if (Color.BLACK.equals(event.getNewValue())) {
                    txtFld.setForeground(Color.WHITE);
                }
            });
        }
        jDateChooser3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton32MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton32MouseReleased(evt);
            }
        });
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel52)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addContainerGap())
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel54))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel53))
                    .addComponent(jButton32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel17, "card10");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel58.setText("Package Received Tab");

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel58)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Warehouse Receipt #", "Tracking No", "Customer Name", "Address", "Package Weight (lbs)", "No. of Packages", "Description of Package", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable10);

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel60.setText("Customer Account No.");

        jTextField14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField14KeyReleased(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel61.setText("No. of Package");

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel62.setText("Package Weight (lbs.)");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel63.setText("Description of Package");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane11.setViewportView(jTextArea1);

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel66.setText("Customer Email");

        jTextField17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arrow-circle.png"))); // NOI18N
        jButton30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton30MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton30MouseReleased(evt);
            }
        });
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel69.setText("Warehouse Receipt #");

        jTextField19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton24.setText("Send Email");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel68.setText("Tracking No.");

        jTextField20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(jLabel66)
                    .addComponent(jLabel69)
                    .addComponent(jLabel68))
                .addGap(12, 12, 12)
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField20)
                    .addGroup(jPanel57Layout.createSequentialGroup()
                        .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                        .addGap(9, 9, 9)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField17)
                    .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel57Layout.createSequentialGroup()
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel57Layout.createSequentialGroup()
                                .addComponent(jLabel62)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                                .addComponent(jLabel63)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel57Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel61)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel57Layout.createSequentialGroup()
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel61)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel62)
                    .addGroup(jPanel57Layout.createSequentialGroup()
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel69)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel68))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel64.setText("Search from Warehouse No.");

        jTextField18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });

        jButton35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton35.setText("Print Mainfest");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(jPanel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel56Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jButton35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel64)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel18, "card11");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        //Customer Tab
        loadCustomers1("", "customer_id", "ASC");
        customertabReset();

        jPanel6.setVisible(false);
        jPanel10.setVisible(true);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);

    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        //Transaction Tab
        loadInvoices("", "", "invoice_id", "ASC");

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(true);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        //Tracking Tab
        loadInvoices("LEFT JOIN `tracking` ON `invoice`.`invoice_id`=`tracking`.`invoice_invoice_id`", "", "invoice_id", "ASC");

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(true);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
        //Report Tab
        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(true);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        Invoice_Created_Report invoice_Created_Report = new Invoice_Created_Report(this, true);
        invoice_Created_Report.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        //Payments Tab
        loadMorePayments();
        completePayments();
        loadMoreShippingPayments("");

        jTextField5.setText("");
        jTextField7.setText("");
        jTable5.clearSelection();

        jTextField6.setText("");
        jTextField8.setText("");
        jTable6.clearSelection();

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(true);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        //Tracking Tab
        loadExpenses();
        jTextField9.setEditable(false);
        jDateChooser2.setDate(null);
        jDateChooser1.setDate(null);
        jTable7.clearSelection();

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(true);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
        jDateChooser1.getDateEditor().setEnabled(false);
        jDateChooser2.getDateEditor().setEnabled(false);
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
        //Upload Tab
        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(true);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);

        jLabel65.setVisible(false);
        jLabel24.setVisible(false);
        jLabel36.setVisible(false);
        jProgressBar1.setVisible(false);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        //Received Tab
        loadReceivedPacDetails("");

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(true);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        loadCustomers1("", "customer_id", "ASC");
        admintabReset();

        jPanel6.setVisible(true);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(false);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        //Notify Tab
        loadNotify("");
        jDateChooser3.setDate(null);
        jTextField11.setText("");
        jTable8.clearSelection();

        jPanel6.setVisible(false);
        jPanel10.setVisible(false);
        jPanel11.setVisible(false);
        jPanel12.setVisible(false);
        jPanel13.setVisible(false);
        jPanel14.setVisible(false);
        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel17.setVisible(true);
        jPanel18.setVisible(false);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        Employee_registration employee_registration = new Employee_registration(this, true);
        employee_registration.setVisible(true);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO add your handling code here:
        SignIn signIn = new SignIn();
        signIn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Customer_Registration customer_Registration = new Customer_Registration(this, true, cusUpdateMap);
        customer_Registration.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Customer_Registration customer_Registration = new Customer_Registration(this, true, cusUpdateMap);
        customer_Registration.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        QuotationN quotations = new QuotationN(this);
        quotations.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        QuotationN quotations = new QuotationN(this);
        quotations.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        trackingTabReset();
        Tracking_System tracking_System = new Tracking_System(this, true, inDetailMap);
        tracking_System.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Service_Charge_Report service_Charge_Report = new Service_Charge_Report(this, true);
        service_Charge_Report.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        Shipping_Report shipping_Report = new Shipping_Report(this, true);
        shipping_Report.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        Active_Customer_Report active_Customer_Report = new Active_Customer_Report(this, true);
        active_Customer_Report.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        Transaction_Changes_Report transaction_Changes_Report = new Transaction_Changes_Report(this, true);
        transaction_Changes_Report.setVisible(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        New_Transaction new_Transaction = new New_Transaction(this, true);
        new_Transaction.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        New_Transaction new_Transaction = new New_Transaction(this, true);
        new_Transaction.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        Quatation_Created_Report quatation_Created_Report = new Quatation_Created_Report(this, true);
        quatation_Created_Report.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        Daily_Report daily_Report = new Daily_Report(this, true);
        daily_Report.setVisible(true);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        Payment_Received_Report payment_Received_Report = new Payment_Received_Report(this, true);
        payment_Received_Report.setVisible(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        New_Expenses new_Expenses = new New_Expenses(this, true);
        new_Expenses.setVisible(true);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        All_Updated_Shipping_Amount all_Updated_Shipping_Amount = new All_Updated_Shipping_Amount(this, true);
        all_Updated_Shipping_Amount.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        customerSearch();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        customerSearch();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        customerSearch();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        customerSearch();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    HashMap<String, String> cusUpdateMap = new HashMap<>();
    int row = -1;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        //adminTab
        row = jTable1.getSelectedRow();

        if (row != -1) {
            jButton1.setEnabled(false);
            if (evt.getClickCount() == 2) {

                String cusID = String.valueOf(jTable1.getValueAt(row, 0));

                try {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `customer_id`='" + cusID + "'");
                    if (resultSet.next()) {
                        cusUpdateMap.put("cusID", resultSet.getString("customer_id"));
                        cusUpdateMap.put("fname", resultSet.getString("first_name"));
                        cusUpdateMap.put("lname", resultSet.getString("last_name"));
                        cusUpdateMap.put("nic", resultSet.getString("nic"));
                        cusUpdateMap.put("email", resultSet.getString("email"));
                        cusUpdateMap.put("mobile", resultSet.getString("mobile"));
                        cusUpdateMap.put("line1", resultSet.getString("address_line1"));
                        cusUpdateMap.put("line2", resultSet.getString("address_line2"));
                        cusUpdateMap.put("line3", resultSet.getString("address_line3"));
                        cusUpdateMap.put("ratePerLb", String.valueOf(resultSet.getDouble("rate_per_lb")));
                        cusUpdateMap.put("accountTypeID", resultSet.getString("account_type_account_type_id"));
                        cusUpdateMap.put("taxNo", resultSet.getString("tax_identification_no"));
                        cusUpdateMap.put("credit", resultSet.getString("credit_limit"));
                        cusUpdateMap.put("uploadPath", resultSet.getString("upload_document_path"));
                        cusUpdateMap.put("note", resultSet.getString("notes"));

                        Customer_Registration customer_Registration = new Customer_Registration(this, true, cusUpdateMap);
                        customer_Registration.setVisible(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22MousePressed

    private void jButton22MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22MouseReleased

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        admintabReset();
    }//GEN-LAST:event_jButton22ActionPerformed
    String cusDID;
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

        //customerTab
        row = jTable2.getSelectedRow();

        if (row != -1) {
            jButton2.setEnabled(false);
            if (evt.getClickCount() == 2) {
                cusUpdateMap.clear();
                String cusID = String.valueOf(jTable2.getValueAt(row, 0));

                try {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `customers` WHERE `customer_id`='" + cusID + "'");
                    if (resultSet.next()) {
                        cusUpdateMap.put("cusID", resultSet.getString("customer_id"));
                        cusUpdateMap.put("fname", resultSet.getString("first_name"));
                        cusUpdateMap.put("lname", resultSet.getString("last_name"));
                        cusUpdateMap.put("nic", resultSet.getString("nic"));
                        cusUpdateMap.put("email", resultSet.getString("email"));
                        cusUpdateMap.put("mobile", resultSet.getString("mobile"));
                        cusUpdateMap.put("line1", resultSet.getString("address_line1"));
                        cusUpdateMap.put("line2", resultSet.getString("address_line2"));
                        cusUpdateMap.put("line3", resultSet.getString("address_line3"));
                        cusUpdateMap.put("ratePerLb", String.valueOf(resultSet.getDouble("rate_per_lb")));
                        cusUpdateMap.put("accountTypeID", resultSet.getString("account_type_account_type_id"));
                        cusUpdateMap.put("taxNo", resultSet.getString("tax_identification_no"));
                        cusUpdateMap.put("credit", resultSet.getString("credit_limit"));
                        cusUpdateMap.put("uploadPath", resultSet.getString("upload_document_path"));
                        cusUpdateMap.put("note", resultSet.getString("notes"));

                        Customer_Registration customer_Registration = new Customer_Registration(this, true, cusUpdateMap);
                        customer_Registration.setVisible(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (evt.getClickCount() == 1) {
                cusDID = String.valueOf(jTable2.getValueAt(row, 0));
            }
        }

    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23MousePressed

    private void jButton23MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23MouseReleased

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        customertabReset();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        invoiceSearch();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        invoiceSearch();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton25MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25MousePressed

    private void jButton25MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton25MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25MouseReleased

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        transactionTabReset();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        invoiceSearch();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        invoiceSearch();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jButton26MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton26MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26MousePressed

    private void jButton26MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton26MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26MouseReleased

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        trackingTabReset();
    }//GEN-LAST:event_jButton26ActionPerformed
    int Irow = -1;
    HashMap<String, String> inDetailMap = new HashMap<>();
    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // TODO add your handling code here:
        //Go to tracking update

        if (evt.getClickCount() == 2) {
            Irow = jTable4.getSelectedRow();
            if (Irow != -1) {
                inDetailMap.put("invoiceID", String.valueOf(jTable4.getValueAt(Irow, 0)));
                inDetailMap.put("cusID", String.valueOf(jTable4.getValueAt(Irow, 1)));
                inDetailMap.put("cusName", String.valueOf(jTable4.getValueAt(Irow, 2)));
                inDetailMap.put("cusEmail", String.valueOf(jTable4.getValueAt(Irow, 3)));

                Tracking_System tracking_System = new Tracking_System(this, true, inDetailMap);
                tracking_System.setVisible(true);
            }
        }
    }//GEN-LAST:event_jTable4MouseClicked
    String filePath;
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();

        // Customize the file chooser here (e.g., set default directory, file filter, etc.)
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        // Show the file chooser dialog
        int returnValue = fileChooser.showOpenDialog(null);

        // Process the selected file (if any)
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
            jTextField10.setText(filePath);
        }
    }//GEN-LAST:event_jButton6ActionPerformed
//getValuesExcellSheet
    HashMap<String, ExcelSheetData> excelsheetDataMap = new HashMap<>();

    private void excellSheetGetValue() {

        try {
            String selectedSheetPath = jTextField10.getText();

            //load the Excel file
            FileInputStream fileInputStream = new FileInputStream(new File(selectedSheetPath));
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            //Get the sheet(assuming the first sheet)
            Sheet sheet = workbook.getSheetAt(0);

            excelsheetDataMap.clear(); // Clear the map first

            // Initialize the HashMap to store ExcelSheetData
            boolean isFirstRow = true;
            //Loop through each row in the sheet
            for (Row rowSheet : sheet) {
                //Loop through each cell in the current row
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                ExcelSheetData esd = new ExcelSheetData();
                int columnIndex = 0;
                for (Cell cell : rowSheet) {
                    //Check cell type and get the cell value
                    String cellValue = "";
                    if (cell.getCellType() == CellType.STRING) {
                        cellValue = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    switch (columnIndex) {
                        case 0:
                            double excelDateValue = Double.parseDouble(cellValue); // Replace this with your Excel cell value
                            Date javaDate = DateUtil.getJavaDate(excelDateValue);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String dateStr = dateFormat.format(javaDate);
                            esd.setDates(String.valueOf(dateStr));
                            break;
                        case 1:
                            esd.setCustomerID(cellValue);
                            break;
                        case 2:
                            esd.setWayBill(cellValue);
                            break;
                        case 3:
                            esd.setPacNumber(cellValue);
                            break;
                        case 4:
                            esd.setCustomerName(cellValue);
                            break;
                        case 5:
                            esd.setDescription(cellValue);
                            break;
                        case 6:
                            esd.setPieces(cellValue);
                            break;
                        case 7:
                            esd.setLbsWeight(cellValue);
                            break;
                        case 8:
                            esd.setWeight(cellValue);
                            break;
//                        case 9:
//                            esd.setFreight(cellValue);
//                            break;
                        case 9:
                            esd.setDuty(cellValue);
                            break;
                        case 10:
                            esd.setVat(cellValue);
                            break;
                        case 11:
                            esd.setHandling(cellValue);
                            break;
//                        case 12:
//                            esd.setTotal(cellValue);
//                            break;
                        // Add cases for other columns here
                    }
                    columnIndex++;
                }
                // Use a unique key for each row (e.g., concatenation of date and customerID)
                String uniqueKey = rowSheet.getRowNum() + "_" + esd.getDates();
                excelsheetDataMap.put(uniqueKey, esd);

            }

            // Close the input stream and release resources
            fileInputStream.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String uploadSheetId = null;
    private String pacnumber;
    private String customername;

    public void setUploadSheetId(String uploadSheetId) {
        this.uploadSheetId = uploadSheetId;
    }

    public String getUploadSheetId() {
        return uploadSheetId;
    }

    public void setPacnumber(String pacnumber) {
        this.pacnumber = pacnumber;
    }

    public String getPacnumber() {
        return pacnumber;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomername() {
        return customername;
    }

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

//getValuesExcellSheet
    byte[] reportBytes;
    boolean sendEmail = false;
    boolean printInvoice = false;

    String originalPath;
    String destinationFolder;

    //String uploadSheetid;
    private void createReceipt() {
        if (jTextField10.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select excel sheet.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                //uploadDocument
                Date date1 = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
                String stringDate1 = format1.format(date1);

//              String projectFolderPath = getClass().getClassLoader().getResource("/uploadExcellSheets").getPath();
//                File sourceFile = new File(filePath);
//                FileInputStream inputStream = new FileInputStream(sourceFile);
//                byte[] excelData = new byte[(int) sourceFile.length()];
//                inputStream.read(excelData);
//
//                // Construct the destination path within the project folder.
//                String editPath = "excellsheet-" + stringDate1 + "-" + sourceFile.getName();
//
//                originalPath = editPath;
                File pdfFile = new File(filePath);
                // Get or generate a unique identifier for the computer

                // Replace "base/destination/folder" with the base destination folder
                String baseDestinationFolder = System.getProperty("user.home") + File.separator + "upload excel sheet";

                destinationFolder = baseDestinationFolder + File.separator;

                try {
                    storePDF(pdfFile, destinationFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                originalPath = String.valueOf(Original).replace("\\", "\\\\"); // Use a forward slash for path separation in resources

                ResultSet resultSet = MySQL.execute("SELECT * FROM `excel_sheet_upload` WHERE `sheet_name`='" + originalPath + "'");
                if (resultSet.next()) {
                    setUploadSheetId(resultSet.getString("excel_sheet_upload_id"));
                    JOptionPane.showMessageDialog(this, "This excel sheet already added.", "Warning", JOptionPane.WARNING_MESSAGE);
                    sendEmail = false;
                    printInvoice = false;
                    jButton9.setEnabled(false);
                    jButton27.setEnabled(false);
                } else {
                    jButton9.setEnabled(true);
                    jButton27.setEnabled(true);

                    MySQL.execute("INSERT INTO `excel_sheet_upload` (`sheet_name`,`uploaded_date`,`employee_employee_id`) "
                            + "VALUES ('" + originalPath + "','" + stringDate1 + "','" + empID + "')");

                    ResultSet resultSet1 = MySQL.execute("SELECT * FROM `excel_sheet_upload` WHERE `sheet_name`='" + originalPath + "'");

                    if (resultSet1.next()) {
                        setUploadSheetId(resultSet1.getString("excel_sheet_upload_id"));

                        //UploadDocumet
                        excellSheetGetValue();

                        JOptionPane.showMessageDialog(this, "Please wait..", "Waiting", JOptionPane.INFORMATION_MESSAGE);

                        //getData
                        //String path = "src//reports//shippingResceNewipt.jasper";
                        for (ExcelSheetData esd : excelsheetDataMap.values()) {
                            InputStream reportStream = MainInterface.class.getResourceAsStream("/reports/shippingResceNewipt.jasper");
                            URL imageUrl = MainInterface.class.getResource("/img/IMG_report.jpg");

                            ResultSet resultSet3 = MySQL.execute("SELECT * FROM `customers` WHERE `customer_id`='" + esd.getCustomerID() + "'");
                            if (resultSet3.next()) {
                                setPacnumber(esd.getPacNumber());
                                setCustomername(esd.getCustomerName());
                                double rateperlb = resultSet3.getDouble("rate_per_lb");

                                double freight = (((Double.parseDouble(esd.getLbsWeight()) * rateperlb) * 220) + (Double.parseDouble(esd.getPieces()) * 990));
                                double roundedFreight = Math.round(freight);
                                double total = roundedFreight + Double.parseDouble(esd.getDuty()) + Double.parseDouble(esd.getVat()) + Double.parseDouble(esd.getHandling());

                                //open report
                                HashMap<String, Object> parameters = new HashMap<>();
                                parameters.put("Parameter1", esd.getCustomerName());
                                parameters.put("Parameter2", esd.getDates());
                                parameters.put("Parameter3", esd.getPacNumber());
                                parameters.put("Parameter4", esd.getDescription());
                                parameters.put("Parameter5", String.valueOf(total));

                                parameters.put("Parameter6", String.valueOf(total));

                                parameters.put("Parameter7", esd.getPieces());
                                parameters.put("Parameter8", esd.getPieces());
                                parameters.put("Parameter9", esd.getPieces());
                                parameters.put("Parameter10", esd.getPieces());

                                parameters.put("Parameter11", String.valueOf(roundedFreight));
                                parameters.put("Parameter12", esd.getDuty());
                                parameters.put("Parameter13", esd.getVat());
                                parameters.put("Parameter14", esd.getHandling());

                                parameters.put("Parameter15", String.valueOf(roundedFreight));
                                parameters.put("Parameter16", esd.getDuty());
                                parameters.put("Parameter17", esd.getVat());
                                parameters.put("Parameter18", esd.getHandling());
                                parameters.put("Parameter19", esd.getLbsWeight());
                                parameters.put("ImageFilePath", imageUrl.toString());

                                JREmptyDataSource dataSource = new JREmptyDataSource();

                                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
                                reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
                                //JasperViewer.viewReport(jasperPrint, false);

                                //Print Only
                                if (printInvoice) {

                                    //JasperPrintManager.printReport(jasperPrint, true);
                                    if (reportStream != null) {
                                        // Jasper Report file found, proceed with generating the report
                                        // Print the report
                                        JasperPrintManager.printReport(jasperPrint, true);
                                    } else {
                                        // Jasper Report file not found, handle the situation gracefully
                                        JOptionPane.showMessageDialog(this, "Jasper Report file not found. Unable to generate the report.", "Warning", JOptionPane.WARNING_MESSAGE);

                                    }

                                    MySQL.execute("INSERT INTO `excelsheetdata` (`customers_customer_id`,`includeDates`,`waybill`,`number`,`description`,`pieces`,"
                                            + "`weight(lbs)`,`weight`,`freight`,`duty`,`vat`,`handling`,`total`,`paidAmount`,`excel_sheet_upload_excel_sheet_upload_id`,`email_status_email_status_id1`,`email_status_email_status_id`)"
                                            + "VALUES ('" + esd.getCustomerID() + "','" + esd.getDates() + "','" + esd.getWayBill() + "','" + esd.getPacNumber() + "','" + esd.getDescription() + "',"
                                            + "'" + esd.getPieces() + "','" + esd.getLbsWeight() + "','" + esd.getWeight() + "','" + roundedFreight + "','" + esd.getDuty() + "',"
                                            + "'" + esd.getVat() + "','" + esd.getHandling() + "','" + total + "','0','" + getUploadSheetId() + "','2','4')");

                                    resetUploadTab();
                                }

                                //Email sent Only
                                if (sendEmail) {
                                    sendMassEmails(esd.getCustomerID());
                                    if (sent == true) {

                                        MySQL.execute("INSERT INTO `excelsheetdata` (`customers_customer_id`,`includeDates`,`waybill`,`number`,`description`,`pieces`,"
                                                + "`weight(lbs)`,`weight`,`freight`,`duty`,`vat`,`handling`,`total`,`paidAmount`,`excel_sheet_upload_excel_sheet_upload_id`,`email_status_email_status_id`,`email_status_email_status_id1`)"
                                                + "VALUES ('" + esd.getCustomerID() + "','" + esd.getDates() + "','" + esd.getWayBill() + "','" + esd.getPacNumber() + "','" + esd.getDescription() + "',"
                                                + "'" + esd.getPieces() + "','" + esd.getLbsWeight() + "','" + esd.getWeight() + "','" + roundedFreight + "','" + esd.getDuty() + "',"
                                                + "'" + esd.getVat() + "','" + esd.getHandling() + "','" + total + "','0','" + getUploadSheetId() + "','1','4')");

                                    } else {
                                        if (reportStream != null) {
                                            // Jasper Report file found, proceed with generating the report
                                            // Print the report
                                            JasperPrintManager.printReport(jasperPrint, true);
                                        } else {
                                            // Jasper Report file not found, handle the situation gracefully
                                            JOptionPane.showMessageDialog(this, "Jasper Report file not found. Unable to generate the report.", "Warning", JOptionPane.WARNING_MESSAGE);

                                        }

                                        MySQL.execute("INSERT INTO `excelsheetdata` (`customers_customer_id`,`includeDates`,`waybill`,`number`,`description`,`pieces`,"
                                                + "`weight(lbs)`,`weight`,`freight`,`duty`,`vat`,`handling`,`total`,`paidAmount`,`excel_sheet_upload_excel_sheet_upload_id`,`email_status_email_status_id1`,`email_status_email_status_id`)"
                                                + "VALUES ('" + esd.getCustomerID() + "','" + esd.getDates() + "','" + esd.getWayBill() + "','" + esd.getPacNumber() + "','" + esd.getDescription() + "',"
                                                + "'" + esd.getPieces() + "','" + esd.getLbsWeight() + "','" + esd.getWeight() + "','" + roundedFreight + "','" + esd.getDuty() + "',"
                                                + "'" + esd.getVat() + "','" + esd.getHandling() + "','" + total + "','0','" + getUploadSheetId() + "','2','4')");
                                        resetUploadTab();
                                    }

                                }
                            } else {
                                JOptionPane.showMessageDialog(this, esd.getCustomerID() + "This Customer not registered. You can try after the register this customer.", "Waiting", JOptionPane.INFORMATION_MESSAGE);
                                //excelsheetDataMap.remove(esd, esd.getCustomerID());
                                sendEmail = false;
                                printInvoice = false;

                                MySQL.execute("DELETE FROM `excel_sheet_upload` WHERE `excel_sheet_upload_id`='" + getUploadSheetId() + "'");
                            }
                        }

                        //getData
                        //resetUploadTab();
                    }
                }
                //System.out.println(originalPath);

            } catch (Exception e) {
                logger.log(Level.WARNING, "Sending Email", e);
                //e.printStackTrace();
            }
        }
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        sendEmail = true;
        printInvoice = false;

        jLabel65.setVisible(true);
        jLabel24.setVisible(true);
        jLabel36.setVisible(true);
        jProgressBar1.setVisible(true);

        createReceipt();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        //Print Invoice
        sendEmail = false;
        printInvoice = true;
        jLabel65.setVisible(true);
        jLabel24.setVisible(true);
        jLabel36.setVisible(true);
        jProgressBar1.setVisible(true);

        createReceipt();


    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton29MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton29MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton29MousePressed

    private void jButton29MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton29MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton29MouseReleased

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        resetUploadTab();
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
        // searchPaymentsTab();
        loadMoreShippingPayments("");
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        // searchPaymentsTab();
        completePayments();
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        // TODO add your handling code here:
        //searchPaymentsTab();
        completePayments();
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        //loadExpenses();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        //loadExpenses();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        loadExpenses();
    }//GEN-LAST:event_jButton28ActionPerformed

    int duePaymentRow = -1;
    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:

        duePaymentRow = jTable5.getSelectedRow();

        if (duePaymentRow != -1) {
            if (evt.getClickCount() == 2) {
                shippDuePaymentRow = -1;
                jTable9.clearSelection();
                HashMap<String, String> dueMap = new HashMap<>();

                String invID = String.valueOf(jTable5.getValueAt(duePaymentRow, 0));
                String cusName = String.valueOf(jTable5.getValueAt(duePaymentRow, 1));
                String paidAmount = String.valueOf(jTable5.getValueAt(duePaymentRow, 2));
                String dueAmount = String.valueOf(jTable5.getValueAt(duePaymentRow, 3));

                dueMap.put("invId", invID);
                dueMap.put("cusName", cusName);
                dueMap.put("paidAmount", paidAmount);
                dueMap.put("duePayment", dueAmount);

                Payment payment = new Payment(this, true, dueMap);
                payment.setVisible(true);

            }
        }
    }//GEN-LAST:event_jTable5MouseClicked

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        if (jTextField11.getText().isEmpty()) {
            loadNotify("");
        } else {
            loadNotify(jTextField11.getText());
        }
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        // TODO add your handling code here:
        loadNotify(jTextField11.getText());
    }//GEN-LAST:event_jTextField11KeyReleased
    private String pacReceivedCusName;

    public void setPacReceivedCusName(String pacReceivedCusName) {
        this.pacReceivedCusName = pacReceivedCusName;
    }

    public String getPacReceivedCusName() {
        return pacReceivedCusName;
    }


    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        //Send email pacage received
        String cusID = jTextField14.getText();
        String cusEmail = jTextField17.getText();
        String noPac = jTextField15.getText();
        String wr = jTextField19.getText();
        String pacWeight = jFormattedTextField1.getText();
        String desOfPac = jTextArea1.getText();

        //Update 23/04/2024
        String trcNo = jTextField20.getText();

        if (wr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter warehouse receipt No.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (cusID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter cusstomer account number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (cusEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter cusstomer email.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (trcNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter tracking number.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!cusEmail.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
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

                ResultSet resultSet = MySQL.execute("SELECT * FROM `package_received_no_tracking` WHERE `warehouse_receipt`='" + wr + "'");
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "This email already sent.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    Date date1 = new Date();
                    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
                    String stringDate1 = format1.format(date1);

                    MySQL.execute("INSERT INTO `package_received_no_tracking` (`customers_customer_id`,`no_of_packages`,"
                            + "`package_weight`,`description`,`email_send`,`warehouse_receipt`,`tracking_no`) "
                            + "VALUES ('" + cusID + "','" + noPac + "','" + pacWeight + "','" + desOfPac + "','" + stringDate1 + "','" + wr + "','" + trcNo + "')");

                    packageReceivedEmailSend();
                    loadReceivedPacDetails("");
                }

            } catch (Exception e) {
                logger.log(Level.WARNING, "package Received", e);
                //e.printStackTrace();
            }

        }


    }//GEN-LAST:event_jButton24ActionPerformed

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

            String emailText = "Hi " + getPacReceivedCusName() + "\n\n"
                    + "Great news! Your package has safely arrived and is now ready for pickup at our office. Here are the details:\n\n\n"
                    + "Package details :\n\n"
                    + "Consignee Name : " + getPacReceivedCusName() + "\n"
                    + "Number of Packages : " + jTextField15.getText() + "\n"
                    //                    + "Warehouse receipt #: " + jTextField19.getText() + "\n"
                    + "Weight(lbs.) : " + jFormattedTextField1.getText() + "\n"
                    + "Description : " + jTextArea1.getText() + "\n"
                    + "Tracking No #: " + jTextField20.getText() + "\n\n\n"
                    + "Feel free to swing by whenever it's convenient for you. We can't wait to hand over your goodies!\n\n"
                    + "Best regards,";

            // Create the text part of the email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailText);
            multipart.addBodyPart(textPart);

            message.setContent(multipart);

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(jTextField17.getText()));
            Transport.send(message);

            resetPackReceived();

            JOptionPane.showMessageDialog(this, "Email send success", "Successfull", JOptionPane.INFORMATION_MESSAGE);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void resetPackReceived() {
        jTextField14.setText("");
        jTextField17.setText("");
        jTextField15.setText("");
        jFormattedTextField1.setText("");
        jTextArea1.setText("");
        loadReceivedPacDetails("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
    }

    public void loadReceivedPacDetails(String Wr) {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `package_received_no_tracking` INNER JOIN `customers`"
                    + "ON `package_received_no_tracking`.`customers_customer_id`=`customers`.`customer_id`"
                    + "WHERE `package_received_no_tracking`.`warehouse_receipt` LIKE '" + Wr + "%' ORDER BY `package_received_no_tracking`.`email_send` DESC");

            DefaultTableModel model = (DefaultTableModel) jTable10.getModel();
            model.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("package_received_no_tracking.warehouse_receipt"));
                vector.add(resultSet.getString("package_received_no_tracking.tracking_no"));
                vector.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                vector.add(resultSet.getString("customers.address_line1") + " " + resultSet.getString("customers.address_line2") + " " + resultSet.getString("customers.address_line3"));
                vector.add(resultSet.getString("package_received_no_tracking.package_weight"));
                vector.add(resultSet.getString("package_received_no_tracking.no_of_packages"));
                vector.add(resultSet.getString("package_received_no_tracking.description"));
                vector.add("Sent");
                vector.add(resultSet.getString("package_received_no_tracking.email_send"));

                model.addRow(vector);
            }

            for (int i = 0; i < 1; i++) {
                jTable10.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            for (int i = 1; i < 2; i++) {
                jTable10.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            }
            for (int i = 2; i < model.getColumnCount(); i++) {
                jTable10.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jButton30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30MousePressed

    private void jButton30MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30MouseReleased

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        resetPackReceived();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased
        // TODO add your handling code here:
        if (jTextField18.getText().isEmpty()) {
            loadReceivedPacDetails("");
        } else {
            loadReceivedPacDetails(jTextField18.getText());
        }
    }//GEN-LAST:event_jTextField18KeyReleased

    private void jButton31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31MousePressed

    private void jButton31MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31MouseReleased

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // TODO add your handling code here:
        loadExpenses();
        jDateChooser2.setDate(null);
        jDateChooser1.setDate(null);
        jTable7.clearSelection();

    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32MousePressed

    private void jButton32MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32MouseReleased

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
        loadNotify("");
        jDateChooser3.setDate(null);
        jTextField11.setText("");
        jTable8.clearSelection();
    }//GEN-LAST:event_jButton32ActionPerformed
    private void resetPayment2() {
        jTextField6.setText("");
        jTextField8.setText("");
        jTable6.clearSelection();
        completePayments();
        jTextField6.grabFocus();
    }
    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
        resetPayment2();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyReleased
        // TODO add your handling code here:
        try {
            if (jTextField14.getText().length() == 7) {
                ResultSet rs = MySQL.execute("SELECT * FROM `customers` WHERE `customer_id` = '" + jTextField14.getText() + "'");
                if (rs.next()) {

                    setPacReceivedCusName(rs.getString("first_name") + " " + rs.getString("last_name"));
                    jTextField17.setText(rs.getString("email"));

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid customer. Please check customer ID.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                jTextField17.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField14KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try {

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select the customer to remove", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                MySQL.execute("DELETE FROM `customers` WHERE `customer_id`='" + cusDID + "'");

                loadCustomers1("", "customer_id", "ASC");
                customertabReset();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = jTable10.getSelectedRows();
        if (selectedRows.length > 0) {

//        for (int i = 0; i < selectedRows.length; i++) {
//            pacReceivedDetailsMap.put("wir", jTable10.getValueAt(i, 0));
//        }
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try ( FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile() + ".xlsx")) {
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Data");

                    int rowIndex = 0;
                    Row headerRow = sheet.createRow(rowIndex++);
// Add column titles (headers) to the Excel sheet
                    for (int col = 0; col < jTable10.getColumnCount(); col++) {
                        Cell cell = headerRow.createCell(col);
                        cell.setCellValue(jTable10.getColumnName(col));
                    }
                    for (int i = 0; i < selectedRows.length; i++) {
                        Row excelRow = sheet.createRow(rowIndex++);
                        for (int col = 0; col < jTable10.getColumnCount(); col++) {
                            Cell cell = excelRow.createCell(col);
                            cell.setCellValue(jTable10.getValueAt(selectedRows[i], col).toString());
                        }
                    }

                    workbook.write(fos);
                    fos.close();
                    workbook.close();
                    resetPackReceived();
//                JOptionPane.showMessageDialog(frame, "Data exported to Excel successfully!");
                    JOptionPane.showMessageDialog(this, "Data exported to Excel successfully!", "Successfull", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
//                JOptionPane.showMessageDialog(frame, "Error exporting data to Excel.");
                    JOptionPane.showMessageDialog(this, "Error exporting data to Excel.", "Successfull", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select rows.", "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_jButton35ActionPerformed

    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable10MouseClicked

    int shippDuePaymentRow = -1;
    String wirID;
    private void jTable9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable9MouseClicked
        // TODO add your handling code here:

        shippDuePaymentRow = jTable9.getSelectedRow();

        if (shippDuePaymentRow != -1) {
            if (evt.getClickCount() == 2) {
                duePaymentRow = -1;
                jTable5.clearSelection();
                HashMap<String, String> dueMap = new HashMap<>();

                String wir = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 0));
                String cusName = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 1));
                String paidAmount = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 2));
                String dueAmount = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 3));

                dueMap.put("wir", wir);
                dueMap.put("cusName", cusName);
                dueMap.put("paidAmount", paidAmount);
                dueMap.put("duePayment", dueAmount);

                Payment payment = new Payment(this, true, dueMap);
                payment.setVisible(true);

            }
        }

        if (evt.getButton() == MouseEvent.BUTTON3) {
            // Right mouse button was clicked
            shippDuePaymentRow = jTable9.rowAtPoint(evt.getPoint());
            if (shippDuePaymentRow >= 0) {
                jTable9.setRowSelectionInterval(shippDuePaymentRow, shippDuePaymentRow);
                jPopupMenu1.show(jTable9, evt.getX(), evt.getY());
            }
        }

//        if (shippDuePaymentRow != -1) {
//            if (evt.getButton() == MouseEvent.BUTTON3) {
//                // Right mouse button was clicked
//                jPopupMenu1.show(jTable9, evt.getX(), evt.getY());
//            }
//        }
    }//GEN-LAST:event_jTable9MouseClicked

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        //searchPaymentsTab();
        loadMorePayments();
    }//GEN-LAST:event_jTextField5KeyReleased
    private void resetPayment() {

        jTable5.clearSelection();
        jTable9.clearSelection();
        jTextField5.setText("");
        jTextField7.setText("");
        loadMorePayments();
        loadMoreShippingPayments("");
        jTextField5.grabFocus();
        paymentDetailMap.clear();
        AllDueAmount = 0;
        AllpaidAmount = 0;
    }
    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
        resetPayment();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        wirID = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 0));
        String name = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 1));

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `excelsheetdata` WHERE `number`='" + wirID + "'");
            if (resultSet.next()) {
                InputStream reportStream = MainInterface.class.getResourceAsStream("/reports/shippingResceNewipt.jasper");
                URL imageUrl = MainInterface.class.getResource("/img/IMG_report.jpg");

                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("Parameter1", name);
                parameters.put("Parameter2", resultSet.getString("includeDates"));
                parameters.put("Parameter3", wirID);
                parameters.put("Parameter4", resultSet.getString("description"));
                parameters.put("Parameter5", resultSet.getString("total"));
                parameters.put("Parameter6", resultSet.getString("total"));

                parameters.put("Parameter7", resultSet.getString("pieces"));
                parameters.put("Parameter8", resultSet.getString("pieces"));
                parameters.put("Parameter9", resultSet.getString("pieces"));
                parameters.put("Parameter10", resultSet.getString("pieces"));

                parameters.put("Parameter11", resultSet.getString("freight"));
                parameters.put("Parameter12", resultSet.getString("duty"));
                parameters.put("Parameter13", resultSet.getString("vat"));
                parameters.put("Parameter14", resultSet.getString("handling"));

                parameters.put("Parameter15", resultSet.getString("freight"));
                parameters.put("Parameter16", resultSet.getString("duty"));
                parameters.put("Parameter17", resultSet.getString("vat"));
                parameters.put("Parameter18", resultSet.getString("handling"));

                parameters.put("Parameter19", resultSet.getString("weight(lbs)"));
                parameters.put("ImageFilePath", imageUrl.toString());

                JREmptyDataSource dataSource = new JREmptyDataSource();

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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed
    int completePaymentRow = -1;
    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked
        // TODO add your handling code here:
//        completePaymentRow = jTable6.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON3) {
            // Right mouse button was clicked
            completePaymentRow = jTable6.rowAtPoint(evt.getPoint());
            if (completePaymentRow >= 0) {
                jTable6.setRowSelectionInterval(completePaymentRow, completePaymentRow);
                jPopupMenu2.show(jTable6, evt.getX(), evt.getY());
            }
        }

    }//GEN-LAST:event_jTable6MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        //Remove paid amount
        try {
            if (jTable6.getValueAt(completePaymentRow, 0).toString().startsWith("INV")) {
                String invID = String.valueOf(jTable6.getValueAt(completePaymentRow, 0));
                MySQL.execute("UPDATE `invoice` SET "
                        + "`paid_ammount`='0' "
                        + "WHERE `invoice_id`='" + invID + "'");

                JOptionPane.showMessageDialog(this, "Removed.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else if (jTable6.getValueAt(completePaymentRow, 0).toString().startsWith("CSW")) {
                String wirNum = String.valueOf(jTable6.getValueAt(completePaymentRow, 0));
                MySQL.execute("UPDATE `excelsheetdata` SET "
                        + "`paidAmount`='0' "
                        + "WHERE `number`='" + wirNum + "'");

                JOptionPane.showMessageDialog(this, "Removed.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "This is not valid.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            resetPayment();
            resetPayment2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        //DELETE invoice
        try {
            if (jTable6.getValueAt(completePaymentRow, 0).toString().startsWith("INV")) {
                String invID = String.valueOf(jTable6.getValueAt(completePaymentRow, 0));
                MySQL.execute("DELETE FROM `tracking` WHERE `invoice_invoice_id`='" + invID + "'");
                MySQL.execute("DELETE FROM `otherordernumbers` WHERE `invoice_invoice_id`='" + invID + "'");
                MySQL.execute("DELETE FROM `converttoinvoice` WHERE `invoice_invoice_id`='" + invID + "'");
                MySQL.execute("DELETE FROM `package_received` WHERE `invoice_invoice_id`='" + invID + "'");
                MySQL.execute("DELETE FROM `invoice_item` WHERE `invoice_invoice_id`='" + invID + "'");
                MySQL.execute("DELETE FROM `invoice` WHERE `invoice_id`='" + invID + "'");

                JOptionPane.showMessageDialog(this, "Deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else if (jTable6.getValueAt(completePaymentRow, 0).toString().startsWith("CSW")) {
                String wirNum = String.valueOf(jTable6.getValueAt(completePaymentRow, 0));
                MySQL.execute("DELETE FROM `excelsheetdata` WHERE `number`='" + wirNum + "'");

                JOptionPane.showMessageDialog(this, "Deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "This is not valid.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            resetPayment();
            resetPayment2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        // TODO add your handling code here:

        loadMoreShippingPayments(jTextField12.getText());

    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20ActionPerformed
    HashMap<String, PaymentDetail> paymentDetailMap = new HashMap<>();
    double AllpaidAmount = 0;
    double AllDueAmount = 0;
    String invNo = null;
    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:

        int[] selectedRows = jTable9.getSelectedRows();
        if (selectedRows.length > 0) {
//            StringBuilder invNoBuilder = new StringBuilder();
//            HashMap<String, String> invMap = new HashMap<>();

            for (int i = 0; i < selectedRows.length; i++) {
                // Assuming the column containing the amount is the 3rd column (index 2)
                AllpaidAmount += Double.parseDouble(jTable9.getValueAt(selectedRows[i], 2).toString());
                AllDueAmount += Double.parseDouble(jTable9.getValueAt(selectedRows[i], 3).toString());

                String invNumber = jTable9.getValueAt(selectedRows[i], 0).toString(); // Get column 1 value
                String pA = jTable9.getValueAt(selectedRows[i], 2).toString(); // Get column 1 value
                String dA = jTable9.getValueAt(selectedRows[i], 3).toString(); // Get column 1 value
                PaymentDetail paymentd = new PaymentDetail();
                paymentd.setWirNo(invNumber);
                paymentd.setPaidAmount(pA);
                paymentd.setDueAmount(dA);

                if (paymentDetailMap.get(invNumber) == null) {
                    paymentDetailMap.put(invNumber, paymentd);
                } else {
                    PaymentDetail found = paymentDetailMap.get(invNumber);
                    JOptionPane.showMessageDialog(this, "Click reset button and select again.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }

            duePaymentRow = -1;
            jTable5.clearSelection();
            HashMap<String, String> dueMap = new HashMap<>();

            String wir = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 0));
            String cusName = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 1));
            String paidAmount = String.valueOf(AllpaidAmount);
            String dueAmount = String.valueOf(AllDueAmount);

            dueMap.put("wir", wir);
            dueMap.put("cusName", cusName);
            dueMap.put("paidAmount", paidAmount);
            dueMap.put("duePayment", dueAmount);

            SelectAndPayment payment = new SelectAndPayment(this, true, dueMap);
            payment.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Not selected any row.", "Warning", JOptionPane.WARNING_MESSAGE);

        }

        resetPayment();


    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = jTable5.getSelectedRows();
        if (selectedRows.length > 0) {
//            StringBuilder invNoBuilder = new StringBuilder();
//            HashMap<String, String> invMap = new HashMap<>();

            for (int i = 0; i < selectedRows.length; i++) {
                // Assuming the column containing the amount is the 3rd column (index 2)
                AllpaidAmount += Double.parseDouble(jTable5.getValueAt(selectedRows[i], 2).toString());
                AllDueAmount += Double.parseDouble(jTable5.getValueAt(selectedRows[i], 3).toString());

                String invNumber = jTable5.getValueAt(selectedRows[i], 0).toString(); // Get column 1 value
                String pA = jTable5.getValueAt(selectedRows[i], 2).toString(); // Get column 1 value
                String dA = jTable5.getValueAt(selectedRows[i], 3).toString(); // Get column 1 value
                PaymentDetail paymentd = new PaymentDetail();
                paymentd.setWirNo(invNumber);
                paymentd.setPaidAmount(pA);
                paymentd.setDueAmount(dA);

                if (paymentDetailMap.get(invNumber) == null) {
                    paymentDetailMap.put(invNumber, paymentd);
                } else {
                    PaymentDetail found = paymentDetailMap.get(invNumber);
                    JOptionPane.showMessageDialog(this, "Click reset button and select again.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }

            shippDuePaymentRow = -1;
            jTable9.clearSelection();
            HashMap<String, String> dueMap = new HashMap<>();

            String wir = String.valueOf(jTable5.getValueAt(duePaymentRow, 0));
            String cusName = String.valueOf(jTable5.getValueAt(duePaymentRow, 1));
            String paidAmount = String.valueOf(AllpaidAmount);
            String dueAmount = String.valueOf(AllDueAmount);

            dueMap.put("wir", wir);
            dueMap.put("cusName", cusName);
            dueMap.put("paidAmount", paidAmount);
            dueMap.put("duePayment", dueAmount);

            SelectAndPayment payment = new SelectAndPayment(this, true, dueMap);
            payment.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Not selected any row.", "Warning", JOptionPane.WARNING_MESSAGE);

        }

        resetPayment();

    }//GEN-LAST:event_jButton37ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:

        wirID = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 0));
        String name = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 1));
        String CusID = String.valueOf(jTable9.getValueAt(shippDuePaymentRow, 5));

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `excelsheetdata` WHERE `customers_customer_id`='" + CusID + "'");
            double totalAmount = 0;
            double invoics = 0;
            while (resultSet.next()) {
                totalAmount += (resultSet.getDouble("total") - resultSet.getDouble("paidAmount"));
                invoics++;
            }

            CustomerAccount customerAccount = new CustomerAccount(this, true, name, invoics, totalAmount);
            customerAccount.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jMenuItem4ActionPerformed

    int currentValue = 0;
    private boolean sent = false;
    private boolean emailSent = false;
    private final AtomicInteger emailCount = new AtomicInteger(0);
    private final int MAX_RETRIES = 3; // Maximum number of retries
    private final int RETRY_DELAY = 5000; // Delay between retries in milliseconds

    //Send Email in shipping reciept
    private void sendMassEmails(String id) {
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
            message.setSubject("Source 1 Solutions : Shipping Reciept.");
            //message.setText("Your Message");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Create the text part of the email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Hello " + getCustomername() + ", \n\n");
            multipart.addBodyPart(textPart);

            // Attach the report
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(reportBytes, "Shipping_invoice/pdf");
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(getPacnumber() + ".pdf");
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            try {
                ResultSet resultSet = MySQL.execute("SELECT `email` FROM `customers` WHERE `customer_id`='" + id + "'");
                if (resultSet.next()) {
                    if (!resultSet.getString("email").isEmpty()) {
                        String email = resultSet.getString("email");
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                        //Transport.send(message);

                        Thread t = new Thread(() -> {
                            int attempt = 0;
                            while (attempt < MAX_RETRIES) {
                                try {
                                    Transport.send(message);
                                    emailSent = true;
                                    updateProgress();
                                    break; // Exit the loop if the email is sent successfully
                                } catch (SMTPSendFailedException e) {
                                    attempt++;
                                    if (attempt < MAX_RETRIES) {
                                        try {
                                            Thread.sleep(RETRY_DELAY);
                                        } catch (InterruptedException ie) {
                                            Thread.currentThread().interrupt();
                                        }
                                    } else {
                                        e.printStackTrace();
                                    }
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        });
                        //jProgressBar1.setValue(100); // Set progress to 100% after all emails are sent
                        t.start();
                        sent = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "There is no email. Now print the receipt.", "Warning", JOptionPane.WARNING_MESSAGE);
                        printInvoice = true;
                        sent = false;
                        //createReceipt();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //System.out.println("Mass emails sent successfully.");
            //JOptionPane.showMessageDialog(this, "Email send success", "Successfull", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void updateProgress() {
        int totalEmails = excelsheetDataMap.size(); // Total number of emails to be sent
        int progressIncrement = 100 / totalEmails; // Increment based on total emails

        if (emailSent) {
            emailCount.incrementAndGet();
            currentValue += progressIncrement;
            // Ensure currentValue does not exceed 100%
            if (currentValue > 100) {
                currentValue = 100;
            }
            // Update the progress bar and label
            jProgressBar1.setValue(currentValue);
            jLabel36.setText(currentValue + " %");

            // Check if all emails are sent
            if (emailCount.get() == totalEmails) {
                JOptionPane.showMessageDialog(this, "Email send success", "Successful", JOptionPane.INFORMATION_MESSAGE);
                resetUploadTab();
            }
        }

    }

    private void resetUploadTab() {
        jTextField10.setText("");
        excelsheetDataMap.clear();
        currentValue = 0;
        jButton6.grabFocus();

        sendEmail = false;
        printInvoice = false;
        jButton9.setEnabled(true);
        jButton27.setEnabled(true);

        jLabel65.setVisible(false);
        jLabel24.setVisible(false);
        jLabel36.setVisible(false);
        jProgressBar1.setVisible(false);
    }

    //Payments Tab
    public void loadMorePayments() {
        String invoiceId = "";
        if (!jTextField5.getText().isEmpty()) {
            invoiceId = jTextField5.getText();
        }

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` "
                    + "INNER JOIN `employee` ON `invoice`.`employee_employee_id`=`employee`.`employee_id`"
                    + "INNER JOIN `customers` ON `invoice`.`customers_customer_id`=`customers`.`customer_id`"
                    + "WHERE `invoice`.`invoice_id` LIKE '" + invoiceId + "%' AND `invoice`.`paid_ammount`<`invoice`.`grandTotal`");

            DefaultTableModel model1 = (DefaultTableModel) jTable5.getModel();
            model1.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

//            DefaultTableModel model2 = (DefaultTableModel) jTable6.getModel();
//            model2.setRowCount(0);
            while (resultSet.next()) {
                double duePayment = resultSet.getDouble("invoice.grandTotal") - resultSet.getDouble("invoice.paid_ammount");
                if (duePayment != 0) {
                    Vector<String> vector1 = new Vector<>();
                    vector1.add(resultSet.getString("invoice.invoice_id"));
                    vector1.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                    vector1.add(resultSet.getString("invoice.paid_ammount"));
                    vector1.add(String.valueOf(duePayment));
                    vector1.add("Unpaid.");
                    vector1.add(resultSet.getString("employee.first_name") + " " + resultSet.getString("employee.last_name"));
                    model1.addRow(vector1);
                }
            }

            for (int i = 0; i < model1.getColumnCount(); i++) {
                jTable5.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void completePayments() {
        String cusName = "";
        String invoiceId = "";
        String warehouseRec = "";
        if (!jTextField6.getText().isEmpty()) {
            cusName = jTextField6.getText();
        }
        if (!jTextField8.getText().isEmpty()) {
            invoiceId = jTextField8.getText();
            warehouseRec = jTextField8.getText();
        }
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` "
                    + "INNER JOIN `employee` ON `invoice`.`employee_employee_id`=`employee`.`employee_id`"
                    + "INNER JOIN `customers` ON `invoice`.`customers_customer_id`=`customers`.`customer_id`"
                    + "WHERE (`customers`.`first_name` LIKE '" + cusName + "%' OR `customers`.`last_name` LIKE '" + cusName + "%')"
                    + "AND `invoice`.`invoice_id` LIKE '" + invoiceId + "%' AND `invoice`.`paid_ammount`=`invoice`.`grandTotal`");

            ResultSet resultSet2 = MySQL.execute("SELECT * FROM `excelsheetdata` "
                    + "INNER JOIN `customers` ON `excelsheetdata`.`customers_customer_id`=`customers`.`customer_id` "
                    + "INNER JOIN `excel_sheet_upload` ON `excelsheetdata`.`excel_sheet_upload_excel_sheet_upload_id`=`excel_sheet_upload`.`excel_sheet_upload_id` "
                    + "INNER JOIN `employee` ON `excel_sheet_upload`.`employee_employee_id`=`employee`.`employee_id` "
                    + "WHERE `excelsheetdata`.`number` LIKE '" + warehouseRec + "%' AND `excelsheetdata`.`paidAmount`=`excelsheetdata`.`total`");

            DefaultTableModel model1 = (DefaultTableModel) jTable6.getModel();
            model1.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            while (resultSet.next()) {
                double duePayment = resultSet.getDouble("invoice.grandTotal") - resultSet.getDouble("invoice.paid_ammount");
                if (duePayment == 0.0) {
                    Vector<String> vector2 = new Vector<>();
                    vector2.add(resultSet.getString("invoice.invoice_id"));
                    vector2.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                    vector2.add(resultSet.getString("invoice.paid_ammount"));
                    vector2.add(String.valueOf(duePayment));
                    vector2.add("Payment Success");
                    vector2.add(resultSet.getString("employee.first_name") + " " + resultSet.getString("employee.last_name"));

                    model1.addRow(vector2);
                }
            }

            while (resultSet2.next()) {
                double duePayment = resultSet2.getDouble("excelsheetdata.total") - resultSet2.getDouble("excelsheetdata.paidAmount");
                if (duePayment == 0.0) {
                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet2.getString("excelsheetdata.number"));
                    vector.add(resultSet2.getString("customers.first_name") + " " + resultSet2.getString("customers.last_name"));
                    vector.add(resultSet2.getString("excelsheetdata.paidAmount"));
                    vector.add(String.valueOf(duePayment));
                    vector.add("Payment Success");
                    vector.add(resultSet2.getString("employee.first_name") + " " + resultSet2.getString("employee.last_name"));

                    model1.addRow(vector);
                }
            }

            for (int i = 0; i < model1.getColumnCount(); i++) {
                jTable6.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadMoreShippingPayments(String cusName) {
        String warehouseRec = "";
        if (!jTextField7.getText().isEmpty()) {
            warehouseRec = jTextField7.getText();
        }
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `excelsheetdata` "
                    + "INNER JOIN `customers` ON `excelsheetdata`.`customers_customer_id`=`customers`.`customer_id` "
                    + "INNER JOIN `excel_sheet_upload` ON `excelsheetdata`.`excel_sheet_upload_excel_sheet_upload_id`=`excel_sheet_upload`.`excel_sheet_upload_id` "
                    + "WHERE "
                    + "CONCAT(`customers`.`first_name`,`customers`.`last_name`) LIKE '%" + cusName + "%' "
                    + "AND `excelsheetdata`.`number` LIKE '" + warehouseRec + "%' AND `excelsheetdata`.`paidAmount`<`excelsheetdata`.`total`");

            DefaultTableModel model1 = (DefaultTableModel) jTable9.getModel();
            model1.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

//            DefaultTableModel model2 = (DefaultTableModel) jTable6.getModel();
//            model2.setRowCount(0);
            while (resultSet.next()) {
                double duePayment = resultSet.getDouble("excelsheetdata.total") - resultSet.getDouble("excelsheetdata.paidAmount");
                if (duePayment != 0) {
                    Vector<String> vector1 = new Vector<>();
                    vector1.add(resultSet.getString("excelsheetdata.number"));
                    vector1.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                    vector1.add(resultSet.getString("excelsheetdata.paidAmount"));
                    vector1.add(String.valueOf(duePayment));
                    vector1.add("Unpaid.");
                    vector1.add(resultSet.getString("customers.customer_id"));

                    model1.addRow(vector1);
                }
            }

            for (int i = 0; i < model1.getColumnCount(); i++) {
                jTable9.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadExpenses() {

        try {

            String query = "SELECT * FROM `expenses` ";

            //exp
            Date start = null;
            Date end = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (jDateChooser2.getDate() != null) {
                if (query.contains("WHERE")) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                }
                start = jDateChooser2.getDate();
                query += "`expenses_date` > '" + format.format(start) + "' ";

            }
            if (jDateChooser1.getDate() != null) {
                if (query.contains("WHERE")) {
                    query += "AND ";
                } else {
                    query += "WHERE ";
                }
                end = jDateChooser1.getDate();
                query += "`expenses_date` < '" + format.format(end) + "' ";

            }

            query += "ORDER BY `expenses_date` ASC";

            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
            model.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            double sumColumn7 = 0.0;

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("expenses_date"));
                vector.add(resultSet.getString("gas"));
                vector.add(resultSet.getString("transportation"));
                vector.add(resultSet.getString("purchases"));
                vector.add(resultSet.getString("rent"));
                vector.add(resultSet.getString("miscellaneous"));
                vector.add(resultSet.getString("freight_forwarding_charges"));
                vector.add(resultSet.getString("total"));

                model.addRow(vector);

                double column4Value = Double.parseDouble(resultSet.getString("total"));
                sumColumn7 += column4Value;

            }

            jTextField9.setText(String.valueOf(sumColumn7));

            for (int i = 0; i < model.getColumnCount(); i++) {
                jTable7.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadNotify(String cusName) {
        try {

            String query = "SELECT * FROM `excelsheetdata` "
                    + "INNER JOIN `customers` ON `excelsheetdata`.`customers_customer_id`=`customers`.`customer_id` "
                    + "INNER JOIN `excel_sheet_upload` ON `excelsheetdata`.`excel_sheet_upload_excel_sheet_upload_id`=`excel_sheet_upload`.`excel_sheet_upload_id` "
                    + "INNER JOIN `email_status` ON `excelsheetdata`.`email_status_email_status_id`=`email_status`.`email_status_id` "
                    + "WHERE (`customers`.`first_name` LIKE '" + cusName + "%' OR `customers`.`last_name` LIKE '" + cusName + "%') ";

            Date seacrchDate = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (jDateChooser3.getDate() != null) {
                seacrchDate = jDateChooser3.getDate();
                query += "AND `excel_sheet_upload`.`uploaded_date`='" + format.format(seacrchDate) + "' ";

            }

            query += "AND `excelsheetdata`.`email_status_email_status_id`='1' ORDER BY `excel_sheet_upload`.`uploaded_date` DESC";

            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel model = (DefaultTableModel) jTable8.getModel();
            model.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("excelsheetdata.number"));
                vector.add(resultSet.getString("customers.first_name") + " " + resultSet.getString("customers.last_name"));
                vector.add(resultSet.getString("excelsheetdata.description"));
                vector.add(resultSet.getString("excelsheetdata.total"));
                vector.add(resultSet.getString("email_status.type"));
                vector.add(resultSet.getString("excel_sheet_upload.uploaded_date"));

                model.addRow(vector);
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                jTable8.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
