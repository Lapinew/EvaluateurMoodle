package evaluateur;

import java.sql.*;

public class Connexion { //Cette classe est configurée pour se connecter au BD locales
	
	//Attributs
	private String driver;
	private String url;
	private String user;
	private String pwd;
	private Connection maConnexion;
	
	//Constructeurs
	public Connexion(String sgbd) {
		switch (sgbd) { //Chargement du driver adapté et connexion au SGBD choisi par l'enseignant sur codeRunner
		  case "mySQL" :
			  driver = "com.mysql.jdbc.Driver";
			  url = "jdbc:mysql://localhost:3306/testevaluateur";
			  user = "root";
			  pwd = "";
		    break;
		  case "oracle" : //Pas configuré
			  driver = "oracle.jdbc.driver.OracleDriver";
			  url = "jdbc:oracle:thin:@";
			  user = "root";
			  pwd = "";
		    break;
		  case "postgre" : //Pas configuré
			  driver = "org.postgresql.Driver";
			  url = "jdbc:postgresql://";
			  user = "root";
			  pwd = "";
		    break;
		}
		try {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Impossible de charger le pilote");
            System.exit(1);
        }
		System.out.println("Pilote chargé");
		
		try {
			maConnexion = DriverManager.getConnection(url,user,pwd);
		}
		catch (SQLException e) {
			System.out.println("Impossible de se connecter à la base de données");
        	System.exit(1);
		}
		System.out.println("Connecté à " + url);
	}
	
	//Methodes
	public Connection getConnection() {
		return maConnexion;
	}
	
	public void deleteBD() {
		try {
			  Statement offConstraints = maConnexion.createStatement();
			  offConstraints.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
			  Statement getTables = maConnexion.createStatement();
			  ResultSet resultat = getTables.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'testevaluateur'");
			  while (resultat.next()) {
				  Statement delTable = maConnexion.createStatement();
				  delTable.executeUpdate("DROP TABLE IF EXISTS " + resultat.getString(1));
			  }
			  Statement onConstraints = maConnexion.createStatement();
			  onConstraints.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
		}
		catch (SQLException e) {
			System.out.println(e);
	    	System.exit(1);
		}
	}
	
}
