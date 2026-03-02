import { useState } from 'react';
import {
  Box, Button, Container, FormControl, InputLabel,
  MenuItem, Select, Stack, TextField, Typography,
  CircularProgress, Backdrop, Alert
} from '@mui/material';
import { Add, Send, DeleteForeverOutlined } from '@mui/icons-material';
import {
  TableContainer, Table, TableBody, TableCell,
  TableHead, TableRow
} from '@mui/material';
import { quizApi } from '../../api/quizApi';

export default function ManualCreation() {
  const [joinCode, setJoinCode] = useState("");
  const [questions, setQuestions] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState({});
  const [schedule, setSchedule] = useState({ date: null, time: null, duration: null });
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState(null); // { success, message }

  const handleAddQuestion = () => {
    const { question, optionA, optionB, optionC, optionD, correctOption } = currentQuestion;
    if (!question || !optionA || !optionB || !optionC || !optionD || !correctOption) return;

    setQuestions([...questions, { ...currentQuestion, id: questions.length + 1 }]);
    setCurrentQuestion({});
  };

  const handleSubmit = async () => {
    if (questions.length === 0) {
      setResponse({ success: false, message: "Please add at least one question" });
      return;
    }
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
          Create Quiz Manually
        </Typography>

        {/* Question Input Row */}
        <Box display="flex" gap={2} flexWrap="wrap" mb={3}>
          <TextField
            value={currentQuestion.question || ""}
            onChange={(e) => setCurrentQuestion({ ...currentQuestion, question: e.target.value })}
            size="small"
            label="Question"
            required
          />
          {["A", "B", "C", "D"].map((opt) => (
            <TextField
              key={opt}
              value={currentQuestion[`option${opt}`] || ""}
              onChange={(e) => setCurrentQuestion({ ...currentQuestion, [`option${opt}`]: e.target.value })}
              size="small"
              label={`Option ${opt}`}
            />
          ))}
          <FormControl size="small" sx={{ minWidth: 160 }}>
            <InputLabel>Correct Answer</InputLabel>
            <Select
              value={currentQuestion.correctOption || ""}
              onChange={(e) => setCurrentQuestion({ ...currentQuestion, correctOption: e.target.value })}
              label="Correct Answer"
            >
              {["A", "B", "C", "D"].map((opt) => (
                <MenuItem key={opt} value={opt}>{opt}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button
            variant="contained"
            endIcon={<Add />}
            onClick={handleAddQuestion}
            sx={{ fontWeight: 700, borderRadius: 3 }}
          >
            Add
          </Button>
        </Box>

        {/* Questions Table */}
        {questions.length > 0 && (
          <TableContainer sx={{ mb: 4, bgcolor: "background.paper", borderRadius: 3 }}>
            <Table>
              <TableHead>
                <TableRow>
                  {["Id", "Question", "Option A", "Option B", "Option C", "Option D", "Correct", "Actions"].map((h) => (
                    <TableCell key={h} sx={{ color: "text.secondary", fontWeight: 600 }}>{h}</TableCell>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {questions.map((q) => (
                  <TableRow key={q.id}>
                    <TableCell>{q.id}</TableCell>
                    <TableCell>{q.question}</TableCell>
                    <TableCell>{q.optionA}</TableCell>
                    <TableCell>{q.optionB}</TableCell>
                    <TableCell>{q.optionC}</TableCell>
                    <TableCell>{q.optionD}</TableCell>
                    <TableCell>{q.correctOption}</TableCell>
                    <TableCell>
                      <Button
                        variant="outlined"
                        color="error"
                        size="small"
                        endIcon={<DeleteForeverOutlined />}
                        onClick={() => setQuestions(questions.filter((el) => el.id !== q.id))}
                      >
                        Delete
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}

        {/* Schedule Row */}
        <Box display="flex" gap={2} flexWrap="wrap" alignItems="center">
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
            slotProps={{ inputLabel: { shrink: true } }}
            onChange={(e) => setSchedule({ ...schedule, duration: e.target.value })}
          />
          <TextField
            label="Join Code"
            placeholder="e.g. QUIZ01"
            slotProps={{ inputLabel: { shrink: true } }}
            onChange={(e) => setJoinCode(e.target.value)}
          />
          <Button
            variant="contained"
            endIcon={<Send />}
            onClick={handleSubmit}
            sx={{ fontWeight: 700, borderRadius: 3 }}
          >
            Submit Quiz
          </Button>
        </Box>

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