package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import application.Demande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AcceptDemandeController {

    @FXML
    private Label iddemandeLabel;

    @FXML
    private Label profLabel;

    @FXML
    private Label coursLabel;

    @FXML
    private Label typesalleLabel;

    @FXML
    private Label jourseanceLabel;

    @FXML
    private Label dureeseanceLabel;

    @FXML
    private ComboBox<String> blocBox;

    @FXML
    private ComboBox<String> nomsalleBox;

    private Demande selectedDemande;

    public void setDemandeDetails(Demande demande) {
        selectedDemande = demande;
        iddemandeLabel.setText(String.valueOf(demande.getIdDemande()));
        profLabel.setText(demande.getNomProf());
        coursLabel.setText(demande.getCours());
        jourseanceLabel.setText(demande.getJourDemande());
        dureeseanceLabel.setText(demande.getHeureDebutDemande() + "-" + demande.getHeureFinDemande());
        typesalleLabel.setText(demande.getTypeSalle());
    }

    @FXML
    private void initialize() {
        blocBox.getItems().addAll("A", "B");
        blocBox.setOnAction(event -> loadComboBoxDataSalle());
    }

    private void loadComboBoxDataSalle() {
        // Récupérer les valeurs des champs
        String day = selectedDemande.getJourDemande();
        LocalDate date = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String jour = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH);
        day = jour.substring(0, 1).toUpperCase() + jour.substring(1);

        String heureDebutText = selectedDemande.getHeureDebutDemande();
        String heureFinText = selectedDemande.getHeureFinDemande();
        String typeSalle = selectedDemande.getTypeSalle();
        String bloc = blocBox.getValue();

        if (day != null && !heureDebutText.isEmpty() && !heureFinText.isEmpty() && typeSalle != null) {
            String url = "jdbc:mysql://localhost:3306/ensahjava";

            try (Connection connection = DriverManager.getConnection(url, "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT s.id_salle, s.nom_salle " +
                                 "FROM salle s " +
                                 "WHERE s.type_salle = ? AND s.bloc = ? AND NOT EXISTS (" +
                                 "    SELECT 1 FROM affectation a " +
                                 "    WHERE a.id_salle = s.id_salle " +
                                 "        AND a.jour = ? " +
                                 "        AND a.heure_debut = ? " +
                                 "        AND a.heure_fin = ?)")) {

                preparedStatement.setString(1, typeSalle);
                preparedStatement.setString(2, bloc);
                preparedStatement.setString(3, day);
                preparedStatement.setString(4, heureDebutText);
                preparedStatement.setString(5, heureFinText);

                System.out.println("Generated SQL Query: " + preparedStatement.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> items = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    items.add(resultSet.getString("nom_salle"));
                }

                nomsalleBox.setItems(items);

            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        }
    }

    public void btnsubmitAccepted(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/ensahjava";

        String datestring = selectedDemande.getJourDemande();
        LocalDate dateinput = LocalDate.parse(datestring, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        java.sql.Date date = java.sql.Date.valueOf(dateinput);

        DayOfWeek dayOfWeek = dateinput.getDayOfWeek();
        String jour = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH);
        String day = jour.substring(0, 1).toUpperCase() + jour.substring(1);

        String heureDebutText = selectedDemande.getHeureDebutDemande();
        String heureFinText = selectedDemande.getHeureFinDemande();
        String typeSalle = selectedDemande.getTypeSalle();
        String bloc = blocBox.getValue();
        String nomSalle = nomsalleBox.getValue();
        String nomProf = selectedDemande.getNomProf();
        String nomCours = selectedDemande.getCours();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Time heureDebut = new Time(sdf.parse(heureDebutText).getTime());
            Time heureFin = new Time(sdf.parse(heureFinText).getTime());

            try (Connection connection = DriverManager.getConnection(url, "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "INSERT INTO affectation (heure_debut, heure_fin, jour, id_salle, id_cours, id_personne,date) " +
                                 "VALUES (?, ?, ?, (SELECT id_salle FROM salle WHERE nom_salle = ? AND bloc = ? AND type_salle = ?), " +
                                 "(SELECT id_cours FROM cours WHERE nom_cours = ?), " +
                                 "(SELECT id_personne FROM personne WHERE nom_personne = ? AND role = 'prof'),?)")) {

                preparedStatement.setTime(1, heureDebut);
                preparedStatement.setTime(2, heureFin);
                preparedStatement.setString(3, day);
                preparedStatement.setString(4, nomSalle);
                preparedStatement.setString(5, bloc);
                preparedStatement.setString(6, typeSalle);
                preparedStatement.setString(7, nomCours);
                preparedStatement.setString(8, nomProf);
                preparedStatement.setDate(9, date);

                int rowsAffected = preparedStatement.executeUpdate();
                updateEtatDemande(selectedDemande, "Approuvé");

                // Show Alert dialog based on the result
                showAlert("Success", "Success", (rowsAffected > 0) ? "Affectation faite avec succès!" : "Échec de l'affectation!");

            } catch (SQLException e) {
                handleDatabaseError(e);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            handleDatabaseError(e);
        }
    }

    public void btncancelAccepted(ActionEvent event) {
        Stage stage = (Stage) nomsalleBox.getScene().getWindow();
        stage.close();
   
    }

    private void handleDatabaseError(SQLException e) {
        e.printStackTrace();
        // Handle the exception (e.g., show an error message to the user)
        showAlert("Database Error", "Error", "An error occurred while accessing the database. Please try again later.");
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
