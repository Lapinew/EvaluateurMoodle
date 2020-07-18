package evaluateur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import com.google.gson.*;

abstract class Table { //Cette classe permet de convertir un ResultSet en un tableau 2D/3D exploitable
	
	//CallableStatement : Appel procédure/fonction
	//PreparedStatement : adapté pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
	//Statement : adapté pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE

	//Attributs
	protected ArrayList<ArrayList<Object>> table;
	
	//Methodes
	public ArrayList<ArrayList<Object>> getTable () {
		return table;
	}
	
	public void afficheTable() {
		System.out.println(table.toString());
	}
	
	private void aLaSuite(String nomTest) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //On récupère les données
			reponses.add(table); //On ajoute la table qu'on a crée aux données récupérées
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
		try (FileWriter file = new FileWriter(nomTest)) { //On remplace le contenu du json par le nouveau arrayList
			gson.toJson(reponses, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void premiereTable(String nomTest) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> buffer = new ArrayList<ArrayList<ArrayList<Object>>>(); //On crée l'arrayList qui va contenir les réponses
		buffer.add(table); //On met la premiere table dans l'array
		try (FileWriter file = new FileWriter(nomTest)) { //On met l'array dans le json
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
	
	public void comparaison(String nomTest, int numQuestion) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null; //On prepare l'arrayList pour récupérer les données dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //Récupère l'array du json
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
		ArrayList<ArrayList<Object>> buffer = reponses.get(numQuestion-1); //On récupère la réponse correspondant au numero de la question à laquelle on est
		System.out.println(buffer); //Affiche la réponse du prof
		System.out.println(table); //Affiche la réponse de l'élève
		if (buffer.equals(table)) {
			System.out.println("Bonne réponse");
		} else {
			System.out.println("Mauvaise réponse");
		}
	}
	
}
