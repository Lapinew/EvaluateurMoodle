package evaluateur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Evaluateur {
	
	//NE PAS OUBLIER DE FAIRE UN COMPARATIF DES SGBD POUR RENDU FINAL

	public static void main(String[] args) throws SQLException {
		
		//TO-DO : Fermer les connexion et les resultSet aux endroits nécéssaires
		
		//RECUPERATION DES DONNEES EXTERNES NECESSAIRES A L'EXECUTION DU PROGRAMME
		int numQuestion = 1; //Pour l'eleve
		boolean enseignant = true;
		String sgbd = "mySQL";
		String requete = "SELECT * FROM test WHERE age = '40'";
		String nomTest = "test" + ".json"; //On pourrait rajouter un chemin en prefixe pour stocker les tests dans un endroit précis
		String nomFichier = "createTable"  + ".sql";
		
		//PHASE DE CONNEXION BD
		Connexion connexion = new Connexion(sgbd);
		Connection maConnexion = connexion.getConnection();
		
		//CREATION DE LA TABLE DE TEST A PARTIR D'UN FICHIER .SQL
		if (enseignant) { //PARTIE ENSEIGNANT
			int c;
			int back = 0;
			int cpt = 0;
			ArrayList<String> requetes = new ArrayList<String>();
			requetes.add("");
			try (FileReader in = new FileReader(nomFichier)) {
				c = in.read();
				while (c!=-1) {
					if (c == 59) { //Si ";"
						System.out.println(requetes.get(cpt));
						Statement stmtUpdate = maConnexion.createStatement();
						stmtUpdate.executeUpdate(requetes.get(cpt));
						requetes.add("");
						cpt++;
					}
					else if ((c == 32 && back == 32) || c == 10 || c == 13) { //Si double espace ou retour chariot linux/windows
						//Ne rien faire
					} else {
						String temp = requetes.get(cpt);
						requetes.set(cpt, temp + Character.toString((char) c));
					}
					back = (char) c;
					c = in.read();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		  //RECUPERATION DU RESULTAT DE LA REQUETE A TRAITER (voir le constructeur de la classe Table) 
		  Table objTable = new Table(requete, maConnexion);
		  
		  //TRAITEMENT DE LA TABLE RESULTAT 
		  if (enseignant) { //PARTIE ENSEIGNANT
		  objTable.toJSON(nomTest); 
		  } 
		  else { //PARTIE ELEVE
		  objTable.comparaison(nomTest, numQuestion); 
		  }
		  
		  //SUPPRESSION DES TABLES POUR MYSQL
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
		  
		  //System.out.println(System.getProperty("user.dir"));
	}

}
