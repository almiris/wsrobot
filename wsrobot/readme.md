# WSRobot

WSRobot is a testing tool for REST web services that

- respond to GET, POST or DELETE request
- exchange mainly JSON content
 
## First steps

The first step is to create a test suite file *openweathermap.json* using JSON. This file describes the web services used and the test scenarios to be executed.

```JSON
{
	"name" : "Simple OpenWeatherMap test",

	"services" : {
		"openweathermap" : { 
			"name" : "Open Weather Map API", 
			"url" : "http://api.openweathermap.org/data/2.5/weather?q={0}", 
			"method" : "get" 
		}
	},

	"scenarios" : [{	
		"name" : "Montrouge Weather test",
		"active" : "true",
		"steps" : [{ 
			"description" : "Check that the service responds", 
			"service" : "openweathermap", 
			"params" : [ "Montrouge,fr" ], 
			"status" : 200, 
			"jcontrols" : { "$.coord.lon" : "2.31"} 
		}]
	}]
}
```

Then we execute the test using a *build.xml* ANT file :

```XML
<project name="robot example" default="run" basedir=".">
	<description>
		WSRobot testing an OpenWeatherMap service
	</description>
	<property name="wsrobot_jar" value="${basedir}/../wsrobot-jar/target/wsrobot.jar"/>
	<property name="openweathermap" value="${basedir}/openweathermap"/>
   	<target name="run">
	    <java classname="fr.almiris.open.wsrobot.RobotRunner" fork="true">
			<classpath>
	           	<pathelement location="${wsrobot_jar}"/>
	        </classpath>
          	<arg value="${openweathermap}.json"/>
	        <arg value="${openweathermap}.out.json"/>
	        <arg value="${openweathermap}.out.html"/>
		</java>
	</target>
</project>
```

The *main* class of WSRobot is **fr.almiris.open.wsrobot.RobotRunner** . The class expects three arguments :

1. the name of the test suite file
2. a name for a JSON report file : 
3. a name for a HTML report file : 

In our example, the files are named respectively *openweathermap.json*, *openweathermap.out.json* and *openweathermap.out.html*. 
Alternatively, we could have called the class directly using the command line :

```Shell
# java -classpath=wsrobot.jar fr.almiris.open.wsrobot.RobotRunner openweathermap.json openweathermap.out.json openweathermap.out.html
```

