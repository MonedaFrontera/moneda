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
		
		BinJson binList = new BinJson();
		
		try {
			// Recibe el JSon BinList.net: http://www.binlist.net/json/{bin}
			// lo asigna a un objeto JsonObject este sera nuestro objeto
			// Json principal
			JsonObject jsonBinList = json.getAsJsonObject();
			
			
			final String bin = 	jsonBinList.get("bin").getAsString();
			final String brand = jsonBinList.get("brand").getAsString();
			final String country_code = jsonBinList.get("country_code").getAsString();
			final String country_name = jsonBinList.get("country_name").getAsString();
			JsonElement elementJson = jsonBinList.get("bank");
			final String bank = elementJson == null ? "" :  elementJson.getAsString();
			final String card_type = jsonBinList.get("card_type").getAsString();
			final String card_category = jsonBinList.get("card_category").getAsString();
			
			
			binList.setBin(bin);
			binList.setBrand(brand);
			binList.setCountry_code(country_code);
			binList.setCountry_name(country_name);
			binList.setBank(bank);
			binList.setCard_type(card_type);
			binList.setCard_category(card_category);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return binList;
	}

}
