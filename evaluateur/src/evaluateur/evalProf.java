package evaluateur;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		System.out.println("Pilote chargé");
		
		//format de connection pour mySQL
		String connection = "jdbc:mysql://localhost:3306/tpweb";
		String userName = "root";
		String mdp = "";
		
		Connection maConnexion = null; //Pour que l'objet existe dans la suite du programme (le try suivant conditionne son existence ce qui ne permet pas de le déclarer dedans)
		
		//format de connection pour mySQL
		try {
			maConnexion = DriverManager.getConnection(connection,userName,mdp);
		}
		catch (SQLException e) {
			System.out.println("Impossible de se connecter à la base de données");
        	System.exit(1);
		}
		System.out.println("Connecté");
		
		//CallableStatement : Appel procédure/fonction
		//PreparedStatement : adapté pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
		//Statement : adapté pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE
		
		Statement stmt = maConnexion.createStatement();
		ResultSet resultat = stmt.executeQuery("SELECT * FROM trajet"); //Requete "FIXE"
		
		//On récupère le nombre de colonnes du résultat de la requete
		int nbCol = resultat.getMetaData().getColumnCount();
		
		//"Table" qui va récupérer chaque ligne du résultat de la requete
		ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();
		
		while(resultat.next()) {
			//Une ligne doit pouvoir stocker différent types car une requete peut renvoyer plusieurs types de données (on utilise donc Object)
			ArrayList<Object> ligne = new ArrayList<Object>();
			for (int i = 1; i < nbCol; i++) { //Pour chaque colonne
				ligne.add(resultat.getObject(i)); //On ajoute à la ligne la valeur de la colonne
			}
			table.add(ligne);
		}
		
		if (table.isEmpty()) {
			System.out.println("La requete n'a rien renvoyé, elle comporte surement une erreur");
			System.exit(1);
		}
		
		System.out.println(table.toString());
		
		//Verifie si le .json est vide
		File exist = new File("test.json");
		if (exist.length() == 0) { //Si le fichier ne contient pas de liste
			System.out.println("Le fichier est vide");
			JSONObject reponse = new JSONObject(); //On crée la premiere réponse de la liste
			reponse.put(1,table);
			JSONArray reponses = new JSONArray();//On crée la liste de reponses
			reponses.add(reponse); //On ajoute la réponse à la liste
			try (FileWriter file = new FileWriter("test.json")) {
	            file.write(reponses.toJSONString()); //On met la liste dans le json
	            file.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} else { //Le fichier contient deja une liste
			JSONParser jsonParser = new JSONParser();
			try (FileReader reader = new FileReader("test.json"))
	        {
	            //Lire fichier json
	            Object obj = jsonParser.parse(reader);
	            JSONArray reponses = (JSONArray) obj;
	            int nb = reponses.size();
	            System.out.println(reponses);
	            JSONObject reponse = new JSONObject(); //On crée la premiere réponse de la liste
				reponse.put(nb+1,table);
				reponses.add(reponse);
				try (FileWriter file = new FileWriter("test.json")) {
		            file.write(reponses.toJSONString()); //On met la liste dans le json
		            file.flush();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
		
		//System.out.println(System.getProperty("user.dir"));
	}

}
