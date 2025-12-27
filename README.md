# Smart Quiz 🧠⚡  
Because quizzes deserve better than boring forms.

Smart Quiz is a web application built to rescue online quizzes from the dark ages of static forms and copy-paste answers. Many colleges still rely on tools that were never meant for serious assessments. You know the type. Open in one tab, Google in another, zero control, zero thrill.

Smart Quiz fixes that by turning quizzes into a live, timed, interactive experience where both teachers and students actually feel like something is happening.

This project has two main parts:  
Frontend built with React  
Backend built with Spring Boot  

Each part has its own README with setup instructions. This file explains the big picture.

---

## What Smart Quiz Actually Does

Smart Quiz lets teachers create quizzes and students join them at the right time, not whenever they feel like it. Questions appear one by one, results update live, and the quiz flows like an event instead of a form you casually scroll through.

Simple idea. Serious execution.

---

## Main Features

### 1. Manual Quiz Creation  
For teachers who want full control.

Teachers can create quizzes by adding their own questions. Each question includes:
- Question text  
- Four options  
- Correct answer  

No surprises. No AI guessing what you want. You decide everything.

---

### 2. Automatic Quiz Creation  
For teachers who value their time.

Just enter:
- Topic  
- Number of questions  
- Difficulty level (1 to 10)  

The system uses an AI model to generate questions instantly. Less preparation, more productivity, and no last-minute panic before class.

---

### 3. Quiz Scheduling  
Because timing matters.

Every quiz has:
- A start time  
- A fixed duration  

Students cannot join early.  
Students cannot join late.  

If you miss the quiz, that’s on you. The system is strict, fair, and unapologetic.

---

### 4. Joining and Playing the Quiz  
No scrolling. No skipping ahead. No cheating buffet.

Students join using a quiz code. Once inside:
- One question appears at a time  
- Student selects an answer  
- Green for correct, red for wrong  
- Automatically moves to the next question  

Questions are locked in sequence, keeping everyone focused and making shortcuts pointless.

---

### 5. Live Leaderboard  
Because competition makes people try harder.

The leaderboard updates in real time using WebSockets (STOMP). It shows:
- Students currently playing  
- Their scores  
- Their ranks  

It stays visible during the quiz and reveals the final standings at the end. Motivation included. Pressure included. Drama included.

---

## Technologies Used

### Frontend
- React  
- WebSockets using Stomp.js  
- Modern, responsive UI components  

### Backend
- Spring Boot  
- WebSocket support for real-time updates  
- REST APIs for quiz creation, joining, and gameplay  
- Database for quizzes, questions, and scores  

---

## Project Structure

smart-quiz/
│
├── frontend/     # React application
├── backend/      # Spring Boot application
└── README.md     # You are here

Each folder has its own README with setup steps and commands.

---

## Why Smart Quiz Exists

Smart Quiz gives teachers control and students a fair challenge.  
No static pages.  
No passive clicking.  
No easy shortcuts.

Just a clean, live, timed quiz experience that actually feels like an assessment.

---

## Smart Quiz – Structured System Flow  
From quiz creation to final leaderboard, step by step.

This section explains how Smart Quiz actually works behind the scenes. Think of it as the story of a quiz, from birth to glory.

---

## 1. Teacher Creates a Quiz

### Option A: Manual Creation
1. Teacher logs in.
2. Teacher creates a new quiz.
3. Adds questions manually:
   - Question text
   - Four options
   - Correct answer
4. Sets:
   - Quiz start time
   - Quiz duration
5. Quiz is saved in the database.
6. System generates a unique **quiz code**.

Result: Quiz is ready and waiting for its scheduled time.

---

### Option B: Automatic (AI-Based) Creation
1. Teacher selects auto-create quiz.
2. Teacher provides:
   - Topic
   - Number of questions
   - Difficulty level (1–10)
3. Backend sends these inputs to the AI model.
4. AI generates questions and answers.
5. Questions are validated and stored.
6. Teacher sets start time and duration.
7. Quiz code is generated.

Result: Quiz created in minutes, not hours.

---

## 2. Quiz Scheduling & Access Control

1. Every quiz has a strict start time and end time.
2. Backend continuously checks current server time.
3. Students attempting to join:
   - Before start time → Access denied
   - After end time → Access denied
4. Only during the active window can students join.

Result: No early birds. No late excuses.

---

## 3. Student Joins the Quiz

1. Student enters the quiz code.
2. Backend validates:
   - Quiz exists
   - Quiz is currently active
3. Student is registered as a participant.
4. Student is added to the live session via WebSocket.

Result: Student enters the quiz arena.

---

## 4. Question Flow During the Quiz

1. Quiz starts.
2. Backend sends **only the first question** to all active students.
3. Student selects an option.
4. Answer is sent to the backend.
5. Backend:
   - Checks correctness
   - Updates score
6. Immediate feedback is sent:
   - Green for correct
   - Red for wrong
7. Backend pushes the next question.

Important rule:
- Students cannot skip questions.
- Students cannot go back.
- Everyone moves forward in sync.

Result: Controlled, fair, distraction-free gameplay.

---

## 5. Live Leaderboard Updates

1. Every answer submission updates the student’s score.
2. Backend recalculates rankings.
3. Updated leaderboard data is pushed via WebSockets (STOMP).
4. Frontend updates leaderboard in real time without refresh.

Leaderboard shows:
- Player names
- Current scores
- Live ranks

Result: Real-time pressure. Real-time motivation.

---

## 6. Quiz Completion

1. Last question is submitted.
2. Backend finalizes scores.
3. Final leaderboard is generated.
4. Quiz session is marked as completed.
5. No further answers are accepted.

Result: The quiz officially ends. Winners are clear.

---

## 7. Post-Quiz State

1. Students can view:
   - Final rank
   - Final score
2. Teachers can later:
   - Review quiz data
   - Analyze performance
3. Quiz remains stored for record and analytics.

Result: Clean closure. No loose ends.

---

## High-Level Flow Summary

Teacher creates quiz  
→ Quiz scheduled  
→ Students join with code  
→ Questions delivered one by one  
→ Answers evaluated instantly  
→ Leaderboard updates live  
→ Quiz ends  
→ Final results shown  

by junaid armaan
