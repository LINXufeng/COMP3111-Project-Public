/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;


import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import javafx.scene.web.WebView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.MenuItem;


/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {

    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private TextArea textAreaConsole;
    
    /*
     * @author Linus
     * Variables for Task 4
     */
    // Refers to the "Go" button
    @FXML
    private Button searchBtn;
    
    // Refers to the table in "Table" tab
    @FXML
    private TableView tableViewTable;
    
    // Refer to each columns in the table tableViewTable
    @FXML
    private TableColumn<Item, String> tableViewTitleCol;
    @FXML
    private TableColumn<Item, String> tableViewPriceCol;
    @FXML
    private TableColumn<Item, String> tableViewUrlCol;
    @FXML
    private TableColumn<Item, String> tableViewPostedDateCol;
    
    
    // List of Item that store the search result
    private List<Item> result;
    /*
     * End of variables for Task 4
     */
    
    private WebScraper scraper;
    
    // private ObservableList<Item> observe;
    
    
    
	/**
	 * The variables below are created by Felix
	 * @author Felix
	 * @param LastSearchFXId - which is the id of Menu Item "Last Search"
	 * @param Button - which is the id of Button "Refine"
	 * @param anotherRoot - which is the root is used to show team information
	 * @param anotherStage - which is stage is used to show team information
	 * @param abountOurTeam_FILE - which is the path is the GUI of team information
	 * @param currentKeyword - which is the variable track current search record
	 * @param searchRecord - which is the ArrayList<String> used to record search history
	 * @param currentKeyword - which is the variable track current search record
	 * 
	 */
    @FXML
    private MenuItem LastSearchFXId;
    @FXML
    private Button refineButton;
    @FXML 
    private Node anotherRoot;
    private Stage anotherStage = new Stage();
    private static final String abountOurTeam_FILE = "/aboutOurTeam.fxml";
    private String currentKeyword = null;
    private ArrayList<String> searchRecord = new ArrayList<String>();
    /**
     * This method was used to print out history of search  keyword
     * @author Felix
     * 
     */
    public void CheckSearchRecord() {
    	for(String record:searchRecord) {
    		System.out.print(record +" ");
    	}
    }
    

    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    	result = scraper.getEmptyList();
    	// observe = FXCollections.observableList(result);
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	
    }
    
    
    /**
     * The method is fired by click button "Go"
     * 1. The method get keyword from text field 
     * 2. The method invoke method searchAndTabularization to show information on console and table
     * 3. The method add keyword to "searchRecord" list
     * 4. The method invoke method CheckSearchReord check if the keyword just searched has been added 
     * into "searchRecord" list successfully
     * 5. The method set 'last search' button enable if there are at least two items(current and last) in search record
     * 6. The method make the button Refine enabled after a new search ( e.g. Go button is clicked).
     * @author Felix, Linus, Tony
     * 
     */
    @FXML
    private void actionSearch() {
    	String keyword = textFieldKeyword.getText();
    	
    	// print out the keyword you input
    	System.out.println("actionSearch: " + keyword);
    	
    	// invoke method searchAndTabularization to show information on console and table
		searchAndTabularization(keyword);
    	// add keyword to "searchRecord" list
    	currentKeyword = textFieldKeyword.getText();
    	searchRecord.add(textFieldKeyword.getText());
    	// check if the keyword just searched has been added into "searchRecord" list successfully
    	CheckSearchRecord();
    	// set 'last search' button enable if there are at least two items(current and last) in search record
    	if(searchRecord.size()>=2) {
    		setLastSearchEnable();
    	}
    	else{
    		setLastSearchDisable();
    	}
    	// Make the button Refine enabled after a new search ( e.g. Go button is clicked).
    	setRefineEnable();
	  } 

    
    
    
    
    
    
    
    /**
     * The method is fired by click menu item "Last Search"
     * 1. Check if the lastRecord is the keyword you just search, if yes, use keyword before it
     * 2. The method revert your search result to the previous search.
     * 3. Before any keyword is being searched, or after Last Search is clicked once, 
	 * it should be disabled (gray and unable to be clicked).
     * 4. Last Search will be enabled after a new search ((Go) button is clicked).
     * @author Felix
     * 
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    	// get last search keyword from search record array list  
    	String lastSearchKeyword = searchRecord.get(searchRecord.size() - 1);
    	// if the lastRecord is the keyword you just search, use keyword before it
    	System.out.println("current keyword is "+currentKeyword);
    	System.out.println("lastSearch Keyword is "+lastSearchKeyword);
    	if(lastSearchKeyword.equals(currentKeyword)) {
    		lastSearchKeyword = searchRecord.get(searchRecord.size() - 2);
    	}
    	
    	System.out.println("Before poping last search keyword");
    	CheckSearchRecord();
    	System.out.println("you are invoking last search, the last search keyword is "+lastSearchKeyword);
    	
    	// use this keyword do research
    	textFieldKeyword.setText(lastSearchKeyword);
    	System.out.println("actionSearch: " + lastSearchKeyword);
    	List<Item> ConsoleResult = scraper.scrape(lastSearchKeyword);
    	String output = "";
    	for (Item item : ConsoleResult) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	// Attempt to search  	
    	searchAndTabularization(lastSearchKeyword);
    	// after finish research, pop it 
    	searchRecord.remove(lastSearchKeyword);
    	System.out.println("after poping last search keyword");
    	CheckSearchRecord();
    	// set 'last search' button disable if there is no any search record
    	// otherwise, enable 'last button' search button
    	if(searchRecord.size()<=1) {
    		setLastSearchDisable();
    	}
    	else{
    		setLastSearchEnable();
    	}
    	// Make the button Refine enabled after a new search ( e.g. Go button is clicked).[done!]
    	setRefineEnable();
    	
    }

    
    
    /**
     * The method is fired by clicking menu item "about your team"
     * The method make about our Team showing a new simple dialog that shows all your team members name, 
     * itsc account, and github account.
     * @author Felix
     * 
     */
    @FXML
    private void actionAOT() {
    	System.out.println("actionAOT");
    	try {
    		FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(getClass().getResource(abountOurTeam_FILE));
    		Parent anotherRoot = loader.load();
            anotherStage.setTitle("About Our Team");
            anotherStage.setScene(new Scene(anotherRoot, 600, 329));
            anotherStage.show();
        } 
    	catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * The method is fired by clicking menu item "Quit"
     * Make Quit button exiting the program and close all connections.
     * @author Felix
     */
    @FXML
    private void actionQuit() {
    	System.out.println("actionQuit");
    	actionClose();
    	scraper = null;
        System.exit(0);
    }
    
    
    
    
    
    
    /**
     * The method is fired by clicking menu item "Close"
     * The method will clear the current search record and initialize all 
     * tabs on the right to their initial state.[5]
     * @author Felix
     * 
     */
    @FXML
    private void actionClose() {
    	System.out.println("actionClose");
    	// close team information GUI 
    	anotherRoot = null;
    	// set refine button and last search button disable
    	setRefineDisable();
    	setLastSearchDisable();
    	// clear searchRecord
    	searchRecord = new ArrayList<String>();
    	// clear current keyword
    	currentKeyword = null;
    	// clear input text and result console to null
    	textAreaConsole.textProperty().unbind();
    	textFieldKeyword.setText("");
    	textAreaConsole.setText("");
    	tableViewTable.setItems(null);
    	result = scraper.getEmptyList();
    	task1(labelCount,labelPrice,labelMin,labelLatest,result);
    	// close about our team window
    	anotherStage.close();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * The method is fired by clicking button "Refine"
     * The method searched data and keep those items with their titles 
     * containing the keywords typed in the text area.
	 * And then it update all tabs on the right. 
	 * Make the button Refine disabled (grey) before any keyword is being searched, 
	 * or after Refine is clicked once.
	 * Make the button Refine enabled after a new search 
     * @author Felix
     */
    @FXML
    private void actionRefine() {
    	System.out.println("actionRefine");
    	List<Item> correctResult = new Vector<Item>();
    	String output = "";
    	for (Item item : result) {
    		if(item.getTitle().toLowerCase().contains(textFieldKeyword.getText().toLowerCase())) {
    			correctResult.add(item);
    			output += item.getTitle() + "\t\t" + "HKD" + item.getPrice() + "\t" + item.getPortal() + "\t\t" + item.getUrl() + "\n";
    		}
    	}
    	textAreaConsole.setText(output);
    	tableViewTable.setItems(FXCollections.observableList(correctResult));
		task1(labelCount,labelPrice,labelMin,labelLatest,correctResult);
    	tableViewTable.refresh();
    	setRefineDisable();
    }
    
    /**
     * This is a helper method to disable Refine button
     * @author Felix
     */
    private void setRefineDisable() {
    	refineButton.setDisable(true);
    }
    /**
     * This is a helper method to enable Refine button
     * @author Felix
     */
    private void setRefineEnable() {
    	refineButton.setDisable(false);
    }

    /**
     * This is a helper method to disable last search button
     * @author Felix
     */
    private void setLastSearchDisable() {
    	LastSearchFXId.setDisable(true);
    }
    /**
     * This is a helper method to enable last search button
     * @author Felix
     */
    private void setLastSearchEnable() {
    	LastSearchFXId.setDisable(false);
    }
    
    
    
    
    
    
    
    
    
    /**
     * Define how to search with a keyword, used for Task 3
     * @author Linus
     */
    class searchTask extends Task<String> {
    	private String keyword;
    	
    	public searchTask(String keyword) {
    		this.keyword = keyword;
    	}

    	@Override
    	protected String call() throws Exception {
    		System.out.println("Searching");
    		scraper.setDomain("Craigslist");
    		int currentPage = 1;
    		int totalPage = scraper.fetchResultCount(keyword);
    		if (totalPage > 0) {
	    		// Loop through pages until there is no pages left (scraper.nextPage() == false)
	    		do {
	    			// Add Items scraped by scraper to the list
	    			
	    			result.addAll(scraper.scrape(keyword));
	    			String output = textAreaConsole.getText() + "Finished scraping page " + Integer.toString(currentPage) + "/" + Integer.toString(totalPage) + " on Craigslist...\n";
	    			System.out.println("Finished scraping page " + Integer.toString(currentPage) + "/" + Integer.toString(totalPage) + " on Craigslist...\n");
	    			// Return the message to textAreaConsole and update it
	    			updateMessage(output);
	    			currentPage += 1;
	    		} while (scraper.nextPage());
    		}
    		scraper.setDomain("DCFever");
    		currentPage = 1;
    		totalPage = scraper.fetchResultCount(keyword);
    		if (totalPage > 0) {
	    		// Loop through pages until there is no pages left (scraper.nextPage() == false)
	    		do {
	    			// Add Items scraped by scraper to the list
	    			
	    			result.addAll(scraper.scrape(keyword));
	    			String output = textAreaConsole.getText() + "Finished scraping page " + Integer.toString(currentPage) + "/" + Integer.toString(totalPage) + " on DC Fever...\n";
	    			// Return the message to textAreaConsole and update it
	    			updateMessage(output);
	    			currentPage += 1;
	    		} while (scraper.nextPage());
    		}
    		// totalPage = scraper.fetchResultCount(keyword);
    		updateMessage(textAreaConsole.getText() + "Finished scraping.\n");
    		return "";
    	}
    }
    
    /**
     * Defines the behavior of cells in the URL column
     * @author Linus
     */
    class urlCell extends TableCell<Item, String> {
    	@Override
    	public void updateItem(String item, boolean empty) {
    		super.updateItem(item, empty);
    		// Set the text in the cell, if no URL is available, it will be blank
    		setText(empty ? null : getString());
    		setGraphic(null);
    	}
    	
    	private String getString() {
    		return getItem() == null ? "" : getItem().toString();
    	}
    }
    
    /**
     * Open browser
     * @author Linus
     * @param url - URL to be browsed
     */
    private void callBrowser(String url) {
    	try {
    		WebView web = new WebView();
    		web.getEngine().load(url);
    		Scene scene = new Scene(web);
    		Stage browser = new Stage();
    		browser.setScene(scene);
    		browser.show();
    	} catch (Exception e) {
			System.out.println("Failed to open URL:");
			e.printStackTrace();
    	}
    }
    

    /**
     * Defines the event handler of URL cell when clicked
     * @author Linus
     */
    class urlCellHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent t) {
    		TableCell c = (TableCell) t.getSource();
    		// Call browser
    		callBrowser(c.getItem().toString());
    	}
    }
    
    
    /**
     * Start searching and store the results into table and console
     * @author Felix, Linus, Tony
     * @param lastSearchKeyword - keyword to be searched
     */
    private void searchAndTabularization(String lastSearchKeyword){
//    	System.out.println("entered searchAndTabulazation"); //debug
    	result = scraper.getEmptyList();
    	try {
			// Create a task for background searching operation
			searchTask search = new searchTask(lastSearchKeyword);
			// Bind the console to the search task
			textAreaConsole.textProperty().bind(search.messageProperty());
			// Disable the "Go", "Last Search", "Refine" button when searching
			search.setOnRunning((succeesesEvent) -> {
				searchBtn.setDisable(true);
				setRefineDisable();		
				
			});
//			System.out.println("before setonsucceedded"); //debug
			// Enable the "Go", "Last Search", "Refine" button when searching
			search.setOnSucceeded((succeededEvent) -> {
				System.out.println("no. of items:" + result.size());
				//tony
//				System.out.println("running setOnsucceeded"); //debug
				if(result.size()!=0) {
//					System.out.println("runing tono0"); //debug
			    	result = toNoZeroPrice(result); //exclude the $0 items
				}
				if(result.size()!=0) {
//					System.out.println("running sorting");//debug
			    	Collections.sort(result); //sort the list
				}
				//
				tableViewTable.setItems(FXCollections.observableList(result));
				searchBtn.setDisable(false);
				setRefineEnable();		
				tableViewTable.refresh();
				textAreaConsole.textProperty().unbind();
				//textAreaConsole.appendText("testing");
				
			    
		    	/*
		    	 * @author Tony
		    	 */
			    String output = "";
		    	for (Item item : result) {
		    		output += item.getTitle() + "\t\t" + "HKD" + item.getPrice() + "\t" + item.getPortal() + "\t\t" + item.getUrl() + "\r\n";
		    	}
		    	textAreaConsole.setText(output);
//		    	if (result.size() != 0) {
//		    		System.out.println("running 1"); //debug
		    		task1(labelCount,labelPrice,labelMin,labelLatest,result);
//		    	}
		    	//end of tony
			});
			
			ExecutorService executor = Executors.newFixedThreadPool(1);
			// Run the task
			executor.execute(search);
			// Stop the task after finishing
			executor.shutdown();
		} catch (Exception e) {
			textAreaConsole.setText(e.toString());
		}
		
		// Assign which field of Item each columns should show
	    tableViewTitleCol.setCellValueFactory(new PropertyValueFactory("title"));
	    tableViewPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
	    tableViewPostedDateCol.setCellValueFactory(new PropertyValueFactory("postedDate"));	  
	    tableViewUrlCol.setCellValueFactory(new PropertyValueFactory<Item, String>("url"));
	    // Assign the "Open Browser when clicked" function to URL cells
	    Callback<TableColumn<Item, String>, TableCell<Item, String>> urlCellFactory = new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {
		    @Override
		    public TableCell call(TableColumn p) {
			    urlCell c = new urlCell();
			    c.addEventHandler(MouseEvent.MOUSE_CLICKED, new urlCellHandler());
			    return c;
		    }
	    };
	    tableViewUrlCol.setCellFactory(urlCellFactory);
	    
	    // Refresh the table
	    tableViewTable.refresh();

    }
	
    
    /*
     * @author Tony
     * 
     */    
	/**
	 * get the number of data fetched
	 * @author tony
	 * @param result - which is the List<Item> result from scrape function
	 * @return the number of data fetched
	 */
    int getNumOfData(List<Item> result) { //get the number of data fetched
    	int num_of_data = 0;
    	for(Item item : result)
    		num_of_data++;
    	return num_of_data;
    }
	/**
	 * get the average price
	 * @author Tony
	 * @param result - which is the List<Item> result from scrape function
	 * @return the average price of the items
	 */
    double getAvgPrice(List<Item> result) { //get the average price
    	double sum = 0;
    	for(Item item: result){
    		sum += item.getPrice();
    	}
    	return sum/getNumOfData(result);
    }
	/**
	 * get the item that is the minimum price
	 * @author tony
	 * @param result - which is the List<Item> result from scrape function
	 * @return the item that is the minimum price
	 */
    Item getMinItem(List<Item> result) { //get the item that is the minimum price
    	Item min = result.get(0);
    	for(Item item : result) {
    		if(item.getPrice() < min.getPrice())
    			min = item;
    	}
    	return min;
    }
	/**
	 * get the latest item
	 * @author tony
	 * @param result - which is the List<Item> result from scrape function
	 * @return a list of items
	 */
    Item getLatest(List<Item> result) { //get the latest item
    	Item latest = result.get(0);
    	for(Item item : result) {
    		if((item.getPostedDate().compareTo(latest.getPostedDate())>0))
    			latest = item;
    	}
    	return latest;
    }
	/**
	 * this is task 1 implementation, includes finding the Number of Data Fetched, Average Selling Price, Lowest Selling Price and Latest Post
	 * @author tony
	 * @param result - which is the List<Item> result from scrape function
	 * @param labelCount - is the Number of Data Fetched
	 * @param labePrice - the Average Selling Price
	 * @param labelMin - the Lowest Selling Price
	 * @param labelLatest - the Latest Post
	 * @return void
	 */
    void task1(Label labelCount,Label labelPrice,Hyperlink labelMin,Hyperlink labelLatest, List<Item> result) {
    	//task 1 implementation
    	if(result.size() == 0) {
		//Put "-" to Average selling price, lowest selling price and latest post for result not found.
    		labelCount.setText("\t0");
    		labelPrice.setText(" -");
    		labelMin.setText("-");
    		labelMin.setDisable(true);
    		labelLatest.setText("-");
    		labelLatest.setDisable(true);
    	}
    	else {
    		labelLatest.setDisable(false);
    		labelLatest.setDisable(false);
//    		System.out.println("inside task1 else"); //debug
	    	labelCount.setText(String.valueOf(getNumOfData(result)));
	    	labelPrice.setText("HKD " + String.format("%.2f",getAvgPrice(result)));
	    	
	    	Item min = getMinItem(result);
	    	labelMin.setText("HKD " + String.format("%.2f", min.getPrice()));
	    	Item latest = getLatest(result);
	    	labelLatest.setText(latest.getTitle());
	    	labelMin.setOnAction((actionEvent) -> {
	    		callBrowser(min.getUrl());
	    	});
	    	labelLatest.setOnAction((actionEvent) -> {
	    		callBrowser(latest.getUrl());
	    	});
    	}
    }
	/**
	 * exclude the zero price item
	 * @author tony
	 * @param result - which is the List<Item> result from scrape function
	 * @return Vector<Item> ,return a new list without zero price item
	 */  
    Vector<Item> toNoZeroPrice(List<Item> result){ //exclude the zero price item
    	if(result == null)
    		return null;
		Vector<Item> newResult = new Vector<Item>();
    	for(Item item : result) {
    		if(item.getPrice()>0)
    			newResult.add(item);
    	}
    	return newResult;
    }
