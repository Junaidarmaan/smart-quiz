import { Box, Button, Card, dividerClasses, TextField } from '@mui/material'
import React, { useState } from 'react'
import SendIcon from '@mui/icons-material/Send';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { Stack, Paper } from '@mui/material';


export default function AutoCreation() {
    const divStyle = {
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        width: "100vw",
        justifyContent: "center",
        alignItems: "center",
        // backgroundColor:"#F9F8F6"

    }
    const [data,setData] = useState({})
    return (
        <div
            style={divStyle}
        >
            <Card
            raised
            sx={{padding:4, mt:2,backgroundColor:"#EFE9E3"}}
            
            >

                <Stack gap={3}>


                    <TextField onChange={(e)=>setData({...data,"topic":e.target.value})} variant='outlined' placeholder='topic name' label='Topic' required />
                    <TextField onChange={(e)=>setData({...data,"quantity":e.target.value})} variant='outlined' placeholder='number  of questions' label="Quantity" required />
                    <TextField onChange={(e)=>setData({...data,"difficulty":e.target.value})} variant='outlined' placeholder='1 = easy | 10 = hard' label="Difficulty" required />
                    <Button
                        variant='contained'
                        color='primary'
                        endIcon={<AutoAwesomeIcon />}
                        onClick={
                            ()=>{
                                const url = "https://smart-quiz-xmzm.onrender.com/gemini"
                                fetch(url,{
                                    method:"POST",
                                    headers:{
                                        'Content-Type' : 'application/json'
                                    },
                                    body:JSON.stringify(data)
                                }).then(response=>{
                                    response.json();
                                }).then(res=>{
                                    console.log(res)
                                })
                            }
                        }
                    >generate</Button>
                </Stack>
            </Card>

        </div>
    )
}
