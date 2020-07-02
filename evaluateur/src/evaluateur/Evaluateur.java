package evaluateur;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class Evaluateur {

	public static void main(String[] args) throws SQLException {
		
		//RECUPERATION DES DONNEES EXTERNES NECESSAIRES A L'EXECUTION DU PROGRAMME
		int numQuestion = 2;
		boolean enseignant = false; //Recu par moyen X de codeRunner
		String sgbd = "mySQL"; //Recu par moyen X de codeRunner
		String requete = "SELECT * FROM trajet WHERE depart = 'orsay'"; //Recu par moyen X de codeRunner
		String nomTest = "test" + ".json"; //Recu par moyen X de codeRunner + On pourrait rajouter un chemin en prefixe pour stocker les tests dans un endroit précis
		
		//PHASE DE CONNEXION BD
		Connexion connexion = new Connexion(sgbd);
		Connection maConnexion = connexion.getConnection();
		
		//CONVERSION DU RESULTAT DE LA REQUETE EN TABLEAU 2D/1D
		Table objTable = new Table(requete, maConnexion);
		if (objTable.vide()) {
			System.out.println("Resultat de la requete vide");
			System.exit(1);
		}
		
		if (enseignant) { //PARTIE ENSEIGNANT
			objTable.toJSON(nomTest);
		} else { //PARTIE ELEVE
			objTable.comparaison(nomTest, numQuestion);
		}
		
		//System.out.println(System.getProperty("user.dir"));
	}

}
