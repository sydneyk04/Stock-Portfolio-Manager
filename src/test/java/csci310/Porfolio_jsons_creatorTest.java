package csci310;

import static org.junit.Assert.*;

import org.junit.Test;

public class Porfolio_jsons_creatorTest {

	@Test
	public void jsons_creator_test() {
		
		Porfolio_jsons_creator pjc = new Porfolio_jsons_creator();
		pjc.jsons_creator("a");
		assertTrue(pjc.check==true);
	}
	
	@Test
	public void jsons_week_test() {
		
		Porfolio_jsons_creator pjc = new Porfolio_jsons_creator();
		pjc.week_json_creator();
		assertTrue(pjc.check==true);
	}
	
	@Test
	public void jsons_month_test() {
		
		Porfolio_jsons_creator pjc = new Porfolio_jsons_creator();
		pjc.month_json_creator();
		assertTrue(pjc.check==true);
	}
	
	@Test
	public void jsons_year_test() {
		
		Porfolio_jsons_creator pjc = new Porfolio_jsons_creator();
		pjc.year_json_creator();
		assertTrue(pjc.check==true);
	}

}
