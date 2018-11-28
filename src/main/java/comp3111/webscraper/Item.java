package comp3111.webscraper;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;


//public class Item {
public class Item implements Comparable<Item>{ //edited by tony for sorting
	private String title ; 
	private double price ;
	private String url ;
	/**
	 * which portal it comes from
	 * @author Tony
	 */
	private String portal; //where does it come from
	//end of tony
	/*
	 * @author Linus
	 * Variables for parsing posted date
	 */
	private Date postedDate;
	private String postedDateString;
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	/*
	 * End of variables for parsing posted date
	 */
	/**
	 * get the title of the item
	 * @return the title of the item
	 * @author tonychan
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * set the title of the item
	 * @param title
	 * @author tonychan
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * get the price of the item
	 * @return the price of the item
	 * @author tonychan
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * set the price of the item
	 * @param price
	 * @author tonychan
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * get the URL of the item
	 * @return the URL of the item
	 * @author tonychan
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * set the URL of the item
	 * @param url
	 * @author tonychan
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * get the portal of the item
	 * @author Tony
	 * @return the portal of the item
	 */
	public String getPortal() {
		return portal;
	}
	/**
	 * set the portal
	 * @author Tony
	 * @param portal
	 */
	public void setPortal(String portal) {
		this.portal = portal;
	}
	//end of tony
	
	/**
	 * Set posted date of item
	 * @author Linus
	 * @param date - posted date in string
	 * @param format - posted date format
	 */
	public void setPostedDate(String date, String format) {
		SimpleDateFormat parseString = new SimpleDateFormat(format, Locale.ENGLISH);
		try {
			this.postedDate = parseString.parse(date);
		} catch (Exception e) {
			this.postedDate = null;
		}
		this.postedDateString = this.processPostedDate(DEFAULT_DATE_FORMAT);
	}
	/**
	 * Generate processed posted date in string
	 * @author Linus
	 * @param format - returned posted date format
	 * @return Posted date in the format specified, N/A if it fails to format
	 */
	private String processPostedDate(String format) {
		SimpleDateFormat parseDate = new SimpleDateFormat(format, Locale.ENGLISH);
		try {
			String result = parseDate.format(this.postedDate);
			return result;
		} catch (Exception e) {
			return "N/A";
		}
	}
	/**
	 * Get posted date of item
	 * @author Linus
	 * @return Posted date in format yyyy-MM-dd
	 */
	public String getPostedDate() {
		return this.postedDateString;
	}
	/*
	 * End of functions for posted date
	 */
	//tony
	/**
	 * Compare the items for sorting. The returned result is sorted by price. If two items have the same price, 
	 * item sold on Craiglist go first. If two items from the same portal has the same price, they are sorted
	 */
	@Override 
    public int compareTo(Item item) {  
		if(this.price == item.getPrice())
			return (this.portal).compareTo(item.getPortal());
		else
			return new Double(this.price).compareTo(new Double(item.getPrice()));
    }  
	//end of tony
}