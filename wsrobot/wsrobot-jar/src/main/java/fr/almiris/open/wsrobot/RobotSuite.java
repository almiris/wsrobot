package fr.almiris.open.wsrobot;

import java.util.Date;

import fr.almiris.open.wsrobot.conf.RobotScenarioConf;
import fr.almiris.open.wsrobot.conf.RobotSuiteConf;
import fr.almiris.open.wsrobot.log.DefaultLogger;
import fr.almiris.open.wsrobot.log.Logger;
import fr.almiris.open.wsrobot.report.RobotScenarioReport;
import fr.almiris.open.wsrobot.report.RobotSuiteReport;

public class RobotSuite {

	public enum Status {
		OK(0,"ok"),
		ERROR(1,"error"),
		EXCEPTION(2,"exception");
		
		private final int code;
		private final String message;

		Status(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}

	private Logger logger = new DefaultLogger();
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public RobotSuite() {
	}
		
	public RobotSuiteReport run(RobotSuiteConf suiteConf) {
		if (suiteConf == null) {
			return null;
		}
		
		int successCount = 0;
		int errorCount = 0;
		long start = System.currentTimeMillis();
		RobotSuiteReport report = new RobotSuiteReport();
		report.setConf(suiteConf);
		try {
			if (suiteConf.getScenarios() != null) {
				for (RobotScenarioConf scenarioConf : suiteConf.getScenarios()) {
					if (scenarioConf.isActive()) {
						RobotScenario scenario = new RobotScenario();
						RobotScenarioReport scenarioReport = scenario.run(suiteConf, scenarioConf, this);
						report.addScenarioReport(scenarioReport);
						if (scenarioReport.getStatus() == RobotScenario.Status.OK) {
							successCount++;
						}
						else {
							errorCount++;
						}
					}
				}
			}
			report.setStatus(errorCount > 0 ? Status.ERROR : Status.OK);
			return report;
		}
		catch (Exception e) {
			report.setStatus(Status.EXCEPTION);
			report.setException(e.toString());
			getLogger().error("Exception : " + e.toString());
			return report;
		}
		finally {
			report.setEndTime(new Date().getTime());
			report.setSuccessCount(successCount);
			report.setErrorCount(errorCount);
			report.setExecutionTime(System.currentTimeMillis() - start);
			getLogger().debug((successCount + errorCount) + " scenario(s) executed in " + (System.currentTimeMillis() - start) + " ms");
			getLogger().debug(errorCount == 0 ? "Success" : "Failed : " + errorCount + " error(s)");
		}
	}
	
}
