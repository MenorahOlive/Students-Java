package students;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Students extends JFrame implements ActionListener{
    Connection conn;
    Statement stmt;
    ResultSet rs;
    JButton save,delete,update,newrec,back,next,exit;
    JTextField surname,othername,nationalid,email,phone,studentid;
    JComboBox gender;
    JLabel students,surnamelbl,othernamelbl,nationalidlbl,emaillbl,phonelbl,genderlbl,studentidlbl;
    
    public void DoConnect(){
        try{
//STEP 1: Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");//JDBC driver name
            
            //Database Credentials
            String host = "jdbc:mysql://localhost/student"; //Database URL
            String uName = "root";
            String uPass = "";
            
//STEP 2: Create a connection
           conn = DriverManager.getConnection(host,uName,uPass);
          
//STEP 3: Create a statement object  
            
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
//Step 4: Execute query
            
//            String SQL ="CREATE TABLE IF NOT EXISTS `students` (`student_id` int(10) NOT NULL AUTO_INCREMENT, `surname` varchar(50) NOT NULL, `othername` varchar(50) NOT NULL, `national_id` int(10) NOT NULL, `email` varchar(50) NOT NULL, `phone` int(10) NOT NULL, `gender` varchar(10) NOT NULL, PRIMARY KEY (`student_id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
//            stmt.executeUpdate(SQL);
//            System.out.println("Students table created successfully!");
            String SQL = "Select * from students";
            rs = stmt.executeQuery(SQL);
        
//Step 5: Extract data from the result set/Process the results
            rs.next();
            surname.setText(rs.getString("surname"));
            othername.setText(rs.getString("othername"));
            nationalid.setText(Integer.toString(rs.getInt("national_id")));
            email.setText(rs.getString("email"));
            phone.setText(Integer.toString(rs.getInt("phone")));
            gender.setSelectedItem(rs.getString("gender"));
            studentid.setText(rs.getString("student_id"));
        }
        catch (SQLException | ClassNotFoundException err){
            JOptionPane.showMessageDialog(Students.this,err.getMessage());
        }
    }
    
    Students(){
        setDefaultCloseOperation(Students.EXIT_ON_CLOSE);
        setSize(500,500);
        setLayout(null);
        Dimension dim=Toolkit.getDefaultToolkit().getScreenSize(); this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
//Labels
        students=new JLabel("STUDENTS"); students.setBounds(200,0,100,25); add(students);
        surnamelbl=new JLabel("Surname"); surnamelbl.setBounds(10,50,100,25); add(surnamelbl);
        othernamelbl=new JLabel("Other names"); othernamelbl.setBounds(10,100,100,25); add(othernamelbl);
        nationalidlbl=new JLabel("National ID"); nationalidlbl.setBounds(10,150,100,25); add(nationalidlbl);
        emaillbl=new JLabel("Email address"); emaillbl.setBounds(10,200,100,25); add(emaillbl);
        phonelbl=new JLabel("Phone number"); phonelbl.setBounds(10,250,100,25); add(phonelbl);
        genderlbl=new JLabel("Gender"); genderlbl.setBounds(10,300,100,25); add(genderlbl);
        studentidlbl=new JLabel("Assigned student ID"); studentidlbl.setBounds(10,350,150,25); add(studentidlbl);
        
//Text Fields
        surname=new JTextField(); surname.setBounds(151,50,100,25); add(surname);
        othername=new JTextField(); othername.setBounds(151,100,100,25); add(othername); 
        nationalid=new JTextField(); nationalid.setBounds(151,150,100,25); add(nationalid);
        email=new JTextField(); email.setBounds(151,200,100,25); add(email);
        phone=new JTextField(); phone.setBounds(151,250,100,25); add(phone);
        studentid=new JTextField(); studentid.setBounds(151,350,100,25); studentid.setEditable(false); add(studentid); 
                
//Combo box
        gender=new JComboBox(); gender.setBounds(151,300,100,25);add(gender); gender.addItem("Male"); gender.addItem("Female");
        
//Buttons
        save=new JButton("Save"); save.setBounds(300,50,100,25); add(save); save.addActionListener(this);
        delete=new JButton("Delete"); delete.setBounds(300,100,100,25); add(delete); delete.addActionListener(this);
        update=new JButton("Update"); update.setBounds(300,150,100,25); add(update); update.addActionListener(this);
        newrec=new JButton("New"); newrec.setBounds(300,200,100,25); add(newrec); newrec.addActionListener(this);
        back=new JButton("Back"); back.setBounds(300,250,100,25); add(back); back.addActionListener(this);
        next=new JButton("Next"); next.setBounds(300,300,100,25); add(next); next.addActionListener(this);
        exit=new JButton("Exit"); exit.setBounds(300,400,100,25); add(exit); exit.addActionListener(this); 
        
        DoConnect();
    }
    
    public static void main (String[] args){
        Students sts= new Students();
        sts.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt){

//Save Button
        if(evt.getSource()==save){
            try{
                int natid = Integer.parseInt(nationalid.getText());
                int phoneno = Integer.parseInt(phone.getText());
                stmt.executeUpdate("INSERT INTO students ( surname, othername, national_id, email, phone, gender) VALUES ('"+surname.getText()+"','"+othername.getText()+"','"+natid+"','"+email.getText()+"','"+phoneno+"','"+gender.getSelectedItem()+"')");
                JOptionPane.showMessageDialog(Students.this, "Record saved!");
                Statement stmtshow;
                stmtshow = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs=stmtshow.executeQuery("select student_id from students where national_id='"+natid+"'");
                rs.next();
                studentid.setText(rs.getString("student_id"));
                save.setEnabled(false);
            }
            catch(SQLException err){
                JOptionPane.showMessageDialog(Students.this, err.getMessage());
                System.out.println(err.getMessage());
            }
            catch(NumberFormatException ev){
                JOptionPane.showMessageDialog(Students.this, "Check that the national ID number and telephone number are numbers only.");
                System.out.println(ev.getMessage());
            }
        }
            
//Delete Button
        if(evt.getSource()==delete){
            try{
                stmt.executeUpdate("delete from students where student_id='"+studentid.getText()+"'");
                JOptionPane.showMessageDialog(Students.this, "Record deleted!");
            
        //Execute query
                rs=stmt.executeQuery("select * from students");
                rs.next();
            
//STEP 5: Extract data from result set
                surname.setText(rs.getString("surname"));
                othername.setText(rs.getString("othername"));
                nationalid.setText(Integer.toString(rs.getInt("national_id")));
                email.setText(rs.getString("email"));
                phone.setText(Integer.toString(rs.getInt("phone")));
                gender.setSelectedItem(rs.getString("gender"));
                studentid.setText(rs.getString("student_id"));
            }
            catch(SQLException err){
                JOptionPane.showMessageDialog(Students.this, err.getMessage());
                System.out.println(err.getMessage());
            }
        }
        
//Update Button
        if(evt.getSource()==update){
            try{
                stmt.executeUpdate("update students set surname='"+surname.getText()+"',othername='"+othername.getText()+"',national_id='"+nationalid.getText()+"',email='"+email.getText()+"',phone='"+phone.getText()+"',gender='"+gender.getSelectedItem()+"' WHERE student_id ='"+studentid.getText()+"'");
                JOptionPane.showMessageDialog(Students.this, "Record updated!");
            }
            catch(SQLException err){
                JOptionPane.showMessageDialog(Students.this, err.getMessage());
                System.out.println(err.getMessage());
            }
        }
        
//New Button
        if(evt.getSource()==newrec){
            surname.setText("");
            othername.setText("");
            nationalid.setText("");
            email.setText("");
            phone.setText("");
            gender.setSelectedItem("Female");
            studentid.setText("");
            save.setEnabled(true);
        }
        
//Back Button
        if(evt.getSource()==back){
            try {
                if(rs.previous()){
                    surname.setText(rs.getString("surname"));
                    othername.setText(rs.getString("othername"));
                    nationalid.setText(Integer.toString(rs.getInt("national_id")));
                    email.setText(rs.getString("email"));
                    phone.setText(Integer.toString(rs.getInt("phone")));
                    gender.setSelectedItem(rs.getString("gender"));
                    studentid.setText(rs.getString("student_id"));
                }
                else{
                    rs.next();
                    JOptionPane.showMessageDialog(Students.this,"Start of File.");
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(Students.this, ex.getMessage());
            }
        }
        
//Next Button
        if(evt.getSource()==next){
            try {
                if(rs.next()){
                    surname.setText(rs.getString("surname"));
                    othername.setText(rs.getString("othername"));
                    nationalid.setText(Integer.toString(rs.getInt("national_id")));
                    email.setText(rs.getString("email"));
                    phone.setText(Integer.toString(rs.getInt("phone")));
                    gender.setSelectedItem(rs.getString("gender"));
                    studentid.setText(rs.getString("student_id"));
                }
                else{
                    rs.previous();
                    JOptionPane.showMessageDialog(Students.this,"End of File.");
                }
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(Students.this, ex.getMessage());
            }
        }
        
//Exit Button
        if(evt.getSource()==exit){
            System.exit(0);
        }
    }
}
