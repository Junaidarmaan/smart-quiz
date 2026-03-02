import { useState } from 'react';
import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function JoinQuiz() {
  const navigate = useNavigate();
  const [code, setCode] = useState("");

  const handleJoin = () => {
    if (!code.trim()) return;
    navigate(`/play/${code}`);
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "background.default",
      }}
    >
      <Container maxWidth="sm">
        <Box
          sx={{
            bgcolor: "background.paper",
            borderRadius: 4,
            p: 4,
            border: (theme) => `1px solid ${theme.palette.divider}`,
            display: "flex",
            flexDirection: "column",
            gap: 3,
          }}
        >
          <Typography variant="h5" fontWeight={700} color="text.primary">
            Join a Quiz
          </Typography>

          <TextField
            label="Join Code"
            placeholder="Enter quiz code to join"
            fullWidth
            onChange={(e) => setCode(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleJoin()}
            required
          />

          <Button
            variant="contained"
            size="large"
            onClick={handleJoin}
            sx={{ fontWeight: 700, borderRadius: 3 }}
          >
            Join
          </Button>
        </Box>
      </Container>
    </Box>
  );
}