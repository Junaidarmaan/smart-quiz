import React from 'react'
import { Button, Card } from '@mui/material'
import Stack from '@mui/material/Stack';
import { useState } from 'react';
import ManualCreation from './ManualCreation';
import { useNavigate } from 'react-router-dom';
export default function CreateQuiz() {
    const Navigate = useNavigate()
    return (
        <>

                <Stack spacing={3}
                    alignItems="center"
                    justifyContent="center"
                    height="90vh"
                >
                
                    <Button variant='contained' onClick={() => Navigate("/makeQuiz")}>Create Manually</Button>
                    <Button variant='contained' onClick={() => Navigate("/generateQuiz")}>Generate from AI</Button>
                </Stack>
            
        </>

    )
}