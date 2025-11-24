import React, { use } from 'react'
import { useState,useEffect } from 'react'
export default function UpcomingQuizzes() {
    useEffect(() => {
        // Fetch upcoming quizzes from the server or API
        const url = "https://smart-quiz-xmzm.onrender.com/"
        fetch(url).then(res=>res.json()).then(data=>console.log(data));
    }, []);
  return (
    <div>UpcomingQuizzes</div>
  )
}
