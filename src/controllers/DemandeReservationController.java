package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DemandeReservationController {
    int userId = UserSession.getUserId();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> typesalleBox;

    @FXML
    private TextField heuredebutField;

    @FXML
    private TextField heurefinField;

    @FXML
    private ComboBox<String> coursBox;

    private ObservableList<String> originalItemscoursBox;

    @FXML
    private DatePicker jourseanceDate;

    @FXML
    private TextArea commentaireField;

    @FXML
    private void initialize() {
        typesalleBox.getItems().addAll("Cours", "TD", "Laboratoire", "TP-informatique", "Amphi");
        loadComboBoxDataCours();
    }

    private void loadComboBoxDataCours() {
        String url = "jdbc:mysql://localhost:3306/ensahjava";

        try {
            Connection connection = DriverManager.getConnection(url, "root", "");

            Statement statement = connection.createStatement();

            String sqlcours = "SELECT nom_cours FROM cours";
            ResultSet resultSetcours = statement.executeQuery(sqlcours);

            ObservableList<String> items = FXCollections.observableArrayList();
            while (resultSetcours.next()) {
                items.add(resultSetcours.getString("nom_cours"));
            }

            coursBox.setItems(items);
            originalItemscoursBox = FXCollections.observableArrayList(items);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlecoursBoxKeyReleased(KeyEvent event) {
        String userInput = coursBox.getEditor().getText().toLowerCase();

        if (!userInput.isEmpty()) {
            ObservableList<String> filteredItems = FXCollections.observableArrayList();
            for (String item : originalItemscoursBox) {
                if (item.toLowerCase().startsWith(userInput)) {
                    filteredItems.add(item);
                }
            }
            coursBox.setItems(filteredItems);
            coursBox.show();
        } else {
            coursBox.setItems(originalItemscoursBox);
            coursBox.show();
        }
    }

    @FXML
    public void btnDemanderSalle(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/ensahjava";
        String heureDebutText = heuredebutField.getText();
        String heureFinText = heurefinField.getText();
        String commentaire = commentaireField.getText();
        String typeSalle = typesalleBox.getValue();
        String nomCours = coursBox.getValue();
        LocalDate date = jourseanceDate.getValue();
        java.sql.Date jour = java.sql.Date.valueOf(date);

        try {
            LocalTime heureDebut = LocalTime.parse(heureDebutText);
            LocalTime heureFin = LocalTime.parse(heureFinText);

            Connection connection = DriverManager.getConnection(url, "root", "");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO demande_salle (id_personne ,id_cours ,type_salle, jour_seance ,heure_debut_seance ,heure_fin_seance, commentaire) " +
                            "VALUES (?, (SELECT id_cours FROM cours WHERE nom_cours = ?), ?,?,?,?,? )");

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, nomCours);
            preparedStatement.setString(3, typeSalle);
            preparedStatement.setDate(4, jour);
            preparedStatement.setTime(5, Time.valueOf(heureDebut));
            preparedStatement.setTime(6, Time.valueOf(heureFin));
            preparedStatement.setString(7, commentaire);

            int rowsAffected = preparedStatement.executeUpdate();

            Alert alert = new Alert(rowsAffected > 0 ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(rowsAffected > 0 ? "Success" : "Failure");
            alert.setHeaderText(rowsAffected > 0 ? "Success" : "Failure");
            alert.setContentText(rowsAffected > 0 ? "Affectation faite avec succès!" : "Échec de l'affectation!");
            alert.showAndWait();

            if (rowsAffected > 0) {
                // Clear input fields on success
                heuredebutField.clear();
                heurefinField.clear();
                commentaireField.clear();
                typesalleBox.getSelectionModel().clearSelection();
                coursBox.getSelectionModel().clearSelection();
                jourseanceDate.getEditor().clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    @FXML
    public void btnDemande(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/interfaces/DemandeReservation.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            System.out.println(userId);

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
