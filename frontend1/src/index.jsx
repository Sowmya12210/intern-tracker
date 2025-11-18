// import React, { useState, useEffect } from "react";
// import ReactDOM from "react-dom/client";
// import "./styles.css";

// function App() {
//   const [token, setToken] = useState(null);
//   const [interns, setInterns] = useState([]);
//   const [courses, setCourses] = useState([]);
//   const [notifications, setNotifications] = useState([]);

//   // Dummy login for demo
//   function handleLogin(e) {
//     e.preventDefault();
//     setToken("demo-token");
//   }

//   // Load dummy data after login
//   useEffect(() => {
//     if (token) {
//       setInterns([{ id: 1, name: "Alice" }, { id: 2, name: "Bob" }]);
//       setCourses([{ id: 1, title: "Cloud Basics" }, { id: 2, title: "DevOps 101" }]);
//       setNotifications([{ id: 1, message: "Welcome interns!" }, { id: 2, message: "Course starts tomorrow" }]);
//     }
//   }, [token]);

//   if (!token) {
//     return (
//       <form onSubmit={handleLogin} className="login-form">
//         <h2>Login</h2>
//         <input placeholder="Username" />
//         <input type="password" placeholder="Password" />
//         <button type="submit">Login</button>
//       </form>
//     );
//   }

//   return (
//     <div className="dashboard">
//       <h1>Intern Tracker Dashboard</h1>

//       <h2>Interns</h2>
//       <ul>{interns.map(i => <li key={i.id}>{i.name}</li>)}</ul>

//       <h2>Courses</h2>
//       <ul>{courses.map(c => <li key={c.id}>{c.title}</li>)}</ul>

//       <h2>Notifications</h2>
//       <ul>{notifications.map(n => <li key={n.id}>{n.message}</li>)}</ul>
//     </div>
//   );
// }

// const root = ReactDOM.createRoot(document.getElementById("root"));
// root.render(<App />);

// 
import React, { useState, useEffect } from "react";
import ReactDOM from "react-dom/client";
import "./styles.css";

function App() {
  const [token, setToken] = useState(null);
  const [interns, setInterns] = useState([]);
  const [courses, setCourses] = useState([]);
  const [notifications, setNotifications] = useState([]);

  async function handleLogin(e) {
    e.preventDefault();
    const username = e.target.username.value;
    const password = e.target.password.value;

    const res = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    const data = await res.json();
    if (data.token) {
      setToken(data.token);
    } else {
      alert("Login failed");
    }
  }

  useEffect(() => {
    if (token) {
      fetch("/api/interns", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => res.json())
        .then(setInterns);

      fetch("/api/courses", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => res.json())
        .then(setCourses);

      fetch("/api/notifications", { headers: { Authorization: `Bearer ${token}` } })
        .then(res => res.json())
        .then(setNotifications);
    }
  }, [token]);

  if (!token) {
    return (
      <form onSubmit={handleLogin} className="login-form">
        <h2>Login</h2>
        <input name="username" placeholder="Username" />
        <input name="password" type="password" placeholder="Password" />
        <button type="submit">Login</button>
      </form>
    );
  }

  return (
    <div className="dashboard">
      <h1>Intern Tracker Dashboard</h1>

      <h2>Interns</h2>
      <ul>{interns.map(i => <li key={i.id}>{i.name}</li>)}</ul>

      <h2>Courses</h2>
      <ul>{courses.map(c => <li key={c.id}>{c.title}</li>)}</ul>

      <h2>Notifications</h2>
      <ul>{notifications.map(n => <li key={n.id}>{n.message}</li>)}</ul>
    </div>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);

