package fr.almiris.open.wsrobot.util;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;

public class TemplateTool {

	private String TEMPLATES_ROOT = "fr/almiris/open/wsrobot/template/";
	
	public TemplateTool() {
	}
	
	private MustacheFactory getTemplateFactory() {
		return new DefaultMustacheFactory(TEMPLATES_ROOT);
	}
	
	public void process(String templateFilename, Object scope, Writer w) throws ProcessingException {
		try {
			Mustache mustache = getTemplateFactory().compile(templateFilename);
			mustache.execute(w, scope).flush();
		}
		catch (Exception e) {
			throw new ProcessingException(e);
		}
	}

	public class ProcessingException extends Exception {		
		private static final long serialVersionUID = 3040584971319800662L;

		public ProcessingException() {
		    super();
		  }

		  public ProcessingException(String message) {
		    super(message);
		  }

		  public ProcessingException(String message, Throwable cause) {
		    super(message, cause);
		  }

		  public ProcessingException(Throwable cause) {
		    super(cause);
		  }
	}
	
}
