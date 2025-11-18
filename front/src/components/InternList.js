import React from "react";

export default function InternList({ interns }) {
  return (
    <ul>
      {interns.map(i => <li key={i.id}>{i.name}</li>)}
    </ul>
  );
}
