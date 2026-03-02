import { useState, useEffect } from 'react';
import {
  Box, Container, Typography, CircularProgress,
  Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow
} from '@mui/material';
import { quizApi } from '../../api/quizApi';

export default function UpcomingQuizzes() {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    quizApi.getUpcomingQuizzes()
      .then((res) => {
        setQuizzes(res.data.data);
      })
      .catch(() => setError("Failed to load upcoming quizzes"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <Box minHeight="100vh" display="flex" justifyContent="center" alignItems="center" bgcolor="background.default">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box minHeight="100vh" display="flex" justifyContent="center" alignItems="center" bgcolor="background.default">
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box minHeight="100vh" bgcolor="background.default" py={4}>
      <Container maxWidth="md">
        <Typography variant="h5" fontWeight={700} mb={3} color="text.primary">
          Upcoming Quizzes
        </Typography>

        {quizzes.length === 0 ? (
          <Typography color="text.secondary">No upcoming quizzes found.</Typography>
        ) : (
          <TableContainer sx={{ bgcolor: "background.paper", borderRadius: 3 }}>
            <Table>
              <TableHead>
                <TableRow>
                  {["S.No", "Join Code", "Schedule", "Duration (mins)"].map((h) => (
                    <TableCell key={h} sx={{ color: "text.secondary", fontWeight: 600 }}>{h}</TableCell>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {quizzes.map((quiz, index) => (
                  <TableRow key={quiz.quizId}>
                    <TableCell color="text.primary">{index + 1}</TableCell>
                    <TableCell color="text.primary">{quiz.joinCode}</TableCell>
                    <TableCell color="text.primary">{quiz.schedule.dateTime}</TableCell>
                    <TableCell color="text.primary">{quiz.schedule.duration}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Container>
    </Box>
  );
}