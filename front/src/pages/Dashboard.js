import React, { useEffect, useState } from "react";
import { getInterns } from "../api/interns";
import { getCourses } from "../api/courses";
import { getNotifications } from "../api/notifications";
import InternList from "../components/InternList";
import CourseList from "../components/CourseList";
import NotificationList from "../components/NotificationList";

export default function Dashboard({ token }) {
  const [interns, setInterns] = useState([]);
  const [courses, setCourses] = useState([]);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    getInterns(token).then(setInterns);
    getCourses(token).then(setCourses);
    getNotifications(token).then(setNotifications);
  }, [token]);

  return (
    <div>
      <h1>Intern Tracker Dashboard</h1>
      <h2>Interns</h2>
      <InternList interns={interns} />
      <h2>Courses</h2>
      <CourseList courses={courses} />
      <h2>Notifications</h2>
      <NotificationList notifications={notifications} />
    </div>
  );
}
