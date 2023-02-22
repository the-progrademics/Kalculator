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

    public Boolean signUp() throws SQLException {
        if(isSignedUp()){
            System.out.println("ERROR: username already exists.");
            return false;
        }else{
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO signinup (username, password) VALUES (?, ?);");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, (this.password));
            int rows = signUpStmt.executeUpdate();
            if (rows != 0){
                this.signedIn= true;
            }
        }
        //the return of else
        return true;
    }
    private boolean isSignedUp() throws SQLException {
        boolean signedUp = false;
        Connection dbConnection = utils.DBConnection.getInstance().getConnection();
        Statement stmt = dbConnection.createStatement();
        PreparedStatement signInStmt =
                dbConnection.prepareStatement("SELECT * from signinup where username=? and password=?;");
        signInStmt.setString(1, this.name);
        signInStmt.setString(2, (this.password));
        ResultSet rs = signInStmt.executeQuery();
        if(rs.next()){
            signedUp = true;
        }
        return signedUp;
    }

    public Boolean signIn() throws SQLException {
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
                System.out.println("ERROR: username or password is wrong.");}
        return signedIn;}
    public Boolean addCustomMeal(String foodname, int cals) throws SQLException {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement pStmt =
                    dbConnection.prepareStatement("INSERT INTO cmeals (username, foodname, cals) VALUES (?, ?, ?);");
            pStmt.setString(1, this.name);
            pStmt.setString(2, foodname);
            pStmt.setInt(3, cals);
            pStmt.executeUpdate();
        return signedIn;
    }

    public Boolean registerMeal(String foodname, int cals) throws SQLException {
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signUpStmt =
                    dbConnection.prepareStatement("INSERT INTO mealseaten (username, foodname, cals, date) VALUES (?, ?, ?, '"+getCurrentDate()+"');");
            signUpStmt.setString(1, this.name);
            signUpStmt.setString(2, foodname);
            signUpStmt.setInt(3, cals);
            signUpStmt.executeUpdate();
        return signedIn;}
    private String getCurrentDate(){
        DateTimeFormatter wantedFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return wantedFormat.format(LocalDateTime.now());
    }
    public String stats(int length) throws SQLException {
        int actualLength=getActualLength(length);
        int totalCals = 0;
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT SUM(cals) FROM mealseaten WHERE username=? and date > now() - interval '"+actualLength+" day';");
            signInStmt.setString(1, this.name);
            ResultSet rs = signInStmt.executeQuery();
            if(rs.next()){
                totalCals = rs.getInt("sum");}
        return tellMeTheStats(totalCals,length);}
    private String tellMeTheStats(int totalCals, int length){
        int result = getIdealCals(length) - totalCals;
        String resultStatement = "";
        if(result < 0){
            resultStatement = ("over the last "+length+" you went over the ideal limit for your BMR by:  "+ result*-1+"Calories");} else{
            resultStatement = ("over the last "+length+" you are still under the ideal limit for your BMR by:  "+ result+"Calories");}
        return resultStatement;}
    private int getIdealCals(int length){
        return (BMRCalculator(new double[]{this.gender,this.weight,this.height,this.age})) * length;}
    private int getActualLength(int length) throws SQLException {
        int actualLength=-1;
        int oldestEntry = oldestEntry();
        if(length>oldestEntry){
            actualLength=oldestEntry;
        }else{
            actualLength=length;}
        return actualLength;}

    private int oldestEntry() throws SQLException {
        int result =-1;
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT MIN((now()::DATE-(SELECT MIN(date) FROM mealseaten))) FROM mealseaten;");
            ResultSet rs = signInStmt.executeQuery();
            if(rs.next()){
                result = rs.getInt("min");
            }
return result;
    }

    public String searchPersonalMeals(String foodname) throws Exception{
        String results = "";
        foodname = "%"+foodname+"%";
            Connection dbConnection = utils.DBConnection.getInstance().getConnection();
            Statement stmt = dbConnection.createStatement();
            PreparedStatement signInStmt =
                    dbConnection.prepareStatement("SELECT foodname,cals from cmeals where username=? and foodname like ?;");
            signInStmt.setString(1, this.name);
            signInStmt.setString(2, (foodname));
            ResultSet rs = signInStmt.executeQuery();
            while(rs.next()){
                String foodN = rs.getString("foodname");
                int cals = rs.getInt("cals");
                results = results.concat(foodN+"\n contains: "+cals+" Calories per 100 grams."+"\n\n");
            }
        return results;}
    public void saveInfo(double[] info){
        if(info[0] < 1){
            this.gender =0;} else{
            this.gender =1;
        }
        this.weight = info[1];
        this.height = info[2];
        this.age = info[3];
    }

    public boolean equals(Member member){
        boolean result = true;
        if(!(this.name == member.getName() && this.password == member.getPassword() && this.signedIn == member.getSignedIn() && this.gender == member.getGender() && this.weight  == member.getWeight() && this.height == member.getHeight() && this.age == member.getAge())){
            result = false;
        }
        return result;
    }}