package lfkimura.model;

import java.util.ArrayList;
import java.util.List;

/**
 * class that maps a route from a given node to a target passing throught trails
 * 
 * @author Luis Kimura
 *
 */
public class Route {
	List<Trail> trails = new ArrayList<Trail>();

	public Route(ArrayList<Trail> trails) {
		this.trails = trails;
	}

	public double getCost(double autonomy, double literPrice) {
		return literPrice * (this.getRouteDistance() / autonomy);
	}

	public Integer getRouteDistance() {
		Integer total = 0;
		for (Trail trail : trails)
			total += trail.getDistance();
		return total;
	}

	@Override
	public String toString() {
		String response = "[ " + this.trails.get(0).getOrigin();
		for (Trail trail : this.trails) {
			response += " " + trail.getDestination();
		}
		response += " ]";
		return response;

	}

}
