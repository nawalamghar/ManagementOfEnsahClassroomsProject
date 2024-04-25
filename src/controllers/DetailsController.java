package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import application.Demande;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailsController {
	  private Stage stage;
	
	@FXML
    private Label idLabel;

    @FXML
    private Label momentLabel;

    @FXML
    private Label profLabel;

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
    
    private GestionDemandesController gestionDemandesController;

    public void setGestionDemandesController(GestionDemandesController controller) {
        gestionDemandesController = controller;
    }

    public void setDemandeDetails(String id, String moment, String prof, String cours, String etat, String jour, String heureDebut, String heureFin, String typeSalle, String commentaire) {
        idLabel.setText(id);
        momentLabel.setText(moment);
        profLabel.setText(prof);
        coursLabel.setText(cours);
        etatLabel.setText(etat);
        jourLabel.setText(jour);
        heuredebutLabel.setText(heureDebut);
        heurefinLabel.setText(heureFin);
        typesalleLabel.setText(typeSalle);
        commentaireLabel.setText(commentaire); 
    }
    
    public void btntestDisponible(ActionEvent event) {
        Demande selectedDemande = gestionDemandesController.getSelectedDemande();

        if (selectedDemande != null) {
            boolean isAvailable = performAvailabilityCheck(selectedDemande);

            if (isAvailable) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/AcceptDemande.fxml"));
                    Parent root = loader.load();

                    // Get the controller
                    AcceptDemandeController acceptDemandeController = loader.getController();

                    // Set details in the controller
                    acceptDemandeController.setDemandeDetails(selectedDemande);

                    // Switch to the new scene
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    stage.setTitle("Demande Approuvée ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showConfirmationDialog();
            }
        } else {
            showAlert("Aucune Demande Sélectionnée", null, "Veuillez sélectionner une demande avant de tester la disponibilité.");
        }
    }


    private boolean performAvailabilityCheck(Demande demande) {
        // Get relevant information from the selected demande
        String selectedTypeSalle = demande.getTypeSalle();
        LocalTime selectedHeureDebut = LocalTime.parse(demande.getHeureDebutDemande());
        LocalTime selectedHeureFin = LocalTime.parse(demande.getHeureFinDemande());
        
        String dateString = demande.getJourDemande();
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String jour= dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL,Locale.FRENCH);
        String selectedJourSeance = jour.substring(0, 1).toUpperCase() + jour.substring(1);;

        String sqlQuery ="SELECT * FROM salle " +
                "WHERE type_salle = ? " +
                "AND id_salle NOT IN (" +
                "    SELECT id_salle " +
                "    FROM affectation " +
                "    WHERE jour = ? " +
                "    AND (? BETWEEN heure_debut AND heure_fin OR ? BETWEEN heure_debut AND heure_fin) " +
                "    OR (heure_debut BETWEEN ? AND ? OR heure_fin BETWEEN ? AND ?)" +
                ")";
               
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

        	 preparedStatement.setString(1, selectedTypeSalle);
             preparedStatement.setString(2, selectedJourSeance);
             preparedStatement.setTime(3, Time.valueOf(selectedHeureDebut));
             preparedStatement.setTime(4, Time.valueOf(selectedHeureFin));
             preparedStatement.setTime(5, Time.valueOf(selectedHeureDebut));
             preparedStatement.setTime(6, Time.valueOf(selectedHeureFin));
             preparedStatement.setTime(7, Time.valueOf(selectedHeureDebut));
             preparedStatement.setTime(8, Time.valueOf(selectedHeureFin));

             System.out.println("Generated SQL Query: " + preparedStatement.toString()); // Print the generated SQL query for debugging

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Si next() retourne true, cela signifie qu'il y a une salle disponible.

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Availability Check Error", "An error occurred while checking availability. See logs for details.");
            return false;
        }
    }


    private void showConfirmationDialog() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Availability Check Failed");
        confirmation.setHeaderText(null);
        confirmation.setContentText("There are no available salles for the selected date, time, and type. What do you want to do?");

        ButtonType cancelBtn = new ButtonType("Cancel");
        ButtonType refuseBtn = new ButtonType("Refuse");

        confirmation.getButtonTypes().setAll(cancelBtn, refuseBtn);
        Demande selectedDemande = gestionDemandesController.getSelectedDemande();

        confirmation.showAndWait().ifPresent(response -> {
            if (response == cancelBtn) {
                System.out.println("Cancel clicked");
                closeCurrentScene();

            } else if (response == refuseBtn) {
                updateEtatDemande(selectedDemande, "Refusé");
                System.out.println("Refuse clicked");
                closeCurrentScene();


            }
        });
    }

    
    private void updateEtatDemande(Demande demande, String newEtat) {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE demande_salle SET etat_demande = ? WHERE id_demande = ?")) {

            preparedStatement.setString(1, newEtat);
            preparedStatement.setInt(2, demande.getIdDemande());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Etat_demande updated successfully");
            } else {
                System.out.println("Failed to update etat_demande");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Update Error", "An error occurred while updating etat_demande. See logs for details.");
        }
    }
    
    private void closeCurrentScene() {
        // Get the current scene's root node
        Parent sourceNode = commentaireLabel.getScene().getRoot();
        Stage stage = (Stage) sourceNode.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
