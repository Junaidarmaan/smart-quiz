import { useNavigate } from "react-router-dom";
import { Box, Button, Card, CardContent, Container, Stack, Typography } from "@mui/material";
import { useAuth } from "../../context/AuthContext";

const QuizCard = ({ title, description, buttonText, onClick }) => (
  <Card
    elevation={0}
    sx={{
      flex: 1,
      borderRadius: 4,
      border: (theme) => `1px solid ${theme.palette.divider}`,
      backgroundColor: "background.paper",
      boxShadow: "0 8px 30px rgba(0,0,0,0.35)",
    }}
  >
    <CardContent sx={{ p: 4, textAlign: "center" }}>
      <Typography variant="h6" fontWeight={700} gutterBottom>
        {title}
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
        {description}
      </Typography>
      <Button
        variant="contained"
        size="medium"
        onClick={onClick}
        sx={{ fontWeight: 700, borderRadius: 3 }}
      >
        {buttonText}
      </Button>
    </CardContent>
  </Card>
);

export default function Dashboard() {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const cards = [
    {
      title: "Create Quiz",
      description: "Create a new quiz and generate a unique code for players.",
      buttonText: "Create",
      onClick: () => navigate("/createQuiz"),
    },
    {
      title: "Join Quiz",
      description: "Enter a quiz code to join a live or scheduled quiz.",
      buttonText: "Join",
      onClick: () => navigate("/join"),
    },
    {
      title: "Upcoming Quizzes",
      description: "View quizzes which are upcoming.",
      buttonText: "View",
      onClick: () => navigate("/upcomingQuizzes"),
    },
    {
      title: "Settings",
      description: "Handle your profile.",
      buttonText: "Logout",
      onClick: () => logout(),
    },
  ];

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
          <Typography variant="h4" fontWeight={800} letterSpacing={1} textAlign="center">
            Quiz App
          </Typography>
          <Stack
            direction={{ xs: "column", sm: "row" }}
            spacing={3}
            width="100%"
            justifyContent="center"
          >
            {cards.map((card) => (
              <QuizCard key={card.title} {...card} />
            ))}
          </Stack>
        </Stack>
      </Container>
    </Box>
  );
}