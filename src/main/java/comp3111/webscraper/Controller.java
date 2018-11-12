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

import java.awt.Desktop;
import java.net.URI;


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
    
    @FXML
    private Button searchBtn;
    
    @FXML
    private TableView tableViewTable;
    
    @FXML
    private TableColumn<Item, String> tableViewTitleCol;
    @FXML
    private TableColumn<Item, String> tableViewPriceCol;
    @FXML
    private TableColumn<Item, String> tableViewUrlCol;
    @FXML
    private TableColumn<Item, String> tableViewPostedDateCol;
    
    private ObservableList<Item> itemList;
    
    
    private WebScraper scraper;
    
    private List<Item> result;
    
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
		result = scraper.getEmptyList();
		try {
			searchTask search = new searchTask(keyword);
			textAreaConsole.textProperty().bind(search.messageProperty());
			search.setOnRunning((succeesesEvent) -> {
				System.out.println("Searching for " + keyword);
				searchBtn.setDisable(true);
			});
			search.setOnSucceeded((succeededEvent) -> {
				tableViewTable.setItems(FXCollections.observableList(result));
				searchBtn.setDisable(false);
			});
			
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(search);
			executor.shutdown();
		} catch (Exception e) {
			textAreaConsole.setText(e.toString());
		}
		

		/*
		String output = "";
		for (Item item : result) {
			output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
		}
		textAreaConsole.appendText(output);
		
	    labelCount.setText("Hi");
	    */
	    tableViewTitleCol.setCellValueFactory(new PropertyValueFactory("title"));
	    tableViewPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
	  
	    tableViewUrlCol.setCellValueFactory(new PropertyValueFactory<Item, String>("url"));      
	    Callback<TableColumn<Item, String>, TableCell<Item, String>> urlCellFactory = new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {
		    @Override
		    public TableCell call(TableColumn p) {
			    urlCell c = new urlCell();
			    c.addEventHandler(MouseEvent.MOUSE_CLICKED, new urlCellHandler());
			    return c;
		    }
	    };
	    tableViewUrlCol.setCellFactory(urlCellFactory);
	    tableViewPostedDateCol.setCellValueFactory(new PropertyValueFactory("postedDate"));
	  
	    tableViewTable.refresh();
	    
	  }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
    
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
    		do {
    			result.addAll(scraper.scrape(keyword));
    			String output = textAreaConsole.getText() + "Finished scraping page " + Integer.toString(currentPage) + "/" + Integer.toString(totalPage) + "...\n";
    			updateMessage(output);
    			currentPage += 1;
    		} while (scraper.nextPage());
    		updateMessage(textAreaConsole.getText() + "Finished scraping.\n");
    		return "";
    	}
    }
    
    class urlCell extends TableCell<Item, String> {
    	@Override
    	public void updateItem(String item, boolean empty) {
    		super.updateItem(item, empty);
    		setText(empty ? null : getString());
    		setGraphic(null);
    	}
    	
    	private String getString() {
    		return getItem() == null ? "" : getItem().toString();
    	}
    }
    
    class urlCellHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent t) {
    		if (Desktop.isDesktopSupported()) {
    			try {
		    		TableCell c = (TableCell) t.getSource();
		    		Desktop.getDesktop().browse(new URI(c.getItem().toString()));
    			} catch (Exception e) {
    				System.out.println("Failed to open URL:");
    				System.out.println(e);
    			}
    		}
    	}
    }
    
}