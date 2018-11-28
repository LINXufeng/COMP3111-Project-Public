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
	@Test
	public void testGetTitle() {
		Item i = new Item();
		i.setTitle("Test");
		assertEquals(i.getTitle(), "Test");
	}
	@Test
	public void testGetPrice() {
		Item i = new Item();
		i.setPrice(100);
		assertEquals(i.getPrice(), 100, 0.000001);
	}
	@Test
	public void testGetUrl() {
		Item i = new Item();
		i.setUrl("https://www.google.com.hk/");
		assertEquals(i.getUrl(), "https://www.google.com.hk/");
	}
	@Test
	public void testGetPortal() {
		Item i = new Item();
		i.setPortal("DCFever");
		assertEquals(i.getPortal(), "DCFever");
	}
	@Test
	public void testCompareTo() {
		Item c = new Item();
		Item d = new Item();
		c.setPortal("Craigslist");
		c.setPrice(100);
		d.setPortal("DCFever");
		d.setPrice(100);
		assertEquals(c.compareTo(d),-1);
		c.setPrice(200);
		assertEquals(c.compareTo(d),1);
		
	}
	@Test
	public void testCompareDate() {
		Item a = new Item();
		Item b = new Item();
		a.setPostedDate("2018-01-01 00:00", "yyyy-MM-dd hh:mm");
		b.setPostedDate("2018-01-02 00:00", "yyyy-MM-dd hh:mm");
		assertEquals(a.compareDate(a),0);
		assertEquals(a.compareDate(b),-1);
		assertEquals(b.compareDate(a),1);
	}

}
