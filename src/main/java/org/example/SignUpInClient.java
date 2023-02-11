package org.example;

import java.sql.*;

public class SignUpInClient {
    private String name;
    private String password;
    private Boolean signedIn;

    public SignUpInClient(String name, String password){
        this.name = name;
        this.password = password;
        this.signedIn = false;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public Boolean signUp(){
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO Sign-in/up (username, password) VALUES (?, ?);");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, (this.password));
            int rows = signUpStmt.executeUpdate();
            if (rows == 0){
                System.out.println("ERROR: username already exists.");
            }else{
                this.signedIn= true;
            }
            //System.out.println("Rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signedIn;
    }

    public Boolean signIn(){
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT * from Sign-in/up where username=? and password=?;");
            signInStmt.setString(1, this.name);
            signInStmt.setString(2, (this.password));
            ResultSet rs = signInStmt.executeQuery();
            if(rs.next()){
                System.out.println("Signed-in successfully.");
                this.signedIn=true;
            }else{
                System.out.println("ERROR: username or password is wrong.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signedIn;
    }
}