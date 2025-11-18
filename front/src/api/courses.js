const API = "/api/courses";

export async function getCourses(token) {
  const res = await fetch(API, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.json();
}
