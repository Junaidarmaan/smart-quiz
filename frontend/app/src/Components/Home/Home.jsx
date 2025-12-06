import React, { useState } from 'react'
import './Home.css'
import { useNavigate } from 'react-router-dom';
import { TextField } from '@mui/material';
import Backdrop from '@mui/material/Backdrop';


export default function Home() {
  const navigate = useNavigate()
  const [isDisabled, setIsDisabled] = useState(true)
  const [userButton, setUserButton] = useState("edit")
  const [userName, setusername] = useState(sessionStorage.getItem("userName"))
  const [blocked,setBlocked] = useState(sessionStorage.getItem("userName")==null?true:false)
  return (
    <div className="home-container">

      <h1 className="title">Quiz App</h1>

      <div className="actions">

        <div className="card">
          <h2>Create Quiz</h2>
          <p>Create a new quiz and generate a unique code for players.</p>
          <button onClick={() => navigate("/CreateQuiz")}>Create</button>
        </div>

        <div className="card">
          <h2>Join Quiz</h2>
          <p>Enter a quiz code to join a live or scheduled quiz.</p>
          <button
            onClick={() => navigate("/join")}
          >Join</button>
        </div>

        <div className='card'>
          <h2>Upcoming Quizes</h2>
          <p>View quizzes which are upcoming</p>
          <button
            onClick={() => navigate("/upcomingQuizzes")}
          >View</button>
        </div>

        <div className={`card user ${userName === null?"card-error":""}`}>
          <h2>YourUsername</h2>
          <TextField
            size='small'
            value={userName}
            variant='outlined'

            disabled={isDisabled}
            onChange={(e) => {
              setusername(e.target.value)
            }}
            sx={{
              margin: "20px"
            }}
          />
          <button
            onClick={() => {
              if (isDisabled) {
                setIsDisabled(false)
                setUserButton("submit")

              } else {
                sessionStorage.setItem("userName",userName);
                setBlocked(false)
                setIsDisabled(true)
                setUserButton("already done")
              }

            }}
            disabled = {!blocked}
          >{userButton}</button>
        </div>

      </div>

      <Backdrop
      open={blocked}
      ></Backdrop>
    </div>
  );

}
