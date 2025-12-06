# CSEP – Meeting Week 3 Minute

## General information

* **Date:** 05/12/2025

* **Time (planned):** 16:45 – 17:30

* **Time (actual):** 16:46 – 17:20

* **Location:** Drebbelweg, PC Hall 2

* **Chair:** Teammate 2

* **Minute Taker:** Teammate 1

* **Attendees:** Lukas, Teammate 4, Teammate 5, Teammate 2, Teammate 3, Teammate 1

* **Absent:** Nobody

---

## Opening + overview of last week (time est. ~6 minutes) – Actual duration: ~7 min

* Discussion summary:
    * What has everybody contributed?
        - Teammate 4: Nearly completed the issue on connecting server and client and implementing WebSockets.
        - Teammate 5: Struggled with technical errors and could not finish his issue; still working on understanding the project structure.
        - Teammate 1: Created a shopping list overview. Due to database changes, the implementation needs refactoring. Tried to do ShoppingListCtrl tests but encountered blocking errors.
        - Teammate 3: Added small UI components like up/down buttons for step ordering in the Ingredient Overview. Implemented recipe search and result sorting. Completed the issue but wrote no tests. Plans to add tests next week.
        - Lukas: Could not complete the favourite recipe feature due to database updates. Wrote several tests for the IngredientTypeController.
        - Teammate 2: Developed the ingredient overview scene and updated the recipe overview (former home screen).
    * Is everything that has been agreed on is executed?
        - The database was successfully restructured (added IngredientType and nutrition values).
        - Fake repositories are now used for server controller tests.

---          

## Announcements by the team (time est. ~1 minute) – Actual duration: ~0.5 min

* Discussion summary:
  - Everyone must submit BuddyCheck following AID principles.

---

## Announcements by the TA (time est. ~2 minutes) – Actual duration: ~2.5 min

* Discussion summary:
    * GitLab Downtime:
        - GitLab will be offline 10/12–17/12/2025.
        - Everyone must pull the main branch before Wednesday and work locally afterward.
    * Merge Request Reviews:
        - Reviews must be constructive, specific, and not vague.
    * BuddyCheck:
        - Everyone must submit BuddyCheck following AID principles.

---

## Presentation of the current app to TA (time est. ~4 minutes) – Actual duration: ~6 min

* Discussion summary:
  - Approximately 90% of basic requirements are implemented.
  - Most features function on both client and server.
  - Recipe search fields exist, though they did not work during the presentation.
  - The database is nearly fully functional.
  - TA suggested using the IntelliJ Database Tool plugin.
  - A suggestion was made to add a “search results counter” as an extra feature. The TA agreed that this would be a nice feature (if it works).
* Action points:
  - Fix the search feature and complete client–server synchronization before the next meeting.

---

## Talking points (time est. ~6 minutes) – Actual duration: ~19 min

* Discussion summary:
    * Calorie and unit conversion logic:
        - Calories for ingredients must be calculated. When a user creates an IngredientType, they must enter the density. So that for example we can find out how many grams are there in milliliters. With that we calculate calories for ingredient if its quantity is in milliliters. 
        - An issue for calorie calculation methods will be created next project week.
        - Helper methods will be created in UI utils.
    * Should nutrition values be filled in by the user in recipe overview if they are creating new ingredient type?
        - It should but as an option. 
        - If the user does not fill them in, it should show a warning that calories cannot be calculated.
    * Should creating an IngredientType in the recipe overview open a new scene?
        - Yes it should.
    * Additional ideas for extra features:
        - Dark mode/theme (not sure if it contributes to the mark).
        - American measurement system.
        - A settings dropdown menu.
    * Urgent tasks:
        - Finishing WebSockets for refresh between clients.
        - Add frontend tests.
    * Adding tablespoon and teaspoon as an units:
        - Teaspoon has 5 milliliters.
        - Tablespoon has 15 milliliters.

---

## Summarize action points: who, what, when? (time est. ~6 minutes) – Actual duration: ~2 min

* Discussion summary:
    - Most issues for next week are unfinished issues postponed from last week.
    - An issue for adding nutrition values will be created.
    - Teammate 4 will try to fix server–client communication.
    - Teammate 1 will try to test the frontend.  

---

## Feedback round: what went well and what can be improved next time? (time est. ~2 minutes) – Actual duration: ~2 min

* Discussion summary:
    - Tests should be written alongside the working code.
    - Teammate 5 needs to start writing code, but he has errors. However, he did not ask for help which he should do next time.
    - Teammate 2 and Teammate 5 should work on backend tasks in the future.

---

## Question round: does anyone have anything to add before the meeting closes? (time est. ~2 minutes) – Actual duration: ~0 min

* Discussion summary:
    - No additional comments.
    - Reminder: merge request comments in reviews only count if they exceed 100 characters.

## Sprint Week 6 – Issues (decided before the meeting, and forwarded from last week)

### Teammate 1
- Basic shopping list properties  
- Test shopping list tests  

### Lukas
- Add tests for IngredientType in commons 
- Unit testing for favouriting recipes 
 - Add ability to favourite recipes  
- Finalise database schema (nutrition, density, etc.)

### Teammate 3
- Make the per recipe kca calculation and convertion from ml to g (and then kca) and from tsp, etc. to ml - Recipe utils (server side) 

### Teammate 4
- Automated change synchronization  

### Teammate 2
- Unit testing for the JavaFX adding recipe scene
- Add nutritional values feature

### Teammate 5
- Settings menu