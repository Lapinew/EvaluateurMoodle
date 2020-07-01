package evaluateur;

import java.sql.*;
import java.util.*;

public class evalProf {

	public static void main(String[] args) throws SQLException {
		//Chargement du driver (choisir celui voulu sans oublier d'importer le jar correspondant : mySQL, oracleDatabase...)
		try {
            Class.forName("com.mysql.jdbc.Driver"); //Pour mySQL https://www.javatpoint.com/example-to-connect-to-the-mysql-database
        }
        catch (ClassNotFoundException e) {
            System.out.println("Impossible de charger le pilote");
            System.exit(1);
        }
		System.out.println("Pilote charg�");
		
		//format de connection pour mySQL
		String connection = "jdbc:mysql://localhost:3306/tpweb";
		String userName = "root";
		String mdp = "";
		
		Connection maConnexion = null; //Pour que l'objet existe dans la suite du programme (le try suivant conditionne son existence ce qui ne permet pas de le d�clarer dedans)
		
		//format de connection pour mySQL
		try {
			maConnexion = DriverManager.getConnection(connection,userName,mdp);
		}
		catch (SQLException e) {
			System.out.println("Impossible de se connecter � la base de donn�es");
        	System.exit(1);
		}
		System.out.println("Connect�");
		
		//CallableStatement : Appel proc�dure/fonction
		//PreparedStatement : adapt� pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
		//Statement : adapt� pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE
		
		Statement stmt = maConnexion.createStatement();
		ResultSet resultat = stmt.executeQuery("SELECT * FROM trajet"); //Requete "FIXE"
		
		//On r�cup�re le nombre de colonnes du r�sultat de la requete
		int nbCol = resultat.getMetaData().getColumnCount();
		
		//"Table" qui va r�cup�rer chaque ligne du r�sultat de la requete
		ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();
		
		while(resultat.next()) {
			//Une ligne doit pouvoir stocker diff�rent types car une requete peut renvoyer plusieurs types de donn�es (on utilise donc Object)
			ArrayList<Object> ligne = new ArrayList<Object>();
			for (int i = 1; i < nbCol; i++) { //Pour chaque colonne
				ligne.add(resultat.getObject(i)); //On ajoute � la ligne la valeur de la colonne
			}
			table.add(ligne);
		}
		
		System.out.println(table.toString());
		
	}

}
