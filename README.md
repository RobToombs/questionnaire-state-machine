The purpose of this project is to create an end-to-end questionnaire state machine. This state machine represents the fundamental principle behind our questionnaire engine in that when provided any state of a questionnaire, it will return the next valid questionnaire state. 

-------------------------------------------------------------------------------------------------------------------
You will need Node, Gradle, and Java installed on your machine to get this working.

Windows:\
Run "npm run start-w", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-w" or any IDE that supports SpringBoot applications.  

Mac:\
Run "npm run start-m", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-m" or any IDE that supports SpringBoot applications.  

Once Tomcat and the Webpack Dev Server are up and running, go to http://localhost:9000/ to check it out.

-------------------------------------------------------------------------------------------------------------------
STEP 1:
Get the application working!

1. git clone https://github.com/RobToombs/questionnaire-state-machine.git
2. cd questionnaire-state-machine
3. npm install
4. cd frontend
5. npm install
6. cd ..
7. npm run start-m (if you're on a windows machine: npm run start-w)
8. go to: http://localhost:9000/

If it takes more than this, then I apologize.