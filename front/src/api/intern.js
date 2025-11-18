const API = "/api/interns";

export async function getInterns(token) {
  const res = await fetch(API, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.json();
}
