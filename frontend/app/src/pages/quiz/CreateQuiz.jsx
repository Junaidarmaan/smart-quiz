import { Box, Button, Stack, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function CreateQuiz() {
  const navigate = useNavigate();

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
      <Stack spacing={3} alignItems="center">
        <Typography variant="h5" fontWeight={700}>
          How do you want to create your quiz?
        </Typography>

        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate("/makeQuiz")}
            sx={{ fontWeight: 700, borderRadius: 3, px: 4 }}
          >
            Create Manually
          </Button>

          <Button
            variant="contained"
            size="large"
            onClick={() => navigate("/generateQuiz")}
            sx={{ fontWeight: 700, borderRadius: 3, px: 4 }}
          >
            Generate from AI
          </Button>
        </Stack>
      </Stack>
    </Box>
  );
}