|              |                                                                                                                     |
| ------------ | ------------------------------------------------------------------------------------------------------------------- |
| Date         | 09/01/2026                                                                                                          |
| Time         | 16:45 - 17:30                                                                                                       |
| Location     | Flux Hall D                                                                                                         |
| Chair        | Teammate 3                                                                                                  |
| Minute Taker | Teammate 5                                                                                                       |
| Attendees:   | Teammate 2, Teammate 1 (online), Teammate 4, Lukas Brookfield, Teammate 5, Teammate 3 |

## Agenda Items:

### 0) Quick start (1 min) - (inform)

- **Goal for today:** recenter as we come back from the holidays (reminding of the feedback we were given) + make a plan for the last 2 weeks of coding
  
  - *Note: This meeting was after the holidays and the week itself was irregular. It focused more on planning the issues and tasks to complete for the last two weeks, as well as remind ourselves of the feedback given by grading.*
    - *Attendance: every attendee was present (including Wojchek through Discord).*
      - *Time spent on this item: ~1 minute.*

### 1) Overview of last week (4 min) - (inform)

- Discuss one by one the contributions from last week. Was everyone able to finish their tasks? If not, why not?

    List from GitLab for reference:

    - Teammate 2: DB + client-server connection (done per GitLab issue) and IngredientTypeOverview tests (open)
      - Teammate 2 created a warning system in case a user wanted to delete a recipe, warning them that their ingredients will also be removed for that recipe. This task is finished
        
    - Lukas: RecipeOverview tests (done per GitLab issue)
      - Lukas created tests using TestFX for RecipeOverview and made an MR. This task is complete and the issue is done.
        
    - Teammate 4: Client-side input validation + warnings (open)
      - Wojchek was responsible for creating a different warning system in case input in certain text fields was not of the data type that is expected. He is currently not done with this assignment (as far as I could tell from the meeting).
        
    - Teammate 5: Interactive language dropdown (open)
      - I created a button behaviour system that turned the attributes DefaultButton and CancelButton on and off depending on which field was being editted. As of now, some of the fields have this behaviour, while others do not. The fields that contain this behaviour allow the use of Enter and Esc to bind to the Done/Next and Cancel/Back keys respectively.
        
    - Teammate 1: Add ingredients from recipe to Shopping List (in progress)
      - Wojchek was responsible for adding the functionality of adding ingredients from the shopping list directly to a recipe. This task is not finished and currently contains bugs, but will be finished next week.
        
    - Teammate 3: Add scaling feature (in progress) + Replace exceptions with alerts (open)
      - As of writing these fleshed out notes, I reviewed Teammate 3's MR containing this feature and is ready to be merged. This feature allows for the user to scale the amount of an ingredient as they wish, complete with rounding functionality. This task is complete, but replacing exceptions with alerts is not done yet. Next week will offer the opportunity to finish this task as well.
        
    
    Extra notes: Apparently, our program used two types of warnings for the same features. It has been decided to use only one of these warning types by vote.
    
    - *Time spent on this item: ~5 minutes, slightly more than expected.*

### 2) Announcements by the TA (2 min) - (inform)

*Announcements: Program should be able to be ran on any device, basic requirements are important as well: missing requirement = 1 less point.*

*Compensation is impossible for major issues.*

*Bugs in extra features: not too bad.*
*Bugs in basic features: problematic, fix as soon as possible.*

*TA will either grade based off of today's main branch or week 8's main branch.*
*In conclusion: main branch should be clean as of now.*

*Time spent on item: ~3 minutes, slightly more than expected.*
### 3) Demo of our app (4 min) - (inform)

- Show our current app from the main branch. Highlight progress compared to last week.
  - *TA Impressions: the TA seemed pleased with the progress made on the application. She mentioned that "progress was being made". A win in my book.*
    
- *Time spent: ~3 minutes*
### 4) Talking points (29 min) - (inform + decide)

