import React, { useEffect, useState } from "react";
import { getCourses } from "../api/courses";
import CourseList from "../components/CourseList";

export default function Courses({ token }) {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    getCourses(token).then(setCourses);
  }, [token]);

  return (
    <div>
      <h1>Courses</h1>
      <CourseList courses={courses} />
    </div>
  );
}
