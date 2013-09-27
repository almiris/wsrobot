package fr.almiris.open.wsrobot.test.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/tests")
public class EndPoint {

	@XmlRootElement
	public static class Result {
		String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	@GET @Path("/hello")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Result sayHello() {
		Result r = new Result();
		r.setMessage("hello");
		return r;
	}

}
