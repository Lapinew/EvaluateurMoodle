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
	
	//CallableStatement : Appel proc�dure/fonction
	//PreparedStatement : adapt� pour SELECT, INSERT, UPDATE, DELETE (parametres changeables)
	//Statement : adapt� pour CREATE, DROP, ALTER, TRUNCATE et pour le reste si requete FIXE

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
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null; //On prepare l'arrayList pour r�cup�rer les donn�es dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //On r�cup�re les donn�es
			reponses.add(table); //On ajoute la table qu'on a cr�e aux donn�es r�cup�r�es
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
		ArrayList<ArrayList<ArrayList<Object>>> buffer = new ArrayList<ArrayList<ArrayList<Object>>>(); //On cr�e l'arrayList qui va contenir les r�ponses
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
			System.out.println("Le fichier contient deja un JSONArray, la table va etre ajout�e a la suite");
			this.aLaSuite(nomTest);
	    }
	}
	
	public void comparaison(String nomTest, int numQuestion) {
		Gson gson = new Gson();
		ArrayList<ArrayList<ArrayList<Object>>> reponses = null; //On prepare l'arrayList pour r�cup�rer les donn�es dans le json
		try (FileReader reader = new FileReader(nomTest)) { //On lit le json
			reponses = (ArrayList<ArrayList<ArrayList<Object>>>) gson.fromJson(reader, Class.forName("java.util.ArrayList")); //R�cup�re l'array du json
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
		ArrayList<ArrayList<Object>> buffer = reponses.get(numQuestion-1); //On r�cup�re la r�ponse correspondant au numero de la question � laquelle on est
		System.out.println(buffer); //Affiche la r�ponse du prof
		System.out.println(table); //Affiche la r�ponse de l'�l�ve
		if (buffer.equals(table)) {
			System.out.println("Bonne r�ponse");
		} else {
			System.out.println("Mauvaise r�ponse");
		}
	}
	
}
