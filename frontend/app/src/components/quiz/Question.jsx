import { useState } from "react";
import { profileStore } from "../../utils/storage";
import { Box, Button, Container, Typography, Backdrop } from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";
import Live from "../../assets/Live";

export default function Question({
  data,
  onNext,
  flag,
  isCorrect,
  curQuestion,
  totalQuestions,
  score,
  quizId
}) {
  const [selected, setSelected] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleClick = async (option) => {
    if (loading) return;
    setSelected(option);
    setLoading(true);

    const res = await isCorrect(option);

    if (res === true) {
      setResult("correct");
      Live.send("/app/updateScore", {
        userName: profileStore.get(),
        quizId: quizId
      });
    } else {
      setResult("wrong");
    }

    setTimeout(() => {
      setSelected(null);
      setResult(null);
      setLoading(false);
      onNext();
    }, 600);
  };

  if (flag) {
    return (
      <Box
        minHeight="100vh"
        display="flex"
        justifyContent="center"
        alignItems="center"
        bgcolor="background.default"
      >
        <Box
          sx={{
            bgcolor: "background.paper",
            padding: 4,
            borderRadius: 4,
            border: (theme) => `1px solid ${theme.palette.divider}`,
            boxShadow: "0 10px 30px rgba(0,0,0,0.25)"
          }}
        >
          <Typography variant="h4" fontWeight={700} color="text.primary">
            Quiz completed 🎉
          </Typography>
        </Box>
      </Box>
    );
  }

  return (
    <Box
      minHeight="100vh"
      width="100%"
      sx={{
        bgcolor: "background.default",
        display: "flex",
        justifyContent: "center",
        padding: 2
      }}
    >
      <Container
        maxWidth="sm"
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 3
        }}
      >
        {/* Question Number */}
        <Box display="flex" justifyContent="center" mt={1}>
          <Box
            sx={{
              width: 64,
              height: 64,
              borderRadius: "50%",
              bgcolor: "primary.main",
              color: "#fff",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              fontWeight: 700,
              fontSize: "1.1rem",
              boxShadow: "0 8px 20px rgba(0,0,0,0.35)"
            }}
          >
            {parseInt(curQuestion) + 1}/{parseInt(totalQuestions)}
          </Box>
        </Box>

        {/* Score */}
        <Box display="flex" justifyContent="center">
          <Typography variant="body1" color="text.secondary" fontWeight={600}>
            Score: {score}
          </Typography>
        </Box>

        {/* Question Card */}
        <Box
          sx={{
            bgcolor: "background.paper",
            borderRadius: 4,
            padding: 3,
            border: (theme) => `1px solid ${theme.palette.divider}`,
            boxShadow: "0 12px 35px rgba(0,0,0,0.25)"
          }}
        >
          <Typography
            variant="h6"
            fontWeight={600}
            textAlign="center"
            lineHeight={1.5}
            color="text.primary"
          >
            {data.question}
          </Typography>
        </Box>

        {/* Options */}
        <Box display="flex" flexDirection="column" gap={1.8}>
          {[
            { key: "A", text: data.optionA },
            { key: "B", text: data.optionB },
            { key: "C", text: data.optionC },
            { key: "D", text: data.optionD }
          ].map((opt) => {
            const isSelected = selected === opt.key;
            const isCorrectAnswer = isSelected && result === "correct";
            const isWrongAnswer = isSelected && result === "wrong";

            return (
              <Button
                key={opt.key}
                fullWidth
                onClick={() => handleClick(opt.key)}
                disabled={loading}
                sx={{
                  justifyContent: "space-between",
                  paddingY: 1.7,
                  paddingX: 2.5,
                  borderRadius: 3,
                  bgcolor: isCorrectAnswer
                    ? "success.light"
                    : isWrongAnswer
                    ? "error.light"
                    : "background.paper",
                  color: isCorrectAnswer
                    ? "success.dark"
                    : isWrongAnswer
                    ? "error.dark"
                    : "text.primary",
                  border: (theme) => isCorrectAnswer
                    ? `1px solid ${theme.palette.success.main}`
                    : isWrongAnswer
                    ? `1px solid ${theme.palette.error.main}`
                    : `1px solid ${theme.palette.divider}`,
                  textTransform: "none",
                  fontSize: "1rem",
                  fontWeight: 500,
                  boxShadow: "0 6px 18px rgba(0,0,0,0.15)",
                  "&:hover": {
                    bgcolor: isCorrectAnswer
                      ? "success.light"
                      : isWrongAnswer
                      ? "error.light"
                      : "background.paper",
                  }
                }}
              >
                {opt.text}
                {isCorrectAnswer && <CheckCircleIcon />}
                {isWrongAnswer && <CancelIcon />}
              </Button>
            );
          })}
        </Box>
      </Container>

      <Backdrop open={loading} sx={{ backgroundColor: "transparent" }} />
    </Box>
  );
}