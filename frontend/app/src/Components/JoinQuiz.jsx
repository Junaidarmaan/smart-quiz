import React, { useState } from 'react'
import { TextField,Button ,Box} from '@mui/material'
import { useNavigate } from 'react-router-dom'
export default function JoinQuiz() {
    const navigate = useNavigate()
    const [code,setCode] = useState("")
  return (
    <div>
        <Box
        display={"flex"}
        gap={4}
        margin={"20px"}
        >
            <TextField
            label="join code"
            placeholder='enter quiz code to join'
            onChange={(e)=>{
                setCode(e.target.value)
            }}
            required
            />
            <Button
            variant='contained'
            color='primary'
            onClick={()=>navigate(`/play/${code}`)}
            >JOIN</Button>

        </Box>
    </div>
  )
}
