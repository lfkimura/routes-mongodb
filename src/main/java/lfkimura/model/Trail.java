package lfkimura.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;

/**
 * Trail class that map a direct conection from a origin to a destination in a
 * given distance
 * 
 * @author Luis Kimura
 *
 */
@CompoundIndex(name = "compound_index", def = "{'map': 1, 'origin': 1, 'destination': 1}")
public class Trail {

	public Trail() {
	}

	public Trail(String map, String origin, String destination, Integer distance) {
		super();
		this.map = map;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}

	@Id
	private String id;

	private String map;

	private String origin;
	private String destination;
	private Integer distance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getKey() {
		return map + origin + destination;
	}

	@Override
	public String toString() {
		return " [ map " + map + ", origin " + origin + ", destination " + destination + ", distance " + distance
				+ " ]\n";
	}

}
