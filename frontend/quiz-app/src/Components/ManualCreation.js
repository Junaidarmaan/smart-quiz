import React from 'react'
import { InputLabel, labelId, FormControl, TextField, Select, MenuItem } from '@mui/material';
import Box from '@mui/material/Box';
import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { DeleteForeverOutlined } from '@mui/icons-material';
import { TableContainer, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

export default function ManualCreation() {
    const [questions, setQuestions] = React.useState([]);
    const [currentQuestion, setCurrentQuestion] = React.useState();
    return (
        <div>
            <Box
                margin="20px"
                display="flex"
                gap={2}
            >
                <TextField variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, question: e.target.value })} size='small' label="enter the question" required/>
                <TextField variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionA: e.target.value })} size='small' label="Enter the option A" />
                <TextField variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionB: e.target.value })} size='small' label="Enter the option B" />
                <TextField variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionC: e.target.value })} size='small' label="Enter the option C" />
                <TextField variant='outlined' onChange={(e) => setCurrentQuestion({ ...currentQuestion, optionD: e.target.value })} size='small' label="Enter the option D" />
                <FormControl >
                    <InputLabel id="Correct_Answer">Correct Answer</InputLabel>
                    < Select 
                    size='small' sx={{width:"200px"}}
                    labelId="Correct_Answer"  
                    onChange={(e) => setCurrentQuestion({ ...currentQuestion, correctOption: e.target.value })}
                    label="Correct Answer"
                    >
                        <MenuItem value="A">A</MenuItem>
                        <MenuItem value="B">B</MenuItem>
                        <MenuItem value="C">C</MenuItem>
                        <MenuItem value="D">D</MenuItem>
                    </Select>
                    </FormControl>
                    <Button variant='contained' onClick={() => { setQuestions([...questions, currentQuestion]) }} endIcon={<AddIcon />}>Add</Button>
            </Box>

            <TableContainer >
                <Table >    
                    <TableHead>
                        <TableCell>Question</TableCell>
                        <TableCell>Option A</TableCell>
                        <TableCell>Option B</TableCell>
                        <TableCell>Option C</TableCell>
                        <TableCell>Option D</TableCell>
                        <TableCell>Correct Option</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableHead>
                    <TableBody>
                        {
                            questions.map((q) => (
                                <TableRow>
                                    <TableCell>{q.question}</TableCell>
                                    <TableCell>{q.optionA}</TableCell>
                                    <TableCell>{q.optionB}</TableCell>
                                    <TableCell>{q.optionC}</TableCell>
                                    <TableCell>{q.optionD}</TableCell>
                                    <TableCell>{q.correctOption}</TableCell>
                                    <TableCell>
                                        <Button variant='outlined' color='error' endIcon={<DeleteForeverOutlined />}>Delete</Button>
                                    </TableCell>
                                </TableRow>
                            ))


                        }
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    )
}
