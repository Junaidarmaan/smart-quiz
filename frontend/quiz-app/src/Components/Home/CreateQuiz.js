    import { Button } from '@mui/material'
    import React, { useState } from 'react'
    import TextField from '@mui/material/TextField'
    import Select from '@mui/material/Select'
    import MenuItem from '@mui/material/MenuItem'
    import Table from "@mui/material/Table";
    import TableHead from "@mui/material/TableHead";
    import TableRow from "@mui/material/TableRow";
    import TableCell from "@mui/material/TableCell";

    export default function CreateQuiz() {
        const [mode, setMode] = useState(null)
        const [questions, setQuestions] = useState([])
        const [question,setQuestion] = useState({
            q:"",
            a:"",
            b:"",
            c:"",
            d:"",
            correct:""
        })
        return (
            <div className="create-quiz">

                {/* option selection */}
                {mode === null && (
                    <div className="quiz-type">
                        <button onClick={() => setMode("manual")}>Manual Question Creation</button>
                        <button onClick={() => setMode("auto")}>Automatic Generation</button>
                    </div>
                )}

                {/* manual question creation */}
                {mode === "manual" && (
                    
                    <div className="manual-section">
                        <h2>Manual Question Creation</h2>
                        <TextField variant='outlined' size='small' placeholder='Enter question' onChange={(e)=>{setQuestion({...question,q:e.target.value})}}/>
                        <TextField variant='outlined' size='small' placeholder='Option A' onChange={(e)=>{setQuestion({...question,a:e.target.value})}}/>
                        <TextField variant='outlined' size='small' placeholder='Option B' onChange={(e)=>{setQuestion({...question,b:e.target.value})}}/>
                        <TextField variant='outlined' size='small' placeholder='Option C' onChange={(e)=>{setQuestion({...question,c:e.target.value})}}/>
                        <TextField variant='outlined' size='small' placeholder='Option D' onChange={(e)=>{setQuestion({...question,d:e.target.value})}}/>

                        <Select label='Correct Option' value={"placeholder"} size='small' onChange={(e)=>{setQuestion({...question,correct:e.target.value})}}>
                            <MenuItem value='placeholder' disabled>Correct Option</MenuItem>
                            <MenuItem value='a'>Option A</MenuItem>
                            <MenuItem value='b'>Option B</MenuItem>
                            <MenuItem value='c'>Option C</MenuItem>
                            <MenuItem value='d'>Option D</MenuItem>

                        </Select>
                        <Button variant='contained' color='secondary' onClick={()=>{setQuestions([...questions,question])}}>add</Button> <br />
                        <Button variant='contained' color='primary'>submit</Button>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Question</TableCell>
                                    <TableCell>Option A</TableCell>
                                    <TableCell>Option B</TableCell>
                                    <TableCell>Option C</TableCell>
                                    <TableCell>Option D</TableCell>
                                    <TableCell>Correct Option</TableCell>
                                </TableRow>
                            </TableHead>
                            {
                                questions.map(el=>{
                                    return(
                                        <TableRow>
                                            <TableCell>{el.q}</TableCell>
                                            <TableCell>{el.a}</TableCell>
                                            <TableCell>{el.b}</TableCell>
                                            <TableCell>{el.c}</TableCell>
                                            <TableCell>{el.d}</TableCell>
                                            <TableCell>{el.correct}</TableCell>
                                        </TableRow>
                                    )
                                })
                            }                            
                        </Table>
                    </div>

                )}

                {/* automatic question generation */}
                {mode === "auto" && (
                    <div className="auto-section">
                        <h2>Automatic Quiz Generation</h2>

                        <input type="text" placeholder="Enter topic" />
                        <input type="number" placeholder="No. of questions" />

                        <select>
                            <option value="">Difficulty Level</option>
                            <option value="easy">Easy</option>
                            <option value="medium">Medium</option>
                            <option value="hard">Hard</option>
                        </select>

                        <button>Generate Quiz</button>
                    </div>
                )}

            </div>

        )
    }
