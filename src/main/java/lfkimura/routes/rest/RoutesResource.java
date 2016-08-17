package lfkimura.routes.rest;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ManagedAsync;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lfkimura.model.Route;
import lfkimura.model.Trail;
import lfkimura.routes.service.RoutesService;

/**
 * REST API responsible for interface of routes handling
 * 
 * @author Luis Kimura
 *
 */
@Path("routes")
public class RoutesResource extends ResourceConfig {

	Logger logger = LoggerFactory.getLogger(RoutesResource.class);

	public RoutesResource() {
		packages("lfkimura.routes.rest");
	}

	// contructor with args
	public RoutesResource(RoutesService service) {
		this.service = service;
	}

	@Autowired
	RoutesService service;

	/**
	 * Um exemplo de entrada seria mapa SP, origem A, destino D, autonomia 10,
	 * valor do litro 2,50; a resposta seria a rota A B D com custo de 6,25.
	 * 
	 * @param asyncResponse
	 * @param map
	 * @param req
	 * @throws Exception
	 */
	@GET
	@Path("/best-route/{ map }")
	public Response getBestRoute(@PathParam("map") @NotNull String map, @QueryParam("origin") String origin,
			@QueryParam("destination") @NotNull String destination, @QueryParam("autonomy") @NotNull Double autonomy,
			@QueryParam("literPrice") @NotNull double literPrice) throws Exception {
		Route route = service.getBestRoute(map, origin, destination);
		if (route == null)
			return Response.status(Status.NOT_FOUND).entity("Route not found").build();
		double cost = route.getCost(autonomy, literPrice);

		return Response.ok("route " + route.toString() + "\n" + "cost(R$) " + cost + "\n").build();

	}

	/**
	 * interface that creates a logistic map with non blocking io
	 * 
	 * @param asyncResponse
	 * @param map
	 * @param req
	 * @throws Exception
	 */
	@POST
	@Path("/maps/{map}")
	@Produces(MediaType.TEXT_PLAIN)
	@ManagedAsync
	public void createMap(@Suspended final AsyncResponse asyncResponse, @PathParam("map") String map,
			@NotNull String req) throws Exception {
		asyncResponse.resume(parseRequest(map, req));

	}

	/**
	 * parse payload resquest to transform the format a b 10 to a trail
	 * 
	 * @param map
	 * @param req
	 * @return
	 */
	private String parseRequest(String map, String req) {

		ArrayList<Trail> trails = new ArrayList<Trail>();
		String[] lines = req.split("\n");
		for (String line : lines) {
			try {
				Trail trail = new Trail();
				String[] lineData = line.split(" {1,}");
				trail.setMap(map);
				trail.setOrigin(lineData[0]);
				trail.setDestination(lineData[1]);
				trail.setDistance(Integer.valueOf(lineData[2]));
				trails.add(trail);
			} catch (Exception e) {
				logger.error("invalid line " + line + " = " + e.getStackTrace());
			}
		}
		service.createMap(map, trails);
		return "Routes successfully processed!";
	}

	/**
	 * EXTRA WM TEST all trails that output from a given origin
	 * 
	 * @param map
	 * @param origin
	 * @return
	 */
	@GET()
	@Path("/all-trails-from/{ map }/{ origin }")
	public String geTrails(@PathParam("map") String map, @PathParam("origin") String origin) {
		return service.getTrails(map, origin);
	}
}
