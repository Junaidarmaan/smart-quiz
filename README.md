

# Smart Quiz

Smart Quiz is a web application that helps teachers create and conduct online quizzes in a better and more controlled way. Many colleges still use basic tools like Google Forms for quizzes, which makes it easy for students to cheat and hard for teachers to manage results. Smart Quiz tries to solve these problems by giving a live and interactive quiz experience.

This project has two main parts:
**Frontend built with React** and **Backend built with Spring Boot**. Both folders have their own separate README files for specific setup instructions.

This README gives a full overview of how the overall system works.

---

## What Smart Quiz Does

Smart Quiz allows teachers to create quizzes and students to join them at the correct time. The system also shows results in real time and keeps the quiz flow smooth. Everything is designed to make the quiz experience simple and fair for everyone.

---

## Main Features

### 1. Manual Quiz Creation

Teachers can create a quiz by adding their own questions.
Each question includes:

* The question text
* Four options
* Correct answer

Teachers can fully control what questions are added.

---

### 2. Automatic Quiz Creation

Teachers can also create a quiz automatically.
They only need to enter:

* A topic
* Number of questions
* Difficulty level (1 to 10)

The system uses an AI model to generate questions based on these inputs. This saves time and helps teachers quickly prepare quizzes.

---

### 3. Quiz Scheduling

Every quiz must have:

* A start time
* A duration

Students can only join the quiz at the scheduled time.
They cannot join early, and they cannot join after the quiz has ended.

---

### 4. Joining and Playing the Quiz

Students join the quiz using the quiz code.

Once they enter the quiz:

* One question appears at a time
* The student selects an answer
* The system shows green for correct and red for wrong
* Then it automatically goes to the next question

The flow is simple and fast, and students cannot see all questions at once, which reduces cheating.

---

### 5. Live Leaderboard

The leaderboard updates in real time using WebSockets (STOMP).

It shows:

* Students currently playing
* Their scores
* Their ranks

The leaderboard stays visible on the right side during the quiz.
At the end, students can also see the final leaderboard.

---

## Technologies Used

### Frontend

* React
* WebSockets (using Stomp.js)
* Modern UI design (React components, etc.)

### Backend

* Spring Boot
* WebSocket support for real-time updates
* REST APIs for quiz creation, joining, and playing
* Database to store quizzes, questions, and scores

---

## Project Structure

```
smart-quiz/
│
├── frontend/     # React application
├── backend/      # Spring Boot application
└── README.md     # This file
```

Each part has its own README with setup steps and commands.

---

## Why Smart Quiz Is Useful

Smart Quiz gives teachers more control and gives students a better experience.
Instead of a static form, quizzes become active, timed, and dynamic.
Teachers can easily create quizzes, and students get a fair and engaging way to attempt them.

---

## Created By

**Junaid Arman**

NOTE: THIS README IS GENERATED FROM GPT