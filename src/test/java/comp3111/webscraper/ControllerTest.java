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
}