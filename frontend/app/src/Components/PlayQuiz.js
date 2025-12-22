import React, { useEffect, useState } from "react";
import { Box, Button, Typography } from "@mui/material";
import { useParams, useNavigate } from "react-router-dom";

import Question from "./Question";
import LeaderBoard from "./LeaderBoard";
import Live from "./Live";

export default function PlayQuiz() {
  const { code } = useParams();
  const navigate = useNavigate();

  const [response, setResponse] = useState({});
  const [requestStatus, setRequestStatus] = useState(false);

  const [curQuestion, setCurQuestion] = useState(0);
  const [rankings, setRankings] = useState([]);
  const [finished, setFinished] = useState(false);
  const [score, setScore] = useState(0);

  const [showLeaderboard, setShowLeaderboard] = useState(false);

  /* -------------------------------
     Handle browser back button
  -------------------------------- */
  useEffect(() => {
    const handleBack = () => {
      Live.disconnect();
    };

    window.addEventListener("popstate", handleBack);

    return () => {
      window.removeEventListener("popstate", handleBack);
    };
  }, []);

  /* -------------------------------
     Validate quiz (joinQuiz)
  -------------------------------- */
  useEffect(() => {
    const url = `http://localhost:8080/joinQuiz/${code}`;

    fetch(url, { method: "POST" })
      .then((pack) => pack.json())
      .then((data) => {
        setResponse(data);
        setRequestStatus(true);
      })
      .catch(() => {
        setRequestStatus(true);
        setResponse({
          action: false,
          message: "Something went wrong"
        });
      });
  }, [code]);

  /* -------------------------------
     WebSocket + current question
  -------------------------------- */
  useEffect(() => {
    if (!requestStatus || !response.action) return;

    sessionStorage.setItem("quizId", code);

    const url = `http://localhost:8080/getCurrentQuestion`;
    const reqData = {
      userName: sessionStorage.getItem("userName"),
      quizId: sessionStorage.getItem("quizId")
    };

    fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reqData)
    })
      .then((pack) => pack.json())
      .then((data) => {
        const n = response.data.questions.length;

        if (data.curQuestion >= n) {
          setFinished(true);
        } else {
          setCurQuestion(data.curQuestion);
        }
      });

    Live.connect(() => {
      Live.subscribe(`/topic/quiz/rankings/${code}`, (msg) => {
        setRankings(msg);
      });

      Live.subscribe(
        `/topic/quiz/scoreUpdates/${sessionStorage.getItem("userName")}`,
        (msg) => {
          setScore(msg.score);
        }
      );

      const profile = {
        userName: sessionStorage.getItem("userName"),
        quizId: code,
        score: 0
      };

      Live.send("/app/joinQuiz", profile);
    });

    return () => {
      Live.disconnect();
    };
  }, [requestStatus, response.action, code]);

  /* -------------------------------
     Auto switch back to Question
     when question changes
  -------------------------------- */
  useEffect(() => {
    setShowLeaderboard(false);
  }, [curQuestion]);

  /* -------------------------------
     Render
  -------------------------------- */
  return (
    <Box minHeight="100vh" width="100vw">
      {/* Invalid quiz */}
      {requestStatus && !response.action && (
        <Box
          minHeight="100vh"
          display="flex"
          justifyContent="center"
          alignItems="center"
        >
          <Typography variant="h5">
            {response.message}
          </Typography>
        </Box>
      )}

      {/* Valid quiz */}
      {requestStatus && response.action && (
        <>
          {/* Toggle Button */}
          <Box
            position="sticky"
            top={0}
            zIndex={10}
            sx={{
              backgroundColor: "#ffffff",
              padding: 1,
              borderBottom: "1px solid #e5e7eb",
              display: "flex",
              justifyContent: "center"
            }}
          >
            <Button
              variant="outlined"
              onClick={() => setShowLeaderboard((prev) => !prev)}
            >
              {showLeaderboard ? "Back to Quiz" : "View Leaderboard"}
            </Button>
          </Box>

          {/* Content */}
          {!showLeaderboard && (
            <Question
              data={response.data.questions[curQuestion]}
              curQuestion={curQuestion}
              totalQuestions={response.data.questions.length}
              score={score}
              flag={finished}
              onNext={() => {
                const n = response.data.questions.length;
                if (curQuestion === n - 1) {
                  setFinished(true);
                } else {
                  setCurQuestion((prev) => prev + 1);
                }
              }}
              isCorrect={(optChoosen) => {
                const url = `http://localhost:8080/isCorrect`;

                const req = {
                  quizId: response.data.quizId,
                  questionId: response.data.questions[curQuestion].id,
                  selectedOption: optChoosen
                };

                return fetch(url, {
                  method: "POST",
                  headers: { "Content-Type": "application/json" },
                  body: JSON.stringify(req)
                })
                  .then((pack) => pack.json())
                  .then((res) => res);
              }}
            />
          )}

          {showLeaderboard && (
            <LeaderBoard
              rankings={rankings}
              currentUserName={sessionStorage.getItem("userName")}
            />
          )}
        </>
      )}
    </Box>
  );
}
