|  |  |
| --- | --- |
| Date | 16/01/2026 |
| Time | 16:45 - 17:30 |
| Location | Drebbelweg PC Hall 2 |
| Chair | Teammate 1 |
| Minute Taker | Teammate 2 |
| Attendees: | Teammate 2, Teammate 1, Teammate 4, Lukas Brookfield, Teammate 5, Teammate 3 |

## Agenda Items:

### 1) Opening - Overview of the last week (4 min) - (inform)

- Was everyone able to finish their tasks? If not, why not?

    List from GitLab for reference:

    - Teammate 2: improve UI and service classes on the server side (done) + add search filter in ingredient overview (open)
    - Lukas: Add character limits for some fields (open) + client tests (done)
    - Teammate 4: Client-side input validation + warnings (done but not for shopping list)
    - Teammate 5: Make it possible to use Enter and Escape to navigate (in progress)
    - Teammate 1: Print and download buttons for the shopping list (in progress)
    - Teammate 3: Live Translation Feature (done)

### 2) Announcements by the TA (2 min) - (inform)

### 3) Demo of our app (4 min) - (inform)

- Show our current app from the main branch. Highlight progress compared to last week.

### 4) Talking points (17 min) - (inform + decide)

**A) Are features implemented? (3 min)**

- We need to check once again with the backlog if features are implemented correctly:  
    1. Automated Change Synchronization – decide who will check
    2. Nutritional Value – decide who will check
    3. Searching for Recipes – decide who will check
    4. Shopping List – decide who will check
    5. Live Language Switch – decide who will check  

**B) “Extra touch” for features (7 min) - (inform + decide)**

- For each topic, comment on whether you agree or disagree that the feature is done
- Extra touch ideas:  
    2. Automated Change Synchronization - (done)

        “Extra touch” - (missing) - ideas?  
        Teammate 4 was supposed to think about it.

    3. Nutritional Value - (done)

        Missing: list all ingredients ordered by name + scaling + show kcal per 100g

        “Extra touch” - (missing) - ideas?  
        Lukas was supposed to think about it.

    4. Searching for Recipes - (done)

        “Extra touch” - filtering a search  
        Teammate 2 was supposed to think about it.

    5. Shopping List - (done)

        “Extra touch” - (missing) - ideas?  
        Teammate 1 was supposed to think about it.

    6. Live Language Switch - (missing)

        Our language switch does not currently filter recipes

        “Extra touch” - (missing) - ideas?  
        Lukas was supposed to think about it.

- Finally: decide what will be implemented next week.

**C) Bug triage (5 min) - (inform + decide)**

- Go over each bug noted by the team 

1. Deleting an ingredient type doesn’t delete all referring ingredients in the shopping list

2. When you select a language and then switch scenes, the choicebox doesn’t get updated

3. Some buttons/labels/tooltips are missing translations (like next button when editing ingredient in RecipeOverview)

4. We might have to think of a different way to select ingredient types in the recipe overview. With lots of ingredient types in the database, a drop-down menu isn’t ideal. Maybe we should switch to the ingredient overview for this or a similar approach.

**D) Questions for the TA (2 min)**

- Can we use service classes on the client?

---

### 5) Summarize action points: who, what, when? (2 min) - (inform)

### 6) Question round: does anyone have anything to add before the meeting closes? (2 min) (inform + decide)

- More time may be given to this if we still have time left.

### 7) Closure (1 min)