**A) Status on each feature (+ their “extra touch”) + tests (11 min) - (inform + decide)**

- Give an overview of what we have accomplished and what we’re missing

    (by “extra touch” I mean the extra functionality we should add to each feature to make it better - in order to score max)

- Warning: the labels regarding completeness are based on our checklist, but I noticed it is not fully accurate, so please be 100% sure when you tick something as done.
- For each topic, comment on whether you disagree with whether the feature is done or not
- Decide: should we add an “extra touch” to all features? (Do we have time?)
    - We don’t have to come up with ideas for all now, but I suggest agreeing to think about them until next Monday's meeting
    
- Go over each feature:
    1. Basic Requirements - (done)
       - *There are still bugs present from previous features, but these will be resolved next week.*
    1. Automated Change Synchronization - (done)
		- *This feature is also complete, but must be added to. *
        “Extra touch” - (missing) - ideas?
        -  *Responsible for implementation of this "extra touch": Teammate 4*

    2. Nutritional Value - (not fully done)

        Missing: list all ingredients ordered by name + scaling + show kcal per 100g

        “Extra touch” - (missing) - ideas?
        -  *Responsible for implementation and adding "extra touch": Lukas Brookfield*

    3. Searching for Recipes - (done)
		- Fully done
        “Extra touch” - filtering a search
        - *Responsible for implementing extra touch: Teammate 2*

    4. Shopping List - (not fully done)
		- Mostly done
        “Extra touch” - (missing) - ideas?
        - *Responsible for implementation: Wojchek*

    5. Live Language Switch - (missing)
		- not done
        “Extra touch” - (missing) - ideas?
        - *Responsible for implementation: Lukas*

- Status on tests:
    - Server: 72% line coverage + 57% method coverage
    - Client: 9% line coverage + 16% method coverage

    Decide how to fix this (take into account that doing them all in the last week can mean we don’t get full points)
    - Order of action: fix bugs of current features first, implement last features after, polish everything afterwards
      - *The order of action above was adopted in the meeting as well by vote. Bugs in features implemented this week will be fixed first in week 8, then last features are implemented next week and the last week will be dedicated to polishing remaining code before code freeze.*

- Finally: decide what will be implemented in the next 2 weeks and how (if, for example, all features should be fully finished next week, so that the week after is for fixing bugs - or vice versa)

- *Time spent on this item: ~20 minutes, substantially less than expected.*
  
**B) Bug triage (13 min) - (inform + decide)**

- Go over each bug noted by the team (and make any necessary clarification by who wrote it down) and decide over other important matters.
1. *Bug: Search bar in ingredient overview doesn’t work *

    *Responsible for fixing this bug: Teammate 2*
    *Extra note: the search feature was apparently never fully finished, so this is not exactly a bug, but a feature forgotten to be implemented.*

2. *Bug:* IngredientType is saved despite the Recipe's creation being cancelled.

    *This bug is fixed.*
    
3. *Bug:* The units KG and L should be available units when adding an ingredient in the recipe overview, but aren't.

    *Responsible for fixing this bug: Teammate 4*

4. *Bug:* Shopping list and recipe overview don’t validate the amount, therefore throw a NumberFormatException

    *This bug is fixed.*

5. Bug: Deleting an ingredient type doesn’t delete all referring ingredients in the shopping list

    *Responsible for fixing this bug: Wojchek*

6. Bug: Adding ingredients from a recipe to the shopping list doesn’t update the list unless you restart the program

    *Responsible for fixing this bug: Wojchek*

7. *Vote:* Should ingredient types have a unique name? Sometimes in the ingredient overview, it can be weird to see multiple ingredients with the same name.

    *Decision: Ingredient types require unique names*
    *Responsible for implementation: Teammate 4 may have done this already. If not: this is to be decided later.*

