package org.example;
import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Kalculator
{
    public static void main( String[] args ) throws IOException
    {
while(true){
            String foodName = letters();
            System.out.println(getCaloricInfo (foodName));
            System.out.println("your BMR is : " + BMRCalculator(userInfo()));
        }    }

    public static Boolean signUpIn(String username,String password,Boolean newUser) throws SQLException {
        Boolean result= false;
        try {
            utils.DBConnection db = utils.DBConnection.getInstance();
            SignUpInClient s = new SignUpInClient(username, password);
            if(newUser){
                result = s.signUp();
            }else{
                result = s.signIn();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
return result;
    }
    //a method that take a food name, search for it in the Caloric_data file, and return result
    public static String getCaloricInfo (String foodName) throws IOException{
        //padding to reduce results
        foodName = (" "+foodName+" ");

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

        //if we've got no matches return "sorry" message
        if(dataAssociatedWithFoodName.equals("")){
            return "Sorry, we have no data associated with the food you typed";
        }
        //return matches
        return dataAssociatedWithFoodName;
    }

    public static int BMRCalculator(double[] info){
        int BMR;
        if(info[0]==0){
            //Men BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
            BMR = (int) (88.362 + (13.397 * info[1]) + (4.799 * info[2]) - (5.677 * info[3]));
        }else{
            //Women BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)
            BMR = (int) (447.593 + (9.247 * info[1]) + (3.098 * info[2]) - (4.330 * info[3]));
        }
        return BMR;
    }

    public static double [] userInfo(){
        Scanner sc = new Scanner(System.in);
        double gender;
        System.out.println("please provide us with your gender male or female");
        String strGender = sc.next().toLowerCase();

        while (!strGender.equalsIgnoreCase("male") && !strGender.equalsIgnoreCase("female")){
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

        return new double[]{gender, wight, height, age};

    }
    public static String letters(){
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

    public static boolean isLetter(String input){
        String str = input.toLowerCase();
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) < 'a' || str.charAt(i) > 'z')
                return false;

        return true;

    }
}
