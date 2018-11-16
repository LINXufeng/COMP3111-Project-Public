package comp3111.webscraper;

import java.net.URLEncoder;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.Vector;


/**
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br/>
 * In this particular sample code, it access to craigslist.org. You can directly search on an entry by typing the URL
 * <br/>
 * https://newyork.craigslist.org/search/sss?sort=rel&amp;query=KEYWORD
 *  <br/>
 * where KEYWORD is the keyword you want to search.
 * <br/>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; section class="page-container" &rarr; form id="searchform" &rarr; div class="content" &rarr; ul class="rows" &rarr; any one of the multiple 
 * li class="result-row" &rarr; p class="result-info". You might see something like this:
 * <br/>
 * <pre>
 * {@code
 *    <p class="result-info">
 *        <span class="icon icon-star" role="button" title="save this post in your favorites list">
 *           <span class="screen-reader-text">favorite this post</span>
 *       </span>
 *       <time class="result-date" datetime="2018-06-21 01:58" title="Thu 21 Jun 01:58:44 AM">Jun 21</time>
 *       <a href="https://newyork.craigslist.org/que/clt/d/green-star-polyp-gsp-on-rock/6596253604.html" data-id="6596253604" class="result-title hdrlnk">Green Star Polyp GSP on a rock frag</a>
 *       <span class="result-meta">
 *               <span class="result-price">$15</span>
 *               <span class="result-tags">
 *                   pic
 *                   <span class="maptag" data-pid="6596253604">map</span>
 *               </span>
 *               <span class="banish icon icon-trash" role="button">
 *                   <span class="screen-reader-text">hide this posting</span>
 *               </span>
 *           <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
 *           <a href="#" class="restore-link">
 *               <span class="restore-narrow-text">restore</span>
 *               <span class="restore-wide-text">restore this posting</span>
 *           </a>
 *       </span>
 *   </p>
 *}
 *</pre>
 * <br/>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()). It also extracts  
 * 
 *
 */
public class WebScraper {

	private static final String DEFAULT_URL = "https://newyork.craigslist.org/";
	private WebClient client;
	
	/*
	 * @author Linus
	 * Extra variables for handling pagination
	 */
	private static final int DEFAULT_PAGE_MAX = 120;
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm";
	// currentCount tells the index of first item of current page
	private int currentCount = -1;
	// itemCount tells the total number of results
	private int itemCount = -1;
	// pageCount tells the total number of pages
	private int pageCount = -1;
	/*
	 * End of extra variables for handling pagination
	 */

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}
	
	/* method fetchResultCount
	 * @author Linus
	 * This function will return the number of pages of search results using the keyword provided
	 * Currently only works on the DEFAULT page (Craigslist)
	 */
	public int fetchResultCount(String keyword) {
		try {
			String searchUrl = DEFAULT_URL + "search/sss?sort=rel&query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);

			// Find the <span> that contains number of results
			HtmlElement spanTotalCount = (HtmlElement)page.getFirstByXPath("//span[@class='totalcount']");
			// Calculate number of pages of results
			itemCount = spanTotalCount == null ? 0 : Integer.parseInt(spanTotalCount.asText());
			pageCount = (int)Math.ceil(itemCount / 120.0);
			//System.out.println("Number of pages found: " + Integer.toString(pageCount));
			currentCount = -1;
		} catch (Exception e) {
			//System.out.println(e);
			return -1;
		}
		//System.out.println("Number of results:" + Integer.toString(itemCount));
		return pageCount;
	}
	/*
	 * End of method fetchResultCount
	 */
	
	/* method nextPage
	 * @author Linus
	 * Add the currentCount by max number of items per page
	 * return true if another page should exists
	 * return false if no more pages
	 */
	public boolean nextPage() {
		currentCount += DEFAULT_PAGE_MAX;
		if (currentCount >= itemCount) {
			return false;
		}
		return true;
	}
	/*
	 * End of method nextPage
	 */
	
	/* method getEmptyList
	 * @author Linus
	 * Just an helper function to help instantiating an empty list in the correct list type
	 * Which by default is Vector
	 */
	public List<Item> getEmptyList() {
		return new Vector<Item>();
	}
	/*
	 * End of method getEmptyList
	 */
	
	/**
	 * The only method implemented in this class, to scrape web content from the craigslist
	 * 
	 * @param keyword - the keyword you want to search
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	/*
	 * Heavily modified to return results on the current page only based on the current item count variables defined above
	 */
	public List<Item> scrape(String keyword) {
		// This function currently only work on Craigslist
		//System.out.println("Scraper: "+keyword);
		try {
			if (pageCount == -1) {
				fetchResultCount(keyword);
			}
			String searchUrl = DEFAULT_URL + "search/sss?sort=rel&query=" + URLEncoder.encode(keyword, "UTF-8");
	
			HtmlPage page;
			List<?> items;			
			Vector<Item> searchResult = new Vector<Item>();

			// 120 is the max amount of items shown in Craigslist page
			int currentPage = (int)Math.ceil(currentCount / 120.0);
			page = client.getPage(searchUrl + "&s=" + Integer.toString(currentPage * 120));
			//System.out.println("Page: " + page);
			items = (List<?>) page.getByXPath("//li[@class='result-row']");
			// Loop through each grids, based on skeleton code
			for (int i = 0; i < items.size(); i++) {
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
				// spanPrice will fetch the <span class='result-price'> element in that grid
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
				// postedDate will fetch the <time class='result-date'> element in that grid
				HtmlElement postedDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='result-date']"));
				
				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

				Item item = new Item();
				item.setTitle(itemAnchor.asText());
				item.setUrl(itemAnchor.getHrefAttribute());
				
				// Parse the posted date using the date format defined above
				item.setPostedDate(postedDate.getAttribute("datetime"), DEFAULT_DATE_FORMAT);

				// Set the price to the item object
				item.setPrice(new Double(itemPrice.replace("$", "")));
				//System.out.print("XXXXX" + item); // bug
				searchResult.add(item);
			}
			client.close();
			//System.out.print(">>>>>>>>>>>>>>>"+searchResult.isEmpty());
			return searchResult;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
