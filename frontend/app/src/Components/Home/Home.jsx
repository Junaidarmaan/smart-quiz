import React from 'react'
import './Home.css'
import { useNavigate } from 'react-router-dom';

export default function Home() {
    const navigate = useNavigate()
   return (
    <div className="home-container">
      
      <h1 className="title">Quiz App</h1>

      <div className="actions">

        <div className="card">
          <h2>Create Quiz</h2>
          <p>Create a new quiz and generate a unique code for players.</p>
          <button onClick={()=>navigate("/CreateQuiz")}>Create</button>
        </div>

        <div className="card">
          <h2>Join Quiz</h2>
          <p>Enter a quiz code to join a live or scheduled quiz.</p>
          <button
          onClick={()=>navigate("/join")}
          >Join</button>
        </div>

        <div className="card">
          <h2>Upcoming Quizes</h2>
          <p>View quizzes which are upcoming</p>
          <button 
          onClick={()=>navigate("/upcomingQuizzes")}
          >View</button>
        </div>

        <div className="card">
          <h2>Leaderboard</h2>
          <p>See top players and performance across quizzes.</p>
          <button>View</button>
        </div>

      </div>

    </div>
  );

}
