package evaluateur;

import java.sql.*;

public class Connexion { //Cette classe est configur�e pour se connecter au BD locales
	
	//Attributs
	private String driver;
	private String url;
	private String user;
	private String pwd;
	
	//Constructeurs
	public Connexion(String sgbd) {
		switch (sgbd) { //Chargement du driver adapt� et connexion au SGBD choisi par l'enseignant sur codeRunner
		  case "mySQL" :
			  driver = "com.mysql.jdbc.Driver";
			  url = "jdbc:mysql://localhost:3306/tpweb";
			  user = "root";
			  pwd = "";
		    break;
		  case "oracle" : //Pas configur�
			  driver = "oracle.jdbc.driver.OracleDriver";
			  url = "jdbc:oracle:thin:@";
			  user = "root";
			  pwd = "";
		    break;
		  case "postgre" : //Pas configur�
			  driver = "org.postgresql.Driver";
			  url = "jdbc:postgresql://";
			  user = "root";
			  pwd = "";
		    break;
		}
	}
	
	//Methodes
	public Connection getConnection() {
		try {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Impossible de charger le pilote");
            System.exit(1);
        }
		System.out.println("Pilote charg�");
		
		Connection maConnexion = null;
		
		try {
			maConnexion = DriverManager.getConnection(url,user,pwd);
		}
		catch (SQLException e) {
			System.out.println("Impossible de se connecter � la base de donn�es");
        	System.exit(1);
		}
		System.out.println("Connect� � " + url);
		
		return maConnexion;
	}
	
}
