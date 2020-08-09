package evaluateur;

import java.sql.*;

public class Evaluateur {
	
	public Evaluateur() {}
	
	//NE PAS OUBLIER DE FAIRE UN COMPARATIF DES SGBD POUR RENDU FINAL

	public static void main(String[] args) throws SQLException {
		
		//TO-DO : Trouver pourquoi il r�cup�re pas tout les champs demand�s lors de l'execution de la requete
		//TO-DO : Fermer les connexion et les resultSet aux endroits n�c�ssaires
		
		//RECUPERATION DES DONNEES EXTERNES NECESSAIRES A L'EXECUTION DU PROGRAMME
		int numQuestion = 3; //Pour l'eleve
		boolean enseignant = false;
		String sgbd = "mySQL";
		String requete = "insert into test ( prenom , age ) values ('Kiroul', 68)";
		//String requete = "UPDATE TEST SET AGE = 41 , nom=\"bob\",prenom='marley' WHERE age> 50 AND prenom = 'Kiroul'";
		String nomTest = "test" + ".json"; //On pourrait rajouter un chemin en prefixe pour stocker les tests dans un endroit pr�cis
		String nomFichier = "createTable"  + ".sql";
		
		Utility utilitaire = new Utility(); //Fonctions "standalone"
		
		if (requete.charAt(requete.length()-1) != 59) { //S'il manque le point virgule
			requete += ";";
		}
		
		//PHASE DE CONNEXION BD
		Connexion connexion = new Connexion(sgbd);
		Connection maConnexion = connexion.getConnection();
		connexion.deleteBD(); //Pansement pour le probleme de destruction de bd en cas d'erreur au cours du programme
		
		Reponse reponseUser = null;
		Reponse reponseProf = null;
		Select dummy = new Select("dummy", "dummy");
		
		//GENERATION DU RESULTAT DE LA REQUETE A TRAITER (voir le constructeur de la classe Table)
		String [] splittedRequete = requete.split(" ", 4); //On r�cup�re les premiers mots de la requete
		String typeRequete = splittedRequete[0].toUpperCase(); //On recupere le type de la requete (1er mot)
		String tableSelect = null; //Dans le cas d'une requete modifiante, on r�cup�re la table qui est modifi� pour faire un select ensuite
		if (enseignant)  { //En tant qu'enseignant on veut juste enregistrer notre reponse dans un JSON
			utilitaire.executeSQLfile(maConnexion, nomFichier);
			switch (typeRequete) { //On cr�er la r�ponse � partir de la requete
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
			  case "INSERT" : //A tweak car la "(" peut etre coll� � la table je crois
				  tableSelect = splittedRequete[2];
				  reponseUser = new Insert(requete, nomFichier, maConnexion, tableSelect);
				  break;
			  default:
				  System.out.println("Requete invalide : type de requete inconnue");
				  System.exit(1);
			}
			reponseUser.toJSON(nomTest); //On met la r�ponse dans le JSON
		} else { //Si on est �l�ve
			//On r�cup�re d'abord la r�ponse du prof
			reponseProf = dummy.getReponseFromJSON(nomTest, numQuestion);
			utilitaire.executeSQLfile(maConnexion, ((QueryResult) reponseProf).getFichier());
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
			  case "INSERT" : //A tweak car la "(" peut etre coll� � la table je crois
				  queryResult = true;
				  tableSelect = splittedRequete[2];
				  reponseUser = new Insert(requete, ((QueryResult) reponseProf).getFichier(), maConnexion, tableSelect);
				  break;
			  default:
				  System.out.println("Requete invalide : type de requete inconnue");
				  System.exit(1);
			}
			if (queryResult) {
				((QueryResult)reponseUser).comparaisonTable((QueryResult) reponseProf);
			}
			reponseUser.compareSyntaxe(reponseProf);
		  }
		  //System.out.println(System.getProperty("user.dir"));
	}

}
