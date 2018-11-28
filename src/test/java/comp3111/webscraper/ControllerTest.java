package comp3111.webscraper;


import org.junit.Test;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static org.junit.Assert.*;
import org.testfx.framework.junit.ApplicationTest;

public class ControllerTest extends ApplicationTest {
	Controller c;
	
	@Override
	public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
    	// loader.setController(c);
    	loader.setLocation(getClass().getResource("/ui.fxml"));
   		VBox root = (VBox) loader.load();
   		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("WebScrapper");
		c = loader.getController();
	}
	
	@Test
	public void testActionSearchNoResult() {
		assertEquals(new Vector<Item>(), c.runSearch("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName"));
	}
	@Test
	public void testActionSearchHasResult() {
		assertTrue(new Vector<Item>() != c.runSearch("note 9"));
	}
	@Test
	public void testGetLastSearchEnabled() {
		assertTrue(!c.getLastSearchEnabled());
	}
	@Test
	public void testActionSearchCallLastSearchEnable() {
		c.runSearch("note9");
		c.runSearch("iphoneXS");
		assertTrue(c.getLastSearchEnabled());
	}
	
	@Test
	public void testCallBrowser() {
		assertTrue(c.runCallBrowser("https://giving.ust.hk"));
	}
	
	@Test
	public void testActionClose() {
		c.runActionClose();
		System.out.println("testActionClose()");
    	// close team information GUI
		assertEquals(c.getAnotherRoot(), null);
    	// set refine button and last search button disable
		assertEquals(c.getRefineButton().isDisable(),true);
    	assertEquals(c.getLastSearchButton().isDisable(),true);
    	// clear searchRecord
    	assertEquals(c.getSearchRecord(),new ArrayList<String>());  	
    	// clear current keyword
    	assertEquals(c.getCurrentKeyword(),null);
    	// clear input text and result console to null
    	//assertEquals(c.getTextFieldKeyword(),"");
    	assertEquals(c.getTextAreaConsole(),"");
    	assertEquals(c.getTableViewTable(), null);
    	// close about our team window
    	assertEquals(c.getAnotherStage().isShowing(),false);
	}
}