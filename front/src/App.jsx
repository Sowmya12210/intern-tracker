import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Interns from "./pages/Interns";
import Courses from "./pages/Courses";
import Notifications from "./pages/Notifications";

export default function App() {
  const [token, setToken] = useState(null);

  return (
    <Router>
      <Navbar />
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
