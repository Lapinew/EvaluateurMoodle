package evaluateur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Evaluateur {
	
	public Evaluateur() {}
	
	//NE PAS OUBLIER DE FAIRE UN COMPARATIF DES SGBD POUR RENDU FINAL

	public static void main(String[] args) throws SQLException {
		
		//TO-DO : Trouver pourquoi il récupère par tout les champs demandés lors de l'execution de la requete
		//TO-DO : Fermer les connexion et les resultSet aux endroits nécéssaires
		
		Evaluateur obj = new Evaluateur(); //Objet vide pour executer methode
		
		//RECUPERATION DES DONNEES EXTERNES NECESSAIRES A L'EXECUTION DU PROGRAMME
		int numQuestion = 1; //Pour l'eleve
		boolean enseignant = false;
		String sgbd = "mySQL";
		String requete = "SELECT * FROM test WHERE age > 40";
		String nomTest = "test" + ".json"; //On pourrait rajouter un chemin en prefixe pour stocker les tests dans un endroit précis
		String nomFichier = "createTable"  + ".sql";
		Utility utilitaire = new Utility();
		
		//PHASE DE CONNEXION BD
		Connexion connexion = new Connexion(sgbd);
		Connection maConnexion = connexion.getConnection();
		connexion.deleteBD(); //Pansement pour le probleme de destruction de bd en cas d'erreur au cours du programme
		
		Reponse reponseUser = null;
		Reponse reponseProf = null;
		
		//CREATION DE LA BD TEST A PARTIR D'UN FICHIER .SQL
		utilitaire.executeSQLfile(maConnexion, nomFichier);
		
		//GENERATION DU RESULTAT DE LA REQUETE A TRAITER (voir le constructeur de la classe Table)
		String [] splittedRequete = requete.split(" ", 4); //On récupère les premiers mots de la requete
		String typeRequete = splittedRequete[0].toUpperCase(); //On recupere le type de la requete (1er mot)
		String tableSelect = null; //Dans le cas d'une requete modifiante, on récupère la table qui est modifié pour faire un select ensuite
		if (enseignant)  { //En tant qu'enseignant on veut juste enregistrer notre reponse dans un JSON
			switch (typeRequete) { //On créer la réponse à partir de la requete
			  case "SELECT" :
				  reponseUser = new Select(requete, nomFichier, maConnexion);
			    break;
			  case "UPDATE" :
				  tableSelect = splittedRequete[1];
				  reponseUser = new Update(requete, nomFichier, maConnexion, tableSelect);
			    break;
			  case "DELETE" :
				  tableSelect = splittedRequete[2];
				  reponseUser = new Delete(requete, nomFichier, maConnexion, tableSelect);
			    break;
			  case "INSERT" : //A tweak car la "(" peut etre collé à la table je crois
				  tableSelect = splittedRequete[2];
				  reponseUser = new Insert(requete, nomFichier, maConnexion, tableSelect);
			  default:
				  System.out.println("Requete invalide : type de requete inconnue");
				  System.exit(1);
			}
			reponseUser.toJSON(nomTest); //On met la réponse dans le JSON
		} else { //Si on est élève
			//On récupère d'abord la réponse du prof
			reponseProf = reponseProf.getReponseFromJSON(nomTest, numQuestion);
			boolean queryResult = false; //Pour savoir si notre requete va avoir une table a comparer
			switch (typeRequete) {
			  case "SELECT" :
				  queryResult = true;
				  reponseUser = new Select(requete, ((QueryResult) reponseProf).getFichier(), maConnexion);
			    break;
			  case "UPDATE" :
				  queryResult = true;
				  tableSelect = splittedRequete[1];
				  reponseUser = new Update(requete, ((QueryResult) reponseProf).getFichier(), maConnexion, tableSelect);
			    break;
			  case "DELETE" :
				  queryResult = true;
				  tableSelect = splittedRequete[2];
				  reponseUser = new Delete(requete, ((QueryResult) reponseProf).getFichier(), maConnexion, tableSelect);
			    break;
			  case "INSERT" : //A tweak car la "(" peut etre collé à la table je crois
				  queryResult = true;
				  tableSelect = splittedRequete[2];
				  reponseUser = new Insert(requete, ((QueryResult) reponseProf).getFichier(), maConnexion, tableSelect);
			  default:
				  System.out.println("Requete invalide : type de requete inconnue");
				  System.exit(1);
			}
			if (queryResult) {
				((QueryResult) reponseUser).comparaisonTable((QueryResult) reponseProf);
			}
			reponseUser.compareSynthaxe(reponseProf.getRequete());
		  }
		  
		  //System.out.println(System.getProperty("user.dir"));
	}

}
