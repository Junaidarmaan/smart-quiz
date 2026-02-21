import { Box } from '@mui/material';
import { useState, useEffect } from 'react'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
export default function UpcomingQuizzes() {
    const [quizzes, setQuizzes] = useState([]);
    useEffect(() => {
        // Fetch upcoming quizzes from the server or API
        const url = "http://localhost:8080/getUpcomingQuizzes";
        fetch(url)
            .then(res => res.json())
            .then((data) => {
                setQuizzes(data.data);
                console.log(data);
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
                            <TableCell>Join code</TableCell>
                            <TableCell>schedule</TableCell>
                            <TableCell>duration(in minutes)</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            quizzes.map((quiz, index) => (
                                <TableRow key={index}>
                                    <TableCell>{quiz.quizId}</TableCell>
                                    <TableCell>{quiz.joinCode}</TableCell>
                                    <TableCell>{quiz.schedule.dateTime}</TableCell>
                                    <TableCell>{quiz.schedule.duration}</TableCell>
                                </TableRow>
                            ))
                        }
                    </TableBody>
                </Table>
            </TableContainer>

        </Box>
    )
}
