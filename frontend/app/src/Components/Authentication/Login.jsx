import React from 'react'
import { useEffect } from 'react';


export default function Login() {
    const handleGoogleResponse = (response)=>{
        console.log("hadling google response"); 
        console.log("this is credential " + response.credential);
        const url = "http://localhost:8080/verifyToken";
        const tokenData = {
            idToken: response.credential
        }
        fetch(url,{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(tokenData)
        }).then(res=>res.json())
        .then(result=>{
            console.log("server response for token verification is : ");
            console.log(result);
            if(result.status){
                sessionStorage.setItem("userName",result.data.name);
                window.location.href="/home"
            }
        })
    }
    useEffect(() => {
        /* global google */
        const cid=  "266908230365-8tol029060o3i6aj02kac64pl9s6ult1.apps.googleusercontent.com"
        google.accounts.id.initialize({
            client_id: cid,
            callback: handleGoogleResponse,
        });

        google.accounts.id.renderButton(
            document.getElementById("google-btn"),
            { theme: "outline", size: "large" }
        );
    }, []);

    return (
        <div>
            <h1>login page</h1>
            <div id="google-btn"></div>

        </div>
    )
}
