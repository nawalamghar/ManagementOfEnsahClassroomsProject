package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import application.Demande;
import application.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SuivreDemandeController {
	int userId = UserSession.getUserId();
	private Stage stage;
	private Scene scene ;
	private Parent root;

    private static SuivreDemandeController instance;

	 @FXML
	    private TableView<Demande> demandeTableView;

	    private ObservableList<Demande> demandes = FXCollections.observableArrayList();

	    @FXML
	    private void initialize() {
	        instance = this;

	        if (demandeTableView == null) {
	            System.out.println("demandeTableView is null");
	        }

	        demandes.addAll(fetchDataFromDatabase());

	        if (demandeTableView != null) {
	            demandeTableView.setItems(demandes);
	            // Add a selection listener to detect selection changes
	            demandeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	                if (newSelection != null) {
	                    showDetailsWindow(newSelection);
	                }
	            });
	        }
	    }

	    private List<Demande> fetchDataFromDatabase() {
	        List<Demande> demandes = new ArrayList<>();

	        String url = "jdbc:mysql://localhost:3306/ensahjava";
	        String user = "root";
	        String password = "";

	        try (Connection connection = DriverManager.getConnection(url, user, password)) {
	            String query = "SELECT ds.id_demande, p.nom_personne, c.nom_cours, ds.type_salle, ds.moment_demande," +
	                    " ds.jour_seance, ds.heure_debut_seance, ds.heure_fin_seance, ds.commentaire, ds.etat_demande" +
	                    " FROM demande_salle ds" +
	                    " INNER JOIN personne p ON ds.id_personne = p.id_personne" +
	                    " INNER JOIN cours c ON ds.id_cours = c.id_cours WHERE p.id_personne=?";

	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            	  preparedStatement.setInt(1, userId);
	                ResultSet resultSet = preparedStatement.executeQuery();
	                while (resultSet.next()) {
	                    int idDemande = resultSet.getInt("id_demande");
	                    String cours = resultSet.getString("nom_cours");
	                    String nomProf = resultSet.getString("nom_personne");
	                    LocalDateTime momentDemande = resultSet.getTimestamp("moment_demande").toLocalDateTime();
	                    String etatDemande = resultSet.getString("etat_demande");
	                    String jourSeance = resultSet.getString("jour_seance");
	                    String heureDebutSeance = resultSet.getString("heure_debut_seance");
	                    String heureFinSeance = resultSet.getString("heure_fin_seance");
	                    String commentaire = resultSet.getString("commentaire");
	                    String typeSalle = resultSet.getString("type_salle");

	                    Demande demande = new Demande(idDemande, nomProf, cours, typeSalle, momentDemande, jourSeance, heureDebutSeance, heureFinSeance, etatDemande, commentaire);
	                    demandes.add(demande);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return demandes;
	    }
	    

	    @FXML
	    private void showDetails() {
	        Demande selectedDemande = demandeTableView.getSelectionModel().getSelectedItem();

	        if (selectedDemande != null) {
	            showDetailsWindow(selectedDemande);
	        } else {
	            showAlert("Aucune Demande Sélectionnée", null, "Veuillez sélectionner une demande avant de voir les détails.");
	        }
	    }

	    private void showDetailsWindow(Demande demande) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/DetailsprofWindow.fxml"));
	            Parent root = loader.load();
	            Stage detailsStage = new Stage();
	            detailsStage.setTitle("Details Window");
	            detailsStage.initModality(Modality.APPLICATION_MODAL);

	            // Get the controller
	            DetailsprofController detailsprofController = loader.getController();

	            // Set details in the controller
	            detailsprofController.setDemandeDetails(
	                    String.valueOf(demande.getIdDemande()),
	                    demande.getMomentDemande().toString(),
	                    demande.getCours(),
	                    demande.getEtatDemande(),
	                    demande.getJourDemande(),
	                    demande.getHeureDebutDemande(),
	                    demande.getHeureFinDemande(),
	                    demande.getTypeSalle(),
	                    demande.getCommentaire() // Pass the value for the new label
	            );

	            // Set the reference to GestionDemandesController
	            detailsprofController.setSuivreDemandeController(this);

	            Scene detailsScene = new Scene(root);
	            detailsStage.setScene(detailsScene);

	            detailsStage.showAndWait();
	        } catch (IOException e) {
	            e.printStackTrace();
	            showAlert("Error", "Error loading details window", "An error occurred while loading the details window.");
	        }
	    }
 
	    
	 // Method to delete a request based on its ID
	    public void deleteRequest(int requestId) {
	        String url = "jdbc:mysql://localhost:3306/ensahjava";
	        String user = "root";
	        String password = "";

	        try (Connection connection = DriverManager.getConnection(url, user, password)) {
	            String query = "DELETE FROM demande_salle WHERE id_demande = ?";

	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setInt(1, requestId);
	                preparedStatement.executeUpdate();

	                // Refresh the TableView after deletion
	                demandes.clear();
	                demandes.addAll(fetchDataFromDatabase());
	                demandeTableView.setItems(demandes);
		            showAlert("Succes", "Cancel succeded ", "Your request has been canceled ");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Error", "Cancel failed", "An error occurred while cancelling the request");

	        }
	    }
  
	    
	    private void showAlert(String title, String headerText, String contentText) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle(title);
	        alert.setHeaderText(headerText);
	        alert.setContentText(contentText);
	        alert.showAndWait();
	    } 
	    
	    
	 
	
	@FXML
	public void btnAcceuil(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/AcceuilProf.fxml"));
	        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();    
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    }
	@FXML
	public void btnDemande(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/DemandeReservation.fxml"));
	        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    }
	  @FXML
		public void MonEmploibtn(ActionEvent event) {
	    	try {
	            root = FXMLLoader.load(getClass().getResource("/interfaces/CalendrierProf.fxml"));
	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		    }
	public void btnSuivreDemande(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/SuivreDemande.fxml"));
	        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    }
	 @FXML
	    public void deconnexionbtn(ActionEvent event) {
			try {
				Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
	            successDialog.setTitle("Success");
	            successDialog.setHeaderText("Success");
	            successDialog.setContentText("vous avez deconnecte avec success!");
	            successDialog.showAndWait();
				root = FXMLLoader.load(getClass().getResource("/interfaces/Main.fxml"));
	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	            
				
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		    }
}
