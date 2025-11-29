import React, { useState } from 'react'
import { InputLabel, FormControl, TextField, Select, MenuItem } from '@mui/material';
import Box from '@mui/material/Box';
import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SendIcon from '@mui/icons-material/Send';
import { DeleteForeverOutlined } from '@mui/icons-material';
import { TableContainer, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import Backdrop from '@mui/material/Backdrop';
import CircularProgress from "@mui/material/CircularProgress";


export default function ManualCreation() {
    const [joinCode, setJoinCode] = useState("");
    const [questionId, setQuestionId] = React.useState(1);
    const [questions, setQuestions] = React.useState([]);
    const [responseCard,setResponseCard] = useState("")
    const [schedule, setSchedule] = React.useState({
        date: null,
        time: null
    });
    const [currentQuestion, setCurrentQuestion] = React.useState({});
    const [requestLoading, setRequestLoading] = useState(false)
    return (
        <div>
            <Box
                margin="20px"
                display="flex"
                gap={2}
            >
                <TextField value={currentQuestion.question || ""} variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, question: e.target.value })} size='small' label="enter the question" required />
                <TextField value={currentQuestion.optionA || ""} variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionA: e.target.value })} size='small' label="Enter the option A" />
                <TextField value={currentQuestion.optionB || ""} variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionB: e.target.value })} size='small' label="Enter the option B" />
                <TextField value={currentQuestion.optionC || ""} variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionC: e.target.value })} size='small' label="Enter the option C" />
                <TextField value={currentQuestion.optionD || ""} variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionD: e.target.value })} size='small' label="Enter the option D" />
                <FormControl >
                    <InputLabel id="Correct_Answer">Correct Answer</InputLabel>
                    < Select
                        size='small' sx={{ width: "200px" }}
                        labelId="Correct_Answer"
                        onChange={(e) => setCurrentQuestion({ ...currentQuestion, correctOption: e.target.value })}
                        label="Correct Answer"
                        value={currentQuestion.correctOption || ''}
                    >
                        <MenuItem value="A">A</MenuItem>
                        <MenuItem value="B">B</MenuItem>
                        <MenuItem value="C">C</MenuItem>
                        <MenuItem value="D">D</MenuItem>
                    </Select>
                </FormControl>

                <Button
                    variant='contained'
                    color='secondary'
                    endIcon={<AddIcon />}
                    onClick={
                        () => {
                            const questionWithId = { ...currentQuestion, id: questionId };
                            setQuestions([...questions, questionWithId]);
                            setQuestionId(questionId + 1);
                            setCurrentQuestion({});
                        }
                    }
                >Add</Button>



            </Box>

            <TableContainer >
                <Table >
                    <TableHead>
                        <TableRow>
                            <TableCell>Id</TableCell>
                            <TableCell>Question</TableCell>
                            <TableCell>Option A</TableCell>
                            <TableCell>Option B</TableCell>
                            <TableCell>Option C</TableCell>
                            <TableCell>Option D</TableCell>
                            <TableCell>Correct Option</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            questions.map((q, index) => (
                                <TableRow key={index}>
                                    <TableCell>{q.id}</TableCell>
                                    <TableCell>{q.question}</TableCell>
                                    <TableCell>{q.optionA}</TableCell>
                                    <TableCell>{q.optionB}</TableCell>
                                    <TableCell>{q.optionC}</TableCell>
                                    <TableCell>{q.optionD}</TableCell>
                                    <TableCell>{q.correctOption}</TableCell>
                                    <TableCell>
                                        <Button variant='outlined'
                                            color='error'
                                            endIcon={<DeleteForeverOutlined />}
                                            onClick={
                                                () => {
                                                    setQuestions(questions.filter(el => q.id !== el.id));
                                                }
                                            }
                                        >Delete</Button>
                                    </TableCell>
                                </TableRow>
                            ))

                        }
                    </TableBody>
                </Table>
            </TableContainer>

            <FormControl
                fullWidth
                sx={{
                    marginTop: 10,
                    padding: "20px 5px",
                }}
            >
                <InputLabel size='large' shrink>Select Schedule</InputLabel>

                <Box
                    display="flex"
                    gap={2}
                >

                    <TextField
                        type='date'
                        variant='outlined'
                        label="Select Schedule Date"
                        // InputLabelProps={{ shrink: true }} -- old way
                        slotProps={{

                            inputLabel: {
                                shrink: true
                            }
                        }
                        }
                        onChange={(e) => {
                            setSchedule({ ...schedule, date: e.target.value });
                        }}
                        required
                    />

                    <TextField
                        type='time'
                        variant='outlined'
                        label="Select Schedule time"
                        // InputLabelProps={{ shrink: true }} -- old way
                        slotProps={{

                            inputLabel: {
                                shrink: true
                            }
                        }
                        }
                        onChange={(e) => {
                            setSchedule({ ...schedule, time: e.target.value });
                        }}
                        required
                    />


                    <TextField
                        type='number'
                        variant='outlined'
                        label='duration'
                        placeholder='in minutes'
                        slotProps={{
                            inputLabel: {
                                shrink: true
                            }
                        }}
                        onChange={(e) => {
                            setSchedule({ ...schedule, duration: e.target.value })
                        }} 


                    />

                    <TextField
                        type='text'
                        variant='outlined'
                        label='quiz id'
                        placeholder='will be used to join quiz'
                        slotProps={{
                            inputLabel: {
                                shrink: true
                            }
                        }}
                        onChange={(e) => {
                            setJoinCode(e.target.value)
                        }}


                    />

                    <Button
                        variant='contained'
                        color='primary'
                        endIcon={<SendIcon />}
                        onClick={
                            () => {
                                setRequestLoading(true)
                                scheduleQuizRequest({
                                    questions: questions,
                                    schedule: schedule,
                                    joinCode: joinCode
                                }).then(result=>{
                                    setResponseCard(result.data)
                                }).finally(() => {
                                    setRequestLoading(false)
                                });
                            }
                        }
                    >Submit Quiz</Button>
                </Box>
            </FormControl>

            
            
            
            <Button
            variant='outlined'
            color='secondary'
            sx={{
                margin:"0 auto",
                display:"block"
            }}
            
            >
                {responseCard}
            </Button>
            <Backdrop
                open={requestLoading}
            >
                <CircularProgress />

            </Backdrop> 
        </div >
    )
}

function scheduleQuizRequest(data) {
    const url = "https://smart-quiz-xmzm.onrender.com/createQuiz";


    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.json())
        .then(result => {
            console.log('backed reply:', result);
            return result
        }).catch(err => {
            console.log("error ocured : ", err)
        })


}