package controllers;

	
	import javafx.event.ActionEvent;
import javafx.fxml.FXML;
	import javafx.scene.control.Label;
	import javafx.stage.Stage;
	public class DetailsprofController {
		  private Stage stage;
		
		@FXML
	    private Label idLabel;

	    @FXML
	    private Label momentLabel;

	    @FXML
	    private Label coursLabel;

	    @FXML
	    private Label etatLabel;

	    @FXML
	    private Label jourLabel;

	    @FXML
	    private Label heuredebutLabel;

	    @FXML
	    private Label heurefinLabel;

	    @FXML 
	    private Label typesalleLabel;

	    @FXML
	    private Label commentaireLabel;
	    
	    private SuivreDemandeController suivreDemandeController;

	    public void setSuivreDemandeController(SuivreDemandeController controller) {
	        suivreDemandeController = controller;
	    }

	    public void setDemandeDetails(String id, String moment, String cours, String etat, String jour, String heureDebut, String heureFin, String typeSalle, String commentaire) {
	        idLabel.setText(id);
	        momentLabel.setText(moment);
	        coursLabel.setText(cours);
	        etatLabel.setText(etat);
	        jourLabel.setText(jour);
	        heuredebutLabel.setText(heureDebut);
	        heurefinLabel.setText(heureFin);
	        typesalleLabel.setText(typeSalle);
	        commentaireLabel.setText(commentaire); 
	    }
	    
	    
	    @FXML
		  public void btnAnnulerDemande(ActionEvent event) {
	    	String id = idLabel.getText();
	        if (!id.isEmpty()) {
	            int requestId = Integer.parseInt(id);
	            suivreDemandeController.deleteRequest(requestId);

	            // Close the details window
	            Stage stage = (Stage) idLabel.getScene().getWindow();
	            stage.close();
	        }
	    	
		  }
		    
	    
	    
	}


