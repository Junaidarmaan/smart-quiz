import React, { useState } from 'react'
import './Auth.css'
import { useNavigate } from 'react-router-dom';
export default function Auth({ data }) {
  const [userForm, setUserFrom] = useState({
    userName: "",
    email: "",
    userPassword: "",
    role: "student",
    message:""
  });
  const navigate = useNavigate()
  return (
    <div className="container">
      <form onSubmit={handleSubmit}>
        <input type="text" id='userName' placeholder='enter user name' onChange={(e) => setUserFrom({ ...userForm, userName: e.target.value })} required />
        <input type="email" id='email' placeholder='enter email' onChange={(e) => setUserFrom({ ...userForm, email: e.target.value })} required />
        <input type="password" id='password' placeholder='enter password' onChange={(e) => setUserFrom({ ...userForm, userPassword: e.target.value })} required />

        <button className='submit' onClick={validateSignUp}>{data.login ? 'login' : 'signup'}</button>
        <h3 className='response'>{userForm.message}</h3>
      </form>
    </div>
  )

  function validateSignUp() {
    console.log(userForm);
    fetch("http://localhost:8080/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(userForm)
    }).then(res => {
      return res.json();
    }).then(result => {
      console.log("server response is : ")
      console.log(result)
      if(result.status){
        navigate("/home")
      }else{
        setUserFrom({...userForm,message:result.message})
        
      }
    })



  }
  function handleSubmit(e) {
    e.preventDefault();
  }

}

