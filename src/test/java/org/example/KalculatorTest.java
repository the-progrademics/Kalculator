package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import static org.example.Kalculator.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class KalculatorTest

{

    @Test
    public void testMenus() throws Exception {
    mainMenu(getInputStream(".\\testmainmenue1.txt"));
    }


    @Test
    public void testUserInfo() throws IOException {
        var member = maleTestSubject();
        assertEquals("0 70 160 25","%d %d %d %d".formatted(member.getGender(),(int)member.getWeight(),(int)member.getHeight(),(int)member.getAge()));
        var returendMember = new Member("ahmad", "123");
        returendMember.setSignedIn(true);
        Scanner sc = new Scanner(getInputStream(".\\test.txt"));
        returendMember.saveInfo(userInfo(returendMember, sc));
        assertTrue(member.equals(returendMember));

        var returendMember1 = new Member("ahmad", "123");
        returendMember1.setSignedIn(true);
        Scanner sc1 = new Scanner(getInputStream(".\\test.txt"));
        returendMember1.saveInfo(userInfo(returendMember1, sc1));
        assertTrue(member.equals(returendMember1));

    }
    private final Kalculator Kal = new Kalculator();

    @Test
   public void testApp()
    {
     assertEquals(false,Kal.isLetter("5"));
        assertEquals(true,Kal.isLetter("a"));
    }

    @Test
    public void testBMR (){
        assertEquals(1652,BMRCalculator(new double[] {0,70,160,25}));
        assertEquals(1482,BMRCalculator(new double[] {1,70,160,25}));
    }
    @Test
    public void testGetCaloricInfo() throws Exception {
        assertEquals("Sorry, we have no data associated with the food you typed",getCaloricInfo ("fake food", null));
        assertEquals("Sherbet Orange\n"+" contains: 144 Calories per 100 grams.\n"+"\n",getCaloricInfo ("Sherbet Orange", null));
        assertEquals("Falafel\n" + " contains: 333 Calories per 100 grams.\n\n",getCaloricInfo ("falafel", maleTestSubject()));
    }
    @Test
    public void testMember(){
        Member member = maleTestSubject();


        member.setName("ali");
        member.setPassword("321");

        assertEquals("ali",member.getName());
        assertEquals("321",member.getPassword());
        assertEquals(true,member.getSignedIn());
        assertEquals(0,member.getGender());
        assertEquals(70,member.getWeight());
        assertEquals(160,member.getHeight());
        assertEquals(25,member.getAge());
    }
    @Test
    public void testSignUpIn() throws SQLException {
        //existing user tset
        Member ahmad = new Member("ahmad","123");
        ahmad.setSignedIn(true);
        Member returnedAhmad = signUpIn("ahmad","123",false);
        assertTrue(ahmad.equals(returnedAhmad));
        //new user test
        String username = "test" + Math.random();
        String password = "" + Math.random();
        Member newUser = new Member(username,password);
        newUser.setSignedIn(true);
        Member returnedNewUser = signUpIn(username,password,true);
        assertTrue(newUser.equals(returnedNewUser));
        //errors test
    }
    @Test
    public void testMemberSignInAndSignUp() throws SQLException {
        Member ahmad = new Member("ahmad","123");
        assertFalse(ahmad.signUp());
        assertTrue(ahmad.signIn());
        Member ahmadImpersonator = new Member("ahmad","122");
        assertFalse(ahmadImpersonator.signIn());
        Member newUser = new Member("test" + Math.random(),"" + Math.random());
        assertFalse(newUser.signIn());
        assertTrue(newUser.signUp());
    }
    @Test
    public void testAddCustomMeal() throws SQLException {
        Member ahmad = maleTestSubject();
        String foodName = "testFood"+Math.random();
        int cals = (int) Math.random();
        ahmad.addCustomMeal(foodName,cals);
        Connection dbConnection = utils.DBConnection.getInstance().getConnection();
        Statement stmt = dbConnection.createStatement();
        PreparedStatement pStmt =
                dbConnection.prepareStatement("select * from cmeals where username = ? and foodname = ? and cals = ?;");
        pStmt.setString(1, ahmad.getName());
        pStmt.setString(2, foodName);
        pStmt.setInt(3, cals);
        ResultSet rs = pStmt.executeQuery();
        assertTrue(rs.next());
    }

    @Test
    public void testRegisterMeal() throws SQLException {
        Member ahmad = maleTestSubject();
        String foodName = "testFood"+Math.random();
        int cals = (int) Math.random();
        ahmad.registerMeal(foodName,cals);
        Connection dbConnection = utils.DBConnection.getInstance().getConnection();
        Statement stmt = dbConnection.createStatement();
        PreparedStatement pStmt =
                dbConnection.prepareStatement("select * from mealseaten where username = ? and foodname = ? and cals = ?;");
        pStmt.setString(1, ahmad.getName());
        pStmt.setString(2, foodName);
        pStmt.setInt(3, cals);
        ResultSet rs = pStmt.executeQuery();
        assertTrue(rs.next());
    }

    @Test
    public void testTellMeTheStats() throws SQLException {
        Member testMan = new Member("the invesible man", "***");
        assertEquals("over the last 1 you are still under the ideal limit for your BMR by:  75Calories",testMan.stats(1));
        assertEquals("over the last -1 you went over the ideal limit for your BMR by:  75Calories",testMan.stats(-1));
    }










    public InputStream getInputStream(String str) throws IOException {

        File f = new File(str);
        if(!f.exists())
            f.createNewFile();
        return new FileInputStream(f);
    }


    public Member maleTestSubject(){
        Member member = new Member("ahmad","123");
        member.setSignedIn(true);
        member.saveInfo(new double[] {0,70,160,25});
        return member;
    }
    public Member femaleTestSubject(){
        Member member = new Member("hind","123");
        member.setSignedIn(true);
        member.saveInfo(new double[] {1,70,160,25});
        return member;
    }
}
