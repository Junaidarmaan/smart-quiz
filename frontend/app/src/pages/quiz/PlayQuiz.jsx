import { useEffect, useState } from "react";
import { Box, Button, Typography, CircularProgress } from "@mui/material";
import { useParams } from "react-router-dom";
import Question from "../../components/quiz/Question";
import LeaderBoard from "../../components/quiz/LeaderBoard";
import Live from "../../assets/Live";
import { profileStore } from "../../utils/storage";
import { quizApi } from "../../api/quizApi";

export default function PlayQuiz() {
  const { code } = useParams();

  const [quiz, setQuiz] = useState(null);
  const [requestStatus, setRequestStatus] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  const [curQuestion, setCurQuestion] = useState(0);
  const [rankings, setRankings] = useState([]);
  const [finished, setFinished] = useState(false);
  const [score, setScore] = useState(0);
  const [showLeaderboard, setShowLeaderboard] = useState(false);

  /* -------------------------------
     Handle browser back button
  -------------------------------- */
  useEffect(() => {
    const handleBack = () => Live.disconnect();
    window.addEventListener("popstate", handleBack);
    return () => window.removeEventListener("popstate", handleBack);
  }, []);

  /* -------------------------------
     Validate quiz (joinQuiz)
  -------------------------------- */
  useEffect(() => {
    quizApi.joinQuiz(code)
      .then((res) => {
        if (res.data.success) {
          setQuiz(res.data.data);
        } else {
          setErrorMessage(res.data.message);
        }
      })
      .catch(() => setErrorMessage("Something went wrong"))
      .finally(() => setRequestStatus(true));
  }, [code]);

  /* -------------------------------
     WebSocket + current question
  -------------------------------- */
  useEffect(() => {
    if (!requestStatus || !quiz) return;

    quizApi.getCurrentQuestion({
      userName: profileStore.get(),
      quizId: code
    }).then((res) => {
      const n = quiz.questions.length;
      if (res.data.curQuestion >= n) {
        setFinished(true);
      } else {
        setCurQuestion(res.data.curQuestion);
      }
    });

    Live.connect(() => {
      Live.subscribe(`/topic/quiz/rankings/${code}`, (msg) => {
        setRankings(msg);
      });

      Live.subscribe(`/topic/quiz/scoreUpdates/${profileStore.get()}`, (msg) => {
        setScore(msg.score);
      });

      Live.send("/app/joinQuiz", {
        userName: profileStore.get(),
        quizId: code,
        score: 0
      });
    });

    return () => Live.disconnect();
  }, [requestStatus, quiz, code]);

  /* -------------------------------
     Auto switch back to Question
  -------------------------------- */
  useEffect(() => {
    setShowLeaderboard(false);
  }, [curQuestion]);

  const handleIsCorrect = (optChoosen) => {
    return quizApi.isCorrect({
      quizId: quiz.quizId,
      questionId: quiz.questions[curQuestion].id,
      selectedOption: optChoosen
    }).then((res) => res.data);
  };

  const handleNext = () => {
    const n = quiz.questions.length;
    if (curQuestion === n - 1) {
      setFinished(true);
    } else {
      setCurQuestion((prev) => prev + 1);
    }
  };

  /* -------------------------------
     Loading state
  -------------------------------- */
  if (!requestStatus) {
    return (
      <Box minHeight="100vh" display="flex" justifyContent="center" alignItems="center" bgcolor="background.default">
        <CircularProgress />
      </Box>
    );
  }

  /* -------------------------------
     Error state
  -------------------------------- */
  if (errorMessage) {
    return (
      <Box minHeight="100vh" display="flex" justifyContent="center" alignItems="center" bgcolor="background.default">
        <Typography variant="h5" color="text.primary">{errorMessage}</Typography>
      </Box>
    );
  }

  /* -------------------------------
     Render
  -------------------------------- */
  return (
    <Box minHeight="100vh" bgcolor="background.default">
      {/* Toggle Button */}
      <Box
        position="sticky"
        top={0}
        zIndex={10}
        sx={{
          bgcolor: "background.paper",
          padding: 1,
          borderBottom: (theme) => `1px solid ${theme.palette.divider}`,
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
          data={quiz.questions[curQuestion]}
          curQuestion={curQuestion}
          totalQuestions={quiz.questions.length}
          score={score}
          flag={finished}
          onNext={handleNext}
          isCorrect={handleIsCorrect}
        />
      )}

      {showLeaderboard && (
        <LeaderBoard
          rankings={rankings}
          currentUserName={profileStore.get()}
        />
      )}
    </Box>
  );
}