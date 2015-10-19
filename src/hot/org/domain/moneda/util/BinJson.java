package org.domain.moneda.util;

public class BinJson
{
	private String bin;
	private String brand;
	private String country_code;
	private String country_name;
	private String bank;
	private String card_type;
	private String card_category;
	private String query_time;
	 
	
	public BinJson(){}
	
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_category() {
		return card_category;
	}
	public void setCard_category(String card_category) {
		this.card_category = card_category;
	}
	public String getQuery_time() {
		return query_time;
	}
	public void setQuery_time(String query_time) {
		this.query_time = query_time;
	}
	
	public String toString()
	{
		//me permite ver el Json
		return 
		String.format("Bin Consultado | Servicio BinLis.net\n" +
				"Bin:\t\t\t%s\n" +
				"Franquicia:\t\t%s\n" +
				"BancoEmisor:\t\t%s\n" +
				"Pais:\t\t\t%s\n" +
				"TipoTarjeta:\t\t%s\n" +
				"CategoriaTarjeta:\t%s\n",
				this.getBin(), this.getBrand(), this.getBank(), this.getCountry_name().toUpperCase(),
				this.getCard_type(), this.getCard_category());
	}

}
