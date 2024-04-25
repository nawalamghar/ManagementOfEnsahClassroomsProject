package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Affectation;
import application.Demande;

//import application.UserSession;


public class GestionsAffectationsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    //int userId = UserSession.getUserId();
    @FXML
    private TableView<Affectation> affectationTableView;

    private ObservableList<Affectation> affectations = FXCollections.observableArrayList();

    private static GestionsAffectationsController instance;

    @FXML
    private void initialize() {
    	updateEnAttenteLabel() ;

        instance = this;

        affectations.addAll(fetchDataFromDatabase());
        affectationTableView.setItems(affectations);

    }

    private List<Affectation> fetchDataFromDatabase() {
        List<Affectation> affectations = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/ensahjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
        	String query = "SELECT a.id_affectation, p.nom_personne, c.nom_cours, s.nom_salle, s.type_salle,s.bloc, " +
        	        "a.jour, a.heure_debut, a.heure_fin " +
        	        "FROM affectation a " +
        	        "INNER JOIN personne p ON a.id_personne = p.id_personne " +
        	        "INNER JOIN cours c ON a.id_cours = c.id_cours " +
        	        "INNER JOIN salle s ON a.id_salle = s.id_salle ORDER BY a.id_affectation DESC";


            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                	int idaffectation=resultSet.getInt("id_affectation");
                    String nomProf = resultSet.getString("nom_personne");
                    String nomCours = resultSet.getString("nom_cours");
                    String jour = resultSet.getString("jour");
                    String duree = resultSet.getString("heure_debut")+"-"+resultSet.getString("heure_fin");
                    String sallebloctype = resultSet.getString("nom_salle")+"- bloc "+resultSet.getString("bloc")+"-"+resultSet.getString("type_salle");

                    Affectation affectation = new Affectation(idaffectation,nomProf, nomCours, jour, duree,sallebloctype );
                    affectations.add(affectation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        return affectations;
    }
    
    @FXML
    public void acceuilbtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/Acceuil.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
	        
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    
    public void btnSupprimer(ActionEvent event) {
    	Affectation selectedAffectation = affectationTableView.getSelectionModel().getSelectedItem();

    	if (selectedAffectation != null) {
    	    if (deleteAffectation(selectedAffectation)) {
    	        affectations.remove(selectedAffectation);
    	    } else {
    	        System.out.println("Erreur lors de la suppression de l'affectation de la base de données.");
    	    }
    	} else {
    	    System.out.println("Veuillez sélectionner une affectation à supprimer.");
    	}

}

    private boolean deleteAffectation(Affectation affectation) {
        String url = "jdbc:mysql://localhost:3306/ensahjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "DELETE FROM affectation WHERE id_affectation = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, affectation.getIdaffectation());
                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public void btnAffecter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/AffecterSalle.fxml"));
            Parent root = loader.load();
            Stage affectationStage = new Stage();
            affectationStage.setTitle("Affecter les séances");
            affectationStage.initModality(Modality.APPLICATION_MODAL);

            // Get the controller
            AffecterSalleController affecterSalleController = loader.getController();

            // Set the reference to GestionDemandesController
            affecterSalleController.setGestionsAffectationsController(this);

            Scene affectationScene = new Scene(root);
            affectationStage.setScene(affectationScene);

            affectationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    public void btnGestionSalles(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/GestionSalles.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading HomeAdminController.fxml: " + e.getMessage());
        }
    }

    public void btnGestionAffectations(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/GestionAffectations.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading GestionAffectations.fxml: " + e.getMessage());
        }
    }

    public void btnGestionDemandes(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/GestionDemandes.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading GestionDemandes.fxml: " + e.getMessage());
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
    /*notification section */
	@FXML
	private Label enAttenteLabel;

	 private void updateEnAttenteLabel() {
	        String url = "jdbc:mysql://localhost:3306/ensahjava";
	        try (Connection connection = DriverManager.getConnection(url, "root", "")) {
	            String query = "SELECT COUNT(*) FROM demande_salle WHERE etat_demande = 'En attente'";

	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                ResultSet resultSet = preparedStatement.executeQuery();

	                if (resultSet.next()) {
	                    int enAttenteCount = resultSet.getInt(1);
	                    if (enAttenteCount > 0) {
	                        enAttenteLabel.setText(String.valueOf(enAttenteCount));
	                        enAttenteLabel.setVisible(true);  // Show the label
	                    } else {
	                        enAttenteLabel.setVisible(false);  // Hide the label
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
    /*notification section*/
   
    
}
