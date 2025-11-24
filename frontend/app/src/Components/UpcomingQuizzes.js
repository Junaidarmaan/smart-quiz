import { Box } from '@mui/material';
import React, { use } from 'react'
import { useState, useEffect } from 'react'
import {Table,TableBody,TableCell,TableContainer,TableHead,TableRow} from '@mui/material';
export default function UpcomingQuizzes() {
    const [quizzes, setQuizzes] = useState([]);
    useEffect(() => {
        // Fetch upcoming quizzes from the server or API
        const url = "https://smart-quiz-xmzm.onrender.com/getUpcomingQuizzes";
        fetch(url)
            .then(res => res.json())
            .then((data) => {
                setQuizzes(data.data);
            }
            );
    }, []);
    return (
        <Box>
            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>S.NO</TableCell>
                            <TableCell>schedule</TableCell>
                            <Tablecell>duration(in minutes)</Tablecell>
                        </TableRow>
                        <TableBody>
                            {
                                quizzes.map((quiz,index)=>{
                                    return(
                                    <TableRow key={index}>
                                        <TableCell>{quiz.id}</TableCell>
                                        <TableCell>{quiz.schedule}</TableCell>
                                        <TableCell>{quiz.duration}</TableCell>
                                    </TableRow>
                                    )
                                })
                            }
                        </TableBody>
                    </TableHead>
                </Table>
            </TableContainer>

        </Box>
    )
}
