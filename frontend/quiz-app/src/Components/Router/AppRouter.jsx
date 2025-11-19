import React from 'react'
import { Routes ,Route} from 'react-router-dom'
import Home from '../Home/Home'
import Auth from '../Authentication/Auth'
import CreateQuiz from '../Home/CreateQuiz'
export default function AppRouter({data})
 {
  return (
    <Routes>
        <Route path='/signup' element={<Auth data={data}/>}/>
        <Route path='/' element={<Home/>}/>
        <Route path='/createQuiz' element={<CreateQuiz/>}/>


    </Routes>
  )
}
