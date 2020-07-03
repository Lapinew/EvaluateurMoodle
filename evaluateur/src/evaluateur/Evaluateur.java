package evaluateur;

import java.sql.*;

public class Evaluateur {

	public static void main(String[] args) throws SQLException {
		
		//TO-DO : Fermer les connexion et les resultSet aux endroits nécéssaires
		
		//RECUPERATION DES DONNEES EXTERNES NECESSAIRES A L'EXECUTION DU PROGRAMME
		int numQuestion = 1; //Pour l'eleve
		boolean enseignant = false;
		String sgbd = "mySQL";
		String requete = "UPDATE trajet SET depart = 'lille' WHERE id_trajet = 4";
		String nomTest = "test" + ".json"; //On pourrait rajouter un chemin en prefixe pour stocker les tests dans un endroit précis
		
		//PHASE DE CONNEXION BD
		Connexion connexion = new Connexion(sgbd);
		Connection maConnexion = connexion.getConnection();
		
		//RECUPERATION DU RESULTAT DE LA REQUETE A TRAITER (voir le constructeur de la classe Table)
		Table objTable = new Table(requete, maConnexion);
		
		//TRAITEMENT DE LA TABLE RESULTAT
		if (enseignant) { //PARTIE ENSEIGNANT
			objTable.toJSON(nomTest);
		} else { //PARTIE ELEVE
			objTable.comparaison(nomTest, numQuestion);
		}
		
		//System.out.println(System.getProperty("user.dir"));
	}

}
