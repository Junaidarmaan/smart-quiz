import { Button, TextField } from '@mui/material'
import { Box } from '@mui/material';
import React, { useState } from 'react'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { TableContainer, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import Backdrop from '@mui/material/Backdrop';
import './LoaderAnimation.css'



export default function AutoCreation() {
    const divStyle = {
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        width: "100vw",

    }
    const [schedule, setSchedule] = useState({})
    const [data, setData] = useState({})
    const [received, setReceived] = useState(false)
    const [response, setResponse] = useState([])
    const [loading, setLoading] = useState(false)
    const [joinCode, setJoinCode] = useState("")
    return (
        <div
            style={divStyle}
        >
            <Box
                display={"flex"}
                gap={3}
                margin={"20px"}
            >



                <TextField onChange={(e) => setData({ ...data, "topic": e.target.value })} variant='outlined' placeholder='topic name' label='Topic' required />
                <TextField onChange={(e) => setData({ ...data, "quantity": e.target.value })} variant='outlined' placeholder='number  of questions' label="Quantity" required />
                <TextField onChange={(e) => setData({ ...data, "difficulty": e.target.value })} variant='outlined' placeholder='1 = easy | 10 = hard' label="Difficulty" required />


                <TextField type='time'
                    variant='outlined'
                    label='time'
                    sx={{
                        width:200
                    }}
                    slotProps={{
                        inputLabel: {
                            shrink: true
                        }
                    }}
                    onChange={(e) => {
                        setSchedule({ ...schedule, time: e.target.value })
                    }}
                />
                <TextField type='date'
                    variant='outlined'
                    label='date'
                    slotProps={{
                        inputLabel: {
                            shrink: true
                        }
                    }}
                    onChange={(e) => {
                        setSchedule({ ...schedule, date: e.target.value })
                    }}
                />
                <TextField type='number'
                    variant='outlined'
                    label='duration'
                    placeholder='duration in minutes'
                    onChange={(e) => {
                        setSchedule({ ...schedule, duration: e.target.value })
                    }}
                />
                <Button
                    variant='contained'
                    color='primary'
                    endIcon={<AutoAwesomeIcon />}
                    onClick={
                        () => {
                            setReceived(false)
                            const url = "http://localhost:8080/gemini"
                            setLoading(true)
                            fetch(url, {
                                method: "POST",
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(data)
                            }).then(pack => {
                                return pack.json();

                            }).then(obj => {
                                console.log(obj)
                                setReceived(true)
                                setResponse(obj)
                            }).finally(() => setLoading(false))
                        }
                    }
                >generate</Button>
            </Box>
            {received &&

                <Box
                    display={"flex"}
                    flexDirection={"column"}
                    alignItems={"center"}
                    gap={3}
                    margin={"20px"}
                >
                    <TableContainer>
                        <Table
                            stickyHeader
                            size='small'
                        >
                            <TableHead>
                                <TableRow>
                                    <TableCell>S.NO</TableCell>
                                    <TableCell>Question</TableCell>
                                    <TableCell>option-A</TableCell>
                                    <TableCell>option-B</TableCell>
                                    <TableCell>option-C</TableCell>
                                    <TableCell>option-D</TableCell>
                                    <TableCell>correctOption</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {
                                    response.map((q, index) => (

                                        <TableRow key={index}>
                                            <TableCell>{index + 1}</TableCell>
                                            <TableCell>{q.question}</TableCell>
                                            <TableCell>{q.optionA}</TableCell>
                                            <TableCell>{q.optionB}</TableCell>
                                            <TableCell>{q.optionC}</TableCell>
                                            <TableCell>{q.optionD}</TableCell>
                                            <TableCell>{q.correctOption}</TableCell>
                                        </TableRow>

                                    ))
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TextField type='text'
                        variant='outlined'
                        label='quiz id'
                        placeholder='will be used to join quiz'

                        onChange={(e) => {
                            setJoinCode(e.target.value)
                        }}
                    />
                    <Button
                        variant='contained'
                        color='primary'
                        onClick={() => {
                            setLoading(true)
                            const url = "http://localhost:8080/createQuiz";
                            const request = {

                                questions: response,
                                schedule: schedule,
                                joinCode: joinCode
                            }
                            return fetch(url, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },

                                body: JSON.stringify(request)
                            }).then(response => response.json())
                                .then(result => {
                                    console.log('backed reply:', result);
                                    return result
                                }).catch(err => {
                                    console.log("error ocured : ", err)
                                }).finally(()=>setLoading(false))


                        }}
                    >submit</Button>
                </Box>
            }

            <Backdrop
                open={loading}
            >
                <span class="loader"></span>
            </Backdrop>
        </div>
    )
}