8. *Vote:* Maybe we should limit the number of characters for the names of ingredients and recipes to avoid unreasonable lengths?

    *Responsible for implementing this: Lukas*
    *Decision: There should be a limit for the amount of characters in an ingredient or recipe's name.*

9. *Bug/Inconsistency:* The frontend doesn’t allow a null unit but the backend does

    *Responsible for fixing bug: Teammate 5*
    *This bug may already be fixed, and I will be asking Teammate 4 if he has already fixed this bug.*

10. *Suggestion:* We might have to think of a different way to select ingredient type in recipe overview as with lots of ingredient types in the database a drop down menu isn’t good. Maybe we should switch to ingredient overview for this or something?

    *Decision: The priority is not great enough to resolve this week. It will be looked at next week.*

11. *Bug:* While creating a new recipe you get HTTP 404 Not Found.

    *This bug is fixed.*

- *Time spent on this item: ~18 minutes, substantially more than expected.*
**C) Reminder: formative feedback highlights (5 min) - (inform)** 

- **Technology:**
    - We scored very high overall, but Server implementation wasn’t “Excellent” (fix - move logic out of @Controller into @Service classes) - Teammate 5 is responsible for this
- **Tasks & Planning:**
    - Total was strong, but Issue Creation scored lower (1.5/2.5) because issue sizes vary wildly (some 30–60 min) (fix - make issues consistently 3-4 hours).
      
    - To reach Excellent: “Clearly specify which user story an issue is related to”
     * *Vote: do we fix all previous issues with proper labels and time consumption?*
      *Decision: Yes, we do. All individuals in the group are responsible for fixing issues they made or did throughout the course of this project.*
      
    - Warning: recent issues had worse descriptions; if judged only on those, the score would drop dramatically (fix - we need to make sure we re-read the criteria and keep the quality of our issues - with SMART descriptions, labels, time estimates, …)
- **Code Contribution:**
    - Overall great, but Reviewability wasn’t “Excellent”:
        - Some MRs were too large (10+ files), making it hard to review.
        - MR descriptions inconsistent (some too short / missing).
        - Some branches were merged while significantly behind main, leading to merge conflicts + messy reviews

---

- **Concrete standards to follow for everyone:**
    - Keep MRs small + focused, with a consistent description
    - Always merge/rebase latest main into your branch before merging.
    - Bundle a feature + its tests in the same MR
    - Remove the default “Person” and “Quote” tests that came with the repository
      - *(Teammate 5 is responsible for removing these classes.)*
- Any other feedback worth mentioning?
  *Extra responsibilities:*
  *Teammate 2: Is responsible for resolving issues in RecipeOverview.*
  *Teammate 4: Responsible for fixing bugs within ShoppinglistOverview.*
  *Teammate 5: Responsible for writing extra Javadoc in places that don't have it.*

- *Time spent on this item: ~5 minutes.*
### 5) Summarize action points: who, what, when? (2 min) - (inform)

*TA: Basic requirements should be checked rigorously and main should be clean.*
*Week 8: Bug fixes, adding last features.*
*Week 9: Polishing old code and preparing for code freeze.*

*Teammate 2 is responsible for implementing the search feature in RecipeOverview.
Wojchek is responsible for fixing bugs in his current features and more in ShoppingList.
Teammate 4 is responsible for adding onto a few other features and fixing some bugs.
Teammate 3 is responsible for finishing his features for next week and adding onto a few existing ones as well. His scaling functionality has already been reviewed by me (Teammate 5).*

- *Time spent on this item: ~4 minutes.*
### 6) Question round: does anyone have anything to add before the meeting closes? (2 min) (inform + decide)

- More time may be given to this if we still have time left.
  
- *(Item was not used.)*

### 7) Closure (1 min)

Note: I apology for uploading the agenda so late; this was due to us having had to change chair this week because of flight cancellations and me being sick. We decided to meet an hour before our actual meeting to go over everything and make sure the meeting was productive. Thanks everyone for your patience and collaboration!