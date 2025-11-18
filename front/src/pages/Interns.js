import React, { useEffect, useState } from "react";
import { getInterns } from "../api/interns";
import InternList from "../components/InternList";

export default function Interns({ token }) {
  const [interns, setInterns] = useState([]);

  useEffect(() => {
    getInterns(token).then(setInterns);
  }, [token]);

  return (
    <div>
      <h1>Interns</h1>
      <InternList interns={interns} />
    </div>
  );
}
