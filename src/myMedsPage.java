/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class myMedsPage extends javax.swing.JFrame {
    
    String medName;
    String rxNumber;
    int dosageNumber;
    String dosageType;
    int frequencyNumber;
    String frequencyType;
    int dosesProv;
    boolean refills;
    int refillNumber;
    boolean remind;
    boolean again;
    String reminder;
    String requiredAgain;
    
    String newReminders;
    String newReminderTimes;
    String newRefillsBool;
    String rx;
    String refillText;
    String reminderText;
    String sqlString;
    String updateString;
    
    String contactName;
    String contactEmail;
    String contactPhone;
    String contactRelation;
    String contactSQLString;
    
    String newConPhone, newConEmail, newConRelation;
    
    String ce, cp, cr;
    String dp, ds;
    
    String docName;
    String docPhone;
    String docSpec;
    String docSQLString;
    
    String newDocPhone, newDocSpec;
    int selection;
    int choice;
    
    
    /*
    Declaring the connection, statements, model, and result set for
    the table to show
    */
    Connection connCon = null;
    Connection connDoc = null;
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DefaultListCellRenderer centerRender = new DefaultListCellRenderer();
    DefaultListModel DLM = new DefaultListModel(); //medsList
    DefaultListModel CL = new DefaultListModel(); //med confirm list
    DefaultListModel MIL = new DefaultListModel(); //med info list
    DefaultListModel ECL = new DefaultListModel(); //emergencyContactList
    DefaultListModel DCL = new DefaultListModel();  //docContactList 
    DefaultListModel ECIL = new DefaultListModel(); //emergency contact information list(detail view)
    DefaultListModel DCIL = new DefaultListModel(); //doctor contact (detail view)
    DefaultListModel CECL = new DefaultListModel(); //add contact confirmation list
    DefaultListModel CDCL = new DefaultListModel(); //add doctor confirmation list

    

    public myMedsPage() 
    {
        initComponents();
        /*
        manually adding DocumentListeners which is not supported by 
        the GUI builder. Don't edit this code!!
        */
        refillNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(refillNumberIn.getText().isBlank() || !refillNumberIn.getText().matches("[0-9]+")){
                    refillNextButton.setEnabled(false);
                }else{
                    refillNextButton.setEnabled(true);
                }                  
            }
        });
        
        medNameIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(medNameIn.getText().isBlank()){
                    nameNextButton.setEnabled(false);
                }else{
                    nameNextButton.setEnabled(true);
                }   
            }
        });
        
        dosageNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(dosageNumberIn.getText().isBlank() || !dosageNumberIn.getText().matches("[0-9]+")){                    
                    dosageNextButton.setEnabled(false);
                }else{
                    dosageNextButton.setEnabled(true);
                }   
            }
        });
        
        
        frequencyNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(frequencyNumberIn.getText().isBlank() || !frequencyNumberIn.getText().matches("[0-9]+")){                    
                    frequencyNextButton.setEnabled(false);
                }else{
                    frequencyNextButton.setEnabled(true);
                }   
            }
        });
        
        dosesProvIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(dosesProvIn.getText().isBlank() || !dosesProvIn.getText().matches("[0-9]+")){                    
                    dosesProvNextButton.setEnabled(false);
                }else{
                    dosesProvNextButton.setEnabled(true);
                }   
            }
        });
        
        
        /*
        Initializing the column to appear on the "my meds" page as well as
        establishing the model for the table on my meds page. Initializing the 
        connection to the local database and calling the updateList() methods
        */
        initList();
        initContactList();
        initDocList();
        updateContactList();
        updateDocList();
        updateList();

    }
    
    
    /* 
    This method connects to the database, makes it pretty, and 
    sets the model so the items appear on the list of medications
    */
    private void initList()
    {

        conn = myMedsPage.ConnectDB();
        
        stylizeList();
        medList.setModel(DLM);
    
    }
    
    private void initContactList()
    {
        connCon = myMedsPage.ConnectConDB();
        
        stylizeList();
        contactList.setModel(DCIL);
    }
    
    private void initDocList()
    {
        connDoc = myMedsPage.ConnectDocDB();
        
        stylizeList();
        doctorList.setModel(DCL);
    }
    
    private void updateContactList()
    {
        connCon = myMedsPage.ConnectConDB();
        ECL.removeAllElements();
        if(connCon != null)
        {
            String sql = "Select Name FROM Contacts;";
            try
            {
                pst = connCon.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    ECL.addElement(rs.getString("Name"));
  
                }
                contactList.setModel(ECL);
                pst.close();
                rs.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    private void updateDocList()
    {
        DCL.removeAllElements();
        
        connDoc = myMedsPage.ConnectDocDB();
        
        if(connDoc != null)
        {
           String sql = "Select * FROM Doctors;";
            
            try
            {
                pst = connDoc.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    DCL.addElement(rs.getString("Name"));
  
                }
                doctorList.setModel(DCL);
                pst.close();
                rs.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            } 
        }
    }
    
    private void toUpcomingReminders(){
        
        
        welcomePanel.setVisible(false);
        medsPanel.setVisible(false);
        icePanel.setVisible(false);
        upcomingRemindersPanel.setVisible(true);
        profileOptionsPanel.setVisible(false);
        
        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Unclicked.png")));
        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Unclicked.png")));
        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Unclicked.png")));
        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Clicked.png")));
    }
    private void toProfileOptions(){
        
        profileOptionsPanel.setVisible(true);
        
        welcomePanel.setVisible(false);
        medsPanel.setVisible(false);
        icePanel.setVisible(false);
        upcomingRemindersPanel.setVisible(false);
        
        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Clicked.png")));
        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Unclicked.png")));
        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Unclicked.png")));
        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Unclicked.png")));
    }
    private void backToIceHome(){
        medsPanel.setVisible(false);
        icePanel.setVisible(true);
        
        contactListPanel.setVisible(true);
        addNewContactPanel.setVisible(false); 
        contactInfoPanel.setVisible(false);
        doctorInfoPanel.setVisible(false);
        editContactPanel.setVisible(false);
        editDocPanel.setVisible(false);
        addNewDocPanel.setVisible(false);
    }
    private void backToMedsHome(){
        icePanel.setVisible(false);
        medsPanel.setVisible(true);
        
        myMedsListPanel.setVisible(true);
        medNamePanel.setVisible(false);
        rxNumberPanel.setVisible(false);
        dosagePanel.setVisible(false);
        frequencyPanel.setVisible(false);
        dosesPanel.setVisible(false);
        refillsPanel.setVisible(false);
        reminderPanel.setVisible(false);
        requireAgainPanel.setVisible(false);
        confirmationPanel.setVisible(false);
        medInfoPanel.setVisible(false);
        editMedPanel.setVisible(false);
    }
    
    /*
    A method to make it look nice
    */
    private void stylizeList()
    {

        centerRender.setHorizontalAlignment(JLabel.CENTER);
        centerRender.setHorizontalTextPosition(JLabel.CENTER);
        centerRender.setVerticalAlignment(JLabel.CENTER);
        centerRender.setBorder(BorderFactory.createBevelBorder(1));
        contactList.setCellRenderer(centerRender);
        doctorList.setCellRenderer(centerRender);
        medList.setCellRenderer(centerRender);
        confirmList.setCellRenderer(centerRender);
        medInfoList.setCellRenderer(centerRender);
        contactConfirmList.setCellRenderer(centerRender);
        docConfirmList.setCellRenderer(centerRender);
        contactInfoList.setCellRenderer(centerRender);
        doctorInfoList.setCellRenderer(centerRender);
    }

    /*
    This method populates the list that you see when you double click or click
    edit on a medication name on the main screen
    */
    public void getMedInfo(){
        conn = myMedsPage.ConnectDB();
        
        
        if (conn != null)
        {
            medName = medList.getSelectedValue();
            String sql = "Select * FROM Medications WHERE MedName = '" + medName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    MIL.addElement("Medication: " + rs.getString("MedName"));
                    
                    String rxNum = rs.getString("RXNumber");
                    
                    if(!rxNum.equals("null")){
                    MIL.addElement("RX Number: " + rs.getString("RXNumber"));
                    }
                    
                    MIL.addElement("Dosage: " + rs.getString("Dosage") + " " + 
                            " " + rs.getString("FrequencyNumber") + " x " + rs.getString("Frequency") + ".");
                    
                    MIL.addElement(rs.getString("DosesProvided") + " Doses Provided.");
                    
                    
                    String check = rs.getString("RefillsBool");
                    
                    if(check.equalsIgnoreCase("yes")){
                    MIL.addElement(rs.getString("RefillNumber") + " Refills Available.");
                    }
                    
                    String rem = rs.getString("Reminders");
                    
                    if(rem.equalsIgnoreCase("yes")){
                        MIL.addElement(rs.getString("Reminders") + " Reminders Are Set.");
                        MIL.addElement("For: " + rs.getString("ReminderTimes"));
                    }else if(rem.equalsIgnoreCase("no")){
                        MIL.addElement(rs.getString("Reminders") + " Reminders Are Set.");
                    }
                    
                    String ag = rs.getString("RequireAgain");
                    
                    if(ag.equalsIgnoreCase("yes")){
                        MIL.addElement("You will be reminded");
                        MIL.addElement("to call your doctor");
                    }else if(ag.equalsIgnoreCase("no")){
                        MIL.addElement("You do not require");
                        MIL.addElement("this medication again");
                    }
  
                }
                
                pst.close();
                rs.close();
                medInfoList.setModel(MIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    

    

    /*
    This method gets the information from the database and then sets all of the 
    fields to reflect the information retrieved
    */
    public void editMedInfo(){
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            medName = medList.getSelectedValue();
            String sql = "Select * FROM Medications WHERE MedName = '" + medName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    medNameEdit.setText(rs.getString("MedName"));
                    
                    String rxNum = rs.getString("RXNumber");
                    
                    if(rxNum.equals("null") == true){
                    rxNumberEdit.setText("");
                    }
                    else{
                        rxNumberEdit.setText(rs.getString("RXNumber"));
                    }
                    
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    String doseNumber = rs.getString("Dosage");
                    char[] chars = doseNumber.toCharArray();
                    for(char c : chars){
                        if(Character.isDigit(c)){
                            sb.append(c);
                        }else if(!Character.isDigit(c)){
                            sb2.append(c);
                        }
                    }
                    
                    dosageNumberEdit.setText(sb.toString()); 
                    frequencyNumberEdit.setText(rs.getString("FrequencyNumber")); 
                    
                    String doseTypeEdits = sb2.toString().stripLeading();
                    
                    switch(doseTypeEdits){
                        
                        case "MG":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Tablet(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Capsule(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "ML":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Injection(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Puff(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Application(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Drop(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                    }
                    
                    String fc = rs.getString("Frequency");
                    
                    if(fc.equalsIgnoreCase("a day")){
                        frequencyTypeIn.setSelectedIndex(0);
                    }else if(fc.equalsIgnoreCase("a week")){
                        frequencyTypeIn.setSelectedIndex(1);
                    }else if(fc.equalsIgnoreCase("a month")){
                        frequencyTypeIn.setSelectedIndex(2);
                    }
                    
                    dosesProvEdit.setText(rs.getString("DosesProvided"));
                    
                    
                    String check = rs.getString("RefillsBool");
                    
                    if(check.equalsIgnoreCase("yes")){
                        yesButtonRefillsEdit.setSelected(true);
                        howManyLabel.setVisible(true);
                        refillNumberEdit.setVisible(true);
                        refillNumberEdit.setText(rs.getString("RefillNumber"));
                    }else if(check.equalsIgnoreCase("no")){
                        noButtonRefillsEdit.setSelected(true);
                    }
                    
                    String rem = rs.getString("Reminders");
                    
                    if(rem.equalsIgnoreCase("yes")){
                        yesButtonRemindEdit.setSelected(true);
                        
                        String time = rs.getString("ReminderTimes");
                        
                        String hour = time.substring(0, 2);
                        String minute = time.substring(3, 5);
                        String half = time.substring(6, 10);
                        hourInEdit.setVisible(true);
                        minuteInEdit.setVisible(true);
                        ampmInEdit.setVisible(true);
                        hourInEdit.setValue(hour);
                        minuteInEdit.setValue(minute);
                        if(half.equalsIgnoreCase("a.m.")){
                            ampmInEdit.setValue("A.M.");
                        }else if(half.equalsIgnoreCase("p.m.")){
                            ampmInEdit.setValue("P.M.");
                        }
                    }else if(rem.equalsIgnoreCase("no")){
                        noButtonRemindEdit.setSelected(true);
                        hourInEdit.setVisible(false);
                        minuteInEdit.setVisible(false);
                        ampmInEdit.setVisible(false);
                    }
                    
                    String ag = rs.getString("RequireAgain");
                    
                    if(ag.equalsIgnoreCase("yes")){
                        yesButtonAgainEdit.setSelected(true);
                        reminderLabelEdit.setVisible(true);
                        jLabel18.setVisible(true);
                    }else if(ag.equalsIgnoreCase("no")){
                        noButtonAgainEdit.setSelected(true);
                    }
                }
                
                pst.close();
                rs.close();
                medInfoList.setModel(MIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public String getMedName()
    {   
        medName = medNameIn.getText();
        
        return medName;
    }   
    
    public String getRXNumber()
    {
        if(rxNumberIn.getText().toString().isBlank()){
            rxNumber = null;
        }else{
            rxNumber = rxNumberIn.getText().toString();
        }
        return rxNumber;
    }
   
    public int getDosageNumber()
    {
        dosageNumber = Integer.parseInt(dosageNumberIn.getText().toString());
        
        return dosageNumber;
    }
    
    public String getDosageType()
    {
        dosageType = dosageTypeIn.getSelectedItem().toString();
        
        return dosageType;
    }
    
    public int getFrequencyNumber()
    {
        frequencyNumber = Integer.parseInt(frequencyNumberIn.getText().toString());
        
        return frequencyNumber;
    }
    
    public String getFrequencyType()
    {
        frequencyType = frequencyTypeIn.getSelectedItem().toString();
        
        return frequencyType;
    }
    
    public int getDosesProv()
    {
        dosesProv = Integer.parseInt(dosesProvIn.getText().toString());
        
        return dosesProv;
    }
    
    
    public boolean getRefills(Boolean refills)
    {
        if(refillNumberIn.getText().isBlank())
        {
            refills = false;
            
        }else if(!refillNumberIn.getText().isBlank())
        {
            refills = true;
            setRefillNumber();
        }
        return refills;
    }
    
    
    public int setRefillNumber()
    {
        refillNumber = Integer.parseInt(refillNumberIn.getText());
        
        return refillNumber;
    }
    
    public boolean getReminder()
    {
        if(hourIn.isVisible()){
            remind = true;
            
        }else if(!hourIn.isVisible()){
            remind = false;
        }
        return remind;
    }
    
    public String getContactName()
    {
        contactName = conNameIn.getText();
        return contactName;
    }
    
    public String getContactPhone()
    {
        contactPhone = conPhoneIn.getText();
        return contactPhone;
    }
    public String getContactEmail()
    {
        contactEmail = conEmailIn.getText();
        return contactEmail;
    }
    
    public String getContactRelation()
    {
        contactRelation = conRelationIn.getText();
        return contactRelation;
    }
    
    public String getDocName(){
        docName = docNameIn.getText();
        return docName;
    }
    
    public String getDocPhone(){
        docPhone = docPhoneIn.getText();
        return docPhone;
    }
    
    public String getDocSpec(){
        docSpec = docSpecIn.getText();
        return docSpec;
    }
    
    public String setReminder(){
        
        
        String hour = hourIn.getValue().toString();
        String minute = minuteIn.getValue().toString();
        String half = ampmIn.getValue().toString();
        reminder = (hour + ":" + minute + " " + half);
        
        return reminder;
    }
    
    public boolean getAgain(Boolean again){
        if(yesButtonA.isSelected()){
            again = true;
        }else if(noButtonA.isSelected()){
            again = false;
        }
        
        return again;
    }
    
    private void setConList(String medName, String rxNumber, int dosageNumber, String dosageType,
        int frequencyNumber, String frequencyType, int dosesProv, boolean refills,
        int refillNumber, boolean remind, boolean again, String reminder){

        CL.clear();
        confirmList.setModel(CL);
        CL.addElement(medName);

        
        if(rxNumber != null){
                CL.addElement("RX Number:  " + rxNumber);
            }else if(rxNumber == null){
                CL.addElement("RX Number: Not Provided");
            }
        CL.addElement(dosageNumber + " " + dosageType + " " +  frequencyNumber + " Times " + frequencyType);

        CL.addElement(dosesProv + " doses provided.");


        if(refills == false){
            CL.addElement("No Refills Available");
        }else if(refills == true){            
            CL.addElement(String.valueOf(refillNumber) + " Refills Available");
        }


        if(remind = false){
            CL.addElement(" ");
        }else if(remind = true){
            CL.addElement(reminder);
        }


        if(yesButtonA.isSelected() == true){
            CL.addElement("You will be reminded");
        }else if(noButtonA.isSelected() == true){
            CL.addElement("You do not require");
            CL.addElement("this medication again");
        }
    }
    private void setContactConfirmList(String contactName, String contactPhone, String contactEmail, String contactRelation){
        CECL.clear();
        contactConfirmList.setModel(CECL);
        CECL.addElement("Emergency Contact:");
        CECL.addElement(contactName);
        
        if(!contactPhone.isEmpty()){
            CECL.addElement("Phone Number:");
            CECL.addElement(contactPhone);
        }else if(contactPhone.isEmpty()){
            CECL.addElement("Phone Number:");
            CECL.addElement("Not Provided");
        }
        
        if(!contactEmail.isEmpty()){
            CECL.addElement("Email Address:");
            CECL.addElement(contactEmail);
        }else if(contactEmail.isEmpty()){
            CECL.addElement("Email Address:");
            CECL.addElement("Not Provided");
        }
        
        if(!contactRelation.isEmpty()){
            CECL.addElement("Relationship:");
            CECL.addElement(contactRelation);
        }else if(contactRelation.isEmpty()){
            CECL.addElement("Relationship:");
            CECL.addElement("Not Provided");
        }
        
        contactConfirmList.setModel(CECL);
        
        
        
    }    
    public String createContactSQLString(String contactName, String contactPhone, String contactEmail, String contactRelation)
    {
        if(!contactPhone.isEmpty()){
            cp = contactPhone;
        }
        else if(contactPhone.isEmpty()){
           cp = "Not Provided"; 
        }
        
        if(!contactEmail.isEmpty()){
            ce = contactEmail;
        }else if (contactEmail.isEmpty()){
            ce = "Not Provided";
        }
        
        if(!contactRelation.isEmpty()){
            cr = contactRelation;
        }else if(contactRelation.isEmpty()){
            cr = "Not Provided";
        }
        
        contactSQLString = ("INSERT INTO Contacts VALUES("
                + "'" + contactName + "',"
                + "'" + cp + "',"
                + "'" + ce + "',"
                + "'" + cr + "');");
        return contactSQLString;
    }

    public String createContactEditString(String contactName, String newConPhone, String newConEmail, String newConRelation){
        String newConName = conNameEdit.getText();
        newConPhone = conPhoneEdit.getText();
        newConEmail = conEmailEdit.getText();
        newConRelation = conRelationEdit.getText();
        
        contactSQLString = ("UPDATE Contacts SET "
                + "Name = '"
                + newConName + "',"
                + "Phone = '"
                + newConPhone + "',"
                + "Email = '"
                + newConEmail + "',"
                + "Relationship = '"
                + newConRelation + "' WHERE Name = '" + contactName + "';");
        
        return contactSQLString;
    }
    public void updateContactDB(String contactSQLString){
        conn = myMedsPage.ConnectConDB();
        
        if (conn != null)
        {
            String sql = contactSQLString;
            
            try
            {
                int rows = 0;
                pst = conn.prepareStatement(sql);
                rows = pst.executeUpdate();

                pst.close();
                rs.close();
                
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public String createDocSQLString(String docName, String docPhone, String docSpec){
        
        if(!docPhone.isEmpty()){
            dp = docPhone;
        }else if(docPhone.isEmpty()){
            dp = "Not Provided";
        }
        
        if(!docSpec.isEmpty()){
            ds = docSpec;
        }else if(docSpec.isEmpty()){
            ds = "Not Provided";
        }
        
        docSQLString = ("INSERT INTO Doctors VALUES("
                + "'" + docName + "',"
                + "'" + dp + "',"
                + "'" + ds + "');");
        
        return docSQLString;
    }
    
    public String createDocEditString(String docName, String newDocPhone, String newDocSpec){
        String newDocName = docNameEdit.getText();
        newDocPhone = docPhoneEdit.getText();
        newDocSpec = docSpecEdit.getText();
        
        docSQLString = ("UPDATE Doctors SET "
                + "Name = '"
                + newDocName + "',"
                + "Phone = '"
                + newDocPhone + "',"
                + "Specialty = '"
                + newDocSpec + "' WHERE Name = '" + docName + "';");
        
        return docSQLString;
    }
    
    public void updateDocDB(String docSQLString){
        conn = myMedsPage.ConnectDocDB();
        
        if (conn != null)
        {
            String sql = docSQLString;
            
            try
            {
                int rows = 0;
                pst = conn.prepareStatement(sql);
                rows = pst.executeUpdate();

                pst.close();
                rs.close();
                
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void setDocConfirmList(String docName, String docPhone, String docSpec){
        CDCL.clear();
        docConfirmList.setModel(CDCL);
        CDCL.addElement("Doctor:");
        CDCL.addElement(docName);
        
        if(!docPhone.isEmpty()){
            CDCL.addElement("Phone Number:");
            CDCL.addElement(docPhone);
        }else if(docPhone.isEmpty()){
            CDCL.addElement("Phone Number:");
            CDCL.addElement("Not Provided");
        }
        
        if(!docSpec.isEmpty()){
            CDCL.addElement("Specialty:");
            CDCL.addElement(docSpec);
        }else if(docSpec.isEmpty()){
            CDCL.addElement("Specialty:");
            CDCL.addElement("Not Provided");
        }
        
        docConfirmList.setModel(CDCL);
    }
    /*
    This method takes the information edited by the user and creates the 
    string command to update the database. This update string is passed to the 
    updateDatabase method to carry out the changes.
    */    
    public String createUpdateString(String medName, String newRefillsBool, String newReminders,
            String newReminderTimes){
        
        String newMedName = medNameEdit.getText();
        String newRXNumber = rxNumberEdit.getText();
        String newDosageNumber = dosageNumberEdit.getText();
        String newDosageType = dosageTypeEdit.getSelectedItem().toString();
        String newDose = (newDosageNumber + " " + newDosageType);
        String newFrequencyNumber = frequencyNumberEdit.getText();
        String newFrequencyType = frequencyTypeEdit.getSelectedItem().toString();
        String newDosesProv = dosesProvEdit.getText();
        String newRefillNumber = refillNumberEdit.getText();
        String newHour = hourInEdit.getValue().toString();
        String newMinute = minuteInEdit.getValue().toString();
        String newAMPM = ampmInEdit.getValue().toString();
        
        

        if(yesButtonRefillsEdit.isSelected()){
            newRefillsBool = "Yes";
        }else if(noButtonRefillsEdit.isSelected()){
            newRefillsBool = "No";
        }
        
        if(yesButtonRemindEdit.isSelected()){
            newReminders = "Yes";
            newReminderTimes = newHour + ":" + newMinute + " " + newAMPM;
        }else if(noButtonRemindEdit.isSelected()){
            newReminders = "No";
            newReminderTimes = "null";
        }
        
        if(yesButtonAgainEdit.isSelected()){
            requiredAgain = "Yes";
        }else if(noButtonAgainEdit.isSelected()){
            requiredAgain = "No";
        }
        
        sqlString = ("UPDATE Medications SET "
            + "MedName = '"
            + newMedName + "',"
            + "RXNumber = '"
            + newRXNumber + "', "
            + "Dosage = '"
            + newDose + "', "
            + "FrequencyNumber = '"
            + newFrequencyNumber + "', "
            + "Frequency = '"
            + newFrequencyType + "', "
            + "DosesProvided = '"
            + newDosesProv + "', "
            + "RefillsBool = '"
            + newRefillsBool + "', "
            + "RefillNumber = '"
            + newRefillNumber + "', "
            + "Reminders = '"
            + newReminders + "', "
            + "ReminderTimes = '"
            + newReminderTimes + "',"
            + "RequireAgain = '"
            + requiredAgain + "' WHERE MedName = '" + medName + "';");
        
        
        
    
        return sqlString;
    }

    /*
      createSQLString method creates the command to enter the information into
        the database based on the information the user has provided    
    */
    public String createSQLString(String medName, String rxNumber, int dosageNumber, String dosageType,
            int frequencyNumber, String frequencyType, int dosesProv, boolean refills, 
            int refillNumber, boolean remind, boolean again, String reminder){
        
        
        String dosage = (String.valueOf(dosageNumber) + " " + dosageType);

        if(rxNumber != null){
            rx = rxNumber;
        }else if(rxNumber == null){
            rx = "null";
        }
            
        String doses = String.valueOf(dosesProv);
        
        if(refills == false){
            refillText = "No";
        }else if(refills == true){            
            refillText = "Yes";
        }
            
        String numOfRefill = String.valueOf(refillNumber);
          
        if(remind == false){
            reminderText = "No";
            reminder = "null";
        }else if(remind == true){
            reminderText = "Yes";
        }

        if(again == false){
            requiredAgain = "No";
        }else if(again == true){
            requiredAgain = "Yes";           
        }
        
        sqlString = ("INSERT INTO Medications VALUES(" 
            + "'" + medName + "',"
            + "'" + rx + "', " 
            + "'" + dosage + "', "
            + "'" + frequencyNumber + "', "
            + "'" + frequencyType + "', " 
            + "'" + doses + "', " 
            + "'" + refillText + "', "
            + "'" + numOfRefill + "', "
            + "'" + reminderText + "', "
            + "'" + reminder + "', "
            + "'" + requiredAgain + "'" + ");");
        
        
        
    
        return sqlString;
    }

    
    public void getContactInfo(){
        conn = myMedsPage.ConnectConDB();
        
        ECIL.removeAllElements();
        if (conn != null)
        {
            contactName = contactList.getSelectedValue();
            String sql = "Select * FROM Contacts WHERE Name = '" + contactName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    ECIL.addElement("Emergency Contact's Name: " + rs.getString("Name"));
                    ECIL.addElement("Phone Number: " + rs.getString("Phone"));
                    ECIL.addElement("Email: " + rs.getString("Email"));
                    ECIL.addElement("Relationship: " + rs.getString("Relationship"));
                   
                }
                
                pst.close();
                rs.close();
                contactInfoList.setModel(ECIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void getDoctorInfo(){
        conn = myMedsPage.ConnectDocDB();
        
        DCIL.removeAllElements();
        if (conn != null)
        {
            docName = doctorList.getSelectedValue();
            String sql = "Select * FROM Doctors WHERE Name = '" + docName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    DCIL.addElement("Doctor's Name: " + rs.getString("Name"));
                    DCIL.addElement("Phone Number: " + rs.getString("Phone"));
                    DCIL.addElement("Specialty: " + rs.getString("Specialty"));
                   
                }
                
                pst.close();
                rs.close();
                doctorInfoList.setModel(DCIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void deleteDoctor(){
        conn = myMedsPage.ConnectDocDB();
        
        if(conn != null)
        {
            String sql = "DELETE FROM Doctors WHERE Name='" + doctorList.getSelectedValue() + "';";
            String selected = doctorList.getSelectedValue();
            try
            {
            int rows = 0;
            pst = conn.prepareStatement(sql);
            rows = pst.executeUpdate();
            
            pst.close();
            
            DCL.removeElement(selected);
            doctorList.setModel(DCL);
            
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void deleteContact(){
        conn = myMedsPage.ConnectConDB();
        
        if(conn != null)
        {
            String sql = "DELETE FROM Contacts WHERE Name='" + contactList.getSelectedValue() + "';";
            String selected = contactList.getSelectedValue();
            try
            {
            int rows = 0;
            pst = conn.prepareStatement(sql);
            rows = pst.executeUpdate();
            
            pst.close();
            
            ECL.removeElement(selected);
            contactList.setModel(ECL);
            
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void editContactInfo(){
        conn = myMedsPage.ConnectConDB();
        
        if (conn != null)
        {
            contactName = contactList.getSelectedValue();
            String sql = "Select * FROM Contacts WHERE Name = '" + contactName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                    conNameEdit.setText(rs.getString("Name"));
                    conPhoneEdit.setText(rs.getString("Phone"));
                    conEmailEdit.setText(rs.getString("Email"));
                    conRelationEdit.setText(rs.getString("Relationship"));   
                }
                
                pst.close();
                rs.close();
                contactInfoList.setModel(ECL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    
    }
    
    public void editDoctorInfo(){
        conn = myMedsPage.ConnectDocDB();
        
        if (conn != null)
        {
            docName = doctorList.getSelectedValue();
            String sql = "Select * FROM Doctors WHERE Name = '" + docName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                    docNameEdit.setText(rs.getString("Name"));
                    docPhoneEdit.setText(rs.getString("Phone"));
                    docSpecEdit.setText(rs.getString("Specialty"));   
                }
                
                pst.close();
                rs.close();
                doctorInfoList.setModel(DCL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
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

        buttonBar = new javax.swing.ButtonGroup();
        refillsYesNoEdit = new javax.swing.ButtonGroup();
        remindersYesNoEdit = new javax.swing.ButtonGroup();
        requireAgainYesNoEdit = new javax.swing.ButtonGroup();
        requireAgainYesNo = new javax.swing.ButtonGroup();
        refillsYesNo = new javax.swing.ButtonGroup();
        middlePanel01 = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        medsPanel = new javax.swing.JPanel();
        myMedsListPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        medList = new javax.swing.JList<>();
        addNewMedButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        medNamePanel = new javax.swing.JPanel();
        nameInstruct = new javax.swing.JLabel();
        medNameIn = new javax.swing.JTextField();
        nameNextButton = new javax.swing.JButton();
        nameBackButton = new javax.swing.JButton();
        nameCancelButton = new javax.swing.JButton();
        rxNumberPanel = new javax.swing.JPanel();
        rxNumInst = new javax.swing.JLabel();
        rxNumberIn = new javax.swing.JTextField();
        rxNumberNextButton = new javax.swing.JButton();
        rxNumberBackButton = new javax.swing.JButton();
        rxNumberCancelButton = new javax.swing.JButton();
        dosagePanel = new javax.swing.JPanel();
        dosageInstruct = new javax.swing.JLabel();
        dosageNumberIn = new javax.swing.JTextField();
        dosageNextButton = new javax.swing.JButton();
        dosageTypeIn = new javax.swing.JComboBox<>();
        dosageBackButton = new javax.swing.JButton();
        dosageCancelButton = new javax.swing.JButton();
        frequencyPanel = new javax.swing.JPanel();
        frequencyInstruct = new javax.swing.JLabel();
        frequencyNumberIn = new javax.swing.JTextField();
        frequencyTypeIn = new javax.swing.JComboBox<>();
        frequencyNextButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        frequencyBackButton = new javax.swing.JButton();
        frequencyCancelButton = new javax.swing.JButton();
        dosesPanel = new javax.swing.JPanel();
        dosesInstruct = new javax.swing.JLabel();
        dosesProvIn = new javax.swing.JTextField();
        dosesProvNextButton = new javax.swing.JButton();
        dosesProvBackButton = new javax.swing.JButton();
        dosesProvCancelButton = new javax.swing.JButton();
        refillsPanel = new javax.swing.JPanel();
        refillInstruct = new javax.swing.JLabel();
        howMany = new javax.swing.JLabel();
        refillNumberIn = new javax.swing.JTextField();
        refillNextButton = new javax.swing.JButton();
        yesButton = new javax.swing.JToggleButton();
        refillsBackButton = new javax.swing.JButton();
        refillsCancelButton = new javax.swing.JButton();
        noButton = new javax.swing.JToggleButton();
        reminderPanel = new javax.swing.JPanel();
        reminderInstruct = new javax.swing.JLabel();
        remindNextButton = new javax.swing.JButton();
        yesButtonR = new javax.swing.JButton();
        noButtonR = new javax.swing.JButton();
        hourIn = new javax.swing.JSpinner();
        minuteIn = new javax.swing.JSpinner();
        ampmIn = new javax.swing.JSpinner();
        reminderBackButton = new javax.swing.JButton();
        reminderCancelButton = new javax.swing.JButton();
        requireAgainPanel = new javax.swing.JPanel();
        requireAgainInstruct = new javax.swing.JLabel();
        requireAgainNextButton = new javax.swing.JButton();
        callMessage = new javax.swing.JLabel();
        requireAgainBackButton = new javax.swing.JButton();
        requireAgainCancelButton = new javax.swing.JButton();
        yesButtonA = new javax.swing.JToggleButton();
        noButtonA = new javax.swing.JToggleButton();
        confirmationPanel = new javax.swing.JPanel();
        confirmButton = new javax.swing.JButton();
        confirmCancelButton = new javax.swing.JButton();
        confirmBackButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        confirmList = new javax.swing.JList<>();
        medInfoPanel = new javax.swing.JPanel();
        medInfoBackButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        medInfoList = new javax.swing.JList<>();
        medInfoEditButton = new javax.swing.JButton();
        editMedPanel = new javax.swing.JPanel();
        editPanelConfirmButton = new javax.swing.JButton();
        editPanelBackButton = new javax.swing.JButton();
        medNameEdit = new javax.swing.JTextField();
        rxNumberEdit = new javax.swing.JTextField();
        dosageNumberEdit = new javax.swing.JTextField();
        dosageTypeEdit = new javax.swing.JComboBox<>();
        frequencyNumberEdit = new javax.swing.JTextField();
        frequencyTypeEdit = new javax.swing.JComboBox<>();
        dosesProvEdit = new javax.swing.JTextField();
        refillNumberEdit = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        howManyLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        hourInEdit = new javax.swing.JSpinner();
        minuteInEdit = new javax.swing.JSpinner();
        ampmInEdit = new javax.swing.JSpinner();
        yesButtonRemindEdit = new javax.swing.JToggleButton();
        noButtonRemindEdit = new javax.swing.JToggleButton();
        yesButtonRefillsEdit = new javax.swing.JToggleButton();
        noButtonRefillsEdit = new javax.swing.JToggleButton();
        yesButtonAgainEdit = new javax.swing.JToggleButton();
        noButtonAgainEdit = new javax.swing.JToggleButton();
        reminderLabelEdit = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        icePanel = new javax.swing.JPanel();
        contactListPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contactList = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        doctorList = new javax.swing.JList<>();
        deleteContactButton = new javax.swing.JButton();
        addContactButton = new javax.swing.JButton();
        addDoctorButton = new javax.swing.JButton();
        deleteDoctorButton = new javax.swing.JButton();
        addNewContactPanel = new javax.swing.JPanel();
        addConNamePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        conNameIn = new javax.swing.JTextField();
        conNameNextButton = new javax.swing.JButton();
        conNameBackButton = new javax.swing.JButton();
        conNameCancelButton = new javax.swing.JButton();
        addConPhonePanel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        conPhoneIn = new javax.swing.JTextField();
        conPhoneBackButton = new javax.swing.JButton();
        conPhoneNextButton = new javax.swing.JButton();
        conPhoneCancelButton = new javax.swing.JButton();
        addConEmailPanel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        conEmailIn = new javax.swing.JTextField();
        conEmailBackButton = new javax.swing.JButton();
        conEmailNextButton = new javax.swing.JButton();
        conEmailCancelButton = new javax.swing.JButton();
        addConRelationshipPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        conRelationIn = new javax.swing.JTextField();
        conRelationBackButton = new javax.swing.JButton();
        conRelationNextButton = new javax.swing.JButton();
        conRelationCancelButton = new javax.swing.JButton();
        addConConfirmPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        contactConfirmList = new javax.swing.JList<>();
        conConfirmBackButton = new javax.swing.JButton();
        conConfirmConfirmButton = new javax.swing.JButton();
        conConfirmCancelButton = new javax.swing.JButton();
        addNewDocPanel = new javax.swing.JPanel();
        addDocNamePanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        docNameIn = new javax.swing.JTextField();
        docNameNextButton = new javax.swing.JButton();
        docNameBackButton = new javax.swing.JButton();
        docNameCancelButton = new javax.swing.JButton();
        addDocPhonePanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        docPhoneIn = new javax.swing.JTextField();
        docPhoneNextButton = new javax.swing.JButton();
        docPhoneBackButton = new javax.swing.JButton();
        docPhoneCancelButton = new javax.swing.JButton();
        addDocSpecPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        docSpecIn = new javax.swing.JTextField();
        docSpecNextButton = new javax.swing.JButton();
        docSpecBackButton = new javax.swing.JButton();
        docSpecCancelButton = new javax.swing.JButton();
        addDocConfirmPanel = new javax.swing.JPanel();
        docConfirmBackButton = new javax.swing.JButton();
        docConfirmConfirmButton = new javax.swing.JButton();
        docConfirmCancelButton = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        docConfirmList = new javax.swing.JList<>();
        contactInfoPanel = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        contactInfoList = new javax.swing.JList<>();
        contactEditButton = new javax.swing.JButton();
        contactInfoBackButton = new javax.swing.JButton();
        doctorInfoPanel = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        doctorInfoList = new javax.swing.JList<>();
        docEditButton = new javax.swing.JButton();
        docInfoBackButton = new javax.swing.JButton();
        editContactPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        conNameEdit = new javax.swing.JTextField();
        conPhoneEdit = new javax.swing.JTextField();
        conEmailEdit = new javax.swing.JTextField();
        conRelationEdit = new javax.swing.JTextField();
        conEditBackButton = new javax.swing.JButton();
        conEditConfirmButton = new javax.swing.JButton();
        conEditCancelButton = new javax.swing.JButton();
        editDocPanel = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        docNameEdit = new javax.swing.JTextField();
        docPhoneEdit = new javax.swing.JTextField();
        docSpecEdit = new javax.swing.JTextField();
        docEditBackButton = new javax.swing.JButton();
        docEditConfirmButton = new javax.swing.JButton();
        docEditCancelButton = new javax.swing.JButton();
        profileOptionsPanel = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        upcomingRemindersPanel = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        titleBar = new javax.swing.JLabel();
        myMedsButton = new javax.swing.JButton();
        upcomingRemindersButton = new javax.swing.JButton();
        iceButton = new javax.swing.JButton();
        profileOptionsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 602, 841));
        setResizable(false);
        setSize(new java.awt.Dimension(510, 800));

        middlePanel01.setBackground(new java.awt.Color(255, 232, 214));
        middlePanel01.setPreferredSize(new java.awt.Dimension(510, 600));
        middlePanel01.setLayout(new java.awt.CardLayout());

        welcomePanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Welcome Panel.png"))); // NOI18N

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addGap(0, 0, 0))
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addComponent(jLabel27)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        middlePanel01.add(welcomePanel, "card2");
        welcomePanel.setVisible(true);

        medsPanel.setBackground(new java.awt.Color(255, 232, 214));
        medsPanel.setLayout(new java.awt.CardLayout());

        myMedsListPanel.setOpaque(false);
        myMedsListPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setViewportView(medList);

        medList.setBackground(new java.awt.Color(107, 112, 92));
        medList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        medList.setForeground(new java.awt.Color(255, 232, 214));
        medList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        medList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        medList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        medList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(medList);

        addNewMedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add A Medication.png"))); // NOI18N
        addNewMedButton.setBorderPainted(false);
        addNewMedButton.setContentAreaFilled(false);
        addNewMedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewMedButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button.png"))); // NOI18N
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button Clicked.png"))); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Button.png"))); // NOI18N
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Button Clicked.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout myMedsListPanelLayout = new javax.swing.GroupLayout(myMedsListPanel);
        myMedsListPanel.setLayout(myMedsListPanelLayout);
        myMedsListPanelLayout.setHorizontalGroup(
            myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myMedsListPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(myMedsListPanelLayout.createSequentialGroup()
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addNewMedButton))
                .addGap(80, 80, 80))
            .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 516, Short.MAX_VALUE))
        );
        myMedsListPanelLayout.setVerticalGroup(
            myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myMedsListPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(addNewMedButton)
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 600, Short.MAX_VALUE))
        );

        medsPanel.add(myMedsListPanel, "card2");
        myMedsListPanel.setVisible(false);

        medNamePanel.setBackground(new java.awt.Color(255, 232, 214));
        medNamePanel.setForeground(new java.awt.Color(255, 232, 214));
        medNamePanel.setOpaque(false);
        medNamePanel.setPreferredSize(new java.awt.Dimension(510, 600));

        nameInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        nameInstruct.setForeground(new java.awt.Color(0, 0, 0));
        nameInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nameInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Med Name Panel.png"))); // NOI18N

        medNameIn.setBackground(new java.awt.Color(107, 112, 92));
        medNameIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        medNameIn.setForeground(new java.awt.Color(255, 232, 214));
        medNameIn.setPreferredSize(new java.awt.Dimension(350, 45));

        nameNextButton.setBackground(new java.awt.Color(107, 112, 92));
        nameNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        nameNextButton.setForeground(new java.awt.Color(221, 190, 169));
        nameNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        nameNextButton.setBorderPainted(false);
        nameNextButton.setContentAreaFilled(false);
        nameNextButton.setEnabled(false);
        nameNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        nameNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        nameNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameNextButtonActionPerformed(evt);
            }
        });

        nameBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        nameBackButton.setBorderPainted(false);
        nameBackButton.setContentAreaFilled(false);
        nameBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        nameBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        nameBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameBackButtonActionPerformed(evt);
            }
        });

        nameCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        nameCancelButton.setContentAreaFilled(false);
        nameCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        nameCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        nameCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout medNamePanelLayout = new javax.swing.GroupLayout(medNamePanel);
        medNamePanel.setLayout(medNamePanelLayout);
        medNamePanelLayout.setHorizontalGroup(
            medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medNamePanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(medNamePanelLayout.createSequentialGroup()
                        .addComponent(nameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nameInstruct, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(medNameIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(medNamePanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(nameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        medNamePanelLayout.setVerticalGroup(
            medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, medNamePanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(nameInstruct)
                .addGap(50, 50, 50)
                .addComponent(medNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(nameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        medsPanel.add(medNamePanel, "card2");
        medNamePanel.setVisible(false);

        rxNumberPanel.setBackground(new java.awt.Color(255, 232, 214));
        rxNumberPanel.setForeground(new java.awt.Color(255, 232, 214));
        rxNumberPanel.setOpaque(false);
        rxNumberPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        rxNumInst.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        rxNumInst.setForeground(new java.awt.Color(0, 0, 0));
        rxNumInst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rxNumInst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RX Number Panel.png"))); // NOI18N

        rxNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        rxNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        rxNumberIn.setForeground(new java.awt.Color(255, 232, 214));

        rxNumberNextButton.setBackground(new java.awt.Color(107, 112, 92));
        rxNumberNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        rxNumberNextButton.setForeground(new java.awt.Color(221, 190, 169));
        rxNumberNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        rxNumberNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rxNumberNextButton.setBorderPainted(false);
        rxNumberNextButton.setContentAreaFilled(false);
        rxNumberNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        rxNumberNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        rxNumberNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberNextButtonActionPerformed(evt);
            }
        });

        rxNumberBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        rxNumberBackButton.setBorderPainted(false);
        rxNumberBackButton.setContentAreaFilled(false);
        rxNumberBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        rxNumberBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        rxNumberBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberBackButtonActionPerformed(evt);
            }
        });

        rxNumberCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        rxNumberCancelButton.setContentAreaFilled(false);
        rxNumberCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        rxNumberCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        rxNumberCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rxNumberPanelLayout = new javax.swing.GroupLayout(rxNumberPanel);
        rxNumberPanel.setLayout(rxNumberPanelLayout);
        rxNumberPanelLayout.setHorizontalGroup(
            rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rxNumberPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(rxNumberPanelLayout.createSequentialGroup()
                        .addComponent(rxNumberBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rxNumberNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rxNumInst)
                    .addComponent(rxNumberIn)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rxNumberPanelLayout.createSequentialGroup()
                        .addComponent(rxNumberCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)))
                .addGap(80, 80, 80))
        );
        rxNumberPanelLayout.setVerticalGroup(
            rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rxNumberPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(rxNumInst)
                .addGap(50, 50, 50)
                .addComponent(rxNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rxNumberNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rxNumberBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(rxNumberCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        medsPanel.add(rxNumberPanel, "card2");
        rxNumberPanel.setVisible(false);

        dosagePanel.setBackground(new java.awt.Color(255, 232, 214));
        dosagePanel.setForeground(new java.awt.Color(255, 232, 214));
        dosagePanel.setOpaque(false);
        dosagePanel.setPreferredSize(new java.awt.Dimension(510, 600));

        dosageInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        dosageInstruct.setForeground(new java.awt.Color(0, 0, 0));
        dosageInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dosageInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Dosage Panel.png"))); // NOI18N

        dosageNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        dosageNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageNumberIn.setForeground(new java.awt.Color(255, 232, 214));
        dosageNumberIn.setToolTipText("Enter a number for the dosage.");

        dosageNextButton.setBackground(new java.awt.Color(107, 112, 92));
        dosageNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageNextButton.setForeground(new java.awt.Color(255, 232, 214));
        dosageNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        dosageNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dosageNextButton.setBorderPainted(false);
        dosageNextButton.setContentAreaFilled(false);
        dosageNextButton.setEnabled(false);
        dosageNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        dosageNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        dosageNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageNextButtonActionPerformed(evt);
            }
        });

        dosageTypeIn.setBackground(new java.awt.Color(107, 112, 92));
        dosageTypeIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageTypeIn.setForeground(new java.awt.Color(255, 232, 214));
        dosageTypeIn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MG", "Tablet(s)", "Capsule(s)", "ML", "Injection(s)", "Puff(s)", "Application(s)", "Drop(s)" }));
        dosageTypeIn.setToolTipText("Select the type of dosage");

        dosageBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        dosageBackButton.setBorderPainted(false);
        dosageBackButton.setContentAreaFilled(false);
        dosageBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        dosageBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        dosageBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageBackButtonActionPerformed(evt);
            }
        });

        dosageCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        dosageCancelButton.setBorderPainted(false);
        dosageCancelButton.setContentAreaFilled(false);
        dosageCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        dosageCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        dosageCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dosagePanelLayout = new javax.swing.GroupLayout(dosagePanel);
        dosagePanel.setLayout(dosagePanelLayout);
        dosagePanelLayout.setHorizontalGroup(
            dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosagePanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dosagePanelLayout.createSequentialGroup()
                        .addComponent(dosageBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dosageNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dosageInstruct)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dosagePanelLayout.createSequentialGroup()
                        .addComponent(dosageNumberIn)
                        .addGap(18, 18, 18)
                        .addComponent(dosageTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dosagePanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(dosageCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        dosagePanelLayout.setVerticalGroup(
            dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosagePanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(dosageInstruct)
                .addGap(50, 50, 50)
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(dosageCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        medsPanel.add(dosagePanel, "card2");
        dosagePanel.setVisible(false);

        frequencyPanel.setBackground(new java.awt.Color(255, 232, 214));
        frequencyPanel.setForeground(new java.awt.Color(255, 232, 214));
        frequencyPanel.setOpaque(false);
        frequencyPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        frequencyInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        frequencyInstruct.setForeground(new java.awt.Color(0, 0, 0));
        frequencyInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        frequencyInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Frequency Panel.png"))); // NOI18N

        frequencyNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        frequencyNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyNumberIn.setForeground(new java.awt.Color(255, 232, 214));
        frequencyNumberIn.setToolTipText("");
        frequencyNumberIn.setPreferredSize(new java.awt.Dimension(130, 45));

        frequencyTypeIn.setBackground(new java.awt.Color(107, 112, 92));
        frequencyTypeIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyTypeIn.setForeground(new java.awt.Color(255, 232, 214));
        frequencyTypeIn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Day", "A Week", "A Month", "" }));
        frequencyTypeIn.setToolTipText("");
        frequencyTypeIn.setPreferredSize(new java.awt.Dimension(195, 45));

        frequencyNextButton.setBackground(new java.awt.Color(107, 112, 92));
        frequencyNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyNextButton.setForeground(new java.awt.Color(255, 232, 214));
        frequencyNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        frequencyNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        frequencyNextButton.setBorderPainted(false);
        frequencyNextButton.setContentAreaFilled(false);
        frequencyNextButton.setEnabled(false);
        frequencyNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        frequencyNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        frequencyNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyNextButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(107, 112, 92));
        jLabel1.setText("X");

        frequencyBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        frequencyBackButton.setBorderPainted(false);
        frequencyBackButton.setContentAreaFilled(false);
        frequencyBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        frequencyBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        frequencyBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyBackButtonActionPerformed(evt);
            }
        });

        frequencyCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        frequencyCancelButton.setBorderPainted(false);
        frequencyCancelButton.setContentAreaFilled(false);
        frequencyCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        frequencyCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        frequencyCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frequencyPanelLayout = new javax.swing.GroupLayout(frequencyPanel);
        frequencyPanel.setLayout(frequencyPanelLayout);
        frequencyPanelLayout.setHorizontalGroup(
            frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frequencyPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(frequencyInstruct)
                        .addGroup(frequencyPanelLayout.createSequentialGroup()
                            .addComponent(frequencyNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(frequencyTypeIn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(frequencyPanelLayout.createSequentialGroup()
                        .addComponent(frequencyBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(frequencyNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frequencyPanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(frequencyCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        frequencyPanelLayout.setVerticalGroup(
            frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frequencyPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(frequencyInstruct)
                .addGap(50, 50, 50)
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frequencyNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(60, 60, 60)
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frequencyNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(frequencyCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        medsPanel.add(frequencyPanel, "card2");
        frequencyPanel.setVisible(false);

        dosesPanel.setBackground(new java.awt.Color(255, 232, 214));
        dosesPanel.setForeground(new java.awt.Color(255, 232, 214));
        dosesPanel.setOpaque(false);
        dosesPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        dosesInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        dosesInstruct.setForeground(new java.awt.Color(0, 0, 0));
        dosesInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dosesInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Doses Panel.png"))); // NOI18N

        dosesProvIn.setBackground(new java.awt.Color(107, 112, 92));
        dosesProvIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosesProvIn.setForeground(new java.awt.Color(255, 232, 214));
        dosesProvIn.setPreferredSize(new java.awt.Dimension(350, 45));

        dosesProvNextButton.setBackground(new java.awt.Color(107, 112, 92));
        dosesProvNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosesProvNextButton.setForeground(new java.awt.Color(221, 190, 169));
        dosesProvNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        dosesProvNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dosesProvNextButton.setBorderPainted(false);
        dosesProvNextButton.setContentAreaFilled(false);
        dosesProvNextButton.setEnabled(false);
        dosesProvNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        dosesProvNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        dosesProvNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvNextButtonActionPerformed(evt);
            }
        });

        dosesProvBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        dosesProvBackButton.setBorderPainted(false);
        dosesProvBackButton.setContentAreaFilled(false);
        dosesProvBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        dosesProvBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        dosesProvBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvBackButtonActionPerformed(evt);
            }
        });

        dosesProvCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        dosesProvCancelButton.setBorderPainted(false);
        dosesProvCancelButton.setContentAreaFilled(false);
        dosesProvCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        dosesProvCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        dosesProvCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dosesPanelLayout = new javax.swing.GroupLayout(dosesPanel);
        dosesPanel.setLayout(dosesPanelLayout);
        dosesPanelLayout.setHorizontalGroup(
            dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosesPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dosesInstruct)
                    .addGroup(dosesPanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(dosesProvCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dosesPanelLayout.createSequentialGroup()
                            .addComponent(dosesProvBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dosesProvNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(dosesProvIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        dosesPanelLayout.setVerticalGroup(
            dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dosesPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(dosesInstruct)
                .addGap(50, 50, 50)
                .addComponent(dosesProvIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dosesProvNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosesProvBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(dosesProvCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        medsPanel.add(dosesPanel, "card2");
        dosesPanel.setVisible(false);

        refillsPanel.setBackground(new java.awt.Color(255, 232, 214));
        refillsPanel.setForeground(new java.awt.Color(255, 232, 214));
        refillsPanel.setOpaque(false);
        refillsPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        refillInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        refillInstruct.setForeground(new java.awt.Color(0, 0, 0));
        refillInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        refillInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Refills Panel.png"))); // NOI18N

        howMany.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        howMany.setForeground(new java.awt.Color(0, 0, 0));
        howMany.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        howMany.setIcon(new javax.swing.ImageIcon(getClass().getResource("/How Many.png"))); // NOI18N

        refillNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        refillNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        refillNumberIn.setForeground(new java.awt.Color(255, 232, 214));
        refillNumberIn.setPreferredSize(new java.awt.Dimension(350, 45));

        refillNextButton.setBackground(new java.awt.Color(107, 112, 92));
        refillNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        refillNextButton.setForeground(new java.awt.Color(221, 190, 169));
        refillNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        refillNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        refillNextButton.setBorderPainted(false);
        refillNextButton.setContentAreaFilled(false);
        refillNextButton.setEnabled(false);
        refillNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        refillNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        refillNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillNextButtonActionPerformed(evt);
            }
        });

        refillsYesNo.add(yesButton);
        yesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button.png"))); // NOI18N
        yesButton.setBorderPainted(false);
        yesButton.setContentAreaFilled(false);
        yesButton.setPreferredSize(new java.awt.Dimension(145, 45));
        yesButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button Clicked.png"))); // NOI18N
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });

        refillsBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        refillsBackButton.setBorderPainted(false);
        refillsBackButton.setContentAreaFilled(false);
        refillsBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        refillsBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        refillsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillsBackButtonActionPerformed(evt);
            }
        });

        refillsCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        refillsCancelButton.setBorderPainted(false);
        refillsCancelButton.setContentAreaFilled(false);
        refillsCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        refillsCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        refillsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillsCancelButtonActionPerformed(evt);
            }
        });

        refillsYesNo.add(noButton);
        noButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button.png"))); // NOI18N
        noButton.setBorderPainted(false);
        noButton.setContentAreaFilled(false);
        noButton.setPreferredSize(new java.awt.Dimension(145, 45));
        noButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button Clicked.png"))); // NOI18N
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout refillsPanelLayout = new javax.swing.GroupLayout(refillsPanel);
        refillsPanel.setLayout(refillsPanelLayout);
        refillsPanelLayout.setHorizontalGroup(
            refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refillsPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(refillsPanelLayout.createSequentialGroup()
                        .addComponent(refillsBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refillNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(refillInstruct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, refillsPanelLayout.createSequentialGroup()
                        .addComponent(yesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(howMany, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refillNumberIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(refillsPanelLayout.createSequentialGroup()
                        .addComponent(refillsCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)))
                .addGap(80, 80, 80))
        );
        refillsPanelLayout.setVerticalGroup(
            refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, refillsPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(refillInstruct, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(howMany)
                .addGap(0, 0, 0)
                .addComponent(refillNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refillsBackButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refillNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(refillsCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        howMany.setVisible(false);
        refillNumberIn.setVisible(false);

        medsPanel.add(refillsPanel, "card2");
        refillsPanel.setVisible(false);

        reminderPanel.setBackground(new java.awt.Color(255, 232, 214));
        reminderPanel.setForeground(new java.awt.Color(255, 232, 214));
        reminderPanel.setOpaque(false);
        reminderPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        reminderInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        reminderInstruct.setForeground(new java.awt.Color(0, 0, 0));
        reminderInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reminderInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Reminder Panel.png"))); // NOI18N

        remindNextButton.setBackground(new java.awt.Color(107, 112, 92));
        remindNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        remindNextButton.setForeground(new java.awt.Color(221, 190, 169));
        remindNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        remindNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        remindNextButton.setBorderPainted(false);
        remindNextButton.setContentAreaFilled(false);
        remindNextButton.setEnabled(false);
        remindNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        remindNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        remindNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remindNextButtonActionPerformed(evt);
            }
        });

        yesButtonR.setBackground(new java.awt.Color(107, 112, 92));
        yesButtonR.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        yesButtonR.setForeground(new java.awt.Color(255, 232, 214));
        yesButtonR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button.png"))); // NOI18N
        yesButtonR.setBorderPainted(false);
        yesButtonR.setContentAreaFilled(false);
        yesButtonR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        yesButtonR.setMaximumSize(new java.awt.Dimension(145, 45));
        yesButtonR.setPreferredSize(new java.awt.Dimension(145, 45));
        yesButtonR.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button Clicked.png"))); // NOI18N
        yesButtonR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonRActionPerformed(evt);
            }
        });

        noButtonR.setBackground(new java.awt.Color(107, 112, 92));
        noButtonR.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        noButtonR.setForeground(new java.awt.Color(255, 232, 214));
        noButtonR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button.png"))); // NOI18N
        noButtonR.setBorderPainted(false);
        noButtonR.setContentAreaFilled(false);
        noButtonR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        noButtonR.setPreferredSize(new java.awt.Dimension(145, 45));
        noButtonR.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button Clicked.png"))); // NOI18N
        noButtonR.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button Clicked.png"))); // NOI18N
        noButtonR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonRActionPerformed(evt);
            }
        });

        hourIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        hourIn.setModel(new javax.swing.SpinnerListModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
        hourIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        minuteIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        minuteIn.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        minuteIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ampmIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        ampmIn.setModel(new javax.swing.SpinnerListModel(new String[] {"A.M.", "P.M."}));

        reminderBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        reminderBackButton.setBorderPainted(false);
        reminderBackButton.setContentAreaFilled(false);
        reminderBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        reminderBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        reminderBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reminderBackButtonActionPerformed(evt);
            }
        });

        reminderCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        reminderCancelButton.setBorderPainted(false);
        reminderCancelButton.setContentAreaFilled(false);
        reminderCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        reminderCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        reminderCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reminderCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reminderPanelLayout = new javax.swing.GroupLayout(reminderPanel);
        reminderPanel.setLayout(reminderPanelLayout);
        reminderPanelLayout.setHorizontalGroup(
            reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reminderPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reminderInstruct)
                    .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(reminderPanelLayout.createSequentialGroup()
                            .addComponent(yesButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(reminderPanelLayout.createSequentialGroup()
                            .addComponent(hourIn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(54, 54, 54)
                            .addComponent(minuteIn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ampmIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(reminderPanelLayout.createSequentialGroup()
                            .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(reminderPanelLayout.createSequentialGroup()
                                    .addGap(50, 50, 50)
                                    .addComponent(reminderCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(reminderPanelLayout.createSequentialGroup()
                                    .addComponent(reminderBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(60, 60, 60)
                                    .addComponent(remindNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addGap(80, 80, 80))
        );
        reminderPanelLayout.setVerticalGroup(
            reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reminderPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(reminderInstruct, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yesButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hourIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ampmIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reminderBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remindNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(reminderCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);

        medsPanel.add(reminderPanel, "card2");
        refillsPanel.setVisible(false);

        requireAgainPanel.setBackground(new java.awt.Color(255, 232, 214));
        requireAgainPanel.setForeground(new java.awt.Color(255, 232, 214));
        requireAgainPanel.setOpaque(false);
        requireAgainPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        requireAgainInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        requireAgainInstruct.setForeground(new java.awt.Color(0, 0, 0));
        requireAgainInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        requireAgainInstruct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Require Again panel.png"))); // NOI18N

        requireAgainNextButton.setBackground(new java.awt.Color(107, 112, 92));
        requireAgainNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        requireAgainNextButton.setForeground(new java.awt.Color(221, 190, 169));
        requireAgainNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        requireAgainNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        requireAgainNextButton.setBorderPainted(false);
        requireAgainNextButton.setContentAreaFilled(false);
        requireAgainNextButton.setEnabled(false);
        requireAgainNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        requireAgainNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        requireAgainNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainNextButtonActionPerformed(evt);
            }
        });

        callMessage.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        callMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Doctor Remind Call.png"))); // NOI18N

        requireAgainBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        requireAgainBackButton.setBorderPainted(false);
        requireAgainBackButton.setContentAreaFilled(false);
        requireAgainBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        requireAgainBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        requireAgainBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainBackButtonActionPerformed(evt);
            }
        });

        requireAgainCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        requireAgainCancelButton.setBorderPainted(false);
        requireAgainCancelButton.setContentAreaFilled(false);
        requireAgainCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        requireAgainCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        requireAgainCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainCancelButtonActionPerformed(evt);
            }
        });

        requireAgainYesNo.add(yesButtonA);
        yesButtonA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button.png"))); // NOI18N
        yesButtonA.setBorderPainted(false);
        yesButtonA.setContentAreaFilled(false);
        yesButtonA.setPreferredSize(new java.awt.Dimension(145, 45));
        yesButtonA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button Clicked.png"))); // NOI18N
        yesButtonA.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Yes Button Clicked.png"))); // NOI18N
        yesButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonAActionPerformed(evt);
            }
        });

        requireAgainYesNo.add(noButtonA);
        noButtonA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button.png"))); // NOI18N
        noButtonA.setBorderPainted(false);
        noButtonA.setContentAreaFilled(false);
        noButtonA.setPreferredSize(new java.awt.Dimension(145, 45));
        noButtonA.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button Clicked.png"))); // NOI18N
        noButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout requireAgainPanelLayout = new javax.swing.GroupLayout(requireAgainPanel);
        requireAgainPanel.setLayout(requireAgainPanelLayout);
        requireAgainPanelLayout.setHorizontalGroup(
            requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requireAgainPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                        .addComponent(yesButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(callMessage, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(requireAgainInstruct, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                                .addComponent(requireAgainBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(requireAgainNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(80, 80, 80))
            .addGroup(requireAgainPanelLayout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addComponent(requireAgainCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        requireAgainPanelLayout.setVerticalGroup(
            requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(requireAgainInstruct)
                .addGap(0, 0, 0)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(callMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(requireAgainNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requireAgainBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(requireAgainCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        callMessage.setVisible(false);

        medsPanel.add(requireAgainPanel, "card2");
        refillsPanel.setVisible(false);

        confirmationPanel.setBackground(new java.awt.Color(255, 232, 214));
        confirmationPanel.setForeground(new java.awt.Color(255, 232, 214));
        confirmationPanel.setOpaque(false);
        confirmationPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        confirmButton.setBackground(new java.awt.Color(107, 112, 92));
        confirmButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        confirmButton.setForeground(new java.awt.Color(221, 190, 169));
        confirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        confirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        confirmButton.setBorderPainted(false);
        confirmButton.setContentAreaFilled(false);
        confirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        confirmButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        confirmCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        confirmCancelButton.setBorderPainted(false);
        confirmCancelButton.setContentAreaFilled(false);
        confirmCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        confirmCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        confirmCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmCancelButtonActionPerformed(evt);
            }
        });

        confirmBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        confirmBackButton.setBorderPainted(false);
        confirmBackButton.setContentAreaFilled(false);
        confirmBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        confirmBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        confirmBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBackButtonActionPerformed(evt);
            }
        });

        jScrollPane3.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 350));

        confirmList.setBackground(new java.awt.Color(107, 112, 92));
        confirmList.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        confirmList.setForeground(new java.awt.Color(255, 232, 214));
        confirmList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        confirmList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        confirmList.setPreferredSize(new java.awt.Dimension(275, 300));
        confirmList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        confirmList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        confirmList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                confirmListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(confirmList);

        javax.swing.GroupLayout confirmationPanelLayout = new javax.swing.GroupLayout(confirmationPanel);
        confirmationPanel.setLayout(confirmationPanelLayout);
        confirmationPanelLayout.setHorizontalGroup(
            confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmationPanelLayout.createSequentialGroup()
                .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(confirmationPanelLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(confirmationPanelLayout.createSequentialGroup()
                                .addComponent(confirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(confirmationPanelLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(confirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        confirmationPanelLayout.setVerticalGroup(
            confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, confirmationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(confirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        medsPanel.add(confirmationPanel, "card2");
        medNamePanel.setVisible(false);

        medInfoPanel.setBackground(new java.awt.Color(255, 232, 214));
        medInfoPanel.setForeground(new java.awt.Color(255, 232, 214));
        medInfoPanel.setOpaque(false);
        medInfoPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        medInfoBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        medInfoBackButton.setBorderPainted(false);
        medInfoBackButton.setContentAreaFilled(false);
        medInfoBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        medInfoBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        medInfoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medInfoBackButtonActionPerformed(evt);
            }
        });

        jScrollPane4.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(300, 350));

        medInfoList.setBackground(new java.awt.Color(107, 112, 92));
        medInfoList.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        medInfoList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        medInfoList.setForeground(new java.awt.Color(255, 232, 214));
        medInfoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        medInfoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        medInfoList.setPreferredSize(new java.awt.Dimension(350, 300));
        medInfoList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        medInfoList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        medInfoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medInfoListMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(medInfoList);

        medInfoEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button.png"))); // NOI18N
        medInfoEditButton.setBorderPainted(false);
        medInfoEditButton.setContentAreaFilled(false);
        medInfoEditButton.setPreferredSize(new java.awt.Dimension(145, 45));
        medInfoEditButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button Clicked.png"))); // NOI18N
        medInfoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medInfoEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout medInfoPanelLayout = new javax.swing.GroupLayout(medInfoPanel);
        medInfoPanel.setLayout(medInfoPanelLayout);
        medInfoPanelLayout.setHorizontalGroup(
            medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medInfoPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(medInfoPanelLayout.createSequentialGroup()
                        .addComponent(medInfoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(medInfoEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );
        medInfoPanelLayout.setVerticalGroup(
            medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, medInfoPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(medInfoBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(medInfoEditButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        medsPanel.add(medInfoPanel, "card2");
        medInfoPanel.setVisible(false);

        editMedPanel.setBackground(new java.awt.Color(255, 232, 214));
        editMedPanel.setForeground(new java.awt.Color(255, 232, 214));
        editMedPanel.setOpaque(false);
        editMedPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        editPanelConfirmButton.setBackground(new java.awt.Color(107, 112, 92));
        editPanelConfirmButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        editPanelConfirmButton.setForeground(new java.awt.Color(221, 190, 169));
        editPanelConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        editPanelConfirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editPanelConfirmButton.setBorderPainted(false);
        editPanelConfirmButton.setContentAreaFilled(false);
        editPanelConfirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        editPanelConfirmButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        editPanelConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPanelConfirmButtonActionPerformed(evt);
            }
        });

        editPanelBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        editPanelBackButton.setBorderPainted(false);
        editPanelBackButton.setContentAreaFilled(false);
        editPanelBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        editPanelBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        editPanelBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPanelBackButtonActionPerformed(evt);
            }
        });

        medNameEdit.setBackground(new java.awt.Color(107, 112, 92));
        medNameEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        medNameEdit.setForeground(new java.awt.Color(255, 232, 214));
        medNameEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        rxNumberEdit.setBackground(new java.awt.Color(107, 112, 92));
        rxNumberEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        rxNumberEdit.setForeground(new java.awt.Color(255, 232, 214));
        rxNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        dosageNumberEdit.setBackground(new java.awt.Color(107, 112, 92));
        dosageNumberEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        dosageNumberEdit.setForeground(new java.awt.Color(255, 232, 214));
        dosageNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        dosageTypeEdit.setBackground(new java.awt.Color(107, 112, 92));
        dosageTypeEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageTypeEdit.setForeground(new java.awt.Color(255, 232, 214));
        dosageTypeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MG", "Tablet(s)", "Capsule(s)", "ML", "Injection(s)", "Puff(s)", "Application(s)", "Drop(s)" }));
        dosageTypeEdit.setToolTipText("Select the type of dosage");

        frequencyNumberEdit.setBackground(new java.awt.Color(107, 112, 92));
        frequencyNumberEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        frequencyNumberEdit.setForeground(new java.awt.Color(255, 232, 214));
        frequencyNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        frequencyTypeEdit.setBackground(new java.awt.Color(107, 112, 92));
        frequencyTypeEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyTypeEdit.setForeground(new java.awt.Color(255, 232, 214));
        frequencyTypeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Day", "A Week", "A Month", "" }));
        frequencyTypeEdit.setToolTipText("");

        dosesProvEdit.setBackground(new java.awt.Color(107, 112, 92));
        dosesProvEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        dosesProvEdit.setForeground(new java.awt.Color(255, 232, 214));
        dosesProvEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        refillNumberEdit.setBackground(new java.awt.Color(107, 112, 92));
        refillNumberEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 14)); // NOI18N
        refillNumberEdit.setForeground(new java.awt.Color(255, 232, 214));
        refillNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(107, 112, 92));
        jLabel2.setText("X");

        jLabel3.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Medication Name");

        jLabel4.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Prescription Number");

        jLabel5.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Dosage");

        jLabel6.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Frequency");

        jLabel7.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Doses Provided");

        jLabel8.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Refills Available?");

        howManyLabel.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        howManyLabel.setForeground(new java.awt.Color(0, 0, 0));
        howManyLabel.setText("How Many?");

        jLabel10.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Reminders?");

        jLabel11.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Require Again?");

        hourInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        hourInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
        hourInEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        minuteInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        minuteInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        minuteInEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ampmInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        ampmInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"A.M.", "P.M."}));

        remindersYesNoEdit.add(yesButtonRemindEdit);
        yesButtonRemindEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button.png"))); // NOI18N
        yesButtonRemindEdit.setBorderPainted(false);
        yesButtonRemindEdit.setContentAreaFilled(false);
        yesButtonRemindEdit.setMaximumSize(new java.awt.Dimension(100, 100));
        yesButtonRemindEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        yesButtonRemindEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button Clicked.png"))); // NOI18N
        yesButtonRemindEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonRemindEditActionPerformed(evt);
            }
        });

        remindersYesNoEdit.add(noButtonRemindEdit);
        noButtonRemindEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button.png"))); // NOI18N
        noButtonRemindEdit.setBorderPainted(false);
        noButtonRemindEdit.setContentAreaFilled(false);
        noButtonRemindEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        noButtonRemindEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button Clicked.png"))); // NOI18N
        noButtonRemindEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonRemindEditActionPerformed(evt);
            }
        });

        refillsYesNoEdit.add(yesButtonRefillsEdit);
        yesButtonRefillsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button.png"))); // NOI18N
        yesButtonRefillsEdit.setBorderPainted(false);
        yesButtonRefillsEdit.setContentAreaFilled(false);
        yesButtonRefillsEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        yesButtonRefillsEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button Clicked.png"))); // NOI18N
        yesButtonRefillsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonRefillsEditActionPerformed(evt);
            }
        });

        refillsYesNoEdit.add(noButtonRefillsEdit);
        noButtonRefillsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button.png"))); // NOI18N
        noButtonRefillsEdit.setBorderPainted(false);
        noButtonRefillsEdit.setContentAreaFilled(false);
        noButtonRefillsEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        noButtonRefillsEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button Clicked.png"))); // NOI18N
        noButtonRefillsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonRefillsEditActionPerformed(evt);
            }
        });

        requireAgainYesNoEdit.add(yesButtonAgainEdit);
        yesButtonAgainEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button.png"))); // NOI18N
        yesButtonAgainEdit.setBorderPainted(false);
        yesButtonAgainEdit.setContentAreaFilled(false);
        yesButtonAgainEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        yesButtonAgainEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small Yes Button Clicked.png"))); // NOI18N
        yesButtonAgainEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonAgainEditActionPerformed(evt);
            }
        });

        requireAgainYesNoEdit.add(noButtonAgainEdit);
        noButtonAgainEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button.png"))); // NOI18N
        noButtonAgainEdit.setBorderPainted(false);
        noButtonAgainEdit.setContentAreaFilled(false);
        noButtonAgainEdit.setPreferredSize(new java.awt.Dimension(100, 25));
        noButtonAgainEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Small No Button Clicked.png"))); // NOI18N
        noButtonAgainEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonAgainEditActionPerformed(evt);
            }
        });

        reminderLabelEdit.setBackground(new java.awt.Color(0, 0, 0));
        reminderLabelEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 16)); // NOI18N
        reminderLabelEdit.setForeground(new java.awt.Color(0, 0, 0));
        reminderLabelEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reminderLabelEdit.setText("You Will Be Reminded");

        jLabel18.setFont(new java.awt.Font("Mongolian Baiti", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("To Call Your Doctor");

        javax.swing.GroupLayout editMedPanelLayout = new javax.swing.GroupLayout(editMedPanel);
        editMedPanel.setLayout(editMedPanelLayout);
        editMedPanelLayout.setHorizontalGroup(
            editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(howManyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reminderLabelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                        .addComponent(editPanelBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editPanelConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(frequencyNumberEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dosageNumberEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dosageTypeEdit, 0, 1, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(frequencyTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(rxNumberEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(medNameEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refillNumberEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dosesProvEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                        .addComponent(yesButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addComponent(hourInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(minuteInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ampmInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(yesButtonAgainEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yesButtonRemindEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noButtonRemindEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noButtonAgainEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editMedPanelLayout.setVerticalGroup(
            editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(medNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rxNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frequencyNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosesProvEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yesButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(noButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refillNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(howManyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(yesButtonRemindEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noButtonRemindEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hourInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ampmInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minuteInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yesButtonAgainEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(noButtonAgainEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reminderLabelEdit)
                .addGap(0, 0, 0)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editPanelBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPanelConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );

        refillNumberEdit.setVisible(false);
        howManyLabel.setVisible(false);
        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);
        reminderLabelEdit.setVisible(false);
        jLabel18.setVisible(false);

        medsPanel.add(editMedPanel, "card2");
        medInfoPanel.setVisible(false);

        middlePanel01.add(medsPanel, "card4");

        icePanel.setBackground(new java.awt.Color(255, 232, 214));
        icePanel.setLayout(new java.awt.CardLayout());

        contactListPanel.setBackground(new java.awt.Color(255, 232, 214));

        contactList.setBackground(new java.awt.Color(107, 112, 92));
        contactList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        contactList.setForeground(new java.awt.Color(255, 232, 214));
        contactList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        contactList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        contactList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        contactList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(contactList);

        doctorList.setBackground(new java.awt.Color(107, 112, 92));
        doctorList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        doctorList.setForeground(new java.awt.Color(255, 232, 214));
        doctorList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        doctorList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        doctorList.setMaximumSize(new java.awt.Dimension(350, 200));
        doctorList.setMinimumSize(new java.awt.Dimension(350, 200));
        doctorList.setPreferredSize(new java.awt.Dimension(350, 200));
        doctorList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        doctorList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        doctorList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doctorListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(doctorList);

        deleteContactButton.setBackground(new java.awt.Color(255, 51, 51));
        deleteContactButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        deleteContactButton.setForeground(new java.awt.Color(0, 0, 0));
        deleteContactButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Contact Button Clicked.png"))); // NOI18N
        deleteContactButton.setToolTipText("");
        deleteContactButton.setBorderPainted(false);
        deleteContactButton.setContentAreaFilled(false);
        deleteContactButton.setPreferredSize(new java.awt.Dimension(150, 45));
        deleteContactButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Contact Button.png"))); // NOI18N
        deleteContactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteContactButtonActionPerformed(evt);
            }
        });

        addContactButton.setBackground(new java.awt.Color(51, 204, 0));
        addContactButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        addContactButton.setForeground(new java.awt.Color(0, 0, 0));
        addContactButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add Contact Button Clicked.png"))); // NOI18N
        addContactButton.setToolTipText("");
        addContactButton.setBorderPainted(false);
        addContactButton.setContentAreaFilled(false);
        addContactButton.setPreferredSize(new java.awt.Dimension(150, 45));
        addContactButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Add Contact Button.png"))); // NOI18N
        addContactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContactButtonActionPerformed(evt);
            }
        });

        addDoctorButton.setBackground(new java.awt.Color(51, 204, 0));
        addDoctorButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        addDoctorButton.setForeground(new java.awt.Color(0, 0, 0));
        addDoctorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add Doctor Button Clicked.png"))); // NOI18N
        addDoctorButton.setToolTipText("");
        addDoctorButton.setBorderPainted(false);
        addDoctorButton.setContentAreaFilled(false);
        addDoctorButton.setPreferredSize(new java.awt.Dimension(150, 45));
        addDoctorButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Add Doctor Button.png"))); // NOI18N
        addDoctorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDoctorButtonActionPerformed(evt);
            }
        });

        deleteDoctorButton.setBackground(new java.awt.Color(255, 51, 51));
        deleteDoctorButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        deleteDoctorButton.setForeground(new java.awt.Color(0, 0, 0));
        deleteDoctorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Doctor Button Clicked.png"))); // NOI18N
        deleteDoctorButton.setToolTipText("");
        deleteDoctorButton.setBorderPainted(false);
        deleteDoctorButton.setContentAreaFilled(false);
        deleteDoctorButton.setMaximumSize(new java.awt.Dimension(150, 45));
        deleteDoctorButton.setPreferredSize(new java.awt.Dimension(150, 45));
        deleteDoctorButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete Doctor Button.png"))); // NOI18N
        deleteDoctorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDoctorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contactListPanelLayout = new javax.swing.GroupLayout(contactListPanel);
        contactListPanel.setLayout(contactListPanelLayout);
        contactListPanelLayout.setHorizontalGroup(
            contactListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactListPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(contactListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(contactListPanelLayout.createSequentialGroup()
                        .addComponent(deleteDoctorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addDoctorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(contactListPanelLayout.createSequentialGroup()
                        .addComponent(deleteContactButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addContactButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1))
                .addGap(80, 80, 80))
        );
        contactListPanelLayout.setVerticalGroup(
            contactListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactListPanelLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contactListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteContactButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addContactButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contactListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteDoctorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDoctorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        icePanel.add(contactListPanel, "card2");
        contactListPanel.setVisible(false);

        addNewContactPanel.setBackground(new java.awt.Color(255, 232, 214));

        addConNamePanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add Contact Panel.png"))); // NOI18N

        conNameIn.setBackground(new java.awt.Color(107, 112, 92));
        conNameIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conNameIn.setForeground(new java.awt.Color(255, 232, 214));
        conNameIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        conNameIn.setPreferredSize(new java.awt.Dimension(350, 45));

        conNameNextButton.setBackground(new java.awt.Color(0, 204, 51));
        conNameNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conNameNextButton.setForeground(new java.awt.Color(0, 0, 0));
        conNameNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        conNameNextButton.setBorderPainted(false);
        conNameNextButton.setContentAreaFilled(false);
        conNameNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conNameNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        conNameNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conNameNextButtonActionPerformed(evt);
            }
        });

        conNameBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conNameBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conNameBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conNameBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conNameBackButton.setBorderPainted(false);
        conNameBackButton.setContentAreaFilled(false);
        conNameBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conNameBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conNameBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conNameBackButtonActionPerformed(evt);
            }
        });

        conNameCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conNameCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conNameCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conNameCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conNameCancelButton.setBorderPainted(false);
        conNameCancelButton.setContentAreaFilled(false);
        conNameCancelButton.setDefaultCapable(false);
        conNameCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conNameCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conNameCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conNameCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addConNamePanelLayout = new javax.swing.GroupLayout(addConNamePanel);
        addConNamePanel.setLayout(addConNamePanelLayout);
        addConNamePanelLayout.setHorizontalGroup(
            addConNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConNamePanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(addConNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(addConNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(addConNamePanelLayout.createSequentialGroup()
                            .addComponent(conNameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(conNameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(conNameIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addConNamePanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(conNameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        addConNamePanelLayout.setVerticalGroup(
            addConNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConNamePanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel9)
                .addGap(50, 50, 50)
                .addComponent(conNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(addConNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conNameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conNameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(conNameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        addConPhonePanel.setBackground(new java.awt.Color(255, 232, 214));

        jPanel13.setBackground(new java.awt.Color(255, 232, 214));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add ConPhone Panel.png"))); // NOI18N

        conPhoneIn.setBackground(new java.awt.Color(107, 112, 92));
        conPhoneIn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        conPhoneIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        conPhoneIn.setPreferredSize(new java.awt.Dimension(350, 45));

        conPhoneBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conPhoneBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conPhoneBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conPhoneBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conPhoneBackButton.setBorderPainted(false);
        conPhoneBackButton.setContentAreaFilled(false);
        conPhoneBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conPhoneBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conPhoneBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conPhoneBackButtonActionPerformed(evt);
            }
        });

        conPhoneNextButton.setBackground(new java.awt.Color(0, 204, 51));
        conPhoneNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conPhoneNextButton.setForeground(new java.awt.Color(0, 0, 0));
        conPhoneNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        conPhoneNextButton.setBorderPainted(false);
        conPhoneNextButton.setContentAreaFilled(false);
        conPhoneNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conPhoneNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        conPhoneNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conPhoneNextButtonActionPerformed(evt);
            }
        });

        conPhoneCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conPhoneCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conPhoneCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conPhoneCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conPhoneCancelButton.setBorderPainted(false);
        conPhoneCancelButton.setContentAreaFilled(false);
        conPhoneCancelButton.setDefaultCapable(false);
        conPhoneCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conPhoneCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conPhoneCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conPhoneCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                            .addComponent(conPhoneBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(conPhoneNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(conPhoneIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(conPhoneCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel12)
                .addGap(50, 50, 50)
                .addComponent(conPhoneIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conPhoneNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conPhoneBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(conPhoneCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        javax.swing.GroupLayout addConPhonePanelLayout = new javax.swing.GroupLayout(addConPhonePanel);
        addConPhonePanel.setLayout(addConPhonePanelLayout);
        addConPhonePanelLayout.setHorizontalGroup(
            addConPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
            .addGroup(addConPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addConPhonePanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        addConPhonePanelLayout.setVerticalGroup(
            addConPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
            .addGroup(addConPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addConPhonePanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        addNewContactPanel.setVisible(false);

        addConEmailPanel.setBackground(new java.awt.Color(255, 232, 214));

        jPanel15.setBackground(new java.awt.Color(255, 232, 214));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add ConEmail Panel.png"))); // NOI18N

        conEmailIn.setBackground(new java.awt.Color(107, 112, 92));
        conEmailIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conEmailIn.setForeground(new java.awt.Color(255, 232, 214));
        conEmailIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        conEmailIn.setPreferredSize(new java.awt.Dimension(350, 45));

        conEmailBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conEmailBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEmailBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conEmailBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conEmailBackButton.setBorderPainted(false);
        conEmailBackButton.setContentAreaFilled(false);
        conEmailBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conEmailBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conEmailBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEmailBackButtonActionPerformed(evt);
            }
        });

        conEmailNextButton.setBackground(new java.awt.Color(0, 204, 51));
        conEmailNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEmailNextButton.setForeground(new java.awt.Color(0, 0, 0));
        conEmailNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        conEmailNextButton.setBorderPainted(false);
        conEmailNextButton.setContentAreaFilled(false);
        conEmailNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conEmailNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        conEmailNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEmailNextButtonActionPerformed(evt);
            }
        });

        conEmailCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conEmailCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEmailCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conEmailCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conEmailCancelButton.setBorderPainted(false);
        conEmailCancelButton.setContentAreaFilled(false);
        conEmailCancelButton.setDefaultCapable(false);
        conEmailCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conEmailCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conEmailCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEmailCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(conEmailIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(conEmailBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(conEmailNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(conEmailCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel13)
                .addGap(50, 50, 50)
                .addComponent(conEmailIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conEmailNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conEmailBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(conEmailCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        javax.swing.GroupLayout addConEmailPanelLayout = new javax.swing.GroupLayout(addConEmailPanel);
        addConEmailPanel.setLayout(addConEmailPanelLayout);
        addConEmailPanelLayout.setHorizontalGroup(
            addConEmailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addConEmailPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        addConEmailPanelLayout.setVerticalGroup(
            addConEmailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConEmailPanelLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        addNewContactPanel.setVisible(false);

        addConRelationshipPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add ConRelation Panel.png"))); // NOI18N

        conRelationIn.setBackground(new java.awt.Color(107, 112, 92));
        conRelationIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conRelationIn.setForeground(new java.awt.Color(255, 232, 214));
        conRelationIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        conRelationIn.setPreferredSize(new java.awt.Dimension(350, 45));

        conRelationBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conRelationBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conRelationBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conRelationBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conRelationBackButton.setBorderPainted(false);
        conRelationBackButton.setContentAreaFilled(false);
        conRelationBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conRelationBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conRelationBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conRelationBackButtonActionPerformed(evt);
            }
        });

        conRelationNextButton.setBackground(new java.awt.Color(0, 204, 51));
        conRelationNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conRelationNextButton.setForeground(new java.awt.Color(0, 0, 0));
        conRelationNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        conRelationNextButton.setBorderPainted(false);
        conRelationNextButton.setContentAreaFilled(false);
        conRelationNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conRelationNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        conRelationNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conRelationNextButtonActionPerformed(evt);
            }
        });

        conRelationCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conRelationCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conRelationCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conRelationCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conRelationCancelButton.setBorderPainted(false);
        conRelationCancelButton.setContentAreaFilled(false);
        conRelationCancelButton.setDefaultCapable(false);
        conRelationCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conRelationCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conRelationCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conRelationCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addConRelationshipPanelLayout = new javax.swing.GroupLayout(addConRelationshipPanel);
        addConRelationshipPanel.setLayout(addConRelationshipPanelLayout);
        addConRelationshipPanelLayout.setHorizontalGroup(
            addConRelationshipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConRelationshipPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(addConRelationshipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addConRelationshipPanelLayout.createSequentialGroup()
                        .addComponent(conRelationBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(conRelationNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addConRelationshipPanelLayout.createSequentialGroup()
                        .addGroup(addConRelationshipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(conRelationIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addConRelationshipPanelLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(conRelationCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        addConRelationshipPanelLayout.setVerticalGroup(
            addConRelationshipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addConRelationshipPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel14)
                .addGap(50, 50, 50)
                .addComponent(conRelationIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(addConRelationshipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conRelationNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conRelationBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(conRelationCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        addConConfirmPanel.setBackground(new java.awt.Color(255, 232, 214));

        contactConfirmList.setBackground(new java.awt.Color(107, 112, 92));
        contactConfirmList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        contactConfirmList.setForeground(new java.awt.Color(255, 232, 214));
        contactConfirmList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        contactConfirmList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactConfirmList.setPreferredSize(new java.awt.Dimension(470, 300));
        jScrollPane6.setViewportView(contactConfirmList);

        conConfirmBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conConfirmBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conConfirmBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conConfirmBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conConfirmBackButton.setBorderPainted(false);
        conConfirmBackButton.setContentAreaFilled(false);
        conConfirmBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conConfirmBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conConfirmBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conConfirmBackButtonActionPerformed(evt);
            }
        });

        conConfirmConfirmButton.setBackground(new java.awt.Color(0, 204, 51));
        conConfirmConfirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conConfirmConfirmButton.setForeground(new java.awt.Color(0, 0, 0));
        conConfirmConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        conConfirmConfirmButton.setBorderPainted(false);
        conConfirmConfirmButton.setContentAreaFilled(false);
        conConfirmConfirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conConfirmConfirmButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        conConfirmConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conConfirmConfirmButtonActionPerformed(evt);
            }
        });

        conConfirmCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conConfirmCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conConfirmCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conConfirmCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conConfirmCancelButton.setBorderPainted(false);
        conConfirmCancelButton.setContentAreaFilled(false);
        conConfirmCancelButton.setDefaultCapable(false);
        conConfirmCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conConfirmCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conConfirmCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conConfirmCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addConConfirmPanelLayout = new javax.swing.GroupLayout(addConConfirmPanel);
        addConConfirmPanel.setLayout(addConConfirmPanelLayout);
        addConConfirmPanelLayout.setHorizontalGroup(
            addConConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConConfirmPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(addConConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(conConfirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addConConfirmPanelLayout.createSequentialGroup()
                        .addComponent(conConfirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(conConfirmConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addConConfirmPanelLayout.setVerticalGroup(
            addConConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConConfirmPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(addConConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conConfirmConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conConfirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(conConfirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout addNewContactPanelLayout = new javax.swing.GroupLayout(addNewContactPanel);
        addNewContactPanel.setLayout(addNewContactPanelLayout);
        addNewContactPanelLayout.setHorizontalGroup(
            addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConPhonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConEmailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConRelationshipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        addNewContactPanelLayout.setVerticalGroup(
            addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 642, Short.MAX_VALUE)
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConPhonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConEmailPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConRelationshipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(addNewContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewContactPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(addConConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        addConNamePanel.setVisible(false);
        addConPhonePanel.setVisible(false);
        addConEmailPanel.setVisible(false);
        addConRelationshipPanel.setVisible(false);
        addConConfirmPanel.setVisible(false);

        icePanel.add(addNewContactPanel, "card2");
        addNewContactPanel.setVisible(false);

        addNewDocPanel.setBackground(new java.awt.Color(255, 232, 214));

        addDocNamePanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add DocName Panel.png"))); // NOI18N

        docNameIn.setBackground(new java.awt.Color(107, 112, 92));
        docNameIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        docNameIn.setForeground(new java.awt.Color(255, 232, 214));
        docNameIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        docNameIn.setPreferredSize(new java.awt.Dimension(350, 45));

        docNameNextButton.setBackground(new java.awt.Color(0, 204, 51));
        docNameNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docNameNextButton.setForeground(new java.awt.Color(0, 0, 0));
        docNameNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        docNameNextButton.setBorderPainted(false);
        docNameNextButton.setContentAreaFilled(false);
        docNameNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docNameNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/No Button Clicked.png"))); // NOI18N
        docNameNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docNameNextButtonActionPerformed(evt);
            }
        });

        docNameBackButton.setBackground(new java.awt.Color(153, 153, 153));
        docNameBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docNameBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docNameBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docNameBackButton.setBorderPainted(false);
        docNameBackButton.setContentAreaFilled(false);
        docNameBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docNameBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docNameBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docNameBackButtonActionPerformed(evt);
            }
        });

        docNameCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        docNameCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docNameCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        docNameCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        docNameCancelButton.setBorderPainted(false);
        docNameCancelButton.setContentAreaFilled(false);
        docNameCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        docNameCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        docNameCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docNameCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDocNamePanelLayout = new javax.swing.GroupLayout(addDocNamePanel);
        addDocNamePanel.setLayout(addDocNamePanelLayout);
        addDocNamePanelLayout.setHorizontalGroup(
            addDocNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocNamePanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(addDocNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(addDocNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addDocNamePanelLayout.createSequentialGroup()
                            .addComponent(docNameBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(docNameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(docNameIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addDocNamePanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(docNameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        addDocNamePanelLayout.setVerticalGroup(
            addDocNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocNamePanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel15)
                .addGap(50, 50, 50)
                .addComponent(docNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(addDocNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docNameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docNameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(docNameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        addDocPhonePanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add DocPhone Panel.png"))); // NOI18N

        docPhoneIn.setBackground(new java.awt.Color(107, 112, 92));
        docPhoneIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        docPhoneIn.setForeground(new java.awt.Color(255, 232, 214));
        docPhoneIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        docPhoneIn.setPreferredSize(new java.awt.Dimension(350, 45));

        docPhoneNextButton.setBackground(new java.awt.Color(0, 204, 51));
        docPhoneNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docPhoneNextButton.setForeground(new java.awt.Color(0, 0, 0));
        docPhoneNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        docPhoneNextButton.setBorderPainted(false);
        docPhoneNextButton.setContentAreaFilled(false);
        docPhoneNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docPhoneNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        docPhoneNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docPhoneNextButtonActionPerformed(evt);
            }
        });

        docPhoneBackButton.setBackground(new java.awt.Color(153, 153, 153));
        docPhoneBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docPhoneBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docPhoneBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docPhoneBackButton.setBorderPainted(false);
        docPhoneBackButton.setContentAreaFilled(false);
        docPhoneBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docPhoneBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docPhoneBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docPhoneBackButtonActionPerformed(evt);
            }
        });

        docPhoneCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        docPhoneCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docPhoneCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        docPhoneCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        docPhoneCancelButton.setBorderPainted(false);
        docPhoneCancelButton.setContentAreaFilled(false);
        docPhoneCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        docPhoneCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        docPhoneCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docPhoneCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDocPhonePanelLayout = new javax.swing.GroupLayout(addDocPhonePanel);
        addDocPhonePanel.setLayout(addDocPhonePanelLayout);
        addDocPhonePanelLayout.setHorizontalGroup(
            addDocPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocPhonePanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(addDocPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(addDocPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addDocPhonePanelLayout.createSequentialGroup()
                            .addComponent(docPhoneBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(docPhoneNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(docPhoneIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addDocPhonePanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(docPhoneCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        addDocPhonePanelLayout.setVerticalGroup(
            addDocPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocPhonePanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel16)
                .addGap(50, 50, 50)
                .addComponent(docPhoneIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(addDocPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docPhoneNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docPhoneBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(docPhoneCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        addDocSpecPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add DocSpec Panel.png"))); // NOI18N

        docSpecIn.setBackground(new java.awt.Color(107, 112, 92));
        docSpecIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        docSpecIn.setForeground(new java.awt.Color(255, 232, 214));
        docSpecIn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        docSpecIn.setPreferredSize(new java.awt.Dimension(350, 45));

        docSpecNextButton.setBackground(new java.awt.Color(0, 204, 51));
        docSpecNextButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docSpecNextButton.setForeground(new java.awt.Color(0, 0, 0));
        docSpecNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button.png"))); // NOI18N
        docSpecNextButton.setBorderPainted(false);
        docSpecNextButton.setContentAreaFilled(false);
        docSpecNextButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docSpecNextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Next Button Clicked.png"))); // NOI18N
        docSpecNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docSpecNextButtonActionPerformed(evt);
            }
        });

        docSpecBackButton.setBackground(new java.awt.Color(153, 153, 153));
        docSpecBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docSpecBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docSpecBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docSpecBackButton.setBorderPainted(false);
        docSpecBackButton.setContentAreaFilled(false);
        docSpecBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docSpecBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docSpecBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docSpecBackButtonActionPerformed(evt);
            }
        });

        docSpecCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        docSpecCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docSpecCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        docSpecCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        docSpecCancelButton.setBorderPainted(false);
        docSpecCancelButton.setContentAreaFilled(false);
        docSpecCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        docSpecCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        docSpecCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docSpecCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addDocSpecPanelLayout = new javax.swing.GroupLayout(addDocSpecPanel);
        addDocSpecPanel.setLayout(addDocSpecPanelLayout);
        addDocSpecPanelLayout.setHorizontalGroup(
            addDocSpecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocSpecPanelLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(addDocSpecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(addDocSpecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addDocSpecPanelLayout.createSequentialGroup()
                            .addComponent(docSpecBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(docSpecNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(docSpecIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addDocSpecPanelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(docSpecCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80))
        );
        addDocSpecPanelLayout.setVerticalGroup(
            addDocSpecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocSpecPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel17)
                .addGap(50, 50, 50)
                .addComponent(docSpecIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(addDocSpecPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docSpecNextButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docSpecBackButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(docSpecCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        addDocConfirmPanel.setBackground(new java.awt.Color(255, 232, 214));

        docConfirmBackButton.setBackground(new java.awt.Color(153, 153, 153));
        docConfirmBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docConfirmBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docConfirmBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docConfirmBackButton.setBorderPainted(false);
        docConfirmBackButton.setContentAreaFilled(false);
        docConfirmBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docConfirmBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docConfirmBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docConfirmBackButtonActionPerformed(evt);
            }
        });

        docConfirmConfirmButton.setBackground(new java.awt.Color(0, 204, 51));
        docConfirmConfirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docConfirmConfirmButton.setForeground(new java.awt.Color(0, 0, 0));
        docConfirmConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        docConfirmConfirmButton.setBorderPainted(false);
        docConfirmConfirmButton.setContentAreaFilled(false);
        docConfirmConfirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docConfirmConfirmButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        docConfirmConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docConfirmConfirmButtonActionPerformed(evt);
            }
        });

        docConfirmCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        docConfirmCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docConfirmCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        docConfirmCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        docConfirmCancelButton.setBorderPainted(false);
        docConfirmCancelButton.setContentAreaFilled(false);
        docConfirmCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        docConfirmCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        docConfirmCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docConfirmCancelButtonActionPerformed(evt);
            }
        });

        docConfirmList.setBackground(new java.awt.Color(107, 112, 92));
        docConfirmList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        docConfirmList.setForeground(new java.awt.Color(255, 232, 214));
        docConfirmList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        docConfirmList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        docConfirmList.setPreferredSize(new java.awt.Dimension(470, 300));
        jScrollPane8.setViewportView(docConfirmList);

        javax.swing.GroupLayout addDocConfirmPanelLayout = new javax.swing.GroupLayout(addDocConfirmPanel);
        addDocConfirmPanel.setLayout(addDocConfirmPanelLayout);
        addDocConfirmPanelLayout.setHorizontalGroup(
            addDocConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocConfirmPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(addDocConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(docConfirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addDocConfirmPanelLayout.createSequentialGroup()
                        .addComponent(docConfirmBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(docConfirmConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        addDocConfirmPanelLayout.setVerticalGroup(
            addDocConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addDocConfirmPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addGroup(addDocConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docConfirmConfirmButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docConfirmBackButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(docConfirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addNewDocPanelLayout = new javax.swing.GroupLayout(addNewDocPanel);
        addNewDocPanel.setLayout(addNewDocPanelLayout);
        addNewDocPanelLayout.setHorizontalGroup(
            addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addDocNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addDocPhonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addDocConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addDocSpecPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)))
        );
        addNewDocPanelLayout.setVerticalGroup(
            addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 642, Short.MAX_VALUE)
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addDocNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addDocPhonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addDocConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(addNewDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addNewDocPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addDocSpecPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)))
        );

        addDocNamePanel.setVisible(false);
        addDocPhonePanel.setVisible(false);
        addDocSpecPanel.setVisible(false);
        addDocConfirmPanel.setVisible(false);

        icePanel.add(addNewDocPanel, "card2");
        addNewContactPanel.setVisible(false);

        contactInfoPanel.setBackground(new java.awt.Color(255, 232, 214));
        contactInfoPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        contactInfoList.setBackground(new java.awt.Color(107, 112, 92));
        contactInfoList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        contactInfoList.setForeground(new java.awt.Color(255, 232, 214));
        contactInfoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        contactInfoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactInfoList.setPreferredSize(new java.awt.Dimension(470, 300));
        jScrollPane10.setViewportView(contactInfoList);

        contactEditButton.setBackground(new java.awt.Color(0, 204, 51));
        contactEditButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        contactEditButton.setForeground(new java.awt.Color(0, 0, 0));
        contactEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button.png"))); // NOI18N
        contactEditButton.setBorderPainted(false);
        contactEditButton.setContentAreaFilled(false);
        contactEditButton.setPreferredSize(new java.awt.Dimension(145, 45));
        contactEditButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button Clicked.png"))); // NOI18N
        contactEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactEditButtonActionPerformed(evt);
            }
        });

        contactInfoBackButton.setBackground(new java.awt.Color(255, 51, 51));
        contactInfoBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        contactInfoBackButton.setForeground(new java.awt.Color(0, 0, 0));
        contactInfoBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        contactInfoBackButton.setBorderPainted(false);
        contactInfoBackButton.setContentAreaFilled(false);
        contactInfoBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        contactInfoBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        contactInfoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactInfoBackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contactInfoPanelLayout = new javax.swing.GroupLayout(contactInfoPanel);
        contactInfoPanel.setLayout(contactInfoPanelLayout);
        contactInfoPanelLayout.setHorizontalGroup(
            contactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactInfoPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(contactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(contactInfoPanelLayout.createSequentialGroup()
                        .addComponent(contactInfoBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(contactEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        contactInfoPanelLayout.setVerticalGroup(
            contactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactInfoPanelLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(contactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactInfoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(147, 147, 147))
        );

        icePanel.add(contactInfoPanel, "card2");
        addNewContactPanel.setVisible(false);

        doctorInfoPanel.setBackground(new java.awt.Color(255, 232, 214));

        doctorInfoList.setBackground(new java.awt.Color(107, 112, 92));
        doctorInfoList.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        doctorInfoList.setForeground(new java.awt.Color(255, 232, 214));
        doctorInfoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        doctorInfoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        doctorInfoList.setPreferredSize(new java.awt.Dimension(470, 300));
        jScrollPane11.setViewportView(doctorInfoList);

        docEditButton.setBackground(new java.awt.Color(0, 204, 51));
        docEditButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docEditButton.setForeground(new java.awt.Color(0, 0, 0));
        docEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button.png"))); // NOI18N
        docEditButton.setBorderPainted(false);
        docEditButton.setContentAreaFilled(false);
        docEditButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docEditButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Edit Button Clicked.png"))); // NOI18N
        docEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docEditButtonActionPerformed(evt);
            }
        });

        docInfoBackButton.setBackground(new java.awt.Color(255, 51, 51));
        docInfoBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docInfoBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docInfoBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docInfoBackButton.setBorderPainted(false);
        docInfoBackButton.setContentAreaFilled(false);
        docInfoBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docInfoBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docInfoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docInfoBackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout doctorInfoPanelLayout = new javax.swing.GroupLayout(doctorInfoPanel);
        doctorInfoPanel.setLayout(doctorInfoPanelLayout);
        doctorInfoPanelLayout.setHorizontalGroup(
            doctorInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorInfoPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(doctorInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(doctorInfoPanelLayout.createSequentialGroup()
                        .addComponent(docInfoBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(docEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        doctorInfoPanelLayout.setVerticalGroup(
            doctorInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(doctorInfoPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(doctorInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docInfoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(136, Short.MAX_VALUE))
        );

        icePanel.add(doctorInfoPanel, "card2");
        addNewContactPanel.setVisible(false);

        editContactPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel19.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Contact Name: ");

        jLabel20.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Phone Number:");

        jLabel21.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Email:");

        jLabel22.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Relationship:");

        conNameEdit.setBackground(new java.awt.Color(107, 112, 92));
        conNameEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conNameEdit.setForeground(new java.awt.Color(255, 232, 214));
        conNameEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        conPhoneEdit.setBackground(new java.awt.Color(107, 112, 92));
        conPhoneEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conPhoneEdit.setForeground(new java.awt.Color(255, 232, 214));
        conPhoneEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        conEmailEdit.setBackground(new java.awt.Color(107, 112, 92));
        conEmailEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conEmailEdit.setForeground(new java.awt.Color(255, 232, 214));
        conEmailEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        conRelationEdit.setBackground(new java.awt.Color(107, 112, 92));
        conRelationEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        conRelationEdit.setForeground(new java.awt.Color(255, 232, 214));
        conRelationEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        conEditBackButton.setBackground(new java.awt.Color(153, 153, 153));
        conEditBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEditBackButton.setForeground(new java.awt.Color(0, 0, 0));
        conEditBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        conEditBackButton.setBorderPainted(false);
        conEditBackButton.setContentAreaFilled(false);
        conEditBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conEditBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        conEditBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEditBackButtonActionPerformed(evt);
            }
        });

        conEditConfirmButton.setBackground(new java.awt.Color(0, 204, 51));
        conEditConfirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEditConfirmButton.setForeground(new java.awt.Color(0, 0, 0));
        conEditConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        conEditConfirmButton.setBorderPainted(false);
        conEditConfirmButton.setContentAreaFilled(false);
        conEditConfirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        conEditConfirmButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        conEditConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEditConfirmButtonActionPerformed(evt);
            }
        });

        conEditCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        conEditCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        conEditCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        conEditCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        conEditCancelButton.setBorderPainted(false);
        conEditCancelButton.setContentAreaFilled(false);
        conEditCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        conEditCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        conEditCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conEditCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editContactPanelLayout = new javax.swing.GroupLayout(editContactPanel);
        editContactPanel.setLayout(editContactPanelLayout);
        editContactPanelLayout.setHorizontalGroup(
            editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editContactPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(36, 36, 36)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conNameEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conPhoneEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conEmailEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conRelationEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editContactPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(editContactPanelLayout.createSequentialGroup()
                        .addComponent(conEditBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(conEditConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(conEditCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83))
        );
        editContactPanelLayout.setVerticalGroup(
            editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editContactPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(conNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(conPhoneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(conEmailEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(conRelationEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addGroup(editContactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conEditBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conEditConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(conEditCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        icePanel.add(editContactPanel, "card2");
        addNewContactPanel.setVisible(false);

        editDocPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel23.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Doctor Name: ");

        jLabel24.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Phone Number:");

        jLabel26.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Specialty:");

        docNameEdit.setBackground(new java.awt.Color(107, 112, 92));
        docNameEdit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docNameEdit.setForeground(new java.awt.Color(255, 232, 214));
        docNameEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        docPhoneEdit.setBackground(new java.awt.Color(107, 112, 92));
        docPhoneEdit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docPhoneEdit.setForeground(new java.awt.Color(255, 232, 214));
        docPhoneEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        docSpecEdit.setBackground(new java.awt.Color(107, 112, 92));
        docSpecEdit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docSpecEdit.setForeground(new java.awt.Color(255, 232, 214));
        docSpecEdit.setPreferredSize(new java.awt.Dimension(300, 45));

        docEditBackButton.setBackground(new java.awt.Color(153, 153, 153));
        docEditBackButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docEditBackButton.setForeground(new java.awt.Color(0, 0, 0));
        docEditBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button.png"))); // NOI18N
        docEditBackButton.setBorderPainted(false);
        docEditBackButton.setContentAreaFilled(false);
        docEditBackButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docEditBackButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Back Button Clicked.png"))); // NOI18N
        docEditBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docEditBackButtonActionPerformed(evt);
            }
        });

        docEditConfirmButton.setBackground(new java.awt.Color(0, 204, 51));
        docEditConfirmButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docEditConfirmButton.setForeground(new java.awt.Color(0, 0, 0));
        docEditConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button.png"))); // NOI18N
        docEditConfirmButton.setBorderPainted(false);
        docEditConfirmButton.setContentAreaFilled(false);
        docEditConfirmButton.setPreferredSize(new java.awt.Dimension(145, 45));
        docEditConfirmButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Confirm Button Clicked.png"))); // NOI18N
        docEditConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docEditConfirmButtonActionPerformed(evt);
            }
        });

        docEditCancelButton.setBackground(new java.awt.Color(255, 51, 51));
        docEditCancelButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        docEditCancelButton.setForeground(new java.awt.Color(0, 0, 0));
        docEditCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button.png"))); // NOI18N
        docEditCancelButton.setBorderPainted(false);
        docEditCancelButton.setContentAreaFilled(false);
        docEditCancelButton.setPreferredSize(new java.awt.Dimension(250, 45));
        docEditCancelButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Cancel Button Clicked.png"))); // NOI18N
        docEditCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docEditCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editDocPanelLayout = new javax.swing.GroupLayout(editDocPanel);
        editDocPanel.setLayout(editDocPanelLayout);
        editDocPanelLayout.setHorizontalGroup(
            editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editDocPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docNameEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docPhoneEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docSpecEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editDocPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(editDocPanelLayout.createSequentialGroup()
                        .addComponent(docEditBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(docEditConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(docEditCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
        );
        editDocPanelLayout.setVerticalGroup(
            editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editDocPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(docNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(docPhoneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(docSpecEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(editDocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(docEditBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docEditConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(docEditCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );

        icePanel.add(editDocPanel, "card2");
        addNewContactPanel.setVisible(false);

        middlePanel01.add(icePanel, "card4");

        profileOptionsPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Panel.png"))); // NOI18N

        javax.swing.GroupLayout profileOptionsPanelLayout = new javax.swing.GroupLayout(profileOptionsPanel);
        profileOptionsPanel.setLayout(profileOptionsPanelLayout);
        profileOptionsPanelLayout.setHorizontalGroup(
            profileOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profileOptionsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel28)
                .addGap(0, 0, 0))
        );
        profileOptionsPanelLayout.setVerticalGroup(
            profileOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileOptionsPanelLayout.createSequentialGroup()
                .addComponent(jLabel28)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        middlePanel01.add(profileOptionsPanel, "card2");
        welcomePanel.setVisible(true);

        upcomingRemindersPanel.setBackground(new java.awt.Color(255, 232, 214));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Panel.png"))); // NOI18N

        javax.swing.GroupLayout upcomingRemindersPanelLayout = new javax.swing.GroupLayout(upcomingRemindersPanel);
        upcomingRemindersPanel.setLayout(upcomingRemindersPanelLayout);
        upcomingRemindersPanelLayout.setHorizontalGroup(
            upcomingRemindersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upcomingRemindersPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(0, 0, 0))
        );
        upcomingRemindersPanelLayout.setVerticalGroup(
            upcomingRemindersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upcomingRemindersPanelLayout.createSequentialGroup()
                .addComponent(jLabel29)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        middlePanel01.add(upcomingRemindersPanel, "card2");
        welcomePanel.setVisible(true);

        topPanel.setBackground(new java.awt.Color(255, 232, 214));
        topPanel.setPreferredSize(new java.awt.Dimension(510, 200));

        titleBar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Title Bar.png"))); // NOI18N
        titleBar.setAlignmentY(0.0F);
        titleBar.setIconTextGap(0);

        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Unclicked.png"))); // NOI18N
        myMedsButton.setBorderPainted(false);
        buttonBar.add(myMedsButton);
        myMedsButton.setContentAreaFilled(false);
        myMedsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myMedsButtonActionPerformed(evt);
            }
        });

        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Unclicked.png"))); // NOI18N
        upcomingRemindersButton.setBorderPainted(false);
        buttonBar.add(upcomingRemindersButton);
        upcomingRemindersButton.setContentAreaFilled(false);
        upcomingRemindersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upcomingRemindersButtonActionPerformed(evt);
            }
        });

        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Unclicked.png"))); // NOI18N
        iceButton.setBorderPainted(false);
        buttonBar.add(iceButton);
        iceButton.setContentAreaFilled(false);
        iceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iceButtonActionPerformed(evt);
            }
        });

        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Unclicked.png"))); // NOI18N
        profileOptionsButton.setBorderPainted(false);
        buttonBar.add(profileOptionsButton);
        profileOptionsButton.setContentAreaFilled(false);
        profileOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileOptionsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(myMedsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upcomingRemindersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profileOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(48, 48, 48))
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileOptionsButton)
                    .addComponent(iceButton)
                    .addComponent(upcomingRemindersButton)
                    .addComponent(myMedsButton))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(middlePanel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(middlePanel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void profileOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileOptionsButtonActionPerformed
        toProfileOptions();
    }//GEN-LAST:event_profileOptionsButtonActionPerformed

    private void addNewMedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewMedButtonActionPerformed
       
        myMedsListPanel.setVisible(false);
        medNamePanel.setVisible(true);
        medNameIn.requestFocus();
        
    }//GEN-LAST:event_addNewMedButtonActionPerformed

    private void nameNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameNextButtonActionPerformed
            if(DLM.contains(medNameIn.getText()) && medInfoEditButton.isSelected() == false){
                JOptionPane.showMessageDialog(this, "This medication name already exists.\n" + 
                "Please enter another medication name\nOR\n" +
                "Enter a number after the medication name", "", 
                JOptionPane.WARNING_MESSAGE);
            }else{
                getMedName();
                medNamePanel.setVisible(false);
                rxNumberPanel.setVisible(true);
                rxNumberIn.requestFocus();

            }
        
    }//GEN-LAST:event_nameNextButtonActionPerformed

    
    private void rxNumberNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberNextButtonActionPerformed
        if(rxNumberIn.getText().isBlank()){
           selection = JOptionPane.showConfirmDialog(this, "Are you sure you don't want to\n"
                    + "enter a prescription number?", "", JOptionPane.YES_NO_OPTION);
           if(selection == 0){
               rxNumberPanel.setVisible(false);
               dosagePanel.setVisible(true);
               dosageNumberIn.requestFocus();
           }
        }else{
            getRXNumber();
            rxNumberPanel.setVisible(false);
            dosagePanel.setVisible(true);
            dosageNumberIn.requestFocus();
        }
    }//GEN-LAST:event_rxNumberNextButtonActionPerformed

    private void dosageNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageNextButtonActionPerformed
        
        getDosageNumber();
        getDosageType();
        dosagePanel.setVisible(false);
        frequencyPanel.setVisible(true);
        frequencyNumberIn.requestFocus();
            
    }//GEN-LAST:event_dosageNextButtonActionPerformed

    private void frequencyNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyNextButtonActionPerformed
   
        getFrequencyNumber();
        getFrequencyType();

        frequencyPanel.setVisible(false);
        dosesPanel.setVisible(true);
        dosesProvIn.requestFocus();
               
    }//GEN-LAST:event_frequencyNextButtonActionPerformed

    private void dosesProvNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvNextButtonActionPerformed
        
        getDosesProv();
        dosesPanel.setVisible(false);
        refillsPanel.setVisible(true);
        
    }//GEN-LAST:event_dosesProvNextButtonActionPerformed

    private void refillNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillNextButtonActionPerformed
        if(yesButton.isSelected() == true){
            yesButton.setSelected(true);
        }else if(noButton.isSelected() == true){
            noButton.setSelected(true);
        }
        getRefills(refills);
        refillsPanel.setVisible(false);
        reminderPanel.setVisible(true);
    }//GEN-LAST:event_refillNextButtonActionPerformed

    private void remindNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remindNextButtonActionPerformed
        getReminder();
        setReminder();
        reminderPanel.setVisible(false);
        requireAgainPanel.setVisible(true);
    }//GEN-LAST:event_remindNextButtonActionPerformed
    /*
    the changeColor method is only an attempt to change the look and feel 
    of the spinner items. It is hardly noticeable but it is there
    */
    private void changeColor(){
        hourIn.setBackground(new java.awt.Color(107, 112, 92));
        hourIn.setForeground(new java.awt.Color(107, 112, 92));
        minuteIn.setBackground(new java.awt.Color(107, 112, 92));
        minuteIn.setForeground(new java.awt.Color(107, 112, 92));
        ampmIn.setBackground(new java.awt.Color(107, 112, 92));
        ampmIn.setForeground(new java.awt.Color(107, 112, 92));
    }
    private void requireAgainNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainNextButtonActionPerformed
        getAgain(again);
        if(confirmBackButton.isSelected() == false){
            setConList( medName,  rxNumber,  dosageNumber,  dosageType,
                  frequencyNumber,  frequencyType,  dosesProv,  refills, 
                  refillNumber,  remind,  again,  reminder);
            confirmList.setModel(CL);
        }
        requireAgainPanel.setVisible(false);
        confirmationPanel.setVisible(true);
    }//GEN-LAST:event_requireAgainNextButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
    
        updateDatabase(createSQLString(medName,rxNumber, dosageNumber, dosageType,
            frequencyNumber, frequencyType, dosesProv, refills, 
            refillNumber, remind, again, reminder));
        
        confirmationPanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        String med = medName;
        DLM.addElement(medName);
        medList.setModel(DLM);
        
        cancelAction();
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void confirmListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmListMouseClicked

    private void nameBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameBackButtonActionPerformed
        medNamePanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        MIL.clear();
        medInfoList.setModel(MIL);
    }//GEN-LAST:event_nameBackButtonActionPerformed

    private void nameCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameCancelButtonActionPerformed

        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            medNamePanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_nameCancelButtonActionPerformed

    private void rxNumberBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberBackButtonActionPerformed
        rxNumberPanel.setVisible(false);
        medNamePanel.setVisible(true);
    }//GEN-LAST:event_rxNumberBackButtonActionPerformed

    private void rxNumberCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberCancelButtonActionPerformed
        
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            rxNumberPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_rxNumberCancelButtonActionPerformed

    private void dosageBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageBackButtonActionPerformed
        dosagePanel.setVisible(false);
        rxNumberPanel.setVisible(true);
    }//GEN-LAST:event_dosageBackButtonActionPerformed

    private void dosageCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            dosagePanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_dosageCancelButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        
        
        
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to\n" + "delete this medication?\n" + "This cannot be undone.", "", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            
            JOptionPane.showMessageDialog(this, medList.getSelectedValue() + " has been deleted.", "", JOptionPane.WARNING_MESSAGE);
            
            deleteItem();
            
   
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void frequencyBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyBackButtonActionPerformed
        frequencyPanel.setVisible(false);
        dosagePanel.setVisible(true);
    }//GEN-LAST:event_frequencyBackButtonActionPerformed

    private void frequencyCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            frequencyPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            dosageNumberIn.setText("");
            rxNumberIn.setText("");
            medNameIn.setText("");
            frequencyNumberIn.setText("");
            frequencyTypeIn.setSelectedIndex(0);
        }
    }//GEN-LAST:event_frequencyCancelButtonActionPerformed

    private void dosesProvBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvBackButtonActionPerformed
        dosesPanel.setVisible(false);
        frequencyPanel.setVisible(true);
    }//GEN-LAST:event_dosesProvBackButtonActionPerformed

    private void dosesProvCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
        
            dosesPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_dosesProvCancelButtonActionPerformed

    private void refillsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillsBackButtonActionPerformed
        refillsPanel.setVisible(false);
        dosesPanel.setVisible(true);
    }//GEN-LAST:event_refillsBackButtonActionPerformed

    private void refillsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillsCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            refillsPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_refillsCancelButtonActionPerformed

    private void reminderCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reminderCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            reminderPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
        
    }//GEN-LAST:event_reminderCancelButtonActionPerformed

    private void reminderBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reminderBackButtonActionPerformed
        reminderPanel.setVisible(false);
        refillsPanel.setVisible(true);
    }//GEN-LAST:event_reminderBackButtonActionPerformed

    private void requireAgainCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            requireAgainPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
            
        }
    }//GEN-LAST:event_requireAgainCancelButtonActionPerformed

    private void requireAgainBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainBackButtonActionPerformed
        
        requireAgainPanel.setVisible(false);
        reminderPanel.setVisible(true);
    }//GEN-LAST:event_requireAgainBackButtonActionPerformed

    private void confirmCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            confirmationPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();

            
        }
    }//GEN-LAST:event_confirmCancelButtonActionPerformed

    private void confirmBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBackButtonActionPerformed
        confirmBackButton.setSelected(true);
        confirmationPanel.setVisible(false);
        requireAgainPanel.setVisible(true);
        
    }//GEN-LAST:event_confirmBackButtonActionPerformed

    private void medInfoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medInfoBackButtonActionPerformed
        medInfoPanel.setVisible(false);
        MIL.removeAllElements();
        myMedsListPanel.setVisible(true);
    }//GEN-LAST:event_medInfoBackButtonActionPerformed

    private void medInfoListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medInfoListMouseClicked
        int selected = medInfoList.getSelectedIndex();
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            switch(selected){
                case 0: //set up editing function here
            }
        }
    }//GEN-LAST:event_medInfoListMouseClicked

    private void medListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medListMouseClicked
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            myMedsListPanel.setVisible(false);
            medInfoPanel.setVisible(true);
            getMedInfo();
        }
    }//GEN-LAST:event_medListMouseClicked

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        myMedsListPanel.setVisible(false);
        medInfoPanel.setVisible(true);
        getMedInfo();
        
    }//GEN-LAST:event_editButtonActionPerformed

    private void medInfoEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medInfoEditButtonActionPerformed
        medInfoPanel.setVisible(false);
        
        editMedPanel.setVisible(true);
        try{
        editMedInfo();
                }catch(Exception e){
                    
                }
    }//GEN-LAST:event_medInfoEditButtonActionPerformed

    private void editPanelConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPanelConfirmButtonActionPerformed
        createUpdateString(medName, newRefillsBool, newReminders, newReminderTimes);
        updateDatabase(sqlString);
        DLM.removeAllElements();
        medList.setModel(DLM);
        MIL.removeAllElements();
        medInfoList.setModel(MIL);
        updateList();
        editMedPanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        
    }//GEN-LAST:event_editPanelConfirmButtonActionPerformed

    private void editPanelBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPanelBackButtonActionPerformed
        CL.removeAllElements();
        confirmList.setModel(CL);
        MIL.removeAllElements();
        medInfoList.setModel(MIL);
        editMedPanel.setVisible(false);
        myMedsListPanel.setVisible(true);
    }//GEN-LAST:event_editPanelBackButtonActionPerformed

    private void yesButtonRemindEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonRemindEditActionPerformed
        
        noButtonRemindEdit.setSelected(false);
        if(yesButtonRemindEdit.isSelected() == true){
            newReminders = "Yes";
        }
        hourInEdit.setVisible(true);
        minuteInEdit.setVisible(true);
        ampmInEdit.setVisible(true);
    }//GEN-LAST:event_yesButtonRemindEditActionPerformed

    private void noButtonRemindEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonRemindEditActionPerformed
        
        yesButtonRemindEdit.setSelected(false);
        if(noButtonRemindEdit.isSelected() == true){
            newReminders = "No";
        }
        hourInEdit.setVisible(false);
        minuteInEdit.setVisible(false);
        ampmInEdit.setVisible(false);
    }//GEN-LAST:event_noButtonRemindEditActionPerformed

    private void yesButtonAgainEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonAgainEditActionPerformed
        reminderLabelEdit.setVisible(true);
        jLabel18.setVisible(true);
    }//GEN-LAST:event_yesButtonAgainEditActionPerformed

    private void yesButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonAActionPerformed
        again = true;                      
        requireAgainNextButton.setEnabled(true);
        callMessage.setVisible(true);
    
    }//GEN-LAST:event_yesButtonAActionPerformed

    private void noButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonAActionPerformed
        again = false;
        requireAgainNextButton.setEnabled(true);
        callMessage.setVisible(false);
    }//GEN-LAST:event_noButtonAActionPerformed

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        refills = true;
        refillNextButton.setEnabled(false);
        howMany.setVisible(true);
        refillNumberIn.setVisible(true);
        refillNumberIn.requestFocus();
    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        refillNextButton.setEnabled(true);
        howMany.setVisible(false);
        refillNumberIn.setVisible(false);
        refills = false;
    }//GEN-LAST:event_noButtonActionPerformed

    private void yesButtonRefillsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonRefillsEditActionPerformed
        refillNumberEdit.setVisible(true);
        howManyLabel.setVisible(true);
    }//GEN-LAST:event_yesButtonRefillsEditActionPerformed

    private void noButtonRefillsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonRefillsEditActionPerformed
        refillNumberEdit.setVisible(false);
        howManyLabel.setVisible(false);
    }//GEN-LAST:event_noButtonRefillsEditActionPerformed

    private void noButtonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonRActionPerformed
        remindNextButton.setEnabled(true);
        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);
    }//GEN-LAST:event_noButtonRActionPerformed

    private void yesButtonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonRActionPerformed
        changeColor();
        hourIn.setVisible(true);
        minuteIn.setVisible(true);
        ampmIn.setVisible(true);
        remindNextButton.setEnabled(true);
    }//GEN-LAST:event_yesButtonRActionPerformed

    private void noButtonAgainEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonAgainEditActionPerformed
        reminderLabelEdit.setVisible(false);
        jLabel18.setVisible(false);
    }//GEN-LAST:event_noButtonAgainEditActionPerformed

    private void iceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iceButtonActionPerformed
        welcomePanel.setVisible(false);
        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Unclicked.png")));
        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Unclicked.png")));
        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Unclicked.png")));
        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Clicked.png")));
        backToIceHome();    
    }//GEN-LAST:event_iceButtonActionPerformed
    
    private void myMedsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myMedsButtonActionPerformed
        welcomePanel.setVisible(false);
        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Unclicked.png")));
        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Clicked.png")));
        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Unclicked.png")));
        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Unclicked.png")));

        backToMedsHome();
        
    }//GEN-LAST:event_myMedsButtonActionPerformed

    private void contactBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNextButton1ActionPerformed
        
    }//GEN-LAST:event_contactNextButton1ActionPerformed

    private void conPhoneBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conPhoneBackButtonActionPerformed
        addConPhonePanel.setVisible(false);
        addConNamePanel.setVisible(true);
        conNameIn.requestFocus();
        
    }//GEN-LAST:event_conPhoneBackButtonActionPerformed

    private void conEmailBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEmailBackButtonActionPerformed
        addConEmailPanel.setVisible(false);
        addConPhonePanel.setVisible(true);
        conPhoneIn.requestFocus();
    }//GEN-LAST:event_conEmailBackButtonActionPerformed

    private void conRelationBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conRelationBackButtonActionPerformed
        addConRelationshipPanel.setVisible(false);
        addConEmailPanel.setVisible(true);
        conEmailIn.requestFocus();
    }//GEN-LAST:event_conRelationBackButtonActionPerformed

    private void conNameNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conNameNextButtonActionPerformed
        if(ECL.contains(conNameIn.getText()) && !contactEditButton.isSelected()){
                JOptionPane.showMessageDialog(this, "This contact name already exists.\n" + 
                "Please enter a different contact name",
                 "", 
                JOptionPane.WARNING_MESSAGE);

        }else if(conNameIn.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter the contact's name", "Error", JOptionPane.WARNING_MESSAGE);
        }else{
            getContactName();
            addConNamePanel.setVisible(false);
            addConPhonePanel.setVisible(true);
            conPhoneIn.requestFocus();
        }     
    }//GEN-LAST:event_conNameNextButtonActionPerformed

    private void conNameBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conNameBackButtonActionPerformed
        addNewContactPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_conNameBackButtonActionPerformed

    private void addContactButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addContactButtonActionPerformed
        contactListPanel.setVisible(false);
        addNewContactPanel.setVisible(true);
        addConNamePanel.setVisible(true);
        conNameIn.requestFocus();
    }//GEN-LAST:event_addContactButtonActionPerformed

    private void conNameCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conNameCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this contact?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addConNamePanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddContact();
        }
    }//GEN-LAST:event_conNameCancelButtonActionPerformed

    private void conPhoneNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conPhoneNextButtonActionPerformed
        getContactPhone();
        addConPhonePanel.setVisible(false);
        addConEmailPanel.setVisible(true);
        conEmailIn.requestFocus();
    }//GEN-LAST:event_conPhoneNextButtonActionPerformed

    private void conPhoneCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conPhoneCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this contact?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addConPhonePanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddContact();
        }
    }//GEN-LAST:event_conPhoneCancelButtonActionPerformed

    private void conEmailNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEmailNextButtonActionPerformed
        getContactEmail();
        addConEmailPanel.setVisible(false);
        addConRelationshipPanel.setVisible(true);
        conRelationIn.requestFocus();
    }//GEN-LAST:event_conEmailNextButtonActionPerformed

    private void conEmailCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEmailCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this contact?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addConEmailPanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddContact();
        }
    }//GEN-LAST:event_conEmailCancelButtonActionPerformed

    private void conRelationNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conRelationNextButtonActionPerformed
        getContactRelation();
        addConRelationshipPanel.setVisible(false);
        addConConfirmPanel.setVisible(true);
        setContactConfirmList(contactName, contactPhone, contactEmail, contactRelation);
        
    }//GEN-LAST:event_conRelationNextButtonActionPerformed

    private void conRelationCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conRelationCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this contact?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addConRelationshipPanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddContact();
        }
    }//GEN-LAST:event_conRelationCancelButtonActionPerformed

    private void conConfirmBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conConfirmBackButtonActionPerformed
        addConConfirmPanel.setVisible(false);
        addConRelationshipPanel.setVisible(true);
        conRelationIn.requestFocus();
    }//GEN-LAST:event_conConfirmBackButtonActionPerformed

    private void conConfirmConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conConfirmConfirmButtonActionPerformed
        createContactSQLString(contactName, contactPhone, contactEmail, contactRelation);
        updateContactDB(contactSQLString);
        updateContactList();
        addNewContactPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_conConfirmConfirmButtonActionPerformed

    private void conConfirmCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conConfirmCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this contact?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addConConfirmPanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddContact();
        }
    }//GEN-LAST:event_conConfirmCancelButtonActionPerformed

    private void docNameNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docNameNextButtonActionPerformed
        getDocName();
        addDocNamePanel.setVisible(false);
        addDocPhonePanel.setVisible(true);
        docPhoneIn.requestFocus();
    }//GEN-LAST:event_docNameNextButtonActionPerformed

    private void docNameBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docNameBackButtonActionPerformed
        addDocNamePanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_docNameBackButtonActionPerformed

    private void docNameCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docNameCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this doctor?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addDocNamePanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddDoctor();
        }
    }//GEN-LAST:event_docNameCancelButtonActionPerformed

    private void docPhoneNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docPhoneNextButtonActionPerformed
        getDocPhone();
        addDocPhonePanel.setVisible(false);
        addDocSpecPanel.setVisible(true);
        docSpecIn.requestFocus();
    }//GEN-LAST:event_docPhoneNextButtonActionPerformed

    private void docPhoneBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docPhoneBackButtonActionPerformed
        addDocPhonePanel.setVisible(false);
        addDocNamePanel.setVisible(true);
        docNameIn.requestFocus();
    }//GEN-LAST:event_docPhoneBackButtonActionPerformed

    private void docPhoneCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docPhoneCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this doctor?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addDocPhonePanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddDoctor();
        }
    }//GEN-LAST:event_docPhoneCancelButtonActionPerformed

    private void docConfirmBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docConfirmBackButtonActionPerformed
        addDocConfirmPanel.setVisible(false);
        addDocSpecPanel.setVisible(true);
        docSpecIn.requestFocus();
    }//GEN-LAST:event_docConfirmBackButtonActionPerformed

    private void docConfirmConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docConfirmConfirmButtonActionPerformed
        createDocSQLString(docName, docPhone, docSpec);
        updateDocDB(docSQLString);
        JOptionPane.showMessageDialog(null, "Doctor has been added!","Added!", JOptionPane.INFORMATION_MESSAGE);
        addNewDocPanel.setVisible(false);
        contactListPanel.setVisible(true);
        updateDocList();
    }//GEN-LAST:event_docConfirmConfirmButtonActionPerformed

    private void docConfirmCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docConfirmCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this doctor?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addDocConfirmPanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddDoctor();
        }
    }//GEN-LAST:event_docConfirmCancelButtonActionPerformed

    private void docSpecNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docSpecNextButtonActionPerformed
        getDocSpec();
        addDocSpecPanel.setVisible(false);
        addDocConfirmPanel.setVisible(true);
        setDocConfirmList(docName, docPhone, docSpec);
    }//GEN-LAST:event_docSpecNextButtonActionPerformed

    private void docSpecBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docSpecBackButtonActionPerformed
        addDocSpecPanel.setVisible(false);
        addDocPhonePanel.setVisible(true);
        docPhoneIn.requestFocus();
    }//GEN-LAST:event_docSpecBackButtonActionPerformed

    private void docSpecCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docSpecCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, "Are you sure you want to "
                + "\n" + "cancel adding this doctor?", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
            addDocSpecPanel.setVisible(false);
            contactListPanel.setVisible(true);
            cancelAddDoctor();
        }
    }//GEN-LAST:event_docSpecCancelButtonActionPerformed

    private void addDoctorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDoctorButtonActionPerformed
        contactListPanel.setVisible(false);
        addNewDocPanel.setVisible(true);
        addDocNamePanel.setVisible(true);
        docNameIn.requestFocus();
    }//GEN-LAST:event_addDoctorButtonActionPerformed

    private void contactListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactListMouseClicked
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            contactListPanel.setVisible(false);
            contactInfoPanel.setVisible(true);
            getContactInfo();
        }
    }//GEN-LAST:event_contactListMouseClicked

    private void contactEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactEditButtonActionPerformed
        editContactInfo();
        contactInfoPanel.setVisible(false);
        editContactPanel.setVisible(true);
    }//GEN-LAST:event_contactEditButtonActionPerformed

    private void contactInfoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactInfoBackButtonActionPerformed
        contactInfoPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_contactInfoBackButtonActionPerformed

    private void docEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docEditButtonActionPerformed
        editDoctorInfo();
        doctorInfoPanel.setVisible(false);
        editDocPanel.setVisible(true);
    }//GEN-LAST:event_docEditButtonActionPerformed

    private void docInfoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docInfoBackButtonActionPerformed
        doctorInfoPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_docInfoBackButtonActionPerformed

    private void doctorListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doctorListMouseClicked
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            contactListPanel.setVisible(false);
            doctorInfoPanel.setVisible(true);
            getDoctorInfo();
        }
    }//GEN-LAST:event_doctorListMouseClicked

    private void deleteDoctorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDoctorButtonActionPerformed
        choice = JOptionPane.showOptionDialog(null, """
                                                    Are you sure you want to delete this doctor?
                                                    This cannot be undone.""", "Confirm Delete", JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.WARNING_MESSAGE, null, null, EXIT_ON_CLOSE);
        if(choice == 0){
            deleteDoctor();
            updateDocList();
        }
    }//GEN-LAST:event_deleteDoctorButtonActionPerformed

    private void deleteContactButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteContactButtonActionPerformed
        choice = JOptionPane.showOptionDialog(null, """
                                                    Are you sure you want to delete this contact?
                                                    This cannot be undone.""", "Confirm Delete", JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.WARNING_MESSAGE, null, null, EXIT_ON_CLOSE);
        if(choice == 0){
            deleteContact();
            updateContactList();
        }
    }//GEN-LAST:event_deleteContactButtonActionPerformed

    private void conEditBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEditBackButtonActionPerformed
        editContactPanel.setVisible(false);
        getContactInfo();
        contactInfoPanel.setVisible(true);
    }//GEN-LAST:event_conEditBackButtonActionPerformed

    private void conEditConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEditConfirmButtonActionPerformed
        createContactEditString(contactName, newConPhone, newConEmail, newConRelation);
        updateContactDB(contactSQLString);
        updateContactList();
        editContactPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_conEditConfirmButtonActionPerformed

    private void conEditCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conEditCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, """
                                                    Are you sure you want to 
                                                    cancel editing this contact?""", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
           conNameEdit.setText("");
           conPhoneEdit.setText("");
           conRelationEdit.setText("");
           editContactPanel.setVisible(false);
           contactListPanel.setVisible(true);  
        }
    }//GEN-LAST:event_conEditCancelButtonActionPerformed

    private void docEditBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docEditBackButtonActionPerformed
        editDocPanel.setVisible(false);
        getDoctorInfo();
        doctorInfoPanel.setVisible(true);
    }//GEN-LAST:event_docEditBackButtonActionPerformed

    private void docEditConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docEditConfirmButtonActionPerformed
        createDocEditString(docName, newDocPhone, newDocSpec);
        updateDocDB(docSQLString);
        updateDocList();
        editDocPanel.setVisible(false);
        contactListPanel.setVisible(true);
    }//GEN-LAST:event_docEditConfirmButtonActionPerformed

    private void docEditCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docEditCancelButtonActionPerformed
        choice = JOptionPane.showOptionDialog(this, """
                                                    Are you sure you want to 
                                                    cancel editing this doctor?""", "Confirm Cancel", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,null,  EXIT_ON_CLOSE);
        if(choice == 0){
           docNameEdit.setText("");
           docPhoneEdit.setText("");
           docSpecEdit.setText("");
           editDocPanel.setVisible(false);
           contactListPanel.setVisible(true);  
        }
    }//GEN-LAST:event_docEditCancelButtonActionPerformed

    private void upcomingRemindersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upcomingRemindersButtonActionPerformed
       toUpcomingReminders();
    }//GEN-LAST:event_upcomingRemindersButtonActionPerformed
    
    /*
    The method that actually creates the connection to the local database
    You will need to use this method in your pages
    */
    public static Connection ConnectDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:medsList.db");
            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static Connection ConnectConDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Contacts.db");
            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public static Connection ConnectDocDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Doctors.db");
            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }    
    /*
    This method will populate the medication database with the given information 
    
    */
    public void updateDatabase(String sqlString) throws NullPointerException
    {
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            String sql = sqlString;
            
            try
            {
                int rows = 0;
                pst = conn.prepareStatement(sql);
                rows = pst.executeUpdate();

                pst.close();
                rs.close();
                
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void cancelAddContact()
    {
        conNameIn.setText("");
        conPhoneIn.setText("");
        conEmailIn.setText("");
        conRelationIn.setText("");
        CECL.removeAllElements();
        contactConfirmList.setModel(CECL);
    }

    public void cancelAddDoctor(){
        docNameIn.setText("");
        docPhoneIn.setText("");
        docSpecIn.setText("");
        CDCL.removeAllElements();
        docConfirmList.setModel(CDCL);
    }
    /*
    This method resets all components of the medication input form. 
    There may be an easier way to do this
    */
    public void cancelAction(){
            dosageNumberIn.setText("");
            dosageTypeIn.setSelectedIndex(0);
            rxNumberIn.setText("");
            medNameIn.setText("");
            frequencyNumberIn.setText("");
            frequencyTypeIn.setSelectedIndex(0);
            dosesProvIn.setText("");
            nameNextButton.setEnabled(false);
            rxNumberNextButton.setEnabled(true);
            dosageNextButton.setEnabled(false);
            frequencyNextButton.setEnabled(false);
            refillNextButton.setEnabled(false);
            remindNextButton.setEnabled(false);
            requireAgainNextButton.setEnabled(false);
            yesButton.setSelected(false);
            noButton.setSelected(false);
            refillNumberIn.setVisible(false);
            refillNumberIn.setText("");
            howMany.setVisible(false);
            refillsYesNo.clearSelection();
            hourIn.setVisible(false);
            hourIn.setValue("01");
            minuteIn.setVisible(false);
            minuteIn.setValue("00");
            ampmIn.setVisible(false);
            ampmIn.setValue("A.M.");
            requireAgainYesNo.clearSelection();
            callMessage.setVisible(false);
            CL.removeAllElements();
            confirmList.setModel(CL);
            MIL.removeAllElements();
            medInfoList.setModel(MIL);
    }
    public void deleteItem(){
        
        conn = myMedsPage.ConnectDB();
        
        if(conn != null)
        {
            String sql = "DELETE FROM Medications WHERE MedName='" + medList.getSelectedValue()+ "';";
            String selected = medList.getSelectedValue();
            try
            {
            int rows = 0;
            pst = conn.prepareStatement(sql);
            rows = pst.executeUpdate();
            
            pst.close();
            
            DLM.removeElement(selected);
            medList.setModel(DLM);
            
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    /*
    This is the main method that will show the information currently in the 
    my meds list table. When there is a connection and there is data to be read, 
    this method will Select the MedName from the Medications table and insert
    it row by row into the table on the my meds page.
    */
     public void updateList()
    {
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            String sql = "Select MedName FROM Medications;";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    DLM.addElement(rs.getString("MedName"));
  
                }
                
                pst.close();
                rs.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
     
     

    
    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new myMedsPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addConConfirmPanel;
    private javax.swing.JPanel addConEmailPanel;
    private javax.swing.JPanel addConNamePanel;
    private javax.swing.JPanel addConPhonePanel;
    private javax.swing.JPanel addConRelationshipPanel;
    private javax.swing.JButton addContactButton;
    private javax.swing.JPanel addDocConfirmPanel;
    private javax.swing.JPanel addDocNamePanel;
    private javax.swing.JPanel addDocPhonePanel;
    private javax.swing.JPanel addDocSpecPanel;
    private javax.swing.JButton addDoctorButton;
    private javax.swing.JPanel addNewContactPanel;
    private javax.swing.JPanel addNewDocPanel;
    private javax.swing.JButton addNewMedButton;
    private javax.swing.JSpinner ampmIn;
    private javax.swing.JSpinner ampmInEdit;
    private javax.swing.ButtonGroup buttonBar;
    private javax.swing.JLabel callMessage;
    private javax.swing.JButton conConfirmBackButton;
    private javax.swing.JButton conConfirmCancelButton;
    private javax.swing.JButton conConfirmConfirmButton;
    private javax.swing.JButton conEditBackButton;
    private javax.swing.JButton conEditCancelButton;
    private javax.swing.JButton conEditConfirmButton;
    private javax.swing.JButton conEmailBackButton;
    private javax.swing.JButton conEmailCancelButton;
    private javax.swing.JTextField conEmailEdit;
    private javax.swing.JTextField conEmailIn;
    private javax.swing.JButton conEmailNextButton;
    private javax.swing.JButton conNameBackButton;
    private javax.swing.JButton conNameCancelButton;
    private javax.swing.JTextField conNameEdit;
    private javax.swing.JTextField conNameIn;
    private javax.swing.JButton conNameNextButton;
    private javax.swing.JButton conPhoneBackButton;
    private javax.swing.JButton conPhoneCancelButton;
    private javax.swing.JTextField conPhoneEdit;
    private javax.swing.JTextField conPhoneIn;
    private javax.swing.JButton conPhoneNextButton;
    private javax.swing.JButton conRelationBackButton;
    private javax.swing.JButton conRelationCancelButton;
    private javax.swing.JTextField conRelationEdit;
    private javax.swing.JTextField conRelationIn;
    private javax.swing.JButton conRelationNextButton;
    private javax.swing.JButton confirmBackButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton confirmCancelButton;
    private javax.swing.JList<String> confirmList;
    private javax.swing.JPanel confirmationPanel;
    private javax.swing.JList<String> contactConfirmList;
    private javax.swing.JButton contactEditButton;
    private javax.swing.JButton contactInfoBackButton;
    private javax.swing.JList<String> contactInfoList;
    private javax.swing.JPanel contactInfoPanel;
    private javax.swing.JList<String> contactList;
    private javax.swing.JPanel contactListPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteContactButton;
    private javax.swing.JButton deleteDoctorButton;
    private javax.swing.JButton docConfirmBackButton;
    private javax.swing.JButton docConfirmCancelButton;
    private javax.swing.JButton docConfirmConfirmButton;
    private javax.swing.JList<String> docConfirmList;
    private javax.swing.JButton docEditBackButton;
    private javax.swing.JButton docEditButton;
    private javax.swing.JButton docEditCancelButton;
    private javax.swing.JButton docEditConfirmButton;
    private javax.swing.JButton docInfoBackButton;
    private javax.swing.JButton docNameBackButton;
    private javax.swing.JButton docNameCancelButton;
    private javax.swing.JTextField docNameEdit;
    private javax.swing.JTextField docNameIn;
    private javax.swing.JButton docNameNextButton;
    private javax.swing.JButton docPhoneBackButton;
    private javax.swing.JButton docPhoneCancelButton;
    private javax.swing.JTextField docPhoneEdit;
    private javax.swing.JTextField docPhoneIn;
    private javax.swing.JButton docPhoneNextButton;
    private javax.swing.JButton docSpecBackButton;
    private javax.swing.JButton docSpecCancelButton;
    private javax.swing.JTextField docSpecEdit;
    private javax.swing.JTextField docSpecIn;
    private javax.swing.JButton docSpecNextButton;
    private javax.swing.JList<String> doctorInfoList;
    private javax.swing.JPanel doctorInfoPanel;
    private javax.swing.JList<String> doctorList;
    private javax.swing.JButton dosageBackButton;
    private javax.swing.JButton dosageCancelButton;
    private javax.swing.JLabel dosageInstruct;
    private javax.swing.JButton dosageNextButton;
    private javax.swing.JTextField dosageNumberEdit;
    private javax.swing.JTextField dosageNumberIn;
    private javax.swing.JPanel dosagePanel;
    private javax.swing.JComboBox<String> dosageTypeEdit;
    private javax.swing.JComboBox<String> dosageTypeIn;
    private javax.swing.JLabel dosesInstruct;
    private javax.swing.JPanel dosesPanel;
    private javax.swing.JButton dosesProvBackButton;
    private javax.swing.JButton dosesProvCancelButton;
    private javax.swing.JTextField dosesProvEdit;
    private javax.swing.JTextField dosesProvIn;
    private javax.swing.JButton dosesProvNextButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel editContactPanel;
    private javax.swing.JPanel editDocPanel;
    private javax.swing.JPanel editMedPanel;
    private javax.swing.JButton editPanelBackButton;
    private javax.swing.JButton editPanelConfirmButton;
    private javax.swing.JButton frequencyBackButton;
    private javax.swing.JButton frequencyCancelButton;
    private javax.swing.JLabel frequencyInstruct;
    private javax.swing.JButton frequencyNextButton;
    private javax.swing.JTextField frequencyNumberEdit;
    private javax.swing.JTextField frequencyNumberIn;
    private javax.swing.JPanel frequencyPanel;
    private javax.swing.JComboBox<String> frequencyTypeEdit;
    private javax.swing.JComboBox<String> frequencyTypeIn;
    private javax.swing.JSpinner hourIn;
    private javax.swing.JSpinner hourInEdit;
    private javax.swing.JLabel howMany;
    private javax.swing.JLabel howManyLabel;
    private javax.swing.JButton iceButton;
    private javax.swing.JPanel icePanel;
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
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JButton medInfoBackButton;
    private javax.swing.JButton medInfoEditButton;
    private javax.swing.JList<String> medInfoList;
    private javax.swing.JPanel medInfoPanel;
    private javax.swing.JList<String> medList;
    private javax.swing.JTextField medNameEdit;
    private javax.swing.JTextField medNameIn;
    private javax.swing.JPanel medNamePanel;
    private javax.swing.JPanel medsPanel;
    private javax.swing.JPanel middlePanel01;
    private javax.swing.JSpinner minuteIn;
    private javax.swing.JSpinner minuteInEdit;
    private javax.swing.JButton myMedsButton;
    private javax.swing.JPanel myMedsListPanel;
    private javax.swing.JButton nameBackButton;
    private javax.swing.JButton nameCancelButton;
    private javax.swing.JLabel nameInstruct;
    private javax.swing.JButton nameNextButton;
    private javax.swing.JToggleButton noButton;
    private javax.swing.JToggleButton noButtonA;
    private javax.swing.JToggleButton noButtonAgainEdit;
    private javax.swing.JButton noButtonR;
    private javax.swing.JToggleButton noButtonRefillsEdit;
    private javax.swing.JToggleButton noButtonRemindEdit;
    private javax.swing.JButton profileOptionsButton;
    private javax.swing.JPanel profileOptionsPanel;
    private javax.swing.JLabel refillInstruct;
    private javax.swing.JButton refillNextButton;
    private javax.swing.JTextField refillNumberEdit;
    private javax.swing.JTextField refillNumberIn;
    private javax.swing.JButton refillsBackButton;
    private javax.swing.JButton refillsCancelButton;
    private javax.swing.JPanel refillsPanel;
    private javax.swing.ButtonGroup refillsYesNo;
    private javax.swing.ButtonGroup refillsYesNoEdit;
    private javax.swing.JButton remindNextButton;
    private javax.swing.JButton reminderBackButton;
    private javax.swing.JButton reminderCancelButton;
    private javax.swing.JLabel reminderInstruct;
    private javax.swing.JLabel reminderLabelEdit;
    private javax.swing.JPanel reminderPanel;
    private javax.swing.ButtonGroup remindersYesNoEdit;
    private javax.swing.JButton requireAgainBackButton;
    private javax.swing.JButton requireAgainCancelButton;
    private javax.swing.JLabel requireAgainInstruct;
    private javax.swing.JButton requireAgainNextButton;
    private javax.swing.JPanel requireAgainPanel;
    private javax.swing.ButtonGroup requireAgainYesNo;
    private javax.swing.ButtonGroup requireAgainYesNoEdit;
    private javax.swing.JLabel rxNumInst;
    private javax.swing.JButton rxNumberBackButton;
    private javax.swing.JButton rxNumberCancelButton;
    private javax.swing.JTextField rxNumberEdit;
    private javax.swing.JTextField rxNumberIn;
    private javax.swing.JButton rxNumberNextButton;
    private javax.swing.JPanel rxNumberPanel;
    private javax.swing.JLabel titleBar;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton upcomingRemindersButton;
    private javax.swing.JPanel upcomingRemindersPanel;
    private javax.swing.JPanel welcomePanel;
    private javax.swing.JToggleButton yesButton;
    private javax.swing.JToggleButton yesButtonA;
    private javax.swing.JToggleButton yesButtonAgainEdit;
    private javax.swing.JButton yesButtonR;
    private javax.swing.JToggleButton yesButtonRefillsEdit;
    private javax.swing.JToggleButton yesButtonRemindEdit;
    // End of variables declaration//GEN-END:variables
}
