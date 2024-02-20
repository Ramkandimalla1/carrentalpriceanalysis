package features;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

//  perform input validation for car rental information
public class DataValidation {

    private static final Pattern SINGLE_LETTER_PATTERN = Pattern.compile("^[AM]$");

    // Regular expressions for  car name, date, time, and city name
    
    static Pattern PTRN_FOR_CAR_NAME = Pattern.compile("^[a-zA-Z0-9\\s]+$");
    private static  Pattern RANGE_PATTERN = Pattern.compile("^\\d+-\\d+$");
    static Pattern PTRN_FOR_DATE = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$"); //date format update
    static Pattern PTRN_FOR_TIME = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9] (AM|PM)$");
    static Pattern PTRN_NAME_FOR_CITY = Pattern.compile("^[a-zA-Z\\s]+$");
    static Pattern PTRN_FOR_INTEGER = Pattern.compile("^-?\\d+$");

    //  to validate car name
    public static boolean validate_CarName(String name_OfCar) {
        boolean ck_ptrn = PTRN_FOR_CAR_NAME.matcher(name_OfCar).matches();
        if (!ck_ptrn) {
            System.out.println("Car name os invalid! Enter proper car name.Apologies, please try again.\n");
        }
        return ck_ptrn;
    }
// to validate integer for while loop cases
    public static boolean validate_Integer(int user_Response) {
        String response_String = String.valueOf(user_Response);
        boolean is_Valid = PTRN_FOR_INTEGER.matcher(response_String).matches();

        if (!is_Valid) {
            System.out.println("Not a valid response. Please try again.");
        }

        return is_Valid;
    }

    // Method to validate date
    public static boolean is_Previous_Date(String input_Date) {
    	
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(input_Date);
            Date currentDate = new Date();

            // Check if the parsed date is before the current date
            return date.before(currentDate);
        } catch (ParseException e) {
            // If there is a parsing error, handle it accordingly
            System.out.println("Invalid date format: " + input_Date);
            return false;
        }
    }
    //static Pattern PTRN_FOR_DATE = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$"); //date formate update
    public static boolean validate_Date(String inputDate)
    {
    	boolean ck_ptrn=false;
        if (is_Previous_Date(inputDate)) {
            System.out.println("The date is a previous date.");
        } else 
        {
        	  ck_ptrn = PTRN_FOR_DATE.matcher(inputDate).matches();
             if (!ck_ptrn) {
                 System.out.println("date format is invalid. Please use the format dd/mm/yy. Kindly try again.");
             }
             return ck_ptrn;
        }
        return ck_ptrn;
    }
   

    // Method to validate time
    public static boolean validate_Time(String time) {
        boolean ck_ptrn = PTRN_FOR_TIME.matcher(time).matches();
        if (!ck_ptrn) {
            System.out.println("Time format is invalid. Please use the format HH:MM. Kindly try again.");
        }
        return ck_ptrn;
    }

    // Method to validate city name
    // it first checks it is word or not then it checks with dictionary of words and gives similar word
    public static boolean validate_City_Name(String cityName) {
        boolean check=false; 
       boolean str = PTRN_NAME_FOR_CITY.matcher(cityName).matches();
        if(str)
        {
        try 
        {
			SpellChecking.initializeDictionary("C:\\acc labs\\webscrapercarrental\\data\\dictionart.txt");
			 WordCompletion.initializeDictionary("C:\\acc labs\\webscrapercarrental\\data\\dictionart.txt");
			 check = SpellChecking.checkSpelling(cityName);
			 //System.out.println(check);
		} catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(check==false)
        {
        	System.out.println("Suggestions: for u r word");
        List<String> suggestions = WordCompletion.getSuggestions(cityName.toLowerCase());
       

        if (!suggestions.isEmpty())
        {
            

            for (int i = 0; i < suggestions.size(); i++)
            {

                System.out.println((i + 1) + ". " + suggestions.get(i));
            }
        }
        System.out.println("please select one from above.");
        }
        return check;
        }
        System.out.println("please type word correctly with letters");
        return str;
    }

    // Method to validate user response
    public static boolean validate_User_Response(String input) {
        if (input.length() == 1 && (input.charAt(0) == 'y' || input.charAt(0) == 'n')) {
            return true;
        } else {
            System.out.print("Invalid input. Please enter 'y' or 'n'\n");
            return false;
        }
    }
  // validating given input is number or not
    public static boolean validate_Range_Input(String input) {
        boolean check = RANGE_PATTERN.matcher(input).matches();

        if (!check) {
            System.out.println("Not a valid range. Please try again.");
        }
        return check;
    }
 // validating transimmiosn types
    public static boolean validate_TTypes(String preferredTransmission)
    {
        boolean check = SINGLE_LETTER_PATTERN.matcher(preferredTransmission).matches();
        if (!check){
            System.out.println("Invalid Selected Type. Please try again.");
        }
        return check;
    }
}
