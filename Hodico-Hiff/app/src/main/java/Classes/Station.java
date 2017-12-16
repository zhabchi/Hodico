package Classes;

import com.google.android.gms.maps.model.LatLng;

public class Station {

	private int id;
	private String name;
	private String address;
	private String description;
	private LatLng position;
	private int type;

	public Station() {

	}

	public Station(int id, String name, String address, String description,
			LatLng position, int type) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.position = position;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public void setPosition(double latitude, double longitude) {
		this.position = new LatLng(latitude, longitude);
	}

}
