package comp3111.webscraper;


import org.junit.Test;
import java.util.List;
import java.util.Vector;
import static org.junit.Assert.*;

public class WebScraperTest {
	private WebScraper w = new WebScraper();
	
	@Test
	public void testSetDomainDefault() {
		w.setDomain("Craigslist");
		assertEquals("Craigslist", w.getDomain());
	}
	@Test
	public void testSetDomainSupported() {
		w.setDomain("DCFever");
		assertEquals("DCFever", w.getDomain());
	}
	@Test
	public void testSetDomainUnsupported() {
		String s = w.getDomain();
		w.setDomain("giving.ust.hk");
		assertEquals(s, w.getDomain());
	}
	
	@Test
	public void testGetEmptyListTest() {
		List l = w.getEmptyList();
		assertEquals(new Vector<Item>(), l);
	}
	
	@Test
	public void testFetchResultCountCraigslistNoResult() {
		w.setDomain("Craigslist");
		int i = w.fetchResultCount("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName");
		assertEquals(i, 0);
	}
	@Test
	public void testFetchResultCountCraigslistHaveResult() {
		w.setDomain("Craigslist");
		int i = w.fetchResultCount("note 9");
		assertTrue(i != 0);
	}
	@Test
	public void testFetchResultCountDCFeverNoResult() {
		w.setDomain("DCFever");
		int i = w.fetchResultCount("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingDCFeverAsNoOneInHongKongIsGoingToGiveTheirProductLongEnglishName");
		assertEquals(i, 0);
	}
	@Test
	public void testFetchResultCountDCFeverHaveResult() {
		w.setDomain("DCFever");
		int i = w.fetchResultCount("note 9");
		assertTrue(i != 0);
	}
	
	@Test
	public void testNextPageNoResult() {
		w = new WebScraper();
		w.scrape("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName");
		assertTrue(!w.nextPage());
	}
	@Test
	public void testNextPageHasNextPage() {
		w = new WebScraper();
		w.scrape("note 9");
		assertTrue(w.nextPage());
	}
	@Test
	public void testNextPageNoNextPage() {
		w = new WebScraper();
		int i = w.fetchResultCount("note 9");
		int count = 0;
		while(true) {
			if (w.nextPage()) {
				count++;
			} else {
				break;
			}
		}
		// Have i - 1 upcoming pages
		assertEquals(count, i - 1);
	}

	@Test
	public void testScrapeNoResult() {
		w.setDomain("Craigslist");
		List l = w.scrape("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName");
		assertEquals(new Vector<Item>(), l);
	}
	
	@Test
	public void testScrapeCraigslistHasResult() {
		w.setDomain("Craigslist");
		List l = w.scrape("note 9");
		// scrape return single page of result only, until nextPage() is called to switch page
		assertEquals(l.size(),120);
//		assertTrue(l.size() == 120);
	}
	@Test
	public void testScrapeDCFeverHasResult() {
		w.setDomain("DCFever");
		List l = w.scrape("note 9");
		// scrape return single page of result only, until nextPage() is called to switch page
//		assertTrue(l.size() == 30);
		assertEquals(l.size(),30);
	}
	@Test
	public void testScrapeHasNoResult() {
		w.setDomain("Craigslist");
		w.setPageCount(-1);
		List l = w.scrape("note 9");
		assertEquals(l.size(),120);
		w.setDomain("DCFever");
		w.setPageCount(-1);
		l = w.scrape("note 9");
		assertEquals(l.size(),30);
	}
	@Test
	public void testSetPageCount() {
		w.setPageCount(10);
		assertEquals(w.getPageCount(),10);
	}
	@Test
	public void testGetPageCount() {
		w.setPageCount(10);
		assertEquals(w.getPageCount(),10);
	}
}