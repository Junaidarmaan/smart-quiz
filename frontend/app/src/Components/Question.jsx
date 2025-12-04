import React from "react";
import { Box, Container, Button, Paper, Typography, Divider } from "@mui/material";

export default function Question({ data, onNext, flag,isCorrect }) {
    const [selectedOption, setSelectedOption] = React.useState(null);
    const [btnColor,setBtnColor] = React.useState("primary");
    return (
        <>
            {flag && <h1>quiz is over u can leave</h1>}
            {!flag &&
                <Box
                    sx={{
                        minHeight: "100vh",
                        width: "100%",
                        display: "flex",
                        background: "linear-gradient(to right, #ece9e6, #ffffff)",
                        padding: 2,
                        boxSizing: "border-box"
                    }}
                >
                    {/* Center Main Quiz */}
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
                                    variant="outlined"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    onClick={(e)=>{
                                        setSelectedOption(data.optionA)
                                    }}
                                    color ={selectedOption===data.optionA?"success":btnColor}
                                >
                                    {data.optionA}
                                </Button>

                                <Button
                                    variant="outlined"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    onClick={(e)=>{
                                        setSelectedOption(data.optionB)
                                    }}
                                    color ={selectedOption===data.optionB?"success":btnColor}

                                >
                                    {data.optionB}
                                </Button>

                                <Button
                                    variant="outlined"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    onClick={(e)=>{
                                        setSelectedOption(data.optionC)
                                    }}
                                    color ={selectedOption===data.optionC?"success":btnColor}

                                >
                                    {data.optionC}
                                </Button>

                                <Button
                                    variant="outlined"
                                    sx={{
                                        paddingY: 2,
                                        borderRadius: 3,
                                        textTransform: "none",
                                        fontSize: "1rem"
                                    }}
                                    onClick={(e)=>{
                                        setSelectedOption(data.optionD)

                                    }}
                                    color ={selectedOption===data.optionD?"success":btnColor}

                                >
                                    {data.optionD}
                                </Button>
                            </Box>

                            {/* Next Button */}
                            <Button
                                variant="contained"
                                fullWidth
                                sx={{
                                    paddingY: 1.8,
                                    borderRadius: 3,
                                    fontSize: "1.05rem",
                                    textTransform: "none"
                                }}
                                onClick={()=>{
                                    if(selectedOption===null){
                                        setBtnColor("error")
                                    }else{
                                        onNext()
                                        setSelectedOption(null)
                                        setBtnColor("primary")
                                    }
                                    isCorrect(selectedOption)
                                }}
                            >
                                Next
                            </Button>
                        </Container>
                    </Box>

                    {/* Right Side Leaderboard */}
                    <Box
                        sx={{
                            width: "260px",
                            paddingLeft: 2,
                            display: "flex",
                            alignItems: "stretch"
                        }}
                    >
                        <Paper
                            elevation={6}
                            sx={{
                                width: "100%",
                                borderRadius: 3,
                                padding: 2,
                                backgroundColor: "#fff",
                                display: "flex",
                                flexDirection: "column",
                                height: "96vh",
                                overflowY: "auto"
                            }}
                        >
                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700,
                                    textAlign: "center",
                                    marginBottom: 1
                                }}
                            >
                                Leaderboard
                            </Typography>

                            <Divider sx={{ marginBottom: 2 }} />

                            {/* Your dynamic users will be inserted here */}
                            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                                <Paper
                                    sx={{
                                        padding: 1.5,
                                        borderRadius: 2,
                                        backgroundColor: "#f7f7f7",
                                        boxShadow: 1
                                    }}
                                >
                                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                                        #1 Username
                                    </Typography>
                                    <Typography variant="body2" sx={{ opacity: 0.6 }}>
                                        Points / whatever
                                    </Typography>
                                </Paper>

                                <Paper
                                    sx={{
                                        padding: 1.5,
                                        borderRadius: 2,
                                        backgroundColor: "#f7f7f7",
                                        boxShadow: 1
                                    }}
                                >
                                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                                        #2 Username
                                    </Typography>
                                    <Typography variant="body2" sx={{ opacity: 0.6 }}>
                                        Points / whatever
                                    </Typography>
                                </Paper>

                                {/* You will dynamically map your list here */}
                            </Box>
                        </Paper>
                    </Box>
                </Box>
            }
        </>
    );
}
