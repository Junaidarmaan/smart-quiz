import React from 'react'
import { useEffect } from 'react';

export default function Login() {
    const handleGoogleResponse = ()=>{
        console.log("handled googgle respose");
    }
    useEffect(() => {
        /* global google */
        google.accounts.id.initialize({
            client_id: "266908230365-8tol029060o3i6aj02kac64pl9s6ult1.apps.googleusercontent.com",
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
