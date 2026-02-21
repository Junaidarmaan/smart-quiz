import { Navigate, Routes, Route } from "react-router-dom";
import ProtectedRoute from "../routes/ProtectedRoute";

import Login from "../pages/auth/Login";
import Signup from "../pages/auth/Signup";
import Dashboard from "../pages/dashboard/Dashboard";
import TeddyAssistant from "../components/assistant/TeddyAssistant";
import Home from "../components/QuizComponents/pages/Home";
import CreateQuiz from "../components/QuizComponents/pages/CreateQuiz";
import UpcomingQuizzes from "../components/QuizComponents/pages/UpcomingQuizzes";
import ManualCreation from "../components/QuizComponents/pages/ManualCreation";
import LeaderBoard from "../components/QuizComponents/components/LeaderBoard";
import AutoCreation from "../components/QuizComponents/pages/AutoCreation";
import PlayQuiz from "../components/QuizComponents/pages/PlayQuiz";
import JoinQuiz from "../components/QuizComponents/pages/JoinQuiz";
export default function AppRoutes() {
  return (
    <Routes>
      {/* Default redirect */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Auth pages */}
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/teddy" element={<TeddyAssistant />} />
      <Route path="/home" element={<Home />} />
      <Route path='/createQuiz' element={<CreateQuiz />} />
      <Route path='/upcomingQuizzes' element={<UpcomingQuizzes />} />
      <Route path='/generateQuiz' element={<AutoCreation />} />
      <Route path='/makeQuiz' element={<ManualCreation />} />
      <Route path='/play/:code' element={<PlayQuiz />} />
      <Route path='/join' element={<JoinQuiz />} />
      <Route path='/leaderBoard' element={<LeaderBoard />} />



      {/* Protected pages */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      {/* 404 fallback */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
