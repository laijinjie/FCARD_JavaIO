/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcardiodemo;

import Net.PC15.Connector.UDP.UDPDetail;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.*;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Data.TCPDetail;
import Net.PC15.FC8800.Command.Door.*;
import Net.PC15.FC8800.Command.Door.Parameter.*;
import Net.PC15.FC8800.Command.System.*;
import Net.PC15.FC8800.Command.System.Parameter.*;
import Net.PC15.FC8800.Command.System.Result.*;
import Net.PC15.FC8800.Command.DateTime.*;
import Net.PC15.FC8800.Command.Card.*;
import Net.PC15.FC8800.Command.Card.Parameter.*;
import Net.PC15.FC8800.Command.Card.Result.*;
import Net.PC15.FC8800.Command.Transaction.*;
import Net.PC15.FC8800.Command.Transaction.Parameter.*;
import Net.PC15.FC8800.Command.Transaction.Result.*;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.Data.TimeGroup.DayTimeGroup;
import Net.PC15.FC8800.Command.Data.TimeGroup.TimeSegment;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import Net.PC15.FC8800.Command.DateTime.Result.ReadTime_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.TimeGroup.AddTimeGroup;
import Net.PC15.FC8800.Command.TimeGroup.ClearTimeGroup;
import Net.PC15.FC8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author ?????????
 */
public class frmMain extends javax.swing.JFrame implements INConnectorEvent {

    private ConnectorAllocator _Allocator;
    private ConcurrentHashMap<String, String> CommandName;
    private ConcurrentHashMap<String, CommandResultCallback> CommandResult;
    private Timer timer = new Timer();
    private boolean mIsClose;

    @Override
    public void ClientOnline(TCPServerClientDetail client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private interface CommandResultCallback {

        public void ResultToLog(StringBuilder strBuf, INCommandResult result);
    }

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        setTitle("FC8900 ?????????????????? V1.0");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mIsClose = false;
        //JFrame.setDefaultLookAndFeelDecorated(true);
        //????????????
        setSize(1040, 800);
        Dimension ScreenSize = getToolkit().getScreenSize();
        Rectangle oldRectangle = getBounds();
        setBounds((ScreenSize.width - oldRectangle.width) / 2, (ScreenSize.height - oldRectangle.height) / 2, oldRectangle.width, oldRectangle.height);

        //???????????????
        bgConnectType.add(RadUDP);
        bgConnectType.add(RadTCPClient);
        bgConnectType.add(RadTCPServer);
        RadTCPClient.setSelected(true);

        //?????????timer
        timer = new Timer();
        timer.schedule(new TimeTask(), 200, 1000);

        ShowConnectPanel();

        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);

        strLog = new StringBuilder(50000);

