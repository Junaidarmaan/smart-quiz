import React, { useEffect, useState } from 'react'
import { Box, Card } from '@mui/material';
import { useParams } from 'react-router-dom';
import { Container } from '@mui/material';
import { Button } from '@mui/material';
import Question from './Question';

export default function PlayQuiz() {
  const { code } = useParams()
  const [response, setResponse] = useState({})
  const [requestStatus,setRequestStatus] = useState(false)
  const [curQuestion,setCurQuestion] = useState(0);
  const [finished,setFinished] = useState(false)
  useEffect(() => {
    const url = `https://smart-quiz-xmzm.onrender.com/joinQuiz/${code}`;
    fetch(url, {
      method: "POST"
    }).then(pack =>
      pack.json())
      .then(data => {
        console.log(data)
        setResponse(data)
        setRequestStatus(true)
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
      {requestStatus &&
          <Question data={response.data.questions[curQuestion]} 
            onNext={()=>{
              let n = response.data.questions.length;
              if(curQuestion == n-1){
                setFinished(true)
              }else{
                setCurQuestion(curQuestion+1)
              }
            }}          
            flag = {finished}
          />
      }
    </Box>
  )
}
