/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;

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
    
    // List of Item that will be visualized by the tableViewTable
    // This will observe the following "result" List
    private ObservableList<Item> itemList;
    
    // List of Item that store the search result
    private List<Item> result;
    /*
     * End of variables for Task 4
     */
    
    private WebScraper scraper;
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    	result = scraper.getEmptyList();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {

    }
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
		String keyword = textFieldKeyword.getText();
		System.out.println("actionSearch: " + keyword);
		// Initialize or clean the result list
		result = scraper.getEmptyList();
		// Attempt to search
		try {
			// Create a task for background searching operation
			searchTask search = new searchTask(keyword);
			// Bind the console to the search task
			textAreaConsole.textProperty().bind(search.messageProperty());
			// Disable the "Go" button when searching
			search.setOnRunning((succeesesEvent) -> {
				System.out.println("Searching for " + keyword);
				searchBtn.setDisable(true);
			});
			// Enable the "Go" button after searching
			search.setOnSucceeded((succeededEvent) -> {
				tableViewTable.setItems(FXCollections.observableList(result));
				searchBtn.setDisable(false);
				textAreaConsole.textProperty().unbind();
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
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
    
    /* class searchTask
     * @author Linus
     * Define how to search with a keyword, used for Task 3
     */
    class searchTask extends Task<String> {
    	private final String keyword;
    	
    	public searchTask(String keyword) {
    		this.keyword = keyword;
    	}

    	@Override
    	protected String call() throws Exception {
    		System.out.println("Searching");
    		int currentPage = 1;
    		int totalPage = scraper.fetchResultCount(keyword);
    		// Loop through pages until there is no pages left (scraper.nextPage() == false)
    		do {
    			// Add Items scraped by scraper to the list
    			result.addAll(scraper.scrape(keyword));
    			String output = textAreaConsole.getText() + "Finished scraping page " + Integer.toString(currentPage) + "/" + Integer.toString(totalPage) + "...\n";
    			// Return the message to textAreaConsole and update it
    			updateMessage(output);
    			currentPage += 1;
    		} while (scraper.nextPage());
    		updateMessage(textAreaConsole.getText() + "Finished scraping.\n");
    		return "";
    	}
    }
    
    /* class urlCell
     * @author Linus
     * Defines the behavior of cells in the URL column
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
    
    /* class urlCellHandler
     * @author Linus
     * Defines the event handler of URL cell for clicking
     */
    class urlCellHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent t) {
			// Try to open URL in browser
			try {
	    		TableCell c = (TableCell) t.getSource();
	    		WebView web = new WebView();
	    		web.getEngine().load(c.getItem().toString());
	    		Scene scene = new Scene(web);
	    		Stage browser = new Stage();
	    		browser.setScene(scene);
	    		browser.show();
	    		
			} catch (Exception e) {
				System.out.println("Failed to open URL:");
				System.out.println(e);
			}	
    	}
    }
    
}