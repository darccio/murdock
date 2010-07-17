package hm.murdock;

import hm.murdock.cli.Router;
import hm.murdock.exceptions.ActionException;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.exceptions.MultipleRoutingException;
import hm.murdock.exceptions.RoutingException;
import hm.murdock.modules.action.Action;
import hm.murdock.utils.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Murdock is an easy and ligtweight framework for command line apps.
 * 
 * CLI apps have been with us a long time and they deserve a new brand framework
 * in these GUI times. BTW, GUI are evil, stop.
 * 
 * It features:
 * <ul>
 * <li>REST-like command line interface (Play! framework inspired)</li>
 * <li>Modules support for enhanced functionality</li>
 * </ul>
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Murdock {

	public static final String VERSION = "0.0.1";

	public static final String NAME = Murdock.class.getSimpleName();

	private final Router router;

	private final Logger logger;

	public Murdock() throws ConfigurationException {
		Context context = new Context();
		this.router = new Router(context);

		this.logger = LoggerFactory.getLogger(Murdock.NAME);
	}

	/**
	 * Based in command-line arguments, delegates them to the corresponding
	 * module.
	 * 
	 * If multiple modules handle the same action name, it shows all
	 * possibilities.
	 * 
	 * @param arguments
	 */
	public void delegate(String... arguments) {
		try {
			this.router.route(arguments);
		} catch (MultipleRoutingException e) {
			logger.info(e.getLocalizedMessage());
			for (Action action : e.getAvailableActions().values()) {
				logger.info("\t" + action.toString());
			}
		} catch (RoutingException e) {
			logger.error(e.getLocalizedMessage());
		} catch (ActionException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public static void main(String... args) throws ConfigurationException {
		Murdock murdock = new Murdock();
		murdock.delegate(args);
	}
}
