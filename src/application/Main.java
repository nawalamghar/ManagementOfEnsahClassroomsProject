package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	public void start(Stage primaryStage) {
		deleteOldData();
		try {
			try {
				
				Parent root=(Parent) FXMLLoader.load(getClass().getResource("/interfaces/Main.fxml"));
				Scene scene =new Scene(root);
				primaryStage.setTitle("Gestion Des Salles de l'ENSAH");
				primaryStage.setScene(scene);
				primaryStage.show();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteOldData() {
        String url = "jdbc:mysql://localhost:3306/ensahjava";
        String utilisateur = "root";
        String motDePasse = "";

        try (Connection connection = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            String sql = "DELETE FROM affectation WHERE date < CURDATE()";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Nombre de lignes supprimées : " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	/*public void changeScene(String fxml)throws IOException{
		Parent pane=FXMLLoader.load(getClass().getResource(fxml));
		stg.getScene().setRoot(pane);
	}*/
	public static void main(String[] args) {
		launch(args);
	}
}
