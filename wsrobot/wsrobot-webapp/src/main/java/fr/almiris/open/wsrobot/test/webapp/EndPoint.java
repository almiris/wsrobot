package fr.almiris.open.wsrobot.test.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/tests")
public class EndPoint {

	@XmlRootElement
	public static class Result {
		String message;
		String from;
		
		public Result() {}
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}
	}
	
	@GET @Path("/sayHello")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Result sayHello(@QueryParam("to") String to) {
		Result r = new Result();
		r.setMessage("Hello " + (to == null ? "" : to) + "!");
		r.setFrom("server");
		return r;
	}

	@GET @Path("/thankYou/{to}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Result sayHello(@HeaderParam("From-Header") String from, @PathParam("to") String to) {
		Result r = new Result();
		r.setMessage("You're welcome " + (from == null ? "" : from) + "!");
		r.setFrom(to == null ? "server" : to);
		return r;
	}

}
