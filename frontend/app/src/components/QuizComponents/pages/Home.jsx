import { useNavigate } from "react-router-dom";
import { Box, Button, Card, CardContent, Container, Stack, Typography } from "@mui/material";

export default function Home() {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        py: { xs: 4, sm: 6 },
        bgcolor: "background.default",
      }}
    >
      <Container maxWidth="md">
        <Stack spacing={4} alignItems="center">
          <Typography
            variant="h4"
            fontWeight={800}
            letterSpacing={1}
            textAlign="center"
          >
            Quiz App
          </Typography>

          <Stack
            direction={{ xs: "column", sm: "row" }}
            spacing={3}
            width="100%"
            justifyContent="center"
          >
            {/* Create Quiz */}
            <Card
              elevation={0}
              sx={{
                flex: 1,
                borderRadius: 4,
                border: (theme) => `1px solid ${theme.palette.divider}`,
                backgroundColor: "background.paper",
                boxShadow:
                  "0 8px 30px rgba(0,0,0,0.35), 0 1px 0 rgba(255,255,255,0.03) inset",
              }}
            >
              <CardContent sx={{ p: 4, textAlign: "center" }}>
                <Typography variant="h6" fontWeight={700} gutterBottom>
                  Create Quiz
                </Typography>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mb: 3 }}
                >
                  Create a new quiz and generate a unique code for players.
                </Typography>

                <Button
                  variant="contained"
                  size="medium"
                  onClick={() => navigate("/CreateQuiz")}
                  sx={{ fontWeight: 700, borderRadius: 3 }}
                >
                  Create
                </Button>
              </CardContent>
            </Card>

            {/* Join Quiz */}
            <Card
              elevation={0}
              sx={{
                flex: 1,
                borderRadius: 4,
                border: (theme) => `1px solid ${theme.palette.divider}`,
                backgroundColor: "background.paper",
                boxShadow:
                  "0 8px 30px rgba(0,0,0,0.35), 0 1px 0 rgba(255,255,255,0.03) inset",
              }}
            >
              <CardContent sx={{ p: 4, textAlign: "center" }}>
                <Typography variant="h6" fontWeight={700} gutterBottom>
                  Join Quiz
                </Typography>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mb: 3 }}
                >
                  Enter a quiz code to join a live or scheduled quiz.
                </Typography>

                <Button
                  variant="contained"
                  size="medium"
                  onClick={() => navigate("/join")}
                  sx={{ fontWeight: 700, borderRadius: 3 }}
                >
                  Join
                </Button>
              </CardContent>
            </Card>

            {/* Upcoming Quizzes */}
            <Card
              elevation={0}
              sx={{
                flex: 1,
                borderRadius: 4,
                border: (theme) => `1px solid ${theme.palette.divider}`,
                backgroundColor: "background.paper",
                boxShadow:
                  "0 8px 30px rgba(0,0,0,0.35), 0 1px 0 rgba(255,255,255,0.03) inset",
              }}
            >
              <CardContent sx={{ p: 4, textAlign: "center" }}>
                <Typography variant="h6" fontWeight={700} gutterBottom>
                  Upcoming Quizzes
                </Typography>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mb: 3 }}
                >
                  View quizzes which are upcoming.
                </Typography>

                <Button
                  variant="contained"
                  size="medium"
                  onClick={() => navigate("/upcomingQuizzes")}
                  sx={{ fontWeight: 700, borderRadius: 3 }}
                >
                  View
                </Button>
              </CardContent>
            </Card>
          </Stack>
        </Stack>
      </Container>
    </Box>
  );
} 