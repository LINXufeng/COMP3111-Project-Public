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
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

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
    
    private WebScraper scraper;
    
    
    
    /**
     * @author felixhui
    */
    @FXML
    private MenuItem LastSearchFXId;
    @FXML
    private Button refineButton;
    @FXML 
    private Node anotherRoot;	// this root was used to show team information
    private Stage anotherStage = new Stage();	// this stage is used to show team information
    private static final String abountOurTeam_FILE = "/aboutOurTeam.fxml";		//this path is the GUI of team information
    private String currentKeyword = null;	// this variable track current search record
    
    private ArrayList<String> searchRecord = new ArrayList<String>();	// record search history
    public void CheckSearchRecord() {
    	// check search record
    	for(String record:searchRecord) {
    		System.out.print(record +" ");
    	}
    }
    /*
     * end of implementation by felix
     */
    
    
    
   
    
    
    
    
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
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
    	textFieldKeyword.setText("");
    	textAreaConsole.setText("");
    	// close about our team window
    	anotherStage.close();
    	
    }
    
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	
    	
    	
    	
    	/*
    	 * @author Felix
    	 */
    	// add keyword to "searchRecord" list
    	currentKeyword = textFieldKeyword.getText();
    	searchRecord.add(textFieldKeyword.getText());
    	CheckSearchRecord();
    	// set 'last search' button enable if there exists some search record
    	if(searchRecord.size()>=2 && LastSearchFXId.isDisable()==true) {
    		setLastSearchEnable();
    	}
    	else{
    		setLastSearchDisable();
    	}
    	// Make the button Refine enabled after a new search ( e.g. Go button is clicked).
    	setRefineEnable();
    	
    	/*
    	 * @author Felix
    	 */
    }
    
    
    
    
    
    
    
    
    
    
	/**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     * @author felixhui
     *
     *New (call Controller.actionNew()) should be renamed to Last Search and revert your search 
     *result to the previous search.[5]
	 *Before any keyword is being searched, or after Last Search is clicked once, 
	 *it should be disabled (gray and unable to be clicked).[2]
	 *Last Search will be enabled after a new search ((Go) button is clicked).[2]
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
    	List<Item> result = scraper.scrape(lastSearchKeyword);
    	String output = "";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	
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
     * Make About your Team showing a new simple dialog that shows all your team members name, 
     * itsc account, and github account.[done!]
     * @author felixhui
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
     * Make Quit button exiting the program and close all connections.[3]
     * @author felixhui
     */
    @FXML
    private void actionQuit() {
    	System.out.println("actionQuit");
    	//scraper.getClass().stop();
    	// simply close it is not correct, you should close all connection first
    	scraper = null;
        System.exit(0);
    }
    
    
    
    
    
    
    /**
     * Close will clear the current search record and initialize all 
     * tabs on the right to their initial state.[5]
     * @author felixhui
     */
    @FXML
    private void actionClose() {
    	System.out.println("actionClose");
    	initialize();
    }
    
    
    
    /**
     * Create an extra button that shows Refine just below the Go button with at least 5px of space.[done]
     * Make this button right align the Go button.[done!]
     * when the refine button is clicked, filter the searched data and keep those items with their titles 
     * containing the keywords typed in the text area.[done!]
     * 
	 * update all tabs on the right. (Note: the correctness of the info of the tabs may depend on other features. 
	 * This requirement needs only to trigger the update process.) [4]
	 * 
	 * Make the button Refine disabled (grey) before any keyword is being searched, or after Refine is clicked once.[done!]
	 * Make the button Refine enabled after a new search ( e.g. Go button is clicked).[done!]
     * @author felixhui
     */
    @FXML
    private void actionRefine() {
    	System.out.println("actionRefine");
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	for (Item item : result) {
    		if(item.getTitle().toLowerCase().contains(textFieldKeyword.getText().toLowerCase())) {
    			output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    		}
    	}
    	textAreaConsole.setText(output);
    	setRefineDisable();
    	// trigger the update process.) [4]
    	// update();
    }
    
    /**
     * This is a helper method implemented by felix to disable/enable Refine button
     * @author felixhui
     */
    private void setRefineDisable() {
    	refineButton.setDisable(true);
    }
    private void setRefineEnable() {
    	refineButton.setDisable(false);
    }
    /**
     * This is a helper method implemented by felix to disable/enable last search button
     * @author felixhui
     */
    private void setLastSearchDisable() {
    	LastSearchFXId.setDisable(true);
    }
    private void setLastSearchEnable() {
    	LastSearchFXId.setDisable(false);
    }
    
    
    
    
    
    
    
    
}

