import React, { useState } from "react";
import {
  Box,
  Button,
  Container,
  Typography,
  Backdrop
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";

import Live from "./Live";
import quizBG from "./assets/quizBG.jpg"; // adjust path if needed

export default function Question({
  data,
  onNext,
  flag,
  isCorrect,
  curQuestion,
  totalQuestions,
  score
}) {
  const [selected, setSelected] = useState(null);
  const [result, setResult] = useState(null); // "correct" | "wrong"
  const [loading, setLoading] = useState(false);

  const handleClick = async (option) => {
    if (loading) return;

    setSelected(option);
    setLoading(true);

    const res = await isCorrect(option);

    if (res === true) {
      setResult("correct");

      // 🔥 SCORE UPDATE (same as your previous logic)
      Live.send("/app/updateScore", {
        userName: sessionStorage.getItem("userName"),
        quizId: sessionStorage.getItem("quizId")
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
        sx={{
          backgroundImage: `url(${quizBG})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat"
        }}
      >
        <Box
          sx={{
            backgroundColor: "rgba(255,255,255,0.9)",
            padding: 4,
            borderRadius: 4,
            boxShadow: "0 10px 30px rgba(0,0,0,0.25)"
          }}
        >
          <Typography variant="h4" fontWeight={700}>
            Quiz completed Your Score was {score}
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
        backgroundImage: `url(${quizBG})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        display: "flex",
        justifyContent: "center",
        padding: 2
      }}
    >
      {/* Overlay */}
      <Box
        sx={{
          position: "absolute",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.25)"
        }}
      />

      <Container
        maxWidth="sm"
        sx={{
          position: "relative",
          zIndex: 1,
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
              background:
                "linear-gradient(135deg, #6a85f1, #8f9cff)",
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

        {/* Question Card */}
        <Box
          sx={{
            backgroundColor: "rgba(255,255,255,0.95)",
            borderRadius: 4,
            padding: 3,
            boxShadow: "0 12px 35px rgba(0,0,0,0.25)"
          }}
        >
          <Typography
            variant="h6"
            fontWeight={600}
            textAlign="center"
            lineHeight={1.5}
            color="#1f2937"
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

            let bg = "rgba(255,255,255,0.95)";
            let color = "#1f2937";
            let border = "1px solid #e5e7eb";

            if (isSelected && result === "correct") {
              bg = "#e8f6ee";
              color = "#1e7f43";
              border = "1px solid #1e7f43";
            }

            if (isSelected && result === "wrong") {
              bg = "#fdecec";
              color = "#c62828";
              border = "1px solid #c62828";
            }

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
                  backgroundColor: bg,
                  color: color,
                  border: border,
                  textTransform: "none",
                  fontSize: "1rem",
                  fontWeight: 500,
                  boxShadow:
                    "0 6px 18px rgba(0,0,0,0.15)",
                  "&:hover": {
                    backgroundColor: bg
                  }
                }}
              >
                {opt.text}
                {isSelected && result === "correct" && (
                  <CheckCircleIcon />
                )}
                {isSelected && result === "wrong" && (
                  <CancelIcon />
                )}
              </Button>
            );
          })}
        </Box>
      </Container>

      {/* Click lock */}
      <Backdrop open={loading} sx={{ backgroundColor: "transparent" }} />
    </Box>
  );
}
