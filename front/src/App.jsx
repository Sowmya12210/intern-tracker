import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Login from "./pages/Login.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import Interns from "./pages/Interns.jsx";
import Courses from "./pages/Courses.jsx";
import Notifications from "./pages/Notifications.jsx";

export default function App() {
  const [token, setToken] = useState(null);

  return (
    <Router>
      <Navbar token={token} setToken={setToken} />
      <Routes>
        <Route path="/" element={<Login setToken={setToken} />} />
        <Route path="/dashboard" element={<Dashboard token={token} />} />
        <Route path="/interns" element={<Interns token={token} />} />
        <Route path="/courses" element={<Courses token={token} />} />
        <Route path="/notifications" element={<Notifications token={token} />} />
      </Routes>
    </Router>
  );
}
