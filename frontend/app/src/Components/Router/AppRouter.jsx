import React from 'react'
import { Routes ,Route} from 'react-router-dom'
import Home from '../Home/Home'
import Auth from '../Authentication/Auth'
import CreateQuiz from '../CreateQuiz'
import UpcomingQuizzes from '../UpcomingQuizzes'
import AutoCreation from '../AutoCreation'
import ManualCreation from '../ManualCreation'
import PlayQuiz from '../PlayQuiz'
import Login from '../Authentication/Login'
import JoinQuiz from '../JoinQuiz'
export default function AppRouter({data})

 {
  return (
    <Routes>
        <Route path='/signup' element={<Auth data={data}/>}/>
        <Route path='/login' element={<Login/>}/>

        <Route path='/' element={<Home/>}/>
        <Route path='/createQuiz' element={<CreateQuiz/>}/>
        <Route path='/upcomingQuizzes' element={<UpcomingQuizzes/>}/>
        <Route path='/generateQuiz' element={<AutoCreation/>}/>
        <Route path='/makeQuiz' element={<ManualCreation/>}/>
        <Route path='/play/:code' element={<PlayQuiz/>}/>
        <Route path='/join' element={<JoinQuiz/>}/>



    </Routes>
  )
}
