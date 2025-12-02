import React, { useEffect, useState } from 'react'
import { Box, Card } from '@mui/material';
import { useParams } from 'react-router-dom';
import {Container} from '@mui/material';
import {Button} from '@mui/material';

export default function PlayQuiz() {
  const { code } = useParams()
  const [response, setResponse] = useState({})
  useEffect(() => {
    const url = `https://smart-quiz-xmzm.onrender.com/joinQuiz/${code}`;
    fetch(url, {
      method: "POST"
    }).then(pack =>
      pack.json())
      .then(data => {
        console.log(data)
        setResponse(data)
      }
      )
  }, [code])
  return (
    <Box
      display={"flex"}
      height={"100vh"}
      width={"100vw"}
      justifyContent={"center"}
      alignItems={"center"}
    >
      {!response.action && <h1>{response.message}</h1>}
      {response.action &&
        <div style={{ display: "flex", justifyContent: "center", padding: 20 }}>
          <Container
            maxWidth="sm"
            sx={{
              padding: 3,
              borderRadius: 3,
              boxShadow: 3,
              backgroundColor: "#fff",
              display: "flex",
              flexDirection: "column",
              gap: 3
            }}
          >
            {/* Question */}
            <div
              style={{
                fontSize: "1.3rem",
                fontWeight: 600,
                textAlign: "center"
              }}
            >
              {/* Replace this with dynamic question */}
              What is the capital of France?
            </div>

            {/* Options */}
            <div style={{ display: "flex", flexDirection: "column", gap: 1 }}>
              <Button
                variant="outlined"
                sx={{
                  paddingY: 2,
                  borderRadius: 2,
                  textTransform: "none",
                  fontSize: "1rem"
                }}
              >
                Option A
              </Button>

              <Button
                variant="outlined"
                sx={{
                  paddingY: 2,
                  borderRadius: 2,
                  textTransform: "none",
                  fontSize: "1rem"
                }}
              >
                Option B
              </Button>

              <Button
                variant="outlined"
                sx={{
                  paddingY: 2,
                  borderRadius: 2,
                  textTransform: "none",
                  fontSize: "1rem"
                }}
              >
                Option C
              </Button>

              <Button
                variant="outlined"
                sx={{
                  paddingY: 2,
                  borderRadius: 2,
                  textTransform: "none",
                  fontSize: "1rem"
                }}
              >
                Option D
              </Button>
            </div>

            {/* Next Button */}
            <Button
              variant="contained"
              sx={{
                marginTop: 1,
                paddingY: 1.5,
                borderRadius: 2,
                fontSize: "1rem",
                textTransform: "none"
              }}
            >
              Next
            </Button>
          </Container>
        </div>


      }
    </Box>
  )
}