//	/**
//	 * sort the list
//	 * @author tony
//	 * @param result - which is the List<Item> result from scrape function
//	 * 
//	 */
//    void sort(List<Item> result) { //sort the list
//    	
//    }
    //end of tony
    
    
    // Functions for testing
    /**
     * function for testing actionSearch
     * @author Linus
     * @param keyword - which is the keyword to be tested
     * @return list of result scraped
     */
    public List<Item> runSearch(String keyword) {
    	this.textFieldKeyword.setText(keyword);
    	this.actionSearch();
    	return this.result;
    }
    /**
     * function for testing setLastSearchEnable() and setLastSearchDisable()
     * @author Linus
     * @return is last search enabled
     */
    public boolean getLastSearchEnabled() {
    	return !LastSearchFXId.isDisable();
    }
    /**
     * function for testing calling of browser, can only test logic but not UI component
     * @param url
     * @return true if the application doesn't crash
     */
    public boolean runCallBrowser(String url) {
    	this.callBrowser(url);
    	return true;
    }
    
    /**
     *  For testing actionClose()
     */ 
    public void runActionClose() {
    	actionClose();
    }
    
    public Node getAnotherRoot() {
    	return anotherRoot;
    }
    public Button getRefineButton() {
    	return refineButton;
    }
    public MenuItem getLastSearchButton() {
    	return LastSearchFXId;
    }
    public ArrayList<String> getSearchRecord() {
    	return searchRecord;
    }
    public String getCurrentKeyword() {
    	return currentKeyword;
    }
    public Stage getAnotherStage() {
    	return anotherStage;
    }
    public TableView getTableViewTable() {
    	return tableViewTable;
    }
    public TextArea getTextAreaConsole() {
    	return textAreaConsole;
    }
    public TextField getTextFieldKeyword() {
    	return textFieldKeyword;
    }
}
