package evaluateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import com.google.gson.*;

public class Table { //Cette classe permet de convertir un ResultSet en un tableau 2D/3D exploitable
	
	//CallableStatement : Appel procédure/fonction
	//PreparedStatement : adapté pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
	//Statement : adapté pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE

	//Attributs
	private ArrayList<ArrayList<Object>> table;
	private boolean vide;
	
	//Constructeurs
	public Table(String requete, Connection connexion) {
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
			System.out.println("table crée");
		}
		catch (SQLException e) {
			System.out.println("Erreur au niveau de la conversion en table");
        	System.exit(1);
		}
		
		if (table.isEmpty()) {
			vide = true;
			System.out.println("La table est vide");
		}
		else {
			vide = false;
			System.out.println("La table contient des données");
		}
	}
	
	//Methodes
	public ArrayList<ArrayList<Object>> getTable () {
		return table;
	}
	
	public boolean vide() {
		return vide;
	}
	
	public void afficheTable() {
		System.out.println(table.toString());
	}
	
	private void aLaSuite(String nomTest) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null;
		try (FileReader reader = new FileReader(nomTest)) {
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList"));
			reponses.add(table);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (FileWriter file = new FileWriter(nomTest)) {
			gson.toJson(reponses, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void premiereTable(String nomTest) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> buffer = new ArrayList<ArrayList<ArrayList<Object>>>();
		buffer.add(table);
		try (FileWriter file = new FileWriter(nomTest)) {
			gson.toJson(buffer, file);
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}
	
	public void toJSON(String nomTest) {
		File exist = new File(nomTest);
		if (exist.length() == 0) { //Si le fichier ne contient pas de liste
			System.out.println("Le fichier est vide, cette table va initialiser un nouveau JSONArray");
			this.premiereTable(nomTest);
		} else { //Le fichier contient deja une liste
			System.out.println("Le fichier contient deja un JSONArray, la table va etre ajoutée a la suite");
			this.aLaSuite(nomTest);
	    }
	}
	
	public void test(String nomTest) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		gson.toJson(table);
	}
	
	public void comparaison(String nomTest, int numQuestion) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null;
		try (FileReader reader = new FileReader(nomTest)) {
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<ArrayList<Object>> buffer = reponses.get(numQuestion-1);
		System.out.println(buffer);
		System.out.println(table);
		if (buffer.equals(table)) {
			System.out.println("Bonne réponse");
		} else {
			System.out.println("Mauvaise réponse");
		}
	}
	
}
