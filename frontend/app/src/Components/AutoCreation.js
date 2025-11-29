import { Button, Card, TextField } from '@mui/material'
import {Box} from '@mui/material';
import React, { useState } from 'react'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { Stack } from '@mui/material';
import { TableContainer, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';


export default function AutoCreation() {
    const divStyle = {
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        width: "100vw",

    }
    const [data, setData] = useState({})
    const [received, setReceived] = useState(false)
    const [response, setResponse] = useState({})
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
                <Button
                    variant='contained'
                    color='primary'
                    endIcon={<AutoAwesomeIcon />}
                    onClick={
                        () => {
                            const url = "https://smart-quiz-xmzm.onrender.com/gemini"
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
                            })
                        }
                    }
                >generate</Button>
            </Box>
            {received &&

                <>
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
                                            <TableCell>{index+1}</TableCell>
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

                </>
            }


        </div>
    )
}
