package comp3111.webscraper;


import org.junit.Test;
import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	@Test
	public void testSetPrice() {
		Item i = new Item();
		i.setPrice(0.0);
		assertEquals(i.getPrice(), 0.0, 0.000001);
	}
	@Test
	public void testSetUrl() {
		Item i = new Item();
		i.setUrl("https://w5.ab.ust.hk/jsdm/donation_form");
		assertEquals(i.getUrl(), "https://w5.ab.ust.hk/jsdm/donation_form");
	}
	@Test
	public void testSetPostedDateValid() {
		Item i = new Item();
		i.setPostedDate("2018-01-01 00:00", "yyyy-MM-dd hh:mm");
		assertEquals(i.getPostedDate(), "2018-01-01");
	}
	@Test
	public void testSetPostedDateInvalid() {
		Item i = new Item();
		i.setPostedDate("00:00 2018-01-01", "yyyy-MM-dd hh:mm");
		assertEquals(i.getPostedDate(), "N/A");
	}
	@Test
	public void testSetPortal() {
		Item i = new Item();
		i.setPortal("Craigslist");
		assertEquals(i.getPortal(), "Craigslist");
	}
}
