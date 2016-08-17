package lfkimura;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides use of jersey framework
 * @author Luis Kimura
 *
 */
@Configuration
@ApplicationPath(value = "app")
public class JerseyConfig extends ResourceConfig {

	public static final String RESOURCES = "lfkimura.routes.rest";

	public JerseyConfig() {
		packages(RESOURCES);
	}

}
