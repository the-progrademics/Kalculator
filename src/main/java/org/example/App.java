package org.example;
import java.io.*;
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println(getCaloricInfo ("apple"));
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
}
