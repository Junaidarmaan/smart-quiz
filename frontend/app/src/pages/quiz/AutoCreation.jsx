import { useState } from 'react';
import {
  Box, Button, Container, TextField, Typography,
  CircularProgress, Backdrop, Alert
} from '@mui/material';
import {
  TableContainer, Table, TableBody, TableCell,
  TableHead, TableRow
} from '@mui/material';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import SendIcon from '@mui/icons-material/Send';
import { quizApi } from '../../api/quizApi';

export default function AutoCreation() {
  const [data, setData] = useState({});
  const [schedule, setSchedule] = useState({});
  const [joinCode, setJoinCode] = useState("");
  const [questions, setQuestions] = useState([]);
  const [generated, setGenerated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState(null); // { success, message }

  const handleGenerate = async () => {
    if (!data.topic || !data.quantity || !data.difficulty) {
      setResponse({ success: false, message: "Please fill topic, quantity and difficulty" });
      return;
    }
    setLoading(true);
    setGenerated(false);
    try {
      const res = await quizApi.generateQuestions(data);
      console.log(res);
      setQuestions(res.data);
      setGenerated(true);
    } catch (err) {
      setResponse({ success: false, message: "Failed to generate questions, please try again" });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!schedule.date || !schedule.time || !schedule.duration) {
      setResponse({ success: false, message: "Please fill all schedule fields" });
      return;
    }
    if (!joinCode) {
      setResponse({ success: false, message: "Please enter a join code" });
      return;
    }
    setLoading(true);
    try {
      const res = await quizApi.createQuiz({ questions, schedule, joinCode });
      setResponse({ success: true, message: res.data.message });
    } catch (err) {
      setResponse({ success: false, message: "Failed to create quiz, please try again" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default", py: 4 }}>
      <Container maxWidth="xl">
        <Typography variant="h5" fontWeight={700} mb={3} color="text.primary">
          Generate Quiz with AI
        </Typography>

        {/* Input Row */}
        <Box display="flex" gap={2} flexWrap="wrap" mb={3}>
          <TextField
            label="Topic"
            placeholder="e.g. World War II"
            onChange={(e) => setData({ ...data, topic: e.target.value })}
            required
          />
          <TextField
            label="Quantity"
            placeholder="e.g. 10"
            type="number"
            onChange={(e) => setData({ ...data, quantity: e.target.value })}
            required
          />
          <TextField
            label="Difficulty"
            placeholder="1 = easy | 10 = hard"
            type="number"
            onChange={(e) => setData({ ...data, difficulty: e.target.value })}
            required
          />
          <TextField
            type="date"
            label="Date"
            slotProps={{ inputLabel: { shrink: true } }}
            onChange={(e) => setSchedule({ ...schedule, date: e.target.value })}
          />
          <TextField
            type="time"
            label="Time"
            slotProps={{ inputLabel: { shrink: true } }}
            onChange={(e) => setSchedule({ ...schedule, time: e.target.value })}
          />
          <TextField
            type="number"
            label="Duration (minutes)"
            onChange={(e) => setSchedule({ ...schedule, duration: e.target.value })}
          />
          <Button
            variant="contained"
            endIcon={<AutoAwesomeIcon />}
            onClick={handleGenerate}
            sx={{ fontWeight: 700, borderRadius: 3 }}
          >
            Generate
          </Button>
        </Box>

        {/* Generated Questions Table */}
        {generated && (
          <Box>
            <TableContainer sx={{ mb: 4, bgcolor: "background.paper", borderRadius: 3 }}>
              <Table stickyHeader size="small">
                <TableHead>
                  <TableRow>
                    {["S.No", "Question", "Option A", "Option B", "Option C", "Option D", "Correct"].map((h) => (
                      <TableCell key={h} sx={{ color: "text.secondary", fontWeight: 600 }}>{h}</TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {questions.map((q, index) => (
                    <TableRow key={index}>
                      <TableCell>{index + 1}</TableCell>
                      <TableCell>{q.question}</TableCell>
                      <TableCell>{q.optionA}</TableCell>
                      <TableCell>{q.optionB}</TableCell>
                      <TableCell>{q.optionC}</TableCell>
                      <TableCell>{q.optionD}</TableCell>
                      <TableCell>{q.correctOption}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>

            {/* Submit Row */}
            <Box display="flex" gap={2} alignItems="center">
              <TextField
                label="Join Code"
                placeholder="e.g. QUIZ01"
                slotProps={{ inputLabel: { shrink: true } }}
                onChange={(e) => setJoinCode(e.target.value)}
              />
              <Button
                variant="contained"
                endIcon={<SendIcon />}
                onClick={handleSubmit}
                sx={{ fontWeight: 700, borderRadius: 3 }}
              >
                Submit Quiz
              </Button>
            </Box>
          </Box>
        )}

        {/* Response */}
        {response && (
          <Alert
            severity={response.success ? "success" : "error"}
            sx={{ mt: 3 }}
            onClose={() => setResponse(null)}
          >
            {response.message}
          </Alert>
        )}
      </Container>

      <Backdrop open={loading} sx={{ zIndex: 999 }}>
        <CircularProgress color="primary" />
      </Backdrop>
    </Box>
  );
}