import { Navigate, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import Login from "../pages/auth/Login";
import Signup from "../pages/auth/Signup";
import Dashboard from "../pages/dashboard/Dashboard";
import CreateQuiz from "../pages/quiz/CreateQuiz";
import UpcomingQuizzes from "../pages/quiz/UpcomingQuizzes";
import ManualCreation from "../pages/quiz/ManualCreation";
import AutoCreation from "../pages/quiz/AutoCreation";
import PlayQuiz from "../pages/quiz/PlayQuiz";
import JoinQuiz from "../pages/quiz/JoinQuiz";

export default function AppRoutes() {
  return (
    <Routes>
      {/* Default redirect */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Public pages */}
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />

      {/* Protected pages */}
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/createQuiz" element={<ProtectedRoute><CreateQuiz /></ProtectedRoute>} />
      <Route path="/upcomingQuizzes" element={<ProtectedRoute><UpcomingQuizzes /></ProtectedRoute>} />
      <Route path="/generateQuiz" element={<ProtectedRoute><AutoCreation /></ProtectedRoute>} />
      <Route path="/makeQuiz" element={<ProtectedRoute><ManualCreation /></ProtectedRoute>} />
      <Route path="/play/:code" element={<ProtectedRoute><PlayQuiz /></ProtectedRoute>} />
      <Route path="/join" element={<ProtectedRoute><JoinQuiz /></ProtectedRoute>} />

      {/* 404 fallback */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}