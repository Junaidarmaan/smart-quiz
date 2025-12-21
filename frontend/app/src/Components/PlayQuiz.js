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
  const [score, setScore] = useState(0);
  const navigate = useNavigate()
  window.addEventListener('popstate', () => {
    console.log("u pressesd back");
    Live.disconnect()
  })
  document.addEventListener('fullscreenchange', () => {
    if (!document.fullscreenElement) {
      setFinished(true)
      setTimeout(() => {
        navigate('/')

      }, 2000)
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
        console.log(JSON.stringify(data));
      }
      )

  }, [])

  useEffect(() => {
    sessionStorage.setItem("quizId", code);

    if (requestStatus && response.action) {
      console.log("use effect for live connet triggered");
      const url = `http://localhost:8080/getCurrentQuestion`;
      const reqData = {
        userName: sessionStorage.getItem("userName"),
        quizId: sessionStorage.getItem("quizId")
      }
      fetch(url, {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqData)
      }).then(pack =>
        pack.json())
        .then(data => {
          console.log("for current question", data);
          let n = response.data.questions.length;
          if (curQuestion >= n) {
            setFinished(true)
          }
          setCurQuestion(data.curQuestion)


          setRequestStatus(true)
        }
        )
      // document.documentElement.requestFullscreen();
      Live.connect(() => {
        console.log("connected now subscribing to quiz");

        Live.subscribe(`/topic/quiz/rankings/${code}`, (msg) => {
          console.log("from broker", msg);
          setRankings(msg);
        });
        Live.subscribe(`/topic/quiz/scoreUpdates/${sessionStorage.getItem("userName")}`, (msg) => {
          console.log("from broker after score update ", msg);
          setScore(msg.score);
        })

        const profile = {
          userName: sessionStorage.getItem("userName"),
          quizId: code,
          score: 0
        };

        Live.send('/app/joinQuiz', profile);
        console.log("sent data to joinUser", profile);
      });
    }

  }, [requestStatus]);

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
            curQuestion={curQuestion}
            totalQuestions={response.data.questions.length}
            score={score}
          />
        </>
      }
      {requestStatus && response.action &&
        <Box
          
        >
          
        </Box>
      }
      {requestStatus && !response.action &&
        <h1>{response.message}</h1>

      }
    </Box>
  )
}

