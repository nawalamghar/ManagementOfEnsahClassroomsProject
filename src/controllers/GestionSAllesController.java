package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionSAllesController {
    	private  int rowCount=0;
	    private Stage stage;
	    private Scene scene ;
	    private Parent root;
	    @FXML
	    private ComboBox<String> Rechercher;
	    @FXML
	    private Button refreshbtn;
	    @FXML
	    private Button gestionSalletn;
	    @FXML
	    private Button gestionAffectationbtn;
	    @FXML
	    private Button acceuilbtn;
	    @FXML
	    private Button ajouterSalleButton;
	    @FXML
	    private Button supprimerSelectionButton;
	    @FXML
	    private Button modifierrSelectionButton;
	    @FXML
	    private TableView<Salle> salleTable;
	    @FXML
	    private TableColumn<Salle, String> nomCol;
	    @FXML
	    private TableColumn<Salle, String> typeCol;
	    @FXML
	    private TableColumn<Salle, Integer> capaciteCol;
	    @FXML
	    private TableColumn<Salle, String> blocCol;
	    @FXML
	    private TableColumn<Salle, Integer> idCol;
	    @FXML
	    private ObservableList<Salle> salleData = FXCollections.observableArrayList();
	    @FXML
	    private Label nombreLignes;
	    @FXML
	    private Salle selectedSalle;
	    private SharedModel sharedModel;
	    @FXML
	    private TableColumn<Salle, String> timeSlotColumn;
	    @FXML
	    private void initialize() {
	    	updateEnAttenteLabel() ;
	    	sharedModel = new SharedModel();
	        // Définir la référence au contrôleur GestionSalleController
	        SharedModel.setGestionSalleController(this);
	    	//System.out.println("salleTable is: " + salleTable);
	        
	        Rechercher.getItems().addAll("Cours", "TD", "Laboratoire", "TP-informatique","Amphi");

	        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom_salle"));
	        typeCol.setCellValueFactory(new PropertyValueFactory<>("type_salle"));
	        capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));
	        blocCol.setCellValueFactory(new PropertyValueFactory<>("bloc"));
	        //descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
	        salleData.addAll(fetchSalleData());
	        salleTable.setItems(salleData);
	     // Add a double-click event handler to the table rows
	        salleTable.setRowFactory(tv -> {
	            TableRow<Salle> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && !row.isEmpty()) {
	                    onRowDoubleClick(row.getItem());
	                }
	            });
	            return row;
	        });
	            salleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            	if (supprimerSelectionButton != null) {
	                    supprimerSelectionButton.setDisable(newValue == null);
	                }
	            	selectedSalle=salleTable.getSelectionModel().getSelectedItem();
	            	System.out.println("selected salle33333:"+selectedSalle);
	            });
	            
	            Rechercher.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	                // Appeler une méthode pour rafraîchir les données en fonction de la nouvelle sélection
	                refreshTableBasedOnType(newValue);
	            });
	           // initializeTableView();
	    }
	    public void refreshbtn(ActionEvent event) {
			try {
				SharedModel.callRefreshTable();
				
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
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
	    public void btnGestionDemandes(ActionEvent event) {
			try {
				root = FXMLLoader.load(getClass().getResource("/interfaces/GestionDemandes.fxml"));
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
	    private void refreshTableBasedOnType(String selectedType) {
	    	salleData.setAll(fetchSalleData());
	    	List<Salle> filteredSalles = salleData.stream()
	                .filter(salle -> salle.getType_salle().equals(selectedType))
	                .collect(Collectors.toList());

	        // Rafraîchissez la table avec les nouvelles données
	        salleData.setAll(filteredSalles);
	    }
	    private List<Salle> fetchSalleData() {
	    	List<Salle> salleList = new ArrayList<>();
	    	
	    	try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");

	            String sql = "SELECT nom_salle, type_salle, capacite, bloc,id_salle,description FROM salle";
	            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	                ResultSet rs = pstmt.executeQuery();

	                while (rs.next()) {
	                    String nomSalle = rs.getString("nom_salle");
	                    String typeSalle = rs.getString("type_salle");
	                    int capacite = rs.getInt("capacite");
	                    String bloc = rs.getString("bloc");
	                    int id = rs.getInt("id_salle");
	                    String description = rs.getString("description");
	                    Salle salle = new Salle(nomSalle, typeSalle, capacite, bloc,id,description);
	                    salleList.add(salle);
	                }
	                Statement stmt = con.createStatement();
	                ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) AS count FROM salle");

	                if (rs2.next()) {
	                    rowCount = rs2.getInt("count");
	                    if(rowCount>0)
	                    nombreLignes.setText("Affichage de " + rowCount+" elements.");
	                    else 
	                    	nombreLignes.setText("Aucun element a afficher.");
	                } else {
	                	nombreLignes.setText("La requête n'a retourné aucun résultat.");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return salleList;
	    }
	
	    private void onRowDoubleClick(Salle selectedSalle) {
	        try {
	            // You can access the selectedSalle and perform any necessary actions
	        	SharedModel.setSalleSelectionnee(selectedSalle);
	            System.out.println("Double-clicked on: " + selectedSalle);

	            // Load the calendrier.fxml file
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/CalendrierSalle.fxml"));
	            Parent newRoot = loader.load();
	            Stage newStage = new Stage();
	            newStage.initModality(Modality.WINDOW_MODAL);
	            newStage.initOwner(stage); // Set the owner stage if needed

	            Scene newScene = new Scene(newRoot);
	            newStage.setScene(newScene);
	            newStage.show();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    private void initializeTableView() {
	        timeSlotColumn.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

	    }

	public void btnAjouterSalle(ActionEvent event) {
		try { 
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/AjouterSalle.fxml"));
			Parent newRoot = loader.load();
	        Stage newStage = new Stage();
	        newStage.initModality(Modality.WINDOW_MODAL);
	        newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
	        
	        Scene newScene = new Scene(newRoot);
	        newStage.setScene(newScene);
	        newStage.show();
	        
	        System.out.println("Nouvelle fenêtre chargée.");
	        System.out.println("selected salle1:"+selectedSalle);
			
        } catch (Exception e) {
            e.printStackTrace();
        }
	    }
	
		@FXML
		private void btnSupprimerSalle(ActionEvent event) {
			//Salle selectedSalle = salleTable.getSelectionModel().getSelectedItem();
	        if (selectedSalle != null) {
	            salleData.remove(selectedSalle);
	            rowCount--;
	            if(rowCount>0)
                    nombreLignes.setText("Affichage de " + rowCount+" elements.");
                else 
                    nombreLignes.setText("Aucun element a afficher.");
	            deleteSalleFromDatabase(selectedSalle);
	            salleTable.getSelectionModel().clearSelection();
	        }
		}
		private void deleteSalleFromDatabase(Salle salle) {
			System.out.print(salle.getid());
	        String deleteSQL = "DELETE FROM salle WHERE id_salle = ?";
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");
	             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

	            preparedStatement.setInt(1, salle.getid()); 

	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	            	Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                    successDialog.setTitle("Success");
                    successDialog.setHeaderText("Success");
                    successDialog.setContentText("Salle deleted successfully!");
                    successDialog.showAndWait();
	                System.out.println("Salle deleted successfully from the database.");
	            } else {
	                System.out.println("No salle deleted from the database.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		public void refreshTable() {
		    try {
		        // Effacer les données actuelles de la table
		    	salleTable.getItems().clear();
		        salleData.clear();

		        // Recharger les données depuis la base de données
		        salleData.addAll(fetchSalleData());

		        // Rafraîchir la table avec les nouvelles données
		        salleTable.setItems(salleData);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		@FXML
	    private void btnModifierSalle(ActionEvent event) {
			try {
				SharedModel.setSalleSelectionnee(selectedSalle);
				//SharedModel.setSalleData(SalleData);
				//SharedModel.refreshTable(salleTable);
			    System.out.println("selected salle0000:"+selectedSalle);
				System.out.println("Chargement du fichier modifier FXML...");	
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaces/ModifierSalle.fxml"));
				Parent newRoot = loader.load();
		        Stage newStage = new Stage();
		        newStage.initModality(Modality.WINDOW_MODAL);
		        newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
		        
		        Scene newScene = new Scene(newRoot);
		        newStage.setScene(newScene);
		        newStage.show();

		      
		        System.out.println("Nouvelle fenêtre modifier chargée.");     
				
	        } catch (Exception e) {
	            e.printStackTrace();
	    }

		}
		
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
}
