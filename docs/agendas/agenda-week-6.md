| |                                                                                                                                                               |
| --- |---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Date: | 18/12/2025                                                                                                                                                    |
| Time: | 16:45 - 17:30                                                                                                                                                 |
| Location : | Flux Hall D                                                                                                                                                   |
| Chair | Teammate 4                                                                                                                                       |
| Minute Taker | Lukas Brookfield                                                                                                                                              |
| Attendees: | Teammate 2, Teammate 1, Teammate 4, Lukas Brookfield, Teammate 5, Teammate 3 |

## Agenda Items :
- Opening + overview of last week (10 min)
    - Discuss individual contributions made in the last week. Has everyone been able to finish their tasks?
    - Discuss the action list of last week:
        - Calculate nutritional values and calories for recipes.
        - Create separate classes for helper methods. These classes will be in the client/utils directory e.g. convert teaspoon and tablespoon into 5 milliliters and 15 milliliters respectively.
        - Create a settings menu (American measurement system).
        - The server broadcasts the changes in the list of recipes across all clients. Clients automatically subscribe to the websocket URL. If a recipe is edited, all clients who are viewing that recipe should see the updated recipe.
        - Create tests for the frontend.
        - Ask the user to provide the density for ingredients that have their quantity expressed in milliliters.
        - The user is able to mark recipes as favourite.
- Announcements by the team (1-2 min)
    - Has everybody passed the buddycheck assignment? Are there any concerns about the feedback? Was it appropriate?
    - Has anybody experienced issues regarding the gitlab outage?
    - Make sure to check the implemented features in the checklist.
- Announcements by the TA (3 min)
- Presentation of the current app to TA (4 min)
    - Show our current app from the main branch. Highlight progress compared to last week.
- Talking Points: (13-17 min)
    - Add warnings and client-side validation for ingredient amount. If a user types four instead of 4, they should receive a message like: "Invalid amount: Provide the amount as an integer!"
    - Should we add data propagation via websockets for the shopping list and favourite recipes as well? (it is an extra feature related to 4.2)
    - Does everybody agree with the current database structure?
    - How are we going to implement serving scaling? What about ingredients that don't have an amount?
    - Should we start implementing the live language switch in week 7 (The Recipe class needs a language attribute so there will be changes across the entire app)? If so, how do we store the list of languages and the list of flags? Do we make a separate entity?
- Summarize action points: who, what, when? (2 min)
- Feedback round: what went well and what can be improved next time? (2 min)
- Question round: does anyone have anything to add before the meeting closes? (2 min)
    - More time may be given to this if we still have time left.
- Closure (1 min)