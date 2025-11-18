const API = "/api/notifications";

export async function getNotifications(token) {
  const res = await fetch(API, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.json();
}
