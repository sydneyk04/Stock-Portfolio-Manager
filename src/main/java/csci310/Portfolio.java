package csci310;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Portfolio {
	private ArrayList<PortfolioStock> stocks;
	private String username;
	private boolean dataFetched;
	private double value;
	
	public Portfolio (String username) throws InterruptedException {
		this.username = username;
		value = 0.00;
		dataFetched = false;

		fetchData();
	}
	
	@SuppressWarnings("deprecation")
	public FirebaseApp initializeFirebase(String filename) {
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount;
			try {
				serviceAccount = new FileInputStream(filename);
				FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://stock16-e451e.firebaseio.com").build();
				return FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	
	/*
	 * Fetch stock-related data from Yahoo Finance API.
	 * The portfolio is represented by an ArrayList[PortfolioStock],
	 * where the class PortfolioStock contains (stockSymbol, stockName, stockShares).
	 */
	public void fetchData() throws InterruptedException {
		System.out.println("fetching portfolio data");
		dataFetched = false;
		initializeFirebase("stock16-service-account.json");
		final ArrayList<PortfolioStock> data = new ArrayList<>();
		
		// Fetch firebase portfolio data
		final DatabaseReference portfolioRef = FirebaseDatabase.getInstance().getReference()
				.child("users")
				.child(username)
				.child("portfolio");
		
		portfolioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.exists()) {				
					System.out.println("Number of stocks in portfolio: " + snapshot.getChildrenCount());
					for (DataSnapshot ds : snapshot.getChildren()) {                    
						String stockSymbol = ds.getKey();
                        String stockName = (String) ds.child("name").getValue();
                        Double stockShares = (Double) ds.child("shares").getValue();
                        
                        System.out.println(stockSymbol + " | " + stockName + " | " + stockShares);
                        data.add(new PortfolioStock(stockSymbol, stockName, stockShares));
                    }
					
					dataFetched = true;
				}
			}
	
			@Override
			public void onCancelled(DatabaseError error) {
				System.out.println(error.getMessage());
				dataFetched = false;
			}
		});
		
		// Wait for Firebase data to be fetched
		for (int i = 0; i < 20; ++i) {
			TimeUnit.SECONDS.sleep(1);
			if (dataFetched) {
				System.out.println("Data fetched at: " + i);
				break;
			}
		}	
		
		stocks = data;
	}
	
	public void calculateValue() {
		for (PortfolioStock stock : stocks) {
			value += stock.getTotalValue();
		}
	}
	
	public Boolean getDataFetched() {
		return dataFetched;
	}
	
	public ArrayList<PortfolioStock> getStocks() {
		return stocks;
	}
	
	public double getValue() {
		return value;
	}
	
}
