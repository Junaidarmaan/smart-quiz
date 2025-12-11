import React, { useState } from "react";
import Backdrop from '@mui/material/Backdrop';
import './LoaderAnimation.css';
import { Box, Container, Button, Typography } from "@mui/material";
import Live from "./Live";
export default function Question({ data, onNext, flag, isCorrect, quizId }) {
    const [errorOption, setErrorOption] = useState()
    const [correctOpt, setCorrectOpt] = useState(null)
    const [loading, setLoading] = useState(false)
    const [correct, setCorrect] = useState(0)
    const [effect, setEffet] = useState("")
    const handleOptionClick = async (cur) => {
        setLoading(true)
        const result = await isCorrect(cur)
        if (result) {
            setCorrectOpt(cur)
            setEffet("correct")
            setCorrect(correct + 1)
            const profile = {
                userName: sessionStorage.getItem("userName"),
                quizId: quizId,
                score: correct
            }
            console.log("sent request to increae score")
            Live.send("/app/updateScore", profile)
           

        } else {
            setErrorOption(cur)
            setEffet("wrong")
        }

        setTimeout(() => {
            setCorrectOpt(null)
            setErrorOption(null)
            onNext()
            setLoading(false)
            setEffet("")
        }, 500)

    }
    return (
        <>
            {flag && <h1>quiz is over. your score is {correct}</h1>}
            {!flag &&
                <Box
                    sx={{
                        minHeight: "100vh",
                        width: "100%",
                        display: "flex",
                        background: "linear-gradient(to right, #ece9e6, #ffffff)",
                        padding: 5,
                        boxSizing: "border-box"
                    }}
                >
                    {/* Center Main Quiz */}
                    <Button
                        variant="contained"
                        color="secondary"
                        sx={{
                            height: 100
                        }}
                    >SCORE : {correct}</Button>
                    <Box
                        sx={{
                            flex: 1,
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            padding: 2
                        }}
                    >
                        <Container
                            maxWidth="sm"
                            sx={{
                                padding: 4,
                                borderRadius: 4,
                                boxShadow: 5,
                                backgroundColor: "#fff",
                                display: "flex",
                                flexDirection: "column",
                                gap: 3
                            }}
                        >
                            {/* Question */}
                            <Typography
                                variant="h5"
                                sx={{
                                    fontWeight: 600,
                                    textAlign: "center",
                                    lineHeight: 1.4
                                }}
                            >
                                {data.question}
                            </Typography>

                            {/* Options */}
                            <Box sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                                <Button
                                    variant="contained"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    color={correctOpt === "A" ? "success" : errorOption === "A" ? "error" : "primary"}
                                    onClick={() => handleOptionClick("A")}
                                >
                                    {data.optionA}
                                </Button>

                                <Button
                                    variant="contained"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    color={correctOpt === "B" ? "success" : errorOption === "B" ? "error" : "primary"}

                                    onClick={() => handleOptionClick("B")}


                                >
                                    {data.optionB}
                                </Button>

                                <Button
                                    variant="contained"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    color={correctOpt === "C" ? "success" : errorOption === "C" ? "error" : "primary"}


                                    onClick={() => handleOptionClick("C")}


                                >
                                    {data.optionC}
                                </Button>

                                <Button
                                    variant="contained"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    color={correctOpt === "D" ? "success" : errorOption === "D" ? "error" : "primary"}


                                    onClick={() => handleOptionClick("D")}


                                >
                                    {data.optionD}
                                </Button>
                            </Box>

                        </Container>
                    </Box>

                    {/* Right Side Leaderboard */}

                </Box>
            }
            <Backdrop
                open={loading}

            >
                <span className={effect}></span>
            </Backdrop>
        </>
    );
}

