package csci310;

public class Porfolio_jsons_creator {
	
	// Have a hashmap var that has each stock and quantity
	boolean check = false;

	public void jsons_creator(String userid)
	{
		 check = true; //for test
		
		//here I input userID to get all stocks and quantity/stock the ass. userid has
		// append it to the hashmap var
		
		week_json_creator();
		
		month_json_creator();
		
		year_json_creator();
		
	}
	
	public void week_json_creator()
	{
		check = true;
		// this fn creates the json for the week graph
	}
	
	public void month_json_creator()
	{
		check = true;
		// this fn creates the json for the month graph
	}
	
	public void year_json_creator()
	{
		check = true;
		// this fn creates the json for the year graph
	}
}
