# CSEP – Meeting Week 3 Minute

## General information

* **Date:** 28/11/2025

* **Time (planned):** 16:45 – 17:30

* **Time (actual):** 16:46 – 17:20

* **Location:** Drebbelweg, PC Hall 2

* **Chair:** Teammate 5

* **Minute Taker:** Teammate 3

* **Attendees:** Lukas, Teammate 4, Teammate 5, Teammate 2, Teammate 3, Teammate 1

* **Absent:** Nobody

---

## Assignments / completed workload (time est. ~4 minutes) – Actual duration: 1 min

* **Self-studies:** Everyone has completed the self-studies.
* **Issues:** Everyone finished their assigned issues except Teammate 2, who is still missing the tests for adding recipes.

  * This missing work will be moved to next week’s milestone.
* **Lecture understanding:** Everyone indicated they understood this week’s lecture.

---

## Announcements (time est. ~2 minutes) – Actual duration: ~5.5 min

### Team / general announcements

**Discussion summary:**

* Reminder to read the **grading rubrics** closely while developing.
* Teammate 4 remarked that our **merge requests are way too short** (often only 2–3 commits).

  * If someone depends on the MR, it can be shorter, but in general they should contain more, smaller commits.
* Sebas added that it’s not just about MR size: **we should break work into many small commits spread throughout the code**, not one giant commit at the end.

**Decisions:**

* Everyone agrees to:

  * Aim for merge requests with a reasonable number of small, meaningful commits.
  * Avoid huge “dump” commits whenever possible.

---

### TA announcements (part of same agenda item)

* **Schedule:** Alternate rooms update for weeks 6 and 9 are now on Brightspace.
* **GitLab downtime:** GitLab will be down in weeks 5 and 6, but this does **not** change the grading setup.
* **Git repair grading:** Grading for the Git repair will be done today or Monday.
* **Code of conduct:** Our code of conduct has been **approved**.
* **Buddy check:**

  * Buddy check is due next week.
  * We should follow the template and write things like:

    > “I noticed that you… This meant that I… Next time, consider…”
  * It’s okay to be subjective and personal in the feedback.
* **Contributions:**

  * The TAs will review our contributions this weekend.
  * If there is a problem, they will warn us next week.
* **Workload distribution:**

  * The project is meant to take the **full 6 weeks**, not be crammed into one.
* **AI usage:**

  * We must understand all the code that ends up in the repository; otherwise it can become an issue for the Board of Examiners.
  * We should clearly **reference AI usage** in our code where relevant.
  * If someone submits “AI slop” just to get 100 lines, the team should **reject it**.


---

## Presentation of current state of application (time est. ~1 minute) – Actual duration: 4 min

* The team showed the current product to the TA on a laptop.
* Short walkthrough of the existing functionality and UI.

---

## Talking points (time est. ~25 minutes) – Actual duration: ~15 min

### Add density to ingredients

* Lukas explained why density might be needed:

  * Example: an ingredient might be stored in **ml**, while nutritional information is given per **100 g**.
  * To calculate calories correctly, we need the link between volume and mass.
* Teammate 2 mentioned that in some cases we may only use grams and not have to deal with ml.
* Lukas will check if recipe ingredients in our context **actually require density** (and when).

---

### Nullable fields in the database

* Question: which fields can be **null** in the database?
* It was agreed that **recipes need a name** (already implemented by Lukas).
* Teammate 4 suggested that if an amount is not provided, the **user should receive a warning** that nutritional values cannot be computed.

**Decisions:**

* Recipe name is **mandatory**.
* Other fields may be nullable, but the UI must warn users when missing data means we can’t compute nutritional info (e.g. no amount → no calories).

**Action points:**

* Later implementation:

  * Add warnings in the UI when missing values (like amount or unit) prevent calculations such as calories or other nutritional metrics.

---

### Serving sizes, calories per 100 g, and ingredient database restructuring

* The team discussed how to implement serving sizes and calories per 100 g properly.
* Sebas explained the idea of having ingredients as **independent entities**:

  * We maintain a **database of ingredients**.
  * When adding a recipe, you **select existing ingredients** from this database.
  * If an ingredient does not exist yet, a **popup** appears to add the necessary information (carbs, other nutritional values, etc.).
* Everyone agreed this makes more sense structurally and makes it easier to compute nutrition consistently.

