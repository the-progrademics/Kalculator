package org.example;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;
//____________________________________________________________________________________________________________________//
public class Kalculator
{
    public static void main( String[] args ) throws Exception {
while(true){
            System.out.println();
            mainMenu();


        }    }
    //________________________________________________________________________________________________________________//
    public static Member signUpIn(String username,String password,Boolean newUser) throws SQLException {
        Member s = null;
            utils.DBConnection db = utils.DBConnection.getInstance();
            s = new Member(username, password);
            if(newUser){
               s.signUp();
            }else{
               s.signIn();
            }

return s;
    }
    //a method that take a food name, search for it in the Caloric_data file, and return result
    public static String getCaloricInfo (String foodName, Member member) throws Exception {
        //padding to reduce results (gives bad results)
        //foodName = (" "+foodName+" ");

        File dataFile = new File(".\\Caloric_data.txt");
        FileReader dataReader = new FileReader(dataFile);
        BufferedReader bufferedDataReader = new BufferedReader(dataReader);

        //Strings for reading and writing into
        String lineRead = null;
        String dataAssociatedWithFoodName = "";

        while(bufferedDataReader.ready() && (lineRead = bufferedDataReader.readLine()) != null){

            //if we get a match write it into the dataAssociatedWithFoodName String
            if(lineRead.toLowerCase().contains(foodName.toLowerCase())){
                String[] lineSplit = lineRead.split(",");
                dataAssociatedWithFoodName = dataAssociatedWithFoodName.concat(lineSplit[0]+"\n contains: "+lineSplit[1]+" Calories per 100 grams."+"\n\n");
            }
        }
        bufferedDataReader.close();
        //members search snippet
        if(member != null && member.getSignedIn()){
            dataAssociatedWithFoodName = dataAssociatedWithFoodName.concat(member.searchPersonalMeals(foodName));
        }

        //if we've got no matches return "sorry" message
        if(dataAssociatedWithFoodName.equals("")){
            return "Sorry, we have no data associated with the food you typed";
        }
        //return matches
        return dataAssociatedWithFoodName;
    }
    //________________________________________________________________________________________________________________//
    public static int BMRCalculator(double[] info){
        int BMR;
        if(info[0]<1){
            //Men BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
            BMR = (int) (88.362 + (13.397 * info[1]) + (4.799 * info[2]) - (5.677 * info[3]));
        }else{
            //Women BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)
            BMR = (int) (447.593 + (9.247 * info[1]) + (3.098 * info[2]) - (4.330 * info[3]));
        }
        return BMR;
    }
    //________________________________________________________________________________________________________________//
    public static double [] userInfo(Member member){
        double[] info = new double[4];
        if(member != null && member.getSignedIn()&& member.getHeight() > 0) {
            info[0] = member.getGender();
            info[1] = member.getWeight();
            info[2] = member.getHeight();
            info[3] = member.getAge();
        }else {
            // before asking user can choicee get BMR or search for food
            Scanner sc = new Scanner(System.in);
            double gender;
            System.out.println("please provide us with your gender male or female");
            String strGender = sc.next().toLowerCase();

            while (!strGender.equalsIgnoreCase("male") && !strGender.equalsIgnoreCase("female")) {
                System.out.println("sorry we didn't understand, could you rewrite it?");
                strGender = sc.next().toLowerCase();
            }

            gender = strGender.equalsIgnoreCase("male") ? 0 : 1;
            System.out.println("Thank you!, now provide us with your weight in Kilogram pleas: ");
            double wight = sc.nextDouble();
            System.out.println("thank you again!, now please provide us with your height in centimeters: ");
            double height = sc.nextDouble();
            System.out.println("thank you for your time, lastly please provide us with your age in years");
            double age = sc.nextDouble();

            //if user wants to save info

            if (member != null && member.getSignedIn()) {
                System.out.println("""
                        would you like to save your BMR information? (digit only)
                        1- yes
                        2- no (you will have to re-enter your information next time also)""");
                int bmrInfo = sc.nextInt();
                switch (bmrInfo){
                    case 1: member.saveInfo(new double[]{gender, wight, height, age}); break;
                    case 2: break;
                }

            }

            info[0] = gender;
            info[1] = wight;
            info[2] = height;
            info[3] = age;
        }
        return info;
    }
    //________________________________________________________________________________________________________________//
    public static String foodName(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter name of the food, only letters are expted:) ");
        System.out.print(">");
        String frutes = sc.next().strip().toLowerCase();
        while(!isLetter(frutes)){
            System.out.println("wrong input, please provide us with letters only");
            System.out.print(">");
            frutes = sc.next().strip().toLowerCase();
        }
        return frutes;
    }
    //________________________________________________________________________________________________________________//
    public static boolean isLetter(String input){
        String str = input.toLowerCase();
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) < 'a' || str.charAt(i) > 'z')
                return false;

        return true;

    }
    //________________________________________________________________________________________________________________//
    public static Member registration(boolean newUser) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("please provide us with your user name: ");
        String userName = sc.next();
        System.out.println("now please provide us with your password: ");
        String password = sc.next();

        return signUpIn(userName,password,newUser);

    }
    //________________________________________________________________________________________________________________//
    public static void mainMenu() throws Exception {
        Scanner sc = new Scanner(System.in);
        Member user;

        System.out.println("""
                Welcome to Kalculator!
                member of the family? please type 1
                wanna be a member? please type 2
                or type 3 to skip (being a member will give you a full monitoring to your biometric signs)""");
        char choice = sc.next().charAt(0);
        while ((choice != '1') && (choice != '2') && (choice != '3')) {
            System.out.println("please enter a valid number (1,2,3)");
            choice = sc.next().charAt(0);

        }
        switch (choice){
            // note: add welcome with name and welcome with guest
            case '1':
                System.out.println("welcome back!");
                user = registration(false);

                logedInSubMenu(user);
                break;
            case '2':
                System.out.println("we are honored!");

                user = registration(true);
                logedInSubMenu(user);



                break;
            case '3' :
                geustMenu();
                break;


        }

    }
    //________________________________________________________________________________________________________________//
    public static void geustMenu() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                Now what would you like to do?
                1- search for a meal
                2- calculate your BMR
                """);
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                System.out.println(getCaloricInfo(foodName(), null)); break;
            case 2:
                System.out.println("Your BMR is : "+BMRCalculator(userInfo(null))); break;
        }


    }
    //________________________________________________________________________________________________________________//
    public static void logedInSubMenu(Member member) throws Exception {
        while(true){
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                Now what would you like to do?(digit only)
                1- search for a meal
                2- enter last meal
                3- calculate your BMR
                4- statistics
                5- add meals""");
        int choice = sc.nextInt();
        switch (choice){

            case 1: System.out.println(getCaloricInfo(foodName(), member)); break;
            //________________________________________________________________________________________________________//
            case 2:
                System.out.println("please provide me with your last meal name: ");
                String mealName = sc.next();
                System.out.println("meal's calorie: ");
                int calorie = sc.nextInt();
                member.registerMeal(mealName,calorie);
                break;

            //________________________________________________________________________________________________________//
            case 3:
                System.out.println("Your BMR is : "+BMRCalculator(userInfo(member))); break;


            //________________________________________________________________________________________________________//
            case 4:
                System.out.println("""
                        Okay let's see ;)
                        how do you like it?(digit only)
                        1-weekly(7 days)
                        2-monthly(30 days)
                        3-custom
                        """);
                int type = sc.nextInt();
                switch (type){
                    case 1: member.stats(7);break;
                    case 2: member.stats(30);break;
                    case 3:
                        System.out.print("how many days you want to calculate(digit only)");
                        member.stats(sc.nextInt());
                }
                break;
            case 5:
                System.out.println("please provide me with your new meal name: ");
                String newMealName = sc.next();
                System.out.println("meal's calorie: ");
                int newCalorie = sc.nextInt();
                member.addCustomMeal(newMealName,newCalorie);
                break;
        }


    }}



}

