package org.domain.moneda.bussiness;

import java.lang.reflect.Type;

import org.domain.moneda.util.BinJson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class BinJsonDeserializer implements JsonDeserializer<BinJson>  {

	public BinJson deserialize(JsonElement json , Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		
		// Recibe el JSon BinList.net: http://www.binlist.net/json/{bin}
		// lo asigna a un objeto JsonObject este sera nuestro objeto
		// Json principal
		JsonObject jsonDToday = json.getAsJsonObject();
		
		
		// obtengo el miembro tipo objeto JSon  _timestamp que contiene el epoch
		// y lo asigno a una varibalbe JsonObject al igual que los demas
		// miembros tipo objetos del Json principal.
		// Los nombres de las comillas es como aparecen textualmente en el Json.
		JsonObject jsonEpoch = jsonDToday.getAsJsonObject("_timestamp");
		

		// del objeto Json "_timestamp" obtengo la variable epoch
		// se multiplica por mil para quedar en milisegundo 
		// y se asigna a una variable primitiva java
		JsonElement jEpoch = jsonEpoch.get("epoch");
		long epoch = jEpoch.getAsLong() * 1000;
		
		return null;
	}

}
