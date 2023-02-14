package org.example;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.*;

import static org.example.Kalculator.BMRCalculator;

public class Member {
    private String name;
    private String password;
    private Boolean signedIn;

    private int gender;
    private double weight;
    private double height;
    private double age;

    public Member(String name, String password){
        this.name = name;
        this.password = password;
        this.signedIn = false;
        this.gender = -1;
        this.weight = -1;
        this.height = -1;
        this.age = -1;
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

    public int getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getAge() {
        return age;
    }

    public Boolean signUp(){
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO signinup (username, password) VALUES (?, ?);");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, (this.password));
            int rows = signUpStmt.executeUpdate();
            if (rows == 0){
                System.out.println("ERROR: username already exists.");
            }else{
                this.signedIn= true;
            }
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
                    dbConnection.prepareStatement("SELECT * from signinup where username=? and password=?;");
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

    public Boolean addCustomMeal(String foodname, int cals){
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO cmeal (username, foodname, cals) VALUES (?, ?, ?);");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, foodname);
            signUpStmt.setInt(3, cals);
            signUpStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signedIn;
    }

    public Boolean registerMeal(String foodname, int cals){
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO mealseaten (username, foodname, cals, date) VALUES (?, ?, ?, ?);");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, foodname);
            signUpStmt.setInt(3, cals);
            signUpStmt.setString(4, getCurrentDate());
            signUpStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signedIn;
    }

    private String getCurrentDate(){
        DateTimeFormatter wantedFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return wantedFormat.format(LocalDateTime.now());
    }

    public String stats(int length){
        int actualLength=getActualLength(length);
        int totalCals = 0;
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT SUM(cals) FROM mealseaten WHERE username=? and date > now() - interval '? day';");
            signInStmt.setString(1, this.name);
            signInStmt.setInt(2, (actualLength));
            ResultSet rs = signInStmt.executeQuery();
            while(rs.next()){
                totalCals = totalCals + rs.getInt("cals");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tellMeTheStats(totalCals,length);
    }

    private String tellMeTheStats(int totalCals, int length){
        int result = getIdealCals(length) - totalCals;
        String resultStatement = "";
        if(result < 0){
            resultStatement = ("over the last "+length+" you went over the ideal limit for your BMR by:  "+ result+"Calories");
        }else{
            resultStatement = ("over the last "+length+" you are still under the ideal limit for your BMR by:  "+ result+"Calories");
        }
        return resultStatement;
    }
    private int getIdealCals(int length){
        return (BMRCalculator(new double[]{this.gender,this.weight,this.height,this.age})) * length;
    }

    private int getActualLength(int length){
        int actualLength=-1;
        int oldestEntry = oldestEntry();
        if(length>oldestEntry){
            actualLength=oldestEntry;
        }else{
            actualLength=length;
        }
        return actualLength;
    }

    private int oldestEntry(){
        int result =-1;
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT MIN((now()::DATE-(SELECT MIN(date) FROM mealseaten))) FROM mealseaten;");
            ResultSet rs = signInStmt.executeQuery();
            if(rs.next()){
                result = rs.getInt("min");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
return result;
    }

    public String searchPersonalMeals(String foodname){
        String results = "";
        try {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT foodname,cals from cmeal where username=? and foodname like '%?%';");
            signInStmt.setString(1, this.name);
            signInStmt.setString(2, (foodname));
            ResultSet rs = signInStmt.executeQuery();
            while(rs.next()){
                String foodN = rs.getString("foodname");
                int cals = rs.getInt("cals");
                results = results.concat(foodN+"\n contains: "+cals+" Calories per 100 grams."+"\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void saveInfo(double[] info){
        if(info[0] < 1){
            this.gender =0;
        }else{
            this.gender =1;
        }
        this.weight = info[0];
        this.height = info[1];
        this.age = info[2];
    }


}