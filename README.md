The purpose of this project is to create an end-to-end application using SpringBoot and Elm. The application is a questionnaire state machine, where provided any state of valid questionnaire, will return the next iteration of the questionnaire. It is extremely basic in that there are only two answerable questions of RADIO and PROMPT types, but is very extensible in its current state.

-------------------------------------------------------------------------------------------------------------------
You will need Node, Gradle, and Java installed on your machine to get this working.

Windows:\
Run "npm run start-w", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-w" or any IDE that supports SpringBoot applications.  

Mac:\
Run "npm run start-m", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-m" or any IDE that supports SpringBoot applications.  

Once Tomcat and the Webpack Dev Server are up and running, go to http://localhost:9000/ to check it out.

-------------------------------------------------------------------------------------------------------------------
STEP 1:
