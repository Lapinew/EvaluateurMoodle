package evaluateur;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

public class InterfaceAdaptater implements JsonSerializer<Reponse>, JsonDeserializer<Reponse> {

	private static final String CLASSNAME = "CLASSNAME";
	private static final String DATA = "DATA";
	
	@Override
	public Reponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
		Reponse buffer = null;
		JsonObject jsonObject = jsonElement.getAsJsonObject();
	    String className = jsonObject.get(CLASSNAME).getAsString();
	    try {
	    	String fullName = type.getTypeName();
	    	String packageText = fullName.substring(0, fullName.lastIndexOf(".") + 1);
	    	Class klass = Class.forName(packageText + className);
	    	buffer = context.deserialize(jsonObject.get(DATA), klass);
	    } catch (ClassNotFoundException e) {
	    	System.out.println("Erreur deserialization");
	    } catch (JsonParseException e) {
	    	System.out.println("Erreur deserialization 2");
	    }
	    return buffer;
	}

	@Override
	public JsonElement serialize(Reponse jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
		System.out.println("serializer en cours d'execution");
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(CLASSNAME, new JsonPrimitive(jsonElement.getClass().getSimpleName()));
		jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
		return jsonObject;
	}
}
