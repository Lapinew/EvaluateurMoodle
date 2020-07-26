package evaluateur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Modify extends Table { //Créer une réponse à partir d'une requete modifiante
	
	protected String tableSelect;
	
	public Modify(String requete, String nomFichier, Connection connexion, String tabSelect) {
		//TRANSFORMATION DU RESULTSET EN ARRAY
		super(requete, nomFichier);
		try {
			//Comme la requete est modifiante on modifie d'abord la table puis on remplace la requete par un select de la table pour voir ce qui a ete modifié
			Statement stmtUpdate = connexion.createStatement();
			stmtUpdate.executeUpdate(requete);
			stmtUpdate.close();
			requete = "SELECT * FROM " + tableSelect;
			
			//TRANSFORMATION DU RESULTSET EN ARRAY
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