**Action points:**

* Lukas will implement the ingredient database and selection logic by **Sunday**.

---

### Home screen layout

* Idea: keep the home screen layout **consistent** (same general look), and then swap what appears in the main pane:

  * Generic welcome image
  * Edit recipe mode
  * View recipe mode
  * etc.
* Currently we’re only missing a **welcome image**, so it doesn’t feel urgent.

**Decisions:**

* The welcome image and polishing the home window will be **postponed** to a later milestone.

**Action points:**

* Teammate 3 will create a milestone for next week to handle:

  * Making the home window look nicer.
  * Warnings in UI when missing values.

---

### Testing: actual database vs fake list / fake repository

* Question: Should we test using the actual database, or use a fake list / repository?
* Teammate 4 argued for having only **one implementation** to keep things consistent.
* Teammate 1 suggested using a **fake repository** for tests.
* Lukas proposed asking on Mattermost; Teammate 5 asked the TA during the meeting.
* TA response:

  * In their year, they used **mocking in several ways**, including fake randomness, etc.
  * Since testing is **not a huge criterion** in the grading, they suggested a simpler setup using a fake implementation.

**Decisions:**

* The team decided to:

  * Use a **fake repository** as the main testing point for the app.
  * Focus on that rather than building “real DB” testing setups.

**Action points:**

* Developers working on tests will:

  * Use the fake repository as the main approach for testing application logic.

---

### Use of `var` vs explicit types

* Question: Do we keep using `var`? Teammate 5 pointed out that `var` can be harder to read.
* Lukas responded that `var` can make sense, especially in tests.
* Teammate 1 said it can be easier in tests, and Teammate 4 agreed, adding that in **production code** we should probably use explicit types for clarity.

**Decisions:**

* The team decided:

  * We **keep using `var` in tests** where it’s convenient.
  * In main / production code, we prefer **explicit types** for readability.


## Issues and problems within collaboration (time est. ~5 minutes) – Actual duration: 4 min

* The chair asked if anyone had any collaboration-related issues or annoyances.
* Teammate 5 shared that he **hasn’t been able to contribute much** because he feels he is not as experienced as the others.

  * He stressed that everyone is doing an incredible job and requested that the team **help him catch up**.
Sebas suggested we go over how everything works together next Monday at 13:45, so everyone is on the same page.

* Lukas added a reminder to use **4-space indentation**.
* Sebas reminded everyone to **pay attention to Checkstyle**.
* Lukas suggested closing merge requests quickly:

  * MRs should ideally be **reviewed within 24 hours**.
* Teammate 5 suggested that if the person assigned to review doesn’t review in time, **someone else can jump in and review**.
* Sebas reminded everyone to use: labels, descriptions and time estimates on GitLab issues.

**Decisions:**

* Monday meeting to get everyone up to speed.
* Merge request reviews should happen within **24 hours**.
* If an assigned reviewer doesn’t review, others are allowed to take over the review.
* Coding style:

  * 4 spaces indentation.
  * Respect Checkstyle rules.
* GitLab usage:

  * Use labels, descriptions, and time estimates consistently.

---

## Summary of meeting and confirmation of decisions (time est. ~5 minutes) – Actual duration: 4 min

* Follow through on:

  * Ingredient DB implementation (Lukas).
  * Density check (Lukas).
  * Milestone creation for UI/home improvements (Sebas).
  * Fake repository-based tests.
  * Coding style and MR review agreements.

---

### Additional TA feedback

* The TA noted that we **forgot to discuss each other’s contributions** during the meeting.
* Next chair should make sure this is explicitly included and discussed in the next meeting.

## Sprint Week 4 – Issues (decided before the meeting)

For this week (Sprint Week 4), the concrete issues and owners are:

### Teammate 1
- Basic shopping list properties  
- Test shopping list tests  

### Lukas
- Nutritional value  
- Unit testing for favouriting recipes  
- Add ability to favourite recipes  
- Finalise database schema (nutrition, density, etc.)

### Teammate 3
- Implement the full search feature  
- Make it so new recipes/ingredients/steps go straight into edit mode  
- Replace exceptions with alerts  

### Teammate 4
- Automated change synchronization  

### Teammate 2
- Unit testing for the JavaFX adding recipe scene and nutritional value  

### Teammate 5
- 

### Teammate 1
- 