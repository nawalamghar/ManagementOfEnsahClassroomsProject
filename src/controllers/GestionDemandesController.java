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
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import application.Demande;
//import application.UserSession;

public class GestionDemandesController {
    private Stage stage;
    private Scene scene;
    private Parent root;

   // int userId = UserSession.getUserId();

    @FXML
    private TableView<Demande> demandeTableView;

    private ObservableList<Demande> demandes = FXCollections.observableArrayList();

    private static GestionDemandesController instance;

    @FXML
    private void initialize() {
    	updateEnAttenteLabel() ;

        instance = this;

        demandes.addAll(fetchDataFromDatabase());
        demandeTableView.setItems(demandes);

        // Add a selection listener to detect selection changes
        demandeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDetailsWindow(newSelection);
            }
        });
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
                    " INNER JOIN cours c ON ds.id_cours = c.id_cours";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int idDemande = resultSet.getInt("id_demande");
                    String nomProf = resultSet.getString("nom_personne");
                    String cours = resultSet.getString("nom_cours");
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
            // Handle the exception appropriately
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/DetailsWindow.fxml"));
            Parent root = loader.load();
            Stage detailsStage = new Stage();
            detailsStage.setTitle("Details Window");
            detailsStage.initModality(Modality.APPLICATION_MODAL);

            // Get the controller
            DetailsController detailsController = loader.getController();

            // Set details in the controller
            detailsController.setDemandeDetails(
                    String.valueOf(demande.getIdDemande()),
                    demande.getMomentDemande().toString(),
                    demande.getNomProf(),
                    demande.getCours(),
                    demande.getEtatDemande(),
                    demande.getJourDemande(),
                    demande.getHeureDebutDemande(),
                    demande.getHeureFinDemande(),
                    demande.getTypeSalle(),
                    demande.getCommentaire() // Pass the value for the new label
            );

            // Set the reference to GestionDemandesController
            detailsController.setGestionDemandesController(this);

            Scene detailsScene = new Scene(root);
            detailsStage.setScene(detailsScene);

            detailsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading details window", "An error occurred while loading the details window.");
        }
    }


    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static GestionDemandesController getInstance() {
        return instance;
    }
    
    public Demande getSelectedDemande() {
        return instance.demandeTableView.getSelectionModel().getSelectedItem();
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
    
    public void btnGestionSalles(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/GestionSalles.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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
            stage.show();
            } catch (IOException e) {
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
