You will need Node, Gradle, and Java installed on your machine to get this working.

Windows:
Working on getting SpringBoot working in Windows, running "npm run start-windows" will kick off the Webpack Dev Server and SHOULD start up Tomcat, but SpringBoot is not cooperating at the moment. Alternatively, starting up the SpringBoot application backend within IntelliJ/Eclipse, and kicking off the Webpack Dev Server using "npm run start:frontend" in the main directory works fine.

Mac:
Run "npm run start" in the main directory to start up the Webpack Dev Sever as well as Tomcat. You can start up the frontend/backend individually if desired with "npm run start:frontend" and "npm run start:backend", or kick off the SprinBoot backend in your favorite IDE to debug.

Once Tomcat and the Webpack Dev Server are up and running, go to http://localhost:9000/ to check it out.

