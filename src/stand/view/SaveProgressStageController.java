package stand.view;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class SaveProgressStageController {
	
	Stage saveProgressStage;
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private ProgressBar pBar;
	

	public void setStage(Stage saveProgressStage) {
		this.saveProgressStage = saveProgressStage;
	}
}
