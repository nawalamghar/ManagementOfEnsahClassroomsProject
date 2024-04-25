package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.stage.Stage;

public class AcceuilController {
	private  int rowCount=0;
    private Stage stage;
    private Scene scene ;
    private Parent root;
	@FXML
    private Button acceuilbtn;
    @FXML
    private Button gestionAffectationbtn;
	@FXML
    private Button gestionSallebtn;
    @FXML
    private Button gestionDemandebtn;
    @FXML
    private Button deconnexionbtn;
    @FXML
	private Label enAttenteLabel;
    @FXML
    private LineChart<String, Number> lineChart;
    
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private void initialize() {
    	updateEnAttenteLabel() ;
    	System.out.println("Controller initialized");
    	setupLineChart();
    	lineChart.setStyle("-fx-background-color: transparent;");
    	
    }


    private void setupLineChart() {       
    	getChartDataFromDatabase();   
    	styleLineChartSeries();
    }

    private void getChartDataFromDatabase() {
        // Créez une seule série de données pour tous les jours
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Données");

        // Ajoutez un point pour chaque jour
        for (String day : new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"}) {
            int count = getCountFromDatabase(day);
            System.out.println("Day: " + day + ", Count: " + count);  // Print for debugging

            // Ajoutez chaque point à la série
            series.getData().add(new XYChart.Data<>(day, count));
        }

        // Ajoutez la série au graphique
        lineChart.getData().add(series);

        // Appliquez le style à la ligne de la série
        Node line = series.getNode().lookup(".chart-series-line");
        if (line != null) {
            line.setStyle("-fx-stroke: orange; -fx-stroke-width: 2px;");
        }
    }

    private void styleLineChartSeries() {
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node line = data.getNode().lookup(".chart-series-line");
                if (line != null) {
                    // Définissez le style de la ligne ici
                    line.setStyle("-fx-stroke: blue; -fx-stroke-width: 2px;");
                }
            }
        }
    }


    private int getCountFromDatabase(String day) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensahjava", "root", "");

            // Use a prepared statement for parameterized queries
            String query = "SELECT COUNT(*) AS count FROM affectation WHERE jour=?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, day);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                    	
                        return rs.getInt("count");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
        
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
    public void gestionDemandebtn(ActionEvent event) {
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
