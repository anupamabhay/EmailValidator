import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Scanner;

public class EmailValidation {
    public static void main(String[] args) {
        //User input
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email: ");
        String email = sc.next();

        //Api key
        var api = "YOUR_API_KEY"; //get your free api key at abstractapi.com
        //base url
        var url = "https://emailvalidation.abstractapi.com/v1/?api_key="+api+"=";

        //API request and response
        HttpResponse<String> response = Unirest.get(url+email)
                .asString();
        String result = response.getBody();
        //System.out.println(result);

        //Create JsonObject(s) to access object entries
        JsonObject jObj = JsonParser.parseString(result).getAsJsonObject();
        JsonObject jIsFormatted = jObj.get("is_valid_format").getAsJsonObject();
        JsonObject jIsFree = jObj.get("is_free_email").getAsJsonObject();
        JsonObject jIsDisposable = jObj.get("is_disposable_email").getAsJsonObject();

        //Grab entries from nested object(s)
        String isFormatted = jIsFormatted.get("text").toString();
        String isFree = jIsFree.get("text").toString();
        String isDisposable = jIsDisposable.get("text").toString();
        String autocorrect = jObj.get("autocorrect").toString();
        String isDeliverable = jObj.get("deliverability").toString();
        String confidenceStr = jObj.get("quality_score").toString();

        //Remove "" from String(s) and parse it as Float for confidence calculation
        confidenceStr = confidenceStr.replaceAll("\"","");
        Float confidence =  Float.parseFloat(confidenceStr);
        autocorrect = autocorrect.replaceAll("\"","");

        //Print response
        System.out.println("\nEmail: "+email);
        if(!autocorrect.equals("")){
            System.out.println("Typo detected in email! Did you mean: "+autocorrect+"\nPlease enter your email correctly!\n");
        }
        System.out.println("Confidence: "+confidence*100 + "%"
                + "\nDeliverability: "+isDeliverable
                + "\nFormatted: "+isFormatted
                + "\nFree: "+isFree
                + "\nDisposable: "+isDisposable);
    }
}
