import React from "react";

export default function CourseList({ courses }) {
  return (
    <ul>
      {courses.map(c => <li key={c.id}>{c.title}</li>)}
    </ul>
  );
}
