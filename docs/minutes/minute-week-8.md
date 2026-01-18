|  |  |
| --- | --- |
| Date | 16/01/2026 |
| Estimated Time | 16:45 - 17:30 |
| Actual Time | 16:46 - 17:26 |
| Location | Drebbelweg PC Hall 2 |
| Chair | Teammate 1 |
| Minute Taker | Teammate 2 |
| Attendees: | Teammate 2, Teammate 1, Teammate 4, Lukas Brookfield, Teammate 5, Teammate 3 |

---

### 1) Opening - Overview of the last week (inform) - (time est. 4 min, actual duration: 2 min)

*Was everyone able to finish their tasks? If not, why not?*

- **Teammate 4**: added client-side input validation + warnings. These have not yet been added to the 'Shopping list' and 'Add to shopping list' scenes. Also implemented WebSockets for the ingredient overview (not merged yet).
- **Teammate 2**: split some of the server-side logic into @Service classes and added tests for these classes right after. Also improved the UI a bit. Did not yet implement the search filter in the ingredient overview.
- **Lukas**: improved the way ingredient types are selected: the user can now create new ingredient types and search for already existing types at the same place (not merged yet). Also added tests for the client.
- **Teammate 5**: made it possible to use Enter and Escape to navigate through the app (not merged yet).
- **Teammate 3**: implemented the live language feature.
- **Teammate 1**: added a download, print and reset button to the shopping list.

### 2) Announcements by the TA (inform) - (time est. 2 min, actual duration: 1 min)

- The feedback for the Implemented Features was released. For the upcoming (and last) week we have to focus on adding the extra features.
- Before the deadline, we need to make sure that the database persists between restarts of the server. This can be changed in the application.properties file.
- Before the deadline, we need to have a correct README file. We need to explain how to run our project and for each feature state why it should get an excellent.

### 3) Demo of our app (inform) - (est. 4 min, actual duration: 5 min)

*Show our current app from the main branch. Highlight progress compared to last week.*

- Our main (visual) differences compared to last week were the live language feature and the print/download/reset buttons at the shopping list.
- **Important**: the user needs to able to create new ingredient types in the same window. We have this right now.
- It was suggested that we ask about the 'Favorite recipe removed' feature on Mattermost.

### 4) Talking points (inform + decide) - (est. 17 min, actual duration: 24 min)

#### A) Are features implemented? (est. 3 min, actual duration 1 min)

*We need to check once again with the backlog if features are implemented correctly.*

- **Automated Change Synchronization**: completed.
- **Nutritional Value**: completed. (however there are still a few issues with the Ingredient class' toString() method and the strings in the unit choice box)
- **Searching for Recipes**: completed.
- **Shopping List**: completed.
- **Live Language Switch**: we are still missing the language filter for recipes. Also, some translations are missing.

#### B) Extra touch” for features (inform + decide) - (est. 7 min, actual duration: 4 min)

- **Automated Change Synchronization**: Teammate 4 will work on automatic synchronization for the ingredient overview.
- **Nutritional Value**: Lukas will implement Nutri-Scores (A-E scores based on the amount of protein, carbs and fat).
- **Searching for Recipes**: completed. Teammate 2 will still add a search filter to the ingredient overview.
- **Shopping List**: add categories to ingredient types (fruit, meat, drinks etc.) to make it easier to browse the supermarket.
- **Live Language Switch**: Teammate 3 suggested three features:
    - Have an option to use the system default language.
    - Add a shortcut to switch between the languages with Ctrl + L.
    - Double tap the language container to go to a different language.

        Lukas suggested the following:
    - Give users the ability to add translations. Maybe only for recipe names.

#### C) Bug triage (inform + decide) - (est. 5 min, actual duration: 10 min)

*Go over each bug noted by the team.*

- *Deleting an ingredient type doesn’t delete all referring ingredients in the shopping list.*

    Not fixed yet, but shouldn't be too difficult. Teammate 2 will fix it.

- *When you select a language and then switch scenes, the choicebox doesn’t get updated.*
    
    Teammate 3 will try to fix. Since it might take a lot of time, Teammate 2 will do the language filter for recipes.

- Some buttons/labels/tooltips are missing translations (e.g. nextEditIngredientButton in recipe overview).*

    We agreed that each of us will try to find missing translations, and add them if they are not present.

- *We might have to think of a different way to select ingredient types in the recipe overview. With lots of ingredient types in the database, a drop-down menu isn’t ideal. Maybe we should switch to the ingredient overview for this or a similar approach.*

    Lukas implemented a new drop-down menu (with search feature) which solves this issue. It's completed but not yet merged at the time of the meeting.

Other bugs/improvements that were raised during the meeting:

- Lukas noted that we should change units to strings. Teammate 3 said that we should also add language to units.
- Teammate 5 said that someone had changed the fx:id of a button, which broke some of his code. Because of this we should be careful when changing fx:id's.
- At the moment the 'Handful' and 'Pinch' units are not being scaled. Teammate 3 will fix this.

#### D) Questions for the TA (est. 2 min, actual duration: 9 min)

- *Can we use service classes on the client? (no @Service annotation, just the concept)*

    In the Technology rubric they state that we should separate business logic from UI code, so it's preferred. The name of the class doesn't matter.

- *Should we refactor? (split logic into seperate classes)*

    The TA was fine with how it looked already. Refactoring will not affect code contribution, only the 'Business logic separate from UI code' in the Technology rubric. It's not our priority, but Teammate 4 will do some refactoring on tuesday. We could help him by explaining how our code can be separated.

---

### 5) Summarize action points: who, what, when? (inform) - (est. 2 min, actual duration: 6 min)

- **Teammate 4**: refactor code, add more tests, fix remaining bugs.
- **Teammate 2**: add search filter in ingredient overview, add language filter for recipes.
- **Lukas**: add Nutri-Scores, translate units.
- **Teammate 5**: finish his current MR, add more tests, maybe help Teammate 4 with refactoring.
- **Teammate 3**: add ability to search recipes and ingredients by type, make 'Handful' and 'Pinch' units scalable.
- **Teammate 1**: sorting the shopping list by the types (fruit, meat, drinks, etc.) of ingredients.

We assigned a person to each feature to check if it's completed: (not the person who did the most work on the feature)

- **Basic Requirements**: Teammate 4
- **Automated Change Synchronization**: Teammate 2
- **Nutritional Value**: Teammate 3
- **Searching for Recipes**: Teammate 5
- **Shopping List**: Lukas
- **Live Language Switch**: Teammate 1

### 6) Question round: does anyone have anything to add before the meeting closes? (inform + decide) - (time est. 2 min, actual duration: 1 min)

No questions were asked.

### 7) Closure (est. 1 min, actual duration: 1 min)

Some things were discussed after the meeting had officialy ended:

- Next week is the last meeting. Everyone has been chair and minute-taker already. We can use this meeting to ask the TA to unofficialy grade our implemented features.
- *Question: how will we write the README file?* We agreed that we would arrange this on Discord.
