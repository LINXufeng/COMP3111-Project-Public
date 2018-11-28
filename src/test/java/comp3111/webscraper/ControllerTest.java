package comp3111.webscraper;


import org.junit.Test;

import java.util.List;
import java.util.Vector;

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
	public void testRunSearchNoResult() {
		assertEquals(new Vector<Item>(), c.RunSearch("SomethingThatIsDefinitelyNotGoingToShowAnyResultWhenQueryingCraigslistAsNoOneIsGoingToGiveTheirProductThisName"));
	}
}