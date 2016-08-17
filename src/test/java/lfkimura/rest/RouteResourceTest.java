package lfkimura.rest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import lfkimura.model.Route;
import lfkimura.model.Trail;
import lfkimura.routes.rest.RoutesResource;
import lfkimura.routes.service.RoutesService;

@RunWith(MockitoJUnitRunner.class)
public class RouteResourceTest {

	private RoutesService service = Mockito.mock(RoutesService.class);
	String map = "kimura";
	String origin = "A";
	String destination = "E";
	int distance = 10;
	RoutesResource resource;

	@Before
	public void init() {

		resource = new RoutesResource(service);
	}

	@Test
	public void shouldGetBestRoute() throws Exception {

		ArrayList<Trail> list = new ArrayList<>();
		list.add(new Trail(map, origin, destination, distance));
		Route route = new Route(list);

		when(service.getBestRoute(map, origin, destination)).thenReturn(route);

		Response resp = resource.getBestRoute("kimura", "A", "E", 10.0, 2.5);

		assert(resp != null);

		assert(resp.getEntity().toString().contains("A"));

		assert(resp.getEntity().toString().contains("E"));

	}

	@Test
	public void shouldCreateMap() throws Exception {

		AsyncResponse asyncResponse = Mockito.mock(AsyncResponse.class);
		String map = "kimura";
		String req = "A C 10";

		resource.createMap(asyncResponse, map, req);

		verify(asyncResponse, Mockito.times(1)).resume(Mockito.anyString());
		verify(service, Mockito.times(1)).createMap(anyString(), Mockito.anyListOf(Trail.class));
	}

}
