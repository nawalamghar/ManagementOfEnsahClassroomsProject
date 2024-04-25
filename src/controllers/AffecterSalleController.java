package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AffecterSalleController {
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

    private ObservableList<String> originalItemscoursBox; // Use the existing items from coursBox

    @FXML
    private ComboBox<String> profBox;

    private ObservableList<String> originalItemsprofBox; // Use the existing items from profBox

    @FXML
    private ComboBox<String> blocBox;

    @FXML
    private ComboBox<String> nomsalleBox;
    

    @FXML
    private ComboBox<String> jourBox;
	private GestionsAffectationsController gestionsAffectationsController;

    @FXML
    private void initialize() {
        typesalleBox.getItems().addAll("Cours", "TD", "Laboratoire", "TP-informatique", "Amphi");
        blocBox.getItems().addAll("A", "B");
        jourBox.getItems().addAll("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi");
        jourBox.setOnAction(event -> loadComboBoxDataSalle());
        heurefinField.setOnKeyReleased(event -> loadComboBoxDataSalle());
        heuredebutField.setOnKeyReleased(event -> loadComboBoxDataSalle());
        typesalleBox.setOnAction(event -> loadComboBoxDataSalle());
        blocBox.setOnAction(event -> loadComboBoxDataSalle());

        loadComboBoxDataCours();
        loadComboBoxDataProf();
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
            originalItemscoursBox = FXCollections.observableArrayList(items); // Use the existing items from coursBox

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    private void loadComboBoxDataProf() {
        String url = "jdbc:mysql://localhost:3306/ensahjava";

        try {
        	Connection connection = DriverManager.getConnection(url, "root", "");
        
             Statement statement = connection.createStatement();

            String sqlprof = "SELECT nom_personne from Personne where role='prof' ";
            ResultSet resultSetprof = statement.executeQuery(sqlprof);

            ObservableList<String> items = FXCollections.observableArrayList();
            while (resultSetprof.next()) {
                items.add(resultSetprof.getString("nom_personne"));
            }

            profBox.setItems(items);
            originalItemsprofBox = FXCollections.observableArrayList(items); // Use the existing items from coursBox

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }
    
    private void loadComboBoxDataSalle() {
        // Récupérer les valeurs des champs
        String day = jourBox.getValue();
        String heureDebutText = heuredebutField.getText();
        String heureFinText = heurefinField.getText();
        String typeSalle = typesalleBox.getValue();
        String bloc=blocBox.getValue();

        if (day != null && !heureDebutText.isEmpty() && !heureFinText.isEmpty() && typeSalle != null) {
            String url = "jdbc:mysql://localhost:3306/ensahjava";

			try {
				Connection connection = DriverManager.getConnection(url, "root", "");
			
			     PreparedStatement preparedStatement = connection.prepareStatement(
			    		 "SELECT s.id_salle, s.nom_salle " +
			                     "FROM salle s " +
			                     "WHERE s.type_salle = ? AND NOT EXISTS (" +
			                     "    SELECT 1 FROM affectation a " +
			                     "    WHERE a.id_salle = s.id_salle " +
			                     "        AND a.jour = ? " +
			                     "        AND a.heure_debut = ? " +
			                     "        AND a.heure_fin = ? " +
			                     "        AND s.bloc = ?)");

			     preparedStatement.setString(1, typeSalle);
			     preparedStatement.setString(2, day);
			     preparedStatement.setString(3, heureDebutText);
			     preparedStatement.setString(4, heureFinText);
			     preparedStatement.setString(5, bloc);


			    ResultSet resultSet = preparedStatement.executeQuery();

			    ObservableList<String> items = FXCollections.observableArrayList();
			    while (resultSet.next()) {
			        items.add(resultSet.getString("nom_salle"));
			    }

			    nomsalleBox.setItems(items);

			} catch (SQLException e) {
			    e.printStackTrace();
			    // Gérer l'exception de manière appropriée (par exemple, afficher un message d'erreur)
			}}}

    @FXML
    private void handlecoursBoxKeyReleased(KeyEvent event) {
        String userInput = coursBox.getEditor().getText().toLowerCase();

        // If the input is not empty, filter the items based on the user input
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
            // If the input is empty, show all original items and the dropdown
            coursBox.setItems(originalItemscoursBox);
            coursBox.show();
        }
    }

    
    @FXML
    private void handleprofBoxKeyReleased(KeyEvent event) {
        String userInput = profBox.getEditor().getText().toLowerCase();

        // If the input is not empty, filter the items based on the user input
        if (!userInput.isEmpty()) {
            ObservableList<String> filteredItems = FXCollections.observableArrayList();
            for (String item : originalItemsprofBox) {
                if (item.toLowerCase().startsWith(userInput)) {
                    filteredItems.add(item);
                }
            }
            profBox.setItems(filteredItems);
            profBox.show();
        } else {
            // If the input is empty, show all original items and the dropdown
            profBox.setItems(originalItemsprofBox);
            profBox.show();
        }
    }
    @FXML
    public void btnAffecterSalle (ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/ensahjava";
        String day = jourBox.getValue();
        String heureDebutText = heuredebutField.getText();
        String heureFinText = heurefinField.getText();
        String typeSalle = typesalleBox.getValue();
        String nomSalle=nomsalleBox.getValue();
        String bloc=blocBox.getValue();
        String nomProf=profBox.getValue();
        String nomCours=coursBox.getValue();
        
        try {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Time heureDebut = new Time(sdf.parse(heureDebutText).getTime());
        Time heureFin = new Time(sdf.parse(heureFinText).getTime());
        
    	
    	
    		 Connection connection = DriverManager.getConnection(url, "root", "");
    	
    	     PreparedStatement preparedStatement = connection.prepareStatement(
    		             "INSERT INTO affectation (heure_debut, heure_fin, jour, id_salle, id_cours, id_personne) " +
    		                     "VALUES (?, ?, ?, (SELECT id_salle FROM salle WHERE nom_salle = ? AND bloc = ? AND type_salle = ?), " +
    		                     "(SELECT id_cours FROM cours WHERE nom_cours = ?), " +
    		                     "(SELECT id_personne FROM personne WHERE nom_personne = ? AND role = 'prof'))");

    		    preparedStatement.setTime(1, heureDebut);
    		    preparedStatement.setTime(2, heureFin);
    		    preparedStatement.setString(3, day);
    		    preparedStatement.setString(4, nomSalle);
    		    preparedStatement.setString(5, bloc);
    		    preparedStatement.setString(6, typeSalle);
    		    preparedStatement.setString(7, nomCours);
    		    preparedStatement.setString(8, nomProf);
    		    int rowsAffected = preparedStatement.executeUpdate();

                // Show Alert dialog based on the result
                Alert alert = new Alert(rowsAffected > 0 ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle(rowsAffected > 0 ? "Success" : "Failure");
                alert.setHeaderText(rowsAffected > 0 ? "Success" : "Failure");
                alert.setContentText(rowsAffected > 0 ? "Affectation faite avec succès!" : "Échec de l'affectation!");
                alert.showAndWait();

                // Clear input fields on success
                if (rowsAffected > 0) {
                    jourBox.setValue(null);
                    heuredebutField.clear();
                    heurefinField.clear();
                    typesalleBox.setValue(null);
                    blocBox.setValue(null);
                    nomsalleBox.setValue(null);
                    coursBox.setValue(null);
                    profBox.setValue(null);
                }
            
            
    		} catch (SQLException | ParseException e ) {
    		    e.printStackTrace();
    		}

    }

    public void setGestionsAffectationsController(GestionsAffectationsController gestionsAffectationsController) {
        this.gestionsAffectationsController = gestionsAffectationsController;
    }    
}
