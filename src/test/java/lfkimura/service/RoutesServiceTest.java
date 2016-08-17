package lfkimura.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import lfkimura.model.Route;
import lfkimura.model.Trail;
import lfkimura.repository.RouteRepository;
import lfkimura.routes.service.RoutesService;

@RunWith(MockitoJUnitRunner.class)
public class RoutesServiceTest {

	RoutesService service;

	RouteRepository repository;

	String map = "kimura";

	String origin = "A";

	String destination = "D";
	ArrayList<Trail> list = new ArrayList<>();

	HashMap<String, Trail> table = new HashMap<String, Trail>();

	Trail ab = new Trail(map, "A", "B", 10);

	Trail ac = new Trail(map, "A", "C", 5);
	Trail cd = new Trail(map, "C", "D", 5);
	Trail bd = new Trail(map, "B", "D", 10);
	Trail de = new Trail(map, "D", "E", 10);

	@Before
	public void init() {

		repository = Mockito.mock(RouteRepository.class);

		list.add(new Trail(map, "A", "B", 10));
		list.add(new Trail(map, "A", "C", 5));
		list.add(new Trail(map, "C", "D", 5));
		list.add(new Trail(map, "B", "D", 10));

		list.add(de);
		for (Trail t : list)
			table.put(t.getKey(), t);

		service = new RoutesService(repository, table, true);
	}

	@Test
	public void shouldCalculateBestRoute() {
		
		Route route = service.getBestRoute(map, origin, "E");
		Assert.isTrue(route.getCost(10, 1) == 2);
		Assert.isTrue(route.getRouteDistance() == 20);

	}

	@Test
	public void shouldCalculateBestRouteDB() {

		RoutesService servicedb = new RoutesService(repository, table, false);

		ArrayList<Trail> lista = new ArrayList<>();
		lista.add(ab);
		lista.add(ac);
		ArrayList<Trail> listb = new ArrayList<>();
		listb.add(bd);
		ArrayList<Trail> listc = new ArrayList<>();
		listc.add(cd);
		ArrayList<Trail> listd = new ArrayList<>();
		listd.add(de);
		Mockito.when(repository.findByMapAndOriginAllIgnoringCase(map, "A")).thenReturn(lista);
		Mockito.when(repository.findByMapAndOriginAllIgnoringCase(map, "C")).thenReturn(listc);
		Mockito.when(repository.findByMapAndOriginAllIgnoringCase(map, "B")).thenReturn(listb);
		Mockito.when(repository.findByMapAndOriginAllIgnoringCase(map, "D")).thenReturn(listd);

		Route route = servicedb.getBestRoute(map, origin, "E");
		Assert.isTrue(route.getCost(10, 1) == 2);
		Assert.isTrue(route.getRouteDistance() == 20);

	}

	@Test
	public void shouldCreateMap() {
		service.createMap(map, list);
		Mockito.verify(repository, Mockito.times(5)).save(Mockito.any(Trail.class));

	}

}
