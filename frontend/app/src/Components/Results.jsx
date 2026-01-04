import React, { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Typography
} from "@mui/material";

export default function Results() {
  const [joinCode, setJoinCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleDownload = async () => {
    if (!joinCode.trim()) {
      setError("Join code is required");
      return;
    }

    setError("");
    setLoading(true);

    try {
      const response = await fetch(
        `http://localhost:8080/quiz/getRankings/${joinCode}`,
        {
          method: "GET"
        }
      );

      if (!response.ok) {
        throw new Error("Results not found");
      }

      const blob = await response.blob();

      // Create download
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `leaderboard_${joinCode}.xlsx`;

      document.body.appendChild(link);
      link.click();

      link.remove();
      window.URL.revokeObjectURL(url);

    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#f5f5f5"
      }}
    >
      <Box
        sx={{
          width: 350,
          padding: 4,
          borderRadius: 2,
          backgroundColor: "white",
          boxShadow: 3
        }}
      >
        <Typography variant="h5" gutterBottom align="center">
          Quiz Results
        </Typography>

        <TextField
          fullWidth
          label="Join Code"
          value={joinCode}
          onChange={(e) => setJoinCode(e.target.value)}
          margin="normal"
        />

        {error && (
          <Typography color="error" variant="body2">
            {error}
          </Typography>
        )}

        <Button
          fullWidth
          variant="contained"
          sx={{ marginTop: 2 }}
          onClick={handleDownload}
          disabled={loading}
        >
          {loading ? "Downloading..." : "Get Result"}
        </Button>
      </Box>
    </Box>
  );
}
