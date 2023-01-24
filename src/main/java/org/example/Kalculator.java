package org.example;
import java.io.*;
import java.util.Scanner;

public class Kalculator
{
    public static void main( String[] args ) throws IOException
    {
while(true){
            String foodName = letters();
            System.out.println(getCaloricInfo (foodName));
        }    }
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
