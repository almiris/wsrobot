package fr.almiris.open.wsrobot;

import fr.almiris.open.wsrobot.conf.RobotScenarioConf;
import fr.almiris.open.wsrobot.conf.RobotStepConf;
import fr.almiris.open.wsrobot.conf.RobotSuiteConf;
import fr.almiris.open.wsrobot.report.RobotScenarioReport;
import fr.almiris.open.wsrobot.report.RobotStepReport;

public class RobotScenario {

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
	
	public RobotScenarioReport run(RobotSuiteConf suiteConf, RobotScenarioConf scenarioConf, RobotSuite suite) {
		int successCount = 0;
		int errorCount = 0;
		long start = System.currentTimeMillis();
		RobotScenarioReport report = new RobotScenarioReport();
		report.setConf(scenarioConf);
		suite.getLogger().debug("=====");
		suite.getLogger().debug("Executing scenario : " + scenarioConf.getName());
		try {
			if (scenarioConf.getSteps() != null) {
				for (RobotStepConf stepConf : scenarioConf.getSteps()) {
					RobotStep step = new RobotStep();
					RobotStepReport stepReport = step.run(suiteConf, scenarioConf, stepConf, suite);
					report.addStepReport(stepReport);
					if (stepReport.getStatus() == RobotStep.Status.OK) {
						successCount++;
					}
					else {
						errorCount++;
					}
				}
			}
			report.setStatus(errorCount > 0 ? Status.ERROR : Status.OK);
			return report;
		}
		catch (Exception e) {
			report.setStatus(Status.EXCEPTION);
			report.setException(e.toString());
			suite.getLogger().error("Exception : " + e.toString());
			return report;
		}
		finally {
			report.setSuccessCount(successCount);
			report.setErrorCount(errorCount);
			report.setExecutionTime(System.currentTimeMillis() - start);
			suite.getLogger().debug("-----");
			suite.getLogger().debug((successCount + errorCount) + " step(s) executed in " + (System.currentTimeMillis() - start) + " ms");
			suite.getLogger().debug(errorCount == 0 ? "Success" : "Failed : " + errorCount + " error(s)");
			suite.getLogger().debug("=====");
		}		
	}
}
