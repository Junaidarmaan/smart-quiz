import React, { useEffect, useState } from 'react'
import { Box } from '@mui/material';
import { useParams } from 'react-router-dom';
import Question from './Question';
import Live from './Live';
import { Paper, Typography, Divider } from "@mui/material";
import { useNavigate } from 'react-router-dom';


export default function PlayQuiz() {
  const { code } = useParams()
  const [response, setResponse] = useState({})
  const [requestStatus, setRequestStatus] = useState(false)
  const [curQuestion, setCurQuestion] = useState(0);
  const [rankings, setRankings] = useState([])
  const [finished, setFinished] = useState(false)
  const navigate = useNavigate()
  window.addEventListener('popstate', () => {
    console.log("u pressesd back");
    const profile = {
      userName: sessionStorage.getItem("userName"),
      quizId: code,
      score: 0
    };
    Live.send(`/app/removeUser`,profile);
    Live.disconnect()
  })
  document.addEventListener('fullscreenchange',()=>{
    if(!document.fullscreenElement){
      setFinished(true)
      setTimeout(()=>{
        navigate('/')

      },2000)
    }
  })
  useEffect(() => {
    const url = `http://localhost:8080/joinQuiz/${code}`;

    fetch(url, {
      method: "POST"
    }).then(pack =>
      pack.json())
      .then(data => {
        console.log(data)
        setResponse(data)
        setRequestStatus(true)
      }
      )

  }, [])

  useEffect(() => {
    sessionStorage.setItem("quizId", code);
    // document.documentElement.requestFullscreen();
    Live.connect(() => {
      console.log("connected now subscribing to quiz");

      Live.subscribe(`/topic/quiz/rankings/${code}`, (msg) => {
        console.log("from broker", msg);
        setRankings(msg);
      });

      const profile = {
        userName: sessionStorage.getItem("userName"),
        quizId: code,
        score: 0
      };

      Live.send('/app/joinQuiz', profile);
      console.log("sent data to joinUser", profile);
    });

  }, []);

  return (
    <Box
      display={"flex"}
      height={"100vh"}
      width={"100vw"}
      justifyContent={"center"}
      alignItems={"center"}
    >
      {requestStatus && response.action &&
        <>
          <Question data={response.data.questions[curQuestion]}
            onNext={() => {
              let n = response.data.questions.length;
              if (curQuestion === n - 1) {
                setFinished(true)
              } else {
                setCurQuestion(curQuestion + 1)
              }
            }}
            flag={finished}
            isCorrect={
              (optChoosen) => {
                const url = `http://localhost:8080/isCorrect`;

                const req = {
                  "quizId": response.data.quizId,
                  "questionId": response.data.questions[curQuestion].id,
                  "selectedOption": optChoosen
                }
                console.log(req)
                return fetch(url, {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify(req)
                }).then(pack => pack.json())
                  .then(res => {
                    console.log(res);
                    return res;
                  })

              }
            }
            quizId={code}
          />
        </>
      }
      <Box
        sx={{
          width: "260px",
          paddingLeft: 2,
          display: "flex",
          alignItems: "stretch"
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: "100%",
            borderRadius: 3,
            padding: 2,
            backgroundColor: "#fff",
            display: "flex",
            flexDirection: "column",
            height: "96vh",
            overflowY: "auto"
          }}
        >
          <Typography
            variant="h6"
            sx={{
              fontWeight: 700,
              textAlign: "center",
              marginBottom: 1
            }}
          >
            Leaderboard
          </Typography>

          <Divider sx={{ marginBottom: 2 }} />

          {/* Your dynamic users will be inserted here */}
          <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
            {
              rankings.map((el, index) => (

                <Paper
                  sx={{
                    padding: 1.5,
                    borderRadius: 2,
                    backgroundColor: "#f7f7f7",
                    boxShadow: 1
                  }}
                  key={index}
                >
                  <Typography variant="body1" sx={{ fontWeight: 600 }}>
                    {el.userName}
                  </Typography>
                  <Typography variant="body2" sx={{ opacity: 0.6 }}>
                    {el.score}
                  </Typography>
                </Paper>
              ))

            }


          </Box>
        </Paper>
      </Box>
      {requestStatus && !response.action &&
        <h1>{response.message}</h1>

      }
    </Box>
  )
}

