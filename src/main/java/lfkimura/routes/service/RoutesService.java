package lfkimura.routes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lfkimura.model.Route;
import lfkimura.model.Trail;
import lfkimura.repository.RouteRepository;
import lfkimura.routes.rest.RoutesResource;

/**
 * class responsible to handler services of routes
 * 
 * @author Luis Kimura
 *
 */
@Service
public class RoutesService {
	@Autowired
	RouteRepository repository;

	// memory
	HashMap<String, Trail> table = new HashMap<String, Trail>();

	// logger slf4
	Logger logger = LoggerFactory.getLogger(RoutesResource.class);

	// control variable of memory loaded
	// ultra optmized performance
	private boolean dbLoadedToMemory = false;
	
	public RoutesService(){}
	
	public RoutesService(RouteRepository repository, HashMap<String, Trail> table, boolean dbLoadedToMemory) {
		this.repository = repository;
		this.table = table;
		this.dbLoadedToMemory = dbLoadedToMemory;
	}

	// load DB data
	@PostConstruct
	public void init() {
		List<Trail> list = repository.findAll();
		for (Trail init : list)
			table.put(init.getKey(), init);
		dbLoadedToMemory = true;

	}

	// map creation
	public void createMap(String map, List<Trail> trails) {
		logger.info("Service Layer entered {} routes ", trails.size());
		for (Trail trail : trails) {
			table.put(trail.getKey(), trail);
			repository.save(trail);
		}
	}

	// get memory database instead of making IO disk operations
	public Route getBestRoute(String map, String origin, String destination) {
		if (dbLoadedToMemory)
			return getBestRouteInMemory(map, origin, destination);
		else
			return getBestRouteDB(map, origin, destination);

	}

	// create mongo DB
	private Route getBestRouteDB(String map, String origin, String destination) {
		List<Trail> trails = repository.findByMapAndOriginAllIgnoringCase(map, origin);

		// get map
		List<Route> list = new ArrayList<>();

		for (Trail trail : trails)
			list.addAll(getRouteToTarget(map, new ArrayList<Trail>(), trail, destination));

		return calculateBestRoute(list);
	}

	// calculate the best route found
	private Route calculateBestRoute(List<Route> list) {
		Integer distance = null;
		Integer minDistance = null;
		Route best = null;
		for (Route route : list) {
			distance = route.getRouteDistance();
			if (minDistance == null || (distance != 0 && distance < minDistance)) {
				minDistance = distance;
				best = route;
			}
		}
		return best;
	}

	// calculate route to target with database
	private List<Route> getRouteToTarget(String map, ArrayList<Trail> passed, Trail currentTrail, String destination) {
		ArrayList<Trail> trailRoute = new ArrayList<>(passed);
		trailRoute.add(currentTrail);
		List<Route> list = new ArrayList<>();
		if (currentTrail.getDestination().equals(destination)) {
			list.add(new Route(trailRoute));
		} else {

			List<Trail> trails = repository.findByMapAndOriginAllIgnoringCase(map, currentTrail.getDestination());
			if (trails != null && !trails.isEmpty()) {
				for (Trail cTrail : trails) {
					list.addAll(getRouteToTarget(map, trailRoute, cTrail, destination));
				}
			}

		}
		return list;

	}

	// calls a recusive method to find target
	private Route getBestRouteInMemory(String map, String origin, String destination) {

		// get map
		List<Route> list = new ArrayList<>();
		for (String key : table.keySet())
			if (key.matches(map + origin + ".*"))
				list.addAll(getRouteToTargetInMemory(map, new ArrayList<Trail>(), table.get(key), destination));

		return calculateBestRoute(list);
	}

	// Memory Search
	private List<Route> getRouteToTargetInMemory(String map, ArrayList<Trail> passed, Trail currentTrail,
			String destination) {
		ArrayList<Trail> trailRoute = new ArrayList<>(passed);
		trailRoute.add(currentTrail);
		List<Route> list = new ArrayList<>();
		if (currentTrail.getDestination().equals(destination)) {
			list.add(new Route(trailRoute));
		} else {
			for (String key : table.keySet())
				if (key.matches(map + currentTrail.getDestination() + ".*"))
					list.addAll(getRouteToTargetInMemory(map, trailRoute, table.get(key), destination));
		}
		return list;

	}

	/**
	 * find trails from a given origin
	 * 
	 * @param map
	 * @param origin
	 * @return
	 */

	public String getTrails(String map, String origin) {
		List<Trail> list = repository.findByMapAndOriginAllIgnoringCase(map, origin);
		String response = "";
		for (Trail trail : list)
			response += trail + " ";
		return response;
	}

}
