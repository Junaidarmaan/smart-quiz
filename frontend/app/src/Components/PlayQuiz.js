import React, { useEffect, useState } from 'react'
import { Box,Card } from '@mui/material';
import { useParams } from 'react-router-dom';

export default function PlayQuiz() {
  const {code} = useParams()
  const [response,setResponse] = useState({})
    useEffect(()=>{
        const url = `https://smart-quiz-xmzm.onrender.com/joinQuiz/${code}`;
        fetch(url,{
          method:"POST"
        }).then(pack=>
          pack.json())
          .then(data=>{
            console.log(data)
            setResponse(data)
          }
          )
    },[code])
  return (
    <Box
    display={"flex"}
    height={"100vh"}
    width={"100vw"}
    justifyContent={"center"}
    alignItems={"center"}
    >
      <Card
      elevation={3}
      sx={{
        height:400,
        width:'max-content',
        padding:7,
        display:'flex',
        flexDirection:'column',
        justifyContent:'center',
        alignItems:'center',
        fontSize:20,
        bgcolor:'gray',
        color:'white',
        fontWeight:'bold'
      }}
      raised
      >
        <p>start time : {response.start}</p>
        <p>end time : {response.end}</p>

        <p>{response.message}</p>
      </Card>
    </Box>
  )
}