        frmMain frm = this;
        /**
         * ???????????????????????????
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frm.setVisible(false);
                if (_Allocator != null) {
                    _Allocator.DeleteListener(frm);
                    /**
                     * ????????????????????????????????????????????????
                     */
                    _Allocator.Release();
                    _Allocator = null;
                }
                mIsClose = true;

                timer.cancel();
                timer = null;
            }
        });

        CommandResult = new ConcurrentHashMap<String, CommandResultCallback>();
        CommandName = new ConcurrentHashMap<String, String>();
        IniCommandName();
        IniCardDataBase();
        IniWatchEvent();

    }

    private class TimeTask extends java.util.TimerTask {

        @Override
        public void run() {
            if (mIsClose) {
                return;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar dt = Calendar.getInstance();
            LblDate.setText(format.format(dt.getTime()));
            format.applyLocalizedPattern("HH:mm:ss");
            lblTime.setText(format.format(dt.getTime()));
        }
    }

    private void ShowConnectPanel() {
        pnlTCPServer.setVisible(false);
        PnlUDP.setVisible(false);
        pnlTCPClient.setVisible(false);
        if (RadTCPClient.isSelected()) {
            pnlTCPClient.setVisible(true);
        }
        if (RadUDP.isSelected()) {
            PnlUDP.setVisible(true);
        }
        if (RadTCPServer.isSelected()) {
            pnlTCPServer.setVisible(true);
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

        bgConnectType = new javax.swing.ButtonGroup();
        jpConnectSetting = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        RadTCPClient = new javax.swing.JRadioButton();
        RadUDP = new javax.swing.JRadioButton();
        RadTCPServer = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        pnlTCPClient = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtTCPServerIP = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtTCPServerPort = new javax.swing.JTextField();
        PnlUDP = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtUDPRemoteIP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUDPRemotePort = new javax.swing.JTextField();
        pnlTCPServer = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtLocalPort = new javax.swing.JTextField();
        butBeginServer = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jpSNDetail = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtSN = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        LblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        prCommand = new javax.swing.JProgressBar();
        lblCommandName = new javax.swing.JLabel();
        jpLog = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jTabSetting = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        butReadConnectPassword = new javax.swing.JButton();
        butWriteConnectPassword = new javax.swing.JButton();
        butResetConnectPassword = new javax.swing.JButton();
        butReadTCPSetting = new javax.swing.JButton();
        butWriteTCPSetting = new javax.swing.JButton();
        ButReadDeadline = new javax.swing.JButton();
        ButWriteDeadline = new javax.swing.JButton();
        butReadVersion = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtWriteSN = new javax.swing.JTextField();
        butWriteSN = new javax.swing.JButton();
        butReadSN = new javax.swing.JButton();
        butBeginWatch = new javax.swing.JButton();
        butCloseWatch = new javax.swing.JButton();
        butCloseAlarm = new javax.swing.JButton();
        butOpenDoor = new javax.swing.JButton();
        butCloseDoor = new javax.swing.JButton();
        butLockDoor = new javax.swing.JButton();
        butUnlockDoor = new javax.swing.JButton();
        butHoldDoor = new javax.swing.JButton();
        butAutoSearchDoor = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        WriteOpenAlarm = new javax.swing.JButton();
        butClearTimeGroup = new javax.swing.JButton();
        WriteSmogAlarm = new javax.swing.JButton();
        ReadOpenAlarm = new javax.swing.JButton();
        ReadSmogAlarm = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        butReadTimeGroup = new javax.swing.JButton();
        txtOpenAlarm = new javax.swing.JTextField();
        txtSmogAlarm = new javax.swing.JTextField();
        butReadFireAlarmState = new javax.swing.JButton();
        butSendFireAlarm = new javax.swing.JButton();
        javax.swing.JButton butReadWorkStatus = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        butReadTransactionDatabaseDetail = new javax.swing.JButton();
        butTransactionDatabaseEmpty = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        butClearTransactionDatabase = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        cmbTransactionType = new javax.swing.JComboBox<>();
        butReadTransactionDatabaseByIndex = new javax.swing.JButton();
        butWriteTransactionDatabaseReadIndex = new javax.swing.JButton();
        butWriteTransactionDatabaseWriteIndex = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtTransactionDatabaseReadIndex = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTransactionDatabaseWriteIndex = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtReadTransactionDatabaseByIndex = new javax.swing.JTextField();
        txtReadTransactionDatabaseByQuantity = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        chkTransactionIsCircle = new javax.swing.JCheckBox();
        butReadTransactionDatabase = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtReadTransactionDatabasePacketSize = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtReadTransactionDatabaseQuantity = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        butReadCardDataBase = new javax.swing.JButton();
        butReadCardDatabaseDetail = new javax.swing.JButton();
        cmbCardDataBaseType = new javax.swing.JComboBox<>();
        butClearCardDataBase = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCard = new javax.swing.JTable();
        butReadCardDetail = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtCardData = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCardPassword = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        TxtCardExpiry = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtOpenTimes = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cmbCardStatus = new javax.swing.JComboBox<>();
        chkCardDoor4 = new javax.swing.JCheckBox();
        chkCardDoor1 = new javax.swing.JCheckBox();
        chkCardDoor2 = new javax.swing.JCheckBox();
        chkCardDoor3 = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        butAddCardToList = new javax.swing.JButton();
        butCardListClear = new javax.swing.JButton();
        butUploadCard = new javax.swing.JButton();
        butDeleteCard = new javax.swing.JButton();
        butCardListAutoCreate = new javax.swing.JButton();
        butWriteCardListBySequence = new javax.swing.JButton();
        butWriteCardListBySort = new javax.swing.JButton();
        butDeleteCardByList = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtCardAutoCreateSzie = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1020, 850));
        setSize(new java.awt.Dimension(1024, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpConnectSetting.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "????????????", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jpConnectSetting.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("???????????????");
        jpConnectSetting.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 21, -1, -1));

        RadTCPClient.setText("TCP?????????");
        RadTCPClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadTCPClientActionPerformed(evt);
            }
        });
        jpConnectSetting.add(RadTCPClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 17, -1, -1));

        RadUDP.setText("UDP");
        RadUDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadUDPActionPerformed(evt);
            }
        });
        jpConnectSetting.add(RadUDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 17, -1, -1));

        RadTCPServer.setText("TCP?????????");
        RadTCPServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadTCPServerActionPerformed(evt);
            }
        });
        jpConnectSetting.add(RadTCPServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 17, -1, -1));

        pnlTCPClient.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP ?????????"));
        pnlTCPClient.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("IP?????????");
        pnlTCPClient.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));

        txtTCPServerIP.setText("192.168.1.66");
        txtTCPServerIP.setName(""); // NOI18N
        pnlTCPClient.add(txtTCPServerIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, -1));

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("????????????");
        pnlTCPClient.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));

        txtTCPServerPort.setText("8000");
        pnlTCPClient.add(txtTCPServerPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));

        PnlUDP.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP"));
        PnlUDP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("IP?????????");
        PnlUDP.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));

        txtUDPRemoteIP.setText("192.168.1.169");
        PnlUDP.add(txtUDPRemoteIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, 20));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("????????????");
        PnlUDP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));

        txtUDPRemotePort.setText("8101");
        PnlUDP.add(txtUDPRemotePort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));

        pnlTCPServer.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP ?????????"));
        pnlTCPServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("??????????????????");
        pnlTCPServer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 79, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("???????????????");
        pnlTCPServer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 79, -1));

        txtLocalPort.setText("8000");
        pnlTCPServer.add(txtLocalPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 72, -1));

        butBeginServer.setText("????????????");
        pnlTCPServer.add(butBeginServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlTCPServer.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 45, 240, -1));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTCPClient, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnlUDP, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlTCPServer, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(pnlTCPClient, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(PnlUDP, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTCPServer, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTCPServer.getAccessibleContext().setAccessibleName("TCP ?????????");

        jpConnectSetting.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 35, 340, 75));

        getContentPane().add(jpConnectSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 350, 115));

        jpSNDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpSNDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("SN???");
        jpSNDetail.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 3, 36, -1));

        txtSN.setText("FC-8940A46060007");
        jpSNDetail.add(txtSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 0, 131, -1));

        txtPassword.setText("FFFFFFFF");
        jpSNDetail.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 0, 88, -1));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("???????????????");
        jpSNDetail.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 3, 69, -1));

        getContentPane().add(jpSNDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 130, 349, 23));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LblDate.setFont(new java.awt.Font("??????", 0, 14)); // NOI18N
        LblDate.setForeground(new java.awt.Color(0, 0, 255));
        LblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LblDate.setText("2017???10???23???");
        jPanel3.add(LblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 121, -1));

        lblTime.setFont(new java.awt.Font("??????", 0, 14)); // NOI18N
        lblTime.setForeground(new java.awt.Color(0, 0, 255));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("18:09:07");
        jPanel3.add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 121, 20));
        jPanel3.add(prCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, -1, -1));

        lblCommandName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCommandName.setText("???????????????");
        lblCommandName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel3.add(lblCommandName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 146, 80));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 14, -1, 140));

        txtLog.setColumns(20);
        txtLog.setRows(5);
        txtLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtLogMouseClicked(evt);
            }
        });
        jpLog.setViewportView(txtLog);

        getContentPane().add(jpLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 163, 501, 600));

        jTabSetting.setName(""); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(null);

        butReadConnectPassword.setText("???????????????");
        butReadConnectPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadConnectPasswordActionPerformed(evt);
            }
        });
        jPanel6.add(butReadConnectPassword);
        butReadConnectPassword.setBounds(20, 170, 93, 23);

        butWriteConnectPassword.setText("???????????????");
        butWriteConnectPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteConnectPasswordActionPerformed(evt);
            }
        });
        jPanel6.add(butWriteConnectPassword);
        butWriteConnectPassword.setBounds(250, 170, 93, 23);

        butResetConnectPassword.setText("??????????????????");
        butResetConnectPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butResetConnectPasswordActionPerformed(evt);
            }
        });
        jPanel6.add(butResetConnectPassword);
        butResetConnectPassword.setBounds(130, 170, 105, 23);

        butReadTCPSetting.setText("???TCP??????");
        butReadTCPSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadTCPSettingActionPerformed(evt);
            }
        });
        jPanel6.add(butReadTCPSetting);
        butReadTCPSetting.setBounds(20, 130, 109, 23);

        butWriteTCPSetting.setText("???TCP??????");
        butWriteTCPSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteTCPSettingActionPerformed(evt);
            }
        });
        jPanel6.add(butWriteTCPSetting);
        butWriteTCPSetting.setBounds(140, 130, 116, 23);

        ButReadDeadline.setText("???????????????????????????");
        ButReadDeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButReadDeadlineActionPerformed(evt);
            }
        });
        jPanel6.add(ButReadDeadline);
        ButReadDeadline.setBounds(310, 90, 190, 23);

        ButWriteDeadline.setText("???????????????????????????");
        ButWriteDeadline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButWriteDeadlineActionPerformed(evt);
            }
        });
        jPanel6.add(ButWriteDeadline);
        ButWriteDeadline.setBounds(350, 120, 151, 23);

        butReadVersion.setText("???????????????");
        butReadVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadVersionActionPerformed(evt);
            }
        });
        jPanel6.add(butReadVersion);
        butReadVersion.setBounds(10, 80, 138, 23);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("SN"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("SN???");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 36, -1));

        txtWriteSN.setText("MC-5824T25070244");
        txtWriteSN.setToolTipText("");
        jPanel1.add(txtWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 131, -1));

        butWriteSN.setText("???");
        butWriteSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteSNActionPerformed(evt);
            }
        });
        jPanel1.add(butWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        butReadSN.setText("???");
        butReadSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadSNActionPerformed(evt);
            }
        });
        jPanel1.add(butReadSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));

        jPanel6.add(jPanel1);
        jPanel1.setBounds(10, 10, 310, 50);

        butBeginWatch.setText("??????????????????");
        butBeginWatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butBeginWatchActionPerformed(evt);
            }
        });
        jPanel6.add(butBeginWatch);
        butBeginWatch.setBounds(30, 220, 140, 23);

        butCloseWatch.setText("??????????????????");
        butCloseWatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCloseWatchActionPerformed(evt);
            }
        });
        jPanel6.add(butCloseWatch);
        butCloseWatch.setBounds(180, 220, 130, 23);

        butCloseAlarm.setText("??????????????????");
        butCloseAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCloseAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(butCloseAlarm);
        butCloseAlarm.setBounds(30, 270, 160, 23);

        butOpenDoor.setText("????????????");
        butOpenDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butOpenDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butOpenDoor);
        butOpenDoor.setBounds(30, 330, 81, 23);

        butCloseDoor.setText("????????????");
        butCloseDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCloseDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butCloseDoor);
        butCloseDoor.setBounds(120, 330, 81, 23);

        butLockDoor.setText("????????????");
        butLockDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butLockDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butLockDoor);
        butLockDoor.setBounds(300, 330, 81, 23);

        butUnlockDoor.setText("????????????");
        butUnlockDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butUnlockDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butUnlockDoor);
        butUnlockDoor.setBounds(390, 330, 81, 23);

        butHoldDoor.setText("????????????");
        butHoldDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butHoldDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butHoldDoor);
        butHoldDoor.setBounds(210, 330, 81, 23);

        butAutoSearchDoor.setText("?????????????????????");
        butAutoSearchDoor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butAutoSearchDoorActionPerformed(evt);
            }
        });
        jPanel6.add(butAutoSearchDoor);
        butAutoSearchDoor.setBounds(20, 390, 130, 23);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("UDP???????????????????????????\n?????????????????????????????????????????????????????????????????????\n??????TCP??????????????????TCP?????????");
        jScrollPane2.setViewportView(jTextArea1);

        jPanel6.add(jScrollPane2);
        jScrollPane2.setBounds(20, 600, 390, 110);

        WriteOpenAlarm.setText("??????????????????????????????");
        WriteOpenAlarm.setToolTipText("");
        WriteOpenAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WriteOpenAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(WriteOpenAlarm);
        WriteOpenAlarm.setBounds(200, 460, 160, 23);

        butClearTimeGroup.setText("??????????????????");
        butClearTimeGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butClearTimeGroupActionPerformed(evt);
            }
        });
        jPanel6.add(butClearTimeGroup);
        butClearTimeGroup.setBounds(320, 420, 130, 23);

        WriteSmogAlarm.setText("??????????????????????????????");
        WriteSmogAlarm.setToolTipText("");
        WriteSmogAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WriteSmogAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(WriteSmogAlarm);
        WriteSmogAlarm.setBounds(200, 500, 160, 23);

        ReadOpenAlarm.setText("??????????????????????????????");
        ReadOpenAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadOpenAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(ReadOpenAlarm);
        ReadOpenAlarm.setBounds(20, 460, 160, 23);

        ReadSmogAlarm.setText("??????????????????????????????");
        ReadSmogAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadSmogAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(ReadSmogAlarm);
        ReadSmogAlarm.setBounds(20, 500, 160, 23);

        jButton5.setText("??????????????????");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5);
        jButton5.setBounds(20, 420, 130, 23);

        butReadTimeGroup.setText("??????????????????");
        butReadTimeGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadTimeGroupActionPerformed(evt);
            }
        });
        jPanel6.add(butReadTimeGroup);
        butReadTimeGroup.setBounds(170, 420, 130, 23);
        jPanel6.add(txtOpenAlarm);
        txtOpenAlarm.setBounds(370, 460, 60, 21);
        jPanel6.add(txtSmogAlarm);
        txtSmogAlarm.setBounds(370, 500, 60, 21);

        butReadFireAlarmState.setText("????????????????????????");
        butReadFireAlarmState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadFireAlarmStateActionPerformed(evt);
            }
        });
        jPanel6.add(butReadFireAlarmState);
        butReadFireAlarmState.setBounds(20, 540, 160, 23);

        butSendFireAlarm.setText("??????????????????");
        butSendFireAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butSendFireAlarmActionPerformed(evt);
            }
        });
        jPanel6.add(butSendFireAlarm);
        butSendFireAlarm.setBounds(200, 540, 110, 23);

        butReadWorkStatus.setText("?????????????????????");
        butReadWorkStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadWorkStatusActionPerformed(evt);
            }
        });
        jPanel6.add(butReadWorkStatus);
        butReadWorkStatus.setBounds(320, 540, 120, 23);

        jTabSetting.addTab("????????????", jPanel6);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("?????????");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));

        jButton2.setText("?????????");
        jButton2.setActionCommand("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

        jTabSetting.addTab("????????????", jPanel8);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        butReadTransactionDatabaseDetail.setText("???????????????????????????");
        butReadTransactionDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadTransactionDatabaseDetailActionPerformed(evt);
            }
        });
        jPanel4.add(butReadTransactionDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        butTransactionDatabaseEmpty.setBackground(new java.awt.Color(255, 0, 0));
        butTransactionDatabaseEmpty.setForeground(new java.awt.Color(255, 0, 0));
        butTransactionDatabaseEmpty.setText("??????????????????");
        butTransactionDatabaseEmpty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butTransactionDatabaseEmptyActionPerformed(evt);
            }
        });
        jPanel4.add(butTransactionDatabaseEmpty, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("???????????????????????????"));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        butClearTransactionDatabase.setBackground(new java.awt.Color(255, 0, 0));
        butClearTransactionDatabase.setForeground(new java.awt.Color(255, 0, 0));
        butClearTransactionDatabase.setText("????????????");
        butClearTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butClearTransactionDatabaseActionPerformed(evt);
            }
        });
        jPanel5.add(butClearTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("???????????????");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, -1));

        cmbTransactionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "????????????", "??????????????????", "????????????", "??????????????????", "????????????", "????????????" }));
        jPanel5.add(cmbTransactionType, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 110, -1));

        butReadTransactionDatabaseByIndex.setText("??????????????????");
        butReadTransactionDatabaseByIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadTransactionDatabaseByIndexActionPerformed(evt);
            }
        });
        jPanel5.add(butReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, -1));

        butWriteTransactionDatabaseReadIndex.setText("?????????????????????");
        butWriteTransactionDatabaseReadIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteTransactionDatabaseReadIndexActionPerformed(evt);
            }
        });
        jPanel5.add(butWriteTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        butWriteTransactionDatabaseWriteIndex.setText("?????????????????????");
        butWriteTransactionDatabaseWriteIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteTransactionDatabaseWriteIndexActionPerformed(evt);
            }
        });
        jPanel5.add(butWriteTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("???????????????");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 73, 79, -1));

        txtTransactionDatabaseReadIndex.setText("0");
        jPanel5.add(txtTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 60, -1));

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("???????????????");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 103, 79, -1));

        txtTransactionDatabaseWriteIndex.setText("0");
        jPanel5.add(txtTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 60, -1));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("??????????????????");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 133, 79, -1));

        txtReadTransactionDatabaseByIndex.setText("1");
        jPanel5.add(txtReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 60, -1));

        txtReadTransactionDatabaseByQuantity.setText("10");
        jPanel5.add(txtReadTransactionDatabaseByQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 60, -1));

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("????????????");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 133, 60, -1));

        chkTransactionIsCircle.setText("??????");
        jPanel5.add(chkTransactionIsCircle, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));

        butReadTransactionDatabase.setText("????????????");
        butReadTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadTransactionDatabaseActionPerformed(evt);
            }
        });
        jPanel5.add(butReadTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 120, -1));

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("?????????????????????");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 90, -1));

        txtReadTransactionDatabasePacketSize.setText("200");
        jPanel5.add(txtReadTransactionDatabasePacketSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 60, -1));

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("????????????");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 60, -1));

        txtReadTransactionDatabaseQuantity.setText("0");
        jPanel5.add(txtReadTransactionDatabaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, 60, -1));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 470, 290));

        jTabSetting.addTab("????????????", jPanel4);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        butReadCardDataBase.setText("?????????????????????????????????");
        butReadCardDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadCardDataBaseActionPerformed(evt);
            }
        });
        jPanel2.add(butReadCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, -1));

        butReadCardDatabaseDetail.setText("?????????????????????");
        butReadCardDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadCardDatabaseDetailActionPerformed(evt);
            }
        });
        jPanel2.add(butReadCardDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, -1));

        cmbCardDataBaseType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cmbCardDataBaseType, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 130, -1));

        butClearCardDataBase.setBackground(new java.awt.Color(255, 51, 51));
        butClearCardDataBase.setForeground(new java.awt.Color(255, 0, 51));
        butClearCardDataBase.setText("?????????????????????");
        butClearCardDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butClearCardDataBaseActionPerformed(evt);
            }
        });
        jPanel2.add(butClearCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        jLabel9.setText("????????????");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 52, -1, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tblCard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblCard);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 480, 400));

        butReadCardDetail.setText("??????????????????????????????????????????");
        butReadCardDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butReadCardDetailActionPerformed(evt);
            }
        });
        jPanel2.add(butReadCardDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 230, -1));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("?????????");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 543, 36, -1));

        txtCardData.setText("123456");
        jPanel2.add(txtCardData, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 70, -1));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("?????????");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 540, 36, -1));

        txtCardPassword.setText("8888");
        jPanel2.add(txtCardPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 540, 70, -1));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("???????????????");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 543, 70, -1));
        jLabel12.getAccessibleContext().setAccessibleName("????????????");

        TxtCardExpiry.setText("2030-12-30 12:30");
        jPanel2.add(TxtCardExpiry, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 110, -1));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("???????????????");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 70, -1));

        txtOpenTimes.setText("65535");
        jPanel2.add(txtOpenTimes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 70, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("?????????");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 570, 50, -1));

        cmbCardStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "??????", "?????????", "?????????" }));
        jPanel2.add(cmbCardStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 570, 70, -1));

        chkCardDoor4.setSelected(true);
        chkCardDoor4.setText("???4");
        jPanel2.add(chkCardDoor4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 600, -1, -1));

        chkCardDoor1.setSelected(true);
        chkCardDoor1.setText("???1");
        jPanel2.add(chkCardDoor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, -1, -1));

        chkCardDoor2.setSelected(true);
        chkCardDoor2.setText("???2");
        jPanel2.add(chkCardDoor2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 600, -1, -1));

        chkCardDoor3.setSelected(true);
        chkCardDoor3.setText("???3");
        jPanel2.add(chkCardDoor3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, -1, -1));

        jLabel15.setText("????????????");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, -1));

        butAddCardToList.setText("???????????????");
        butAddCardToList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butAddCardToListActionPerformed(evt);
            }
        });
        jPanel2.add(butAddCardToList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 160, -1));

        butCardListClear.setText("????????????");
        butCardListClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCardListClearActionPerformed(evt);
            }
        });
        jPanel2.add(butCardListClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 490, 110, -1));

        butUploadCard.setText("??????????????????????????????");
        butUploadCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butUploadCardActionPerformed(evt);
            }
        });
        jPanel2.add(butUploadCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 630, -1, -1));

        butDeleteCard.setText("???????????????????????????");
        butDeleteCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butDeleteCardActionPerformed(evt);
            }
        });
        jPanel2.add(butDeleteCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 630, -1, -1));

        butCardListAutoCreate.setText("??????????????????");
        butCardListAutoCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCardListAutoCreateActionPerformed(evt);
            }
        });
        jPanel2.add(butCardListAutoCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 160, -1));

        butWriteCardListBySequence.setText("????????????????????????????????????");
        butWriteCardListBySequence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteCardListBySequenceActionPerformed(evt);
            }
        });
        jPanel2.add(butWriteCardListBySequence, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, -1, -1));

        butWriteCardListBySort.setText("?????????????????????????????????");
        butWriteCardListBySort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butWriteCardListBySortActionPerformed(evt);
            }
        });
        jPanel2.add(butWriteCardListBySort, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));

        butDeleteCardByList.setText("????????????????????????");
        butDeleteCardByList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butDeleteCardByListActionPerformed(evt);
            }
        });
        jPanel2.add(butDeleteCardByList, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 690, -1, -1));

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("?????????????????????");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 663, 90, -1));

        txtCardAutoCreateSzie.setText("2000");
        jPanel2.add(txtCardAutoCreateSzie, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 660, 70, -1));

        jTabSetting.addTab("????????????", jPanel2);

        getContentPane().add(jTabSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 510, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RadTCPClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadTCPClientActionPerformed
        // TODO add your handling code here:
        ShowConnectPanel();
    }//GEN-LAST:event_RadTCPClientActionPerformed

    private void RadUDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadUDPActionPerformed
        // TODO add your handling code here:
        ShowConnectPanel();
    }//GEN-LAST:event_RadUDPActionPerformed

    private void RadTCPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadTCPServerActionPerformed
        // TODO add your handling code here:
        ShowConnectPanel();
    }//GEN-LAST:event_RadTCPServerActionPerformed

    private void txtLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLogMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            strLog.delete(0, strLog.length());
            txtLog.setText("");
        }

    }//GEN-LAST:event_txtLogMouseClicked

    /**
     * ???????????????
     *
     * @param evt
     */
    private void butReadTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadTransactionDatabaseActionPerformed
        // ???????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;
        int type = cmbTransactionType.getSelectedIndex() + 1;

        String strPacketSize = txtReadTransactionDatabasePacketSize.getText();
        if (!StringUtil.IsNum(strPacketSize) || strPacketSize.length() > 3) {
            JOptionPane.showMessageDialog(null, "????????????????????????????????????????????????1-300???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int PacketSize = Integer.valueOf(strPacketSize);
        if (PacketSize > 300 || PacketSize < 0) {
            JOptionPane.showMessageDialog(null, "????????????????????????????????????????????????1-300???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String strQuantity = txtReadTransactionDatabaseQuantity.getText();

        if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 6) {
            JOptionPane.showMessageDialog(null, "????????????????????????????????????????????????0-160000???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int Quantity = Integer.valueOf(strQuantity);
        if (Quantity < 0) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????0???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
        par.PacketSize = PacketSize;
        par.Quantity = Quantity;
        
        ReadTransactionDatabase cmd = new Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabase(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) y;
            x.append("\n???????????????");
            x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
            x.append("????????????????????????");
            x.append(result.Quantity);
            x.append("??????????????????????????????");
            x.append(result.readable);

            if (result.Quantity > 0) {
                x.append("\n");
                //????????????????????????
                StringBuilder log = new StringBuilder(result.Quantity * 100);
                String[] sTransactionList = null;
                switch (result.DatabaseType) {
                    case OnCardTransaction:
                    sTransactionList = mCardTransactionList;
                    break;
                    case OnButtonTransaction:
                    sTransactionList = mButtonTransactionList;
                    break;
                    case OnDoorSensorTransaction:
                    sTransactionList = mDoorSensorTransactionList;
                    break;
                    case OnSoftwareTransaction:
                    sTransactionList = mSoftwareTransactionList;
                    break;
                    case OnAlarmTransaction:
                    sTransactionList = mAlarmTransactionList;
                    break;
                    case OnSystemTransaction:
                    sTransactionList = mSystemTransactionList;
                    break;
                }

                PrintTransactionDatabase(result.TransactionList, log, sTransactionList);

                String path = WriteFile("??????????????????????????????", log, false);
                if (path == null) {
                    x.append("\n??????????????????");
                } else {
                    x.append("\n???????????????????????????");
                    x.append(path);
                }
                x.append("\n");
                if (result.Quantity < 1000) {
                    x.append(log);
                }

            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadTransactionDatabaseActionPerformed

    private void butWriteTransactionDatabaseWriteIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteTransactionDatabaseWriteIndexActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        int type = cmbTransactionType.getSelectedIndex() + 1;
        String strWriteIndex = txtTransactionDatabaseWriteIndex.getText();
        if (!StringUtil.IsNum(strWriteIndex) || strWriteIndex.length() > 6) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int index = Integer.valueOf(strWriteIndex);
        if (index > 160000 || index < 0) {
            JOptionPane.showMessageDialog(null, "????????????????????????0-160000???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WriteTransactionDatabaseWriteIndex_Parameter par = new WriteTransactionDatabaseWriteIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
        par.WriteIndex = index;
        WriteTransactionDatabaseWriteIndex cmd = new WriteTransactionDatabaseWriteIndex(par);

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteTransactionDatabaseWriteIndexActionPerformed

    private void butWriteTransactionDatabaseReadIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteTransactionDatabaseReadIndexActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        int type = cmbTransactionType.getSelectedIndex() + 1;
        String strReadIndex = txtTransactionDatabaseReadIndex.getText();
        if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int index = Integer.valueOf(strReadIndex);
        if (index > 160000 || index < 0) {
            JOptionPane.showMessageDialog(null, "????????????????????????0-160000???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WriteTransactionDatabaseReadIndex_Parameter par = new WriteTransactionDatabaseReadIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
        par.ReadIndex = index;
        par.IsCircle = chkTransactionIsCircle.isSelected();
        WriteTransactionDatabaseReadIndex cmd = new WriteTransactionDatabaseReadIndex(par);

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteTransactionDatabaseReadIndexActionPerformed

    private void butReadTransactionDatabaseByIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadTransactionDatabaseByIndexActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;

        int type = cmbTransactionType.getSelectedIndex() + 1;
        String strReadIndex = txtReadTransactionDatabaseByIndex.getText();
        if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int index = Integer.valueOf(strReadIndex);
        if (index > 160000 || index < 0) {
            JOptionPane.showMessageDialog(null, "????????????????????????????????????0-160000???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String strQuantity = txtReadTransactionDatabaseByQuantity.getText();

        if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 3) {
            JOptionPane.showMessageDialog(null, "?????????????????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int Quantity = Integer.valueOf(strQuantity);
        if (Quantity > 400 || Quantity <= 0) {
            JOptionPane.showMessageDialog(null, "???????????????????????????1-400???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
        par.ReadIndex = index;
        par.Quantity = Quantity;
        //???????????????
        Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabaseByIndex cmd = new Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabaseByIndex(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) y;
            x.append("\n???????????????");
            x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
            x.append("???????????????");
            x.append(result.ReadIndex);
            x.append("??????????????????");
            x.append(result.Quantity);

            if (result.Quantity > 0) {
                x.append("\n");
                //????????????????????????
                StringBuilder log = new StringBuilder(result.Quantity * 100);
                String[] sTransactionList = null;
                switch (result.DatabaseType) {
                    case OnCardTransaction:
                    sTransactionList = mCardTransactionList;
                    break;
                    case OnButtonTransaction:
                    sTransactionList = mButtonTransactionList;
                    break;
                    case OnDoorSensorTransaction:
                    sTransactionList = mDoorSensorTransactionList;
                    break;
                    case OnSoftwareTransaction:
                    sTransactionList = mSoftwareTransactionList;
                    break;
                    case OnAlarmTransaction:
                    sTransactionList = mAlarmTransactionList;
                    break;
                    case OnSystemTransaction:
                    sTransactionList = mSystemTransactionList;
                    break;
                }

                PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
                x.append(log);
            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadTransactionDatabaseByIndexActionPerformed

    private void butClearTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butClearTransactionDatabaseActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        int type = cmbTransactionType.getSelectedIndex() + 1;
        ClearTransactionDatabase_Parameter par = new ClearTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
        ClearTransactionDatabase cmd = new ClearTransactionDatabase(par);

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butClearTransactionDatabaseActionPerformed

    private void butTransactionDatabaseEmptyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butTransactionDatabaseEmptyActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        TransactionDatabaseEmpty cmd = new TransactionDatabaseEmpty(par);

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butTransactionDatabaseEmptyActionPerformed

    /**
     * ???????????????????????????
     *
     * @param evt
     */
    private void butReadTransactionDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadTransactionDatabaseDetailActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        ReadTransactionDatabaseDetail cmd = new ReadTransactionDatabaseDetail(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadTransactionDatabaseDetail_Result result = (ReadTransactionDatabaseDetail_Result) y;
            PrintTransactionDatabaseDetail(mWatchTypeNameList[1], result.DatabaseDetail.CardTransactionDetail, x);
            PrintTransactionDatabaseDetail(mWatchTypeNameList[2], result.DatabaseDetail.ButtonTransactionDetail, x);
            PrintTransactionDatabaseDetail(mWatchTypeNameList[3], result.DatabaseDetail.DoorSensorTransactionDetail, x);
            PrintTransactionDatabaseDetail(mWatchTypeNameList[4], result.DatabaseDetail.SoftwareTransactionDetail, x);
            PrintTransactionDatabaseDetail(mWatchTypeNameList[5], result.DatabaseDetail.AlarmTransactionDetail, x);
            PrintTransactionDatabaseDetail(mWatchTypeNameList[6], result.DatabaseDetail.SystemTransactionDetail, x);

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadTransactionDatabaseDetailActionPerformed

    private void butDeleteCardByListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDeleteCardByListActionPerformed
        // ????????????????????????
        if (mCardList == null) {
            JOptionPane.showMessageDialog(null, "?????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;//????????????????????????????????????

        int ilstLen = mCardList.size();
        
        //???????????????
        //DeleteCard cmd = new DeleteCard(par);
        String[] lst = new String[ilstLen];
            for (int i = 0; i < ilstLen; i++) {
                lst[i] = mCardList.get(i).GetCardData();
            }
            DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
          FC8800Command cmd = new Net.PC15.FC89H.Command.Card.DeleteCard(par);
        
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butDeleteCardByListActionPerformed

    private void butWriteCardListBySortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteCardListBySortActionPerformed
        //???????????????????????????????????????
        if (mCardList == null) {
            JOptionPane.showMessageDialog(null, "?????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;//????????????????????????????????????

        WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(dt, mCardList);
        //???????????????
        //WriteCardListBySort cmd = new WriteCardListBySort(par);
        FC8800Command cmd = new Net.PC15.FC89H.Command.Card.WriteCardListBySort(par);
       
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            WriteCardListBySort_Result result = (WriteCardListBySort_Result) y;
            //ArrayList<Net.PC15.FC89H.Command.Data.CardDetail> _list =  result.CardList;
            ArrayList<? extends CardDetail> _list =  result.CardList;
            x.append("????????????");
            if (result.FailTotal > 0) {
                x.append("???????????????");
                x.append(result.FailTotal);
                x.append("??????????????????\n");
                //for (Net.PC15.FC89H.Command.Data.CardDetail c : _list) {
                for (CardDetail c : _list) {
                    x.append(c.GetCardData());
                    x.append("\n");
                }
            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteCardListBySortActionPerformed

    private void butWriteCardListBySequenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteCardListBySequenceActionPerformed
        //??????????????????????????????????????????
        if (mCardList == null) {
            JOptionPane.showMessageDialog(null, "?????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        
        dt.Timeout = 8000;//????????????????????????????????????
        
        //????????? ??????
        WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, mCardList);
        
        //WriteCardListBySequence cmd = new WriteCardListBySequence(par);
        FC8800Command cmd = new Net.PC15.FC89H.Command.Card.WriteCardListBySequence(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
            x.append("????????????");
            //ArrayList<CardDetail> _list = (Net.PC15.FC89H.Command.Data.CardDetail)result.CardList;
             ArrayList<? extends CardDetail> _list =  result.CardList;
            if (result.FailTotal > 0) {
                x.append("???????????????");
                x.append(result.FailTotal);
                x.append("??????????????????\n");
                //for (Net.PC15.FC89H.Command.Data.CardDetail c : _list) {
                for (CardDetail c : _list) {
                    x.append(c.GetCardData());
                    x.append("\n");
                }
            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteCardListBySequenceActionPerformed

    private void butCardListAutoCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCardListAutoCreateActionPerformed
        // ???????????????????????????
        int maxSize = 1000;

        String strSize = txtCardAutoCreateSzie.getText();
        if (!StringUtil.IsNum(strSize) || strSize.length() > 6) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????????????????1-120000???", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        maxSize = Integer.valueOf(strSize);

        if (mCardList == null) {
            mCardList = new ArrayList<>(10000);
        }
        

        if ((maxSize + mCardList.size()) > 120000) {
            JOptionPane.showMessageDialog(null, "???????????????????????????????????????????????????12??????", "????????????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        Random rnd = new Random();
        int max = Integer.MAX_VALUE;
        int min = 100000000;
        int iSearch = 0;

        CardDetail surCard = GetCardDetail();
        if (surCard == null) {
            return;
        }

        Collections.sort(mCardList);
        //???????????????
        ArrayList<Net.PC15.FC89H.Command.Data.CardDetail> tmplst = new ArrayList<>(1500);
        
        //Calendar time=Calendar.getInstance();
        while (maxSize > 0) {
            long card = rnd.nextInt(max) % (max - min + 1) + min;
            //String rndCard = Net.PC15.Util.StringUtil.randomHexString(16);
            //???????????????
            //CardDetail cd = new CardDetail(card);
            Net.PC15.FC89H.Command.Data.CardDetail cd = new Net.PC15.FC89H.Command.Data.CardDetail();//
            try 
            {
                cd.SetCardDataHEX(String.valueOf(card));
            }
            catch (Exception e){
                
            }
            iSearch = Net.PC15.FC89H.Command.Data.CardDetail.SearchCardDetail(mCardList, cd);
            if (iSearch == -1) {
                if (tmplst.indexOf(cd) == -1) {
                    cd.Password = surCard.Password;//????????????
                    cd.Expiry = surCard.Expiry;//???????????????
                    cd.OpenTimes = surCard.OpenTimes;//????????????
                    cd.CardStatus = surCard.CardStatus;
                    //??????4?????????????????????
                    for (int i = 1; i <= 4; i++) {
                        cd.SetTimeGroup(i, 1);//?????????????????????1???
                        cd.SetDoor(i, surCard.GetDoor(i));//?????????????????????
                    }
                    cd.SetNormal();//????????????????????????
                    cd.HolidayUse = true;//???????????????????????????

                    tmplst.add(cd);
                    if (tmplst.size() >= 1000) {
                        mCardList.addAll(tmplst);
                        Collections.sort(mCardList);

                        tmplst.clear();
                    }

                    maxSize--;
                }

            }

        }
        if (tmplst.size() > 0) {
            mCardList.addAll(tmplst);
            Collections.sort(mCardList);

            tmplst.clear();
        }
        //Calendar endtime=Calendar.getInstance();
        //int hs=(int)(endtime.getTimeInMillis()- time.getTimeInMillis());
        //System.out.println("?????????" + hs);
        FillCardToList();
    }//GEN-LAST:event_butCardListAutoCreateActionPerformed

    private void butDeleteCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDeleteCardActionPerformed
        //????????????
        Net.PC15.FC89H.Command.Data.CardDetail cd = GetCardDetail();
        if (cd == null) {
            return;
        }
        //??????????????????????????????????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;//????????????????????????????????????

        String[] lst = new String[1];
        lst[0] = cd.GetCardData();
        DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
        Net.PC15.FC89H.Command.Card.DeleteCard cmd = new Net.PC15.FC89H.Command.Card.DeleteCard(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            y = y;

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butDeleteCardActionPerformed

    private void butUploadCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butUploadCardActionPerformed
        //???????????????????????????
        Net.PC15.FC89H.Command.Data.CardDetail cd = GetCardDetail();
        if (cd == null) {
            return;
        }
        ArrayList<Net.PC15.FC89H.Command.Data.CardDetail> lst = new ArrayList<>(1);
        lst.add(cd);

        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;//????????????????????????????????????
        //???????????????
        WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, lst);
        //WriteCardListBySequence cmd = new WriteCardListBySequence(par);
        FC8800Command cmd = new Net.PC15.FC89H.Command.Card.WriteCardListBySequence(par);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
            x.append("????????????");
            ArrayList<? extends CardDetail> _list =  result.CardList;
            //ArrayList<Net.PC15.FC89H.Command.Data.CardDetail.CardDetail> list = (Net.PC15.FC89H.Command.Data.CardDetail)result.CardList;
            if (result.FailTotal > 0) {
                x.append("???????????????");
                x.append(result.FailTotal);
                x.append("??????????????????\n");
                for (CardDetail c : _list) {
                    x.append(c.GetCardData());
                    x.append("\n");
                }
            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butUploadCardActionPerformed

    private void butCardListClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCardListClearActionPerformed
        ClearCardList();
        mCardList.clear();
        mCardList = null;
    }//GEN-LAST:event_butCardListClearActionPerformed

    private void butAddCardToListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddCardToListActionPerformed
        Net.PC15.FC89H.Command.Data.CardDetail cd = GetCardDetail();
        if (cd == null) {
            return;
        }
        if (mCardList == null) {
            mCardList = new ArrayList<>(1000);
        }
        //????????????
        int iIndex = mCardList.indexOf(cd);
        if (iIndex > -1) {
            mCardList.remove(iIndex);
        }

        mCardList.add(cd);

        if (iIndex > -1) {
            FillCardToList();//????????????
        } else {
            Object[] row = CardDetailToRow(cd, mCardList.size());

            DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
            tableModel.addRow(row);
        }
    }//GEN-LAST:event_butAddCardToListActionPerformed

    private void butReadCardDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadCardDetailActionPerformed
        CardDetail cd = GetCardDetail();
        if (cd == null) {
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;//????????????????????????????????????

        ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(dt, cd.GetCardData());
        //ReadCardDetail cmd = new ReadCardDetail(par);
        FC8800Command cmd = new Net.PC15.FC89H.Command.Card.ReadCardDetail(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadCardDetail_Result result = (ReadCardDetail_Result) y;
            if (result.IsReady) {
                x.append("?????????????????????????????????????????????\n");
                
                Net.PC15.FC89H.Command.Data.CardDetail card = (Net.PC15.FC89H.Command.Data.CardDetail)result.Card;
                
                Object[] arr = CardDetailToRow(card, 0);
                StringBuilder builder = new StringBuilder(200);
                builder.append("?????????");
                builder.append(arr[1]);//cd.CardData
                builder.append("????????????");
                builder.append(arr[2]);//cd.Password
                builder.append("???????????????");
                builder.append(arr[3]);//cd.Expiry
                builder.append("\n???????????????");
                builder.append(arr[5]);//OpenTimes
                builder.append("????????????");
                builder.append(arr[6]);//Privilege
                builder.append("???????????????");
                builder.append(arr[4]);//cd.CardStatus

                builder.append("??????????????????\n");
                int arrIndex = 7;
                for (int i = 1; i <= 4; i++) {
                    builder.append("???");
                    builder.append(i);
                    builder.append("???");
                    builder.append(arr[arrIndex]);
                    arrIndex++;
                    builder.append("??????");
                    builder.append(",????????????:");
                    builder.append(arr[arrIndex]);
                    arrIndex++;
                    builder.append("???");
                }
                builder.append("\n???????????????:");
                builder.append(arr[arrIndex]);
                x.append(builder);
                builder = null;
            } else {
                x.append("?????????????????????????????????");
            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadCardDetailActionPerformed

    private void butClearCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butClearCardDataBaseActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 5000;

        int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
        ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(dt, iType);
        ClearCardDataBase cmd = new ClearCardDataBase(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
            x.append("???????????????");
            x.append(cmbCardDataBaseType.getSelectedItem());
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butClearCardDataBaseActionPerformed

    private void butReadCardDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadCardDatabaseDetailActionPerformed
        //??????????????????????????????????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        ReadCardDatabaseDetail cmd = new ReadCardDatabaseDetail(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
            x.append("\n?????????????????????");
            x.append(result.SortDataBaseSize);
            x.append("????????????????????????");
            x.append(result.SortCardSize);
            x.append("\n????????????????????????");
            x.append(result.SequenceDataBaseSize);
            x.append("???????????????????????????");
            x.append(result.SequenceCardSize);
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadCardDatabaseDetailActionPerformed

    private void butReadCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadCardDataBaseActionPerformed
        CommandDetail dt = getCommandDetail();

        if (dt == null) {
            return;
        }

        dt.Timeout = 10000;
        int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
        ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(dt, iType);
        //???????????????
        //ReadCardDataBase cmd = new ReadCardDataBase(par);
        Net.PC15.FC89H.Command.Card.ReadCardDataBase cmd = new Net.PC15.FC89H.Command.Card.ReadCardDataBase(par);
        String[] CardTypeList = new String[]{"", "?????????", "????????????", "????????????"};
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadCardDataBase_Result result = (ReadCardDataBase_Result) y;
            x.append("?????????????????????");
            x.append(result.DataBaseSize);
            x.append("???????????????????????????");
            x.append(CardTypeList[result.CardType]);

            if (result.DataBaseSize > 0) {
                //ArrayList<Net.PC15.FC89H.Command.Data.CardDetail> _List =  result.CardList;
                //mCardList.clear();
                mCardList = result.CardList;
                String CardStatusList[] = new String[]{"??????", "?????????", "?????????"};
                DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
                tableModel.setRowCount(0);// ???????????????

                int Index = 1;
                StringBuilder builder = new StringBuilder(result.DataBaseSize * 140);
                for (Net.PC15.FC89H.Command.Data.CardDetail cd : mCardList) {
                   
                    Object[] arr = CardDetailToRow(cd, Index);

                    builder.append("?????????");
                    builder.append(arr[1]);//cd.CardData
                    builder.append("????????????");
                    builder.append(arr[2]);//cd.Password
                    builder.append("???????????????");
                    builder.append(arr[3]);//cd.Expiry
                    builder.append("??????????????????");
                    builder.append(arr[5]);//OpenTimes
                    builder.append("????????????");
                    builder.append(arr[6]);//Privilege
                    builder.append("???????????????");
                    builder.append(arr[4]);//cd.CardStatus

                    builder.append("??????????????????");
                    int arrIndex = 7;
                    for (int i = 1; i <= 4; i++) {
                        builder.append("???");
                        builder.append(i);
                        builder.append("???");
                        builder.append(arr[arrIndex]);
                        arrIndex++;
                        builder.append("??????");
                        builder.append(",????????????:");
                        builder.append(arr[arrIndex]);
                        arrIndex++;
                        builder.append("???");
                    }
                    builder.append("???????????????:");
                    builder.append(arr[arrIndex]);
                    builder.append("\n");
                    // ?????????????????????
                    tableModel.addRow(arr);
                    Index += 1;

                }

                // ????????????
                tblCard.invalidate();
                try {
                    String file = WriteFile("CardDatabase", builder, false);
                    if (file == null) {
                        x.append("\n????????????????????????");
                    } else {
                        x.append("\n?????????????????????????????????");
                        x.append(file);
                    }
                } catch (Exception e) {
                }

            }

        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadCardDataBaseActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;

        CommandParameter par = new CommandParameter(dt);
        WriteTime cmd = new WriteTime(par);

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;

        CommandParameter par = new CommandParameter(dt);
        ReadTime cmd = new ReadTime(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("??????????????????");

            ReadTime_Result result = (ReadTime_Result) y;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            x.append(sdf.format(result.ControllerDate.getTime()));
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * ?????????????????????
     *
     * @param evt
     */
    private void butAutoSearchDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAutoSearchDoorActionPerformed
        //???????????????????????????
        CommandDetail dt = new CommandDetail();

        UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
        udp.LocalIP = "192.168.1.138";
        udp.LocalPort = 8886;
        udp.Broadcast = true;
        dt.Connector = udp;
        dt.Identity = new FC8800Identity("0000000000000000", FC8800Command.NULLPassword, E_ControllerType.FC8900);

        Random rnd = new Random();
        int max = 65535;
        int min = 10000;

        //?????????????????????????????????
        dt.RestartCount = 0;
        dt.Timeout = 5000;//??????5????????????????????????????????????5?????????

        //?????????????????????????????????
        SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;//????????????

        SearchTimes = 1;//????????????;

        SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
        SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            SearchEquptOnNetNum_Result result = (SearchEquptOnNetNum_Result) y;
            x.append("\n???");
            x.append(SearchTimes);
            x.append("???????????????????????????????????????");
            x.append(result.SearchTotal);
            x.append("??????");
            if (result.SearchTotal > 0) {
                x.append("\n");
                PrintSearchDoor(result.ResultList, x);
                //????????????????????????????????????????????????????????????????????????
                SetDoorNetFlag(result.ResultList);
            }
            if (SearchTimes < 4) {
                SearchEquptOnNetNum_Parameter par1 = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
                SearchEquptOnNetNum cmd1 = new SearchEquptOnNetNum(par1);
                _Allocator.AddCommand(cmd1);

            } else {
                x.append("\n*************???????????????*******************");
                return;
            }
            SearchTimes++;
        });

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butAutoSearchDoorActionPerformed

    /**
     * ????????????
     *
     * @param evt
     */
    private void butHoldDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butHoldDoorActionPerformed
        //????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;

        RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
        par.Door.SetDoor(1, 1);//??????1??????????????????
        par.Door.SetDoor(2, 0);//??????2?????????????????????
        HoldDoor cmd = new HoldDoor(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butHoldDoorActionPerformed

    private void butUnlockDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butUnlockDoorActionPerformed
        //??????????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
        par.Door.SetDoor(1, 1);//??????1??????????????????
        par.Door.SetDoor(2, 0);//??????2?????????????????????
        UnlockDoor cmd = new UnlockDoor(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butUnlockDoorActionPerformed

    private void butLockDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butLockDoorActionPerformed
        //????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
        par.Door.SetDoor(1, 1);//??????1??????????????????
        par.Door.SetDoor(2, 0);//??????2?????????????????????
        LockDoor cmd = new LockDoor(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butLockDoorActionPerformed

    /**
     * ????????????
     *
     * @param evt
     */
    private void butCloseDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCloseDoorActionPerformed
        //????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
        par.Door.SetDoor(1, 1);//??????1??????????????????
        par.Door.SetDoor(2, 0);//??????2?????????????????????
        CloseDoor cmd = new CloseDoor(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butCloseDoorActionPerformed

    /**
     * ????????????
     *
     * @param evt
     */
    private void butOpenDoorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butOpenDoorActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        dt.Timeout = 1000;

        OpenDoor_Parameter par = new OpenDoor_Parameter(dt);
        par.Door.SetDoor(1, 1);//??????1??????????????????
        par.Door.SetDoor(2, 0);//??????2?????????????????????
        OpenDoor cmd = new OpenDoor(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butOpenDoorActionPerformed

    /**
     * ????????????????????????????????????
     *
     * @param evt
     */
    private void butCloseAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCloseAlarmActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CloseAlarm_Parameter par = new CloseAlarm_Parameter(dt);
        par.Alarm.Alarm = 65535;//??????????????????
        CloseAlarm cmd = new CloseAlarm(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            y = y;
            
        });
    }//GEN-LAST:event_butCloseAlarmActionPerformed

    private void butCloseWatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCloseWatchActionPerformed
        //??????????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        CloseWatch cmd = new CloseWatch(par);

        _Allocator.CloseForciblyConnect(dt.Connector);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butCloseWatchActionPerformed

    private void butBeginWatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butBeginWatchActionPerformed
        // ??????????????????
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        BeginWatch cmd = new BeginWatch(par);

        _Allocator.OpenForciblyConnect(dt.Connector);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butBeginWatchActionPerformed

    private void butReadSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadSNActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadSN(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("?????????SN???");
            ReadSN_Result result = (ReadSN_Result) y;
            x.append(result.SN);
            txtWriteSN.setText(result.SN);
            txtSN.setText(result.SN);
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadSNActionPerformed

    private void butWriteSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteSNActionPerformed
        String sSN = txtWriteSN.getText();
        if (sSN.length() != 16) {
            JOptionPane.showMessageDialog(null, "SN?????????16??????", "??????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        WriteSN_Parameter par = new WriteSN_Parameter(dt, sSN);
        INCommand cmd = new WriteSN(par);

        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("?????????SN???");
            String sSN1 = txtWriteSN.getText();
            x.append(sSN1);
            txtSN.setText(sSN1);
        });

        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteSNActionPerformed

    private void butReadVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadVersionActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadVersion(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadVersion_Result result = (ReadVersion_Result) y;
            x.append("???????????????????????????");
            x.append(result.Version);
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadVersionActionPerformed

    private void ButWriteDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButWriteDeadlineActionPerformed
        CommandDetail dt = getCommandDetail();
        WriteDeadline_Parameter par = new WriteDeadline_Parameter(dt, 1000);
        INCommand cmd = new WriteDeadline(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_ButWriteDeadlineActionPerformed

    private void ButReadDeadlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButReadDeadlineActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadDeadline(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadDeadline_Result result = (ReadDeadline_Result) y;
            if (result.Deadline == 0) {
                x.append("?????????????????????");
            } else if (result.Deadline == 65535) {
                x.append("????????????????????????");
            } else {
                x.append("??????????????????????????????");
                x.append(result.Deadline);
                x.append("???.");
            }
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_ButReadDeadlineActionPerformed

    private void butWriteTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteTCPSettingActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        if (RadUDP.isSelected()) {
            dt.Timeout = 15000;//UDP????????????
        }

        if (mReadTCPDetail == null) {
            JOptionPane.showMessageDialog(null, "????????????TCP?????????", "??????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mReadTCPDetail.SetServerIP("0.0.0.0");
        mReadTCPDetail.SetServerAddr("www.123.cn");

        WriteTCPSetting_Parameter par = new WriteTCPSetting_Parameter(dt, mReadTCPDetail);
        INCommand cmd = new WriteTCPSetting(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteTCPSettingActionPerformed

    private void butReadTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadTCPSettingActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        if (RadUDP.isSelected()) {
            dt.Timeout = 15000;//UDP????????????
        }

        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadTCPSetting(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadTCPSetting_Result result = (ReadTCPSetting_Result) y;
            TCPDetail tcp = result.TCP;
            PirntTCPDetail(tcp, x);
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadTCPSettingActionPerformed

    private void butResetConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butResetConnectPasswordActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ResetConnectPassword(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butResetConnectPasswordActionPerformed

    private void butWriteConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butWriteConnectPasswordActionPerformed
        //String pwd = txtPassword.getText();
        String pwd = "12345678";
        if (pwd.length() != 8) {
            JOptionPane.showMessageDialog(null, "?????????????????????8??????", "??????", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        WriteConnectPassword_Parameter par = new WriteConnectPassword_Parameter(dt, pwd);
        INCommand cmd = new WriteConnectPassword(par);
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butWriteConnectPasswordActionPerformed

    private void butReadConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadConnectPasswordActionPerformed
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        INCommand cmd = new ReadConnectPassword(par);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("???????????????");
            ReadConnectPassword_Result result = (ReadConnectPassword_Result) y;
            x.append(result.Password);
            txtPassword.setText(result.Password);
        });
        _Allocator.AddCommand(cmd);
    }//GEN-LAST:event_butReadConnectPasswordActionPerformed

    private void WriteOpenAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WriteOpenAlarmActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        WriteOpenAlarmOption_Parameter par = new WriteOpenAlarmOption_Parameter(dt);
        par.Option = Short.valueOf(txtOpenAlarm.getText());
        WriteOpenAlarmOption cmd = new WriteOpenAlarmOption(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            y = y;
            
        });
    }//GEN-LAST:event_WriteOpenAlarmActionPerformed

    //??????????????????
    private void butClearTimeGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butClearTimeGroupActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        
        CommandParameter par = new CommandParameter(dt);
        ClearTimeGroup cmd = new ClearTimeGroup(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            y = y;
            x.append("????????????");
        });
        
    }//GEN-LAST:event_butClearTimeGroupActionPerformed

    private void WriteSmogAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WriteSmogAlarmActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        SendSmogAlarm cmd = new SendSmogAlarm(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("????????????");
        });
    }//GEN-LAST:event_WriteSmogAlarmActionPerformed

    private void ReadOpenAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadOpenAlarmActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        
        ReadOpenAlarmOption cmd = new ReadOpenAlarmOption(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadOpenAlarmOption_Result _result = (ReadOpenAlarmOption_Result) y;
            txtOpenAlarm.setText(String.valueOf(_result.OpenAlarm));
            x.append("????????????????????????????????????" + _result.OpenAlarm);
        });
    }//GEN-LAST:event_ReadOpenAlarmActionPerformed

    private void ReadSmogAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadSmogAlarmActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        
        ReadSmogAlarmState cmd = new ReadSmogAlarmState(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadSmogAlarmState_Result _result = (ReadSmogAlarmState_Result) y;
            txtSmogAlarm.setText(String.valueOf(_result.SmogAlarm));
            x.append("????????????????????????????????????" + _result.SmogAlarm);
        });
    }//GEN-LAST:event_ReadSmogAlarmActionPerformed

    //??????????????????
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        ArrayList<WeekTimeGroup> _list = new ArrayList<WeekTimeGroup>();
        for (int i = 0; i < 64; i++) {
            WeekTimeGroup weekTimeGroup = new WeekTimeGroup(8);
            for (E_WeekDay e : E_WeekDay.values()) {
                DayTimeGroup dayTimeGroup = weekTimeGroup.GetItem(e);
                    //????????????
                for (int j = 0; j < 8; j++)
                {
                    TimeSegment segment = dayTimeGroup.GetItem(j);
                    segment.SetBeginTime(8, 0);
                    segment.SetEndTime(18, 0);
                }
            }
            _list.add(weekTimeGroup);
           
        }
        AddTimeGroup_Parameter par = new AddTimeGroup_Parameter(dt,_list);
        AddTimeGroup cmd = new AddTimeGroup(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            y = y;
            x.append("????????????");
        });
    }//GEN-LAST:event_jButton5ActionPerformed

    //??????????????????
    private void butReadTimeGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadTimeGroupActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }
        
        CommandParameter par = new CommandParameter(dt);
        Net.PC15.FC8800.Command.TimeGroup.ReadTimeGroup cmd = new Net.PC15.FC8800.Command.TimeGroup.ReadTimeGroup(par);
        _Allocator.AddCommand(cmd);
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            Net.PC15.FC8800.Command.TimeGroup.Result.ReadTimeGroup_Result _result = (Net.PC15.FC8800.Command.TimeGroup.Result.ReadTimeGroup_Result) y;
            x.append("?????????????????????????????????:" + _result.DataBaseSize);
        });
    }//GEN-LAST:event_butReadTimeGroupActionPerformed

    private void butReadFireAlarmStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadFireAlarmStateActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        ReadFireAlarmState cmd = new ReadFireAlarmState(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadFireAlarmState_Result r = (ReadFireAlarmState_Result) y;
            x.append("????????????,????????????:" + r.FireAlarm);
        });
    }//GEN-LAST:event_butReadFireAlarmStateActionPerformed

    //??????????????????????????????
    private void butSendFireAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butSendFireAlarmActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        SendFireAlarm cmd = new SendFireAlarm(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            x.append("????????????:");
        });
    }//GEN-LAST:event_butSendFireAlarmActionPerformed

    private void butReadWorkStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butReadWorkStatusActionPerformed
        // TODO add your handling code here:
        CommandDetail dt = getCommandDetail();
        if (dt == null) {
            return;
        }

        CommandParameter par = new CommandParameter(dt);
        ReadWorkStatus cmd = new ReadWorkStatus(par);
        _Allocator.AddCommand(cmd);
        
        AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
            ReadWorkStatus_Result r = (ReadWorkStatus_Result) y;
            x.append("????????????:" );
        });
    }//GEN-LAST:event_butReadWorkStatusActionPerformed

    private TCPDetail mReadTCPDetail;
    private void PirntTCPDetail(TCPDetail tcp, StringBuilder x) {
        x.append("\nMAC???");
        x.append(tcp.GetMAC());
        x.append("\n");
        x.append("IP???");
        x.append(tcp.GetIP());
        x.append(",???????????????");
        x.append(tcp.GetIPMask());
        x.append(",?????????");
        x.append(tcp.GetIPGateway());
        x.append("\n");
        x.append("DNS1???");
        x.append(tcp.GetDNS());
        x.append(",DNS2???");
        x.append(tcp.GetDNSBackup());
        x.append("\n");
        x.append("TCP?????????");
        x.append(tcp.GetTCPPort());
        x.append(",UDP?????????");
        x.append(tcp.GetUDPPort());
        x.append("\n");
        x.append("?????????IP???");
        x.append(tcp.GetServerIP());
        x.append(",?????????");
        x.append(tcp.GetServerPort());
        x.append("\n");
        x.append("??????????????????");
        x.append(tcp.GetServerAddr());
    }

    private void IniCardDataBase() {
        cmbCardDataBaseType.removeAllItems();
        cmbCardDataBaseType.addItem("???????????????");
        cmbCardDataBaseType.addItem("??????????????????");
        cmbCardDataBaseType.addItem("????????????");

        DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
        String Cols[] = "?????????????????????????????????????????????????????????????????????????????????1????????????1????????????2????????????2????????????3????????????3????????????4????????????4????????????????????????".split("???");
        //tblCard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCard.setColumnSelectionAllowed(true);

        int ColWidth[] = new int[]{40, 70, 60, 110, 60, 60, 70, 55, 55, 55, 55, 55, 55, 55, 55, 70};
        for (String Col : Cols) {
            tableModel.addColumn(Col);
        }
        for (int i = 0; i < ColWidth.length; i++) {
            TableColumn Column = tblCard.getColumnModel().getColumn(i);
            Column.setMinWidth(ColWidth[i]);
            Column.setMaxWidth(150);
            Column.setPreferredWidth(ColWidth[i]);

        }
        // ??????table????????????
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ??????table????????????
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tblCard.setDefaultRenderer(Object.class, tcr);

        // ??????table????????????
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tblCard.getTableHeader().getDefaultRenderer();
        hr.setHorizontalAlignment(SwingConstants.CENTER);

        //
        tblCard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCard.getTableHeader().setReorderingAllowed(false);   //??????????????????    
        //tblCard.getTableHeader().setResizingAllowed(false);   //??????????????????

        //tableModel.addRow(new Object[]{1, "0000000000", "00000000", "2017-11-14 12:30", "?????????", "?????????", "???????????????", "???", "64", "???", "64", "???", "64", "???", "64", "???"});
        tblCard.invalidate();

        tblCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblCard_mouseClicked();
            }
        });

    }

    private void tblCard_mouseClicked() {
        int numRow = tblCard.getSelectedRow();

        javax.swing.table.TableModel model = tblCard.getModel();

        String Card = model.getValueAt(numRow, 1).toString();
        Net.PC15.FC89H.Command.Data.CardDetail cd = new Net.PC15.FC89H.Command.Data.CardDetail();
        try 
        {
        cd.SetCardDataHEX(Card);
        }
        catch (Exception e){
            return;
        }
        int Index = mCardList.indexOf(cd);
        if (Index == -1) {
            return;
        }
        cd = mCardList.get(Index);
        //?????????????????????????????????????????????????????????????????????????????????1????????????1????????????2????????????2????????????3????????????3????????????4????????????4????????????????????????
        txtCardData.setText(model.getValueAt(numRow, 1).toString());

        txtCardPassword.setText(model.getValueAt(numRow, 2).toString());
        TxtCardExpiry.setText(model.getValueAt(numRow, 3).toString());
        txtOpenTimes.setText(model.getValueAt(numRow, 5).toString());
        cmbCardStatus.setSelectedIndex(cd.CardStatus);
        JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
        for (int i = 1; i <= 4; i++) {
            doors[i - 1].setSelected(cd.GetDoor(i));

        }

    }


    private String WriteFile(String sFileName, StringBuilder strBuf, boolean append) {
        File[] roots = File.listRoots();
        String Path = "d:\\";
        for (int i = 1; i < roots.length; i++) {
            if (roots[i].getFreeSpace() > 0) {
                Path = roots[i].getPath();
                break;
            }
        }

        Path += sFileName + ".txt";

        try {
            File file = new File(Path);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                if (!append) {
                    file.delete();
                    file.createNewFile();
                }
            }
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(strBuf.toString().getBytes("utf-8"));
            out.close();
        } catch (Exception e) {
            return null;
        }

        return Path;
    }

    private String GetBooleanStr(boolean v) {
        if (v) {
            return "???";
        } else {
            return "???";
        }
    }

    private ArrayList<Net.PC15.FC89H.Command.Data.CardDetail> mCardList;
    private void ClearCardList() {
        DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
        tableModel.setRowCount(0);// ???????????????
    }

    private void FillCardToList() {
        if (mCardList == null) {
            return;
        }
        if (mCardList.size() == 0) {
            return;
        }

        DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
        ClearCardList();

        int Index = 1;

        for (Net.PC15.FC89H.Command.Data.CardDetail cd : mCardList) {

            Object[] row = CardDetailToRow(cd, Index);
            // ?????????????????????
            tableModel.addRow(row);
            Index += 1;
        }
        // ????????????
        tblCard.invalidate();

    }
    String CardStatusList[] = new String[]{"??????", "?????????", "?????????"};
    String PrivilegeList[] = new String[]{"???", "???????????????", "???????????????", "?????????", "???????????????"};
    String OpenTimesUnlimited = "?????????";

    private Object[] CardDetailToRow(Net.PC15.FC89H.Command.Data.CardDetail cd, int Index) {
        String OpenTimes;//?????????
        String Privilege = PrivilegeList[0];// "???";//??????
        if (cd.OpenTimes == 65535) {
            OpenTimes = OpenTimesUnlimited;//"?????????";
        } else {
            OpenTimes = String.valueOf(cd.OpenTimes);
        }

        if (cd.IsPrivilege()) {
            Privilege = PrivilegeList[1];//"???????????????";
        } else if (cd.IsTiming()) {
            Privilege = PrivilegeList[2];//"???????????????";
        } else if (cd.IsGuardTour()) {
            Privilege = PrivilegeList[3];// "?????????";
        } else if (cd.IsAlarmSetting()) {
            Privilege = PrivilegeList[4];// "???????????????";
        }
        Object[] row = new Object[]{Index,
            cd.GetCardData(),
            cd.Password.replaceAll("F", ""),
            TimeUtil.FormatTimeHHmm(cd.Expiry),
            CardStatusList[cd.CardStatus],
            OpenTimes,
            Privilege,
            GetBooleanStr(cd.GetDoor(1)), cd.GetTimeGroup(1),
            GetBooleanStr(cd.GetDoor(2)), cd.GetTimeGroup(2),
            GetBooleanStr(cd.GetDoor(3)), cd.GetTimeGroup(3),
            GetBooleanStr(cd.GetDoor(4)), cd.GetTimeGroup(4),
            GetBooleanStr(cd.HolidayUse)};

        return row;
    }

    private Net.PC15.FC89H.Command.Data.CardDetail GetCardDetail() {
        String Card = txtCardData.getText();
        String Password = txtCardPassword.getText();
        String Expiry = TxtCardExpiry.getText();
        String OpenTimes = txtOpenTimes.getText();
        int CardStatus = cmbCardStatus.getSelectedIndex();
        int iOpenTimes = 0;
        Calendar CardExpiry = Calendar.getInstance();

        if (StringUtil.IsNullOrEmpty(Card)) {
            JOptionPane.showMessageDialog(null, "?????????????????????", "????????????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        
        if (!StringUtil.IsNullOrEmpty(Password)) {
            if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
                JOptionPane.showMessageDialog(null, "?????????4-8????????????", "????????????", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(Expiry);
            CardExpiry.setTime(dt1);
            dt1 = CardExpiry.getTime();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????yyyy-MM-dd hh:mm??????", "????????????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (OpenTimes.equals(OpenTimesUnlimited)) {
            OpenTimes = "65535";
        }
        if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????????????????0-65535???", "????????????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        iOpenTimes = Integer.valueOf(OpenTimes);
        if (iOpenTimes > 65535) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????????????????0-65535???", "????????????", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};

        Net.PC15.FC89H.Command.Data.CardDetail cd = new  Net.PC15.FC89H.Command.Data.CardDetail();//????????????
        try {
            cd.SetCardDataHEX(Card);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "????????????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        cd.Password = Password;//????????????
        cd.Expiry = CardExpiry;//???????????????
        cd.OpenTimes = iOpenTimes;//????????????
        cd.CardStatus = (byte) CardStatus;
        //??????4?????????????????????
        for (int i = 1; i <= 4; i++) {
            cd.SetTimeGroup(i, 1);//?????????????????????1???
            cd.SetDoor(i, doors[i - 1].isSelected());//?????????????????????
        }
        cd.SetNormal();//????????????????????????
        cd.HolidayUse = true;//???????????????????????????

        return cd;
    }


    /**
     * ??????????????????
     *
     * @param dt
     * @param buf
     */
    private void PrintTransactionDatabaseDetail(String TransactionName, TransactionDetail dt, StringBuilder buf) {
        buf.append("\n");
        buf.append(TransactionName);
        buf.append("????????????");
        buf.append(dt.DataBaseMaxSize);
        buf.append("??????????????????");
        buf.append(dt.readable());
        buf.append("???????????????");
        buf.append(dt.WriteIndex);
        buf.append("???????????????");
        buf.append(dt.ReadIndex);
        buf.append("??????????????????");
        buf.append(dt.IsCircle);

    }

    /**
     * ??????????????????
     *
     * @param TransactionList
     * @param log
     */
    private void PrintTransactionDatabase(ArrayList<AbstractTransaction> TransactionList, StringBuilder log, String[] sTransactionList) {
        int Quantity = TransactionList.size();

        int code = 0;
        String sCode = null;

        for (int i = 0; i < Quantity; i++) {
            AbstractTransaction Transaction = TransactionList.get(i);

            log.append("?????????");
            log.append(Transaction.SerialNumber);

            if (!Transaction.IsNull()) {
                code = Transaction.TransactionCode();
                sCode = null;
                log.append("????????????");
                log.append(TimeUtil.FormatTime(Transaction.TransactionDate()));

                log.append("????????????");//??????
                log.append(code);
                if (code < 255) {
                    sCode = sTransactionList[code];
                    if (StringUtil.IsNullOrEmpty(sCode)) {
                        sCode = "????????????";
                    }
                } else {
                    sCode = "????????????";
                }

                log.append("--");
                log.append(sCode);
                if (Transaction instanceof AbstractDoorTransaction) {
                    AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
                    if (dt.Door != 255) {
                        log.append("????????????");
                        log.append(dt.Door);
                    }
                }

                if (Transaction instanceof Net.PC15.FC89H.Command.Data.CardTransaction) {
                    Net.PC15.FC89H.Command.Data.CardTransaction cardTrn = (Net.PC15.FC89H.Command.Data.CardTransaction) Transaction;
                    log.append("????????????");
                    log.append(cardTrn.CardDataStr);
                    log.append("????????????");
                    log.append(cardTrn.DoorNum());
                    if (cardTrn.IsEnter()) {
                        log.append("???????????????");
                    } else {
                        log.append("???????????????");
                    }
                }
            }

            log.append("\n");
        }
    }

    private int SearchTimes = 0;
    private int SearchNetFlag;

    private void PrintSearchDoor(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst, StringBuilder log) {
        ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
        TCPDetail td = new TCPDetail();
        for (SearchEquptOnNetNum_Result.SearchResult r : lst) {

            log.append("SN???");
            log.append(r.SN);
            if (r.ResultData != null) {
                log.append("??????????????????");
                log.append(ByteUtil.ByteToHex(r.ResultData));
                if (r.ResultData.length == 0x89) {
                    tmpBuf.readerIndex(0);
                    tmpBuf.writerIndex(0);
                    tmpBuf.writeBytes(r.ResultData);
                    td.SetBytes(tmpBuf);;
                    PirntTCPDetail(td, log);

                }
            }
            log.append("\n");

        }
    }

    /**
     * ??????????????????????????????
     *
     * @param lst
     * @param log
     */
    private void SetDoorNetFlag(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst) {
        //ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
        //TCPDetail td = new TCPDetail();
        for (SearchEquptOnNetNum_Result.SearchResult r : lst) {

            /*if (r.ResultData != null) {

                if (r.ResultData.length == 0x89) {
                    tmpBuf.readerIndex(0);
                    tmpBuf.writerIndex(0);
                    tmpBuf.writeBytes(r.ResultData);
                    td.SetBytes(tmpBuf);

                    

                    
                }
            }*/
            CommandDetail detail = new CommandDetail();
            UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
            udp.Broadcast = true;
            detail.Connector = udp;
            
            detail.Identity = new FC8800Identity(r.SN, FC8800Command.NULLPassword, E_ControllerType.FC8900);
           
            SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(detail, SearchNetFlag);
            WriteEquptNetNum cmd = new WriteEquptNetNum(par);
            _Allocator.AddCommand(cmd);

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButReadDeadline;
    private javax.swing.JButton ButWriteDeadline;
    private javax.swing.JLabel LblDate;
    private javax.swing.JPanel PnlUDP;
    private javax.swing.JRadioButton RadTCPClient;
    private javax.swing.JRadioButton RadTCPServer;
    private javax.swing.JRadioButton RadUDP;
    private javax.swing.JButton ReadOpenAlarm;
    private javax.swing.JButton ReadSmogAlarm;
    private javax.swing.JTextField TxtCardExpiry;
    private javax.swing.JButton WriteOpenAlarm;
    private javax.swing.JButton WriteSmogAlarm;
    private javax.swing.ButtonGroup bgConnectType;
    private javax.swing.JButton butAddCardToList;
    private javax.swing.JButton butAutoSearchDoor;
    private javax.swing.JButton butBeginServer;
    private javax.swing.JButton butBeginWatch;
    private javax.swing.JButton butCardListAutoCreate;
    private javax.swing.JButton butCardListClear;
    private javax.swing.JButton butClearCardDataBase;
    private javax.swing.JButton butClearTimeGroup;
    private javax.swing.JButton butClearTransactionDatabase;
    private javax.swing.JButton butCloseAlarm;
    private javax.swing.JButton butCloseDoor;
    private javax.swing.JButton butCloseWatch;
    private javax.swing.JButton butDeleteCard;
    private javax.swing.JButton butDeleteCardByList;
    private javax.swing.JButton butHoldDoor;
    private javax.swing.JButton butLockDoor;
    private javax.swing.JButton butOpenDoor;
    private javax.swing.JButton butReadCardDataBase;
    private javax.swing.JButton butReadCardDatabaseDetail;
    private javax.swing.JButton butReadCardDetail;
    private javax.swing.JButton butReadConnectPassword;
    private javax.swing.JButton butReadFireAlarmState;
    private javax.swing.JButton butReadSN;
    private javax.swing.JButton butReadTCPSetting;
    private javax.swing.JButton butReadTimeGroup;
    private javax.swing.JButton butReadTransactionDatabase;
    private javax.swing.JButton butReadTransactionDatabaseByIndex;
    private javax.swing.JButton butReadTransactionDatabaseDetail;
    private javax.swing.JButton butReadVersion;
    private javax.swing.JButton butResetConnectPassword;
    private javax.swing.JButton butSendFireAlarm;
    private javax.swing.JButton butTransactionDatabaseEmpty;
    private javax.swing.JButton butUnlockDoor;
    private javax.swing.JButton butUploadCard;
    private javax.swing.JButton butWriteCardListBySequence;
    private javax.swing.JButton butWriteCardListBySort;
    private javax.swing.JButton butWriteConnectPassword;
    private javax.swing.JButton butWriteSN;
    private javax.swing.JButton butWriteTCPSetting;
    private javax.swing.JButton butWriteTransactionDatabaseReadIndex;
    private javax.swing.JButton butWriteTransactionDatabaseWriteIndex;
    private javax.swing.JCheckBox chkCardDoor1;
    private javax.swing.JCheckBox chkCardDoor2;
    private javax.swing.JCheckBox chkCardDoor3;
    private javax.swing.JCheckBox chkCardDoor4;
    private javax.swing.JCheckBox chkTransactionIsCircle;
    private javax.swing.JComboBox<String> cmbCardDataBaseType;
    private javax.swing.JComboBox<String> cmbCardStatus;
    private javax.swing.JComboBox<String> cmbTransactionType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabSetting;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jpConnectSetting;
    private javax.swing.JScrollPane jpLog;
    private javax.swing.JPanel jpSNDetail;
    private javax.swing.JLabel lblCommandName;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlTCPClient;
    private javax.swing.JPanel pnlTCPServer;
    private javax.swing.JProgressBar prCommand;
    private javax.swing.JTable tblCard;
    private javax.swing.JTextField txtCardAutoCreateSzie;
    private javax.swing.JTextField txtCardData;
    private javax.swing.JTextField txtCardPassword;
    private javax.swing.JTextField txtLocalPort;
    private javax.swing.JTextArea txtLog;
    private javax.swing.JTextField txtOpenAlarm;
    private javax.swing.JTextField txtOpenTimes;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtReadTransactionDatabaseByIndex;
    private javax.swing.JTextField txtReadTransactionDatabaseByQuantity;
    private javax.swing.JTextField txtReadTransactionDatabasePacketSize;
    private javax.swing.JTextField txtReadTransactionDatabaseQuantity;
    private javax.swing.JTextField txtSN;
    private javax.swing.JTextField txtSmogAlarm;
    private javax.swing.JTextField txtTCPServerIP;
    private javax.swing.JTextField txtTCPServerPort;
    private javax.swing.JTextField txtTransactionDatabaseReadIndex;
    private javax.swing.JTextField txtTransactionDatabaseWriteIndex;
    private javax.swing.JTextField txtUDPRemoteIP;
    private javax.swing.JTextField txtUDPRemotePort;
    private javax.swing.JTextField txtWriteSN;
    // End of variables declaration//GEN-END:variables

    private CommandDetail getCommandDetail() {
        CommandDetail detail = new CommandDetail();

        String ip = "0", strPort = "0";

        if (RadTCPClient.isSelected()) {
            ip = txtTCPServerIP.getText();
            strPort = txtLocalPort.getText();
        }
        if (RadUDP.isSelected()) {
            ip = txtUDPRemoteIP.getText();
            strPort = txtUDPRemotePort.getText();
        }

        int iPort = Integer.valueOf(strPort);

        if (ip.length() == 0) {
            JOptionPane.showMessageDialog(null, "????????????IP?????????", "??????", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (iPort <= 0 || iPort > 65535) {
            JOptionPane.showMessageDialog(null, "TCP?????????????????????1-65535???", "??????", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        detail.Connector = new TCPClientDetail(ip, iPort);

        if (RadUDP.isSelected()) {
            UDPDetail udpd = new UDPDetail(ip, iPort);
            udpd.Broadcast = true;
            //udpd.LocalPort = 10088;//???????????????????????????

            detail.Connector = udpd;
        }

        String sn, pwd;
        sn = txtSN.getText();

        if (sn.length() != 16) {
            JOptionPane.showMessageDialog(null, "SN?????????16??????", "??????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        pwd = txtPassword.getText();

        if (pwd.length() != 8) {
            JOptionPane.showMessageDialog(null, "?????????????????????8??????", "??????", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        detail.Identity = new FC8800Identity(sn, pwd, E_ControllerType.FC8900);
        
        return detail;
    }
    private void AddCommandResultCallback(String sCommand, CommandResultCallback callback) {
        if (!CommandResult.containsKey(sCommand)) {
            CommandResult.put(sCommand, callback);
        }
        /*else {
            CommandResult.remove(sCommand);
            CommandResult.put(sCommand, callback);
        }*/
    }

    private void IniCommandName() {
        if (CommandName.size() > 0) {
            return;
        }

        CommandName.put(WriteSN.class.getName(), "????????????SN");
        CommandName.put(WriteSN_Broadcast.class.getName(), "???????????????SN");
        CommandName.put(ReadSN.class.getName(), "?????????????????????SN");
        CommandName.put(WriteConnectPassword.class.getName(), "???????????????????????????");
        CommandName.put(ReadConnectPassword.class.getName(), "?????????????????????????????????");
        CommandName.put(ResetConnectPassword.class.getName(), "???????????????????????????");
        CommandName.put(ReadTCPSetting.class.getName(), "???????????????TCP????????????");
        CommandName.put(WriteTCPSetting.class.getName(), "???????????????TCP????????????");
        CommandName.put(ReadDeadline.class.getName(), "????????????????????????????????????");
        CommandName.put(WriteDeadline.class.getName(), "????????????????????????");
        CommandName.put(ReadVersion.class.getName(), "????????????????????????");
        CommandName.put(ReadSystemStatus.class.getName(), "????????????????????????");
        CommandName.put(ReadAllSystemSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadRecordMode.class.getName(), "????????????????????????");
        CommandName.put(WriteRecordMode.class.getName(), "????????????????????????");
        CommandName.put(ReadKeyboard.class.getName(), "?????????????????????????????????????????????");
        CommandName.put(WriteKeyboard.class.getName(), "?????????????????????????????????????????????");
        CommandName.put(ReadLockInteraction.class.getName(), "????????????????????????");
        CommandName.put(WriteLockInteraction.class.getName(), "????????????????????????");
        CommandName.put(ReadFireAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(WriteFireAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(ReadOpenAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(WriteOpenAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(ReadReaderIntervalTime.class.getName(), "??????????????????????????????");
        CommandName.put(WriteReaderIntervalTime.class.getName(), "??????????????????????????????");
        CommandName.put(ReadBroadcast.class.getName(), "???????????????????????????");
        CommandName.put(WriteBroadcast.class.getName(), "???????????????????????????");
        CommandName.put(ReadReaderCheckMode.class.getName(), "???????????????????????????");
        CommandName.put(WriteReaderCheckMode.class.getName(), "???????????????????????????");
        CommandName.put(ReadBuzzer.class.getName(), "???????????????????????????");
        CommandName.put(WriteBuzzer.class.getName(), "???????????????????????????");
        CommandName.put(ReadSmogAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(WriteSmogAlarmOption.class.getName(), "??????????????????????????????");
        CommandName.put(ReadEnterDoorLimit.class.getName(), "??????????????????????????????");
        CommandName.put(WriteEnterDoorLimit.class.getName(), "??????????????????????????????");
        CommandName.put(ReadTheftAlarmSetting.class.getName(), "??????????????????????????????");
        CommandName.put(WriteTheftAlarmSetting.class.getName(), "??????????????????????????????");
        CommandName.put(ReadCheckInOut.class.getName(), "?????????????????????");
        CommandName.put(WriteCheckInOut.class.getName(), "?????????????????????");
        CommandName.put(ReadCardPeriodSpeak.class.getName(), "??????????????????????????????");
        CommandName.put(WriteCardPeriodSpeak.class.getName(), "??????????????????????????????");
        CommandName.put(ReadReadCardSpeak.class.getName(), "????????????????????????????????????");
        CommandName.put(WriteReadCardSpeak.class.getName(), "????????????????????????????????????");
        CommandName.put(BeginWatch.class.getName(), "??????????????????");
        CommandName.put(BeginWatch_Broadcast.class.getName(), "??????????????????");
        CommandName.put(CloseWatch.class.getName(), "??????????????????");
        CommandName.put(CloseWatch_Broadcast.class.getName(), "??????????????????");
        CommandName.put(SendFireAlarm.class.getName(), "??????????????????????????????");
        CommandName.put(CloseFireAlarm.class.getName(), "??????????????????");
        CommandName.put(ReadFireAlarmState.class.getName(), "????????????????????????");
        CommandName.put(SendSmogAlarm.class.getName(), "??????????????????????????????");
        CommandName.put(CloseSmogAlarm.class.getName(), "??????????????????");
        CommandName.put(ReadSmogAlarmState.class.getName(), "????????????????????????");
        CommandName.put(CloseAlarm.class.getName(), "????????????");
        CommandName.put(ReadWorkStatus.class.getName(), "??????????????????????????????????????????");
        CommandName.put(ReadTheftAlarmState.class.getName(), "??????????????????????????????");
        CommandName.put(FormatController.class.getName(), "??????????????????");
        CommandName.put(SearchEquptOnNetNum.class.getName(), "???????????????");
        CommandName.put(WriteEquptNetNum.class.getName(), "??????SN??????????????????");
        CommandName.put(WriteKeepAliveInterval.class.getName(), "????????????????????????");
        CommandName.put(ReadKeepAliveInterval.class.getName(), "????????????????????????");
        CommandName.put(SetTheftDisarming.class.getName(), "??????????????????");
        CommandName.put(SetTheftFortify.class.getName(), "??????????????????");
        CommandName.put(WriteBalcklistAlarmOption.class.getName(), "?????????????????????????????????");
        CommandName.put(ReadBalcklistAlarmOption.class.getName(), "?????????????????????????????????");
        CommandName.put(ReadExploreLockMode.class.getName(), "???????????????????????????");
        CommandName.put(WriteExploreLockMode.class.getName(), "???????????????????????????");
        CommandName.put(ReadCheck485Line.class.getName(), "??????485????????????????????????");
        CommandName.put(WriteCheck485Line.class.getName(), "??????485????????????????????????");
        CommandName.put(ReadCardDeadlineTipDay.class.getName(), "???????????????????????????????????????");
        CommandName.put(WriteCardDeadlineTipDay.class.getName(), "???????????????????????????????????????");

        CommandName.put(ReadTime.class.getName(), "????????????????????????????????????");
        CommandName.put(WriteTime.class.getName(), "?????????????????????????????????????????????");
        CommandName.put(WriteTimeBroadcast.class.getName(), "????????????");
        CommandName.put(WriteTimeDefine.class.getName(), "?????????????????????????????????");

        CommandName.put(WriteReaderOption.class.getName(), "???????????????4???????????????????????????");
        CommandName.put(ReadReaderOption.class.getName(), "???????????????4???????????????????????????");
        CommandName.put(ReadRelayOption.class.getName(), "??????????????????????????????");
        CommandName.put(WriteRelayOption.class.getName(), "??????????????????????????????");
        CommandName.put(OpenDoor.class.getName(), "??????????????????");
        CommandName.put(CloseDoor.class.getName(), "??????????????????");
        CommandName.put(HoldDoor.class.getName(), "?????????????????????");
        CommandName.put(LockDoor.class.getName(), "???????????????");
        CommandName.put(UnlockDoor.class.getName(), "???????????????????????????");
        CommandName.put(ReadReaderWorkSetting.class.getName(), "???????????????????????????????????????");
        CommandName.put(WriteReaderWorkSetting.class.getName(), "???????????????????????????????????????");
        CommandName.put(ReadDoorWorkSetting.class.getName(), "????????????????????????");
        CommandName.put(WriteDoorWorkSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadAutoLockedSetting.class.getName(), "?????????????????????");
        CommandName.put(WriteAutoLockedSetting.class.getName(), "?????????????????????");
        CommandName.put(ReadRelayReleaseTime.class.getName(), "????????????????????????");
        CommandName.put(WriteRelayReleaseTime.class.getName(), "????????????????????????");
        CommandName.put(ReadReaderInterval.class.getName(), "????????????????????????");
        CommandName.put(WriteReaderInterval.class.getName(), "????????????????????????");
        CommandName.put(ReadInvalidCardAlarmOption.class.getName(), "???????????????????????????????????????");
        CommandName.put(WriteInvalidCardAlarmOption.class.getName(), "???????????????????????????????????????");
        CommandName.put(ReadAlarmPassword.class.getName(), "????????????????????????");
        CommandName.put(WriteAlarmPassword.class.getName(), "????????????????????????");
        CommandName.put(ReadAntiPassback.class.getName(), "???????????????");
        CommandName.put(WriteAntiPassback.class.getName(), "???????????????");
        CommandName.put(WriteOvertimeAlarmSetting.class.getName(), "??????????????????????????????");
        CommandName.put(ReadOvertimeAlarmSetting.class.getName(), "??????????????????????????????");
        CommandName.put(WritePushButtonSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadPushButtonSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadSensorAlarmSetting.class.getName(), "????????????????????????");
        CommandName.put(WriteSensorAlarmSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadAnyCardSetting.class.getName(), "????????????????????????");
        CommandName.put(WriteAnyCardSetting.class.getName(), "????????????????????????");
        CommandName.put(ReadReadingBroadcast.class.getName(), "");
        CommandName.put(WriteReadingBroadcast.class.getName(), "");

        CommandName.put(ReadCardDatabaseDetail.class.getName(), "??????????????????????????????????????????");
        CommandName.put(ClearCardDataBase.class.getName(), "?????????????????????");
        CommandName.put(ReadCardDataBase.class.getName(), "?????????????????????????????????");
        CommandName.put(ReadCardDetail.class.getName(), "??????????????????????????????????????????");
        CommandName.put(WriteCardListBySequence.class.getName(), "?????????????????????????????????????????????");
        CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySequence.class.getName(), "?????????????????????????????????????????????");
        CommandName.put(WriteCardListBySort.class.getName(), "??????????????????????????????????????????");
        CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySort.class.getName(), "??????????????????????????????????????????");
        CommandName.put(DeleteCard.class.getName(), "????????????");
        CommandName.put(Net.PC15.FC89H.Command.Card.DeleteCard.class.getName(), "????????????");

        CommandName.put(ReadTransactionDatabaseDetail.class.getName(), "??????????????????????????????????????????");
        CommandName.put(TransactionDatabaseEmpty.class.getName(), "????????????????????????????????????");
        CommandName.put(ClearTransactionDatabase.class.getName(), "????????????????????????????????????");
        CommandName.put(WriteTransactionDatabaseReadIndex.class.getName(), "???????????????????????????????????????");
        CommandName.put(WriteTransactionDatabaseWriteIndex.class.getName(), "???????????????????????????????????????");
        CommandName.put(ReadTransactionDatabaseByIndex.class.getName(), "????????????");
        CommandName.put(ReadTransactionDatabase.class.getName(), "???????????????");
        CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabaseByIndex.class.getName(), "????????????");
        CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabase.class.getName(), "???????????????");
    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        try {
            StringBuilder strBuf = new StringBuilder(100);

            GetCommandDetail(strBuf, cmd);
            String sKey = cmd.getClass().getName();
            if (CommandResult.containsKey(sKey)) {

                strBuf.append("??????????????????????????????????????????");
                CommandResult.get(sKey).ResultToLog(strBuf, result);
            } else {
                strBuf.append("??????????????????!");
            }
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.CommandCompleteEvent() -- ???????????????" + e.toString());
        }

    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        try {

            //lblCommandName.setText("???????????????" + GetCommandName(cmd) + "\n????????????" );
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("???????????????");
            strBuf.append(GetCommandName(cmd));
            strBuf.append("<br/>??????????????? ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
            lblCommandName.setText(strBuf.toString());

            if (prCommand.getMaximum() != cmd.getProcessMax()) {
                //prCommand.setValue(0);
                prCommand.setMaximum(cmd.getProcessMax());
            }
            prCommand.setValue(cmd.getProcessStep());

            //AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.CommandProcessEvent() -- ???????????????" + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            GetCommandDetail(strBuf, cmd);
            if (isStop) {
                strBuf.append("?????????????????????!");
            } else {
                strBuf.append("??????????????????!");
            }
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ConnectorErrorEvent() --- " + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("?????????????????????IP?????????");
            GetConnectorDetail(strBuf, detail);
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ConnectorErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        try {
            if (cmd instanceof SearchEquptOnNetNum) {
                CommandCompleteEvent(cmd, cmd.getCommandResult());
                return;
            }
            StringBuilder strBuf = new StringBuilder(100);
            GetCommandDetail(strBuf, cmd);
            strBuf.append("???????????????????????????");
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.CommandTimeout() -- " + e.toString());
        }

    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            GetCommandDetail(strBuf, cmd);
            strBuf.append("?????????????????????????????????");
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.PasswordErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            GetCommandDetail(strBuf, cmd);
            strBuf.append("?????????????????????????????????????????????");
            AddLog(strBuf.toString());
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ChecksumErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("????????????:");
            GetConnectorDetail(strBuf, detail);

            if (event instanceof FC8800WatchTransaction) {
                FC8800WatchTransaction WatchTransaction = (FC8800WatchTransaction) event;
                strBuf.append("???SN???");
                strBuf.append(WatchTransaction.SN);
                strBuf.append("\n");
                PrintWatchEvent(WatchTransaction, strBuf);
            } else {
                strBuf.append("??????????????????");
                strBuf.append(event.getClass().getName());
            }
            AddLog(strBuf.toString());
            strBuf = null;

        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.WatchEvent() -- " + e.toString());
        }

    }

    private String[] mWatchTypeNameList;
    private String[] mCardTransactionList, mButtonTransactionList, mDoorSensorTransactionList, mSoftwareTransactionList, mAlarmTransactionList, mSystemTransactionList;

    /**
     * ??????????????????????????????????????????
     */
    private void IniWatchEvent() {
        mWatchTypeNameList = new String[]{"", "????????????", "??????????????????", "????????????", "??????????????????", "????????????", "????????????", "??????????????????", "??????????????????"};
        mCardTransactionList = new String[256];
        mButtonTransactionList = new String[256];
        mDoorSensorTransactionList = new String[256];
        mSoftwareTransactionList = new String[256];
        mAlarmTransactionList = new String[256];
        mSystemTransactionList = new String[256];

        mCardTransactionList[1] = "????????????";//
        mCardTransactionList[2] = "????????????";//------------???????????????
        mCardTransactionList[3] = "????????????";//
        mCardTransactionList[4] = "????????????????????????";//
        mCardTransactionList[5] = "????????????";//
        mCardTransactionList[6] = "?????????";//   ---  ????????????????????????????????????????????????
        mCardTransactionList[7] = "????????????";//  --  ?????????????????????????????????
        mCardTransactionList[8] = "????????????";//
        mCardTransactionList[9] = "???????????????";//
        mCardTransactionList[10] = "??????????????????";//
        mCardTransactionList[11] = "???????????????";//
        mCardTransactionList[12] = "????????????";//
        mCardTransactionList[13] = "?????????";//  --  ?????????
        mCardTransactionList[14] = "????????????";//
        mCardTransactionList[15] = "???????????????";//
        mCardTransactionList[16] = "?????????";//
        mCardTransactionList[17] = "????????????";//------------?????????????????????
        mCardTransactionList[18] = "??????????????????????????????";//----??????????????????
        mCardTransactionList[19] = "?????????(??????)???(???????????????)??????";//
        mCardTransactionList[20] = "?????????(????????????)";//
        mCardTransactionList[21] = "???????????????";//
        mCardTransactionList[22] = "?????????";//
        mCardTransactionList[23] = "????????????";//
        mCardTransactionList[24] = "????????????????????????????????????";//
        mCardTransactionList[25] = "????????????????????????(?????????)";//
        mCardTransactionList[26] = "????????????????????????(?????????)";//
        mCardTransactionList[27] = "????????????????????????(??????)";//
        mCardTransactionList[28] = "????????????????????????(??????)";//
        mCardTransactionList[29] = "?????????(??????)???(???????????????)??????";//
        mCardTransactionList[30] = "?????????(????????????)";//
        mCardTransactionList[31] = "????????????";//
        mCardTransactionList[32] = "????????????--???????????????";//
        mCardTransactionList[33] = "????????????--????????????";//
        mCardTransactionList[34] = "???????????????????????????";//
        mCardTransactionList[35] = "???????????????????????????";//
        mCardTransactionList[36] = "??????????????????";//  --  ???????????????????????????????????????????????????????????????
        mCardTransactionList[37] = "??????????????????";//  --  ???????????????????????????????????????????????????????????????
        mCardTransactionList[38] = "???????????????????????????????????????";//???????????????????????????
        mCardTransactionList[39] = "???????????????????????????????????????";//???????????????????????????
        mCardTransactionList[40] = "???????????????";//(?????????????????????????????????)(?????????)
        mCardTransactionList[41] = "???????????????";//(?????????????????????????????????)(?????????)
        mCardTransactionList[42] = "???????????????????????????????????????";//
        mCardTransactionList[43] = "??????????????????_??????";//
        mCardTransactionList[44] = "??????????????????_??????";//
        mCardTransactionList[45] = "???????????????????????????????????????";//
        mCardTransactionList[46] = "????????????--????????????????????????????????????";//
        mCardTransactionList[47] = "????????????--?????????????????????????????????";//
        mCardTransactionList[48] = "???????????????--????????????";//
        mCardTransactionList[49] = "????????????--??????????????????????????????";//

        mButtonTransactionList[1] = "????????????";//
        mButtonTransactionList[2] = "??????????????????";//
        mButtonTransactionList[3] = "???????????????";//
        mButtonTransactionList[4] = "??????????????????";//
        mButtonTransactionList[5] = "???????????????(?????????)";//

        mDoorSensorTransactionList[1] = "??????";//
        mDoorSensorTransactionList[2] = "??????";//
        mDoorSensorTransactionList[3] = "????????????????????????";//
        mDoorSensorTransactionList[4] = "????????????????????????";//
        mDoorSensorTransactionList[5] = "????????????";//

        mSoftwareTransactionList[1] = "????????????";//
        mSoftwareTransactionList[2] = "????????????";//
        mSoftwareTransactionList[3] = "????????????";//
        mSoftwareTransactionList[4] = "???????????????????????????";//
        mSoftwareTransactionList[5] = "????????????????????????";//
        mSoftwareTransactionList[6] = "????????????????????????";//
        mSoftwareTransactionList[7] = "????????????????????????";//
        mSoftwareTransactionList[8] = "????????????";//
        mSoftwareTransactionList[9] = "??????????????????";//
        mSoftwareTransactionList[10] = "?????????????????????";//--?????????????????????
        mSoftwareTransactionList[11] = "???????????????????????????";//--???????????????????????????
        mSoftwareTransactionList[12] = "??????--??????";//
        mSoftwareTransactionList[13] = "??????--????????????";//
        mSoftwareTransactionList[14] = "?????????????????????";//

        mAlarmTransactionList[1] = "????????????";//
        mAlarmTransactionList[2] = "????????????";//
        mAlarmTransactionList[3] = "????????????";//
        mAlarmTransactionList[4] = "??????????????????";//
        mAlarmTransactionList[5] = "????????????";//
        mAlarmTransactionList[6] = "????????????(????????????)";//
        mAlarmTransactionList[7] = "????????????";//
        mAlarmTransactionList[8] = "????????????";//
        mAlarmTransactionList[9] = "???????????????";//
        mAlarmTransactionList[10] = "??????????????????";//
        mAlarmTransactionList[0x11] = "??????????????????";//
        mAlarmTransactionList[0x12] = "??????????????????";//
        mAlarmTransactionList[0x13] = "??????????????????";//
        mAlarmTransactionList[0x14] = "????????????????????????";//
        mAlarmTransactionList[0x15] = "??????????????????";//
        mAlarmTransactionList[0x17] = "??????????????????";//
        mAlarmTransactionList[0x18] = "??????????????????";//
        mAlarmTransactionList[0x19] = "?????????????????????";//
        mAlarmTransactionList[0x1A] = "????????????????????????";//
        mAlarmTransactionList[0x21] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x22] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x23] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x24] = "????????????????????????(????????????)";//
        mAlarmTransactionList[0x25] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x27] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x28] = "??????????????????(????????????)";//
        mAlarmTransactionList[0x29] = "?????????????????????(????????????)";//
        mAlarmTransactionList[0x2A] = "????????????????????????";//

        mSystemTransactionList[1] = "????????????";//
        mSystemTransactionList[2] = "?????????????????????????????????";//
        mSystemTransactionList[3] = "?????????????????????";//
        mSystemTransactionList[4] = "?????????????????????????????????>75";//
        mSystemTransactionList[5] = "??????UPS????????????";//
        mSystemTransactionList[6] = "????????????????????????????????????>100";//
        mSystemTransactionList[7] = "?????????????????????<09V";//
        mSystemTransactionList[8] = "?????????????????????>14V";//
        mSystemTransactionList[9] = "??????????????????";//
        mSystemTransactionList[10] = "???????????????????????????";//
        mSystemTransactionList[11] = "????????????????????????";//
        mSystemTransactionList[12] = "???????????????????????????14V?????????9V";//
        mSystemTransactionList[13] = "???????????????";//
        mSystemTransactionList[14] = "???????????????";//
    }

    /**
     * ????????????????????????StringBuilder???
     *
     * @param Transaction
     * @param strBuf
     */
    private void logTransaction(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf, String[] sTransactionList) {
        AbstractTransaction Transaction = WatchTransaction.EventData;
        //??????????????????
        if (WatchTransaction.CmdIndex >= 1 && WatchTransaction.CmdIndex <= 6) {
            strBuf.append(mWatchTypeNameList[WatchTransaction.CmdIndex]);//??????
        } else if (WatchTransaction.CmdIndex == 0x23 && WatchTransaction.CmdIndex == 0x22) {
            int tmpType = WatchTransaction.CmdIndex - 27;

            strBuf.append(mWatchTypeNameList[tmpType]);//??????
        } else {
            strBuf.append("?????????????????????");//??????
            strBuf.append(WatchTransaction.CmdIndex);
        }
        strBuf.append("??????????????????");
        strBuf.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
        if (sTransactionList == null) {
            return;
        }

        int code = Transaction.TransactionCode();
        String sCode = null;

        strBuf.append("\n???????????????");//??????
        strBuf.append(code);
        if (code < 255) {
            sCode = sTransactionList[code];
            if (StringUtil.IsNullOrEmpty(sCode)) {
                sCode = "????????????";
            }
        } else {
            sCode = "????????????";
        }
        strBuf.append("--");
        strBuf.append(sCode);
        if (Transaction instanceof AbstractDoorTransaction) {
            AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
            if (dt.Door != 255) {
                strBuf.append("????????????");
                strBuf.append(dt.Door);
            }

        }

        if (Transaction instanceof CardTransaction) {
            Net.PC15.FC89H.Command.Data.CardTransaction cardTrn = (Net.PC15.FC89H.Command.Data.CardTransaction) WatchTransaction.EventData;
            //CardTransaction cardTrn = (CardTransaction) WatchTransaction.EventData;
            strBuf.append("\n?????????");
            strBuf.append(cardTrn.CardDataStr);
            strBuf.append("????????????");
            strBuf.append(cardTrn.DoorNum());
            if (cardTrn.IsEnter()) {
                strBuf.append("???????????????");
            } else {
                strBuf.append("???????????????");
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param WatchTransaction ????????????
     * @param strBuf ???????????????
     */
    private void PrintWatchEvent(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf) {
        switch (WatchTransaction.CmdIndex) {
            case 1://????????????
                logTransaction(WatchTransaction, strBuf, mCardTransactionList);
                break;
            case 2://??????????????????
                logTransaction(WatchTransaction, strBuf, mButtonTransactionList);
                break;
            case 3://????????????
                logTransaction(WatchTransaction, strBuf, mDoorSensorTransactionList);
                break;
            case 4://??????????????????
                logTransaction(WatchTransaction, strBuf, mSoftwareTransactionList);
                break;
            case 5://????????????
                logTransaction(WatchTransaction, strBuf, mAlarmTransactionList);
                break;
            case 6://????????????
                logTransaction(WatchTransaction, strBuf, mSystemTransactionList);
                break;
            default:
                logTransaction(WatchTransaction, strBuf, null);

        }
    }

    private StringBuilder strLog;

    private void GetCommandDetail(StringBuilder strBuf, INCommand cmd) {
        if (cmd == null) {
            strBuf.append("???????????????null");
            return;
        }
        strBuf.append("???????????????");
        strBuf.append(GetCommandName(cmd));
        if (cmd.getCommandParameter() != null) {
            strBuf.append("???SN:");
            CommandDetail det = cmd.getCommandParameter().getCommandDetail();
            strBuf.append(det.Identity.GetIdentity());
            strBuf.append(",");
            GetConnectorDetail(strBuf, det.Connector);
        }
        strBuf.append("\n");

    }

    private String GetCommandName(INCommand cmd) {
        if (cmd == null) {
            return "null";
        }
        String sKey = cmd.getClass().getName();
        if (CommandName.containsKey(sKey)) {
            return CommandName.get(sKey);
        } else {
            return sKey;
        }
    }

    private void GetConnectorDetail(StringBuilder strBuf, ConnectorDetail conn) {
        if (conn == null) {
            strBuf.append("ConnectorDetail???null");
            return;
        }
        if (conn instanceof TCPClientDetail) {
            TCPClientDetail tcp = (TCPClientDetail) conn;
            strBuf.append("TCP????????????:");
            strBuf.append(tcp.IP);
            strBuf.append(":");
            strBuf.append(tcp.Port);
        } else if (conn instanceof UDPDetail) {
            UDPDetail udp = (UDPDetail) conn;
            strBuf.append("UDP????????????:");
            strBuf.append(udp.IP);
            strBuf.append(":");
            strBuf.append(udp.Port);
        }
        //strBuf.append(",");
    }

    private void AddLog(String log) {
        synchronized (strLog) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//??????????????????
            strLog.append(df.format(new Date()));
            strLog.append(":");
            strLog.append(log);
            strLog.append("\n");
            String txt = strLog.toString();
            //System.out.println("fcardiodemo.frmMain.AddLog() + " + txt);
            txtLog.setText(txt);

            if (strLog.length() > 50000) {
                strLog.delete(0, 30000);
            }

        }

    }

}
