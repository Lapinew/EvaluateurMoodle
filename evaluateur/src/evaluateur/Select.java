package evaluateur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Select extends Table { //Créer une réponse à partir d'un SELECT

	public Select(String requete, String nomFichier, Connection connexion) {
		//TRANSFORMATION DU RESULTSET EN ARRAY
		super(requete, nomFichier);
		try {
			Statement stmt = connexion.createStatement();
			ResultSet resultat = stmt.executeQuery(requete); //Requete "FIXE"
			int nbCol = resultat.getMetaData().getColumnCount(); //Recupere nb colonne du resultat de la requete
			table = new ArrayList<ArrayList<Object>>();
			
			while(resultat.next()) {
				//Une ligne doit pouvoir stocker différent types car une requete peut renvoyer plusieurs types de données (on utilise donc Object)
				ArrayList<Object> ligne = new ArrayList<Object>();
				for (int i = 1; i < nbCol; i++) { //Pour chaque colonne
					ligne.add(resultat.getString(i)); //On ajoute à la ligne la valeur de la colonne
				}
				table.add(ligne);
			}
			stmt.close();
			System.out.println("table crée");
		}
		catch (SQLException e) {
			System.out.println(e);
	    	System.exit(1);
		}
	}
}