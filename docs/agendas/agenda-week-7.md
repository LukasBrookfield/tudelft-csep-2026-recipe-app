|  |  |
| --- | --- |
| Date | 09/01/2026 |
| Time | 16:45 - 17:30 |
| Location | Flux Hall D |
| Chair | Teammate 3 |
| Minute Taker | Teammate 5 |
| Attendees: | Teammate 2, Teammate 1, Teammate 4, Lukas Brookfield, Teammate 5, Teammate 3 |

## Agenda Items:

### 0) Quick start (1 min) - (inform)

- **Goal for today:** recenter as we come back from the holidays (reminding of the feedback we were given) + make a plan for the last 2 weeks of coding

### 1) Overview of last week (4 min) - (inform)

- Discuss one by one the contributions from last week. Was everyone able to finish their tasks? If not, why not?

    List from GitLab for reference:

    - Teammate 2: DB + client-server connection (done per GitLab issue) and IngredientTypeOverview tests (open)
    - Lukas: RecipeOverview tests (done per GitLab issue)
    - Teammate 4: Client-side input validation + warnings (open)
    - Teammate 5: Interactive language dropdown (open)
    - Teammate 1: Add ingredients from recipe to Shopping List (in progress)
    - Teammate 3: Add scaling feature (in progress) + Replace exceptions with alerts (open)

### 2) Announcements by the TA (2 min) - (inform)

### 3) Demo of our app (4 min) - (inform)

- Show our current app from the main branch. Highlight progress compared to last week.

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
    2. Automated Change Synchronization - (done)

        “Extra touch” - (missing) - ideas?

    3. Nutritional Value - (not fully done)

        Missing: list all ingredients ordered by name + scaling + show kcal per 100g

        “Extra touch” - (missing) - ideas?

    4. Searching for Recipes - (done)

        “Extra touch” - filtering a search

    5. Shopping List - (not fully done)

        “Extra touch” - (missing) - ideas?

    6. Live Language Switch - (missing)

        “Extra touch” - (missing) - ideas?

- Status on tests:
    - Server: 72% line coverage + 57% method coverage
    - Client: 9% line coverage + 16% method coverage

    Decide how to fix this (take into account that doing them all in the last week can mean we don’t get full points)

- Finally: decide what will be implemented in the next 2 weeks and how (if, for example, all features should be fully finished next week, so that the week after is for fixing bugs - or vice versa)

**B) Bug triage (13 min) - (inform + decide)**

- Go over each bug noted by the team (and make any necessary clarification by who wrote it down)
1. Search bar in ingredient overview doesn’t work 

    Pick who will fix

2. I'm not sure if the ingredientType created in a cancelled recipe should still be saved

    Decide and pick who will fix

3. The units KG and L should be available units when adding an ingredient in the recipe overview

    Pick who will fix

4. Shopping list and recipe overview don’t validate the amount, therefore throw a NumberFormatException

    Pick who will fix

5. Deleting an ingredient type doesn’t delete all referring ingredients in the shopping list

    Pick who will fix

6. Adding ingredients from a recipe to the shopping list doesn’t update the list unless you restart the program

    Pick who will fix

7. Should ingredient types have a unique name? Sometimes in the ingredient overview, it can be weird to see multiple ingredients with the same name

    Decide and pick who will fix

8. Maybe we should limit the number of characters for the names of ingredients and recipes to avoid unreasonable lengths

    Decide and pick who will fix

9. The frontend doesn’t allow a null unit but the backend does

    Decide and pick who will fix

10. We might have to think of a different way to select ingredient type in recipe overview as with lots of ingredient types in the database a drop down menu isn’t good. Maybe we should switch to ingredient overview for this or something

    Decide and pick who will fix

11. While creating a new recipe you get HTTP 404 Not Found.

    Decide and pick who will fix


**C) Reminder: formative feedback highlights (5 min) - (inform)** 

- **Technology:**
    - We scored very high overall, but Server implementation wasn’t “Excellent” (fix - move logic out of @Controller into @Service classes) - Pick who will do this
- **Tasks & Planning:**
    - Total was strong, but Issue Creation scored lower (1.5/2.5) because issue sizes vary wildly (some 30–60 min) (fix - make issues consistently 3-4 hours).
    - To reach Excellent: “Clearly specify which user story an issue is related to”
    - Warning: recent issues had worse descriptions; if judged only on those, the score would drop dramatically (fix - we need to make sure we re-read the criteria and keep the quality of our issues - with SMART descriptions, labels, time estimates, …)
- **Code Contribution:**
    - Overall great, but Reviewability wasn’t “Excellent”:
        - Some MRs were too large (10+ files), making it hard to review.
        - MR descriptions inconsistent (some too short / missing).
        - Some branches were merged while significantly behind main, leading to merge conflicts + messy reviews

---

- **Concrete standards to follow:**
    - Keep MRs small + focused, with a consistent description
    - Always merge/rebase latest main into your branch before merging.
    - Bundle a feature + its tests in the same MR
    - Remove the default “Person” and “Quote” tests that came with the repository
- Any other feedback worth mentioning?

### 5) Summarize action points: who, what, when? (2 min) - (inform)

### 6) Question round: does anyone have anything to add before the meeting closes? (2 min) (inform + decide)

- More time may be given to this if we still have time left.

### 7) Closure (1 min)

Note: I apology for uploading the agenda so late; this was due to us having had to change chair this week because of flight cancellations and me being sick. We decided to meet an hour before our actual meeting to go over everything and make sure the meeting was productive. Thanks everyone for your patience and collaboration!