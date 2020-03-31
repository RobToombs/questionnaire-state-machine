The purpose of this project is to create an end-to-end questionnaire state machine. This state machine represents the fundamental principle behind our questionnaire engine in that when provided any state of a questionnaire, it will return the next valid questionnaire state. 

-------------------------------------------------------------------------------------------------------------------
You will need Node, Gradle, and Java installed on your machine to get this working.

Windows:\
Run "npm run start-w", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-w" or any IDE that supports SpringBoot applications.  

Mac:\
Run "npm run start-m", to run the frontend/backend independently start up the webpack devServer with "npm run start:frontend" and tomcat with "npm run start:backend-m" or any IDE that supports SpringBoot applications.  

Once Tomcat and the Webpack Dev Server are up and running, go to http://localhost:9000/ to check it out.

-------------------------------------------------------------------------------------------------------------------
STEP 1: Get the application working!

1. git clone https://github.com/RobToombs/questionnaire-state-machine.git
2. cd questionnaire-state-machine
3. npm install
4. cd frontend
5. npm install
6. cd ..
7. npm run start-m (if you're on a windows machine: npm run start-w)
8. go to: http://localhost:9000/

If it takes more than this, then I apologize.

\
STEP 2: JSON Template Format
The questionnaire JSON template is separated into two parts:
1. Sections - Sections are all located (not unsurprisingly!) in the "sections" list parameter. They all must be identified with a negative id and will appear on screen in the same order as they are listed in the template file from top to bottom.
 
    **Try moving the "Final Section" before the "Question Demo" section!** 
    
2. Questions - Questions are located within the "questions" list parameter. They are all identified by a positive decimal id and the order does not matter as long as question leaf nodes are appear after their respective internal and root node questions. The reasoning behind this is because the template reader creates the questionnaire blueprint from the leaves to root and must know about the children before assigning them to their parent questions. 
    
    Questions are added to sections by adding the question id to the section's "questions" parameter. The questions will be listed in this order within the section.
    
    In TRx2.0 questions have a plethora of possible parameters and types, but in this application they can only be "Prompts" or "Radio" buttons. TRx2.0 also contains the concept of medication based questions which are dynamically created under the "root" question filling out the question id decimal point. For example, if question 2.0 was designated a medication based question, the med based questions would appear as 2.1, 2.2, 2.3, etc. 
    
   **Try adding an additional question to the "Final Section"!** 
   
3. Responses - Responses are located within a question's "responses" list parameter. They appear in the order that they are listed and have an integer id. The "children" parameter contains the ids of questions to show when that response is selected much like the section's "questions" parameter.

    In TRx2.0, response options can reference both section ids and question id with varying differences, hence the need to distinguish between sections and question ids. This application does not currently support this.
    
    **Try adding an additional response to question id = 6.0!**
    
\
STEP 3: Questionnaire as a state machine

In this demo, our backend acts solely as a state machine and the questionnaire state is retained in browser memory. The current state is represented by the JSON block on screen.

In TRx2.0, our current questionnaire state is determined after each user action and stored within the questionnaire_json database columns. This is also why new questionnaire changes are not picked up until a user makes an action on a card.

To help reinforce this idea:
    
    1. Start up the frontend and backend independently
    2. Make some selection in the questionnaire
    3. Stop the backend, modify the questionnaire JSON, and restart the backend 
    4. Make a new questionnaire selection
    5. Observe that the questionnaire state now contains the modification from #3
    
You can also open up the Elm debugger (the little blue box in the bottom right corner of your browser) and click through the "ReceivedQuestionnaire" messages to view the questionnaire in various states.

\
STEP 4: Adding new question types

This demo questionnaire only has two question types, 'PROMPT' and 'RADIO', but adding new question types is simple:
 
    1. Define the new question type in QuestionType.java (FieldType.java in TRx2.0)
    2. Update the HttpActions.elm encoder/decoder to parse the new type correctly
    3. Update the View.elm to render the new question appropriately
    4. Update the Update.elm to handle response selections appropriately. Radio buttons only allow a single selection per question, Checkboxes allow multiple selections, and Prompts have nothing to select! How should the new question type behave onBlur? What about onInput?
    
   **Try implementing a Checkbox or Input box question!**