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
	public void testRunSearchNoResult() {
		assertEquals(new Vector<Item>(), c.runSearch("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName"));
	}
	
	
	
	
	@Test
	public void testActionClose() {
		c.runActionClose();
		System.out.println("testActionClose()");
    	// close team information GUI
		assertEquals(null,c.getAnotherRoot());
    	// set refine button and last search button disable
		assertEquals(true,c.getRefineButton().isDisable());
    	assertEquals(true,c.getLastSearchButton().isDisable());
    	// clear searchRecord
    	assertEquals(new ArrayList<String>(),c.getSearchRecord());  	
    	// clear current keyword
    	assertEquals(null,c.getCurrentKeyword());
    	// clear input text and result console to null
    	assertEquals("",c.getTextFieldKeyword().getText());
    	assertEquals("",c.getTextAreaConsole().getText());
    	assertEquals(null,c.getTableViewTable().getItems());
    	// close about our team window
    	assertEquals(false,c.getAnotherStage().isShowing());
	}
	
	
	
	
	@Test
	public void testActionAOT() {
		System.out.println("testActionAOT()");
		//assertEquals("About Our Team",c.getAnotherStage().getTitle());
	    //anotherStage.setScene(new Scene(anotherRoot, 600, 329));
		//assertEquals(new Scene((Parent) c.getAnotherRoot(), 600, 329),c.getAnotherStage().getScene());	
	    //anotherStage.show();
		//assertEquals(true,c.getAnotherStage().isShowing());
    }
	
	
}