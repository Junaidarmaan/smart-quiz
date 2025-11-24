import React from 'react'
import { Button, Card } from '@mui/material'
import Stack from '@mui/material/Stack';
import { useState } from 'react';
import ManualCreation from './ManualCreation';
export default function CreateQuiz() {
    const [mode, setMode] = useState(null);
    return (
        <>

            {mode === null && (
                <Stack spacing={3}
                    alignItems="center"
                    justifyContent="center"
                    height="90vh"
                >
                    <Button variant='contained' onClick={() => setMode("manual")}>Create Manually</Button>
                    <Button variant='contained' onClick={() => setMode("auto")}>Generate from AI</Button>
                </Stack>
            )}
            {mode === "manual" && <ManualCreation />}
            {mode === "auto" && <Card>Generate Quiz from AI Component</Card>}
        </>

    )
}