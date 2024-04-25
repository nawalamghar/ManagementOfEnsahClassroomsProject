package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjouterSalleController {
	private Stage stage;
    private Scene scene ;
    private Parent root;
    @FXML
    private Button gestionSalletn;
    @FXML
    private Button gestionAffectationbtn;
    @FXML
    private Button submitButton;
    
    @FXML
    private TextField nomSalle;

    @FXML
    private ComboBox<String> typeSalle;

    @FXML
    private TextField capaciteSalle;

    @FXML
    private TextArea descriptionSalle;

    @FXML
    private ComboBox<String> blocSalle;
    @FXML
    private Label checkAjout;
    private SharedModel sharedModel;
    @FXML
    private void initialize() {
    	sharedModel = new SharedModel();
        typeSalle.getItems().addAll("Cours", "TD", "Laboratoire", "TP-informatique","Amphi");
        blocSalle.getItems().addAll("A", "B" );

    }
    @FXML
    public void gestionSallebtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/GestionSalles.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
	        
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    @FXML
    public void gestionAffectationbtn(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/GestionAffectations.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
    @FXML
    public void btnSubmit(ActionEvent event) {
        try {
        	if (nomSalle.getText().isEmpty() || typeSalle.getValue() == null || capaciteSalle.getText().isEmpty() || descriptionSalle.getText().isEmpty() || blocSalle.getValue() == null) {
                // Show validation failure message
                Alert validationFailureDialog = new Alert(Alert.AlertType.ERROR);
                validationFailureDialog.setTitle("Validation Error");
                validationFailureDialog.setHeaderText("Validation Error");
                validationFailureDialog.setContentText("Please enter all required fields.");
                validationFailureDialog.showAndWait();
                return; // Stop further execution
            }
            // Charger le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion à la base de données
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");

            // Préparer la requête d'insertion
            String sql = "INSERT INTO salle (nom_salle, type_salle, capacite, description, bloc) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                // Définir les valeurs des paramètres
                pstmt.setString(1, nomSalle.getText());
                pstmt.setString(2, typeSalle.getValue());
                pstmt.setInt(3, Integer.parseInt(capaciteSalle.getText()));
                pstmt.setString(4, descriptionSalle.getText());
                pstmt.setString(5, blocSalle.getValue());

                // Exécuter la requête d'insertion
                int rowsAffected = pstmt.executeUpdate();
                
                
                if (rowsAffected > 0) {
                	// Show success message
                    Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                    successDialog.setTitle("Success");
                    successDialog.setHeaderText("Success ");
                    successDialog.setContentText("Salle added successfully!");
                    successDialog.showAndWait();
                	//checkAjout.setText("La salle a été ajoutée avec succès !");
                	nomSalle.setText("");
                    typeSalle.setValue("");  
                    capaciteSalle.setText("");
                    descriptionSalle.setText("");
                    blocSalle.setValue("");
                } else {
                	// Show success message
                    Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                    successDialog.setTitle("Failure");
                    successDialog.setHeaderText("Failure");
                    successDialog.setContentText("Échec de l'ajout de la salle!");
                    successDialog.showAndWait();
                	//checkAjout.setText("Échec de l'ajout de la salle.");                
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedModel.callRefreshTable();
    }
}
